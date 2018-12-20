/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 属性
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_attribute")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_attribute_sequence")
public class Attribute extends OrderEntity {

	private static final long serialVersionUID = 2447794131117928367L;
	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "propertyIndex", "order"};

	/** 城市编码 */
//	private String cityCode;

	/** 名称 */
	private String name;

	/** 属性序号 */
	private Integer propertyIndex;

	/** 绑定分类 */
	private ProductCategory productCategory;

	/** 可选项 */
	private List<String> options = new ArrayList<String>();

//	@NotBlank
//	@JsonProperty
//	@Pattern(regexp = "^[0-9a-z_A-Z]+$")
//	@Column(nullable = false, length = 100)
//	public String getCityCode() {
//		return cityCode;
//	}
//
//	public void setCityCode(String cityCode) {
//		this.cityCode = cityCode;
//	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
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

	/**
	 * 获取属性序号
	 * 
	 * @return 属性序号
	 */
	@Column(nullable = false, updatable = false)
	public Integer getPropertyIndex() {
		return propertyIndex;
	}

	/**
	 * 设置属性序号
	 * 
	 * @param propertyIndex
	 *            属性序号
	 */
	public void setPropertyIndex(Integer propertyIndex) {
		this.propertyIndex = propertyIndex;
	}

	/**
	 * 获取绑定分类
	 * 
	 * @return 绑定分类
	 */
	@NotNull(groups = Save.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置绑定分类
	 * 
	 * @param productCategory
	 *            绑定分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * 获取可选项
	 * 
	 * @return 可选项
	 */
	@JsonProperty
	@NotEmpty
	@ElementCollection
	@CollectionTable(name = "xx_attribute_option")
	public List<String> getOptions() {
		return options;
	}

	/**
	 * 设置可选项
	 * 
	 * @param options
	 *            可选项
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Attribute attribute = ((Attribute)entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("productCategory", JsonUtil.toJSONObject(((Attribute)entity).getProductCategory(), new String[]{"id","name"}));

		JSONArray ja = new JSONArray();
		for (String option : ((Attribute)entity).getOptions()){
			JSONObject opJo = new JSONObject();
			opJo.put("option", option);
			ja.add(opJo);
		}
		jo.put("options",ja);
		jo.put("value",""); //设置初始化选中数据，仅供商品添加用
		jo.put("optionDesc", String.join(",", ((Attribute)entity).getOptions()));
		return jo;
	}
}