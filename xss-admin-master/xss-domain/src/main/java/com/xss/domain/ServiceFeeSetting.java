/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayCategory;
import com.xss.domain.enums.PayFrom;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 服务费配置
 * 
 * @author hu
 * @version 1.0
 */
@Entity
@Table(name = "xx_service_fee_setting")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_service_fee_setting_sequence")
public class ServiceFeeSetting extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "feeCategory", "payCategory","monthCount", "monthFee", "fee"};
	//public static final String[] SPECIAL_JSON_PARAMS = new String[]{"city", "payFrom", "feeCategory"};

	private static final long serialVersionUID = -2158109459123036967L;

	/** 城市 */
	private City city;

//	/** 缴费对象 */
//	private PayFrom payFrom;

	/** 缴费类别 */
	private FeeCategory feeCategory;

	/**
	 * 缴费方式
	 */
	private PayCategory payCategory;

	/** 缴费月数 */
	private Integer monthCount;

	/** 服务费 */
	private BigDecimal fee;

	/** 每月服务费 */
	private BigDecimal monthFee;

	@ManyToOne(fetch = FetchType.LAZY)
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

//	@NotNull
//	public PayFrom getPayFrom() {
//		return payFrom;
//	}
//
//	public void setPayFrom(PayFrom payFrom) {
//		this.payFrom = payFrom;
//	}

	@NotNull
	public FeeCategory getFeeCategory() {
		return feeCategory;
	}

	public void setFeeCategory(FeeCategory feeCategory) {
		this.feeCategory = feeCategory;
	}

	@NotNull
	public PayCategory getPayCategory() {
		return payCategory;
	}

	public void setPayCategory(PayCategory payCategory) {
		this.payCategory = payCategory;
	}

	public Integer getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
	}

	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(BigDecimal monthFee) {
		this.monthFee = monthFee;
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
		ServiceFeeSetting serviceFeeSetting = ((ServiceFeeSetting)entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);

		jo.put("createDate", DateUtil.format(serviceFeeSetting.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		//jo.put("payFromDesc", serviceFeeSetting.getPayFrom().getDesc());
		jo.put("feeCategoryDesc", serviceFeeSetting.getFeeCategory().getDesc());
		jo.put("payCategoryDesc", serviceFeeSetting.getPayCategory().getDesc());
		// 城市
		if (ArrayUtils.contains(params, "city")){
			if(null!=serviceFeeSetting.getCity()){
				jo.put("city", JsonUtil.toJSONObject(serviceFeeSetting.getCity(), City.DEFAULT_JSON_PARAMS));
			}else{
				jo.put("city", null);
			}
		}

//		// 户口性质
//		if (ArrayUtils.contains(params, "payFrom")){
//			if(null!=serviceFeeSetting.getPayFrom()){
//				jo.put("payFrom", JsonUtil.toJSONObject(serviceFeeSetting.getPayFrom(), new String[]{"value", "desc"}));
//			}else{
//				jo.put("payFrom", null);
//			}
//		}
//
//		// 缴费类别
//		if (ArrayUtils.contains(params, "feeCategory")){
//			if(null!=serviceFeeSetting.getFeeCategory()){
//				jo.put("feeCategory", JsonUtil.toJSONObject(serviceFeeSetting.getFeeCategory(), new String[]{"value", "desc"}));
//			}else{
//				jo.put("feeCategory", null);
//			}
//		}

		return jo;
	}

}