/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Entity - 赠品项
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_gift_item", uniqueConstraints = { @UniqueConstraint(columnNames = { "gift_id"}) })
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_gift_item_sequence")
public class GiftItem extends BaseEntity {

	private static final long serialVersionUID = 6593657730952481829L;

	/** 数量 */
	private Integer quantity;

	/** 赠品 */
	private Product gift;

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	@JsonProperty
	@NotNull
	@Min(1)
	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取赠品
	 * 
	 * @return 赠品
	 */
	@JsonProperty
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getGift() {
		return gift;
	}

	/**
	 * 设置赠品
	 * 
	 * @param gift
	 *            赠品
	 */
	public void setGift(Product gift) {
		this.gift = gift;
	}

	@Override
	public JSONArray convertList(List list, String[] params) {
		return super.convertList(list, params);
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params) {
		return super.convertEntity(entity, params);
	}
}