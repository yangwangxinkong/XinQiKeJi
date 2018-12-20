/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 订单
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface ProductDao extends JpaRepository<Product, Long> {
}