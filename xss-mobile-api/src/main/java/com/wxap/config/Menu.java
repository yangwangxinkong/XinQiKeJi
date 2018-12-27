package com.wxap.config;


import com.xss.util.ApiUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;


/**
 * 自定义菜单管理
 * 
 * @author Christ
 *
 */
public class Menu {
	
	/**
	 * 自定义菜单创建接口
	 * 
	 * @param accessToken
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static JSONObject createMenu(String accessToken, String json) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json, "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 自定义菜单查询接口
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getMenu(String accessToken) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken, map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 自定义菜单查询接口
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static JSONObject deleteMenu(String accessToken) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken, map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	public static void main(String[] args) throws IOException {
//		 JSONObject accessToken = Basic.getAccessToken();
//		 System.out.println("access_token : " + accessToken);
//		 String token = accessToken.getString("access_token");
//		JSONObject menu = Menu.getMenu(token);
//		System.out.println(menu);
//		 //String json = "{\"button\":[{\"type\":\"click\",\"name\":\"我要上网\",\"key\":\"V1001_TODAY_MUSIC\",\"sub_button\":[]}}";
//		// Menu.createMenu(token, json);
//		String menuJson = "{\"button\":[{\"name\":\"最新活动\",\"sub_button\":[{\"type\":\"click\",\"name\":\"八周年庆典\",\"key\":\"V1001_TODAY_INTER1\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"会议室预定\",\"url\":\"http://cs.ecqun.com/mobile/rand?id=690485scheme=1\",\"sub_button\":[]},{\"type\":\"click\",\"name\":\"电话咨询\",\"key\":\"menu_1289\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"微信咨询\",\"url\":\"http://cs.ecqun.com/mobile/rand?id=690485\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"微官网\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/\",\"sub_button\":[]}]},{\"name\":\"产品服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"服务式办公室\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/801/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"连锁会议室\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/802/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"汉督国际中心\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/821/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"项目地址\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/803/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"维权\",\"url\":\"https://mp.weixin.qq.com/payfb/payfeedbackindex?appid=wx3ebf519e38d03ad9#wechat_webview_type=1&wechat_redirect\",\"sub_button\":[]}]},{\"type\":\"click\",\"name\":\"我要上网\",\"key\":\"V1001_TODAY_INTER\",\"sub_button\":[]}]}";
//		//String menuJson = "{\"menu\":{\"button\":[{\"name\":\"预定咨询\",\"sub_button\":[{\"type\":\"view\",\"name\":\"会议室预定\",\"url\":\"http://cs.ecqun.com/mobile/rand?id=690485scheme=1\",\"sub_button\":[]},{\"type\":\"click\",\"name\":\"电话咨询\",\"key\":\"menu_1289\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"微信咨询\",\"url\":\"http://cs.ecqun.com/mobile/rand?id=690485\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"微官网\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/\",\"sub_button\":[]}]},{\"name\":\"产品服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"服务式办公室\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/801/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"连锁会议室\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/802/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"汉督国际中心\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/821/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"项目地址\",\"url\":\"http://weixin.vfood.com/api/f39ba0d696/web/column/803/\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"维权\",\"url\":\"https://mp.weixin.qq.com/payfb/payfeedbackindex?appid=wx3ebf519e38d03ad9#wechat_webview_type=1&wechat_redirect\",\"sub_button\":[]}]},{\"type\":\"click\",\"name\":\"我要上网\",\"key\":\"V1001_TODAY_INTER\",\"sub_button\":[]}]}}";
//		System.out.println(Menu.createMenu(token, menuJson));
	}
}
