package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.PaymentMethodDao;
import com.xss.domain.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *  支付方式Service
 * @author fan.hu
 * @since 2018-08-06
 */

@Service
public class PaymentMethodService extends BaseService<PaymentMethod, Long> {

    @Autowired
    private PaymentMethodDao paymentMethodDao;

    @Resource
    public void setBaseDao(PaymentMethodDao paymentMethodDao) {
        super.setBaseDao(paymentMethodDao);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PaymentMethod findById(Long id){
        return paymentMethodDao.findOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(PaymentMethod paymentMethod){
        paymentMethodDao.save(paymentMethod);
    }

    public JSONObject getMethod(){
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        for (PaymentMethod.Method method : PaymentMethod.Method.values()) {
            ja.add(method.name());
        }
        jo.put("data",ja);
        return jo;
    }

    public PaymentMethodDao getPaymentMethodDao() {
        return paymentMethodDao;
    }

    public void setPaymentMethodDao(PaymentMethodDao paymentMethodDao) {
        this.paymentMethodDao = paymentMethodDao;
    }
}
