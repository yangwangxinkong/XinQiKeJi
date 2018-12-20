/*
 *  
 *  
 *  
 */
package com.xss.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Entity - 商品配送地区
 * 
 * @author DannyZou
 * @version 1.0
 */
@Embeddable
public class ProductArea implements Serializable {

	private static final long serialVersionUID = -673883300094536107L;

	private Long areaId;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
}