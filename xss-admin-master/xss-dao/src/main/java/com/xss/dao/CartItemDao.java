package com.xss.dao;

import com.xss.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 订单项
 * @author Hu
 * @date 2018/8/30
 */
@Component
public interface CartItemDao extends JpaRepository<CartItem,Long> {
}
