package com.xss.dao;

import com.xss.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 分享
 * @author zzl
 * @date 2018/12/15
 */
@Component
public interface ShareDao extends JpaRepository<Share,Long> {

    Share findBySharedMemberId(Long shareMemberId);
}
