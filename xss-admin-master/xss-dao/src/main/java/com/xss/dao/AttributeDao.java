/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Attribute;
import com.xss.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dao - 商品属性
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface AttributeDao extends JpaRepository<Attribute, Long> {

    List<Attribute> findByProductCategory(ProductCategory productCategory);

}