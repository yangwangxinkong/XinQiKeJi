package com.xss.service;

import com.xss.dao.AdDao;
import com.xss.domain.Ad;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *  Service - 广告
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class AdService extends BaseService<Ad,Long> {
    @Resource
    private AdDao adDao;
    @Resource
    public void setBaseDao(AdDao adDao) {
        super.setBaseDao(adDao);
    }

    public AdDao getAdDao() {
        return adDao;
    }

    public void setAdDao(AdDao adDao) {
        this.adDao = adDao;
    }
}
