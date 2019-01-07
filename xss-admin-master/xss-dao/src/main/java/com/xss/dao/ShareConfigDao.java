package com.xss.dao;

import com.xss.domain.AdPosition;
import com.xss.domain.ShareConfig;
import com.xss.domain.enums.AdPositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 分享描述持久层
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface ShareConfigDao extends JpaRepository<ShareConfig,Long> {
}
