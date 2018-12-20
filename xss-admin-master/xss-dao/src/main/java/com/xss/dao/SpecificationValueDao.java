/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.SpecificationValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 商品规格值
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface SpecificationValueDao extends JpaRepository<SpecificationValue, Long> {

}