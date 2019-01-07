package com.xss.service;

import com.xss.dao.ProposerDao;
import com.xss.domain.Proposer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  Service - 申请人
 * @author zzl
 * @date 2018/12/15
 */
@Service
public class ProposerService extends BaseService<Proposer,Long> {

    @Resource
    private ProposerDao proposerDao;
    @Resource
    public void setBaseDao(ProposerDao proposerDao) {
        super.setBaseDao(proposerDao);
    }

    public ProposerDao getProposerDao() {
        return proposerDao;
    }

    public void setProposerDao(ProposerDao proposerDao) {
        this.proposerDao = proposerDao;
    }

}
