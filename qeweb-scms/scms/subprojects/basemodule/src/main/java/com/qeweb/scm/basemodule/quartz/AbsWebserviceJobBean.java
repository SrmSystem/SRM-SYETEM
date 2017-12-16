package com.qeweb.scm.basemodule.quartz;

import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.codehaus.xfire.client.Client;

/**
 * webservice job
 * @author ALEX
 *
 */
public abstract class AbsWebserviceJobBean extends AbstractJobBean {
	
	private final static Log log = LogFactory.getLog(AbsWebserviceJobBean.class);
	
	/**
	 * 获取webservice数据
	 * @param url
	 * @param method
	 * @param param
	 * @return
	 */
	protected String getWsDate(String url, String method, Object[] param){
//		try {
//			Client c = new Client(new URL(url));
//        	Object[] o = c.invoke(method, param);
//        	return (String)o[0];
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("web service:" + e.getMessage() + " url:" + url);
//		}
		return null;
	}
  
	/**
	 * 将webservice返回数据转换成对象
	 * @param data
	 * @param t
	 * @return
	 */
	protected abstract <T> List<T> convertToObject(String data);
}
