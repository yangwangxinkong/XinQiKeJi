package com.xss.service;

import com.xss.dao.InvoiceDao;
import com.xss.domain.Invoice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  Service - 发票
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class InvoiceService extends BaseService<Invoice,Long> {
    @Resource
    private InvoiceDao invoiceDao;
    @Resource
    public void setBaseDao(InvoiceDao invoiceDao) {
        super.setBaseDao(invoiceDao);
    }

    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }
}
