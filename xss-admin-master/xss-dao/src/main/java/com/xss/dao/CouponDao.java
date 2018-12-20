package com.xss.dao;

import com.xss.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CouponDao extends JpaRepository<Coupon, Long> {





}
