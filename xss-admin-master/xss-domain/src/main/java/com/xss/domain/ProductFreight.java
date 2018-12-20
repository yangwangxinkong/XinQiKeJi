/*
 *  
 *  
 *  
 */
package com.xss.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity - 商品运费
 * 
 * @author DannyZou
 * @version 1.0
 */
@Embeddable
public class ProductFreight implements Serializable {

	private static final long serialVersionUID = -673883300094536107L;

	/** 最小数量*/
	private Integer minWeight;
	/** 最大数量 */
	private Integer maxWeight;
	/** 运送单价 */
	private BigDecimal price;
	
	public Integer getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(Integer minWeight) {
		this.minWeight = minWeight;
	}
	
	public Integer getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(Integer maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}