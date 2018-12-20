package com.wxap.config;

import com.message.RequestMessage;
import com.message.ResponseMessage;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WXUtils {

	public static String signature(Map<String, String> parameters) {

		List<String> values = new ArrayList<String>();
		for (String value : parameters.values()) {
			values.add(value);
		}
		Collections.sort(values);
		String tmpStr = StringUtils.join(values, "");

		return DigestUtils.sha1Hex(tmpStr);
	}

	public static RequestMessage unmarshal(InputStream is) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(RequestMessage.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		return (RequestMessage) unmarshaller.unmarshal(is);
	}

	public static String marshaller(ResponseMessage rm) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ResponseMessage.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		// 去掉生成xml的默认报文头。
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		//TODO: zcl 20161116 mvn自动打包有错，暂时去掉  解决方案：参考 -XDignore.symbol.file  
		marshaller.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",
		        new CharacterEscapeHandler() {
			        @Override
			        public void escape(char[] ch, int start, int length, boolean isAttVal, Writer writer)
			                throws IOException {
				        writer.write(ch, start, length);
			        }
		        });

		StringWriter sw = new StringWriter();
		marshaller.marshal(rm, sw);

		return sw.toString();
	}
	
	/**
	 * 获取关注用户数
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static int getUserCount(String accessToken){
		int total = 0;
		try {
			JSONObject jo = new JSONObject();
			jo = User.getUserList(accessToken,null);
			total = jo.getInt("total");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

	public static String concatTemplateUrl(String redirectUrl){
		StringBuilder weixinUrl = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize");
		weixinUrl.append("?appid=").append(WeiXinConfig.getInstance().getAppId());
		try {
			redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
			System.out.println("redirectUrl---" + redirectUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		weixinUrl.append("&redirect_uri=").append(redirectUrl);
		weixinUrl.append("&response_type=").append("code");
		weixinUrl.append("&scope=").append("snsapi_userinfo");
		weixinUrl.append("&state=").append("STATE&connect_redirect=1#wechat_redirect");
		System.out.println("weixinUrl---" + weixinUrl.toString());
		return weixinUrl.toString();
	}
	public static void main(String[] args) {
//		ResponseMessage rmRet = new ResponseMessage();
//		rmRet.setFromUserName("2323423");
//		rmRet.setToUserName("345345345");
//		rmRet.setCreateTime(System.currentTimeMillis());
//		rmRet.setMsgType(MsgType.text.name());
//		rmRet.setContent("243234242");
//		try {
//			String res = WXUtils.marshaller(rmRet);
//			System.out.println("res----"+res);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
		//String redirectUrl = "http://www.028bmz.com/weixin/redirect.jhtml?redirectType=wx_snsapi_base&redirectUrl=https://easydeco.bayview-tech.com/client.html?appid=wx286627c1c8e266d5";
//		String redirectUrl = "https://feasydeco.bayview-tech.com/client.html?appid=wx286627c1c8e266d5";
		//concatTemplateUrl(redirectUrl);
//		System.out.println(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx286627c1c8e266d5&redirect_uri=http%3A%2F%2Fwww.028bmz.com%2Fwx%2Fredirect.jhtml%3FredirectType%3Dwx_snsapi_base&response_type=code&scope=snsapi_base&state=123#wechat_redirect"));
		try {
			String url = URLEncoder.encode("http://www.sptxmall.com/cdbmz/h5/index.jhtml","UTF-8");
			System.out.println(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
