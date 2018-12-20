package com.xss.domain.enums;

/**
 * 计算方式
 */
public enum CalculatorCategory {

    cc0(0, "计算器"),cc1(1, "计算报价单"),cc2(2, "计算报价单+明细");

    private int value;
    private String desc;

    CalculatorCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static CalculatorCategory findByValue(int value) {
        CalculatorCategory fc = null;

        for (CalculatorCategory rs : CalculatorCategory.values()) {
            if (rs.value == value) {
                fc = rs;
                break;
            }
        }

        return fc;
    }


}
