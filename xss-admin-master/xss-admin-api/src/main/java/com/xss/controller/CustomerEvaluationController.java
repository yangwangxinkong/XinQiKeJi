package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Article;
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
//@RequestMapping("/api/article")
public class CustomerEvaluationController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(CustomerEvaluationController.class);

    @Autowired
    private CustomerEvaluationService customerEvaluationService;


    /*客户评价保存*/
    @Log(description="平台中心保存客户评价列表接口:/api/customerEvaluation/save")
    @PostMapping("/save")
    public PublicResult<Object> save(CustomerEvaluation customerEvaluation){
        System.out.println("客户评价保存=====" + customerEvaluation.toString());
        System.out.println("长度=="+customerEvaluation.getCustomerName().length());
        //数据的有效性验证(客户名、公司名称、评价内容、客户图片
        if (customerEvaluation.getCustomerName() == null || customerEvaluation.getCustomerName().length() < 2){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户名不能为空并且最少两位");
        }
        if (customerEvaluation.getEvaluation() == null){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户评价不能为空");
        }
        if (customerEvaluation.getCompanyName() == null){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"所属公司不能为空");
        }
        if (customerEvaluation.getCustomerImg() == null){
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户图片不能为空");
        }
        //保存客户评价
        try {
            //判断请求中是否有主键ID，如果没有就保存，反则根据ID修改客户评价
            if (customerEvaluation.getId() == null) {
                customerEvaluationService.save(customerEvaluation);
                return new PublicResult<Object>(PublicResultConstant.SUCCESS, "客户评价保存成功");
            }else {
                //修改客户评价
                customerEvaluation.setModifyDate(new Date());
                CustomerEvaluation updateResult = customerEvaluationService.update(customerEvaluation);
                return new PublicResult<Object>(PublicResultConstant.SUCCESS,updateResult);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CustomerEvaluationController Method save exception!!! params:"
                    + "客户名称" + customerEvaluation.getCustomerName()
                    + "客户评价" + customerEvaluation.getEvaluation()
                    + "所属公司名称" + customerEvaluation.getCompanyName()
                    + "客户图片" + customerEvaluation.getCustomerImg());
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户评价保存失败");
        }
    }

    //根据ID删除记录
    @GetMapping("/delete")
    public PublicResult<Object> CustomerDelete(Long... ids){
        System.out.println("删除ID是：====："+ids.length);
        for (int i=0;i<ids.length;i++){
            System.out.println("ID为===="+ids[i]);
        }
        if (ids == null) return new PublicResult<Object>(PublicResultConstant.ERROR,"id不能为空");
        try {
            //执行删除
            customerEvaluationService.delete(ids);
            return new PublicResult<Object>(PublicResultConstant.SUCCESS,"客户评价删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<Object>(PublicResultConstant.ERROR,"客户评价删除失败");
        }
    }


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

    /*,produces = "application/json,charset='utf-8'"*/
    @ApiOperation(value="客户评价详情", notes="通过id获取客户评价详情",produces = "application/json")
    @GetMapping(value="/info" )
    @Log(description="平台中心获取客户评价详情接口:/api/customerEvaluation/info")
    public PublicResult<JSONObject> getInfo(Long id)throws Exception{
        LOG.debug("id = "+id);
        try{
            CustomerEvaluation customerEvaluation = customerEvaluationService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,customerEvaluationService.createEntity().convertEntity(customerEvaluation,new String[]{"id","companyName","customerName","customerImg","evaluation"}));
        }catch (Exception e){
            LOG.error("get CustomerEvaluation info error. {}", e);
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,null);
        }
    }

}
