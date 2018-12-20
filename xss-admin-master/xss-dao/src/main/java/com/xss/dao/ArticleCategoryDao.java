/*
 *  
 *  
 *  
 */
package com.xss.dao;

import com.xss.domain.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 文章分类
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface ArticleCategoryDao extends JpaRepository<ArticleCategory, Long> {

    ArticleCategory findArticleCategoryByCode(String code);
}