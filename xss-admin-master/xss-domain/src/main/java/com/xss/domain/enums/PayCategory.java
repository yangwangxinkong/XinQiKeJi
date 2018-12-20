package com.xss.domain.enums;

/**
 * 缴费方式
 */
public enum PayCategory {

    pc0(0, "新参"),pc1(1, "续缴"),pc2(2, "补缴");

    private int value;
    private String desc;

    PayCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PayCategory findByValue(int value) {
        PayCategory fc = null;

        for (PayCategory rs : PayCategory.values()) {
            if (rs.value == value) {
                fc = rs;
                break;
            }
        }

        return fc;
    }


}
