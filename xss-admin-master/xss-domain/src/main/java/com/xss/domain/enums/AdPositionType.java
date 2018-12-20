package com.xss.domain.enums;

/**
 * 广告位类型
 */
public enum AdPositionType {

    wxIndexBanner(0, "微信首页banner"),
    webIndexBanner(1, "web首页banner"),
    wxConsultingBanner(2, "微信促销活动广告位"),
    webSocialBanner(3, "web缴社保页面banner"),
    wxProvidentBanner(4, "web缴公积金页面banner");

    private int value;
    private String desc;

    AdPositionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AdPositionType findByValue(int value) {
        AdPositionType status = null;

        for (AdPositionType rs : AdPositionType.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }

        return status;
    }


}
