package com.xss.controller;

import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.domain.Proposer;
import com.xss.domain.Share;
import com.xss.service.ProposerService;
import com.xss.service.ShareService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  申请人管理接口
 * @author zzl
 * @date 2018/12/15
 */
@RestController
@RequestMapping("/api/proposer")
@Api(description="申请人管理模块")
public class ProposerController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ProposerController.class);

    @Autowired
    private ProposerService proposerService;


    @ApiOperation(value = "获取申请人列表",notes = "获取申请人列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取申请人列表接口:/api/share/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Proposer list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Proposer> page = proposerService.findPage(pageable);
            result = new PageResult<Object>((int)page.getTotal(),page.getPageNumber(),page.getPageSize(), proposerService.createEntity().convertList(page.getContent(), null));
        }catch (Exception e){
            LOG.error("get Proposer list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

}