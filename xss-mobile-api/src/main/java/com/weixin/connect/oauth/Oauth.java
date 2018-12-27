package com.weixin.connect.oauth;

import com.wxap.config.WeiXinConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Oauth {
	//获得code的请求地址
	public String authorize(String response_type,String state,String scope) throws UnsupportedEncodingException {
		return WeiXinConfig.getParam("weixin.qrconnect").trim() + "?appid="
				+ WeiXinConfig.getParam("weixin.qrconnect.appid").trim() + "&redirect_uri="
				+ URLEncoder.encode(WeiXinConfig.getParam("weixin.qrconnect.redirect_uri").trim(),"UTF-8")
				+ "&response_type=" + response_type
				+ "&scope="+scope
				+ "&state="+state
				+"#wechat_redirect";
	}
	//获得code的请求地址
	public String authorize_pc(String response_type,String state,String scope) throws UnsupportedEncodingException {
		return WeiXinConfig.getParam("weixin.qrconnect").trim() + "?appid="
				+ WeiXinConfig.getParam("weixin.oauth.appid").trim() + "&redirect_uri="
				+ URLEncoder.encode(WeiXinConfig.getParam("weixin.qrconnect.redirect_uri").trim(),"UTF-8")
				+ "&response_type=" + response_type
				+ "&scope="+scope
				+ "&state="+state
				+"#wechat_redirect";
	}
	public String authorizeM(String response_type,String state,String scope)  {
		return WeiXinConfig.getParam("weixin.m.qrconnect").trim() + "?appid="
				+ WeiXinConfig.getParam("weixin.m.qrconnect.appid").trim() + "&redirect_uri="
				+ WeiXinConfig.getParam("weixin.m.qrconnect.redirect_uri").trim()
				+ "&response_type=" + response_type
				+ "&scope="+scope
				+ "&state="+state
				+"#wechat_redirect";
	}
	//获得accessToken的请求地址
	public String accessToken(String code,String grant_type) {
		return WeiXinConfig.getParam("weixin.qrconnect.access_token").trim() + "?appid="
				+ WeiXinConfig.getParam("weixin.qrconnect.appid").trim() + "&secret="
				+ WeiXinConfig.getParam("weixin.qrconnect.secret").trim()
				+ "&code="+code
				+ "&grant_type="+grant_type;
				
	}
	public String accessTokenM(String code,String grant_type)  {
		return WeiXinConfig.getParam("weixin.m.qrconnect.access_token").trim() + "?appid="
				+ WeiXinConfig.getParam("weixin.m.qrconnect.appid").trim() + "&secret="
				+ WeiXinConfig.getParam("weixin.m.qrconnect.secret").trim()
				+ "&code="+code
				+ "&grant_type="+grant_type;
				
	}
	
	//获得userinfo的请求地址
		public String userInfo(String accessToken,String openid) {
			return WeiXinConfig.getParam("weixin.qrconnect.userinfo").trim() + "?access_token="
					+ accessToken + "&openid="
					+ openid
					+"&lang=zh_CN";
					
		}
		public String userInfoM(String accessToken,String openid)  {
			return WeiXinConfig.getParam("weixin.m.qrconnect.userinfo").trim() + "?access_token="
					+ accessToken + "&secret="
					+ openid;
					
		}
	
}
