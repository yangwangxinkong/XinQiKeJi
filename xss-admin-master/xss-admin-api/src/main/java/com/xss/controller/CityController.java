package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.City;
import com.xss.service.CityService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  城市管理接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/api/city")
@Api(description="城市管理模块")
public class CityController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(CityController.class);

    @Autowired
    private CityService cityService;

    @ApiOperation(value="地区列表", notes="按层级获取地区列表",produces = "application/json")
    @GetMapping("/children")
    @Log(description="平台中心获取地区列表接口:/api/area/children")
    public PublicResult<JSONObject> list(@RequestParam(required = false) Long parentId) {
        LOG.info("get area list param : parentId = " + parentId);
        try {
            City parent =cityService.find(parentId);
            JSONArray cityJa = new JSONArray();
            JSONObject jo = new JSONObject();
            if (parent != null) {
                jo.put("parentId",parentId);
                jo.put("parent_parentId",parent.getParent()==null?null:parent.getParent().getId());
                jo.put("parentName",parent.getName());
                List<City> list = cityService.findChildren(parent);
                cityJa = cityService.createEntity().convertList(list, City.DEFAULT_JSON_PARAMS);
            } else {
                List<City> list = cityService.findRoots();
                cityJa = cityService.createEntity().convertList(list, City.DEFAULT_JSON_PARAMS);
            }
            jo.put("citys",cityJa);
            return new PublicResult<>(PublicResultConstant.SUCCESS, jo);
        } catch (Exception e) {
            LOG.error("get area list error. " + e);
            return new PublicResult<>(PublicResultConstant.FAILED, new JSONObject());
        }
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存地区", notes="body体参数,不需要Authorization",produces = "application/json")
    @PostMapping("/save")
    public PublicResult<Boolean> save(@RequestBody JSONObject areaJson) {
        LOG.info("save area param : areaJson = " + areaJson);
        try {
            City city = new City();
            Long parentId = areaJson.getLong("parent");
            city.setParent(cityService.find(parentId));
            city.setCode(areaJson.getString("code"));
            city.setName(areaJson.getString("name"));
            city.setOrder(areaJson.getInteger("order"));
            city.setFullName(null);
            city.setTreePath(null);
            city.setChildren(null);
            city.setOrders(null);
            city.setIsMaster(false);
            cityService.save(city);
        } catch (Exception e) {
            LOG.error("save area error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 更新
     */
    @ApiOperation(value="更新地区", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaJson", value = "{\"id\":\"1\",\"name\":\"北京\",\"order\":\"1\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @PostMapping("/update")
    public PublicResult<Boolean> update(@RequestBody JSONObject areaJson) {
        LOG.info("update area param : areaJson = " + areaJson);
        try {
            City city = new City();
            city.setId(areaJson.getLong("id"));
            city.setName(areaJson.getString("name"));
            city.setCode(areaJson.getString("code"));
            city.setOrder(areaJson.getInteger("order"));
            cityService.update(city, "fullName", "treePath", "parent", "children", "orders", "isMaster");
        } catch (Exception e) {
            LOG.error("update area error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    @ApiOperation(value="上级分类列表", notes="上级分类列表，不需要传参",produces = "application/json")
    @GetMapping("/tree")
    @Log(description="平台中心获取文章分类上级分类列表接口:/api/product_category/tree")
    public PublicResult<JSONArray> tree() throws Exception{
        JSONArray cityJa = new JSONArray();
        List<City> list = cityService.findTree();
        cityJa = cityService.createEntity().convertList(list, null);
        return new PublicResult<>(PublicResultConstant.SUCCESS, cityJa);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="城市列表", notes="分页展示城市列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取城市列表接口:/api/city/list")
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
    
    /*@PostMapping("/save")
    @ApiOperation(value="保存/更新城市", notes="保存城市",produces = "application/json")
    @Log(description="平台中心保存城市接口:/api/city/save")
    public PublicResult<String> save(City city)throws Exception{

        if (null != city && null != city.getId()) {

            if (city.getParent() != null) {
                City parent = city.getParent();
                if (parent.equals(city)) {
                    return new PublicResult<>(PublicResultConstant.ERROR,"城市父地区不能是自己");
                }
                List<City> children = cityService.findChildren(parent);
                if (children != null && children.contains(parent)) {
                    return new PublicResult<>(PublicResultConstant.ERROR,"城市父地区不能是自己的子类");
                }
            }
            cityService.update(city);
        }else{
            cityService.save(city);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }*/

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value="批量删除城市", notes="批量删除城市",produces = "application/json")
    @Log(description="平台中心批量删除城市接口:/api/city/delete")
    public PublicResult<Boolean> delete(Long id) {
        LOG.info("delete city param: " + id);
        PublicResult<Boolean> result = null;
        try{
            boolean isExist = false;
            City city = cityService.find(id);
            if(city != null){
                if(!city.getPayBases().isEmpty() || !city.getChildren().isEmpty() || !city.getOrders().isEmpty()
                        || !city.getProvidentFundRatioSettings().isEmpty() || !city.getServiceFeeSettings().isEmpty()
                        || !city.getSocialSecurityRatioSettings().isEmpty() || !city.getQuotations().isEmpty()) {
                    isExist = true;
                }
            }

            if(isExist) {
                return new PublicResult<Boolean>("该城市有数据关联，无法删除。", false);
            } else {
                cityService.delete(id);
                result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
            }
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
    public PublicResult<JSONArray> listAll() throws Exception{

        JSONArray cityJa = new JSONArray();
        List<City> list = cityService.findAll();
        cityJa = cityService.createEntity().convertList(list, City.DEFAULT_JSON_PARAMS);
        return new PublicResult<>(PublicResultConstant.SUCCESS, cityJa);
    }

    /**
     * 检查编号是否唯一
     */
    @GetMapping("/check_code")
    public PublicResult<Boolean> checkCode(Long id, @RequestParam String code) throws Exception{
        PublicResult<Boolean> result = null;
        if (StringUtils.isEmpty(code)) {
            result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS, false);
        }
        City exist = cityService.getCityByCode(code);
        if (null == exist){
            result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,false);
        }else{
            if (null == id || !id .equals(exist.getId())){
                result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
            }else{
                result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,false);
            }
        }
        return result;
    }
}
