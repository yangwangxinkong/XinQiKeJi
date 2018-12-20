package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.City;
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
@RequestMapping("/api/activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Resource
    private ActivityService activityService;
    @Resource
    private CityService cityService;
//    @Resource
//    private ProductService productService;
//
//    /**
//     * 商品选择
//     */
//    @GetMapping("/product_select")
//    public PublicResult productSelect(String text) {
//        List<Map<String, Object>> data = new ArrayList<>();
//        if (StringUtils.isNotEmpty(text)) {
//            List<Product> products = productService.search(text, false, 20);
//            for (Product product : products) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("productSn", product.getSn());
//                map.put("fullName", product.getFullName());
//                map.put("price", product.getPrice());
//                map.put("storeId", product.getStore()==null?null:product.getStore().getId());
//                data.add(map);
//            }
//        }
//        return new PublicResult<>(PublicResultConstant.SUCCESS, data);
//    }


    /**
     * 分页查询活动列表
     * @return
     */
    @GetMapping("/list")
    public PageResult list(Pageable pageable, String dataType, @CurrentUser Admin admin){
        String[] status = null;
        if(dataType!=null){
            if("audit".equals(dataType)){
                status = new String[]{"auditing","auditFail"};
            }else if("list".equals(dataType)){
                status = new String[]{"auditThroughNotStart","underway","discontinue","end"};
            }
        }

        PageResult pageResult = new PageResult();

        JSONObject result = activityService.list(pageable, null, status, null, null);
        if(result!=null && result.getInteger("errcode")==0){
            JSONObject data = result.getJSONObject("data");
            JSONArray activityList = data.getJSONArray("activities");
            for(int i=0; i<activityList.size(); i++){
                JSONObject startDate = activityList.getJSONObject(i).getJSONObject("startDate");
                if(startDate!=null){
                    activityList.getJSONObject(i).put("startDate", startDate.getLong("time"));
                }
                JSONObject endDate = activityList.getJSONObject(i).getJSONObject("endDate");
                if(startDate!=null){
                    activityList.getJSONObject(i).put("endDate", endDate.getLong("time"));
                }
            }

            pageResult.setTotal(data.getInteger("total"));
            pageResult.setPageSize(data.getInteger("pageSize"));
            pageResult.setPageIndex(data.getInteger("pageNumber"));
            pageResult.setList(activityList);
        }
        return pageResult;
    }

    /**
     * 添加活动
     * @return
     */
    @ParamXssPass
    @ApiOperation(value = "添加活动",notes = "添加活动",httpMethod = "POST")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public PublicResult save(@RequestBody JSONObject activityJson, @CurrentUser Admin admin){
        //1.参数记录日志
        logger.info("Method activity save param: activity:{}", activityJson.toJSONString());
//        JSONObject activityJson = JSONObject.parseObject(activityJsonStr);
        JSONArray beginAndEndDate = activityJson.getJSONArray("beginAndEndDate");
        if(beginAndEndDate!=null){
            if(beginAndEndDate.getLong(0)!=null){
                activityJson.put("startDate", beginAndEndDate.getLong(0));
            }
            if(beginAndEndDate.getLong(1)!=null){
                activityJson.put("endDate", beginAndEndDate.getLong(1));
            }
            activityJson.put("beginAndEndDate", null);
        }
        //3.保存活动信息
        try{

            JSONObject result = activityService.save(activityJson);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            // 记录错误日志
            logger.error("Method activity save exception!! param: activity:{}", activityJson.toString());
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }


    /**
     * 修改活动
     * @param activityJson
     * @return
     */
    @ParamXssPass
    @ApiOperation(value = "修改活动",notes = "修改活动信息",httpMethod = "POST")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public PublicResult update(@RequestBody JSONObject activityJson, @CurrentUser Admin admin){
        //1.参数记录日志
        logger.info("Method activity update param: activity:{}", activityJson.toJSONString());

        JSONArray beginAndEndDate = activityJson.getJSONArray("beginAndEndDate");
        if(beginAndEndDate!=null){
            if(beginAndEndDate.getLong(0)!=null){
                activityJson.put("startDate", beginAndEndDate.getLong(0));
            }
            if(beginAndEndDate.getLong(1)!=null){
                activityJson.put("endDate", beginAndEndDate.getLong(1));
            }
            activityJson.put("beginAndEndDate", null);
        }
        //3.保存活动信息
        try{

            JSONObject result = activityService.update(activityJson);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            // 记录错误日志
            logger.error("Method activity update exception!! param: activity:{}", activityJson.toString());
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }


    /**
     * 用户申请参加报名活动
     * @return
     */
    @ApiOperation(value = "提交报名信息",notes = "报名活动-提交用户报名信息",httpMethod = "POST")
    @PostMapping(value = "/sign_up")
    public PublicResult signUp(@RequestBody JSONObject signupUserJson){

        // 1.记录参数日志
        logger.info("Method activity sign up param: signupUser:{}, activityId:{}", signupUserJson.toString());

        // 3.调用serivce保存报名信息
        try{

            JSONObject result = activityService.signUp(signupUserJson);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity sign up service exception!! param: activity:{}", signupUserJson.toString());
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }



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
     * 审核活动
     * @param activityId    活动id
     * @param decision  审核结果：0=通过、1=驳回
     * @return
     */
    @ApiOperation(value = "审核活动",notes = "审核活动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId",value = "活动id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "decision",value = "审核结果(0通过/1驳回)",required = true,dataType = "Integer")
    })
    @PostMapping("/audit")
    public PublicResult audit(Long activityId, Integer decision){
        // 1.记录参数日志
        logger.info("Method activity audit param: activityId:{},decision:{}", activityId, decision);

        // 1.参数验证
        if(decision==null || (decision!=0 && decision!=1)){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service审核活动
        try{
            JSONObject result = activityService.audit(activityId, decision);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity audit exception!! param: activityId:{},decision:{}", activityId, decision);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }



    /**
     * 中止活动
     * @param activityId    活动id
     * @return
     */
    @ApiOperation(value = "中止活动",notes = "中止活动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId",value = "活动id",required = true,dataType = "Long")
    })
    @PostMapping("/discontinue")
    public PublicResult discontinue(Long activityId){

        // 1.参数验证
        if(activityId==null){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service中止活动
        try{
            JSONObject result = activityService.discontinue(activityId);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity discontinue exception!! param: activityId:{}", activityId);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }



    /**
     * 开始活动
     * @param activityId    活动id
     * @return
     */
    @ApiOperation(value = "开始活动",notes = "手动开始活动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId",value = "活动id",required = true,dataType = "Long")
    })
    @PostMapping("/start")
    public PublicResult start(Long activityId){

        // 1.参数验证
        if(activityId==null){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service开始活动
        try{
            JSONObject result = activityService.start(activityId);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity start exception!! param: activityId:{}", activityId);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }



    /**
     * 结束活动
     * @param activityId    活动id
     * @return
     */
    @ApiOperation(value = "结束活动",notes = "手动结束活动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId",value = "活动id",required = true,dataType = "Long")
    })
    @PostMapping("/end")
    public PublicResult end(Long activityId){
        // 1.参数验证
        if(activityId==null){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service结束活动
        try{
            JSONObject result = activityService.end(activityId);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity end exception!! param: activityId:{}", activityId);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }


    /**
     * 删除活动
     * @param ids    活动id
     * @return
     */
    @ApiOperation(value = "删除活动",notes = "删除活动",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "活动id",required = true,dataType = "Long[]")
    })
    @PostMapping("/delete")
    public PublicResult delete(@RequestParam(value = "ids[]", required = false) Long[] ids){
        // 1.参数验证
        if(ids==null || ids.length<1){
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }

        // 2.调用service结束活动
        try{
            JSONObject result = activityService.delete(ids);
            if(result!=null && result.getInteger("errcode")==0){
                return new PublicResult<>(PublicResultConstant.SUCCESS, null);
            }else{
                PublicResult publicResult = new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
                publicResult.setMsg(result.getString("errmsg"));
                return publicResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Method activity delete exception!! param: ids:{}", ids);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

    }



}
