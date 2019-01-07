package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.ShareConfig;
import com.xss.service.ShareConfigService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/shareConfig")
public class ShareConfigController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ShareConfigController.class);

    @Autowired
    private ShareConfigService shareConfigService;

    /*分享信息修改与保存*/
    @Log(description="平台中心保存客户评价列表接口:/api/shareConfig/save")
    @PostMapping("/save")
    public PublicResult<Object> save(ShareConfig shareConfig){
        LOG.debug("=====debug：：：分享信息修改与保存=====");
        //数据的有效性验证
        if (shareConfig.getShareTitle().isEmpty() || shareConfig.getShareTitle().length() >= 30){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"分享信息的标题不能为空，并且不能超过30个字");
        }
        if (shareConfig.getShareDesc().isEmpty()){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"分享信息描述不能为空");
        }
        if (shareConfig.getShareImg().isEmpty()){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"分享图标不能为空");
        }
        if (shareConfig.getShareTypes() == null){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"分享类型不能为空");
        }

        //保存分享信息
        try {
            //判断请求中是否有主键ID，如果没有就保存，反则根据ID修改分享信息
            if (shareConfig.getId() == null) {
                /*判断当前要添加的分享信息类型是否存在，每种类型只能存在一条数据，如果存在就返回保存失败*/
                ShareConfig shareConfigs = null;
                try {
                    /*在保存之前先查询是否存在此种类型的分享信息，有则不予添加，没有就添加*/
                    shareConfigs = shareConfigService.findShareConfigByType(shareConfig.getShareTypes());
                    return new PublicResult<Object>(PublicResultConstant.ERROR, "当前类型分享信息以存在");
                } catch (Exception e) {
                    //执行保存
                    shareConfigService.save(shareConfig);
                    return new PublicResult<Object>(PublicResultConstant.SUCCESS, "分享信息保存成功");
                }
            }else {
                //修改客户评价
                shareConfig.setModifyDate(new Date());
                ShareConfig updateResult = shareConfigService.update(shareConfig);
                return new PublicResult<Object>(PublicResultConstant.SUCCESS,updateResult);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CustomerEvaluationController Method save exception!!! params:"
                    + "分享标题" + shareConfig.getShareTitle()
                    + "分享内容" + shareConfig.getShareDesc()
                    + "分享图标" + shareConfig.getShareImg()
                    + "分享类型" + shareConfig.getShareTypes());
            return new PublicResult<Object>(PublicResultConstant.ERROR,"分享信息保存失败");
        }
    }

    //根据ID删除记录
    @GetMapping("/delete")
    public PublicResult<Object> CustomerDelete(Long... ids){
        if (ids == null) return new PublicResult<Object>(PublicResultConstant.ERROR,"id不能为空");
        try {
            //执行删除
            shareConfigService.delete(ids);
            return new PublicResult<Object>(PublicResultConstant.SUCCESS,"客户评价删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户评价删除失败");
        }
    }


    /*后台管理分享列表展示*/
    @ApiOperation(value="分享信息列表", notes="获取分享信息列表",produces = "application/json")
    @GetMapping(value="/list")
    @Log(description="平台中心获取客户评价列表接口:/api/shareConfig/list")
    public PageResult<Object> queryList(Pageable pageable)throws Exception{
        LOG.debug("获取客户评价列表参数 ==== " + pageable.toString());
        PageResult<Object> result = null;
        try {
            Page<ShareConfig> shareConfigPage = shareConfigService.findPage(pageable);
            /* customerServicePage.getTotal() ---->   获取总记录数
                customerServicePage.getPageNumber()---->获取页码
                customerServicePage.getPageSize() ----> 获取每页记录数
                customerServicePage.getContent()  -----> 获取内容
             */
            result = new PageResult<Object>((int)shareConfigPage.getTotal(),shareConfigPage.getPageNumber(),
                    shareConfigPage.getPageSize(),
                    shareConfigService.createEntity().convertList(shareConfigPage.getContent(),
                            new String[]{"id","createDate","modifyDate","shareTitle","shareDesc","shareImg","shareTypes"}));
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("获取分享信息列表失败");
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
            return result;
        }
    }

    /*,produces = "application/json,charset='utf-8'"*/
    @ApiOperation(value="分享信息详情", notes="通过id获取分享信息详情",produces = "application/json")
    @GetMapping(value="/info" )
    @Log(description="平台中心获取分享信息详情接口:/api/customerEvaluation/info")
    public PublicResult<JSONObject> getInfo(Long id)throws Exception{
        LOG.debug("id = "+id);
        try{
            ShareConfig shareConfig = shareConfigService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,shareConfigService.createEntity().convertEntity(shareConfig,new String[]{"id","shareTitle","shareDesc","shareImg","shareTypes"}));
        }catch (Exception e){
            LOG.error("get CustomerEvaluation info error. {}", e);
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,null);
        }
    }

}
