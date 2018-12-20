package com.xss.service;

import com.xss.dao.TagDao;
import com.xss.domain.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Service - 标签
 * @author zzl
 * @date 2018/8/8
 */
@Service
public class TagService extends BaseService<Tag,Long> {
    @Resource
    private TagDao tagDao;
    @PersistenceContext
    protected EntityManager entityManager;
    @Resource
    public void setBaseDao(TagDao tagDao) {
        super.setBaseDao(tagDao);
    }

    @Transactional(readOnly = true)
    public List<Tag> findList(Tag.Type type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        if (type != null) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
        }
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

    public List<Tag> findListByIDList(List<Long> ids){
        return tagDao.findAll(ids);
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }
}
