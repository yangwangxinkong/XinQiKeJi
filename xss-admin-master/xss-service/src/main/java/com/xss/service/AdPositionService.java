package com.xss.service;

import com.xss.dao.AdPositionDao;
import com.xss.domain.AdPosition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *  Service - 广告位
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class AdPositionService extends BaseService<AdPosition,Long> {
    @Resource
    private AdPositionDao adPositionDao;
    @PersistenceContext
    protected EntityManager entityManager;
    @Resource
    public void setBaseDao(AdPositionDao adPositionDao) {
        super.setBaseDao(adPositionDao);
    }

    public AdPositionDao getAdPositionDao() {
        return adPositionDao;
    }

    public void setAdPositionDao(AdPositionDao adPositionDao) {
        this.adPositionDao = adPositionDao;
    }
}
