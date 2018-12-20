package com.xss.dao;

import com.xss.domain.City;
import com.xss.domain.ResidenceType;
import com.xss.domain.SocialSecurityRatioSetting;
import com.xss.domain.enums.PayFrom;
import com.xss.domain.enums.SocialSecurityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  Dao - 社保比例配置
 * @author zzl
 * @date 2018/8/31
 */
@Component
public interface SocialSecurityRatioSettingDao extends JpaRepository<SocialSecurityRatioSetting,Long> {
    List<SocialSecurityRatioSetting> findByCityAndResidenceTypeAndSocialSecurityCategoryAndPayFrom(City city, ResidenceType residenceType, SocialSecurityCategory socialSecurityCategory, PayFrom payFrom);
}
