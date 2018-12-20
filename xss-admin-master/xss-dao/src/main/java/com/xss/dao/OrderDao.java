/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.City;
import com.xss.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 订单
 * 
 * @author hu
 * @version 1.0
 */
@Component
public interface OrderDao extends JpaRepository<Order, Long> {

}