package com.xss.dao;

import com.xss.domain.PayBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 基数
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface PayBaseDao extends JpaRepository<PayBase,Long> {
}
