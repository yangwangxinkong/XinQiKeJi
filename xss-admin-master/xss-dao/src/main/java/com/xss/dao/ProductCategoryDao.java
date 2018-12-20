/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 商品分类
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Long> {

}