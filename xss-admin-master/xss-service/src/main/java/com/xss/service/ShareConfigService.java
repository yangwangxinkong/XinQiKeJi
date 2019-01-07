package com.xss.service;

import com.xss.dao.ShareConfigDao;
import com.xss.domain.ShareConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

/**
 *  Service - 文章
 * @author zzl
 * @date 2018/8/7
 */
@Service
public class ShareConfigService extends BaseService<ShareConfig,Long> {
    @Resource
    private ShareConfigDao shareConfigDao;
    @Resource
    public void setBaseDao(ShareConfigDao shareConfigDao) {super.setBaseDao(shareConfigDao);
    }

    public ShareConfigDao getShareConfigDao() {
        return shareConfigDao;
    }

    public void setShareConfigDao(ShareConfigDao shareConfigDao) {
        this.shareConfigDao = shareConfigDao;
    }


    /*根据分型信息类型查询记录（每种类型只能有一条记录例如：0,1,2）*/
    @Transactional(readOnly = true)
    public ShareConfig findShareConfigByType(Integer shareConfigType) {
        String jpql = "select shareConfig from ShareConfig shareConfig where shareConfig.shareTypes=:type";
        TypedQuery<ShareConfig> query = entityManager.createQuery(jpql, ShareConfig.class)
                .setFlushMode(
                        FlushModeType.COMMIT);
        query.setParameter("type",shareConfigType);
        return query.getSingleResult();
    }
}
