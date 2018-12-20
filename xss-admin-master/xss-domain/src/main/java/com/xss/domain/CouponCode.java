/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 优惠码
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_coupon_code")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_coupon_code_sequence")
public class CouponCode extends BaseEntity {

	private static final long serialVersionUID = -1812874037224306719L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "code", "isUsed"};

	/** 号码 */
	private String code;

	/** 是否已使用 */
	private Boolean isUsed;

	/** 使用日期 */
	private Date usedDate;

	/** 优惠券 */
	private Coupon coupon;

	/** 会员 */
	private Member member;

	/** 订单 */
	private Order order;

	/**
	 * 获取号码
	 * 
	 * @return 号码
	 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getCode() {
		return code;
	}

	/**
	 * 设置号码
	 * 
	 * @param code
	 *            号码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取是否已使用
	 * 
	 * @return 是否已使用
	 */
	@Column(nullable = false)
	public Boolean getIsUsed() {
		return isUsed;
	}

	/**
	 * 设置是否已使用
	 * 
	 * @param isUsed
	 *            是否已使用
	 */
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * 获取使用日期
	 * 
	 * @return 使用日期
	 */
	public Date getUsedDate() {
		return usedDate;
	}

	/**
	 * 设置使用日期
	 * 
	 * @param usedDate
	 *            使用日期
	 */
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	/**
	 * 获取优惠券
	 * 
	 * @return 优惠券
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Coupon getCoupon() {
		return coupon;
	}

	/**
	 * 设置优惠券
	 * 
	 * @param coupon
	 *            优惠券
	 */
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
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
	 * 获取订单
	 * 
	 * @return 订单
	 */
	@OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
//		if (getOrder() != null) {
//			getOrder().setCouponCode(null);
//		}
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params) {
		CouponCode couponCode = ((CouponCode) entity);
		JSONObject jo = super.convertEntity(entity, DEFAULT_JSON_PARAMS);
		jo.put("createDate", DateUtil.format(couponCode.getCreateDate()));
		//使用日期
		if (null != couponCode.getUsedDate()){
			jo.put("usedDate", DateUtil.format(couponCode.getUsedDate()));
		}
		//优惠券
		if (null != couponCode.getCoupon()){
			jo.put("coupon", JsonUtil.toJSONObject(couponCode.getCoupon(), new String[]{"id","name"}));
		}
		//用户信息
		if (null != couponCode.getMember()){
			jo.put("member", JsonUtil.toJSONObject(couponCode.getMember(), new String[]{"id", "mobile", "name"}));
		}
		//订单信息
		if (null != couponCode.getOrder()){
			JSONObject orderJo = JsonUtil.toJSONObject(couponCode.getOrder(), new String[]{"id", "sn"});
			orderJo.put("orderType", couponCode.getOrder().getOrderType().getDesc());
			orderJo.put("paymentStatus", couponCode.getOrder().getPaymentStatus().getDesc());
			jo.put("order", orderJo);
		}
		return jo;
	}

}