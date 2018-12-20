package com.xss.dao;

import com.xss.domain.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 订单发票
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface OrderInvoiceDao extends JpaRepository<OrderInvoice,Long> {
}
