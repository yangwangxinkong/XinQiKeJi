package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.CustomerEvaluation;
import com.xss.service.CustomerEvaluationService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/customerEvaluation")
public class CustomerEvaluationController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(CustomerEvaluationController.class);

    @Autowired
    private CustomerEvaluationService customerEvaluationService;

    //查询客户评价
    @ApiOperation(value="客户评价列表", notes="获取客户评价列表",produces = "application/json")
    @GetMapping(value="/list")
    @Log(description="平台中心获取客户评价列表接口:/api/customerEvaluation/list")
    public PageResult<Object> queryList(Pageable pageable)throws Exception{
        System.out.println("获取客户评价列表执行了");
        LOG.debug("获取客户评价列表参数 ==== " + pageable.toString());
        PageResult<Object> result = null;
        try {
            Page<CustomerEvaluation> customerServicePage = customerEvaluationService.findPage(pageable);
            /* customerServicePage.getTotal() ---->   获取总记录数
                customerServicePage.getPageNumber()---->获取页码
                customerServicePage.getPageSize() ----> 获取每页记录数
                customerServicePage.getContent()  -----> 获取内容
             */
            result = new PageResult<Object>((int)customerServicePage.getTotal(),customerServicePage.getPageNumber(),
                    customerServicePage.getPageSize(),
                    customerEvaluationService.createEntity().convertList(customerServicePage.getContent(),
                            new String[]{"id","createDate","modifyDate","companyName","customerName","customerImg","evaluation"}));
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("获取客户评价列表失败");
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
            return result;
        }
    }

}
