/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.ParameterGroup;
import com.xss.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dao - 商品参数组
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface ParameterGroupDao extends JpaRepository<ParameterGroup, Long> {
    List<ParameterGroup> findByProductCategory(ProductCategory productCategory);

}