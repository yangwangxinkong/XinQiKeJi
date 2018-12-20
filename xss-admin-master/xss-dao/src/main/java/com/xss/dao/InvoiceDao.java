package com.xss.dao;

import com.xss.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 发票
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface InvoiceDao extends JpaRepository<Invoice,Long> {
}
