package com.xss.dao;

import com.xss.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 广告
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface AdDao extends JpaRepository<Ad,Long> {
}
