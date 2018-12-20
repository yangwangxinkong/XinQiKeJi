package com.xss.dao;

import com.xss.domain.City;
import com.xss.domain.ServiceFeeSetting;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayCategory;
import com.xss.domain.enums.PayFrom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  Dao - 服务费配置
 * @author zzl
 * @date 2018/8/31
 */
@Component
public interface ServiceFeeSettingDao extends JpaRepository<ServiceFeeSetting,Long> {
    List<ServiceFeeSetting> findByCityAndFeeCategoryAndPayCategoryAndMonthCount(City city, FeeCategory feeCategory, PayCategory payCategory, Integer monthCount);
}
