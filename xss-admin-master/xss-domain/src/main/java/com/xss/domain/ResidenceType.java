/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 户口类型
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_residence_type")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_residence_type_sequence")
public class ResidenceType extends OrderEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","name", "order"};

	/** 户口类型名称 */
	private String name;

	/**
	 * 城市
	 */
	private City city;

	/** 报价单 */
	private Set<Quotation> quotations = new HashSet<Quotation>();

	/** 社保比例配置 */
	private Set<SocialSecurityRatioSetting> socialSecurityRatioSettings = new HashSet<SocialSecurityRatioSetting>();

	/**
	 * 获取户口类型名称
	 * 
	 * @return 户口类型名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * 设置户口类型名称
	 * 
	 * @param name
	 *            户口类型名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 城市
	 * @return
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public City getCity() {
		return city;
	}

	/**
	 * 设置 城市
	 * @param city
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * 获取报价单
	 *
	 * @return 报价单
	 */
	@OneToMany(mappedBy = "residenceType", fetch = FetchType.LAZY)
	public Set<Quotation> getQuotations() {
		return quotations;
	}

	/**
	 * 设置 报价单
	 * @param quotations
	 */
	public void setQuotations(Set<Quotation> quotations) {
		this.quotations = quotations;
	}

	/**
	 * 获取社保比例配置
	 *
	 * @return 社保比例配置
	 */
	@OneToMany(mappedBy = "residenceType", fetch = FetchType.LAZY)
	public Set<SocialSecurityRatioSetting> getSocialSecurityRatioSettings() {
		return socialSecurityRatioSettings;
	}

	/**
	 * 设置 社保比例配置
	 * @param socialSecurityRatioSettings
	 */
	public void setSocialSecurityRatioSettings(Set<SocialSecurityRatioSetting> socialSecurityRatioSettings) {
		this.socialSecurityRatioSettings = socialSecurityRatioSettings;
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
		ResidenceType residenceType =((ResidenceType) entity);
		JSONObject jo = super.convertEntity(residenceType,DEFAULT_JSON_PARAMS);

		if(ArrayUtils.contains(params, "city")){
			JSONObject pmJo = JsonUtil.toJSONObject(residenceType.getCity(), new String[]{"id", "name", "code", "fullName"});
			jo.put("city", pmJo);
		}

		return jo;
	}

}