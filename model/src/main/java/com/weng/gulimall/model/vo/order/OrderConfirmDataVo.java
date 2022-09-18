package com.weng.gulimall.model.vo.order;

import com.weng.gulimall.model.user.UserAddress;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmDataVo {
    //购物车中需要结算的所有商品的信息
    private List<CartInfoVo> detailArrayList ;

    private Integer totalNum;

    private BigDecimal totalAmount ;

    private  List<UserAddress> userAddressList;

    //交易追踪号
    private String tradeNo;

}
