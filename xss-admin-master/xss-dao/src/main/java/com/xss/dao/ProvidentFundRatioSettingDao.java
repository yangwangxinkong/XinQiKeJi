package com.xss.dao;

import com.xss.domain.City;
import com.xss.domain.ProvidentFundRatioSetting;
import com.xss.domain.enums.PayFrom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  Dao - 公积金比例配置
 * @author zzl
 * @date 2018/8/31
 */
@Component
public interface ProvidentFundRatioSettingDao extends JpaRepository<ProvidentFundRatioSetting,Long> {
    List<ProvidentFundRatioSetting> findByCityAndPayFrom(City city, PayFrom payFrom);
}
