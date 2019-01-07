package com.xss.service;

import com.xss.dao.ShareLogDao;
import com.xss.domain.ShareLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  Service - 分享操作日志
 * @author zzl
 * @date 2018/12/15
 */
@Service
public class ShareLogService extends BaseService<ShareLog,Long> {
    @Resource
    private ShareLogDao shareLogDao;
    @Resource
    public void setBaseDao(ShareLogDao shareLogDao) {
        super.setBaseDao(shareLogDao);
    }

    public ShareLogDao getShareLogDao() {
        return shareLogDao;
    }

    public void setShareLogDao(ShareLogDao shareLogDao) {
        this.shareLogDao = shareLogDao;
    }
}
