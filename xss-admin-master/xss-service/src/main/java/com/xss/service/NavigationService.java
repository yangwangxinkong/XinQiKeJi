package com.xss.service;

import com.xss.dao.NavigationDao;
import com.xss.domain.Navigation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *  Service - 导航
 * @author zzl
 * @date 2018/8/2
 */
@Service
public class NavigationService extends BaseService<Navigation,Long> {
    @Resource
    NavigationDao navigationDao;

    @Resource
    public void setBaseDao(NavigationDao navigationDao) {
        super.setBaseDao(navigationDao);
    }

    public NavigationDao getNavigationDao() {
        return navigationDao;
    }

    public void setNavigationDao(NavigationDao navigationDao) {
        this.navigationDao = navigationDao;
    }
}

