package com.xss.service;

import com.xss.dao.ServiceFeeSettingDao;
import com.xss.domain.Quotation;
import com.xss.domain.ServiceFeeSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 *  Service - 服务费配置
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class ServiceFeeSettingService extends BaseService<ServiceFeeSetting,Long> {
    @Resource
    private ServiceFeeSettingDao serviceFeeSettingDao;
    @Resource
    public void setBaseDao(ServiceFeeSettingDao serviceFeeSettingDao) {
        super.setBaseDao(serviceFeeSettingDao);
    }

    public ServiceFeeSettingDao getServiceFeeSettingDao() {
        return serviceFeeSettingDao;
    }

    public void setServiceFeeSettingDao(ServiceFeeSettingDao serviceFeeSettingDao) {
        this.serviceFeeSettingDao = serviceFeeSettingDao;
    }

    @Transactional(readOnly = true)
    public ServiceFeeSetting getServiceFeeSettings(Quotation quotation) {

        String jpql = "select serviceFeeSetting from ServiceFeeSetting serviceFeeSetting " +
                "where serviceFeeSetting.city.id = :cityId AND serviceFeeSetting.feeCategory = :feeCategory" +
                " AND serviceFeeSetting.payCategory = :payCategory AND serviceFeeSetting.monthCount = :monthCount ";
        List<ServiceFeeSetting> serviceFeeSettings =  entityManager.createQuery(jpql, ServiceFeeSetting.class)
                .setFlushMode(FlushModeType.COMMIT).setParameter("cityId", quotation.getCity().getId())
                .setParameter("feeCategory", quotation.getFeeCategory())
                .setParameter("payCategory", quotation.getPayCategory())
                .setParameter("monthCount", quotation.getMonthCount())
                .getResultList();
        if(serviceFeeSettings != null && serviceFeeSettings.size() > 0) {
            return serviceFeeSettings.get(0);
        } else {
            return null;
        }
    }
}
