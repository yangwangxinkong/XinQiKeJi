package com.xss.dao;

import com.xss.domain.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

/**
 * Created by huperson on 2018/08/03.
 */
@Component
public interface CouponCodeDao extends JpaRepository<CouponCode, Long> {

    @Query(value = "select count(*)>0 from CouponCode couponCode where lower(couponCode.code) = lower(:code)")
    boolean codeExists(String code);

    @Query(value = "select couponCode from CouponCode couponCode where lower(couponCode.code) = lower(:code)")
    CouponCode findByCode(String code);



}
