/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.ParameterDao;
import com.xss.domain.Parameter;
import com.xss.domain.ParameterGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Service - 商品参数
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class ParameterService extends BaseService<Parameter, Long>{

	@Resource
	private ParameterDao parameterDao;

	@Resource
	public void setBaseDao(ParameterDao parameterDao) {
		super.setBaseDao(parameterDao);
	}

	public List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Parameter> criteriaQuery = criteriaBuilder.createQuery(Parameter.class);
		Root<Parameter> root = criteriaQuery.from(Parameter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (parameterGroup != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parameterGroup"), parameterGroup));
		}
		if (excludes != null && !excludes.isEmpty()) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(root.in(excludes)));
		}
		criteriaQuery.where(restrictions);
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

}