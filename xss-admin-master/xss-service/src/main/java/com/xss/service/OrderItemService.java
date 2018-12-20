package com.xss.service;

import com.xss.dao.OrderItemDao;
import com.xss.domain.OrderItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by huperson on 2018/08/08.
 */
@Service
public class OrderItemService extends BaseService<OrderItem, Long> {

    @Resource
    public void setBaseDao(OrderItemDao orderItemDao) {
        super.setBaseDao(orderItemDao);
    }

}
