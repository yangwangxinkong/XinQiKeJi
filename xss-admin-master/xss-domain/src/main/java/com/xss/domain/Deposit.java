/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xss.util.JsonUtil;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Entity - 预存款
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_deposit_sequence")
public class Deposit extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","type","createDate","status","credit","debit","balance","operator","memo"};

	public static final String[] RANKING_JSON_PARAMS = new String[]{"id","type","createDate","status","credit","debit","balance","operator","memo","member","order","payment"};

	/**
	 * 类型
	 */
	public enum Type {

		/** 会员充值 */
		memberRecharge(0,"会员充值"),

		/** 会员支付 */
		memberPayment(1,"会员支付"),

		/** 后台充值 */
		adminRecharge(2,"后台充值"),

		/** 后台扣费 */
		adminChargeback(3,"后台扣费"),

		/** 后台支付 */
		adminPayment(4,"后台支付"),

		/** 后台退款 */
		adminRefunds(5,"后台退款"),
		
		/** 提现 */
		cash(6,"提现"),
		
		/** 结算 */
		settlement(7,"结算");
		
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
	 * 状态
	 */
	public enum Status {
		
		approving(0,"审核中"),approved(1,"审核通过"),not_approved(2,"审核不通过");
		
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

	/** 类型 */
	private Type type;
	
	/** 状态 */
	private Status status;

	/** 收入金额 */
	private BigDecimal credit;

	/** 支出金额 */
	private BigDecimal debit;

	/** 当前余额 */
	private BigDecimal balance;

	/** 操作员 */
	private String operator;

	/** 备注 */
	private String memo;

	/** 会员 */
	private Member member;

	/** 订单 */
	private Order order;

	/** 收款单 */
	private Payment payment;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取收入金额
	 * 
	 * @return 收入金额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getCredit() {
		return credit;
	}

	/**
	 * 设置收入金额
	 * 
	 * @param credit
	 *            收入金额
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * 获取支出金额
	 * 
	 * @return 支出金额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getDebit() {
		return debit;
	}

	/**
	 * 设置支出金额
	 * 
	 * @param debit
	 *            支出金额
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前余额
	 * 
	 * @return 当前余额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置当前余额
	 * 
	 * @param balance
	 *            当前余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
	 * 获取收款单
	 * 
	 * @return 收款单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public Payment getPayment() {
		return payment;
	}

	/**
	 * 设置收款单
	 * 
	 * @param payment
	 *            收款单
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}


	@Override
	public JSONArray convertList(List list, String[] params) {
		JSONArray jsonArray = super.convertList(list, params);
		List paramsList = Arrays.asList(params);
		for(int i=0; i<list.size(); i++){
			Deposit deposit = (Deposit) list.get(i);
			JSONObject depositJo = jsonArray.getJSONObject(i);
			if (paramsList.contains("createDate")){
				depositJo.put("createDate", deposit.getCreateDate().getTime());
			}
			if (paramsList.contains("type") && deposit.type!=null){
				depositJo.put("typeDesc", deposit.type.desc);
			}
			if (paramsList.contains("status") && deposit.status!=null){
				depositJo.put("statusDesc", deposit.status.desc);
			}
			if (paramsList.contains("member") && deposit.getMember()!=null){
				depositJo.put("member", JsonUtil.toJSONObject(deposit.getMember(), new String[]{"id","username"}));
			}
			if (paramsList.contains("order") && deposit.getOrder()!=null){
				depositJo.put("order", JsonUtil.toJSONObject(deposit.getOrder(), new String[]{"id","sn"}));
			}
			if (paramsList.contains("payment") && deposit.getPayment()!=null){
				depositJo.put("payment", JsonUtil.toJSONObject(deposit.getPayment(), new String[]{"id", "sn"}));
			}
		}


		return jsonArray;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params) {
		JSONObject jsonObject = super.convertEntity(entity, params);

		return jsonObject;
	}
}