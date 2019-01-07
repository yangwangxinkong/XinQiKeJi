package com.xss.domain.enums;

/**
 * 分享信息类别
 */
public enum SharecCnfigEnums {

    s0(0, "促销活动"),s1(1, "文章详情"),s2(2, "邀请好友");

    private int value;
    private String desc;

    SharecCnfigEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SharecCnfigEnums findByValue(int value) {
        SharecCnfigEnums pf = null;

        for (SharecCnfigEnums rs : SharecCnfigEnums.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
