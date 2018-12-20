/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.PayBaseDao;
import com.xss.domain.PayBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 * Service - 基数
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class PayBaseService extends BaseService<PayBase, Long> {

	@Resource
	public void setBaseDao(PayBaseDao payBaseDao) {
		super.setBaseDao(payBaseDao);
	}

	@Transactional(readOnly = true)
	public PayBase getPayBaseByCityId(Long id) {
		if (id == null) {
			return null;
		}
		String jpql = "select payBase from PayBase payBase where payBase.city.id = :id";
		List<PayBase> payBases =  entityManager.createQuery(jpql, PayBase.class).setFlushMode(FlushModeType.COMMIT).setParameter("id", id).getResultList();
		if(payBases != null && payBases.size() > 0) {
			return payBases.get(0);
		} else {
			return null;
		}
	}
}