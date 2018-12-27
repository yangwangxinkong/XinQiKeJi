package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Feedback;
import com.xss.domain.FeedbackImage;
import com.xss.domain.Member;
import com.xss.service.FeedbackService;
import com.xss.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端用户反馈接口
 * @author zzl
 * @date 2018/8/31
 */
@RestController
@RequestMapping("/m/member/feedback")
@Api(description="用户反馈接口")
public class FeedbackController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value="保存用户反馈", notes="保存用户反馈", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "", required = true, dataType = "String",paramType="body")})
    @PostMapping("/save")
    @Log(description="保存用户反馈接口:/m/member/feedback/save")
    public PublicResult<Boolean> save(@RequestBody JSONObject feedbackJo, @CurrentUser Member current) {
        try {
            Feedback feedback = JsonUtil.toBean(feedbackJo.toJSONString(), Feedback.class);
            JSONArray ja = feedbackJo.getJSONArray("images");
            if (null != ja && ja.size() > 0) {
                for(int i=0; i < ja.size(); i++) {
                    String image = ja.getJSONObject(i).getString("url");
                    FeedbackImage feedbackImage = new FeedbackImage();
                    feedbackImage.setImage(image);
                    feedback.getFeedbackImages().add(feedbackImage);
                }
            }
            feedback.setMember(current);
            feedbackService.save(feedback);
            return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, null);
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<Boolean>(PublicResultConstant.ERROR, null);
        }
    }

}
