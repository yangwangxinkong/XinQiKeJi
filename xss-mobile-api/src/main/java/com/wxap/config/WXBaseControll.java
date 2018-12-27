package com.wxap.config;

import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class WXBaseControll {
	private static final WXBaseControll wxBaseControll = new WXBaseControll();
	private static final String redirectType ="?redirectType=wx_snsapi_base" ;
	private String accessToken;
	private Date accessTokenExpireDate;
	private String jsapi_ticket;
	private Date jsapi_ticket_expireDate;
	public static WXBaseControll getInstance() {
		return wxBaseControll;
	}
	static {
		try {
			wxBaseControll.refreshAccessToken();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public void refreshAccessToken() throws Exception {
//		Basic basic = new Basic();
		JSONObject jo = Basic.getAccessToken();
		System.out.println("token jo-----" + jo.toString());
		String access_token  = jo.getString("access_token");
		int expires_in  = jo.getInt("expires_in");
		Date expire_date = DateUtils.addSeconds(new Date(), 600);
//		Date expire_date = DateUtils.addSeconds(new Date(), expires_in-30);
		this.accessToken = access_token;
		this.accessTokenExpireDate = expire_date;
		System.out.println("re new access token-----------------------------");
	}
	public synchronized String getAccessToken() throws Exception {
		if(this.getAccessTokenExpireDate()==null || new Date().after(this.getAccessTokenExpireDate())){
			refreshAccessToken();
		}
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getAccessTokenExpireDate() {
		return accessTokenExpireDate;
	}
	public void setAccessTokenExpireDate(Date expireDate) {
		this.accessTokenExpireDate = expireDate;
	}
	
	public void refresh_Jsapi_ticket() throws Exception{
		String token = this.getAccessToken();
		JSONObject jsApiTicket =  Basic.jsAPI(token,"jsapi");
		String jsapi_ticket = jsApiTicket.getString("ticket");
		if(jsapi_ticket!=null){
			int expires_in  = jsApiTicket.getInt("expires_in");
			Date expire_date = DateUtils.addSeconds(new Date(), expires_in-30);
			this.jsapi_ticket = jsapi_ticket;
			this.jsapi_ticket_expireDate = expire_date;
		}
		System.out.println("re new Jsapi_ticket-----------------------------");
	}
	
	public synchronized String getJsapi_ticket() throws Exception {
		
		if(this.getJsapi_ticket_expireDate()==null || new Date().after(this.getJsapi_ticket_expireDate())){
			try {
				refresh_Jsapi_ticket();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsapi_ticket;
	}
	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}
	public Date getJsapi_ticket_expireDate() {
		return jsapi_ticket_expireDate;
	}
	public void setJsapi_ticket_expireDate(Date jsapi_ticket_expireDate) {
		this.jsapi_ticket_expireDate = jsapi_ticket_expireDate;
	}
	
	public JSONObject signature(String url){
		try {
			return Basic.signature(getJsapi_ticket(), url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encodeAndJoin(String url) throws UnsupportedEncodingException{
//			url = URLEncoder.encode(url, "UTF-8");
//			WeiXinConfig weiXinConfig = WeiXinConfig.getInstance();
			return WXUtils.concatTemplateUrl(url+redirectType);
		}
}
