package com.xss.service;

import com.xss.dao.SocialSecurityRatioSettingDao;
import com.xss.domain.City;
import com.xss.domain.ResidenceType;
import com.xss.domain.SocialSecurityRatioSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 *  Service - 社保比例配置
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class SocialSecurityRatioSettingService extends BaseService<SocialSecurityRatioSetting,Long> {
    @Resource
    private SocialSecurityRatioSettingDao socialSecurityRatioSettingDao;
    @Resource
    public void setBaseDao(SocialSecurityRatioSettingDao socialSecurityRatioSettingDao) {
        super.setBaseDao(socialSecurityRatioSettingDao);
    }

    public SocialSecurityRatioSettingDao getSocialSecurityRatioSettingDao() {
        return socialSecurityRatioSettingDao;
    }

    public void setSocialSecurityRatioSettingDao(SocialSecurityRatioSettingDao socialSecurityRatioSettingDao) {
        this.socialSecurityRatioSettingDao = socialSecurityRatioSettingDao;
    }

    @Transactional(readOnly = true)
    public List<SocialSecurityRatioSetting> getSocialSecurityRatioSetting(City city, ResidenceType residenceType) {
        if (city == null) {
            return null;
        }
        String jpql = "select socialSecurityRatioSetting from SocialSecurityRatioSetting socialSecurityRatioSetting where socialSecurityRatioSetting.city.id = :cityId and socialSecurityRatioSetting.residenceType.id = :residenceTypeId";
        return entityManager.createQuery(jpql, SocialSecurityRatioSetting.class).setFlushMode(FlushModeType.COMMIT).setParameter("cityId", city.getId()).setParameter("residenceTypeId", residenceType.getId()).getResultList();
    }
}
