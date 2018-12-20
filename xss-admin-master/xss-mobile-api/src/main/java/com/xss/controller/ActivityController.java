package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.service.ActivityService;
import com.xss.service.CityService;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 活动
 */
@RestController
@RequestMapping("/m/activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Resource
    private ActivityService activityService;

    /**
     * 根据活动id查询活动信息
     * @param activityId
     * @return
     */
    @ApiOperation(value = "根据来源活动id查询单个活动",notes = "根据来源、活动id查询单个活动信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId",value = "活动id",required = true,dataType = "Long")
    })
    @ResponseBody
    @GetMapping("/info")
    public PublicResult info(Long activityId){

        // 1.参数验证
        if(activityId==null){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service查询活动
        try{

            JSONObject result = activityService.info(activityId, null);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, result.get("data"));
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity info exception!! " +
                    "param: id:{}", activityId);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }

    /**
     * 微信首页获取推荐的专题活动
     */
    @GetMapping("/recommend")
    @ApiOperation(value="微信首页获取推荐的专题活动", notes="微信首页获取推荐的专题活动",produces = "application/json")
    public PublicResult<JSONArray> recommend() {
        PublicResult<JSONArray> result = null;
        try{
            result = new PublicResult<>(PublicResultConstant.SUCCESS, activityService.recommend(null, null, null));
        }catch (Exception e){
            result = new PublicResult<>(PublicResultConstant.ERROR,null);
        }
        return result;
    }

}
