/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.ResidenceTypeDao;
import com.xss.domain.ResidenceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 * Service - 户口性质
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class ResidenceTypeService extends BaseService<ResidenceType, Long> {

	@Resource
	public void setBaseDao(ResidenceTypeDao residenceTypeDao) {
		super.setBaseDao(residenceTypeDao);
	}

	@Transactional(readOnly = true)
	public List<ResidenceType> getResidenceTypeByCityId(Long id) {
		if (id == null) {
			return null;
		}
		String jpql = "select residenceType from ResidenceType residenceType where residenceType.city.id = :id";
		return entityManager.createQuery(jpql, ResidenceType.class).setFlushMode(FlushModeType.COMMIT).setParameter("id", id).getResultList();
	}

}