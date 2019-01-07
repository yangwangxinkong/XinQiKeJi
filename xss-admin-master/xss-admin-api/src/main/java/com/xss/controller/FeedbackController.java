package com.xss.controller;

import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Coupon;
import com.xss.domain.Feedback;
import com.xss.domain.Share;
import com.xss.service.FeedbackService;
import com.xss.service.ShareService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  意见反馈管理接口
 * @author zzl
 * @date 2018/12/15
 */
@RestController
@RequestMapping("/api/feedback")
@Api(description="意见反馈管理模块")
public class FeedbackController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;


    @ApiOperation(value = "获取意见反馈列表",notes = "获取意见反馈列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取意见反馈列表接口:/api/feedback/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Feedback list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Feedback> page = feedbackService.findPage(pageable);
            result = new PageResult<Object>((int)page.getTotal(),page.getPageNumber(),page.getPageSize(), feedbackService.createEntity().convertList(page.getContent(), null));
        }catch (Exception e){
            LOG.error("get Feedback list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }


    /**
     * 查询单个用户意见反馈
     */
    @GetMapping("/info")
    public PublicResult info(Long id) {
        try{
            Feedback feedback = feedbackService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS, feedback.convertEntity(feedback, null));
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

}