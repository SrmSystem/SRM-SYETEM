package com.qeweb.scm.basemodule.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qeweb.scm.basemodule.constants.Constant;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

	/**
	 * 判断是否为空，空返回true
	 */
	final public static boolean isEmpty(String str) {
		if (null == str)
			str = "";
		return "".equals(str.trim());
	}

	final public static boolean isEmptyStr(String str) {
		return isEmpty(str) || "null".equals(str) || "undefined".equalsIgnoreCase(str);
	}
	/**
	 * 判断是否不为空，不空返回true
	 */
	final public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	final public static boolean isNotEmptyStr(String str) {
		return !isEmptyStr(str);
	}

	final public static boolean isEqualStr(String str1, String str2) {
		if(isEmptyStr(str1) || isEmptyStr(str2))
			return false;
		else
			return isEqual(str1, str2);
	}

	/**
	 * 判断两个字符串是否相等
	 *
	 * @param str1
	 * @param str2
	 * @return 相等返回true
	 */
	final public static boolean isEqual(String str1, String str2) {
		if (isEmpty(str1) && isEmpty(str2))
			return true;
		else if (isEmpty(str1) || isEmpty(str2))
			return false;
		else
			return str1.trim().equals(str2.trim());
	}

	final public static boolean isNotEqual(String str1, String str2) {
		return !isEqual(str1, str2);
	}

	/**
	 * 判断两个字符串是否相等, 不区分大小写
	 *
	 * @param str1
	 * @param str2
	 * @return 相等返回true
	 */
	final public static boolean isEqualIgnoreCase(String str1, String str2) {
		if (isEmpty(str1) && isEmpty(str2))
			return true;
		else if (isEmpty(str1) || isEmpty(str2))
			return false;
		else
			return str1.trim().equalsIgnoreCase(str2.trim());
	}

	/**
	 * str是否在arr中
	 * @param str
	 * @param arr
	 * @return
	 */
	final public static boolean isInArray(String str, String[] arr) {
		if(isEmpty(arr) || isEmpty(str))
			return false;

		for(String s : arr) {
			if(s.equals(str))
				return true;
		}

		return false;
	}
	
	/**
	 * arr中如果有一个元素与str相等,返回次一个相等的元素的下标,否则返回-1
	 * @param str
	 * @param arr
	 * @return
	 */
	final public static int indexOf(String str, String[] arr) {
		if(isEmpty(arr) || isEmpty(str))
			return -1;
		
		for (int i = 0; i < arr.length; i++) {
			if(str.equals(arr[i]))
				return i;
		}
		
		return -1;
	}

	/**
	 * 去除所有空格,包括中间的
	 */
	final public static String removeAllSpace(String str) {
		if (isEmpty(str))
			return "";

		return str.replaceAll("\\s+", "");
	}

	/**
	 * String.trim
	 */
	final public static String trim(String str) {
		if (isEmpty(str))
			return "";

		return str.trim();
	}

	/**
	 * str.indexOf(subString) >= 0
	 * @param str
	 * @param subString
	 * @return
	 */
	final public static boolean hasSubString(String str, String subString) {
		if(isEmpty(str) || isEmpty(subString))
			return false;

		return str.indexOf(subString) >= 0;
	}
	/**
	 * 判断字符串数组是否为空
	 *
	 * @param strs
	 * @return 不空返回true
	 */
	final public static boolean isNotEmpty(String[] strs) {
		return strs != null && strs.length != 0;
	}

	/**
	 *
	 * @param strs
	 * @return
	 */
	final public static boolean isEmpty(String[] strs) {
		return !isNotEmpty(strs);
	}

	/**
	 * 将str去掉所有空格并转换成大写
	 * @param str
	 * @return
	 */
	final public static String toUpperCase(String str) {
		if(isEmpty(str))
			return "";
		return StringUtils.removeAllSpace(str.toUpperCase());
	}
	
	/**
	 * 将字符串转换为数字
	 */
	final public static int convertToInt(String str) {
		try {
			return isNotEmpty(str) ? Integer.parseInt(str) : 0;
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * 将字符串转换为数字
	 */
	final public static long convertLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return Long.MAX_VALUE;
		}
	}

	/**
	 * 将字符串转换为bool
	 */
	final public static boolean convertToBool(String str) {
		if(isEmpty(str))
			return false;

		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将字符串数组转换为数字数组
	 * @param str
	 * @return
	 */
	final public static int[] convertToInt(String[] str) {
		str = removeNull(str);
		if(!isNotEmpty(str))
			return null;

		int[] result = new int[str.length];
		for(int i = 0; i < str.length; i++) {
			result[i] = convertToInt(str[i]);
		}

		return result;
	}

	/**
	 * 将字符串数组转换为数字数组
	 * @param str
	 * @return
	 */
	final public static long[] convertToLong(String[] str) {
		str = removeNull(str);
		if(!isNotEmpty(str))
			return null;

		long[] result = new long[str.length];
		for(int i = 0; i < str.length; i++) {
			result[i] = convertToLong(str[i]);
		}

		return result;
	}

	/**
	 * 将字符串转换为Integer
	 */
	final public static Integer convertToInteger(String str) {
		if(isEmpty(str))
			return null;

		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串转换为Long
	 */
	final public static Long convertToLong(String str) {
		if(isEmpty(str))
			return null;

		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串转换为Float数字
	 */
	final public static Float convertToFloat(String str) {
		if(isEmpty(str))
			return null;

		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串转换为Double数字
	 */
	final public static Double convertToDouble(String str) {
		if(isEmpty(str))
			return null;

		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 去掉 resource中的空元素
	 *
	 * @param resource
	 * @return
	 */
	final public static String[] removeNull(String[] resource) {
		List<String> target = new LinkedList<String>();

		if (!isNotEmpty(resource))
			return null;

		for (String s : resource) {
			if (isNotEmpty(s))
				target.add(s);
		}

		return target.toArray(new String[target.size()]);
	}

	/**
	 * 去掉结尾的字符
	 * @param str
	 * @return
	 */
	final public static String removeEnd(String str) {
		if(isEmptyStr(str))
			return str;
		else
			return str.substring(0, str.length() - 1);
	}
	/**
	 * 封装String类的split
	 *
	 * @param str
	 * @param symbol
	 * @return
	 */
	final public static String[] split(String str, String symbol) {
		if (isEmpty(str))
			return null;

		if(isEqual(symbol, "."))
			symbol = "\\.";
		else if(isEqual(symbol, "$"))
			symbol = "\\$";
		else if(isEqual(symbol, "?"))
			symbol = "\\?";

		return str.split(trim(symbol));
	}

	/**
	 * 将首字母变为大写
	 *
	 * @param str
	 * @return
	 */
	final public static String toUperFirstChar(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	final public static String convertString(String str) {
		return str.replace('.', '/');
	}

	/**
	 * 编码
	 *
	 * @param str
	 * @return
	 */
	final public static String encode(String str) {
		try {
			return isNotEmpty(str) ? URLEncoder.encode(str, Constant.ENCODING_UTF8) : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 解码
	 *
	 * @param str
	 * @return
	 */
	final public static String decode(String str) {
		try {
			return isNotEmpty(str) ? URLDecoder.decode(str, Constant.ENCODING_UTF8) : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将对象转至String类型
	 *
	 * @param obj
	 * @return
	 */
	final public static String convertToString(Object obj) {
		if (obj != null && !"null".equals(obj + "")) {
			return String.valueOf(obj);  
		}
		return null;
	}

	/**
	 * 将filepath转至url
	 *
	 * @param file
	 * @return
	 */
	final public static String filePathToURL(File file) {
		return file == null ? "" : filePathToURL(file.getPath());
	}

	/**
	 * 将filepath转至url
	 *
	 * @param filePath
	 * @return
	 */
	final public static String filePathToURL(String filePath) {
		if (StringUtils.isNotEmpty(filePath))
			return filePath.replace("\\", "//");
		return null;
	}

	/**
	 * 若字符串包含字符‘\’ java 转义 ‘\\’ 返回页面 ext 对‘\’ 会再次转义
	 * 同理还有符号',",\n,\r
	 * 所以需将返回的字符串.replace("\\", "\\\\")
	 * @param text
	 * @return
	 */
	final public static String getUnescapedText(String text) {
		return isEmpty(text) ? "" : text.replace("\\", "\\\\")
										.replace("'", "\\\'")
										.replace("\"", "\\\"")
										.replaceAll("\n", "\\\\n")
										.replace("\r", "\\\\r");
	}

	final public static String getNotNullStr(String str) {
		return isEmpty(str) ? "" : str;
	}
	
	final public static String filter(String value){
        if(value == null)
            return null;
        char content[] = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for(int i = 0; i < content.length; i++){
            switch(content[i]) {
	            case '<':
	                result.append("&lt;");
	                break;
	            case '>':
	                result.append("&gt;");
	                break;
	            case '&':
	                result.append("&amp;");
	                break;
	            case '"':
	                result.append("&quot;");
	                break;
	            case '\'':
	                result.append("&#39;");
	                break;
	            default:
	                result.append(content[i]);
            }
        }
        return result.toString();
    }
	
	/**
	 * 格式化HTML文本
	 * @param content
	 * @return
	 */
	public static String html(String content) {
		if(isEmpty(content))
			return "";        
		
		String html = content;
		html = html.replaceAll("'", "&apos;");
		html = html.replaceAll("\"", "&quot;");
		html = html.replaceAll("\t", "&nbsp;&nbsp;");
		html = html.replaceAll(" ", "&nbsp;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		return html;
	}


	/**
	 * 将c重复count次
	 * @param count
	 * @param c
	 * @return
	 */
	final public static String duplicate(int count, char c) {
		StringBuffer buf = new StringBuffer(count);
		for (int i = 0; i < count; ++i) {
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * 将ojb转换成String, double和float默认小数点后保留两位有效数字
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return toString(obj, 2);
	}

	public static String toString(Object obj, int scale) {
		String patten = "#";
		boolean flag = true;
		for(int i = 0; i < scale && i < 8; i++){
			if(flag){
				patten += ".";
				flag = false;
			}
			patten += "#";
		}
		DecimalFormat df = new DecimalFormat(patten);
		String value = "";
		if(obj == null)
			return value;
		if(obj.getClass() == double.class
				|| obj.getClass() == Double.class) {
			if(scale != -1)
				value = df.format(obj);
			else
				value = obj + "";
		}
		else if(obj.getClass() == float.class
				|| obj.getClass() == Float.class) {
			if(scale != -1)
				value = df.format(obj);
			else
				value = obj + "";
		}
		else if(obj.getClass() == java.util.Date.class
				|| obj.getClass() == java.sql.Date.class
				|| obj.getClass() == java.sql.Timestamp.class){
			value = DateUtil.dateToString((Date) obj, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		}
		else if(obj instanceof List
				|| obj instanceof Set
				|| obj instanceof Map){
			value = removeAllSpace(removeFirstAndLast(obj.toString()));
		}
		else {
			value = obj.toString();
		}

		return value;
	}
	
	/**
	 * 将arr中等于str的元素移除, 将结果集转换为字符串, 元素间以symbol分隔
	 * @param arr
	 * @param str
	 * @param symbol
	 * @return
	 */
	final public static String removeFromArr(String[] arr, String str, String symbol) {
		if(isEmpty(arr))
			return null;
		
		String result = "";
		for(String s : arr) {
			if(isNotEqual(s, str))
				result += (s + symbol);
		}
		
		return removeEnd(result);
	}
	
	/**
	 * 将arr转换为字符串,元素间以symbol分隔
	 * @param arr
	 * @param symbol
	 * @return
	 */
	final public static String convertArrToStr(String[] arr, String symbol) {
		if(isEmpty(arr))
			return null;
		
		String result = "";
		for(String s : arr) {
			result += (s + symbol);
		}
		
		return removeEnd(result);
	}
	
	/**
	 * 移出第一个和最后一个字符
	 * @param value
	 * @return
	 */
	private static String removeFirstAndLast(String value){
		if(value == null || value.length() < 2)
			return "";
		return value.subSequence(1, value.length() - 1).toString();
	}
	
	/**
	 * 将InputStream转换为string
	 * @param is
	 * @return
	 */
	final public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 1024);
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	/**
	 * 字符串拼接
	 * @param logMsg
	 * @param msgBuf
	 */
	public static void log(String logMsg, StringBuffer msgBuf) {
		if (logMsg != null && msgBuf != null) {
			msgBuf.append(logMsg).append("\n");
		}
	}
	
	/**
	 * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->HELLO_WORLD
	 * 
	 * @param str
	 * 转换前的驼峰式命名的字符串
	* @return 转换后下划线大写方式命名的字符串
	*/
	public static String underString(String str) {
		StringBuilder result = new StringBuilder();
		if (str != null && str.length() > 0) {
			// 将第一个字符处理成大写
			result.append(str.substring(0, 1).toUpperCase());
			// 循环处理其余字符
			for (int i = 1; i < str.length(); i++) {
				String s = str.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase())
						&& !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toUpperCase());
			}
		}
		return result.toString();
	 }
}
