package com.xss.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "xx_wx_click_key")
//@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_wx_click_key_sequence")
public class WxClickKey extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum ClickType{
		PICTURE("图文"), WORD("文字");
		
		private String description;

		ClickType(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }
	}
	
	/** 名称 */
	private String name;     
	/** key值 */
	private String keyString;    
	/** 广告类别：0"图文"   1"文字" */
	private ClickType clickType;
	/** 点击率 */
	private Integer clickNum;  
	
	
	private List<WxClickValue> wxClickValueList = new ArrayList<WxClickValue>();


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getKeyString() {
		return keyString;
	}


	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}


	public ClickType getClickType() {
		return clickType;
	}


	public void setClickType(ClickType clickType) {
		this.clickType = clickType;
	}


	public Integer getClickNum() {
		return clickNum;
	}


	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	@OneToMany(mappedBy = "wxClickKey", fetch = FetchType.LAZY)
	@OrderBy(value=" createDate ASC ")
	public List<WxClickValue> getWxClickValueList() {
		return wxClickValueList;
	}

	public void setWxClickValueList(List<WxClickValue> wxClickValueList) {
		this.wxClickValueList = wxClickValueList;
	}
	
}
