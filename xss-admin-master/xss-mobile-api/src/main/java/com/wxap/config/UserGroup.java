package com.wxap.config;

import com.xss.util.ApiUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;


/**
 * 用户分组管理
 * 
 * @author Christ
 *
 */
public class UserGroup {
	
	/**
	 * 创建分组
	 * 
	 * @param accessToken
	 * @param groupName
	 * @return
	 * @throws IOException
	 */
	public static JSONObject createGroup(String accessToken, String groupName) throws IOException {
		JSONObject json = new JSONObject();
		JSONObject group = new JSONObject();
		json.accumulate("group", group);
		group.accumulate("name", groupName);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/create?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 修改分组名
	 * 
	 * @param accessToken
	 * @param groupId
	 * @param groupName
	 * @return
	 * @throws IOException
	 */
	public static JSONObject updateGroup(String accessToken, String groupId, String groupName) throws IOException {
		JSONObject json = new JSONObject();
		JSONObject group = new JSONObject();
		json.accumulate("group", group);
		group.accumulate("id", groupId);
		group.accumulate("name", groupName);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/update?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 查询所有分组
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getGroup(String accessToken) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/get", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 查询用户所在分组
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getUserGroup(String accessToken, String openId) throws IOException {
		JSONObject json = new JSONObject();
		json.accumulate("openid", openId);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 移动用户分组
	 * 
	 * @param accessToken
	 * @param openId
	 * @param toGroupId
	 * @return
	 * @throws IOException
	 */
	public static JSONObject updateUserGroup(String accessToken, String openId, String toGroupId) throws IOException {
		JSONObject json = new JSONObject();
		json.accumulate("openid", openId);
		json.accumulate("to_groupid", toGroupId);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 批量移动用户分组
	 * 
	 * @param accessToken
	 * @param openIdList
	 * @param toGroupId
	 * @return
	 * @throws IOException
	 */
	public static JSONObject batchUpdateUserGroup(String accessToken, List<String> openIdList, String toGroupId) throws IOException {
		JSONObject json = new JSONObject();
		json.accumulate("openid", openIdList);
		json.accumulate("to_groupid", toGroupId);
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	public static void main(String[] args) throws IOException {
//		 JSONObject accessToken = Basic.getAccessToken();
//		 System.out.println("access_token : " + accessToken);
//		 String token = accessToken.getString("access_token");
		String token = "m7f8EwDoWmDMfqYUt7b0uIC77LcA1P9LSSJ6nUsER-tmrLLj4iOzBTg_G48n59rSRXB9WQpWWSk-4_G1AQzmyFg_fYSBUxOfDC49HyWi6Ts";
		JSONObject group = UserGroup.createGroup(token, "测试");
		System.out.println(group);
		
		JSONObject group2 = UserGroup.getGroup(token);
		System.out.println(group2);
	}
	
}
