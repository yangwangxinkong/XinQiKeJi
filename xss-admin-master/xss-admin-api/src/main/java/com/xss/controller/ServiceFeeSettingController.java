package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.ServiceFeeSetting;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayCategory;
import com.xss.domain.enums.PayFrom;
import com.xss.service.CityService;
import com.xss.service.ServiceFeeSettingService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务费配置管理接口
 * @author Hu
 * @date 2018/9/9
 */
@RestController
@RequestMapping("/api/serviceFeeSetting")
@Api(description="服务费配置管理模块")
public class ServiceFeeSettingController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ServiceFeeSettingController.class);

    @Autowired
    private ServiceFeeSettingService serviceFeeSettingService;
    @Autowired
    private CityService cityService;

    @ApiOperation(value = "获取服务费配置列表",notes = "获取服务费配置列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取服务费配置列表接口:/api/ServiceFeeSetting/list")
    public PageResult<Object> list(Pageable pageable, String cityCode, Integer payFromId, Integer feeCategoryId, Integer payCategoryId)throws Exception{
        LOG.debug("get serviceFeeSetting list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<>();

            if(StringUtils.hasText(cityCode)) {
                filters.add(Filter.eq("city", cityService.getCityByCode(cityCode)));
            }

            if(payFromId != null) {
                filters.add(Filter.eq("payFrom", PayFrom.findByValue(payFromId)));
            }

            if(feeCategoryId != null) {
                filters.add(Filter.eq("feeCategory", FeeCategory.findByValue(feeCategoryId)));
            }

            if(payCategoryId != null) {
                filters.add(Filter.eq("payCategory", PayCategory.findByValue(payCategoryId)));
            }

            if(filters.size() > 0) {
                pageable.setFilters(filters);
            }
            Page<ServiceFeeSetting> serviceFeeSettingPage = serviceFeeSettingService.findPage(pageable);
            result = new PageResult<Object>((int)serviceFeeSettingPage.getTotal(), serviceFeeSettingPage.getPageNumber(),
                    serviceFeeSettingPage.getPageSize(), serviceFeeSettingService.createEntity().convertList(serviceFeeSettingPage.getContent(), new String[]{"city"}));

        }catch (Exception e){
            LOG.error("get ServiceFeeSetting list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(), pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "获取服务费配置详情",notes = "获取服务费配置详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取服务费配置详情接口:/api/ServiceFeeSetting/findById")
    public PublicResult<JSONObject>info(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            ServiceFeeSetting serviceFeeSetting = serviceFeeSettingService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS, serviceFeeSettingService.createEntity().convertEntity(serviceFeeSetting, new String[]{"city"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    @ApiOperation(value = "保存/更新服务费配置",notes = "保存/更新服务费配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新服务费配置接口:/api/ServiceFeeSetting/save")
    public PublicResult<String> save(@RequestBody ServiceFeeSetting serviceFeeSetting)throws Exception{
        LOG.debug("ServiceFeeSettingObj =" + serviceFeeSetting.toString());
        try{
            List<ServiceFeeSetting> list = serviceFeeSettingService.getServiceFeeSettingDao().findByCityAndFeeCategoryAndPayCategoryAndMonthCount(
                    serviceFeeSetting.getCity(), serviceFeeSetting.getFeeCategory(), serviceFeeSetting.getPayCategory(), serviceFeeSetting.getMonthCount());
            if (null != serviceFeeSetting.getId()) {
                if(list.size() > 1 || (list.size() == 1 && !list.get(0).getId().equals(serviceFeeSetting.getId()))){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }
                serviceFeeSettingService.update(serviceFeeSetting);
            } else {
                if (!list.isEmpty()){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }
                serviceFeeSettingService.save(serviceFeeSetting);
            }
            return new PublicResult<String>(PublicResultConstant.SUCCESS,"操作成功");
        }catch (Exception e){
            LOG.error("save/update  error. {}", e);
            return  new PublicResult<String>(PublicResultConstant.ERROR,"操作失败");
        }
    }

    @ApiOperation(value = "批量删除服务费配置",notes = "批量删除服务费配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/delete")
    @Log(description="平台中心批量删除服务费配置接口:/api/ServiceFeeSetting/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        serviceFeeSettingService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

}
