package com.xss.domain.enums;

/**
 * 开具类型
 */
public enum InvoiceType {

    it0(0, "企业单位"),it1(1, "个人");

    private int value;
    private String desc;

    InvoiceType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static InvoiceType findByValue(int value) {
        InvoiceType pf = null;

        for (InvoiceType rs : InvoiceType.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
