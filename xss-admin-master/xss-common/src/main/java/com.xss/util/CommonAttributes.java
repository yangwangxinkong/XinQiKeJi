/*
 *  
 *  
 *  
 */
package com.xss.util;

/**
 * 公共参数
 * 
 * @author DannyZou
 * @version 1.0
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** jdudu.xml文件路径 */
	public static final String jdudu_XML_PATH = "/jdudu.xml";

	/** jdudu.properties文件路径 */
	public static final String jdudu_PROPERTIES_PATH = "/jdudu.properties";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}