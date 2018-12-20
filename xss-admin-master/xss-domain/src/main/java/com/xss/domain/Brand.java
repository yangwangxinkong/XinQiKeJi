/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.util.JsonUtil;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 品牌
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_brand")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_brand_sequence")
public class Brand extends OrderEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "logo", "url", "order", "type", "introduction"};

	private static final long serialVersionUID = -6109590619136943215L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/brand/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/**
	 * 类型
	 */
	public enum Type {
		text(0, "文本"), image(1, "图片");

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
			Type type = null;
			for (Type cc : Type.values()) {
				if (cc.value == value) {
					type = cc;
					break;
				}
			}
			return type;
		}
	}

//	/** 城市编码 */
//	private String cityCode;

	/** 名称 */
	private String name;

	/** 类型 */
	private Type type;

	/** logo */
	private String logo;

	/** 网址 */
	private String url;

	/** 介绍 */
	private String introduction;

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

	/** 商品分类 */
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();


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
	 * 获取logo
	 * 
	 * @return logo
	 */
	@Length(max = 200)
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * 
	 * @param logo
	 *            logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	@Length(max = 200)
	public String getUrl() {
		return url;
	}

	/**
	 * 设置网址
	 * 
	 * @param url
	 *            网址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	@Lob
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置介绍
	 * 
	 * @param introduction
	 *            介绍
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
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
	 * 获取商品分类
	 * 
	 * @return 商品分类
	 */
	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置商品分类
	 * 
	 * @param productCategories
	 *            商品分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
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

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Product> products = getProducts();
		if (products != null) {
			for (Product product : products) {
				product.setBrand(null);
			}
		}
		Set<ProductCategory> productCategories = getProductCategories();
		if (productCategories != null) {
			for (ProductCategory productCategory : productCategories) {
				productCategory.getBrands().remove(this);
			}
		}
//		Set<Promotion> promotions = getPromotions();
//		if (promotions != null) {
//			for (Promotion promotion : promotions) {
//				promotion.getBrands().remove(this);
//			}
//		}
	}
	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Brand brand = ((Brand) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("typeDesc", brand.getType().getDesc());
//		if(null != brand.getCompany()){
//			jo.put("company", JsonUtil.toJSONObject(brand.getCompany(), new String[]{"id", "name"}));
//		}
		return jo;
	}
}