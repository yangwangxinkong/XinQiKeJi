package com.xss.dao;

import com.xss.domain.CustomerEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 客户评价
 * @author Administrator
 * @date 2018/8/7
 */
@Component
public interface CustomerEvaluationDao extends JpaRepository<CustomerEvaluation,Long> {
}
