package com.xss.dao;

import com.xss.domain.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 系统配置
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface OrderLogDao extends JpaRepository<OrderLog,Long> {
}
