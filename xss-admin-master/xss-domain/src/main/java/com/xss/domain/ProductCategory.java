/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.apache.commons.lang.StringUtils;
//import org.hibernate.search.annotations.Field;
//import org.hibernate.search.annotations.Index;

/**
 * Entity - 商品分类
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_product_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_category_sequence")
public class ProductCategory extends OrderEntity {

	private static final long serialVersionUID = 5095521437302782717L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","name","code","seoTitle","seoKeywords","seoDescription","order", "grade", "image"};

	//找设计商品分类code
	public static final String CAILIAO_DEFAULT_CODE = "ZCL";
	//旧房翻新商品分类code
	public static final String JIUFANGFANXIN_DEFAULT_CODE = "JFFX";
	//web找材料页面楼层1商品分类code
	public static final String MATERIAL_FLOOR_FIRST = "0010";
	//web找材料页面楼层2商品分类code
	public static final String MATERIAL_FLOOR_SECOND = "0020";
	//web找材料页面楼层3商品分类code
	public static final String MATERIAL_FLOOR_THIRD="0030";
	//web找材料页面楼层4商品分类code
	public static final String MATERIAL_FLOOR_FOURTH="0040";
	//web找材料页面楼层5商品分类code
	public static final String MATERIAL_FLOOR_FIFTH="0050";
	//web找材料页面楼层6商品分类code
	public static final String MATERIAL_FLOOR_SIXTH="0060";
	//web找材料页面楼层7商品分类code
	public static final String MATERIAL_FLOOR_SEVENTH="0070";
	//web找材料页面楼层8商品分类code
	public static final String MATERIAL_FLOOR_EIGHTH="0080";

	//web旧房翻新主页楼层1商品分类code
	public static final String OLD_HOUSE_FLOOR_1 = JIUFANGFANXIN_DEFAULT_CODE+"_001";
	//web旧房翻新主页楼层2商品分类code
	public static final String OLD_HOUSE_FLOOR_2 = JIUFANGFANXIN_DEFAULT_CODE+"_002";
	//web旧房翻新主页楼层3商品分类code
	public static final String OLD_HOUSE_FLOOR_3 = JIUFANGFANXIN_DEFAULT_CODE+"_003";
	//web旧房翻新主页楼层4商品分类code
	public static final String OLD_HOUSE_FLOOR_4 = JIUFANGFANXIN_DEFAULT_CODE+"_004";
	//web旧房翻新主页楼层5商品分类code
	public static final String OLD_HOUSE_FLOOR_5 = JIUFANGFANXIN_DEFAULT_CODE+"_005";


	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/product/list";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 名称 */
	private String name;

	/** 编码 */
	private String code;

	/** 页面标题 */
	private String seoTitle;

	/** 页面关键词 */
	private String seoKeywords;

	/** 页面描述 */
	private String seoDescription;

	/** 树路径 */
	private String treePath;

	/** 层级 */
	private Integer grade;

	/** 上级分类 */
	private ProductCategory parent;

	/** 下级分类 */
	private Set<ProductCategory> children = new HashSet<ProductCategory>();

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

	/** 筛选品牌 */
	private Set<Brand> brands = new HashSet<Brand>();

	/** 参数组 */
	private Set<ParameterGroup> parameterGroups = new HashSet<ParameterGroup>();

	/** 规格集合 */
	private Set<Specification> specifications = new HashSet<Specification>();

	/** 筛选属性 */
	private Set<Attribute> attributes = new HashSet<Attribute>();
	
	/** 展示图片 */
	private String image;

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
	 * 获取商品分类编码
	 * @return 商品分类编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置商品分类编码
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取页面标题
	 * 
	 * @return 页面标题
	 */
	@Length(max = 200)
	public String getSeoTitle() {
		return seoTitle;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param seoTitle
	 *            页面标题
	 */
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	/**
	 * 获取页面关键词
	 * 
	 * @return 页面关键词
	 */
	@Length(max = 200)
	public String getSeoKeywords() {
		return seoKeywords;
	}

	/**
	 * 设置页面关键词
	 * 
	 * @param seoKeywords
	 *            页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取页面描述
	 * 
	 * @return 页面描述
	 */
	@Length(max = 200)
	public String getSeoDescription() {
		return seoDescription;
	}

	/**
	 * 设置页面描述
	 * 
	 * @param seoDescription
	 *            页面描述
	 */
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	@Column(nullable = false)
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
	 * 获取上级分类
	 * 
	 * @return 上级分类
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public ProductCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * 
	 * @param parent
	 *            上级分类
	 */
	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * 
	 * @param children
	 *            下级分类
	 */
	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
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
	 * 获取筛选品牌
	 * 
	 * @return 筛选品牌
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_category_brand")
	@OrderBy("order asc")
	public Set<Brand> getBrands() {
		return brands;
	}

	/**
	 * 设置筛选品牌
	 * 
	 * @param brands
	 *            筛选品牌
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<ParameterGroup> getParameterGroups() {
		return parameterGroups;
	}

	/**
	 * 设置参数组
	 * 
	 * @param parameterGroups
	 *            参数组
	 */
	public void setParameterGroups(Set<ParameterGroup> parameterGroups) {
		this.parameterGroups = parameterGroups;
	}

	/**
	 * 获取筛选属性
	 * 
	 * @return 筛选属性
	 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * 设置筛选属性
	 * 
	 * @param attributes
	 *            筛选属性
	 */
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Specification> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}


	/**
	 * 获取展示图片
	 * 
	 * @return 展示图片
	 */
	@JsonProperty
//	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getImage() {
		return image;
	}

	/**
	 * 设置展示图片
	 * 
	 * @param image
	 *            展示图片
	 */
	public void setImage(String image) {
		this.image = image;
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

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {

	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		ProductCategory productCategory = ((ProductCategory) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		if (ArrayUtils.contains(params, "children")){
			JSONArray childJa = new JSONArray();
			for (ProductCategory child : productCategory.getChildren()){
				JSONObject childJo = super.convertEntity(child,DEFAULT_JSON_PARAMS);
				childJa.add(childJo);
			}
			jo.put("children", childJa);
		}
		if (ArrayUtils.contains(params, "parent")){
			if (null != productCategory.getParent()){
				JSONObject parentJo = super.convertEntity(productCategory.getParent(),DEFAULT_JSON_PARAMS);
				jo.put("parent", parentJo);
			}

		}
		return jo;
	}

}