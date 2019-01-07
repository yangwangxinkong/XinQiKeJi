package com.xss.dao;

import com.xss.domain.ShareLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 分享操作日志
 * @author zzl
 * @date 2018/12/15
 */
@Component
public interface ShareLogDao extends JpaRepository<ShareLog,Long> {
}
