package com.xss.dao;

import com.xss.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 文章
 * @author Administrator
 * @date 2018/8/7
 */
@Component
public interface ArticleDao extends JpaRepository<Article,Long> {
    Article findByCode(String code);
}
