package com.xss.base;

/**
 * @author zzl
 * @since 2018-05-03
 */
public enum PublicResultConstant {

    /**
     * 异常
     */
    FAILED("90000001", "系统错误"),
    /**
     * 成功
     */
    SUCCESS("00000000", "success"),
    /**
     * 未登录/token过期
     */
    UNAUTHORIZED("90000002", "获取登录用户信息失败"),
    /**
     * 失败
     */
    ERROR("90000000", "操作失败"),
    /**
     * 失败
     */
    PARAM_ERROR("90000003", "参数错误"),

    /**
     * 用户名或密码错误
     */
    INVALID_USERNAME_PASSWORD("10000003", "用户名或密码错误"),
    /**
     *
     */
    INVALID_RE_PASSWORD("10000010", "两次输入密码不一致"),
    /**
    /**
     * 用户名或密码错误
     */
    INVALID_PASSWORD("10000009", "旧密码错误"),
    /**
     * 用户名重复
     */
    USERNAME_ALREADY_IN("10000002", "用户已存在"),

    /**
     * 密码长度为8至20位
     */
    INVALID_BU_PASSWORD("10086","密码长度为8至20位"),
    /**
     * 用户不存在
     */
    INVALID_USER("10000001", "用户不存在"),
    /**
     * 角色不存在
     */
    INVALID_ROLE("10000004", "角色不存在"),

    /**
     * 角色不存在
     */
    ROLE_USER_USED("10000008", "角色使用中，不可删除"),

    /**
     * 参数错误-已存在
     */
    INVALID_PARAM_EXIST("10000005", "请求参数已存在"),
    /**
     * 参数错误
     */
    INVALID_PARAM_EMPTY("10000006", "请求参数为空"),
    /**
     * 没有权限
     */
    USER_NO_PERMITION("10000007", "当前用户无该接口权限"),
    /**
     * 校验码错误
     */
    VERIFY_PARAM_ERROR("10000011", "校验码错误"),
    /*
     * 校验码过期
     */
    VERIFY_PARAM_PASS("10000012", "校验码过期"),

    /**
     * 用户没有添加、删除评论或回复的权限
     */
    USER_NO_AUTHORITY("10000013","该用户没有权限"),

    /**
     * 用户没有添加、删除评论或回复的权限
     */
    MOBILE_ERROR("10000014","手机号格式错误") ,

    /**
     * 订单不存在
     */
    ORDER_NULL_ERROR("10000015","订单不存在") ,

    /**
     * 订单
     */
    ORDER_STATUS_ERROR("10000016","订单状态不正确") ,

    /**
     * 数据更新或增加失败
     */
    DATA_ERROR("10000017","数据操作错误"),


    UN_SUPPORTED_EMAIL("10000018","该账号不支持E-mail登录"),

    ACCOUNT_DISABLED("10000019", "账号已被禁用"),

    ACCOUNT_LOCKED("10000020", "账号已被锁定"),

    ACCOUNT_LOCKED_COUNT("10000021", "密码错误，若连续%s次密码错误账号将被锁定"),

    COMPANY_ACCOUNT_ERR("10000022","请使用正确的企业账号登录"),

    COMPANY_ACCOUNT_STATUS_ERR("10000023","您的企业还未通过审核，暂时无法登陆"),

    PRODUCT_NOT_EXIST_ERROR("10000025","商品不存在"),
    PRODUCT_NOT_MARKETABLE_ERROR("10000026","商品已下架"),
    PRODUCT_PRODUCT_OUT_OF_STOCK_ERROR("10000027","商品缺货"),

    PRODUCT_FAVORITED_ERROR("10000029","商品已收藏"),
    PRODUCT_FAVORITED_OVER_ERROR("10000030","商品已收藏"),

    PRODUCT_QUANTITY_NULL_ERROR("10000031","请 选择商品数量"),
    PRODUCT_IS_GIFT_ERROR("10000032","该商品不是销售商品"),
    MAX_PRODUCT_COUNT_ERROR("10000033","你的购物车的商品太多了"),
    AVAILABLE_STOCK_ERROR("10000034","该商品库存不足"),
    CART_ITEM_NOT_EXSIT_ERROR("10000035","订单商品为空"),
    CART_HAS_CHANGED_ERROR("10000036","购物车商品已修改，请检查购物车"),
    CART_LOW_STOCK_ERROR("10000037","购物车商品库存不足，请修改购物车商品"),
    RECEIVER_NOT_EXSIT_ERROR("10000037","收货地址不存在"),
    PAYMENT_METHOD_NOT_EXSIT_ERROR("10000037","支付方式不存在"),
    SHIPPING_METHOD_NOT_EXSIT_ERROR("10000037","配送方式不存在"),
    PARTISIAL_RECEIVER_NOT_EXSIT_ERROR("10000038","您的订单中有商品不能运送到您的收货地址,请重新选择或修改地址"),
    MAX_RECEIVER_COUNT_ERROR("10000039","您的收货地址超过最大限制"),
    RECEIVER_NULL_ERROR("10000040","收货地址不存在"),
    RECEIVER_MEMBER_ERROR("10000041","收货地址所有者不匹配"),
    ORDER_MEMBER_MATCH_ERROR("10000042","订单不属于该用户"),
    ORDER_LOCKED_ERROR("10000043","该订单已被锁定，请稍后再试！"),
    ORDER_IS_EXSIT_ERROR("10000044","该订单已经存在！"),
    QUOTATION_IS_EXSIT_ERROR("10000045","该月已经缴纳，去订单里查看"),
    MEMBER_QUOTATION_IS_NOT_NULL_ERROR("10000046","该用户下有报价单或订单存在，不能删除！"),


    /**
     * 订单锁定
     */
    ORDER_LOCK_ERROR("10000024","订单被锁定"),
    SMS_CODE_ERROR("10000028","短信验证码错误");


    public String result;
    public String msg;

    PublicResultConstant(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
