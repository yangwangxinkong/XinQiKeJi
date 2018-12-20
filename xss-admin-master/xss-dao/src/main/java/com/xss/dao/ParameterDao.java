/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 商品参数
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface ParameterDao extends JpaRepository<Parameter, Long> {

}