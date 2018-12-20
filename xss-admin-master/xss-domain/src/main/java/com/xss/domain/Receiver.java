/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Entity - 收货地址
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_receiver")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_receiver_sequence")
public class Receiver extends BaseEntity {

	private static final long serialVersionUID = 2673602067029665976L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "consignee", "areaName", "address", "zipCode", "phone", "isDefault"};

	/** 收货地址最大保存数 */
	public static final Integer MAX_RECEIVER_COUNT = 8;

	/** 收货人 */
	private String consignee;

	/** 地区名称 */
	private String areaName;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

	/** 是否默认 */
	private Boolean isDefault;

	/** 地区 */
	private Area area;

	/** 会员 */
	private Member member;

	/**
	 * 获取收货人
	 * 
	 * @return 收货人
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * 
	 * @param consignee
	 *            收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区名称
	 * 
	 * @return 地区名称
	 */
	@JsonProperty
	@Column(nullable = false)
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * 
	 * @param areaName
	 *            地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address
	 *            地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * 
	 * @param zipCode
	 *            邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * 
	 * @return 电话
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * 
	 * @param phone
	 *            电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取是否默认
	 * 
	 * @return 是否默认
	 */
	@JsonProperty
	@NotNull
	@Column(nullable = false)
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * 
	 * @param isDefault
	 *            是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取地区
	 * 
	 * @return 地区
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}


	@Override
	public JSONArray convertList(List list, String[] params){
		JSONArray array = new JSONArray();
		if (null != list && !list.isEmpty()){
			for (Object entity : list){
				JSONObject jo = convertEntity(entity, params);
				array.add(jo);
			}
		}
		return array;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Receiver receiver = (Receiver)entity;
		JSONObject Jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);

		if (ArrayUtils.contains(params, "treePath")) {
			Area area = receiver.getArea();
			if (area != null) {
				Jo.put("treePath", area.getTreePath());
				Jo.put("areaId", area.getId());
			}
		}

		if (ArrayUtils.contains(params, "area")){
			Area area = receiver.getArea();
			if(area != null){

				JSONObject receiverJo = new JSONObject();

				String areaPath = area.getTreePath();
				String[] ids = areaPath.split(",");
				if (ids.length == 1){
					receiverJo.put("provinceValue", area.getId());
					receiverJo.put("provinceName", area.getName());
				}else if (ids.length == 2){
					receiverJo.put("provinceValue", ids[1]);
					receiverJo.put("cityValue", area.getId());
					receiverJo.put("provinceName", area.getParent().getName());
					receiverJo.put("cityName", area.getName());
				}else if (ids.length == 3){
					receiverJo.put("provinceValue", ids[1]);
					receiverJo.put("cityValue", ids[2]);
					receiverJo.put("districtValue", area.getId());
					receiverJo.put("provinceName", area.getParent().getParent().getName());
					receiverJo.put("cityName", area.getParent().getName());
					receiverJo.put("districtName", area.getName());
				}

				Jo.put("area", receiverJo);
			}
		}

		return Jo;
	}

}