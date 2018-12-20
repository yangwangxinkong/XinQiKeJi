/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 地区管理
 * 
 * @author fan.hu
 * @version 1.0
 */
@Component
public interface ReviewDao extends JpaRepository<Review, Long> {

}