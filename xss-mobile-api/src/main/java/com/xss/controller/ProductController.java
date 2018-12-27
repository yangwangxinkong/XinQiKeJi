package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.ProductDao;
import com.xss.domain.Member;
import com.xss.domain.Product;
import com.xss.domain.ProductCategory;
import com.xss.domain.enums.ProductType;
import com.xss.service.CityService;
import com.xss.service.MemberService;
import com.xss.service.ProductCategoryService;
import com.xss.service.ProductService;
import com.xss.util.*;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  商品管理接口
 * @author zzl
 * @since 2018-08-02
 */
@RestController
@RequestMapping("/m/product")
@Api(description="商品管理模块")
public class ProductController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ProductController.class);

    @Resource
    private ProductDao productDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="商品列表", notes="分页展示列表，支持根据名称搜索",produces = "application/json")
    @Log(description="平台中心获取商品列表接口:/api/product/list")
    public PageResult<Object> list(Pageable pageable, Long categoryId, ProductType productType) {
        LOG.debug("get Product list param = " + pageable.toString() + "productType:" + productType);
        PageResult<Object> result = null;
        try{
            if (null == productType){
                productType = ProductType.service;
            }
            List<Filter> filters =new ArrayList<>();
            filters.add(Filter.eq("isMarketable", true));
            filters.add(Filter.eq("isList", true));

            //商品类型
            filters.add(Filter.eq("productType", productType));

            if(categoryId != null) {
                ProductCategory productCategory = productCategoryService.find(categoryId);
                filters.add(Filter.eq("productCategory", productCategory));
            }

            if(filters.size() > 0) {
                pageable.setFilters(filters);
            }
            //页面选择排序
            Page<Product> data = productService.findPage(pageable);
            JSONArray dataJsArr=productService.createEntity().convertList(data.getContent(),
                    new String[]{"isMarketable", "isLimit", "deliveryTime", "deliveryPlace", "thumbnail"});
            result = new PageResult<Object>((int)data.getTotalPages(),data.getPageNumber(),data.getPageSize(), dataJsArr);
        }catch (Exception e){
            LOG.error("get Product list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Product list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="商品详情", notes="根据id获取商品详情",produces = "application/json")
    @Log(description="平台中心获取商品详情接口:/m/product/info")
    public PublicResult<JSONObject> info(Long id) {
        LOG.info("product info param : id = " + id);
        PublicResult<JSONObject> result = null;
        try {
            Product product = productService.find(id);
            if (product == null) {
                return new PublicResult<JSONObject>(PublicResultConstant.PARAM_ERROR, null);
            }

            JSONObject jo = product.convertEntity(product, new String[]{"brand", "isMarketable", "isTop", "isGift",
                    "isList", "IsStoreFreight", "isLimit", "deliveryTime", "deliveryPlace", "productImages",
                    "introduction", "company", "specificationValues", "availableStock", "productSpecificationValues",
                    "specificationValueIds", "specificationValues_all", "specificationValueNames"});

            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
        } catch (Exception e) {
            LOG.error("product info error." + e);
            result = new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
        }
        return result;
    }


    /**
     * 判断是否收藏了商品
     */
    @GetMapping("/isFavorited")
    @ApiOperation(value="判断是否收藏了商品", notes="根据id判断是否收藏了商品",produces = "application/json")
    @Log(description="平台中心获取商品详情接口:/m/product/isFavorited")
    public PublicResult<JSONObject> isFavorited(Long id, @CurrentUser Member member) {
        PublicResult<JSONObject> result = null;
        try {
            Product product = productService.find(id);
            if (product == null) {
                return new PublicResult<JSONObject>(PublicResultConstant.PARAM_ERROR, null);
            }

            JSONObject jo = new JSONObject();
            // 商品收藏
            if(member != null && member.getId() != null) {
                jo.put("productFavorited", member.getFavoriteProducts().contains(product));
            } else {
                jo.put("productFavorited", false);
            }

            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
        } catch (Exception e) {
            LOG.error("product info error." + e);
            result = new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
        }
        return result;
    }

}
