package com.wxap.util;

import com.wxap.config.WeiXinConfig;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyStore;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TenpayUtils {
	public static final String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static final String refund = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
    public static String callForApi(String url, String body) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);

        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        post.setRequestBody(body);

        client.executeMethod(post);

        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();

        

        String result = new String(post.getResponseBodyAsString());
        post.releaseConnection();
        return result;
    }
    public static String callForApiByCert(String url, String body) throws Exception {
		String cert = "/usr/local/tomcat/webapps/ROOT/WEB-INF/classes/conf/apiclient_cert.p12";
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(cert));
        try {
            keyStore.load(instream, WeiXinConfig.getInstance().getPartner().toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, WeiXinConfig.getInstance().getPartner().toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
		
        String result = "";
        try {

            HttpPost httppost = new HttpPost(url);

            
            StringEntity entity1 = new StringEntity(body,"utf-8");//解决中文乱码问题
            //entity.setContentEncoding("utf-8");
            httppost.setEntity(entity1);    
            
            System.out.println("executing request" + httppost.getRequestLine());
            
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        System.out.println(text);
                        result += text;
                    }
                   
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }
    
    public static String refundorder(String out_trade_no, String out_refund_no, BigDecimal totalFee) throws Exception {
    	//totalFee = new BigDecimal("2");
		String noncestr = Sha1Util.getNonceStr();
		String fee = String.valueOf(totalFee.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
		SortedMap<String, String> respPackage = new TreeMap<String, String>();
		respPackage.put("appid", WeiXinConfig.getInstance().getAppId());
		respPackage.put("mch_id", WeiXinConfig.getInstance().getPartner());
		respPackage.put("nonce_str", noncestr);
		respPackage.put("out_trade_no", out_trade_no);
		respPackage.put("out_refund_no", out_refund_no);  
		respPackage.put("total_fee", fee);
		respPackage.put("refund_fee", fee);
		respPackage.put("op_user_id", WeiXinConfig.getInstance().getPartner());

		
		String sign = createMd5Sign(respPackage, WeiXinConfig.getInstance().getPartnerKey());
		System.out.println("sign : " + sign);
		respPackage.put("sign", sign);
		
		
		String xml = mapToXml(respPackage);
		 
        String res = TenpayUtils.callForApiByCert(TenpayUtils.refund,xml);
		
		SAXReader saxReader = new SAXReader();
		System.out.println("refund : " + res);
		StringReader sr = new StringReader(res);
		Document doc = saxReader.read(sr);
		Node resultNode = doc.selectSingleNode("/xml/result_code");
		Node recNode = doc.selectSingleNode("/xml/return_code");
		Node remNode = doc.selectSingleNode("/xml/return_msg");
		if (recNode != null) {
			if ("SUCCESS".equalsIgnoreCase(recNode.getText()) && "OK".equalsIgnoreCase(remNode.getText())) {
				if ("SUCCESS".equalsIgnoreCase(resultNode.getText())) {
					return "SUCCESS";
				}else{
					Node error = doc.selectSingleNode("/xml/err_code_des");
					return error.getText();
				}
			}else{
				Node error = doc.selectSingleNode("/xml/err_code_des");
				return error.getText();
			}
		}
		return "FAIL";
	}
    public static String mapToXml(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (String key : map.keySet()) {
			if (!"sign".equals(key)) {
				sb.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
		}
		sb.append("<").append("sign").append(">").append(map.get("sign")).append("</").append("sign").append(">");
		sb.append("</xml>");
		return sb.toString();
	}
	
    public static String createMd5Sign(Map<String, String> map, String parnterKey) {
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key).append("=").append(map.get(key)).append("&");
		}
		sb.append("key=").append(parnterKey);
		System.out.println(sb.toString());
		return MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
	}
}
