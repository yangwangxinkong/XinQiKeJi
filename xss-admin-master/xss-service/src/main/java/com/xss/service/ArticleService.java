package com.xss.service;

import com.xss.dao.ArticleDao;
import com.xss.domain.Article;
import com.xss.domain.ArticleCategory;
import com.xss.domain.Order;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 *  Service - 文章
 * @author zzl
 * @date 2018/8/7
 */
@Service
public class ArticleService extends BaseService<Article,Long> {
    @Resource
    private ArticleDao articleDao;
    @Resource
    public void setBaseDao(ArticleDao articleDao) {super.setBaseDao(articleDao);
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Transactional(readOnly = true)
    public Page<Article> findPage(ArticleCategory articleCategory, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
        Root<Article> root = criteriaQuery.from(Article.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != articleCategory) {
            if (null != articleCategory.getId()) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                        //root.get("articleCategory").get("parent").isNull(),
                        criteriaBuilder.equal(root.get("articleCategory").get("id"), articleCategory.getId()),
                        criteriaBuilder.like(root.get("articleCategory").<String>get("treePath"), "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%")));
            }
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Transactional(readOnly = true)
    public List<ArticleCategory> findArticles(Integer count) {
        String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.order asc";
        TypedQuery<ArticleCategory> query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }
}
