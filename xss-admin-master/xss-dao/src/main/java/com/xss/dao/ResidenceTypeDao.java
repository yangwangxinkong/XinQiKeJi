/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.ResidenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 户口性质
 * 
 * @author hu
 * @version 1.0
 */
@Component
public interface ResidenceTypeDao extends JpaRepository<ResidenceType, Long> {

}