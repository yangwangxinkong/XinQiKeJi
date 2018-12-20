/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.util.BigDecimalUtils;
import com.xss.util.DateUtil;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 收款单
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_payment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_payment_sequence")
public class Payment extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","sn","paymentMethod","payer","operator","bank","account","memo"};

	private static final long serialVersionUID = -5052430116564638634L;

	/** 支付方式分隔符 */
	public static final String PAYMENT_METHOD_SEPARATOR = " - ";

	/**
	 * 类型
	 */
	public enum Type {
		
		payment(0, "订单支付"), recharge(1, "会员充值");
	    
	    private int value;
	    private String desc;
	    
	    Type(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static Type findByValue(int value) {
	    	Type status = null;
	        
	        for (Type rs : Type.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}
	
	

	/**
	 * 方式
	 */
	public enum Method {

		online(0, "在线支付"), offline(1, "线下支付"), deposit(2, "会员费支付");
	    
	    private int value;
	    private String desc;
	    
	    Method(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static Method findByValue(int value) {
	    	Method status = null;
	        
	        for (Method rs : Method.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}

	/**
	 * 状态
	 */
	public enum Status {

		wait(0, "等待支付"), success(1, "支付成功"), failure(2, "支付失败");
	    
	    private int value;
	    private String desc;
	    
	    Status(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static Status findByValue(int value) {
	    	Status status = null;
	        
	        for (Status rs : Status.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}

	/** 编号 */
	private String sn;

	/** 类型 */
	private Type type;

	/** 方式 */
	private Method method;

	/** 状态 */
	private Status status;

	/** 支付方式 */
	private String paymentMethod;

	/** 收款银行 */
	private String bank;

	/** 收款账号 */
	private String account;

	/** 支付手续费 */
	private BigDecimal fee;

	/** 付款金额 */
	private BigDecimal amount;

	/** 付款人 */
	private String payer;

	/** 操作员 */
	private String operator;

	/** 付款日期 */
	private Date paymentDate;

	/** 备注 */
	private String memo;

	/** 支付插件ID */
	private String paymentPluginId;

	/** 到期时间 */
	private Date expire;

	/** 会员 */
	private Member member;

	/** 订单 */
	private Order order;

	/** 商品 */
	private Product product;

	/** 第三方支付交易号 */
	private String tradeNo;

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * 
	 * @param sn
	 *            编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取方式
	 * 
	 * @return 方式
	 */
	@NotNull
	@Column(nullable = false, updatable = false)
	public Method getMethod() {
		return method;
	}

	/**
	 * 设置方式
	 * 
	 * @param method
	 *            方式
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	@Column(updatable = false)
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取收款银行
	 * 
	 * @return 收款银行
	 */
	@Length(max = 200)
	public String getBank() {
		return bank;
	}

	/**
	 * 设置收款银行
	 * 
	 * @param bank
	 *            收款银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取收款账号
	 * 
	 * @return 收款账号
	 */
	@Length(max = 200)
	public String getAccount() {
		return account;
	}

	/**
	 * 设置收款账号
	 * 
	 * @param account
	 *            收款账号
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 获取支付手续费
	 * 
	 * @return 支付手续费
	 */
	@Column(nullable = false, precision = 21, scale = 6)
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
	 * 获取付款金额
	 * 
	 * @return 付款金额
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置付款金额
	 * 
	 * @param amount
	 *            付款金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取付款人
	 * 
	 * @return 付款人
	 */
	@Length(max = 200)
	public String getPayer() {
		return payer;
	}

	/**
	 * 设置付款人
	 * 
	 * @param payer
	 *            付款人
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}

	/**
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取付款日期
	 * 
	 * @return 付款日期
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * 设置付款日期
	 * 
	 * @param paymentDate
	 *            付款日期
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取支付插件ID
	 * 
	 * @return 支付插件ID
	 */
	@JoinColumn(updatable = false)
	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	/**
	 * 设置支付插件ID
	 * 
	 * @param paymentPluginId
	 *            支付插件ID
	 */
	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	/**
	 * 获取到期时间
	 * 
	 * @return 到期时间
	 */
	@JoinColumn(updatable = false)
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
	 * 获取会员
	 * 
	 * @return 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", updatable = false)
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
	 * 获取商品
	 *
	 * @return 商品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product", updatable = false)
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 *
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取有效金额
	 * 
	 * @return 有效金额
	 */
	@Transient
	public BigDecimal getEffectiveAmount() {
		BigDecimal effectiveAmount = getAmount().subtract(getFee());
		return effectiveAmount.compareTo(BigDecimal.ZERO) > 0 ? effectiveAmount : BigDecimal.ZERO;
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Payment payment = (Payment)entity;
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("amount", BigDecimalUtils.setScaleDown(payment.getAmount(), 2));
		jo.put("fee", BigDecimalUtils.setScaleDown(payment.getFee(), 2));

		jo.put("createDate", DateUtil.format(payment.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		if(null != payment.getPaymentDate()) {
			jo.put("paymentDate", DateUtil.format(payment.getPaymentDate(), "yyyy-MM-dd HH:mm:ss"));
		}

		jo.put("type", payment.getType().name());
		jo.put("typeValue", payment.getType().getValue());
		jo.put("typeDesc", payment.getType().getDesc());

		jo.put("method", payment.getMethod().name());
		jo.put("methodValue", payment.getMethod().getValue());
		jo.put("methodDesc", payment.getMethod().getDesc());

		jo.put("status", payment.getStatus().name());
		jo.put("statusValue", payment.getStatus().getValue());
		jo.put("statusDesc", payment.getStatus().getDesc());

		if(payment.getOrder() != null) {
			jo.put("orderSn", payment.getOrder().getSn());
		} else {
			jo.put("orderSn", "");
		}

		if(payment.getMember() != null) {
			jo.put("username", payment.getMember().getUsername());
		} else {
			jo.put("username", "");
		}

		return jo;
	}

}