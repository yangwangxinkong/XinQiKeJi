package com.wxap.config;

import com.xss.util.ApiUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;


/**
 * 
 * 基础接口
 * 
 * @author Christ
 *
 */
public class Basic {

	/**
	 * 获取access_token
	 * 
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getAccessToken() throws IOException {
		WeiXinConfig config = WeiXinConfig.getInstance();
		MultiValueMap map = new MultiValueMap();
		map.put("grant_type", "client_credential");
		map.put("appid", config.getAppId());
		map.put("secret", config.getSecret());

		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/token", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}

	/**
	 * 获取微信服务器IP地址
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getIp(String accessToken) throws IOException {
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);

		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip", map, null);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}

	/**
	 * 上传多媒体文件
	 * 
	 * @param accessToken
	 * @param type
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static JSONObject mediaUpload(String accessToken, String type, File f) throws IOException {
		MultiValueMap map = new MultiValueMap();
		Part[] parts = { new StringPart("access_token", accessToken), new StringPart("type", type),
		        new FilePart("media", f) };

		String res = ApiUtils.postForObject("http://file.api.weixin.qq.com/cgi-bin/media/upload", map, parts);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}
	/**
	 * 获取jsapi_ticket
	 * 
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static JSONObject jsAPI(String accessToken, String type) throws IOException {
		MultiValueMap map = new MultiValueMap();
		Part[] parts = { new StringPart("access_token", accessToken), new StringPart("type", type)};

		String res = ApiUtils.postForObject("https://api.weixin.qq.com/cgi-bin/ticket/getticket", map, parts);

		if (StringUtils.isNotBlank(res)) {
			return JSONObject.fromObject(res);
		}

		return new JSONObject();
	}

	/**
	 * 下载多媒体文件
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @return
	 */
	public static byte[] mediaGet(String accessToken, String mediaId) {
		MultiValueMap map = new MultiValueMap();
		map.put("access_token", accessToken);
		map.put("media_id", mediaId);

		return ApiUtils.callForApi("http://file.api.weixin.qq.com/cgi-bin/media/get", map, null);
	}
	
	 public static void sign(String jsapi_ticket,String url, Model model)
	 {
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        model.addAttribute("url", url);
        model.addAttribute("jsapi_ticket", jsapi_ticket);
        model.addAttribute("nonceStr", nonce_str);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("signature", signature);
        

	 }
	 
	 /**
	  * 获取位置时需要根据jsapi_ticket获取signature
	  * @param jsapi_ticket
	  * @param url
	  * @return
	  */
	 public static JSONObject signature(String jsapi_ticket, String url)
	 {
		JSONObject jo = new JSONObject();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println("str:" + string1);
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
            jo.accumulate("appId", WeiXinConfig.getInstance().getAppId());
            jo.accumulate("noncestr", nonce_str);
            jo.accumulate("timestamp", timestamp);
            jo.accumulate("signature", signature);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return jo;
    }

	 public static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }

	    public static String create_nonce_str() {
	    	return UUID.randomUUID().toString();
	    }

	    public static String create_timestamp() {
	        return Long.toString(System.currentTimeMillis() / 1000);
	    }

	public static void wx(Model model, HttpServletRequest request)  {
		try{
			WXBaseControll wxBaseControll = WXBaseControll.getInstance();
			String token = wxBaseControll.getAccessToken();
			JSONObject jsApiTicket =  Basic.jsAPI(token,"jsapi");
			String jsapi_ticket = jsApiTicket.getString("ticket");
			System.out.println("jsapi_ticket-----------"+jsapi_ticket);
	//		String jsapi_ticket = "bxLdikRXVbTPdHSM05e5uyFZv8sfhIhDVn0Ye6-KUjkpn7Z7rqttxfKVa19Hcz8HPjR_aKH7MJZ06X1RkoA6Tw&noncestr";
	        
			String url = "http://" + request.getServerName() //服务器地址
			                    + request.getServletPath();      //请求页面或其他地址
			if(request.getQueryString()!=null && !request.getQueryString().equals(""))
				url = url  + "?" + (request.getQueryString()); //参数
			System.out.println("url================"+url);
			Basic.sign(jsapi_ticket, url, model);
			model.addAttribute("appId", WeiXinConfig.getInstance().getAppId());
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}

}
