package com.qeweb.scm.basemodule.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 匹配检验信息工具类
 */
public class MatcherUtil {

	/**
	 * 判断是否是邮件格式
	 * @param str
	 * @return
	 */
	final public static boolean isEmail(String str) {
		if(StringUtils.isEmpty(str))
			return false;

		//Pattern.CASE_INSENSITIVE： 整体忽略大小写
		Pattern p = Pattern.compile("\\w+([\\.?\\-?]\\w+)*@(\\w+\\-?\\w+\\.)+[a-z]{2,3}", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 只能输入数字或字母或下划线
	 * @param str
	 * @return
	 */
	final public static boolean isWord(String str) {
		return isAllMatch(str, "\\w*");
	}
	
	/**
	 * 只能输入数字或字母
	 * @param str
	 * @return
	 */
	final public static boolean isCharAndNum(String str) {
		return isAllMatch(str, "[\\da-zA-Z]*");
	}

	/**
	 * 只能输入数字
	 * @author alex.yao 2011-1-28上午10:05:59
	 *
	 * @param str
	 * @return
	 */
	final public static boolean isNumber(String str) {
		return isAllMatch(str, "\\d*") || isDecimal(str);
	}
	
	public static boolean isDecimal(String str){  
	    return isAllMatch(str, "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+");  
	}  
	
	/**
	 * 是否是日期型
	 * @param str
	 * @return
	 */
	final public static boolean isDate(String str) {
		return isAllMatch(str, "(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)");
	}

	/**
	 * 验证数字(integer\double)
	*/
	final public static boolean isDouble(String str) {
		return StringUtils.convertToDouble(str) != null;
	}

	/**
	 * str全部匹配正则表达式
	 * @param str	目标字符串
	 * @param regx  正则表达式
	 * @return
	 */
	final public static boolean isAllMatch(String str, String regx) {
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);

		return m.matches();
	}

	/**
	 * str部分匹配正则表达式
	 * @param str	目标字符串
	 * @param regx  正则表达式
	 * @return
	 */
	final public static boolean isSubMatch(String str, String regx) {
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);

		return m.find();
	}
	
	/**
	 * str 是否以symbol开头
	 * @param source
	 * @param symbol
	 * @return
	 */
	final public static boolean isHeadMatch(String str, String symbol) {
		return str.startsWith(StringUtils.trim(symbol));
	}

	/**
	 * 将可能出现在字符最后的symble去掉
	 * @param sbr
	 * @param symbol
	 * @return
	 */
	final static public String removeEnd(StringBuilder sbr, String symbol) {
		return sbr.toString().trim().replaceAll(symbol + "$", "");
	}

	/**
	 * 数值精度是否在指定精度范围内
	 * @param str
	 * @param scale 指定精度
	 * @return
	 */
	final public static boolean isRightScale(String str, int scale) {
		String regx = "^([/-]?)\\d+(\\.\\d{1," + scale + "})?$";
		return MatcherUtil.isAllMatch(str, regx);
	}
	
	/**
	 * 是否以http或https开头
	 * @param str
	 * @return
	 */
	final public static boolean ishttp(String url) {
		if(StringUtils.isEmpty(url))
			return false;
		
		String str = url.toLowerCase(); 
		return str.startsWith("http://") || str.startsWith("https://");
	}
	
	public static void main(String[] args) {
		boolean b = isNumber("55.5");
		System.out.println(b);
	}

}
