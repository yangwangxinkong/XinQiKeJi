package com.xss.util;

import java.math.BigDecimal;

/**
 * @author zzl
 * @date 2018-08-13
 */
public class CurrencyMethod {

    /** 价格精确位数 */
    private static final Integer priceScale = 2;
    /** 货币符号 */
    private static final String currencySign = "￥";
    /** 货币单位 */
    private static final String currencyUnit = "元";

    /**
     * 小数位精确方式
     */
    public enum RoundType {

        /** 四舍五入 */
        roundHalfUp,

        /** 向上取整 */
        roundUp,

        /** 向下取整 */
        roundDown
    }

    public static String currency(BigDecimal amount){
        return currency(amount, false, false);
    }
    public static String currency(BigDecimal amount, boolean showSign){
        return currency(amount, showSign, false);
    }
    public static String currency(BigDecimal amount, boolean showSign, Boolean showUnit){
        if (null == amount){
            return null;
        }
        String price = setScale(amount).toString();
        if (showSign) {
            price = currencySign + price;
        }
        if (showUnit) {
            price += currencyUnit;
        }
        return price;
    }
    private static BigDecimal setScale(BigDecimal amount){
        return setScale(amount, RoundType.roundHalfUp, priceScale);
    }

   /**
    * 设置精度
    *
    * @param amount
    *            数值
    * @return 数值
    */
    private static BigDecimal setScale(BigDecimal amount, RoundType roundType, Integer priceScale) {
        if (amount == null) {
            return null;
        }
        int roundingMode;
        if (roundType == RoundType.roundUp) {
            roundingMode = BigDecimal.ROUND_UP;
        } else if (roundType == RoundType.roundDown) {
            roundingMode = BigDecimal.ROUND_DOWN;
        } else {
            roundingMode = BigDecimal.ROUND_HALF_UP;
        }
        return amount.setScale(priceScale, roundingMode);
    }
}
