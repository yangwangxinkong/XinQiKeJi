package com.xss.domain.enums;

/**
 * 商品类型枚举
 * @author zzl
 *
 */
public enum ProductType {
	standard(0, "标准商品"), service(1, "兑换商品");
    
    private int value;
    private String desc;
    
    ProductType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    
    public static ProductType findByValue(int value) {
        ProductType status = null;
        
        for (ProductType rs : ProductType.values()) {
            if (rs.value == value) {
                status = rs;
                break;
            }
        }
        
        return status;
    }
    
}
