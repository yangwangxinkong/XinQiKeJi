package com.xss.dao;

import com.xss.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 标签
 * @author Administrator
 * @date 2018/8/8
 */
@Component
public interface TagDao extends JpaRepository<Tag,Long> {
}
