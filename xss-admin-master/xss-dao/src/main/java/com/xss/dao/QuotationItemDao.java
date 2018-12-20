/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 报价单
 * 
 * @author hu
 * @version 1.0
 */
@Component
public interface QuotationItemDao extends JpaRepository<QuotationItem, Long> {

}