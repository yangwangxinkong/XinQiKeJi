package com.xss.domain.enums;

/**
 * 缴费对象
 */
public enum PayFrom {

    pf0(0, "个人"),pf1(1, "公司");

    private int value;
    private String desc;

    PayFrom(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PayFrom findByValue(int value) {
        PayFrom pf = null;

        for (PayFrom rs : PayFrom.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
