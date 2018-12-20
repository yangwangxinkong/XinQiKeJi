package com.xss.domain.enums;

public enum SmsResource {
    mobile(0, "手机"), web(1, "网站"), app(2, "app"), wx(3,"微信");
    
    private int value;
    private String desc;
    
    SmsResource(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
    public static SmsResource findByValue(int value) {
        SmsResource status = null;
        
        for (SmsResource rs : SmsResource.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }
        
        return status;
    }
    
}
