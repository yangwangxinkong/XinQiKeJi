package com.xss.domain;

import javax.persistence.*;

@Entity
//@Table(name = "xx_wx_click_value")
//@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_wx_click_value_sequence")
public class WxClickValue extends OrderEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 标题 */
	private String title;     	
	/** 描述 */
	private String content;    
	/** 广告图片路径 */
	private String imagePath; 
	/** 广告跳转路径 */
	private String url;
	/** 是否授权链接 */
	private WxMenu.Category category;
	private WxClickKey wxClickKey;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public WxMenu.Category getCategory() {
		return category;
	}

	public void setCategory(WxMenu.Category category) {
		this.category = category;
	}

	@ManyToOne(fetch= FetchType.LAZY)
	public WxClickKey getWxClickKey() {
		return wxClickKey;
	}

	public void setWxClickKey(WxClickKey wxClickKey) {
		this.wxClickKey = wxClickKey;
	}
	
}
