package com.xss.domain.enums;

/**
 * 发票类型
 */
public enum InvoiceCategory {

    it0(0, "增值税普通发票"),it1(1, "增值税专票");

    private int value;
    private String desc;

    InvoiceCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static InvoiceCategory findByValue(int value) {
        InvoiceCategory pf = null;

        for (InvoiceCategory rs : InvoiceCategory.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
