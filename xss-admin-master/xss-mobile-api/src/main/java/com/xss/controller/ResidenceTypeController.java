package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.City;
import com.xss.domain.ResidenceType;
import com.xss.service.CityService;
import com.xss.service.ResidenceTypeService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  户口性质管理接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/residenceType")
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
    @Log(description="平台中心获取户口性质列表接口:/m/residenceType/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.debug("get city list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<City> data = cityService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), cityService.createEntity().convertList(data.getContent(), new String[]{"area"}));
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
    @Log(description="平台中心获取户口性质列表接口:/m/residenceType/listByCityId")
    public PageResult<Object> listByCityId(Long id) {
        LOG.debug("get city list param = " + id);
        PageResult<Object> result = null;
        try{
            if(id == null) {
                return new PageResult<Object>(0, 0, 0,null);
            }
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
    @ApiOperation(value="城市详情", notes="根据id获取城市详情",produces = "application/json")
    @Log(description="平台中心获取城市详情接口:/api/city/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get city info param = " + id);

        City city = cityService.find(id);
        JSONObject result = city.convertEntity(city, new String[]{"area"});

        LOG.debug("get city info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新城市", notes="保存城市",produces = "application/json")
    @Log(description="平台中心保存城市接口:/api/city/save")
    public PublicResult<String> save(@RequestBody City city)throws Exception{
        if (null != city && null != city.getId()) {
            cityService.update(city);
        }else{
            cityService.save(city);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除城市", notes="批量删除城市",produces = "application/json")
    @Log(description="平台中心批量删除城市接口:/api/city/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete city param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            cityService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete city error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete city result = " + result.toString());
        return result;
    }

    @ApiOperation(value="所有城市列表", notes="所有城市列表，不需要传参",produces = "application/json")
    @GetMapping("/list_all")
    @Log(description="平台中心获取所有城市列表接口:/api/city/list_all")
    public PublicResult<JSONArray> tree() throws Exception{

        JSONArray cityJa = new JSONArray();
        List<City> list = cityService.findAll();
        cityJa = cityService.createEntity().convertList(list, City.DEFAULT_JSON_PARAMS);
        return new PublicResult<>(PublicResultConstant.SUCCESS, cityJa);
    }
}
