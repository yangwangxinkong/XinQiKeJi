package com.xss.domain.enums;

/**
 * 社保类别
 */
public enum SocialSecurityCategory {

    ssc0(0, "养老"),ssc1(1, "失业"),ssc2(2, "工伤"),ssc3(3, "医疗"),ssc4(4, "生育"),ssc5(5, "残保");

    private int value;
    private String desc;

    SocialSecurityCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SocialSecurityCategory findByValue(int value) {
        SocialSecurityCategory ssc = null;

        for (SocialSecurityCategory rs : SocialSecurityCategory.values()) {
            if (rs.value == value) {
                ssc = rs;
                break;
            }
        }

        return ssc;
    }


}
