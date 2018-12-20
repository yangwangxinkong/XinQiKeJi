package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Brand;
import com.xss.domain.Member;
import com.xss.service.BrandService;
import com.xss.service.CityService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  商品品牌管理接口
 * @author zzl
 * @since 2018-08-09
 */
@RestController
@RequestMapping("/api/brand")
@Api(description="商品品牌管理模块")
public class BrandController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(BrandController.class);

    @Autowired
    private BrandService brandService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="商品品牌列表", notes="分页展示品牌列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取商品品牌列表接口:/api/brand/list")
    public PageResult<Object> list(Pageable pageable, @CurrentUser Member member) {
        LOG.debug("get Brand list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
//            pageable.getFilters().add(Filter.eq("company", member.getCompany()));
            Page<Brand> data = brandService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), brandService.createEntity().convertList(data.getContent(), Brand.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            LOG.error("get Brand list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Brand list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="商品品牌详情", notes="根据id获取商品品牌详情",produces = "application/json")
    @Log(description="平台中心获取商品品牌详情接口:/api/brand/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get brand info param = " + id);

        Brand brand = brandService.find(id);
        JSONObject result = brand.convertEntity(brand, Brand.DEFAULT_JSON_PARAMS);

        LOG.debug("get brand info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新商品品牌", notes="保存商品品牌",produces = "application/json")
    @Log(description="平台中心保存商品品牌接口:/api/brand/save")
    public PublicResult<Boolean> save(@RequestBody Brand brand, @CurrentUser Member member)throws Exception{

//        brand.setCityCode(cityService.getCityCodeByMember(member));
//        LOG.info("Method save cityCode:"+brand.getCityCode());
        if (null != brand && null != brand.getId()) {
            brandService.update(brand, new String[]{"company"});
        }else{
//            brand.setCompany(member.getCompany());
            brandService.save(brand);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除商品品牌", notes="批量删除商品品牌",produces = "application/json")
    @Log(description="平台中心批量删除商品品牌接口:/api/brand/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete brand param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            brandService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete brand error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete brand result = " + result.toString());
        return result;
    }

    /**
     * 全部列表
     */
    @GetMapping("/all")
    public PublicResult<JSONArray> getAll(@CurrentUser Member member) {
        PublicResult<JSONArray> result = null;
        try{
            List<Filter> filters = new ArrayList<>();
//            filters.add(Filter.eq("company", member.getCompany()));
            List<Brand> data = brandService.findList(null, null, filters, null);
            result = new PublicResult<JSONArray>(PublicResultConstant.SUCCESS,brandService.createEntity().convertList(data,Brand.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            LOG.error("getAll brand error. " + e);
            result = new PublicResult<JSONArray>(PublicResultConstant.FAILED,null);
        }
        return result;
    }
}
