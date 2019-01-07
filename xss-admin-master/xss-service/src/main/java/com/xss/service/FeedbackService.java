package com.xss.service;

import com.xss.dao.FeedbackDao;
import com.xss.domain.Feedback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  Service - 用户反馈
 * @author zzl
 * @date 2018/12/16
 */
@Service
public class FeedbackService extends BaseService<Feedback,Long> {
    @Resource
    private FeedbackDao feedbackDao;
    @Resource
    public void setBaseDao(FeedbackDao feedbackDao) {
        super.setBaseDao(feedbackDao);
    }

    public FeedbackDao getFeedbackDao() {
        return feedbackDao;
    }

    public void setFeedbackDao(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }
}
