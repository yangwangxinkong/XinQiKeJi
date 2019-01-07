/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 用户反馈
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_feedback")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_feedback_sequence")
public class Feedback extends BaseEntity {

	private static final long serialVersionUID = -1307743303786909390L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id","phone","content","type"};

	/**
	 * 类型
	 */
	public enum Type{

		fb0(0,"操作难用"), fb1(1,"操作失败"), fb2(2,"加载缓慢"), fb3(3,"产品建议"), fb4(4,"其他");

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

	/** 手机 */
	private String phone;

	/** 内容 */
	private String content;

	/** 类型 */
	private Type type;

	/** 商品图片 */
	private List<FeedbackImage> feedbackImages = new ArrayList<FeedbackImage>();

	/** 所属用户 */
	private Member member;

	@NotNull
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_feedback_image")
	public List<FeedbackImage> getFeedbackImages() {
		return feedbackImages;
	}

	public void setFeedbackImages(List<FeedbackImage> feedbackImages) {
		this.feedbackImages = feedbackImages;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		Feedback feedback =((Feedback) entity);
		JSONObject jo = super.convertEntity(feedback,DEFAULT_JSON_PARAMS);
		jo.put("typeDesc", feedback.getType().getDesc());
		jo.put("createDate", DateUtil.format(feedback.getCreateDate()));
		if (null != feedback.getFeedbackImages() && !feedback.getFeedbackImages().isEmpty()) {
			JSONArray images = new JSONArray();
			for(FeedbackImage image : feedback.getFeedbackImages()) {
				JSONObject imageJo = new JSONObject();
				imageJo.put("image", image.getImage());
				images.add(imageJo);
			}
			jo.put("images", images);
		}
		if (null != feedback.getMember()) {
			jo.put("member", JsonUtil.toJSONObject(feedback.getMember(), new String[]{"id", "username", "mobile"}));
		}

		return jo;
	}

}