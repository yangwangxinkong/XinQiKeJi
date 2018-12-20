/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.InvoiceStatus;
import com.xss.util.BigDecimalUtils;
import com.xss.util.DateTimeUtil;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * Entity - 订单
 * 
 * @author hu
 * @version 1.0
 */
@Entity
@Table(name = "xx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_order_sequence")
public class Order extends BaseEntity {

	private static final long serialVersionUID = 8370942500343156156L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "sn", "point", "memo", "expire", "lockExpire",
			"orderStatus","paymentStatus", "shippingStatus", "paymentMethodName", "consignee"
			, "areaName", "address", "zipCode", "phone"};

	/** 订单名称分隔符 */
	private static final String NAME_SEPARATOR = " ";

	/**
	 * 订单状态
	 */
	public enum OrderStatus {
		unconfirmed(0, "未确认"), confirmed(1, "已确认"), canceling(2, "申请停缴订单"), cancel_reject(3, "取消停缴驳回"), completed(4, "已完成"), cancelled(5,"已取消");
	    
	    private int value;
	    private String desc;
	    
	    OrderStatus(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static OrderStatus findByValue(int value) {
	        OrderStatus status = null;
	        
	        for (OrderStatus rs : OrderStatus.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}

	/**
	 * 支付状态
	 */
	public enum PaymentStatus {
		unpaid(0, "未支付"), partialPayment(1, "部分支付"), paid(2, "已支付"), refunded(3,"已退款"),refundApply(4,"申请退款"),refundReject(5,"申请退款驳回");
	    
	    private int value;
	    private String desc;
	    
	    PaymentStatus(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static PaymentStatus findByValue(int value) {
	    	PaymentStatus status = null;
	        
	        for (PaymentStatus rs : PaymentStatus.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}

	/**
	 * 配送状态
	 */
	public enum ShippingStatus {

		unshipped(0, "未发货"), partialShipment(1, "部分发货"), shipped(2, "已发货"), received(3,"已收货"), returnApply(4,"申请退货"), returnBarter(5,"申请换货"), partialReturns(6,"部分退货"), returned(7,"已退货"), returnReject(8,"退货驳回"), changing(9,"换货中"), changeComplete(10, "换货完成"), changeReject(11, "换货驳回"), changeCancel(12, "换货取消");

		private int value;
		private String desc;

		ShippingStatus(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

		public static ShippingStatus findByValue(int value) {
			ShippingStatus status = null;

			for (ShippingStatus rs : ShippingStatus.values()) {
				if (rs.value == value) {
					status = rs;
					break;
				}
			}

			return status;
		}
	}

	/**
	 * 订单类型
	 */
	public enum OrderType {

		standard(0, "标准订单"), service(1, "兑换订单"), quotation(2,"报价订单");

		private int value;
		private String desc;

		OrderType(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

		public static OrderType findByValue(int value) {
			OrderType orderType = null;

			for (OrderType rs : OrderType.values()) {
				if (rs.value == value) {
					orderType = rs;
					break;
				}
			}

			return orderType;
		}
	}

	/**
	 * 城市
	 */
	private City city;

	/** 订单编号 */
	private String sn;

	/** 订单状态 */
	private OrderStatus orderStatus;

	/** 支付状态 */
	private PaymentStatus paymentStatus;

	/** 配送状态 */
	private ShippingStatus shippingStatus;

	/** 订单类型 */
	private OrderType orderType;

	/** 支付服务费 */
	private BigDecimal fee;

	/** 订单金额 */
	private BigDecimal price;

	/** 已付金额 */
	private BigDecimal amountPaid;

	/** 赠送积分 */
	private Long point;

	/** 地区 */
	private Area area;

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

	/** 附言 */
	private String memo;

	/** 到期时间 */
	private Date expire;

	/** 锁定到期时间 */
	private Date lockExpire;

	/** 支付方式 */
	private PaymentMethod paymentMethod;

	/** 支付方式名称 */
	private String paymentMethodName;

	/** 运费 */
	private BigDecimal freight;

	/** 优惠券折扣 */
	private BigDecimal couponDiscount;


	/** 调整金额 */
	private BigDecimal offsetAmount;


	/** 操作员 */
	private Admin operator;

	/** 会员 */
	private Member member;

	/** 订单日志 */
	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

	/** 收款单 */
	private Set<Payment> payments = new HashSet<Payment>();

	/** 退款申请 */
	private List<OrderCancelApply> orderCancelApplies = new ArrayList<OrderCancelApply>();

	/**
	 * 报价单
	 */
	private Quotation quotation;

	/**
	 * 发票
	 */
	private OrderInvoice invoice;

	/** 优惠码 */
	private CouponCode couponCode;

	/** 优惠券 */
	private List<Coupon> coupons = new ArrayList<Coupon>();

	/** 订单项 */
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	/** 预存款 */
	private List<Deposit> deposits = new ArrayList<Deposit>();

	/** 发货单 */
	private Set<Shipping> shippings = new HashSet<Shipping>();

	/**
	 * 获取订单编号
	 * 
	 * @return 订单编号
	 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getSn() {
		return sn;
	}

	/**
	 * 设置订单编号
	 * 
	 * @param sn
	 *    订单编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
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
	 * 获取支付状态
	 * 
	 * @return 支付状态
	 */
	@Column(nullable = false)
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * 设置支付状态
	 * 
	 * @param paymentStatus
	 *            支付状态
	 */
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * 获取配送状态
	 *
	 * @return 配送状态
	 */
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	/**
	 * 设置配送状态
	 *
	 * @param shippingStatus
	 *            配送状态
	 */
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	/**
	 * 获取订单类型
	 * @return
	 */
	public OrderType getOrderType() {
		return orderType;
	}
	/**
	 * 设置订单类型
	 * @param orderType
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * 获取支付手续费
	 * 
	 * @return 支付手续费
	 */
	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置支付手续费
	 * 
	 * @param fee
	 *            支付手续费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * 获取订单金额
	 *
	 * @return 订单金额
	 */
	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置订单金额
	 *
	 * @param price
	 *           订单金额
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取已付金额
	 * 
	 * @return 已付金额
	 */
	@Column(nullable = false, precision = 21, scale = 2)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	/**
	 * 设置已付金额
	 *
	 * @param amountPaid
	 *            已付金额
	 */
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}


	/**
	 * 获取赠送积分
	 *
	 * @return 赠送积分
	 */
	@NotNull
	@Min(0)
	@Column(nullable = false)
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置赠送积分
	 *
	 * @param point
	 *            赠送积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取地区
	 *
	 * @return 地区
	 */
	//@NotNull
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
	 * 获取收货人
	 *
	 * @return 收货人
	 */
	@Length(max = 200)
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
//	@NotEmpty
	@Length(max = 200)
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
	@Length(max = 200)
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
	@Length(max = 200)
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
	 * 获取附言
	 * 
	 * @return 附言
	 */
	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置附言
	 * 
	 * @param memo
	 *            附言
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取到期时间
	 *
	 * @return 到期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置到期时间
	 *
	 * @param expire
	 *            到期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取锁定到期时间
	 *
	 * @return 锁定到期时间
	 */
	public Date getLockExpire() {
		return lockExpire;
	}

	/**
	 * 获取支付方式名称
	 *
	 * @return 支付方式名称
	 */

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	/**
	 * 设置支付方式名称
	 *
	 * @param paymentMethodName
	 *            支付方式名称
	 */
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * 获取运费
	 *
	 * @return 运费
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置运费
	 *
	 * @param freight
	 *            运费
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * 获取优惠券折扣
	 *
	 * @return 优惠券折扣
	 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	/**
	 * 设置优惠券折扣
	 *
	 * @param couponDiscount
	 *            优惠券折扣
	 */
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	/**
	 * 获取调整金额
	 *
	 * @return 调整金额
	 */
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	/**
	 * 设置调整金额
	 *
	 * @param offsetAmount
	 *            调整金额
	 */
	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}


	/**
	 * 设置锁定到期时间
	 *
	 * @param lockExpire
	 *            锁定到期时间
	 */
	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public Admin getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(Admin operator) {
		this.operator = operator;
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
	 * 获取 城市
	 * @return
	 */
	//@NotNull
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
	 * 获取订单日志
	 * 
	 * @return 订单日志
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	/**
	 * 设置订单日志
	 * 
	 * @param orderLogs
	 *            订单日志
	 */
	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	/**
	 * 获取收款单
	 * 
	 * @return 收款单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return payments;
	}

	/**
	 * 设置收款单
	 * 
	 * @param payments
	 *            收款单
	 */
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * 设置 退款申请
	 * @return
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<OrderCancelApply> getOrderCancelApplies() {
		return orderCancelApplies;
	}

	/**
	 * 获取退款申请
	 * @param orderCancelApplies
	 */
	public void setOrderCancelApplies(List<OrderCancelApply> orderCancelApplies) {
		this.orderCancelApplies = orderCancelApplies;
	}

	/**
	 * 设置 发票
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public OrderInvoice getInvoice() {
		return invoice;
	}

	/**
	 * 获取优惠码
	 *
	 * @return 优惠码
	 */
	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return couponCode;
	}

	/**
	 * 设置优惠码
	 *
	 * @param couponCode
	 *            优惠码
	 */
	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	public void setInvoice(OrderInvoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * 获取优惠券
	 *
	 * @return 优惠券
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_order_coupon")
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置优惠券
	 *
	 * @param coupons
	 *            优惠券
	 */
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取订单项
	 *
	 * @return 订单项
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("isGift asc")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 *
	 * @param orderItems
	 *            订单项
	 */
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}


	/**
	 * 设置 报价单
	 * @return
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Quotation getQuotation() {
		return quotation;
	}

	/**
	 * 获取 报价单
	 * @param quotation
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	/**
	 * 获取发货单
	 *
	 * @return 发货单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return shippings;
	}

	/**
	 * 设置发货单
	 *
	 * @param shippings
	 *            发货单
	 */
	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	/**
	 * 是否已过期
	 *
	 * @return 是否已过期
	 */
	@Transient
	public boolean isExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/**
	 * 获取订单金额
	 *
	 * @return 订单金额
	 */
	@Transient
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice();
		if (getFee() != null) {
			amount = amount.add(getFee());
		}
		return amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO;
	}

	/**
	 * 获取应付金额
	 *
	 * @return 应付金额
	 */
	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
		return amountPayable.compareTo(BigDecimal.ZERO) > 0 ? amountPayable : BigDecimal.ZERO;
	}

	/**
	 * 获取订单项
	 *
	 * @param sn
	 *            商品编号
	 * @return 订单项
	 */
	@Transient
	public OrderItem getOrderItem(String sn) {
		if (sn != null && getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && sn.equalsIgnoreCase(orderItem.getSn())) {
					return orderItem;
				}
			}
		}
		return null;
	}

	/**
	 * 判断是否已锁定
	 *
	 * @param operator
	 *            操作员
	 * @return 是否已锁定
	 */
	@Transient
	public boolean isLocked(Admin operator) {
		return getLockExpire() != null && new Date().before(getLockExpire()) && ((operator != null && !operator.equals(getOperator())) || (operator == null && getOperator() != null));
	}

	/**
	 * 持久化前处理
	 */
	@Override
	@PrePersist
	public void prePersist() {
	}

	/**
	 * 更新前处理
	 */
	@Override
	@PreUpdate
	public void preUpdate() {
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
		Order order = (Order)entity;
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		//订单手续费
		jo.put("fee", BigDecimalUtils.setScaleDown(order.getFee(), 2));
		//订单金额
		jo.put("price", BigDecimalUtils.setScaleDown(order.getPrice(), 2));
		//订单总金额 = 订单手续费 + 订单金额
		jo.put("amount", BigDecimalUtils.setScaleDown(order.getAmount(), 2));
		if(null != order.getPaymentMethod()){
			JSONObject pmJo = JsonUtil.toJSONObject(order.getPaymentMethod(), new String[]{"id", "name", "code"});
			jo.put("paymentMethod", pmJo);
		}

		jo.put("orderStatusId", order.getOrderStatus().getValue());
		jo.put("paymentStatusId", order.getPaymentStatus().getValue());
		jo.put("orderStatusDesc", order.getOrderStatus().getDesc());
		jo.put("paymentStatusDesc", order.getPaymentStatus().getDesc());
		if(null != order.getCreateDate()) {
			jo.put("createDate", DateUtil.format((order.getCreateDate())));
		}
		if(order.getExpire() != null) {
			jo.put("expireDate", DateUtil.format((order.getExpire())));
		}

		//城市
		if(ArrayUtils.contains(params, "city")){
			JSONObject cityJo = JsonUtil.toJSONObject(order.getQuotation().getCity(), new String[]{"id", "name", "fullName", "code"});
			jo.put("city", cityJo);
		}

		//用户名
		if(ArrayUtils.contains(params, "userName")){
			try {
//				String nickName = new String(org.apache.commons.codec.binary.Base64.decodeBase64(order.getQuotation().getUsername()), "utf-8");
				jo.put("userName", order.getQuotation().getUsername());
			}catch (Exception e){
				jo.put("userName", "");
			}

		}

		//身份证
		if(ArrayUtils.contains(params, "identification")){
			jo.put("identification", order.getQuotation().getIdentification());
		}

		//电话号码
		if(ArrayUtils.contains(params, "mobile")){
			jo.put("mobile", order.getQuotation().getMobile());
		}

		//身份证
		if(ArrayUtils.contains(params, "invoice")){
			if(order.getInvoice() == null) {
				jo.put("invoiceDesc", InvoiceStatus.is0.getDesc());
			} else {
				//jo.put("invoiceDesc", order.getInvoice().getInvoiceStatus().getDesc());
			}
		}

		//需付款
		if(ArrayUtils.contains(params, "amountPayable")){
			jo.put("amountPayable", BigDecimalUtils.setScaleDown(order.getAmountPayable(), 2));
		}

		//订单列表总体状态
		if(ArrayUtils.contains(params, "status")){

			String strStatus = "";
			if (order.isExpired()) {
				strStatus = "已过期";
			} else if (order.getOrderStatus() != OrderStatus.unconfirmed && order.getOrderStatus() != OrderStatus.confirmed) {
				strStatus = order.getOrderStatus().getDesc();
			} else if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
				strStatus = "等待付款";
			} else {
				strStatus = order.getPaymentStatus().getDesc();
			}

			jo.put("status", strStatus);
		}

		if(ArrayUtils.contains(params, "isExpired")) {
			jo.put("isExpired", order.isExpired());
		}

		// 剩 X时X分 过期
		if(ArrayUtils.contains(params, "expiredTime")){
            String expiredTime = "";

			if(order.getExpire() != null && new Date().before(order.getExpire())) {
				long from = System.currentTimeMillis();
				long to = order.getExpire().getTime();
				long between = (to - from) / 1000;

				long hours = between % (24 * 3600) / 3600;
				long minute = between % 3600 / 60;

				if(hours > 0) {
					expiredTime = hours + "时";
				}
				if(minute > 0) {
					expiredTime = expiredTime + minute + "分";
				}
			}
			jo.put("expiredTime", expiredTime);
		}

		// 订单 列表显示 typename：个人社保
		if(ArrayUtils.contains(params, "typeName")){
			String strServiceName = "";
			if(order.getMember() != null && order.getMember().getNature() != null) {
				strServiceName = strServiceName + order.getMember().getNature().getDesc();
			}

			if(order.getQuotation() != null && order.getQuotation().getFeeCategory() != null) {
				strServiceName = strServiceName + order.getQuotation().getFeeCategory().getDesc() + "  ";
			}
			jo.put("typeName", strServiceName);
		}

		// 订单 列表显示 服务项目：个人社保 年付 新参 2018-09至2019-08
		if(ArrayUtils.contains(params, "serviceName")){
			String strServiceName = "";
			if(order.getMember() != null && order.getMember().getNature() != null) {
				strServiceName = strServiceName + order.getMember().getNature().getDesc();
			}

			if(order.getQuotation() != null && order.getQuotation().getFeeCategory() != null) {
				strServiceName = strServiceName + order.getQuotation().getFeeCategory().getDesc() + "  ";
			}

			if(order.getQuotation() != null && order.getQuotation().getPayCategory() != null) {
				strServiceName = strServiceName + order.getQuotation().getPayCategory().getDesc() + "  ";
			}

			if(order.getQuotation() != null && order.getQuotation().getStartDate() != null) {
				strServiceName = strServiceName + order.getQuotation().getStartDate();

				if(order.getQuotation() != null && order.getQuotation().getMonthCount() > 1 && order.getQuotation().getEndDate() != null) {
					strServiceName = strServiceName + "至" + order.getQuotation().getEndDate();
				}
			}

			jo.put("serviceName", strServiceName);
		}

		// 订单 列表显示 2018-09至2019-08
		if(ArrayUtils.contains(params, "payCategory")) {
			if (order.getQuotation() != null && order.getQuotation().getPayCategory() != null) {
				jo.put("payCategoryName", order.getQuotation().getPayCategory().getDesc());
			}

		}

		// 订单 列表显示 2018-09至2019-08
		if(ArrayUtils.contains(params, "startEndDate")) {
			String strStartEndDate = "";
			if (order.getQuotation() != null && order.getQuotation().getStartDate() != null) {
				strStartEndDate = strStartEndDate + order.getQuotation().getStartDate();

				if (order.getQuotation() != null && order.getQuotation().getMonthCount() > 1 && order.getQuotation().getEndDate() != null) {
					strStartEndDate = strStartEndDate + "至" + order.getQuotation().getEndDate();
				}
			}
			jo.put("startEndDate", strStartEndDate);
		}

		//参保类型
		if(ArrayUtils.contains(params, "feeCategory") && null != order.getQuotation()){
			jo.put("feeCategory", order.getQuotation().getFeeCategory());
		}

		// 显示停缴申请
		if(ArrayUtils.contains(params, "cancelPaid") && null != order.getQuotation()){
			if(DateUtil.getIntervalMonths(new Date(), DateUtil.parse(order.getQuotation().getEndDate() + "-01", DateTimeUtil.FMT_yyyyMMdd)) > 0) {
				jo.put("cancelPaid", true);
			} else {
				jo.put("cancelPaid", false);
			}
		}

		return jo;
	}

}