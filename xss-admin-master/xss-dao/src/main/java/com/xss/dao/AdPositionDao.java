package com.xss.dao;

import com.xss.domain.AdPosition;
import com.xss.domain.enums.AdPositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 广告位
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface AdPositionDao extends JpaRepository<AdPosition,Long> {
    AdPosition findAdPositionByType(AdPositionType type);
}
