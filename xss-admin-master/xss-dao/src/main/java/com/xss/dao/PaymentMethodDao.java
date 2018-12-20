/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 支付方法
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface PaymentMethodDao extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByCode(String code);
}