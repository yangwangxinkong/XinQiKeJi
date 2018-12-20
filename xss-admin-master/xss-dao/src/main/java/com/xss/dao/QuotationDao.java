/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 报价单
 * 
 * @author hu
 * @version 1.0
 */
@Component
public interface QuotationDao extends JpaRepository<Quotation, Long> {

}