package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Attribute;
import com.xss.domain.Member;
import com.xss.domain.Product;
import com.xss.service.AttributeService;
import com.xss.service.CityService;
import com.xss.service.ProductCategoryService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  商品属性管理接口
 * @author zzl
 * @since 2018-08-09
 */
@RestController
@RequestMapping("/api/attribute")
@Api(description="商品属性管理模块")
public class AttributeController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(AttributeController.class);

    @Autowired
    private AttributeService attributeService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="商品属性列表", notes="分页展示品牌列表，支持根据名称搜索",produces = "application/json")
    @Log(description="平台中心获取商品属性列表接口:/api/attribute/list")
    public PageResult<Object> list(Pageable pageable,@CurrentUser Member member) {
        LOG.debug("get Attribute list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Attribute> data = attributeService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), attributeService.createEntity().convertList(data.getContent(), null));
        }catch (Exception e){
            LOG.error("get Attribute list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Attribute list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="商品属性详情", notes="根据id获取商品属性详情",produces = "application/json")
    @Log(description="平台中心获取商品属性详情接口:/api/attribute/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get attribute info param = " + id);

        Attribute attribute = attributeService.find(id);
        JSONObject result = attribute.convertEntity(attribute, null);

        LOG.debug("get attribute info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新商品属性", notes="保存商品属性",produces = "application/json")
    @Log(description="平台中心保存商品属性接口:/api/attribute/save")
    public PublicResult<String> save(@RequestBody JSONObject attributeJo, @CurrentUser Member member)throws Exception{
        Attribute attribute = JsonUtil.json2Obj(attributeJo.toJSONString(), Attribute.class);
        JSONArray optionJa = attributeJo.getJSONArray("optionArr");
        for(int i=0;i<optionJa.size();i++){
            attribute.getOptions().add(optionJa.getJSONObject(i).getString("option"));
        }
        if (null != attribute && null != attribute.getId()) {
            attributeService.update(attribute, "propertyIndex", "productCategory", "company");
        }else{
            attribute.setProductCategory(productCategoryService.find(attribute.getProductCategory().getId()));
            if (attribute.getProductCategory().getAttributes().size() >= Product.ATTRIBUTE_VALUE_PROPERTY_COUNT) {

                return new PublicResult<>(PublicResultConstant.ERROR,String.format("添加失败，同一绑定分类最多允许添加{0}个属性", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT));
            } else {
                attribute.setPropertyIndex(null);
                attributeService.save(attribute);
            }
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除商品属性", notes="批量删除商品属性",produces = "application/json")
    @Log(description="平台中心批量删除商品属性接口:/api/attribute/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete attribute param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            attributeService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete attribute error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete attribute result = " + result.toString());
        return result;
    }
}
