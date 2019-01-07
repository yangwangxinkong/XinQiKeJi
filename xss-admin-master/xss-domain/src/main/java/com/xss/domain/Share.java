/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xss.domain.enums.InvoiceCategory;
import com.xss.domain.enums.InvoiceType;
import com.xss.domain.enums.ShareCategory;
import com.xss.util.CurrencyMethod;
import com.xss.util.DateUtil;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 分享信息
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_share")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_share_sequence")
public class Share extends BaseEntity {

	private static final long serialVersionUID = 2673602067029665976L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "shareMemberId", "shareMemberMobile", "sharedMemberId", "sharedMemberMobile", "shareCategory"};

	/**
	 * 分享人编码
	 */
	private Long shareMemberId;

	/**
	 * 分享人手机
	 */
	private String shareMemberMobile;

	/**
	 * 分享对象
	 */
	private Long sharedMemberId;

	/**
	 * 分享对象手机
	 */
	private String sharedMemberMobile;

	/**
	 * 分享类型
	 */
	private ShareCategory shareCategory;

	/** 注册返佣红包 */
	private BigDecimal registerMoney;

	/** 订单返佣红包 */
	private BigDecimal orderMoney;

	public Long getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(Long shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public String getShareMemberMobile() {
		return shareMemberMobile;
	}

	public void setShareMemberMobile(String shareMemberMobile) {
		this.shareMemberMobile = shareMemberMobile;
	}

	public Long getSharedMemberId() {
		return sharedMemberId;
	}

	public void setSharedMemberId(Long sharedMemberId) {
		this.sharedMemberId = sharedMemberId;
	}

	public String getSharedMemberMobile() {
		return sharedMemberMobile;
	}

	public void setSharedMemberMobile(String sharedMemberMobile) {
		this.sharedMemberMobile = sharedMemberMobile;
	}

	public ShareCategory getShareCategory() {
		return shareCategory;
	}

	public void setShareCategory(ShareCategory shareCategory) {
		this.shareCategory = shareCategory;
	}

	public BigDecimal getRegisterMoney() {
		return registerMoney;
	}

	public void setRegisterMoney(BigDecimal registerMoney) {
		this.registerMoney = registerMoney;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
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
		Share share = (Share)entity;
		JSONObject jo = super.convertEntity(share,DEFAULT_JSON_PARAMS);
		jo.put("createDate", DateUtil.format(share.getCreateDate()));
		jo.put("shareCategoryDesc", share.getShareCategory().getDesc());
		if (null != share.getRegisterMoney()) {
			jo.put("registerMoney", CurrencyMethod.currency(share.getRegisterMoney()));
		}
		if (null != share.getOrderMoney()) {
			jo.put("orderMoney", CurrencyMethod.currency(share.getOrderMoney()));
		}
		return jo;
	}

}