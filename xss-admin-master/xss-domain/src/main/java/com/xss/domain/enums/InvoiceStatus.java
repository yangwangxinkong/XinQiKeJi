package com.xss.domain.enums;

/**
 * 发票状态
 */
public enum InvoiceStatus {

    is0(0, "未开票"),is1(1, "已开票"),is2(2, "已邮寄");

    private int value;
    private String desc;

    InvoiceStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static InvoiceStatus findByValue(int value) {
        InvoiceStatus pf = null;

        for (InvoiceStatus rs : InvoiceStatus.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
