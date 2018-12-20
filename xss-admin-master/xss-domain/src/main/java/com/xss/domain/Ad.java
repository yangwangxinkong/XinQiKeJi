/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity - 广告
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_ad")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_ad_sequence")
public class Ad extends OrderEntity {

	private static final long serialVersionUID = -1307743303786909390L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","cityCode","title","url","content","path","videoImage","type", "order"};

	/**
	 * 类型
	 */
	public enum Type{

		/** 文本 */       /** 图片 */       /**flash */
		text(0,"文本"), image(1,"图片"), flash(2,"flash");

		private int value;
		private String desc;

		Type(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}
		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

		public static Type findByValue(int value) {
			Type type = null;
			for (Type type1 : Type.values()){
				if (type1.value == value) {
					type = type1;
					break;
				}
			}
			return type;
		}
	}

	/** 标题 */
	private String title;

	/** 类型 */
	private Type type;

	/** 内容 */
	private String content;

	/** 路径 */
	private String path;

	/** 起始日期 */
	private Date beginDate;

	/** 结束日期 */
	private Date endDate;

	/** 链接地址 */
	private String url;

	/** 广告位 */
	private AdPosition adPosition;

	/** 视频图片 */
	private String videoImage;
	

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@NotNull
	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Lob
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@Length(max = 200)
	public String getPath() {
		return path;
	}

	/**
	 * 设置路径
	 * 
	 * @param path
	 *            路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取起始日期
	 * 
	 * @return 起始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置起始日期
	 * 
	 * @param beginDate
	 *            起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取结束日期
	 * 
	 * @return 结束日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置结束日期
	 * 
	 * @param endDate
	 *            结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	@Length(max = 200)
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
	 * 获取广告位
	 * 
	 * @return 广告位
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public AdPosition getAdPosition() {
		return adPosition;
	}

	/**
	 * 设置广告位
	 * 
	 * @param adPosition
	 *            广告位
	 */
	public void setAdPosition(AdPosition adPosition) {
		this.adPosition = adPosition;
	}

	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	/**
	 * 判断是否已结束
	 * 
	 * @return 是否已结束
	 */
	@Transient
	public boolean hasEnded() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	public String getVideoImage() {
		return videoImage;
	}

	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}


	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Ad ad =((Ad) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		jo.put("typeDesc", ad.getType().getDesc());
		if (null != ad.getBeginDate()) {
			jo.put("beginDate", DateUtil.format(ad.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
			jo.put("beginDateTime", ad.getBeginDate().getTime());
		}
		if (null != ad.getEndDate()) {
			jo.put("endDate", DateUtil.format(ad.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			jo.put("endDateTime", ad.getEndDate().getTime());

		}
		if (ArrayUtils.contains(params, "adPosition")) {
			AdPosition adPosition = ad.getAdPosition();
			if (null != adPosition) {
				JSONObject adpJo = JsonUtil.toJSONObject(adPosition, new String[]{"id", "name"});
				jo.put("adPosition", adpJo);
			}
		}

		return jo;
	}

}