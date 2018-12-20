package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.annotation.ValidationParam;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Member;
import com.xss.service.MemberService;
import com.xss.service.SmsService;
import com.xss.service.WeixinService;
import com.xss.util.CommonUtil;
import com.xss.util.JWTUtil;
import com.xss.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  登录接口
 * @author zzl
 * @since 2018-09-03
 */
@RestController
@RequestMapping("/m/login")
@Api(description="身份认证模块")
public class LoginController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private SmsService smsService;

    @ApiOperation(value="手机密码登录", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("/pwd")
    @Log(description="前台密码登录接口:/m/login/pwd")
    @Pass
    public PublicResult<Map<String, Object>> loginPwd(@ValidationParam("username,password")@RequestBody JSONObject requestJson, HttpServletRequest request) throws Exception{
        PublicResult result = login(requestJson.getString("username"), requestJson.getString("password"), request);
        if (result.getResult() != PublicResultConstant.SUCCESS.getResult()) {
            return result;
        }
        Member member = (Member) result.getData();
        JSONObject memberJo = JsonUtil.toJSONObject(member, new String[]{"id","username","name", "mobile","headImage", "gender", "idFace", "idBackFace", "hukouIndex", "hukouPerson", "onePhone"});
        memberJo.put("nickName",
                new String(Base64.decodeBase64(
                        member.getNickName()), "utf-8"));
        String token = JWTUtil.sign(member.getUsername(), member.getPassword());
        memberJo.put("token", token);

        // 20181128 更新token，防止多重登录。
        member.setToken(token);
        memberService.update(member);

        return new PublicResult<>(PublicResultConstant.SUCCESS, memberJo);
    }

    /**
     * 登录验证，验证失败返回错误码，验证成功返回成功返回码（data为member对象）
     * @param username
     * @param password
     * @param request
     * @return
     */
    private PublicResult login(String username, String password, HttpServletRequest request)throws Exception{
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        Member member;
        int flag = CommonUtil.estimateRegType(username);
        if (flag == 1) {
            //手机号码
            member = memberService.findByMobile(username);
            if (null == member) {
                member = memberService.findByUsername(username);
            }
        } else {
            //用户名登录
            member = memberService.findByUsername(username);
        }
        if (member == null) {
            return new PublicResult<>(PublicResultConstant.INVALID_USER, null);
        }
        if (!member.getIsEnabled()) {
            return new PublicResult<>(PublicResultConstant.ACCOUNT_DISABLED, null);
        }
        if (member.getIsLocked()) {
            return new PublicResult<>(PublicResultConstant.ACCOUNT_LOCKED, null);
        }

        if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
            return new PublicResult<>(PublicResultConstant.INVALID_USERNAME_PASSWORD, null);
        }
        if(member.getLoginIp()!=null){
            member.setLastLoginIp(member.getLoginIp());
        }
        if(member.getLoginDate()!=null){
            member.setLastLoginDate(member.getLoginDate());
        }
        member.setLoginIp(request.getRemoteAddr());
        member.setLoginDate(new Date());
        member.setLoginFailureCount(0);
        String token = request.getHeader("Authorization");
        String userNo = JWTUtil.getUserNo(token);
        Member user = memberService.findByUsername(userNo);
        String openId = null;
        if (null != user){
            openId = user.getOpenId();
        }
        System.out.println("wx member openId : " + openId);
        Member memberWx = StringUtils.isBlank(openId) ? null : user;
        if (null != memberWx && !memberWx.getId().equals(member.getId()) && StringUtils.isBlank(memberWx.getMobile())){
            //String nickName = Base64.encodeBase64String(memberWx.getName().getBytes("utf-8"));
//            member.setName(nickName);
            member.setNickName(memberWx.getNickName());
            member.setGender(memberWx.getGender());
            member.setOpenId(memberWx.getOpenId());
            member.setHeadImage(memberWx.getHeadImage());
            member.setSubscribeDate(memberWx.getSubscribeDate());
            member.setSubscription(memberWx.getSubscription());

            memberService.delete(memberWx);

        }
        member = memberService.update(member);

        return new PublicResult<>(PublicResultConstant.SUCCESS, member);
    }

    @ApiOperation(value="微信公众号登录", notes="",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("/wx")
    @Log(description="前台微信公众号登录接口:/m/login/wx")
    public PublicResult<Map<String, Object>> loginByWxCode(String code, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("LoginController /m/login/wx, code:" + code);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(code)){
            return new PublicResult<>(PublicResultConstant.ERROR, map);
        }
        try {
//            String testInfo = "{\"openid\":\"oDtLj1X9tLtm7XJP-czSMKzgoxc8\",\"nickname\":\"阿亮\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"大兴\",\"province\":\"北京\",\"country\":\"中国\",\"headimgurl\":\"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eogrnzaYfkyf78A3NIGKd8IialJQvnf41fcrt3leic2BaibPuZdatYq1o4n2skBBf3Ogc2iaST8YsVTXA/132\",\"privilege\":[]}";
//            JSONObject userInfo = JSONObject.parseObject(testInfo);
            JSONObject userInfo = weixinService.getUserInfo("wx_snsapi_base", code);
            if (null != userInfo && StringUtils.isNotBlank(userInfo.getString("openid"))){
                Member member = memberService.mergeUserInfoByWeixin(userInfo, "mp", request);
                map.put("id", member.getId());
                map.put("username", member.getUsername());
                map.put("mobile", member.getMobile());
                map.put("name", member.getName());
                //map.put("nickName", member.getNickName());
                map.put("nickName", new String(Base64.decodeBase64(member.getNickName()), "utf-8"));
                String token = JWTUtil.sign(member.getUsername(), member.getPassword());
                map.put("token", token);

                // 20181128 更新token，防止多重登录。
                member.setToken(token);
                memberService.update(member);

                return new PublicResult<>(PublicResultConstant.SUCCESS, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PublicResult<>(PublicResultConstant.ERROR, map);

    }


    @ApiIgnore
    @RequestMapping(path = "/401",produces = "application/json;charset=utf-8")
    public PublicResult<String> unauthorized() {
        return new PublicResult<>(PublicResultConstant.UNAUTHORIZED, null);
    }
}
