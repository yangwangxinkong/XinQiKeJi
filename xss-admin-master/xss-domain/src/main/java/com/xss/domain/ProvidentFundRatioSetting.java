/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.PayFrom;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 公积金比例配置
 * 
 * @author hu
 * @version 1.0
 */
@Entity
@Table(name = "xx_provident_fund_ratio_setting")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_provident_fund_ratio_setting_sequence")
public class ProvidentFundRatioSetting extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "payFrom", "ratio"};

	private static final long serialVersionUID = -2158109459123036967L;

	/** 城市 */
	private City city;

	/** 缴费对象 */
	private PayFrom payFrom;

	/** 缴费比例 */
	private BigDecimal ratio;

	@ManyToOne(fetch = FetchType.LAZY)
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@NotNull
	public PayFrom getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(PayFrom payFrom) {
		this.payFrom = payFrom;
	}

	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
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
		ProvidentFundRatioSetting providentFundRatioSetting = ((ProvidentFundRatioSetting)entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);

		jo.put("createDate", DateUtil.format(providentFundRatioSetting.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		jo.put("payFromDesc", providentFundRatioSetting.getPayFrom().getDesc());
		//城市
		if (ArrayUtils.contains(params, "city")){
			if(null!=providentFundRatioSetting.getCity()){
				jo.put("city", JsonUtil.toJSONObject(providentFundRatioSetting.getCity(), City.DEFAULT_JSON_PARAMS));
			}else{
				jo.put("city", null);
			}
		}
        //缴费对象
		/*if (ArrayUtils.contains(params, "payFrom")){
			if(null!=providentFundRatioSetting.getPayFrom()){
				jo.put("payFrom", JsonUtil.toJSONObject(providentFundRatioSetting.getPayFrom(), new String[]{"value", "desc"}));
			}else{
				jo.put("payFrom", null);
			}
		}*/
		return jo;
	}
}