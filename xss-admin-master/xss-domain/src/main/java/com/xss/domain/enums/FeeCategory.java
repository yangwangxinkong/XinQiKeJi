package com.xss.domain.enums;

/**
 * 缴费类别
 */
public enum FeeCategory {

    fc0(0, "社保"),fc1(1, "公积金"),fc2(2, "社保+公积金");

    private int value;
    private String desc;

    FeeCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static FeeCategory findByValue(int value) {
        FeeCategory fc = null;

        for (FeeCategory rs : FeeCategory.values()) {
            if (rs.value == value) {
                fc = rs;
                break;
            }
        }

        return fc;
    }


}
