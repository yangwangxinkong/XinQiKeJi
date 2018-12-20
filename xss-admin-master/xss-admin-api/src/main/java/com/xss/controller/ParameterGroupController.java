package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Member;
import com.xss.domain.Parameter;
import com.xss.domain.ParameterGroup;
import com.xss.service.CityService;
import com.xss.service.ParameterGroupService;
import com.xss.service.ProductCategoryService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

/**
 *  商品参数组管理接口
 * @author zzl
 * @since 2018-08-09
 */
@RestController
@RequestMapping("/api/parameter_group")
@Api(description="商品参数组管理模块")
public class ParameterGroupController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ParameterGroupController.class);

    @Autowired
    private ParameterGroupService parameterGroupService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="商品参数组列表", notes="分页展示品牌列表，支持根据名称搜索",produces = "application/json")
    @Log(description="平台中心获取商品参数组列表接口:/api/parameterGroup/list")
    public PageResult<Object> list(Pageable pageable,@CurrentUser Member member) {
        LOG.debug("get ParameterGroup list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
//            pageable.getFilters().add(Filter.eq("company", member.getCompany()));
            Page<ParameterGroup> data = parameterGroupService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), parameterGroupService.createEntity().convertList(data.getContent(), null));
        }catch (Exception e){
            LOG.error("get ParameterGroup list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get ParameterGroup list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="商品参数组详情", notes="根据id获取商品参数组详情",produces = "application/json")
    @Log(description="平台中心获取商品参数组详情接口:/api/parameterGroup/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get parameterGroup info param = " + id);

        ParameterGroup parameterGroup = parameterGroupService.find(id);
        JSONObject result = parameterGroup.convertEntity(parameterGroup, null);

        LOG.debug("get parameterGroup info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新商品参数组", notes="保存商品参数组",produces = "application/json")
    @Log(description="平台中心保存商品参数组接口:/api/parameterGroup/save")
    public PublicResult<String> save(@RequestBody ParameterGroup parameterGroup, @CurrentUser Member member)throws Exception{
        for (Iterator<Parameter> iterator = parameterGroup.getParameters().iterator(); iterator.hasNext();) {
            Parameter parameter = iterator.next();
            if (parameter == null || parameter.getName() == null) {
                iterator.remove();
            } else {
                parameter.setParameterGroup(parameterGroup);
            }
        }
        parameterGroup.setProductCategory(productCategoryService.find(parameterGroup.getProductCategory().getId()));

        if (null != parameterGroup && null != parameterGroup.getId()) {
            parameterGroupService.update(parameterGroup, new String[]{"company"});
        }else{
//            parameterGroup.setCompany(member.getCompany());
            parameterGroupService.save(parameterGroup);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除商品参数组", notes="批量删除商品参数组",produces = "application/json")
    @Log(description="平台中心批量删除商品参数组接口:/api/parameterGroup/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete parameterGroup param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            parameterGroupService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete parameterGroup error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete parameterGroup result = " + result.toString());
        return result;
    }
}
