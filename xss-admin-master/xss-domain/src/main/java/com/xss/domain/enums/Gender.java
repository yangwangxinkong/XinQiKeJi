package com.xss.domain.enums;

/**
 * 商品类型枚举
 * @author zzl
 *
 */
public enum Gender {
    male(0, "男"), female(1, "女");

    private int value;
    private String desc;

    Gender(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
    public static Gender findByValue(int value) {
        Gender status = null;
        
        for (Gender rs : Gender.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }
        
        return status;
    }
    
}
