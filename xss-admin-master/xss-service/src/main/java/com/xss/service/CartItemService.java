package com.xss.service;

import com.xss.dao.CartItemDao;
import com.xss.domain.CartItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CartItemService extends BaseService<CartItem, Long> {

    @Resource
    private CartItemDao cartItemDao;
    @Resource
    public void setBaseDao(CartItemDao cartItemDao) {
        super.setBaseDao(cartItemDao);
    }
}
