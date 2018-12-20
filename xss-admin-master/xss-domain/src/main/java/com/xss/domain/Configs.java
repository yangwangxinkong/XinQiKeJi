/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.util.DateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity - 广告
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_configs")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_configs_sequence")
public class Configs extends OrderEntity {

	private static final long serialVersionUID = -1307743303786909390L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","code","codeValue", "memo", "isSystem"};

	public static final String DEFAULT_NOTICE_CODE = "notice_code";  //最新公告文章分类内置code
	public static final String DEFAULT_SERVICE_CALL_CODE = "service_call_code"; //系统服务电话code
	public static final String DEFAULT_WX_SLOGAN_CODE = "wx_slogan"; //微信首页口号
	public static final String DEFAULT_WEB_SLOGAN_CODE = "web_slogan"; //web端首页口号
	public static final String MEDICALCARE_CODE = "MedicalCare"; //医疗保险个人 微调金额
	public static final String SHB_TIP_CODE = "shbTip"; //社保参保提示信息
	public static final String SURRENDER_COUNT_DOWN = "surrender_count_down"; //城市每月代缴终止日
//	public static final String MEMBERSHIP_MEAL = "membership_meal"; //充值{100}元，享受{{3}}个月服务费{{{0.2}}}折
//	public static final String NEWHAND_MEAL = "newhand_meal"; //新手参保服务费折扣0.8
	public static final String DEFAULT_VIP_RECHARGE_FEE_CODE = "vip_recharge_fee"; //会员充值费用
	public static final String DEFAULT_VIP_VALID_MONTH_CODE = "vip_valid_month"; //会员有效月份
	public static final String DEFAULT_VIP_DISCOUNT_CODE = "vip_discount"; //会员优惠折扣
	public static final String DEFAULT_FIRST_ORDER_DISCOUNT_CODE = "fist_order_discount"; //首单优惠折扣
	public static final String DEFAULT_PRICE_TO_POINT_CODE = "one_price_to_point"; //1元转金豆比例
	public static final String WEB_VIDEO_URL = "web_video_url"; //WEB左下角视频播放地址
	public static final String SHB_SET_SUB_TITLE = "shb_set_sub_title"; //缴社保金页面，社保套餐副标题展示
	public static final String GJJ_SET_SUB_TITLE = "gjj_set_sub_title"; //缴公积金页面，公积金套餐副标题展示



	/** 编码 */
	private String code;

	/** 编码值 */
	private String codeValue;

	/** 备注 */
	private String memo;

	/** 是否内置 */
	private Boolean isSystem;


	@Column(nullable = false, unique = true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取是否内置
	 *
	 * @return 是否内置
	 */
	@Column(nullable = false, updatable = false)
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * 设置是否内置
	 *
	 * @param isSystem
	 *            是否内置
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params) {
		Configs configs =((Configs) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("createDate", DateUtil.format(configs.getCreateDate()));
		return jo;
	}
}