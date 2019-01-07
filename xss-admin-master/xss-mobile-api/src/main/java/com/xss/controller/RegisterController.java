package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Configs;
import com.xss.domain.Member;
import com.xss.domain.enums.ShareCategory;
import com.xss.domain.enums.SmsResource;
import com.xss.domain.enums.SmsType;
import com.xss.service.ConfigsService;
import com.xss.service.MemberService;
import com.xss.service.ShareService;
import com.xss.service.SmsService;
import com.xss.util.JWTUtil;
import com.xss.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  注册接口
 * @author zzl
 * @since 2018-09-03
 */
@RestController
@RequestMapping("/m/register")
@Api(description="用户注册模块")
public class RegisterController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private ShareService shareService;


    @ApiOperation(value="手机密码注册", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("")
    @Log(description="前台手机密码注册接口:/m/register")
    @Pass
    public PublicResult<Map<String, Object>> register(@RequestParam String mobile, @RequestParam String password, @RequestParam String smsCode, Long mid, ShareCategory shareCategory, HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(smsCode)){
            map.put("msg", PublicResultConstant.PARAM_ERROR.getMsg());
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        JSONObject result = smsService.checkVerificationCode(mobile, SmsResource.mobile, SmsType.register, smsCode);
        if(null == result || result.getInteger("errcode") != 0){
            map.put("msg", result.getString("errmsg"));
            return new PublicResult<>(PublicResultConstant.SMS_CODE_ERROR, map);
        }
        Member member = new Member();
        //int random = ((int)((Math.random()*9+1)*1000));
        String nickName = "小豆社保" + System.currentTimeMillis()/1000;
        nickName = Base64.encodeBase64String(nickName.getBytes("utf-8"));
        member.setNickName(nickName);
        //member.setName(nickName);
        member.setUsername(mobile);
        member.setPassword(DigestUtils.md5Hex(password));
        member.setMobile(mobile);
        member.setPoint(0L);
        member.setAmount(BigDecimal.ZERO);
        member.setBalance(BigDecimal.ZERO);
        member.setIsEnabled(true);
        member.setIsLocked(false);
        member.setLoginFailureCount(0);
        member.setLockedDate(null);
        member.setRegisterIp(request.getRemoteAddr());
        member.setLoginIp(request.getRemoteAddr());
        member.setLoginDate(new Date());
        member.setNature(Member.Nature.buyer);//注册用户默认为买家
        member.setIndustry(null);
        member.setIsVip(false);
        member.setIsNew(true);

        // 20181128 更新token，防止多重登录。
        String token = JWTUtil.sign(member.getUsername(), member.getPassword());
        member.setToken(token);
        member.setType(Member.Type.def);
        member.setCityName(null);
        member.setShareBalance(BigDecimal.ZERO);
        member.setHasShareOrder(false);
        memberService.save(member);

        //分享者记录
        try {
            shareService.saveRegisterShare(member, mid, shareCategory);
        }catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject memberJo = JsonUtil.toJSONObject(member, new String[]{"id","username","name", "nickName", "mobile", "headImage"});
        memberJo.put("nickName", new String(Base64.decodeBase64(member.getNickName()), "utf-8"));
        //memberJo.put("token", JWTUtil.sign(member.getUsername(), member.getPassword()));

        memberJo.put("token", token);

        return new PublicResult<>(PublicResultConstant.SUCCESS, memberJo);
    }

    @ApiOperation(value="手机密码注册", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("findPassword")
    @Log(description="前台手机密码注册接口:/m/register/findPassword")
    @Pass
    public PublicResult<Map<String, Object>> findPassword(@RequestParam String mobile, @RequestParam String password, @RequestParam String smsCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(smsCode)){
            map.put("msg", PublicResultConstant.PARAM_ERROR.getMsg());
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        JSONObject result = smsService.checkVerificationCode(mobile, SmsResource.mobile, SmsType.register, smsCode);
        if(null == result || result.getInteger("errcode") != 0){
            map.put("msg", result.getString("errmsg"));
            return new PublicResult<>(PublicResultConstant.SMS_CODE_ERROR, map);
        }
        // 检查是否存在此电话号码
        Member member = memberService.findByMobile(mobile);
        if(member == null) {
            return new PublicResult<>(PublicResultConstant.INVALID_USER, null);
        } else {
            if(!member.getIsEnabled()) {
                return new PublicResult<>(PublicResultConstant.ACCOUNT_DISABLED, null);
            }

            member.setPassword(DigestUtils.md5Hex(password));
            memberService.update(member);
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

    @ApiOperation(value="短信推广注册", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("/share")
    @Log(description="前台短信推广注册接口:/m/register/share")
    @Pass
    public PublicResult<Map<String, Object>> shareRegister(@RequestParam String mobile, @RequestParam String name, @RequestParam String cityName, Long mid, ShareCategory shareCategory, HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map<String, Object> map = new HashMap<>();

        Member persistent = memberService.findByMobile(mobile);
        if (null != persistent && null != persistent.getId()){
            map.put("msg", "手机号码已经注册");
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, map);
        }

        Member member = new Member();
        String nickName = "小豆社保" + System.currentTimeMillis()/1000;
        nickName = Base64.encodeBase64String(nickName.getBytes("utf-8"));
        member.setNickName(nickName);
        member.setName(name);
        member.setUsername(mobile);
        member.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(mobile)));
        member.setMobile(mobile);
        member.setPoint(0L);
        member.setAmount(BigDecimal.ZERO);
        member.setBalance(BigDecimal.ZERO);
        member.setIsEnabled(true);
        member.setIsLocked(false);
        member.setLoginFailureCount(0);
        member.setLockedDate(null);
        member.setRegisterIp(request.getRemoteAddr());
        member.setLoginIp(request.getRemoteAddr());
        member.setLoginDate(new Date());
        member.setNature(Member.Nature.buyer);//注册用户默认为买家
        member.setIndustry(null);
        member.setIsVip(false);
        member.setIsNew(true);

        // 20181128 更新token，防止多重登录。
        String token = JWTUtil.sign(member.getUsername(), member.getPassword());
        member.setToken(token);
        member.setType(Member.Type.sms);
        member.setCityName(cityName);
        member.setShareBalance(BigDecimal.ZERO);
        if (null != mid) {
            member.setShareMember(memberService.find(mid));
            member.setHasShareOrder(false);
        }
        memberService.save(member);
        //分享者记录
        try {
            shareService.saveRegisterShare(member, mid, shareCategory);
        }catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject memberJo = JsonUtil.toJSONObject(member, new String[]{"id","username","name", "mobile", "headImage"});
        memberJo.put("nickName", new String(Base64.decodeBase64(member.getNickName()), "utf-8"));
        memberJo.put("token", token);

        return new PublicResult<>(PublicResultConstant.SUCCESS, memberJo);
    }

    @ApiIgnore
    @RequestMapping(path = "/401",produces = "application/json;charset=utf-8")
    public PublicResult<String> unauthorized() {
        return new PublicResult<>(PublicResultConstant.UNAUTHORIZED, null);
    }
}
