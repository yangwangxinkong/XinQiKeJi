/*
 *  
 *  
 *  
 */
package com.xss.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Entity - 商品配送站点
 * 
 * @author DannyZou
 * @version 1.0
 */
@Embeddable
public class ProductStation implements Serializable {

	private static final long serialVersionUID = -673883300094536107L;

	private Long stationId;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
}