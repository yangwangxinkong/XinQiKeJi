/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.ParameterGroupDao;
import com.xss.domain.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service - 商品参数组
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class ParameterGroupService extends BaseService<ParameterGroup, Long>{

	@Resource
	private ParameterGroupDao parameterGroupDao;
	@Resource
	private ParameterService parameterService;

	@Resource
	public void setBaseDao(ParameterGroupDao parameterGroupDao) {
		super.setBaseDao(parameterGroupDao);
	}
	/**
	 * 处理商品参数并更新
	 *
	 * @param parameterGroup
	 *            参数组
	 * @return 参数组
	 */
	@Override
	public ParameterGroup merge(ParameterGroup parameterGroup) {
		Assert.notNull(parameterGroup);

		Set<Parameter> excludes = new HashSet<Parameter>();
		CollectionUtils.select(parameterGroup.getParameters(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Parameter parameter = (Parameter) object;
				return parameter != null && parameter.getId() != null;
			}
		}, excludes);
		List<Parameter> parameters = parameterService.findList(parameterGroup, excludes);
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			String jpql = "select product from Product product join product.parameterValue parameterValue where index(parameterValue) = :parameter";
			List<Product> products = entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("parameter", parameter).getResultList();
			for (Product product : products) {
				product.getParameterValue().remove(parameter);
				if (i % 20 == 0) {
					super.flush();
					super.clear();
				}
			}
		}
		return super.merge(parameterGroup);
	}

	/**
	 * 处理商品参数并删除
	 *
	 * @param parameterGroup
	 *            参数组
	 */
	@Override
	public void remove(ParameterGroup parameterGroup) {
		if (parameterGroup != null) {
			for (int i = 0; i < parameterGroup.getParameters().size(); i++) {
				Parameter parameter = parameterGroup.getParameters().get(i);
				String jpql = "select product from Product product join product.parameterValue parameterValue where index(parameterValue) = :parameter";
				List<Product> products = entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("parameter", parameter).getResultList();
				for (Product product : products) {
					product.getParameterValue().remove(parameter);
					if (i % 20 == 0) {
						super.flush();
						super.clear();
					}
				}
			}
			super.remove(super.merge(parameterGroup));
		}
	}

	/**
	 * 根据商品分类获取指定的商品参数Json列表数据
	 * @param productCategory
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonListByProductCategory(ProductCategory productCategory) throws Exception{
		JSONArray parameterGroupJa = new JSONArray();
		if (null != productCategory && null != productCategory.getId()){
			List<ParameterGroup> parameterGroups = parameterGroupDao.findByProductCategory(productCategory);
			parameterGroupJa = this.createEntity().convertList(parameterGroups, null);
		}
		return parameterGroupJa;
	}
	/**
	 * 根据商品分类获取指定的商品参数Json列表数据
	 * @param productCategory
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonListByProductCategoryAndCampany(ProductCategory productCategory) throws Exception{
		JSONArray parameterGroupJa = new JSONArray();
		if (null != productCategory && null != productCategory.getId()){
			List<ParameterGroup> parameterGroups = parameterGroupDao.findByProductCategory(productCategory);
			parameterGroupJa = this.createEntity().convertList(parameterGroups, null);
		}
		return parameterGroupJa;
	}

	/**
	 * 商品参数回显
	 * 根据商品分类获取指定的商品参数Json列表数据
	 * @param productCategory
	 * @return
	 * @throws Exception
	 */
	public JSONArray getJsonListByProductCategory(ProductCategory productCategory, Map<Parameter, String> parameterMap) throws Exception{
		JSONArray parameterGroupJa = new JSONArray();
		if(productCategory.getParameterGroups().isEmpty()){
			productCategory = productCategory.getParent();
		}
		if (null != productCategory && null != productCategory.getId()){
			List<ParameterGroup> parameterGroups = parameterGroupDao.findByProductCategory(productCategory);
			for (ParameterGroup pg : parameterGroups) {
				JSONObject parameterGroupJo = new JSONObject();
				parameterGroupJo.put("id",pg.getId());
				parameterGroupJo.put("name",pg.getName());
				JSONArray parameterJa = new JSONArray();
				for (Parameter parameter : pg.getParameters()) {
					JSONObject pJo = new JSONObject();
					pJo.put("id", parameter.getId());
					pJo.put("name", parameter.getName());
					if(StringUtils.isNotEmpty(parameterMap.get(parameter))){
						pJo.put("value", parameterMap.get(parameter));
					} else {
						pJo.put("value", "");
					}
					parameterJa.add(pJo);
				}
				parameterGroupJo.put("parameters",parameterJa);
				parameterGroupJa.add(parameterGroupJo);
			}
		}
		return parameterGroupJa;
	}


}