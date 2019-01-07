package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.PaymentMethodDao;
import com.xss.dao.QuotationDao;
import com.xss.dao.QuotationItemDao;
import com.xss.domain.*;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayCategory;
import com.xss.service.*;
import com.xss.util.DateUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
        return result;
    }

}
