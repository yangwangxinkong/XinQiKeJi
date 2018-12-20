package com.xss.dao;

import com.xss.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 购物车
 * @author Hu
 * @date 2018/8/30
 */
@Component
public interface CartDao extends JpaRepository<Cart,Long> {
}
