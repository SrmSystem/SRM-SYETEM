package com.qeweb.scm.basemodule.quartz;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qeweb.scm.basemodule.utils.FTPUtil;


/**
 * FTP任务
 * @author ALEX
 *
 */
public abstract class AbsFtpJobBean extends AbstractJobBean {
	
	private final static Log log = LogFactory.getLog(AbsFtpJobBean.class);
	
	protected boolean getFtpFile(String ftpFileName, String localDir){
		log.debug("get ftp file..."); 
		FTPUtil ftpUtil = new FTPUtil();
		return ftpUtil.down(ftpFileName, localDir);
	}
   
	/**
	 * 将FTP返回数据转换成对象
	 * @param data
	 * @param t
	 * @return
	 */
	protected abstract <T> List<T> convertToObject(File file);
}
