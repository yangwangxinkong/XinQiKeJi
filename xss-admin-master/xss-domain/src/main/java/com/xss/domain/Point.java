/*
 *  
 *  
 *  
 */
package com.xss.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * Entity - 预存款
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_point")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_point_sequence")
public class Point extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 收入 */
		income(0,"收入"),

		/** 支出 */
		payout(1,"支出");
		
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
	 * 来源
	 */
	public enum Resource {
		
		/** 订单 */
		order(0,"订单"),
		/** 优惠券 */
		coupon(1,"优惠券");
		
		
		private int value;
		private String desc;
		
		Resource(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}
		
		public int getValue() {
			return value;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public static Resource findByValue(int value) {
			Resource status = null;
			
			for (Resource rs : Resource.values()) {
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
	
	/** 来源 */
	private Resource resource;
	
	/** 收入积分 */
	private Long credit;

	/** 支出积分 */
	private Long debit;

	/** 当前积分 */
	private Long balance;


	/** 备注 */
	private String memo;

	/** 会员 */
	private Member member;
	
	/** 来源 */
	private Long resourceId;


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
	 * 获取来源
	 * 
	 * @return 来源
	 */
	@Column(nullable = false, updatable = false)
	public Resource getResource() {
		return resource;
	}


	public void setResource(Resource resource) {
		this.resource = resource;
	}


	/**
	 * 获取收入积分
	 * 
	 * @return 收入积分
	 */
	@Column(precision = 21, scale = 6)
	public Long getCredit() {
		return credit;
	}

	/**
	 * 设置收入积分
	 * 
	 * @param credit
	 *            收入积分
	 */
	public void setCredit(Long credit) {
		this.credit = credit;
	}

	/**
	 * 获取支出积分
	 * 
	 * @return 支出积分
	 */
	@Column(precision = 21, scale = 6)
	public Long getDebit() {
		return debit;
	}

	/**
	 * 设置支出积分
	 * 
	 * @param debit
	 *            支出积分
	 */
	public void setDebit(Long debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前积分
	 * 
	 * @return 当前积分
	 */
	@Column( precision = 21, scale = 6)
	public Long getBalance() {
		return balance;
	}

	/**
	 * 设置当前积分
	 * 
	 * @param balance
	 *            当前积分
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
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
	 * 获取来源id
	 * @return
	 */
	public Long getResourceId() {
		return resourceId;
	}


	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}