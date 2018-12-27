package com.wxap.config;

import com.xss.util.ApiUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;


/**
 * 用户管理
 * 
 * @author Christ
 *
 */
public class User {
	
	/**
	 * 网页授权获取用户基本信息  获取openid
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getOpenId_web(String code) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("appid", WeiXinConfig.getInstance().getAppId());
		map.put("secret", WeiXinConfig.getInstance().getSecret());
		map.put("code", code);
		map.put("grant_type", "authorization_code");
		System.out.println("appid--" + WeiXinConfig.getInstance().getAppId() + "secret--" + WeiXinConfig.getInstance().getSecret());
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/sns/oauth2/access_token", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 网页授权获取用户基本信息  获取openid
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getOpenId_pc(String code) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("appid", WeiXinConfig.getInstance().getOauthAppid());
		map.put("secret", WeiXinConfig.getInstance().getOauthSecret());
		map.put("code", code);
		map.put("grant_type", "authorization_code");
		System.out.println("oauthAppid--" + WeiXinConfig.getInstance().getOauthAppid() + "--oauthSecret--" + WeiXinConfig.getInstance().getOauthSecret());
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/sns/oauth2/access_token", map, null);
		
		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}
		
		return new JSONObject();
	}
	
	/**
	 * 根据openid，判断用户是否关注了服务号
	 * 
	 * @param accessToken
	 * @param openid
	 * @return
	 * @throws IOException
	 */
	public static boolean hasFollow(String accessToken,String openid) throws Exception {
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.accumulate("openid", openid);
		json1.accumulate("lang", "zh-CN");
		array.add(json1);
		json.accumulate("user_list", array);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token="+accessToken, map, requestEntity);

		JSONObject returnjo = new JSONObject();
		
		if (StringUtils.isNotBlank(res)) {
			returnjo = JSONObject.fromObject(res);
		}
		if(returnjo.containsKey("user_info_list")){
			int subscribe = returnjo.getJSONArray("user_info_list").getJSONObject(0).getInt("subscribe");
			return subscribe==0?false:true;
		}
		return false;
	}
	
	/**
	 * 设置备注名
	 * 
	 * @param accessToken
	 * @param openid
	 * @param remark
	 * @return
	 * @throws IOException
	 */
	public static JSONObject updateRemark(String accessToken, String openid, String remark) throws IOException {
		JSONObject json = new JSONObject();
		json.accumulate("openid", openid);
		json.accumulate("remark", remark);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * 
	 * @param accessToken
	 * @param openid
	 * @param lang
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getUser(String accessToken, String openid, String lang) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);
		map.put("openid", openid);
		map.put("lang", lang);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/user/info", map, null);
		System.out.println(res.toString());
		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	/**
	 * 用于用户授权获得用户基本信息
	 * @param accessToken
	 * @param openid
	 * @param lang
	 * @return
	 * @throws IOException
	 * @作者：lwz
	 * @创建时间：2016-7-25下午2:48:54
	 */
	public static JSONObject getUserInfo(String accessToken, String openid, String lang) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);
		map.put("openid", openid);
		map.put("lang", lang);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/sns/userinfo", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 获取用户列表
	 * 
	 * @param accessToken
	 * @param openid
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getUserList(String accessToken, String openid) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);
		
		if (StringUtils.isNotBlank(openid)) {
			map.put("next_openid", openid);
		}
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/user/get", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception {
//		 JSONObject accessToken = Basic.getAccessToken();
//		 System.out.println("access_token : " + accessToken);
//		 String token = accessToken.getString("access_token");
		String token = "I-DDv-5kWjzleT9YgpTROIMTcibOFbO_kFce96OnRFQWsmv9NLP3HvqW-onFcBPFddQYRa1QoI0gEXqf6bB4Plqa-ia3OG463OGPmOpHgio";
//		JSONObject userList = User.getUserList(token, "");
//		System.out.println(userList);
		
		String openid = "oL1WwjufrARL2ITHuF_3bH-bQI_0";
//		JSONObject user = User.getUser(openid);
//		System.out.println(user);
		
//		String remark = "王凯";
//		JSONObject remarkJo = User.updateRemark(token, openid, remark);
//		System.out.println(remarkJo);
	}
	
}
