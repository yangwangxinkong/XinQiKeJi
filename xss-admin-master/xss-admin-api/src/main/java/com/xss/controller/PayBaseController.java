package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.OrderInvoice;
import com.xss.domain.PayBase;
import com.xss.domain.enums.InvoiceCategory;
import com.xss.domain.enums.InvoiceStatus;
import com.xss.domain.enums.InvoiceType;
import com.xss.service.CityService;
import com.xss.service.OrderInvoiceService;
import com.xss.service.PayBaseService;
import com.xss.util.StringUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  发票管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/api/payBase")
@Api(description="发票管理模块")
public class PayBaseController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(PayBaseController.class);

    @Autowired
    private PayBaseService payBaseService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="基数列表", notes="分页展示基数列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取基数列表接口:/api/invoice/list")
    public PageResult<Object> list(String cityCode, Pageable pageable, @CurrentUser Admin admin) {
        LOG.debug("get payBase list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<Filter>();

            if (StringUtils.isNotBlank(cityCode)){
                pageable.getFilters().add(Filter.eq("city", cityService.getCityByCode(cityCode)));
            }

            Page<PayBase> data = payBaseService.findPage(pageable);

            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),
                    payBaseService.createEntity().convertList(data.getContent(), new String[]{"city"}));
        }catch (Exception e){
            LOG.error("get payBase list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get payBase list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="基数详情", notes="根据id获取基数详情",produces = "application/json")
    @Log(description="平台中心获取基数详情接口:/api/payBase/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get payBase info param = " + id);

        PayBase payBase = payBaseService.find(id);
        JSONObject result = payBaseService.createEntity().convertEntity(payBase, new String[]{"city"});

        LOG.debug("get payBase info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新基数", notes="保存基数",produces = "application/json")
    @Log(description="平台中心保存基数接口:/api/payBase/save")
    public PublicResult<String> save(@RequestBody PayBase payBase)throws Exception{
        if (null != payBase && null != payBase.getId()) {
            payBaseService.update(payBase);
        }else{
            payBaseService.save(payBase);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value="批量删除基数", notes="批量删除基数",produces = "application/json")
    @Log(description="平台中心批量删除基数接口:/m/payBase/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete payBase param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            payBaseService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete quotation error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete payBase result = " + result.toString());
        return result;
    }

}
