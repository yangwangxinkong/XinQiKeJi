/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 规格
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_specification")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_specification_sequence")
public class Specification extends OrderEntity {

	private static final long serialVersionUID = -6346775052811140926L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "type", "memo", "order"};
	/**
	 * 类型
	 */
	public enum Type {

		/** 文本 */
		text,

		/** 图片 */
		image
	};

//	/** 城市编码 */
//	private String cityCode;

	/** 名称 */
	private String name;

	/** 类型 */
	private Type type;

	/** 备注 */
	private String memo;

	/** 绑定分类 */
	private ProductCategory productCategory;

	/** 规格值 */
	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@NotNull
	@Column(nullable = false)
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
	 * 获取规格值
	 * 
	 * @return 规格值
	 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "specification", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order asc")
	public List<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	/**
	 * 设置规格值
	 * 
	 * @param specificationValues
	 *            规格值
	 */
	public void setSpecificationValues(List<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@ManyToMany(mappedBy = "specifications", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
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

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		Specification specification = ((Specification)entity);
		jo.put("typeDesc", specification.getType().equals(Type.text) ? "文本" : "图片");
		jo.put("productCategory", JsonUtil.toJSONObject(specification.getProductCategory(), new String[]{"id","name"}));

		if (null != params){
			if (ArrayUtils.contains(params, "specificationValues")){
				StringBuffer specValueNames = new StringBuffer();
				JSONArray specValueJa = new JSONArray();
				for(SpecificationValue sv : ((Specification)entity).getSpecificationValues()){
					JSONObject svJo = JsonUtil.toJSONObject(sv, new String[]{"id","name","order"});
					specValueJa.add(svJo);
					specValueNames.append(sv.getName()).append(" ");
				}
				jo.put("specificationValues",specValueJa);
				jo.put("specValueNames", specValueNames.toString());
			}
		}
		return jo;
	}
}