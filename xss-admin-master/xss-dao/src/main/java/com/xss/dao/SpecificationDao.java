/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.ProductCategory;
import com.xss.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dao - 商品规格
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface SpecificationDao extends JpaRepository<Specification, Long> {
    List<Specification> findByProductCategory(ProductCategory productCategory);
}