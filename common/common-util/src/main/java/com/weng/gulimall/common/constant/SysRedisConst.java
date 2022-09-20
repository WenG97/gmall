package com.weng.gulimall.common.constant;

/**
 * 系统常量
 */
public class SysRedisConst {
    public static final String NULL_VAL = "x";
    public static final String SKU_INFO_PREFIX = "sku:info:";
    public static final String LOCK_SKU_DETAIL = "lock:sku:detail:";
    public static final Long NULL_VAL_TTL = 60*30L;
    public static final Long SKUDETAIL_VAL_TTL = 60 * 60 * 24 * 7L;
    public static final String BLOOM_SKUID = "bloom:skuid";
    public static final String USER_LOGIN = "user:login";
    public static final String REQUEST_TOKEN = "token";
    public static final String USERID_HEADER = "userid";
    public static final String COOKIE_DOMAIN = ".gmall.com";
    public static final String USERTEMPID_HEADER = "usertempid";
    public static final String CART_KEY = "cart:user:";
    public static final Long CART_ITEMS_LIMIT = 200L;
    public static final Integer CART_ITEMS_NUM_LIMT = 200;
    public static final String ORDER_TRADE_TOKEN = "order:tradeNo:";
    public static final String MQ_RETRY = "mq:message:";
}
