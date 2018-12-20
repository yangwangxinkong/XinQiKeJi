/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 品牌
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface BrandDao extends JpaRepository<Brand, Long> {

}