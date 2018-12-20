/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xss.domain.enums.SocialSecurityCategory;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 城市
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_city")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_city_sequence")
public class City extends OrderEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "fullName", "code", "isMaster", "grade"};

	private static final long serialVersionUID = -2158109459123036967L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/city/list";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 名称 */
	private String name;

	/** 城市编码 */
	private String code;

	/** 是否总站*/
	private Boolean isMaster;

	/** 全称 */
	private String fullName;

	/** 树路径 */
	private String treePath;

	/** 层级 */
	private Integer grade;

	/** 上级地区 */
	private City parent;

	/** 下级地区 */
	private Set<City> children = new HashSet<City>();

	/** 社保比例配置集合 */
	private Set<SocialSecurityRatioSetting> socialSecurityRatioSettings = new HashSet<>();

	/** 公积金比例配置集合 */
	private Set<ProvidentFundRatioSetting> providentFundRatioSettings = new HashSet<>();

	/** 服务费集合 */
	private Set<ServiceFeeSetting> serviceFeeSettings = new HashSet<>();

	/** 报价单集合 */
	private Set<Quotation> quotations = new HashSet<>();

	/** 订单集合 */
	private Set<Order> orders = new HashSet<>();

	/** 户口类型集合 */
	private Set<ResidenceType> residenceTypes = new HashSet<>();

	/** 缴费基数集合 */
	private Set<PayBase> payBases = new HashSet<>();

	/**
	 * 获取名称
	 *
	 * @return 名称
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 *
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	@NotEmpty
	@Pattern(regexp = "^[0-9a-z_A-Z]+$")
	@Column(nullable = false, unique = true, length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}

	/**
	 * 获取全称
	 *
	 * @return 全称
	 */
	@Column(nullable = false, length = 500)
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置全称
	 *
	 * @param fullName
	 *            全称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获取树路径
	 *
	 * @return 树路径
	 */
	@Column(nullable = false, updatable = false)
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 *
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 *
	 * @return 层级
	 */
	@Column(nullable = false)
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 *
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 获取上级地区
	 *
	 * @return 上级地区
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public City getParent() {
		return parent;
	}

	/**
	 * 设置上级地区
	 *
	 * @param parent
	 *            上级地区
	 */
	public void setParent(City parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级地区
	 *
	 * @return 下级地区
	 */
	@JsonProperty
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("order asc")
	public Set<City> getChildren() {
		return children;
	}

	/**
	 * 设置下级地区
	 *
	 * @param children
	 *            下级地区
	 */
	public void setChildren(Set<City> children) {
		this.children = children;
	}


	/**
	 * 持久化前处理
	 */
	@PrePersist
	@Override
	public void prePersist() {
		City parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}



	/**
	 * 更新前处理
	 */
	@PreUpdate
	@Override
	public void preUpdate() {
		City parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
	}

	/**
	 * 重写toString方法
	 *
	 * @return 全称
	 */
	@Override
	public String toString() {
		return getFullName();
	}


	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	public Set<SocialSecurityRatioSetting> getSocialSecurityRatioSettings() {
		return socialSecurityRatioSettings;
	}

	public void setSocialSecurityRatioSettings(Set<SocialSecurityRatioSetting> socialSecurityRatioSettings) {
		this.socialSecurityRatioSettings = socialSecurityRatioSettings;
	}

	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	public Set<ProvidentFundRatioSetting> getProvidentFundRatioSettings() {
		return providentFundRatioSettings;
	}

	public void setProvidentFundRatioSettings(Set<ProvidentFundRatioSetting> providentFundRatioSettings) {
		this.providentFundRatioSettings = providentFundRatioSettings;
	}

	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	public Set<ServiceFeeSetting> getServiceFeeSettings() {
		return serviceFeeSettings;
	}

	public void setServiceFeeSettings(Set<ServiceFeeSetting> serviceFeeSettings) {
		this.serviceFeeSettings = serviceFeeSettings;
	}

	/**
	 * 获取报价单基本表
	 * @return
	 */
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	public Set<Quotation> getQuotations() {
		return quotations;
	}

	/**
	 * 设置报价单基本表
	 * @param quotations
	 */
	public void setQuotations(Set<Quotation> quotations) {
		this.quotations = quotations;
	}

	/**
	 * 获取订单
	 * @return
	 */
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * @param orders
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取户口类型
	 * @return
	 */
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<ResidenceType> getResidenceTypes() {
		return residenceTypes;
	}

	/**
	 * 设置户口类型
	 * @param residenceTypes
	 */
	public void setResidenceTypes(Set<ResidenceType> residenceTypes) {
		this.residenceTypes = residenceTypes;
	}

	/**
	 * 获取缴费基数
	 * @return
	 */
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<PayBase> getPayBases() {
		return payBases;
	}

	/**
	 * 设置缴费基数
	 * @param payBases
	 */
	public void setPayBases(Set<PayBase> payBases) {
		this.payBases = payBases;
	}

	/**
	 * 获取树路径
	 *
	 * @return 树路径
	 */
	@Transient
	public List<Long> getTreePaths() {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				treePaths.add(Long.valueOf(id));
			}
		}
		return treePaths;
	}

	/**
	 * 获取访问路径
	 *
	 * @return 访问路径
	 */
	@Transient
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
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
		City city = ((City)entity);
		JSONObject jo = super.convertEntity(city, DEFAULT_JSON_PARAMS);
		/*if (ArrayUtils.contains(params, "socialSecurityRatioSettings")){
			if(null!=city.getSocialSecurityRatioSettings()){
				jo.put("area", JsonUtil.toJSONObject(city.getSocialSecurityRatioSettings(), new String[]{"id", "name", "fullName", "treePath"}));
			}else{
				jo.put("area", null);
			}
		}*/

		jo.put("order",city.getOrder() != null ? city.getOrder() : null);
		jo.put("treePath", city.getTreePath());
		if (null != city.getParent()){
			jo.put("parent", city.getParent().getId());
		} else {
			jo.put("parent", "");
		}

		return jo;
	}
}