package com.xss.domain.enums;

/**
 * 商品配送区域枚举
 * @描述： 
 * @作者：zzl
 * @版本：1.0
 * @创建时间：2016-9-21 下午3:15:18
 */
public enum ProductDelivery {
	all(0, "全国"), area(1, "指定地区"), station(2, "指定站点");
    
    private int value;
    private String desc;
    
    ProductDelivery(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
    public static ProductDelivery findByValue(int value) {
        ProductDelivery status = null;
        
        for (ProductDelivery rs : ProductDelivery.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }
        
        return status;
    }
    
}
