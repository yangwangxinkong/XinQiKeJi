package com.xss.dao;

import com.xss.domain.Configs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 系统配置
 * @author zzl
 * @date 2018/8/29
 */
@Component
public interface ConfigsDao extends JpaRepository<Configs,Long> {
    Configs findConfigsByCode(String code);
}
