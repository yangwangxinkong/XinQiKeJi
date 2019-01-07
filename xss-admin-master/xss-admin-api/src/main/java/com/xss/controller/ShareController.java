package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Ad;
import com.xss.domain.AdPosition;
import com.xss.domain.Share;
import com.xss.domain.enums.ShareCategory;
import com.xss.service.AdPositionService;
import com.xss.service.AdService;
import com.xss.service.ArticleService;
import com.xss.service.ShareService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 *  分享管理接口
 * @author zzl
 * @date 2018/12/15
 */
@RestController
@RequestMapping("/api/share")
@Api(description="分享管理模块")
public class ShareController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ShareController.class);

    @Autowired
    private ShareService shareService;


    @ApiOperation(value = "获取分享列表",notes = "获取分享列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取分享列表接口:/api/share/list")
    public PageResult<Object> list(Pageable pageable, String shareMemberMobile, ShareCategory shareCategory)throws Exception{
        LOG.debug("get Share list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            if (StringUtils.isNotBlank(shareMemberMobile)){
                pageable.getFilters().add(Filter.like("shareMemberMobile", "%" + shareMemberMobile + "%"));
            }
            if (null != shareCategory){
                pageable.getFilters().add(Filter.eq("shareCategory", shareCategory));
            }
            Page<Share> page = shareService.findPage(pageable);
            result = new PageResult<Object>((int)page.getTotal(),page.getPageNumber(),page.getPageSize(), shareService.createEntity().convertList(page.getContent(), null));
        }catch (Exception e){
            LOG.error("get Share list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

}