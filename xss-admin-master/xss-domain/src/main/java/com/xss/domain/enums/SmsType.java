package com.xss.domain.enums;

public enum SmsType {
	default_value(0, ""),register(1, "注册"), 
	updatePwd(2, "修改密码"), updateMobile(3, "修改手机号码"),
	validMobile(4, "安全验证"), login(5, "登陆"), validMail(6, "验证邮箱"),
	findPwd(7, "找回密码"),hintOrder(8,"订单提示"),successOrder(9,"订单成功"),cancleOrder(10,"订单取消");
    
    private int value;
    private String desc;
    
    SmsType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
    public static SmsType findByValue(int value) {
        SmsType status = null;
        
        for (SmsType rs : SmsType.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }
        
        return status;
    }
    
}
