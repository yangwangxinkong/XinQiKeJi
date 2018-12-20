package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.SocialSecurityRatioSettingDao;
import com.xss.domain.SocialSecurityRatioSetting;
import com.xss.domain.enums.PayFrom;
import com.xss.domain.enums.SocialSecurityCategory;
import com.xss.service.CityService;
import com.xss.service.SocialSecurityRatioSettingService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  社保比例配置管理接口
 * @author Hu
 * @date 2018/9/9
 */
@RestController
@RequestMapping("/api/socialSecurityRatioSetting")
@Api(description="社保比例配置管理模块")
public class SocialSecurityRatioSettingController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(SocialSecurityRatioSettingController.class);

    @Resource
    private SocialSecurityRatioSettingDao socialSecurityRatioSettingDao;
    @Autowired
    private SocialSecurityRatioSettingService socialSecurityRatioSettingService;
    @Autowired
    private CityService cityService;

    @ApiOperation(value = "获取社保比例配置列表",notes = "获取社保比例配置列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取社保比例配置列表接口:/api/SocialSecurityRatioSetting/list")
    public PageResult<Object> list(Pageable pageable, String cityCode, Integer payFromId, Integer socialSecurityCategoryId)throws Exception{
        LOG.debug("get SocialSecurityRatioSetting list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<>();

            if(StringUtils.hasText(cityCode)) {
                filters.add(Filter.eq("city", cityService.getCityByCode(cityCode)));
            }

            if(payFromId != null) {
                filters.add(Filter.eq("payFrom", PayFrom.findByValue(payFromId)));
            }

//            if(accountPropertyId != null) {
//                filters.add(Filter.eq("residenceType", AccountProperty.findByValue(accountPropertyId)));
//            }

            if(socialSecurityCategoryId != null) {
                filters.add(Filter.eq("socialSecurityCategory", SocialSecurityCategory.findByValue(socialSecurityCategoryId)));
            }

            if(filters.size() > 0) {
                pageable.setFilters(filters);
            }
            Page<SocialSecurityRatioSetting> socialSecurityRatioSettingPage = socialSecurityRatioSettingService.findPage(pageable);
            result = new PageResult<Object>((int)socialSecurityRatioSettingPage.getTotal(), socialSecurityRatioSettingPage.getPageNumber(),
                    socialSecurityRatioSettingPage.getPageSize(), socialSecurityRatioSettingService.createEntity().convertList(socialSecurityRatioSettingPage.getContent(), new String[]{"city"}));

        }catch (Exception e){
            LOG.error("get SocialSecurityRatioSetting list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(), pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "获取社保比例配置详情",notes = "获取社保比例配置详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取社保比例配置详情接口:/api/SocialSecurityRatioSetting/findById")
    public PublicResult<JSONObject>info(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            SocialSecurityRatioSetting socialSecurityRatioSetting = socialSecurityRatioSettingService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS, socialSecurityRatioSettingService.createEntity().convertEntity(socialSecurityRatioSetting, new String[]{"city", "residenceType"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    @ApiOperation(value = "保存/更小豆社保比例配置",notes = "保存/更小豆社保比例配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更小豆社保比例配置接口:/api/socialSecurityRatioSetting/save")
    public PublicResult<String> save(@RequestBody SocialSecurityRatioSetting socialSecurityRatioSetting)throws Exception{
        LOG.debug("SocialSecurityRatioSettingObj =" + socialSecurityRatioSetting.toString());
        try{
            List<SocialSecurityRatioSetting> list = socialSecurityRatioSettingDao.findByCityAndResidenceTypeAndSocialSecurityCategoryAndPayFrom(
                    socialSecurityRatioSetting.getCity(), socialSecurityRatioSetting.getResidenceType(),
                    socialSecurityRatioSetting.getSocialSecurityCategory(), socialSecurityRatioSetting.getPayFrom());

            if(socialSecurityRatioSetting.getFixed() != null && socialSecurityRatioSetting.getFixed()) {
                socialSecurityRatioSetting.setRatio(BigDecimal.ZERO);
            } else {
                socialSecurityRatioSetting.setFixedValue(BigDecimal.ZERO);
            }

            if (null != socialSecurityRatioSetting.getId()) {
                if(list.size() > 1 || (list.size() == 1 && !list.get(0).getId().equals(socialSecurityRatioSetting.getId()))){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }

                socialSecurityRatioSettingService.update(socialSecurityRatioSetting);
            } else {
                if (!list.isEmpty()){
                    return new PublicResult<String>(PublicResultConstant.ERROR,"该配置已存在");
                }
                socialSecurityRatioSettingService.save(socialSecurityRatioSetting);
            }
            return new PublicResult<String>(PublicResultConstant.SUCCESS,"操作成功");
        }catch (Exception e){
            LOG.error("save/update  error. {}", e);
            return  new PublicResult<String>(PublicResultConstant.ERROR,"操作失败");
        }
    }

    @ApiOperation(value = "批量删除社保比例配置",notes = "批量删除社保比例配置",httpMethod = "Post",produces = "application/json")
    @PostMapping("/delete")
    @Log(description="平台中心批量删除社保比例配置接口:/api/socialSecurityRatioSetting/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        socialSecurityRatioSettingService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

}
