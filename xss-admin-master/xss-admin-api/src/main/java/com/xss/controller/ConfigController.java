package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Configs;
import com.xss.service.ConfigsService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  系统配置管理接口
 * @author zzl
 * @date 2018/8/9
 */
@RestController
@RequestMapping("/api/config")
@Api(description="系统配置管理模块")
public class ConfigController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ConfigController.class);

    @Autowired
    ConfigsService configsService;

    @ApiOperation(value = "获取系统配置列表",notes = "获取系统配置列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取系统配置列表接口:/api/configs/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Configs list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Configs> configsPage = configsService.findPage(pageable);
            result = new PageResult<Object>((int)configsPage.getTotal(),configsPage.getPageNumber(),configsPage.getPageSize(), configsService.createEntity().convertList(configsPage.getContent(),new String[]{"configsPosition","type","beginDate","endDate","order"}));
        }catch (Exception e){
            LOG.error("get Configs list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "获取系统配置详情",notes = "获取系统配置详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取系统配置详情列表接口:/api/configs/info")
    public PublicResult<JSONObject> info(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            Configs configs = configsService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,configsService.createEntity().convertEntity(configs,new String[]{"intType","configsPosition","product","store","order","article","beginDate","endDate"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    @ApiOperation(value = "保存/更新系统配置",notes = "保存/更新系统配置",httpMethod = "POST",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新系统配置接口:/api/configs/save")
    public PublicResult<String> save(@RequestBody Configs configs){
        try {
            if (null == configs.getId()){
                Configs existConfig = configsService.getConfigsDao().findConfigsByCode(configs.getCode());
                if (null != existConfig && null != existConfig.getId()){
                    return new PublicResult<String>(PublicResultConstant.PARAM_ERROR,"编码已存在");
                }
                configsService.save(configs);
            }else {
                Configs existConfig = configsService.getConfigsDao().findConfigsByCode(configs.getCode());
                if (null != existConfig && null != existConfig.getId() && !configs.getId().equals(existConfig.getId())){
                    return new PublicResult<String>(PublicResultConstant.PARAM_ERROR,"编码已存在");
                }
                configsService.update(configs);
            }
            return new PublicResult<String>(PublicResultConstant.SUCCESS,"操作成功");
        }catch (Exception e){
            LOG.error("save configs  error. {}", e);
            return new PublicResult<String>(PublicResultConstant.ERROR,"操作失败");
        }
    }


    @ApiOperation(value = "批量删除系统配置",notes = "批量删除系统配置",httpMethod = "GET",produces = "application/json")
    @GetMapping("/delete")
    @Log(description="平台中心批量删除系统配置接口:/api/configs/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        LOG.debug("ids[] = " + ids);
        configsService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

}