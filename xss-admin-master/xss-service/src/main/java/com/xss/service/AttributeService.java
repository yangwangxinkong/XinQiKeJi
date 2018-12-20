/*
 *  
 *  
 *  
 */
package com.xss.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.AttributeDao;
import com.xss.domain.Attribute;
import com.xss.domain.Product;
import com.xss.domain.ProductCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 * Service - 商品属性
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class AttributeService extends BaseService<Attribute, Long>{

	@Resource
	private AttributeDao attributeDao;

	@Resource
	public void setBaseDao(AttributeDao attributeDao) {
		super.setBaseDao(attributeDao);
	}

	/**
	 * 设置propertyIndex并保存
	 *
	 * @param attribute
	 *            属性
	 */
	@Override
	public void persist(Attribute attribute) {
		Assert.notNull(attribute);
		String jpql = "select attribute.propertyIndex from Attribute attribute where attribute.productCategory = :productCategory";
		List<Integer> propertyIndexs = entityManager.createQuery(jpql, Integer.class).setFlushMode(FlushModeType.COMMIT).setParameter("productCategory", attribute.getProductCategory()).getResultList();
		for (int i = 0; i < Product.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			if (!propertyIndexs.contains(i)) {
				attribute.setPropertyIndex(i);
				super.persist(attribute);
				break;
			}
		}
	}

	/**
	 * 清除商品属性值并删除
	 *
	 * @param attribute
	 *            属性
	 */
	@Override
	public void remove(Attribute attribute) {
		if (attribute != null) {
			String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
			String jpql = "update Product product set product." + propertyName + " = null where product.productCategory = :productCategory";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("productCategory", attribute.getProductCategory()).executeUpdate();
			super.remove(attribute);
		}
	}

	/**
	 * 根据商品分类获取指定的商品属性Json列表数据
	 * @param productCategory
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonListByProductCategory(ProductCategory productCategory) throws Exception{
		JSONArray attributeJa = new JSONArray();
		if (null != productCategory && null != productCategory.getId()){
			List<Attribute> attributes = attributeDao.findByProductCategory(productCategory);
			attributeJa = this.createEntity().convertList(attributes, null);
		}
		return attributeJa;
	}

	/**
	 * 根据商品分类获取指定的商品属性Json列表数据
	 * @param productCategory
	 * @return
	 * @throws Exception
	 */
	public JSONArray findByProductCategoryAndCompany(ProductCategory productCategory) throws Exception{
		JSONArray attributeJa = new JSONArray();
		if (null != productCategory && null != productCategory.getId()){
			List<Attribute> attributes = attributeDao.findByProductCategory(productCategory);
			attributeJa = this.createEntity().convertList(attributes, null);
		}
		return attributeJa;
	}

	/**
	 * 商品属性回显
	 * 根据商品获取指定的商品属性Json列表数据
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonListByProduct(Product product) throws Exception{
		ProductCategory productCategory = product.getProductCategory();
		if(productCategory.getAttributes().isEmpty()){
			productCategory = productCategory.getParent();
		}
		JSONArray attributeJa = new JSONArray();
		if (null != productCategory && null != productCategory.getId()){
			List<Attribute> attributes = attributeDao.findByProductCategory(productCategory);
			for (Attribute attribute : attributes) {
				JSONObject attributeJo = new JSONObject();
				attributeJo.put("id", attribute.getId());
				attributeJo.put("name", attribute.getName());
				JSONArray ja = new JSONArray();
				for (String option : attribute.getOptions()){
					JSONObject opJo = new JSONObject();
					opJo.put("option", option);
					ja.add(opJo);
				}
				attributeJo.put("options", ja);
				if(StringUtils.isNotEmpty(product.getAttributeValue(attribute))){
					attributeJo.put("value", product.getAttributeValue(attribute));
				} else {
					attributeJo.put("value", "");
				}
				attributeJa.add(attributeJo);
			}
		}
		return attributeJa;
	}

}