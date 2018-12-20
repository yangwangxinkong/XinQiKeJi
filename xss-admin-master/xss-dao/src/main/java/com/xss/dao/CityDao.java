/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.Area;
import com.xss.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * Dao - 城市
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface CityDao extends JpaRepository<City, Long> {

}