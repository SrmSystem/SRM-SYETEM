package com.qeweb.sap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SapConfigUtil {
	
private final static Log log = LogFactory.getLog(SapConfigUtil.class);	
 private static SapConfigUtil sapConfigUtil = new SapConfigUtil();
 private static java.util.Properties p = null;
 
 public static SapConfigUtil getInstance(){
	 return sapConfigUtil;
 }
 
 
 static {
		java.io.InputStream inputStream  = SapConfigUtil.class.getClassLoader().getResourceAsStream("sap-connection.properties");    
		p  =   new  java.util.Properties();    
		try    {    
			p.load(inputStream);    
		} catch  (java.io.IOException e)   {    
			e.printStackTrace();  
			log.error(e.getMessage());
		}     
	}
 
 

	/**
	 * 获取系统编号 
	 */
	public String getJcoSysnr(){
		return  p.getProperty("JCO_SYSNR");
	}
 
	/**
	 * 获取登录语言
	 */
	public String getJcoLang(){
		return  p.getProperty("JCO_LANG");
	}
	
	/**
	 * 获取最大连接数
	 */
	public String getJcoPoolCapacity(){
		return  p.getProperty("JCO_POOL_CAPACITY");
	}
	
	/**
	 * 获取最大连接线程 
	 */
	public String getJcoPeakLimit(){
		return  p.getProperty("JCO_PEAK_LIMIT");
	}
	
	/**
	 * 获取服务器IP 
	 */
	public String getJcoAshost(){
		return  p.getProperty("JCO_ASHOST");
	}
	
	 
	/**
	 * 获取SAP集团 
	 */
	public String getJcoClinet(){
		return  p.getProperty("JCO_CLIENT");
	}
	
	
	/**
	 * 获取SAP用户名
	 */
	public String getJcoUser(){
		return  p.getProperty("JCO_USER");
	}
	
	/**
	 * 获取密码  
	 */
	public String getJcoPasswd(){
		return  p.getProperty("JCO_PASSWD");
	}
}
