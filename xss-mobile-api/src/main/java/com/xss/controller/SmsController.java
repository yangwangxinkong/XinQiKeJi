package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.enums.SmsResource;
import com.xss.domain.enums.SmsType;
import com.xss.service.MemberService;
import com.xss.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 短信业务入口
 * @author zzl
 *
 */
@RestController
@RequestMapping("/m/sms")
@Api(description="短信接口")
public class SmsController {
	
	public static final Logger logger = LoggerFactory.getLogger(SmsController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private SmsService smsService;

	/**
	 * 异步发送短信验证码
	 * @param mobile
	 * @param resource
	 * @param type
	 * @param mobile
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ApiOperation(value = "异步发送短信验证码",notes = "异步发送短信验证码",httpMethod = "GET",produces = "application/json")
	@GetMapping("/sendCode")
	@Log(description="异步发送短信验证码接口:/m/sms/sendCode")
	public PublicResult<Object> sendSmsCode(@RequestParam String mobile, @RequestParam(defaultValue = "0") Integer resource,
												@RequestParam(defaultValue = "1") Integer type, HttpServletRequest request)throws UnsupportedEncodingException {
		/************************Begin 图片验证码安全校验**************************/
//		String ctoken = request.getHeader("ctoken");
//		String vrifyCode = request.getParameter("vrifyCode");
//		Captcha captcha  = captchaService.getCaptchaDao().findCaptchaByToken(ctoken);
//		if (!vrifyCode.equals(captcha.getCaptcha())) {
//			return new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "图片验证码不正确");
//		}
		/************************End 图片验证码安全校验**************************/
		/************************Begin 单IP访问限制安全校验**********************/
//		String ip = WebUtils.getIpAddr(request);
//		SmsLog querySmsLog = new SmsLog();
//		querySmsLog.setIp(ip);
//		Date endTime = new Date();
//		Date beginTime = DateUtil.addMintus(endTime, -1);
//		Long count = smsLogService.count(querySmsLog, beginTime, endTime);
//		try {
//			if (null != count && count >= SettingUtils.get().getIpSendSmsCountLimit()) {
//				result.setCode(0);
//				result.setMessage("超出访问限制！");
//				return result;
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		/************************End 单IP访问限制安全校验************************/
		
		String random = RandomStringUtils.randomNumeric(6);
		Boolean exist = memberService.mobileExists(mobile);
		if (exist && 1 == type) {
			return new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "该号码已经注册，不能重复注册！");
		} else {
			SmsType smsType = SmsType.findByValue(type);
			SmsResource smsResource = SmsResource.findByValue(resource);
			//调用短信模块
			JSONObject sendResult = smsService.sendVerificationCode(mobile, smsResource, smsType);
			if (null != sendResult ){
				if (sendResult.getInteger("errcode") == 0){
					return new PublicResult<Object>(PublicResultConstant.SUCCESS, "短信验证码已发送");
				}else{
					return new PublicResult<Object>(PublicResultConstant.ERROR, sendResult.getString("errmsg"));
				}
			}else{
				return new PublicResult<Object>(PublicResultConstant.ERROR, "获取验证码失败，请重新再试！");
			}
		}
	}

	/**
	 * 异步发送短信验证码
	 * @param mobile
	 * @param resource
	 * @param type
	 * @param mobile
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ApiOperation(value = "异步发送短信验证码",notes = "异步发送短信验证码",httpMethod = "GET",produces = "application/json")
	@GetMapping("/sendSmsCodeByFindPassword")
	@Log(description="异步发送短信验证码接口:/m/sms/sendCode")
	public PublicResult<Object> sendSmsCodeByFindPassword(@RequestParam String mobile, @RequestParam(defaultValue = "0") Integer resource,
											@RequestParam(defaultValue = "1") Integer type, HttpServletRequest request)throws UnsupportedEncodingException {

		String random = RandomStringUtils.randomNumeric(6);
		Boolean exist = memberService.mobileExists(mobile);
		if (!exist && 1 == type) {
			return new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "该号码未注册！");
		} else {
			SmsType smsType = SmsType.findByValue(type);
			SmsResource smsResource = SmsResource.findByValue(resource);
			//调用短信模块
			JSONObject sendResult = smsService.sendVerificationCode(mobile, smsResource, smsType);
			if (null != sendResult ){
				if (sendResult.getInteger("errcode") == 0){
					return new PublicResult<Object>(PublicResultConstant.SUCCESS, "短信验证码已发送");
				}else{
					return new PublicResult<Object>(PublicResultConstant.ERROR, sendResult.getString("errmsg"));
				}
			}else{
				return new PublicResult<Object>(PublicResultConstant.ERROR, "获取验证码失败，请重新再试！");
			}
		}
	}

	/**
	 * 验证手机号、验证码
	 * 
	 * @param mobile
	 * @param type
	 *            1:注册 2:找回密码 3:修改手机号
	 * @return
	 */
	@ApiOperation(value = "验证手机号、验证码",notes = "验证手机号、验证码",httpMethod = "GET",produces = "application/json")
	@GetMapping("/checkCode")
	@Log(description="验证手机号和验证码接口:/m/sms/checkCode")
	public PublicResult<Object> checkCode(@RequestParam String mobile,
                                            @RequestParam(defaultValue = "2") Integer resource, @RequestParam(defaultValue = "1") Integer type,
                                            @RequestParam String verificationCode) {
		//调用短信模块
		JSONObject sendResult = smsService.checkVerificationCode(mobile, SmsResource.findByValue(resource), SmsType.findByValue(type), verificationCode);
		if (null != sendResult ){
			if (sendResult.getInteger("errcode") == 0){
				return new PublicResult<Object>(PublicResultConstant.SUCCESS, "验证通过");
			}else{
				return new PublicResult<Object>(PublicResultConstant.ERROR, sendResult.getString("errmsg"));
			}
		}else{
			return new PublicResult<Object>(PublicResultConstant.ERROR, "验证码不正确");
		}
	}
}
