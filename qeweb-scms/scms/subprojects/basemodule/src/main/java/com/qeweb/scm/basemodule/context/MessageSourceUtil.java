package com.qeweb.scm.basemodule.context;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 读取国际化资源文件工具类
 * @author ALEX
 *
 */
public class MessageSourceUtil {

	public static String getMessage(String code, Object[] args, Locale locale) {
		return SpringContextUtils.getApplicationContext().getMessage(code, args, locale);  
	}
	
	public static String getMessage(String code, Locale locale) {
		return getMessage(code, null, locale);  
	}
	
	public static String getMessage(String code, Object[] args, HttpServletRequest request) {
		return getMessage(code, args, RequestContextUtils.getLocaleResolver(request).resolveLocale(request));  
	}
	
	public static String getMessage(String code, HttpServletRequest request) {
		return getMessage(code, null, request);  
	}
}
