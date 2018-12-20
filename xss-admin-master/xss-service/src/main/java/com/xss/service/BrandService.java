/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.BrandDao;
import com.xss.domain.Brand;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 品牌
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class BrandService extends BaseService<Brand, Long>{

	@Resource
	private BrandDao brandDao;

	@Resource
	public void setBaseDao(BrandDao brandDao) {
		super.setBaseDao(brandDao);
	}

}