package com.qeweb.scm.basemodule.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果集工具类
 * @author pjjxiajun
 * @date 2015年10月22日
 * @path com.qeweb.scm.basemodule.utils.ResultUtil.java
 */
public class ResultUtil {
	
	public final static String SUCCESS = "success"; 
	public final static String MSG = "msg"; 
	
	/**
	 * 或得一个结果集，默认为成功
	 * @return 结果集
	 */
	public static Map<String,Object> createMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(SUCCESS, true);
		return map;
	}

}
