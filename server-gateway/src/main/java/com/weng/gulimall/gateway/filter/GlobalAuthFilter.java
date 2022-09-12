package com.weng.gulimall.gateway.filter;


import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.gateway.properties.AuthUrlProperties;
import com.weng.gulimall.model.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class GlobalAuthFilter implements GlobalFilter {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private AuthUrlProperties authUrlProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 路径过滤
     *
     * @param exchange exchange
     * @param chain    chain
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        //1、前置拦截
        String path = exchange.getRequest().getURI().getPath();

        //2、验证无须登录就就能访问的静态资源
        //静态资源就算带了token 也不需要做id透传
        for (String noLoginUrl : authUrlProperties.getNoAuthUrl()) {
            if (antPathMatcher.match(noLoginUrl, path)) {
                return chain.filter(exchange);
            }
        }

        //3、禁止外部访问内部的接口
        for (String denyUrl : authUrlProperties.getDenyUrl()) {
            if (antPathMatcher.match(denyUrl, path)) {
                //直接响应 拒绝访问的json
                Result<Object> result = Result.build(null, ResultCodeEnum.PERMISSION);
                return responseResult(result, exchange);
            }
        }

        //4、验证需要登录才能访问的资源
        for (String loginUrl : authUrlProperties.getLoginAuthUrl()) {
            if (antPathMatcher.match(loginUrl, path)) {
                //需要登录，进行token验证
                String tokenValue = getTokenValue(exchange);
                if (!StringUtils.isEmpty(tokenValue)) {
                    UserInfo userInfo = getTokenUserInfo(tokenValue);
                    if (!ObjectUtils.isEmpty(userInfo)) {
                        //redis中获取到了此用户的信息,表明用户已经登录，
                        //将用户信息放在请求头中
                        ServerWebExchange serverWebExchange = userIdOrTempIdDelivery(userInfo, exchange);
                        //放行
                        return chain.filter(serverWebExchange);
                    }
                }
                //redis中没有获取到用户信息，没有token，或者token为假
                //返回到登录页面，同时带上点击登录之前的地址
                URI originURL = exchange.getRequest().getURI();
                return redirectToCustomPage(authUrlProperties.getLoginPage() + "?originUrl=" + originURL, exchange);

                // //验证token
                // UserInfo userInfo = getTokenUserInfo(tokenValue);
                // if (!ObjectUtils.isEmpty(userInfo)) {
                //     //redis中获取到了此用户的信息,表明用户已经登录，
                //     //将用户信息放在请求头中 进行透传
                //     ServerWebExchange serverWebExchange = userIdDelivery(userInfo, exchange);
                //     //放行
                //     return chain.filter(serverWebExchange);
                // } else {
                //     }
            }
        }

        //5、普通请求，进行id透传
        //5.1 判断是否携带token
        String tokenValue = getTokenValue(exchange);
        if (!StringUtils.isEmpty(tokenValue)) {
            //携带了token ，验证token。尝试进行id透传
            UserInfo tokenUserInfo = getTokenUserInfo(tokenValue);
            if (!ObjectUtils.isEmpty(tokenUserInfo)) {
                //通过当前token从redis中获取到用户信息，
                //可以进行id透传
                ServerWebExchange serverWebExchange = userIdOrTempIdDelivery(tokenUserInfo, exchange);
                return chain.filter(serverWebExchange);
            } else {
                //说明携带了token，但是token信息不对,代表是假令牌
                //重定向到登录页
                URI originURL = exchange.getRequest().getURI();
                return redirectToCustomPage(authUrlProperties.getLoginPage() + "?originUrl=" + originURL, exchange);
            }
        }
        //未携带token不进行用户id透传， 只透传临时id
        ServerWebExchange serverWebExchange = userIdOrTempIdDelivery(null, exchange);
        return chain.filter(serverWebExchange);
    }

    /**
     * 响应自定义的数据回去
     *
     * @param result   result
     * @param exchange exchange
     * @return Mono<Void>
     */
    private Mono<Void> responseResult(Result<Object> result, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        String json = Jsons.toStr(result);
        DataBuffer dataBuffer = response.bufferFactory().wrap(json.getBytes());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 将用户信息 和 临时id 进行透传
     *
     * @param userInfo userInfo
     * @param exchange exchange
     * @return ServerWebExchange
     */
    private ServerWebExchange userIdOrTempIdDelivery(UserInfo userInfo, ServerWebExchange exchange) {
        // ServerHttpRequest request = exchange.getRequest();
        String userTempId = getUserTempId(exchange);
        String userId = null ;
        if (!ObjectUtils.isEmpty(userInfo)){
            userId = userInfo.getId().toString();
        }
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .header(SysRedisConst.USERID_HEADER, userId)
                .header(SysRedisConst.USERTEMPID_HEADER, userTempId)
                .build();
        // return exchange;
        return exchange.mutate().request(newRequest).build();
    }

    /**
     * 获取临时id
     * @param exchange exchange
     * @return String
     */
    private String getUserTempId(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String userTempId = request.getHeaders().getFirst("userTempId");
        if (StringUtils.isEmpty(userTempId)) {
            HttpCookie userTempIdCookie = request.getCookies().getFirst("userTempId");
            if (!ObjectUtils.isEmpty(userTempIdCookie)){
                userTempId = userTempIdCookie.getValue();
            }
        }
        return userTempId;
    }

    /**
     * 重定向要指定位置
     *
     * @param path     path
     * @param exchange exchange
     * @return Mono<Void>
     */
    private Mono<Void> redirectToCustomPage(String path, ServerWebExchange exchange) {
        //1、重定向【302状态码 + 响应头中 Location：新位置】
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().add(HttpHeaders.LOCATION, path);

        //2、清除旧的token
        ResponseCookie cookie = ResponseCookie.from(SysRedisConst.REQUEST_TOKEN, null)
                .maxAge(0)
                .domain(SysRedisConst.COOKIE_DOMAIN)
                .path("/")
                .build();
        // System.out.println("cookie.getPath() = " + cookie.getPath());
        response.getCookies().add(SysRedisConst.REQUEST_TOKEN, cookie);
        //3、响应结束
        return response.setComplete();
    }

    /**
     * 根据token的值，从redis中获取用户真正的信息
     *
     * @param tokenValue tokenValue
     * @return UserInfo
     */
    private UserInfo getTokenUserInfo(String tokenValue) {
        String json = redisTemplate.opsForValue().get(SysRedisConst.USER_LOGIN + tokenValue);
        if (!StringUtils.isEmpty(json)) {
            return Jsons.toObj(json, UserInfo.class);
        }
        return null;
    }

    /**
     * 从请求头或者cookie中获取token
     *
     * @param exchange exchange
     * @return String
     */
    private String getTokenValue(ServerWebExchange exchange) {
        //判断cookie中有无token
        HttpCookie token = exchange.getRequest().getCookies().getFirst(SysRedisConst.REQUEST_TOKEN);
        if (!ObjectUtils.isEmpty(token)) {
            return token.getValue();
        }
        //如果cookie中没有，从请求头中获取

        return exchange.getRequest().getHeaders().getFirst(SysRedisConst.REQUEST_TOKEN);
    }

    // public static void main(String[] args) {
    //
    //     Mono<Integer> in = null;
    //     try {
    //         in = getIn();
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    //     Flux<Integer> flux = in.flux();
    //     flux.subscribe(System.out::println);
    //     flux.
    // }
    // static Mono<Integer> getIn() throws InterruptedException {
    //     TimeUnit.SECONDS.sleep(2);
    //     return Mono.just(5);
    // }
}
