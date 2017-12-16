package com.qeweb.scm.basemodule.utils;

import java.io.File;
import java.util.Date;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;

/**
 * 日志工具类，主要用来记录各类日志文件，主要用于业务日志记录，所以有相对固定的处理
 * @author pjjxiajun
 * @date 2015年9月16日
 * @path com.qeweb.scm.basemodule.utils.LogUtil.java
 */
public class LogUtil {
	
	public final static String ENCODING_DEFAULT = "UTF-8";
	
	/**
	 * 记录业务日志并返回日志文件路径
	 * @param logMsg 日志信息
	 * @param filePre 特别指定的文件目录前缀
	 * @return 文件路径
	 */
	public static String logBussiness(String logMsg,String filePre,boolean append) throws Exception{
		try{
			String rootDir = PropertiesUtil.getProperty(ApplicationProConstant.FILE_LOG_DIR, "/");
			if(StringUtils.isNotEmpty(filePre)){
				rootDir+='/'+filePre+"/";
			}
			String datePre = DateUtil.dateToString(new Date(),DateUtil.DATE_FORMAT_YYYYMMDD);
			String fileName = datePre+".txt";
			File logFile = new File(rootDir,fileName);
			FileUtil.writeStringToFileX(logFile, logMsg, "UTF-8",append);
			String filePath = logFile.getAbsolutePath().replaceAll("\\\\", "/");
			return filePath;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
