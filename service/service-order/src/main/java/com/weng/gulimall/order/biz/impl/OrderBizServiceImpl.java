package com.weng.gulimall.order.biz.impl;

import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.feign.cart.CartFeignClient;
import com.weng.gulimall.feign.product.SkuProductFeignClient;
import com.weng.gulimall.feign.user.UserFeignClient;
import com.weng.gulimall.feign.ware.WareFeignClient;
import com.weng.gulimall.model.cart.CartInfo;
import com.weng.gulimall.model.vo.order.CartInfoVo;
import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;
import com.weng.gulimall.order.biz.OrderBizService;
import com.weng.gulimall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private SkuProductFeignClient skuProductFeignClient;
    @Autowired
    private WareFeignClient wareFeignClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 获取订单确认页需要的数据
     *
     * @return
     */
    @Override
    public OrderConfirmDataVo getConfirmData() {
        Result<List<CartInfo>> checked = cartFeignClient.getChecked();
        if (!checked.isOk() || checked.getData() == null) {
            throw new GmallException(ResultCodeEnum.CHECKED_ITEMS);
        }
        //获取购物车中选定的商品,所以不能代表购物车中最新的价格
        OrderConfirmDataVo orderConfirmDataVo = new OrderConfirmDataVo();

        List<CartInfo> data = checked.getData();
        List<CartInfoVo> infoVos = data.stream().map(o -> {
            CartInfoVo vo = new CartInfoVo();
            vo.setSkuId(o.getSkuId());
            vo.setImgUrl(o.getImgUrl());
            vo.setSkuName(o.getSkuName());
            vo.setSkuNum(o.getSkuNum());
            //http://localhost:14001/hasStock?skuId=10221&num=2
            // 查询数据库最新库存
            String hasStock = wareFeignClient.hasStock(o.getSkuId(), o.getSkuNum());
            vo.setHasStock(hasStock);
            // 查询数据库最新价格
            Result<BigDecimal> sku1010Price = skuProductFeignClient.getSku1010Price(o.getSkuId());
            vo.setOrderPrice(sku1010Price.getData());
            return vo;
        }).collect(Collectors.toList());
        orderConfirmDataVo.setDetailArrayList(infoVos);

        //查询商品总数量
        Integer totalNUms = infoVos.stream().map(CartInfoVo::getSkuNum)
                .reduce(Integer::sum).get();
        orderConfirmDataVo.setTotalNum(totalNUms);
        //设置每一个种类的商品的总金额
        BigDecimal totalAmount = infoVos.stream().map(c -> c.getOrderPrice().multiply(new BigDecimal(c.getSkuNum().toString())))
                .reduce(BigDecimal::add).get();
        orderConfirmDataVo.setTotalAmount(totalAmount);

        orderConfirmDataVo.setUserAddressList(userFeignClient.getUserAddressList().getData());
        //生成一个追踪号
        //5.1 从一开始追踪订单
        //5.2 对外交易号：和第三方交互时的订单号
        //5.3 防止订单重复提交
        String tradeNo = generateTradeNo();
        // String tradeNo = UUID.randomUUID().toString().replaceAll("-", "");
        orderConfirmDataVo.setTradeNo(tradeNo);
        return orderConfirmDataVo;

    }

    /**
     * 生成防重令牌，防止一个订单，多次提交
     *
     * @return String
     */
    @Override
    public String generateTradeNo() {

        long millis = System.currentTimeMillis();
        Long userId = AuthUtils.getCurrentAuthInfo().getUserId();
        String tradeNo = millis + "_" + userId;
        redisTemplate.opsForValue().set(SysRedisConst.ORDER_TRADE_TOKEN + tradeNo, "1", 15, TimeUnit.MINUTES);

        return tradeNo;
    }

    /**
     * 校验令牌，防重提交
     *
     * @return
     */
    @Override
    public boolean checkTradeNo(String tradeNo) {
        // String val = redisTemplate.opsForValue().get(tradeNo);
        //校验令牌，删除redis 中的数据时，可以加锁进行操作，不然还没有删除的时候
        //就有其他请求进来，造成空删，和多次下单
        //最优方案：lua脚本操作
        String lua = "if redis.call(\"get\",KEYS[1])==ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(lua, Long.class), Arrays.asList(SysRedisConst.ORDER_TRADE_TOKEN + tradeNo), new String[]{"1"});
        if (execute > 0) {
            //redis有这个令牌
            // redisTemplate.delete(SysRedisConst.ORDER_TRADE_TOKEN+tradeNo);
            return true;
        }
        return false;
    }

    /**
     * 提交订单
     *
     * @param tradeNo       tradeNo
     * @param orderSubmitVo orderSubmitVo
     * @return
     */
    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo) {
        //验证令牌，并防重复提交
        if (!checkTradeNo(tradeNo)) {
            throw new GmallException(ResultCodeEnum.TOKEN_INVALID);
        }

        for (CartInfoVo cartInfoVo : orderSubmitVo.getOrderDetailList()) {
            String hasStock = wareFeignClient.hasStock(cartInfoVo.getSkuId(), cartInfoVo.getSkuNum());
            //验证库存
            if (!"1".equals(hasStock)) {
                throw new GmallException(ResultCodeEnum.ORDER_NO_STOCK.getMessage() + cartInfoVo.getSkuName(), ResultCodeEnum.ORDER_NO_STOCK.getCode());
            }
            //查询价格
            BigDecimal price = skuProductFeignClient.getSku1010Price(cartInfoVo.getSkuId()).getData();
            // if (price.subtract(cartInfoVo.getOrderPrice()).doubleValue() < 0.001)
            if (!price.equals(cartInfoVo.getOrderPrice())) {
                throw new GmallException(ResultCodeEnum.ORDER_PRICE_CHANGED.getMessage() + cartInfoVo.getSkuName(), ResultCodeEnum.ORDER_PRICE_CHANGED.getCode());
            }
        }
        //保存数据库

        return orderInfoService.savaOrder(orderSubmitVo,tradeNo);
    }
}
