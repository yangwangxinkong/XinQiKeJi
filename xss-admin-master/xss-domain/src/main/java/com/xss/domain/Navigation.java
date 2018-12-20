/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity - 导航
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_navigation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_navigation_sequence")
public class Navigation extends OrderEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name","url"};

	private static final long serialVersionUID = -7635757647887646795L;

	/**
	 * 位置
	 */
	public enum Position {

		top(0,"顶部"), middle(1,"中间"), bottom(2,"底部");
		
		private int value;
		
	    private String desc;
	    
	    Position(int value, String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getDesc() {
	        return desc;
	    }
	    
	    public static Position findByValue(int value) {
	    	Position status = null;
	        
	        for (Position rs : Position.values()) {
	            if (rs.value == value) {
	                status = rs;
	                break;
	            }
	        }
	        
	        return status;
	    }
	}

	/** 名称 */
	private String name;

	/** 位置 */
	private Position position;

	/** 链接地址 */
	private String url;

	/** 是否新窗口打开 */
	private Boolean isBlankTarget;

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取位置
	 * 
	 * @return 位置
	 */
	@NotNull
	@Column(nullable = false)
	public Position getPosition() {
		return position;
	}

	/**
	 * 设置位置
	 * 
	 * @param position
	 *            位置
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getUrl() {
		return url;
	}

	/**
	 * 设置链接地址
	 * 
	 * @param url
	 *            链接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取是否新窗口打开
	 * 
	 * @return 是否新窗口打开
	 */
	@NotNull
	@Column(nullable = false)
	public Boolean getIsBlankTarget() {
		return isBlankTarget;
	}

	/**
	 * 设置是否新窗口打开
	 * 
	 * @param isBlankTarget
	 *            是否新窗口打开
	 */
	public void setIsBlankTarget(Boolean isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Navigation navigation =((Navigation) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		if (ArrayUtils.contains(params,"position")) {
			jo.put("position",(((Navigation) entity).getPosition().getDesc()));
		}
		if (ArrayUtils.contains(params,"isBlankTarget")) {
			if (((Navigation) entity).getIsBlankTarget()){
				jo.put("isBlankTarget","是");
			}else{
				jo.put("isBlankTarget","否");
			}
		}if (ArrayUtils.contains(params,"order")) {
			jo.put("order",(((Navigation) entity).getOrder()));
		}
		return jo;
	}
}