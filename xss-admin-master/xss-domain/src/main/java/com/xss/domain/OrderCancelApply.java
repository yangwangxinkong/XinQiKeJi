/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.Order.OrderStatus;
import com.xss.util.BigDecimalUtils;
import com.xss.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 订单取消申请
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_order_cancel_apply")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_order_cancel_apply_sequence")
public class OrderCancelApply extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "sn", "content", "rejectContent", "monthCount", "startDate", "endDate"};

	private static final long serialVersionUID = -8019074120457087212L;

	/** 申请人 */
	private Member member;
	
	/** 状态：取消订单 */
	private OrderStatus orderStatus;

	/** 取消原因 */
	private String content;
	
	/** 驳回原因 */
	private String rejectContent;

	/** 订单 */
	private Order order;
	
	/** 订单编号 */
	private String sn;

	/** 退款金额 */
	private BigDecimal amount;

	/** 缴费开始年月 */
	private String startDate;

	/** 缴费结束年月 */
	private String endDate;

	/** 缴费月数 */
	private Integer monthCount;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member", nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Lob
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Lob
	public String getRejectContent() {
		return rejectContent;
	}

	public void setRejectContent(String rejectContent) {
		this.rejectContent = rejectContent;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
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
	 * 获取订单编号
	 * @return
	 */
	@NotNull
	public String getSn() {
		return sn;
	}

	/**
	 * 设置订单编号
	 * @param sn
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取退款金额
	 * 
	 * @return 退款金额
	 */
	@NotNull
	@Min(0)
	@Column(nullable = false, updatable = false, precision = 21, scale = 2)
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置退款金额
	 * 
	 * @param amount
	 *            退款金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取订单状态
	 * 
	 * @return 订单状态
	 */
	@Column(nullable = false)
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 设置订单状态
	 * 
	 * @param orderStatus
	 *            订单状态
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * 获取 缴费开始年月
	 * @return
	 */
	@NotBlank
	@Column(nullable = false, length = 8)
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 设置 缴费开始年月
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 获取 缴费结束年月
	 * @return
	 */
	@NotBlank
	@Column(nullable = false, length = 8)
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 设置 缴费结束年月
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取 缴费月数
	 * @return
	 */
	@NotNull
	@Min(1)
	@Column(nullable = false)
	public Integer getMonthCount() {
		return monthCount;
	}

	/**
	 * 设置 缴费月数
	 * @param monthCount
	 */
	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
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

		OrderCancelApply orderCancelApply = (OrderCancelApply) entity;

		JSONObject jo = super.convertEntity(orderCancelApply, DEFAULT_JSON_PARAMS);
		jo.put("createDate", DateUtil.format(orderCancelApply.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		jo.put("amount", BigDecimalUtils.setScaleDown(orderCancelApply.getAmount(), 2));
		jo.put("orderStatus", orderCancelApply.getOrderStatus().getDesc());
		jo.put("orderStatusId", orderCancelApply.getOrderStatus().getValue());
		if(orderCancelApply.getOrder() != null) {
			jo.put("orderSn", orderCancelApply.getOrder().getSn());
		} else {
			jo.put("orderSn", "");
		}

		//用户名
		if(ArrayUtils.contains(params, "order")){
			if(orderCancelApply.getOrder() != null) {
				jo.put("order", orderCancelApply.getOrder().convertEntity(orderCancelApply.getOrder(), new String[]{"userName", "status", "typeName", "feeCategory", "payCategory", "startEndDate", "isExpired"}));
			}
		}

		return jo;
	}
}