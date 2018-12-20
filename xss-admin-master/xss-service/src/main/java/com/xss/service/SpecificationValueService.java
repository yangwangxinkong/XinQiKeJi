/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.SpecificationValueDao;
import com.xss.domain.SpecificationValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 商品规格值
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class SpecificationValueService extends BaseService<SpecificationValue, Long>{

	@Resource
	private SpecificationValueDao specificationValueDao;

	@Resource
	public void setBaseDao(SpecificationValueDao specificationValueDao) {
		super.setBaseDao(specificationValueDao);
	}

}