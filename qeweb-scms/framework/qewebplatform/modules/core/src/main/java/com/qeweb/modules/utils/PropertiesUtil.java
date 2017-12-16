package com.qeweb.modules.utils;

import java.util.Properties;

/**
 * 属性的工具类，默认加载classpath中以project开头的properties文件
 * 其实是代理了一个PropertiesLoader类
 * @author pjjxiajun
 * @date 2015年5月29日
 * @path com.qeweb.modules.utils.PropertiesUtil.java
 */
public class PropertiesUtil {

	private static PropertiesLoaderEx propertiesLoader = new PropertiesLoaderEx("classpath*:/framework.properties","classpath*:/framework-*.properties","classpath*:/project-*.properties");
    
	public PropertiesUtil(String... resourcesPaths) {
		propertiesLoader = new PropertiesLoaderEx(resourcesPaths);
	}

	public static Properties getProperties() {
		return propertiesLoader.getProperties();
	}

	/**
	 * 取出String类型的Property,如果都為Null则抛出异常.
	 */
	public static String getProperty(String key) {
		return propertiesLoader.getProperty(key);
	}

	/**
	 * 取出String类型的Property.如果都為Null則返回Default值.
	 */
	public static String getProperty(String key, String defaultValue) {
		return propertiesLoader.getProperty(key, defaultValue);
	}

	/**
	 * 取出Integer类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public static Integer getInteger(String key) {
		return propertiesLoader.getInteger(key);
	}

	/**
	 * 取出Integer类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Integer getInteger(String key, Integer defaultValue) {
		return propertiesLoader.getInteger(key,defaultValue);
	}

	/**
	 * 取出Double类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public static Double getDouble(String key) {
		return propertiesLoader.getDouble(key);
	}

	/**
	 * 取出Double类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Double getDouble(String key, Integer defaultValue) {
		return propertiesLoader.getDouble(key, defaultValue);
	}

	/**
	 * 取出Boolean类型的Property.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 */
	public static Boolean getBoolean(String key) {
		return propertiesLoader.getBoolean(key);
	}

	/**
	 * 取出Boolean类型的Propert.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 */
	public static Boolean getBoolean(String key, boolean defaultValue) {
		return propertiesLoader.getBoolean(key, defaultValue);
	}

}
