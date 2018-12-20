package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.ResidenceType;
import com.xss.service.CityService;
import com.xss.service.ResidenceTypeService;
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
 *  户口性质管理接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/api/residenceType")
@Api(description="户口性质管理模块")
public class ResidenceTypeController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ResidenceTypeController.class);

    @Autowired
    private CityService cityService;
    @Autowired
    private ResidenceTypeService residenceTypeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="户口性质列表", notes="分页展示户口性质列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取户口性质列表接口:/api/residenceType/list")
    public PageResult<Object> list(Pageable pageable, String cityCode) {
        LOG.debug("get city list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<>();

            if(StringUtils.hasText(cityCode)) {
                filters.add(Filter.eq("city", cityService.getCityByCode(cityCode)));
            }
            if(filters.size() > 0) {
                pageable.setFilters(filters);
            }
            Page<ResidenceType> data = residenceTypeService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), residenceTypeService.createEntity().convertList(data.getContent(), new String[]{"city"}));
        }catch (Exception e){
            LOG.error("get city list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get city list result = " + result.toString());
        return result;
    }

    /**
     * 列表
     */
    @GetMapping("/listByCityId")
    @ApiOperation(value="户口性质列表", notes="分页展示户口性质列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取户口性质列表接口:/api/residenceType/listByCityId")
    public PageResult<Object> listByCityId(Long id) {
        LOG.debug("get city list param = " + id);
        PageResult<Object> result = null;
        try{
            List<ResidenceType> residenceTypes = residenceTypeService.getResidenceTypeByCityId(id);
            result = new PageResult<Object>((int)residenceTypes.size(), 1, 1, residenceTypeService.createEntity().convertList(residenceTypes, new String[]{""}));
        }catch (Exception e){
            LOG.error("get city list error. {}", e);
            result = new PageResult<Object>(0, 0, 0,null);
        }
        LOG.debug("get city list result = " + result.toString());
        return result;
    }
    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="户口性质详情", notes="根据id获取城市详情",produces = "application/json")
    @Log(description="平台中心获取户口性质详情接口:/api/residenceType/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get residenceType info param = " + id);

        ResidenceType residenceType = residenceTypeService.find(id);
        JSONObject result = residenceType.convertEntity(residenceType, new String[]{"city"});

        LOG.debug("get residenceType info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新户口性质", notes="保存户口性质",produces = "application/json")
    @Log(description="平台中心保存户口性质接口:/api/residenceType/save")
    public PublicResult<String> save(@RequestBody ResidenceType residenceType)throws Exception{
        if (null != residenceType && null != residenceType.getId()) {
            residenceTypeService.update(residenceType);
        }else{
            residenceTypeService.save(residenceType);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value="批量删除户口性质", notes="批量删除户口性质",produces = "application/json")
    @Log(description="平台中心批量删除户口性质接口:/api/residenceType/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete city param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            boolean isExist = false;
            ResidenceType residenceType;
            for (Long id : ids) {
                residenceType = residenceTypeService.find(id);
                if(residenceType != null){
                    if(!residenceType.getSocialSecurityRatioSettings().isEmpty() || !residenceType.getQuotations().isEmpty()) {
                        isExist = true;
                        break;
                    }
                }
            }

            if(isExist) {
                return new PublicResult<Boolean>("该户口性质有数据关联，无法删除。", false);
            } else {
                cityService.delete(ids);
                result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
            }
            residenceTypeService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete city error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete city result = " + result.toString());
        return result;
    }

}
