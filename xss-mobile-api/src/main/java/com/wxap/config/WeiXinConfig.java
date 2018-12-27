package com.wxap.config;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Properties;


public class WeiXinConfig {
	
	private static final WeiXinConfig weiXinConfig = new WeiXinConfig();
	
	private static final String config_file_location = "weixin.properties";
	private static Properties p = new Properties();
	static {
		InputStream is = null;
		try {
			is = WeiXinConfig.class.getClassLoader().getResourceAsStream(config_file_location);
			 p = new Properties();
			p.load(is);
			weiXinConfig.setAppId(p.getProperty("weixin.appId"));
			weiXinConfig.setSecret(p.getProperty("weixin.secret"));
			weiXinConfig.setAppKey(p.getProperty("weixin.appKey"));
			weiXinConfig.setToken(p.getProperty("weixin.token"));
			weiXinConfig.setPartner(p.getProperty("weixin.partner"));
			weiXinConfig.setPartnerKey(p.getProperty("weixin.partnerKey"));
			weiXinConfig.setNotifyUrl(p.getProperty("weixin.notifyUrl"));
			weiXinConfig.setLogingDir(p.getProperty("weixin.logingDir"));
			weiXinConfig.setOauthAppid(p.getProperty("weixin.oauth.appid"));
			weiXinConfig.setOauthSecret(p.getProperty("weixin.oauth.secret"));
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	public static String getParam(String key){
		return p.getProperty(key);
	}
	private String appId;
	private String secret;
	private String appKey;
	private String token;
	private String partner;
	private String partnerKey;
	private String notifyUrl;
	private String logingDir;
	private String oauthAppid;
	private String oauthSecret;
	
	public String getAppId() {
    	return appId;
    }

	public void setAppId(String appId) {
    	this.appId = appId;
    }

	public String getSecret() {
    	return secret;
    }

	public void setSecret(String secret) {
    	this.secret = secret;
    }

	public String getAppKey() {
    	return appKey;
    }

	public void setAppKey(String appKey) {
    	this.appKey = appKey;
    }
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

//	public String getPartner() {
//    	return partner;
//    }
//
//	public void setPartner(String partner) {
//    	this.partner = partner;
//    }
//
//	public String getPartnerKey() {
//    	return partnerKey;
//    }
//
//	public void setPartnerKey(String partnerKey) {
//    	this.partnerKey = partnerKey;
//    }
//	
//	
//	public String getKeyValue() {
//		return keyValue;
//	}
//
//	public void setKeyValue(String keyValue) {
//		this.keyValue = keyValue;
//	}
//	
//
//
//	
//	public String getGoInterImageUrl() {
//		return goInterImageUrl;
//	}
//
//	public void setGoInterImageUrl(String goInterImageUrl) {
//		this.goInterImageUrl = goInterImageUrl;
//	}
//
//	public String getGoInterUrl() {
//		return goInterUrl;
//	}
//
//	public void setGoInterUrl(String goInterUrl) {
//		this.goInterUrl = goInterUrl;
//	}

	public static WeiXinConfig getInstance() {
		return weiXinConfig;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getLogingDir() {
		return logingDir;
	}

	public void setLogingDir(String logingDir) {
		this.logingDir = logingDir;
	}
	
	public String getOauthAppid() {
		return oauthAppid;
	}

	public void setOauthAppid(String oauthAppid) {
		this.oauthAppid = oauthAppid;
	}

	public String getOauthSecret() {
		return oauthSecret;
	}

	public void setOauthSecret(String oauthSecret) {
		this.oauthSecret = oauthSecret;
	}

	public static void main(String[] args) {
		System.out.println(p.getProperty("weixin.appId"));
	}
}
