package com.xss.util;

import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
	
	/**
	 * 随机获取UUID字符串(无中划线)
	 * 
	 * @return UUID字符串
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
//		return uuid.replaceAll("-", "");
	}
	/**
     * 随机获取字符串
     * 
     * @param length
     *            随机字符串长度
     * 
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        if (length <= 0) {
            return "";
        }
        char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
                'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
        }
        return stringBuffer.toString();
    }
	
	public static boolean isEmail(String str){
		boolean flag = false;
		//邮箱的正则表达式规则
        String emailRules = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern regex = Pattern.compile(emailRules);  
        Matcher matcher = regex.matcher(str); 
        flag = matcher.matches(); 
        
		return flag;
	}
	
	public static boolean isMobile(String str){
		boolean flag = false;
//		String mobileRules = "^(1(([35][0-9])|(47)|[8][0126789]))\\d{8}$";
//		String mobileRules = "^(1(([35][0-9])|([4][57])|[8][0123456789]))\\d{8}$";
		String mobileRules = "^(1)\\d{10}$";
		Pattern regex_ = Pattern.compile(mobileRules);  
        Matcher matcher_ = regex_.matcher(str);  
        flag = matcher_.matches();
        return flag;
	}

	//判断整数（int）
	public static boolean isInteger(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	//判断浮点数（double和float）
	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断会员注册类型 邮箱/手机号/普通用户名
	 * @param loginMemberName 会员登录账号名
	 * @return 0:代表邮箱账号 1:手机号账号 9:普通账号
	 * @see [类、类#方法、类#成员]
	 */
	public static int estimateRegType(String loginMemberName){
	    //邮箱的正则表达式规则
        String emailRules = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        //手机号的规则--与注册页面中的账户名规则保持一致
        //String mobileRules = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        //String mobileRules = "^((13[0-9])|(15[^4,\\D])|(18[0126789]))\\d{8}$";
//        String mobileRules = "^(1(([35][0-9])|([4][57])|[8][0123456789]))\\d{8}$";
        String mobileRules = "^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$";
        
        Pattern regex = Pattern.compile(emailRules);  
        Matcher matcher = regex.matcher(loginMemberName);  
        boolean flag = matcher.matches(); 
        
        Pattern regex_ = Pattern.compile(mobileRules);  
        Matcher matcher_ = regex_.matcher(loginMemberName);  
        boolean flag_ = matcher_.matches();
        if(flag){
            return 0;//邮箱
        }else if(flag_){
            return 1;//手机号
        }
	    return 9;//普通账号
	}

	/**
	 * 去掉文本中的html标签
	 *
	 * @param inputString
	 * @return
	 */
	public static String html2Text(String inputString) {
		if (StringUtils.isEmpty(inputString)) {
			return null;
		}
		String htmlStr = inputString;
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;

		Pattern p_html1;
		Matcher m_html1;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script,
					Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern
					.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern
					.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

			// 替换&amp;nbsp;
			textStr = textStr.replaceAll("&amp;", "").replaceAll("nbsp;", "");

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}
	
	public static String gernateSn(String prefix, int length){
		return prefix + getRandomString(length);
	}
	public static void main(String[] args){
		System.out.println(isMobile("18343917677"));
	}
}
