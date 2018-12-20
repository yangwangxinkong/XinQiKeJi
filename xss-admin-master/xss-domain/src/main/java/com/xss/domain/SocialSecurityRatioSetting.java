/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.PayFrom;
import com.xss.domain.enums.SocialSecurityCategory;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 社保比例配置
 * 
 * @author hu
 * @version 1.0
 */
@Entity
@Table(name = "xx_social_security_ratio_setting")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_social_security_ratio_setting_sequence")
public class SocialSecurityRatioSetting extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "ratio", "socialSecurityCategory", "payFrom", "fixed", "fixedValue"};

	private static final long serialVersionUID = -2158109459123036967L;

	/** 城市 */
	private City city;

	/** 户口性质 */
	private ResidenceType residenceType;

	/** 社保类别 */
	private SocialSecurityCategory socialSecurityCategory;

	/** 缴费对象 */
	private PayFrom payFrom;

	/** 缴费比例 */
	private BigDecimal ratio;

	/** 残保金是否固定值 */
	private Boolean fixed;

	/** 固定值 */
	private BigDecimal fixedValue;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public ResidenceType getResidenceType() {
		return residenceType;
	}

	public void setResidenceType(ResidenceType residenceType) {
		this.residenceType = residenceType;
	}

	@NotNull
	public SocialSecurityCategory getSocialSecurityCategory() {
		return socialSecurityCategory;
	}

	public void setSocialSecurityCategory(SocialSecurityCategory socialSecurityCategory) {
		this.socialSecurityCategory = socialSecurityCategory;
	}

	@NotNull
	public PayFrom getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(PayFrom payFrom) {
		this.payFrom = payFrom;
	}

	@Column(nullable = true, precision = 21, scale = 3)
	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	@Column(nullable = true, precision = 21, scale = 2)
	public BigDecimal getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(BigDecimal fixedValue) {
		this.fixedValue = fixedValue;
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
		SocialSecurityRatioSetting socialSecurityRatioSetting = ((SocialSecurityRatioSetting)entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);

		jo.put("createDate", DateUtil.format(socialSecurityRatioSetting.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		jo.put("payFromDesc", socialSecurityRatioSetting.getPayFrom().getDesc());
		jo.put("socialSecurityCategoryDesc", socialSecurityRatioSetting.getSocialSecurityCategory().getDesc());
		jo.put("residenceTypeName", socialSecurityRatioSetting.getResidenceType().getName());

		// 城市
		if (ArrayUtils.contains(params, "city")){
			if(null!=socialSecurityRatioSetting.getCity()){
				jo.put("city", JsonUtil.toJSONObject(socialSecurityRatioSetting.getCity(), City.DEFAULT_JSON_PARAMS));
			}else{
				jo.put("city", null);
			}
		}

		// 户口性质
		if (ArrayUtils.contains(params, "residenceType")){
			if(null!=socialSecurityRatioSetting.getResidenceType()){
				jo.put("residenceType", JsonUtil.toJSONObject(socialSecurityRatioSetting.getResidenceType(), new String[]{"id","name"}));
			}else{
				jo.put("residenceType", null);
			}
		}

		/*// 户口性质
		if (ArrayUtils.contains(params, "payFrom")){
			if(null!=socialSecurityRatioSetting.getPayFrom()){
				jo.put("payFrom", JsonUtil.toJSONObject(socialSecurityRatioSetting.getPayFrom(), new String[]{"value", "desc"}));
			}else{
				jo.put("payFrom", null);
			}
		}

		// 社保类别
		if (ArrayUtils.contains(params, "socialSecurityCategory")){
			if(null!=socialSecurityRatioSetting.getSocialSecurityCategory()){
				jo.put("socialSecurityCategory", JsonUtil.toJSONObject(socialSecurityRatioSetting.getSocialSecurityCategory(), new String[]{"value", "desc"}));
			}else{
				jo.put("socialSecurityCategory", null);
			}
		}

		// 缴费对象
		if (ArrayUtils.contains(params, "accountProperty")){
			if(null!=socialSecurityRatioSetting.getAccountProperty()){
				jo.put("accountProperty", JsonUtil.toJSONObject(socialSecurityRatioSetting.getAccountProperty(), new String[]{"value", "desc"}));
			}else{
				jo.put("accountProperty", null);
			}
		}*/

		return jo;
	}
}