/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.OrderCancelApply;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dao - 换货单
 * 
 * @author hu
 * @version 1.0
 */
public interface OrderCancelApplyDao extends JpaRepository<OrderCancelApply, Long> {

}