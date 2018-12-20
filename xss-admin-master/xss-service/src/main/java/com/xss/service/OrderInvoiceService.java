package com.xss.service;

import com.xss.dao.InvoiceDao;
import com.xss.dao.OrderInvoiceDao;
import com.xss.domain.Invoice;
import com.xss.domain.OrderInvoice;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *  Service - 发票
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class OrderInvoiceService extends BaseService<OrderInvoice,Long> {
    @Resource
    private OrderInvoiceDao orderInvoiceDao;
    @Resource
    public void setBaseDao(OrderInvoiceDao orderInvoiceDao) {
        super.setBaseDao(orderInvoiceDao);
    }

    public OrderInvoiceDao getOrderInvoiceDao() {
        return orderInvoiceDao;
    }

    public void setOrderInvoiceDao(OrderInvoiceDao orderInvoiceDao) {
        this.orderInvoiceDao = orderInvoiceDao;
    }

    @Transactional(readOnly = true)
    public Page<OrderInvoice> findPage(OrderInvoice orderInvoice, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderInvoice> criteriaQuery = criteriaBuilder.createQuery(OrderInvoice.class);
        Root<OrderInvoice> root = criteriaQuery.from(OrderInvoice.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != orderInvoice) {

            if(orderInvoice.getInvoiceType() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("invoiceType"), orderInvoice.getInvoiceType()));
            }
            if(orderInvoice.getInvoiceCategory() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("invoiceCategory"), orderInvoice.getInvoiceCategory()));
            }
            if(orderInvoice.getInvoiceStatus() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("invoiceStatus"), orderInvoice.getInvoiceStatus()));
            }
            if (StringUtils.isNotBlank(orderInvoice.getInvoiceTitle())) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("invoiceTitle"), "%" + orderInvoice.getInvoiceTitle() + "%"));
            }
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}
