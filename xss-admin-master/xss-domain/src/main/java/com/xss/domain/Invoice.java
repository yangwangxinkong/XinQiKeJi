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
import com.xss.domain.enums.InvoiceStatus;
import com.xss.domain.enums.InvoiceType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 发票信息
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_invoice")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_invoice_sequence")
public class Invoice extends BaseEntity {

	private static final long serialVersionUID = 2673602067029665976L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "invoiceTitle", "consignee", "address", "zipCode", "phone", "invoiceType", "invoiceCategory"};

//	private InvoiceStatus invoiceStatus;
	/**
	 * 开具类型
	 */
	private InvoiceType invoiceType;

	/**
	 * 发票类型
	 */
	private InvoiceCategory invoiceCategory;

	/** 发票抬头 */
	private String invoiceTitle;

	/** 发票税号 */
	private String invoiceNo;

//	/** 发票内容 */
//	private String invoiceContent;

//	/**
//	 * 发票金额
//	 */
//	private BigDecimal amount;
//
//	/**
//	 * 备注
//	 */
//	private String memo;

//	/**
//	 * 地址和电话
//	 */
//	private String addressAndTel;

//	/**
//	 * 开户行和账号
//	 */
//	private String bankAndAccount;
//
//	/**
//	 * 订单项
//	 */
//	private Set<Order> orders = new HashSet<Order>();

	/** 收货人 */
	private String consignee;

//	/** 地区名称 */
//	private String areaName;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

//	/** 地区 */
//	private Area area;

	/** 会员 */
	private Member member;

//	@Column(nullable = false)
//	public InvoiceStatus getInvoiceStatus() {
//		return invoiceStatus;
//	}
//
//	public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
//		this.invoiceStatus = invoiceStatus;
//	}

	@Column(nullable = false)
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	@Column(nullable = false)
	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	public InvoiceCategory getInvoiceCategory() {
		return invoiceCategory;
	}

	public void setInvoiceCategory(InvoiceCategory invoiceCategory) {
		this.invoiceCategory = invoiceCategory;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

//	public String getInvoiceContent() {
//		return invoiceContent;
//	}
//
//	public void setInvoiceContent(String invoiceContent) {
//		this.invoiceContent = invoiceContent;
//	}

//	@Column(nullable = false, precision = 21, scale = 2)
//	public BigDecimal getAmount() {
//		return amount;
//	}
//
//	public void setAmount(BigDecimal amount) {
//		this.amount = amount;
//	}

//	public String getMemo() {
//		return memo;
//	}
//
//	public void setMemo(String memo) {
//		this.memo = memo;
//	}

//	public String getAddressAndTel() {
//		return addressAndTel;
//	}
//
//	public void setAddressAndTel(String addressAndTel) {
//		this.addressAndTel = addressAndTel;
//	}

//	public String getBankAndAccount() {
//		return bankAndAccount;
//	}
//
//	public void setBankAndAccount(String bankAndAccount) {
//		this.bankAndAccount = bankAndAccount;
//	}
//
//	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
//	public Set<Order> getOrders() {
//		return orders;
//	}
//
//	public void setOrders(Set<Order> orders) {
//		this.orders = orders;
//	}

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

//	/**
//	 * 获取地区名称
//	 *
//	 * @return 地区名称
//	 */
//	@JsonProperty
//	@Column(nullable = false)
//	public String getAreaName() {
//		return areaName;
//	}
//
//	/**
//	 * 设置地区名称
//	 *
//	 * @param areaName
//	 *            地区名称
//	 */
//	public void setAreaName(String areaName) {
//		this.areaName = areaName;
//	}

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

//	/**
//	 * 获取地区
//	 *
//	 * @return 地区
//	 */
//	@NotNull
//	@ManyToOne(fetch = FetchType.LAZY)
//	public Area getArea() {
//		return area;
//	}
//
//	/**
//	 * 设置地区
//	 *
//	 * @param area
//	 *            地区
//	 */
//	public void setArea(Area area) {
//		this.area = area;
//	}

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
	@Override
	public void prePersist() {
//		if (getArea() != null) {
//			setAreaName(getArea().getFullName());
//		}
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	@Override
	public void preUpdate() {
//		if (getArea() != null) {
//			setAreaName(getArea().getFullName());
//		}
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
		Invoice receiver = (Invoice)entity;
		JSONObject jo = super.convertEntity(receiver,DEFAULT_JSON_PARAMS);
		jo.put("invoiceTypeDesc", receiver.getInvoiceType().getDesc());
		jo.put("invoiceCategoryDesc", receiver.getInvoiceCategory().getDesc());
		return jo;
	}

}