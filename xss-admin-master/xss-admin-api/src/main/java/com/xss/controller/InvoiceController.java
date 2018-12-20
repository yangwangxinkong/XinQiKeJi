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
import com.xss.domain.enums.InvoiceCategory;
import com.xss.domain.enums.InvoiceStatus;
import com.xss.domain.enums.InvoiceType;
import com.xss.service.OrderInvoiceService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *  发票管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/api/invoice")
@Api(description="发票管理模块")
public class InvoiceController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(InvoiceController.class);

    @Autowired
    private OrderInvoiceService orderInvoiceService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="发票列表", notes="分页展示发票列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取发票列表接口:/api/invoice/list")
    public PageResult<Object> list(Integer invoiceTypeId, Integer invoiceCategoryId, Integer invoiceStatusId,
                                   String invoiceTitle, Pageable pageable, @CurrentUser Admin admin) {
        LOG.debug("get order list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<Filter>();

            OrderInvoice orderInvoice = new OrderInvoice();
            if(invoiceTypeId != null) {
                orderInvoice.setInvoiceType(InvoiceType.findByValue(invoiceTypeId));
            }
            if(invoiceCategoryId != null) {
                orderInvoice.setInvoiceCategory(InvoiceCategory.findByValue(invoiceCategoryId));
            }
            if(invoiceStatusId != null) {
                orderInvoice.setInvoiceStatus(InvoiceStatus.findByValue(invoiceStatusId));
            }

            if (StringUtils.isNotBlank(invoiceTitle)) {
                orderInvoice.setInvoiceTitle(invoiceTitle);
            }

            Page<OrderInvoice> data = orderInvoiceService.findPage(orderInvoice, pageable);

            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),
                    orderInvoiceService.createEntity().convertList(data.getContent(), null));
        }catch (Exception e){
            LOG.error("get order list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get order list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="发票详情", notes="根据id获取发票详情",produces = "application/json")
    @Log(description="平台中心获取发票详情接口:/api/invoice/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get order info param = " + id);

        OrderInvoice orderInvoice = orderInvoiceService.find(id);
        JSONObject result = orderInvoice.convertEntity(orderInvoice, new String[]{"orders"});

        //方式
        JSONArray methodJa = new JSONArray();
        JSONObject methodJo;
        for(InvoiceStatus method : InvoiceStatus.values()) {
            methodJo = new JSONObject();
            methodJo.put("value", method.getValue());
            methodJo.put("label", method.getDesc());

            methodJa.add(methodJo);
        }
        result.put("invoiceStatusOptions", methodJa);

        LOG.debug("get order info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/update")
    @ApiOperation(value="保存/更新发票", notes="保存发票",produces = "application/json")
    @Log(description="平台中心保存发票接口:/api/invoice/update")
    public PublicResult<String> update(Long orderInvoiceId, Integer invoiceStatusId)throws Exception{
        if (null == orderInvoiceId || invoiceStatusId == null) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"");
        }

        OrderInvoice orderInvoiceTemp = orderInvoiceService.find(orderInvoiceId);
        orderInvoiceTemp.setInvoiceStatus(InvoiceStatus.findByValue(invoiceStatusId));
        orderInvoiceService.update(orderInvoiceTemp);

        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }
}
