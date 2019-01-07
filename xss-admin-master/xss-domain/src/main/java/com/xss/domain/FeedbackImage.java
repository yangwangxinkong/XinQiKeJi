/*
 *  
 *  
 *  
 */
package com.xss.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Entity - 反馈图片
 * 
 * @author DannyZou
 * @version 1.0
 */
@Embeddable
public class FeedbackImage implements Serializable, Comparable<FeedbackImage> {

	private static final long serialVersionUID = -673883300094536107L;
	/** 原图片 */
	private String image;

	/** 排序 */
	private Integer order;

	/** 文件 */
	private MultipartFile file;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	@Min(0)
	@Column(name = "orders")
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 获取文件
	 * 
	 * @return 文件
	 */
	@Transient
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * 设置文件
	 * 
	 * @param file
	 *            文件
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param feedbackImage
	 *            商品图片
	 * @return 比较结果
	 */
	@Override
	public int compareTo(FeedbackImage feedbackImage) {
		return new CompareToBuilder().append(getOrder(), feedbackImage.getOrder()).toComparison();
	}

}