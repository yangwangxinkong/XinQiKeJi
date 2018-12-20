package com.xss.dao;

import com.xss.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 广告
 * @author Huang
 * @date 2018/8/9
 */
@Component
public interface GoodsDao extends JpaRepository<Goods,Long> {
}
