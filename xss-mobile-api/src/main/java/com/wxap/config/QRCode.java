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
 * @author zcl
 *
 */
public class QRCode {
	
	/**
	 * 生成二维码
	 * 
	 * @param accessToken
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static JSONObject create(String accessToken, String json) throws IOException {
		
		MultiValueMap map = new MultiValueMap();
		
		RequestEntity requestEntity = new StringRequestEntity(json, "application/json", ApiUtils.encoding);
		
		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken, map, requestEntity);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	
	
	
	public static void main(String[] args) throws IOException {
//		 JSONObject accessToken = Basic.getAccessToken();
//		 System.out.println("access_token : " + accessToken);
//		 String token = accessToken.getString("access_token");
//		String menuJson = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": zcl}}}";
//		System.out.println(QRCode.create(token, menuJson));
	}
}
