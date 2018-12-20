/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dao - 收款单
 * 
 * @author hu
 * @version 1.0
 */
public interface PaymentDao extends JpaRepository<Payment, Long> {

}