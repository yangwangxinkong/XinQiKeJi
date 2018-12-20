package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.ProvidentFundRatioSettingDao;
import com.xss.domain.ProvidentFundRatioSetting;
import com.xss.domain.enums.PayFrom;
import com.xss.service.CityService;
import com.xss.service.ProvidentFundRatioSettingService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  公积金比例配置管理接口
 * @author Hu
 * @date 2018/9/9
 */
@RestController
@RequestMapping("/api/providentFundRatioSetting")
@Api(description="公积金比例配置管理模块")
public class ProvidentFundRatioSettingController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ProvidentFundRatioSettingController.class);

    @Resource
    private ProvidentFundRatioSettingDao providentFundRatioSettingDao;
    @Autowired
    private ProvidentFundRatioSettingService providentFundRatioSettingService;
    @Autowired
    private CityService cityService;

    @ApiOperation(value = "获取公积金比例配置列表",notes = "获取公积金比例配置列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取公积金比例配置列表接口:/api/providentFundRatioSetting/list")
    public PageResult<Object> list(Pageable pageable, String cityCode, Integer payFromId)throws Exception{
        LOG.debug("get providentFundRatioSetting list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{

            List<Filter> filters = new ArrayList<>();

            if(StringUtils.hasText(cityCode)) {
                filters.add(Filter.eq("city", cityService.getCityByCode(cityCode)));
            }

            if(payFromId != null) {
                filters.add(Filter.eq("payFrom", PayFrom.findByValue(payFromId)));
            }

            if(filters.size() > 0) {
                pageable.setFilters(filters);
            }

            Page<ProvidentFundRatioSetting> providentFundRatioSettingPage = providentFundRatioSettingService.findPage(pageable);
            result = new PageResult<Object>((int)providentFundRatioSettingPage.getTotal(), providentFundRatioSettingPage.getPageNumber(),
                    providentFundRatioSettingPage.getPageSize(), providentFundRatioSettingService.createEntity().convertList(providentFundRatioSettingPage.getContent(), new String[] {"city"}));

        }catch (Exception e){
            LOG.error("get providentFundRatioSetting list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(), pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "获取公积金比例配置详情",notes = "获取公积金比例配置详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取公积金比例配置详情接口:/api/providentFundRatioSetting/findById")
    public PublicResult<JSONObject>info(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            ProvidentFundRatioSetting providentFundRatioSetting = providentFundRatioSettingService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS, providentFundRatioSettingService.createEntity().convertEntity(providentFundRatioSetting, new String[] {"city"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    @ApiOperation(value = "保存/更新公积金比例配置",notes = "保存/更新公积金比例配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新公积金比例配置接口:/api/providentFundRatioSetting/save")
    public PublicResult<String> save(@RequestBody ProvidentFundRatioSetting providentFundRatioSetting)throws Exception{
        LOG.debug("providentFundRatioSettingObj =" + providentFundRatioSetting.toString());
        try{
            List<ProvidentFundRatioSetting> list = providentFundRatioSettingDao.findByCityAndPayFrom(
                    providentFundRatioSetting.getCity(), providentFundRatioSetting.getPayFrom());
            if (null != providentFundRatioSetting.getId()) {
                if(list.size() > 1 || (list.size() == 1 && !list.get(0).getId().equals(providentFundRatioSetting.getId()))){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }
                providentFundRatioSettingService.update(providentFundRatioSetting);
            } else {
                if (!list.isEmpty()){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }
                providentFundRatioSettingService.save(providentFundRatioSetting);
            }
            return new PublicResult<String>(PublicResultConstant.SUCCESS,"操作成功");
        }catch (Exception e){
            LOG.error("save/update  error. {}", e);
            return  new PublicResult<String>(PublicResultConstant.ERROR,"操作失败");
        }
    }

    @ApiOperation(value = "批量删除公积金比例配置",notes = "批量删除公积金比例配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/delete")
    @Log(description="平台中心批量删除公积金比例配置接口:/api/providentFundRatioSetting/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        providentFundRatioSettingService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

}
