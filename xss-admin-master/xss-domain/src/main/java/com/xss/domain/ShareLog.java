/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xss.domain.enums.ShareCategory;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Entity - 分享操作日志
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_share_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_share_log_sequence")
public class ShareLog extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","type", "shareCategory", "createDate","status","credit","debit","balance","operator","memo"};

	/**
	 * 类型
	 */
	public enum Type {

		/** 注册分享 */
		register(0,"注册分享"),

		/** 订单分享 */
		order(1,"订单分享"),
		
		/** 佣金提现 */
		cash(2,"佣金提现"),
		
		/** 佣金结算 */
		settlement(3,"佣金结算");
		
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

	/** 分享类别 */
	private ShareCategory shareCategory;

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

	@Column(nullable = false, updatable = false)
	public ShareCategory getShareCategory() {
		return shareCategory;
	}

	public void setShareCategory(ShareCategory shareCategory) {
		this.shareCategory = shareCategory;
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

	@Override
	public JSONArray convertList(List list, String[] params) {
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
	public JSONObject convertEntity(Object entity, String[] params) {
		ShareLog shareLog = (ShareLog)entity;
		JSONObject jsonObject = super.convertEntity(shareLog, params);
		jsonObject.put("shareCategoryDesc", shareLog.getShareCategory().getDesc());
		jsonObject.put("typeDesc", shareLog.getType().getDesc());
		if(ArrayUtils.contains(params, "member")){
			if (null != shareLog.getMember()) {
				jsonObject.put("member", JsonUtil.toJSONObject(shareLog.getMember(), new String[]{"id", "mobile"}));
			}
		}
		return jsonObject;
	}
}