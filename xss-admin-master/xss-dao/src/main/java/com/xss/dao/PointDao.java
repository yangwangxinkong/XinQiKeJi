package com.xss.dao;

import com.xss.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 积分
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface PointDao extends JpaRepository<Point, Long> {


}