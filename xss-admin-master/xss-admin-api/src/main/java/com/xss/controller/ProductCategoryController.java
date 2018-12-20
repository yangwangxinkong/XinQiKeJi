package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Member;
import com.xss.domain.Product;
import com.xss.domain.ProductCategory;
import com.xss.service.CityService;
import com.xss.service.ProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 *  商品分类接口
 * @author zzl
 * @since 2018-08-02
 */
@RestController
@RequestMapping("/api/product_category")
@Api(description="商品分类管理模块")
public class ProductCategoryController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ProductCategoryController.class);

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CityService cityService;

    @ApiOperation(value="商品分类列表", notes="按层级获取所有商品分类，不需要传参",produces = "application/json")
    @GetMapping("/list")
    @Log(description="企业中心获取商品分类列表接口:/api/product_category/list")
    public PublicResult<JSONArray> list() throws Exception{

        JSONArray productCategoryJa = new JSONArray();
        List<ProductCategory> list = productCategoryService.findRoots();
        productCategoryJa = productCategoryService.createEntity().convertList(list, new String[]{"children"});
        return new PublicResult<>(PublicResultConstant.SUCCESS, productCategoryJa);
    }

    @ApiOperation(value="上级分类列表", notes="上级分类列表，不需要传参",produces = "application/json")
    @GetMapping("/tree")
    @Log(description="企业中心获取商品分类上级分类列表接口:/api/product_category/tree")
    public PublicResult<JSONArray> tree() throws Exception{

        JSONArray productCategoryJa = new JSONArray();
        List<ProductCategory> list = productCategoryService.findTree();
        productCategoryJa = productCategoryService.createEntity().convertList(list, null);
        return new PublicResult<>(PublicResultConstant.SUCCESS, productCategoryJa);
    }

    @ApiOperation(value="商品分类", notes="通过id获取商品分类",produces = "application/json")
    @GetMapping("/info")
    @Log(description="企业中心获取商品分类接口:/api/product_category/info")
    public PublicResult<JSONObject> info(Long id)throws Exception{
        ProductCategory productCategory = productCategoryService.find(id);
        JSONObject productJo = productCategory.convertEntity(productCategory, new String[]{"parent"});
//        if (null != productCategory.getParent()){
//            productJo.put("parent", productCategory.getParent().getId());
//        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,productJo);
    }

    @ApiOperation(value="保存/更新商品分类", notes="保存商品分类",produces = "application/json")
    @PostMapping("/save")
    @Log(description="企业中心保存商品分类接口:/api/product_category/save")
    public PublicResult<Boolean> save(@RequestBody ProductCategory productCategory, @CurrentUser Member member)throws Exception{
        if (null != productCategory.getParent() && null != productCategory.getParent().getId()){
            productCategory.setParent(productCategoryService.find(productCategory.getParent().getId()));
        }else{
            productCategory.setParent(null);
        }
//        productCategory.setCityCode(cityService.getCityCodeByMember(member));
//        LOG.info("Method save cityCode:"+productCategory.getCityCode());
        if (null != productCategory && null != productCategory.getId()) {
            productCategoryService.update(productCategory);
        }else{
            productCategoryService.save(productCategory);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }
//
//    @ApiOperation(value="更新商品分类", notes="更新商品分类",produces = "application/json")
//    @GetMapping("/update")
//    @Log(description="企业中心更新商品分类接口:/api/product_category/update")
//    public PublicResult<Boolean> update(ProductCategory productCategory)throws Exception{
//        productCategoryService.update(productCategory);
//        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
//    }
//
    @ApiOperation(value="商品分类删除", notes="通过id删除商品分类",httpMethod = "GET")
    @GetMapping("/deleteById")
    @Log(description="企业中心删除商品分类列表接口:/api/product_category/deleteById")
    public PublicResult<String> deleteById(Long id) throws Exception{
        ProductCategory productCategory = productCategoryService.find(id);
        if (productCategory == null) {
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,"数据错误，删除失败");
        }
        Set<ProductCategory> children = productCategory.getChildren();
        if (children != null && !children.isEmpty()) {
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,"存在下级分类，无法删除");
        }
        Set<Product> products = productCategory.getProducts();
        if (products != null && !products.isEmpty()) {
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,"被商品引用，无法删除");
        }
       productCategoryService.delete(id);
       return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }
}
