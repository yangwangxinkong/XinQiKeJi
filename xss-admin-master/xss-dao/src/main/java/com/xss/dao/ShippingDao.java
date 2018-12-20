/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dao - 退款单
 * 
 * @author hu
 * @version 1.0
 */
public interface ShippingDao extends JpaRepository<Shipping, Long> {

}