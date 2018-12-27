package com.wxap.config;

import com.xss.util.ApiUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class TemplateMessage {
	
	/**
	 * 模板发送
	 * 
	 * @param accessToken
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static JSONObject send(String accessToken, String json) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json, "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, map, requestEntity);
		System.out.println("send   res==="+res);
		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	/**
	 * 模板添加
	 * 
	 * @param accessToken
	 * @param template_id_short
	 * @return
	 * @throws IOException
	 * OPENTM403107158
	 */
	public static JSONObject add(String accessToken, String template_id_short) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		String json = "{\"template_id_short\":"+template_id_short+"}";
		
		RequestEntity requestEntity = new StringRequestEntity(json, "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=" + accessToken, map, requestEntity);
		System.out.println("send   res==="+res);
		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	public static void main(String[] args) throws IOException {
//		 JSONObject accessToken = Basic.getAccessToken();
//		 System.out.println("access_token : " + accessToken);
//		 String token = accessToken.getString("access_token");
		String token = "I-DDv-5kWjzleT9YgpTROIMTcibOFbO_kFce96OnRFQWsmv9NLP3HvqW-onFcBPFddQYRa1QoI0gEXqf6bB4Plqa-ia3OG463OGPmOpHgio";
		
		String json = IOUtils.toString(new FileInputStream("template2.json"), "utf-8");
		
		send(token, json);
	}
	
}
