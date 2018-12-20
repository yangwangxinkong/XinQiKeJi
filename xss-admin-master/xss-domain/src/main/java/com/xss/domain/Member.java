/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.Gender;
import com.xss.util.CurrencyMethod;
import com.xss.util.DateUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 会员
 * 
 * @author DannyZou
 * @version 1.0
 */

@Entity
@Table(name = "xx_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_member_sequence")
public class Member extends BaseEntity {

	private static final long serialVersionUID = 1533130686714725835L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "openId", "username", "name", "mobile", "point"
			, "balance", "isEnabled", "isLocked", "registerIp", "identification", "gender", "headImage", "isVip"
			, "isNew", "idFace", "idBackFace", "hukouIndex", "hukouPerson", "onePhone", "cityName"};

	/**
	 * 用户性质
	 */
	public enum Nature {
		
		buyer(0,"个人"), company(1,"公司");
		
		 private int value;
		    private String desc;
		    
		    Nature(int value, String desc) {
		        this.value = value;
		        this.desc = desc;
		    }

		    public int getValue() {
		        return value;
		    }

		    public String getDesc() {
		        return desc;
		    }
		    
		    public static Nature findByValue(int value) {
		    	Nature nature = null;
		        
		        for (Nature rs : Nature.values()) {
		            if (rs.value == value) {
		            	nature = rs;
		                break;
		            }
		        }
		        
		        return nature;
		    }
	}

	/**
	 * 关注会员
	 */
	public enum Subscription {

		subscribe(0,"已关注"),unSubscribe(1,"未关注");

		private int value;
		private String desc;

		Subscription(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

		public static Subscription findByDesc(String desc) {
			Subscription subscription = null;

			for (Subscription rs : Subscription.values()) {
				if (desc.equals(rs.desc)) {
					subscription = rs;
					break;
				}
			}

			return subscription;
		}

		public static Subscription findByValue(int value) {
			Subscription subscription = null;

			for (Subscription rs : Subscription.values()) {
				if (rs.value == value) {
					subscription = rs;
					break;
				}
			}

			return subscription;
		}
	}

	/**
	 * 会员类型
	 */
	public enum Type {

		def(0,"普通会员"),sms(1,"短信推广用户");

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

		public static Type findByDesc(String desc) {
			Type type = null;

			for (Type rs : Type.values()) {
				if (desc.equals(rs.desc)) {
					type = rs;
					break;
				}
			}

			return type;
		}

		public static Type findByValue(int value) {
			Type type = null;

			for (Type rs : Type.values()) {
				if (rs.value == value) {
					type = rs;
					break;
				}
			}

			return type;
		}
	}

	/** "身份信息"参数名称 */
//	public static final String PRINCIPAL_ATTRIBUTE_NAME = MemberInterceptor.class.getName() + ".PRINCIPAL";

	/** session中的openId名称 */
	public static final String SESSION_OPEN_ID = "openId";

	/** "用户编号"Cookie名称 */
	public static final String USERID_COOKIE_NAME = "mid";

	/** "用户名"Cookie名称 */
	public static final String USERNAME_COOKIE_NAME = "username";

	/** 会员注册项值属性个数 */
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

	/** 会员注册项值属性名称前缀 */
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	/** 最大收藏商品数 */
	public static final Integer MAX_FAVORITE_COUNT = 10;

	/** 用户名 */
	private String username;

	/** 密码 */
	private String password;

	/**
	 * 身份证
	 */
	private String identification;

	/**
	 * 行业
	 */
	private Industry industry;

	/** 微信openId */
	private String openId;

	/** 是否关注用户 */
	private Subscription subscription;

	/** 关注日期 */
	private Date subscribeDate;
	
	/** 头像 */
	private String headImage;

	/** 积分 */
	private Long point;

	/** 消费金额 */
	private BigDecimal amount;

	/** 余额 */
	private BigDecimal balance;

	/** 是否启用 */
	private Boolean isEnabled;

	/** 是否锁定 */
	private Boolean isLocked;

	/** 是否会员 */
	private Boolean isVip;

	/** 是否新手 */
	private Boolean isNew;

	/** 会员套餐过期时间 */
	private Date vipExpireDate;

	/** 会员套餐优惠折扣 */
	private BigDecimal vipDiscount;

	/** 连续登录失败次数 */
	private Integer loginFailureCount;

	/** 锁定日期 */
	private Date lockedDate;

	/** 注册IP */
	private String registerIp;

	/** 最后登录IP */
	private String loginIp;

	/** 最后登录日期 */
	private Date loginDate;
	
	/** 上次登录IP */
	private String lastLoginIp;
	
	/** 上次登录日期 */
	private Date lastLoginDate;

	/** 昵称 */
	private String nickName;

	/** 姓名 */
	private String name;

	/** 性别 */
	private Gender gender;
	
	/** 性质 */
	private Nature nature;

	/** 出生日期 */
	private Date birth;

	/** 手机 */
	private String mobile;

	/** 身份证正面照 */
	private String idFace;

	/** 身份证反面照 */
	private String idBackFace;

	/** 户口本首页 */
	private String hukouIndex;

	/** 户口本本人页 */
	private String hukouPerson;

	/** 本人一寸照片 */
	private String onePhone;

	/** token */
	private String token;

	/** 城市名称 */
	private String cityName;

	/** 会员类型 */
	private Type type;

	/** 购物车 */
	private Cart cart;

	/** 收货地址 */
	private Set<Receiver> receivers = new HashSet<Receiver>();

	/** 订单 */
	private Set<Order> orders = new HashSet<Order>();

	/**
	 * 报价单基本表
	 */
	private Set<Quotation> quotations = new HashSet<Quotation>();

	/**
	 * 订单发票集合
	 */
	private Set<Invoice> invoices = new HashSet<Invoice>();

	/** 收藏商品 */
	private Set<Product> favoriteProducts = new HashSet<Product>();

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
	@Column(nullable = false, unique = true, length = 100)
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 获取行业
	 *
	 * @return 行业
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public Industry getIndustry() {
		return industry;
	}

	/**
	 * 设置行业
	 * @param industry
	 */
	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	/**
	 * 获取身份证
	 *
	 * @return 身份证
	 */
	@Column
	public String getIdentification() {
		return identification;
	}

	/**
	 * 设置身份证
	 * @param identification
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
	}

	/**
	 * 关注性质
	 * @return
	 */
	public Subscription getSubscription() {
		return subscription;
	}

	/**
	 * 设置关注性质
	 * @param subscription
	 */
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Date getSubscribeDate() {
		return subscribeDate;
	}

	public void setSubscribeDate(Date subscribeDate) {
		this.subscribeDate = subscribeDate;
	}

	/**
	 * 获取头像
	 * @return
	 */
	public String getHeadImage() {
		return headImage;
	}
	/**
	 * 设置头像
	 * @param headImage
	 */
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	/**
	 * 获取积分
	 * 
	 * @return 积分
	 */
	@NotNull(groups = Save.class)
	@Min(0)
	@Column(nullable = false)
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置积分
	 * 
	 * @param point
	 *            积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取消费金额
	 * 
	 * @return 消费金额
	 */
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置消费金额
	 * 
	 * @param amount
	 *            消费金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取余额
	 * 
	 * @return 余额
	 */
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置余额
	 * 
	 * @param balance
	 *            余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否锁定
	 * 
	 * @return 是否锁定
	 */
	@Column(nullable = false)
	public Boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * 设置是否锁定
	 * 
	 * @param isLocked
	 *            是否锁定
	 */
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsVip() {
		return isVip;
	}

	public void setIsVip(Boolean isVip) {
		this.isVip = isVip;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Date getVipExpireDate() {
		return vipExpireDate;
	}

	public void setVipExpireDate(Date vipExpireDate) {
		this.vipExpireDate = vipExpireDate;
	}

	public BigDecimal getVipDiscount() {
		return vipDiscount;
	}

	public void setVipDiscount(BigDecimal vipDiscount) {
		this.vipDiscount = vipDiscount;
	}

	/**
	 * 获取连续登录失败次数
	 * 
	 * @return 连续登录失败次数
	 */
	@Column(nullable = false)
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	/**
	 * 设置连续登录失败次数
	 * 
	 * @param loginFailureCount
	 *            连续登录失败次数
	 */
	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	/**
	 * 获取锁定日期
	 * 
	 * @return 锁定日期
	 */
	public Date getLockedDate() {
		return lockedDate;
	}

	/**
	 * 设置锁定日期
	 * 
	 * @param lockedDate
	 *            锁定日期
	 */
	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	/**
	 * 获取注册IP
	 * 
	 * @return 注册IP
	 */
	@Column(nullable = false, updatable = false)
	public String getRegisterIp() {
		return registerIp;
	}

	/**
	 * 设置注册IP
	 * 
	 * @param registerIp
	 *            注册IP
	 */
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	/**
	 * 获取最后登录IP
	 * 
	 * @return 最后登录IP
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * 设置最后登录IP
	 * 
	 * @param loginIp
	 *            最后登录IP
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * 获取最后登录日期
	 * 
	 * @return 最后登录日期
	 */
	public Date getLoginDate() {
		return loginDate;
	}

	/**
	 * 设置最后登录日期
	 * 
	 * @param loginDate
	 *            最后登录日期
	 */
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	/**
	 * 获取上一次登录ip
	 * @return
	 */
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	
	/**
	 * 设置上一次登录
	 * @param lastLoginIp
	 */
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	/**
	 * 获取上一次登录时间
	 * @return
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	/**
	 * 设置上一次登录时间
	 * @param lastLoginDate
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Length(max = 200)
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */
	@Length(max = 200)
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 * 
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取性别
	 * 
	 * @return 性别
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * 设置性别
	 * 
	 * @param gender
	 *            性别
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	/**
	 * 获取出生日期
	 * 
	 * @return 出生日期
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * 设置出生日期
	 * 
	 * @param birth
	 *            出生日期
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * 获取手机
	 * 
	 * @return 手机
	 */
	@Length(max = 200)
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机
	 * 
	 * @param mobile
	 *            手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdFace() {
		return idFace;
	}

	public void setIdFace(String idFace) {
		this.idFace = idFace;
	}

	public String getIdBackFace() {
		return idBackFace;
	}

	public void setIdBackFace(String idBackFace) {
		this.idBackFace = idBackFace;
	}

	public String getHukouIndex() {
		return hukouIndex;
	}

	public void setHukouIndex(String hukouIndex) {
		this.hukouIndex = hukouIndex;
	}

	public String getHukouPerson() {
		return hukouPerson;
	}

	public void setHukouPerson(String hukouPerson) {
		this.hukouPerson = hukouPerson;
	}

	public String getOnePhone() {
		return onePhone;
	}

	public void setOnePhone(String onePhone) {
		this.onePhone = onePhone;
	}

	/**
	 * 获取购物车
	 *
	 * @return 购物车
	 */
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Cart getCart() {
		return cart;
	}

	/**
	 * 设置购物车
	 *
	 * @param cart
	 *            购物车
	 */
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取收货地址
	 * 
	 * @return 收货地址
	 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("isDefault desc, createDate desc")
	public Set<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * 设置收货地址
	 * 
	 * @param receivers
	 *            收货地址
	 */
	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	/**
	 * 获取报价单基本表
	 * @return
	 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	public Set<Quotation> getQuotations() {
		return quotations;
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	/**
	 * 获取收藏商品
	 *
	 * @return 收藏商品
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_member_favorite_product")
	@OrderBy("createDate desc")
	public Set<Product> getFavoriteProducts() {
		return favoriteProducts;
	}

	/**
	 * 设置收藏商品
	 *
	 * @param favoriteProducts
	 *            收藏商品
	 */
	public void setFavoriteProducts(Set<Product> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}


	/**
	 * 设置报价单基本表
	 * @param quotations
	 */
	public void setQuotations(Set<Quotation> quotations) {
		this.quotations = quotations;
	}

	@Length(max = 200)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Transient
	public String getMemberDefaultPassward() {
		//TODO 待配置到系统配置项中
		return "123456";
		//return SettingUtils.get().getDefaultPassword();
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Member member = ((Member) entity);
		//第一种方式
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		//第二种方式
		if(StringUtils.isNotBlank(member.getNickName())){
			try {
				String nickName = new String(Base64.decodeBase64(member.getNickName()), "utf-8");
				jo.put("nickName", nickName);
			}catch (Exception e){
				jo.put("nickName", "");
			}

		}
		if (null != member.getType()){
			jo.put("type", member.getType());
			jo.put("typeDesc", member.getType().getDesc());
		}

		if (null != member.getIsVip() && member.getIsVip()){
			//组装会员套餐优惠信息
			String expireDate = DateUtil.format(member.getVipExpireDate());
			jo.put("vipExpireDate", expireDate);
			jo.put("vipDiscount", member.getVipDiscount());
		}
		jo.put("createDate", DateUtil.format(member.getCreateDate()));
		//第三种方式
		if (ArrayUtils.contains(params,"memberStatus")){
			String memberStatus = "";
			if (!member.isEnabled){
				memberStatus = "禁用";
			}else if(member.isLocked){
				memberStatus = "锁定";
			}else{
				memberStatus = "正常";
			}
			jo.put("memberStatus",memberStatus);
		}
		//消费金额
        if (ArrayUtils.contains(params,"amount")){
            jo.put("amount", CurrencyMethod.currency(member.getAmount()));
        }
		if (ArrayUtils.contains(params,"lastLoginDate")){
			jo.put("lastLoginDate", DateUtil.format(member.getLastLoginDate(),"yyyy-MM-dd HH:mm:ss"));
		}

		return jo;
	}
	public static void main(String[] args) {
		try {
			System.out.println(new String(Base64.decodeBase64("8J+SsPCfkrDwn5Kw"), "utf-8"));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}