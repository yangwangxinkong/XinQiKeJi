package com.xss.service;

import com.xss.dao.CouponDao;
import com.xss.domain.Coupon;
import com.xss.util.FreemarkerUtils;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CouponService extends BaseService<Coupon, Long>{


    @Resource(name = "couponDao")
    private CouponDao couponDao;

    @Resource(name = "couponDao")
    public void setBaseDao(CouponDao couponDao) {
        super.setBaseDao(couponDao);
    }

    @Transactional(readOnly = true)
    public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
        Root<Coupon> root = criteriaQuery.from(Coupon.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (isEnabled != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
        }
        if (isExchange != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchange"), isExchange));
        }
        if (hasExpired != null) {
            if (hasExpired) {
                restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("endDate"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("endDate").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("endDate"), new Date())));
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Transactional
    public Coupon saveCoupon(Coupon coupon) throws IOException, TemplateException {
        if (StringUtils.isNotEmpty(coupon.getPriceExpression())) {
            Map<String, Object> model = new HashMap<>();
            model.put("quantity", 111);
            model.put("price", new BigDecimal("9.99"));
            new BigDecimal(FreemarkerUtils.process("#{(" + coupon.getPriceExpression() + ");M50}", model));
        }
        if (!coupon.getIsExchange()) {
            coupon.setPoint(null);
        }
        coupon.setCouponCodes(null);
//        coupon.setPromotions(null);
        coupon.setOrders(null);
        super.persist(coupon);
        return coupon;
    }

    @Transactional
    public Coupon updateCoupon(Coupon coupon) throws IOException, TemplateException {

        if (StringUtils.isNotEmpty(coupon.getPriceExpression())) {
            Map<String, Object> model = new HashMap<>();
            model.put("quantity", 111);
            model.put("price", new BigDecimal("9.99"));
            new BigDecimal(FreemarkerUtils.process("#{(" + coupon.getPriceExpression() + ");M50}", model));
        }
        if (!coupon.getIsExchange()) {
            coupon.setPoint(null);
        }
        super.update(coupon, "couponCodes", "promotions", "orders");
        return coupon;
    }


}
