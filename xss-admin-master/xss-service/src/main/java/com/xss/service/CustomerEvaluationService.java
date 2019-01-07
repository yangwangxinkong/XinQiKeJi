package com.xss.service;

import com.xss.dao.CustomerEvaluationDao;
import com.xss.domain.CustomerEvaluation;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

@Service
public class CustomerEvaluationService extends BaseService<CustomerEvaluation,Long> {
    @PersistenceContext
    protected EntityManager entityManager;

    @Resource
    private CustomerEvaluationDao customerEvaluationDao;

    //将当前所需要的dao类配置给当前的service
    @Resource
    public void setBaseDao(CustomerEvaluationDao customerEvaluationDao) {
        super.setBaseDao(customerEvaluationDao);
    }

    public CustomerEvaluationDao getCustomerEvaluationDao() {
        return customerEvaluationDao;
    }

    public void setCustomerEvaluationDao(CustomerEvaluationDao customerEvaluationDao) {
        this.customerEvaluationDao = customerEvaluationDao;
    }

    public CustomerEvaluation saveCustomerEvaluation(CustomerEvaluation customerEvaluation) throws IOException , TemplateException {
        /*添加创建时间和修改时间*/
        /*执行保存到数据库*/
        super.persist(customerEvaluation);
        return customerEvaluation;
    }

}
