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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 参数组
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_parameter_group")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_parameter_group_sequence")
public class ParameterGroup extends OrderEntity {

	private static final long serialVersionUID = 192003501177471941L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "order"};

//	/** 城市编码 */
//	private String cityCode;

	/** 名称 */
	private String name;

	/** 绑定分类 */
	private ProductCategory productCategory;

	/** 参数 */
	private List<Parameter> parameters = new ArrayList<Parameter>();

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
	 * 获取绑定分类
	 * 
	 * @return 绑定分类
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
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
	 * 获取参数
	 * 
	 * @return 参数
	 */
	@JsonProperty
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "parameterGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order asc")
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * 设置参数
	 * 
	 * @param parameters
	 *            参数
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		ParameterGroup parameterGroup = ((ParameterGroup)entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("productCategory", JsonUtil.toJSONObject(parameterGroup.getProductCategory(), new String[]{"id","name"}));

		JSONArray ja = new JSONArray();
		StringBuffer sb = new StringBuffer();
		for (Parameter parameter : ((ParameterGroup)entity).getParameters()){
			JSONObject opJo = new JSONObject();
			opJo.put("id", parameter.getId());
			opJo.put("name", parameter.getName());
			opJo.put("value", "");
			opJo.put("order", parameter.getOrder());
			ja.add(opJo);
			sb.append(parameter.getName()).append(" ");
		}
		jo.put("parameters",ja);
		jo.put("parameterDesc", sb.toString());
		return jo;
	}

}