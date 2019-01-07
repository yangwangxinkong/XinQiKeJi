package com.xss.dao;

import com.xss.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 用户反馈
 * @author zzl
 * @date 2018/12/15
 */
@Component
public interface FeedbackDao extends JpaRepository<Feedback,Long> {
}
