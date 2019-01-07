package com.xss.domain.enums;

/**
 * 分享类别
 */
public enum ShareCategory {

    s0(0, "链接分享"),s1(1, "二维码分享");

    private int value;
    private String desc;

    ShareCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ShareCategory findByValue(int value) {
        ShareCategory pf = null;

        for (ShareCategory rs : ShareCategory.values()) {
            if (rs.value == value) {
                pf = rs;
                break;
            }
        }

        return pf;
    }


}
