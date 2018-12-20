package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.PaymentMethodDao;
import com.xss.dao.QuotationDao;
import com.xss.domain.Quotation;
import com.xss.service.OrderService;
import com.xss.service.QuotationItemService;
import com.xss.service.QuotationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  报价单明细管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/quotationItem")
@Api(description="报价单管理模块")
public class QuotationItemController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(QuotationItemController.class);

    @Resource
    private QuotationDao quotationDao;
    @Resource
    private PaymentMethodDao paymentMethodDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private QuotationItemService quotationItemService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="报价单列表", notes="分页展示报价单列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取报价单列表接口:/m/quotationItem/list")
    public PublicResult<Object> list(Long id) {

        PublicResult<Object> result = null;
        JSONObject jo = new JSONObject();
        try{

            Quotation quotation = quotationDao.getOne(id);
            if(quotation != null) {
                jo = quotationItemService.getJsonFromQuotation(quotation, true);
            }

            result = new PublicResult<>(PublicResultConstant.SUCCESS, jo);

        }catch (Exception e){
            LOG.error("get quotation list error. {}", e);
            result = new PublicResult<Object>(PublicResultConstant.FAILED, null);
        }
        LOG.debug("get quotation list result = " + result.toString());
        System.out.println("列表的返回值==="+jo.toJSONString());
        return result;
    }

}
