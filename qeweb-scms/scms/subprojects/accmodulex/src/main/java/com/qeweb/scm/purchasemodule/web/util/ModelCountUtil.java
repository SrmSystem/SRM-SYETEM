package com.qeweb.scm.purchasemodule.web.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;

/**
 * 获取每个模块的编号工具类
 * @author zhangjiejun
 */
public interface ModelCountUtil {
	/**
	 * 日志工具
	 */
	public static ILogger logger = new FileLogger();
	
	/**
	 * 每个模块的编号的Map
	 */
	public static Map<String, String> modelCountMap = new HashMap<String, String>();
	
	/**
	 * 文件数据分隔符号
	 */
	public static final String FILE_DATA_SPLIT = "|";			
	
	/**
	 * 文件结束行的标识
	 */
	public static final String FILE_ROW_END = "^";		
	
	/**
	 * 定义时间转换格式
	 */
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	
	/**
	 * 定义时间格式
	 */
	public String useDateType = "MM/dd/yy";
	
	/**
	 * 上传文件名结束字符串
	 */
	public static final String UPLOAD_FILE_TYPE_END = ".imp";		
	
	/**
	 * 上传文件名结束字符串,且最终修改的文件名后缀
	 */
	public static final String UPLOAD_FILE_TYPE_END_DONE = ".impa";		
	
	/**
	 * 下载文件名结束字符串
	 */
	public static final String DOWNLOAD_FILE_TYPE_EXP = ".exp";
	
	/**
	 * 下载文件名结束字符串
	 */
	public static final String DOWNLOAD_FILE_TYPE_OUT = ".out";
	
	/**
	 * 下载文件名结束字符串
	 */
	public static final String DOWNLOAD_FILE_TYPE_REV = ".rev";
	
	/**
	 * 定义数字长度
	 */
	public static final int COUNT_LENGTH = 4;
	
	public static Map<String, Boolean> remindMap = new HashMap<String, Boolean>();		//提醒map
}
