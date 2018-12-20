/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.ProductCategoryDao;
import com.xss.domain.Product;
import com.xss.domain.ProductCategory;
import com.xss.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service - 商品分类
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class ProductCategoryService extends BaseService<ProductCategory, Long>{

	@Resource
	private ProductCategoryDao productCategoryDao;

	@Resource
	public void setBaseDao(ProductCategoryDao productCategoryDao) {
		super.setBaseDao(productCategoryDao);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots() {
		return findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory) {
		return findParents(productCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findTree() {
		return findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory) {
		return findChildren(productCategory, null);
	}

	@Transactional
	public void save(ProductCategory productCategory) {
		persist(productCategory);
	}

	public List<ProductCategory> findRoots(Integer count) {
		String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent is null order by productCategory.order asc";
		TypedQuery<ProductCategory> query = entityManager.createQuery(jpql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional
	public ProductCategory findByCode(String code) {
		String jpql = "select productCategory from ProductCategory productCategory where lower(productCategory.code) = lower(:code)";
		List<ProductCategory> productCategorys = entityManager.createQuery(jpql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getResultList();
		if(productCategorys.size()>0){
			return productCategorys.get(0);
		}else{
			return null;
		}
	}

	@Transactional
	public List<ProductCategory> findParents(ProductCategory productCategory, Integer count) {
		if (productCategory == null || productCategory.getParent() == null) {
			return Collections.<ProductCategory> emptyList();
		}
		String jpql = "select productCategory from ProductCategory productCategory where productCategory.id in (:ids) order by productCategory.grade asc";
		TypedQuery<ProductCategory> query = entityManager.createQuery(jpql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", productCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional
	public List<ProductCategory> findChildren(ProductCategory productCategory, Integer count) {
		TypedQuery<ProductCategory> query;
		if (productCategory != null) {
			String jpql = "select productCategory from ProductCategory productCategory where productCategory.treePath like :treePath order by productCategory.order asc";
			query = entityManager.createQuery(jpql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select productCategory from ProductCategory productCategory order by productCategory.order asc";
			query = entityManager.createQuery(jpql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), productCategory);
	}

	@Transactional
	public List<ProductCategory> findListByWholesale(Integer count){
		StringBuffer sb = new StringBuffer();
		sb.append("select * from xx_product_category pc ");
		sb.append(" where pc.id in(select p.product_category from xx_product p where p.product_type=1 and p.is_list=1 and p.is_marketable=1 GROUP BY p.product_category ORDER BY COUNT(p.id) DESC)");
		if (null != count) {
			sb.append(" LIMIT ").append(count);
		}
		Query query = entityManager.createNativeQuery(sb.toString(), ProductCategory.class).setFlushMode(FlushModeType.COMMIT);

		return query.getResultList();
	}
	/**
	 * 设置treePath、grade并保存
	 *
	 * @param productCategory
	 *            商品分类
	 */
	@Override
	public void persist(ProductCategory productCategory) {
		Assert.notNull(productCategory);
		setValue(productCategory);
		super.persist(productCategory);
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param productCategory
	 *            商品分类
	 * @return 商品分类
	 */
	@Override
	public ProductCategory merge(ProductCategory productCategory) {
		Assert.notNull(productCategory);
		setValue(productCategory);
		for (ProductCategory category : findChildren(productCategory, null)) {
			setValue(category);
		}
		return super.merge(productCategory);
	}

	/**
	 * 清除商品属性值并删除
	 *
	 * @param productCategory
	 *            商品分类
	 */
	@Override
	public void remove(ProductCategory productCategory) {
		if (productCategory != null) {
			StringBuffer jpql = new StringBuffer("update Product product set ");
			for (int i = 0; i < Product.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
				String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
				if (i == 0) {
					jpql.append("product." + propertyName + " = null");
				} else {
					jpql.append(", product." + propertyName + " = null");
				}
			}
			jpql.append(" where product.productCategory = :productCategory");
			entityManager.createQuery(jpql.toString()).setFlushMode(FlushModeType.COMMIT).setParameter("productCategory", productCategory).executeUpdate();
			super.remove(productCategory);
		}
	}

	/**
	 * 排序商品分类
	 *
	 * @param productCategories
	 *            商品分类
	 * @param parent
	 *            上级商品分类
	 * @return 商品分类
	 */
	private List<ProductCategory> sort(List<ProductCategory> productCategories, ProductCategory parent) {
		List<ProductCategory> result = new ArrayList<ProductCategory>();
		if (productCategories != null) {
			for (ProductCategory productCategory : productCategories) {
				if ((productCategory.getParent() != null && productCategory.getParent().equals(parent)) || (productCategory.getParent() == null && parent == null)) {
					result.add(productCategory);
					result.addAll(sort(productCategories, productCategory));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 *
	 * @param productCategory
	 *            商品分类
	 */
	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null && null != parent.getId()) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getTreePaths().size());
	}

	public JSONArray convertProductCategories(List<ProductCategory> productCategories){
		JSONArray array = new JSONArray();
		if (null != productCategories && !productCategories.isEmpty()){
			for (ProductCategory productCategory : productCategories){
				JSONObject jo = convertProductCategory(productCategory);
				jo.put("children", convertProductCategories(new ArrayList<ProductCategory>(productCategory.getChildren())));
				array.add(jo);
			}
		}
		return array;
	}
	public JSONObject convertProductCategory(ProductCategory productCategory){
		JSONObject jo = JsonUtil.toJSONObject(productCategory, new String[]{"id", "name", "grade", "order"});
		jo.put("path",productCategory.getPath());
		if (null != productCategory.getParent()){
			jo.put("parent", productCategory.getParent().getId());
		}
		return jo;
	}

	public boolean codeExists(String code) {
		if (code == null) {
			return false;
		}
		String jpql = "select count(*) from ProductCategory productCategory where lower(productCategory.code) = lower(:code)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		return count > 0;
	}
}