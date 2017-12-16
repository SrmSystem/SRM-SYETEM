package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 采购组
 * @author chao.gu
 * 20170511
 *
 */
public class PurchasingGroupSAP extends CommonSapFw{
	
	//private final static Log logger = LogFactory.getLog(PurchasingGroupSAP.class);
	
	public static void main(String[] args) throws Exception {
		/*List<PurchasingGroupEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	public static List<PurchasingGroupEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(PurchasingGroupSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取采购组开始").append(DateUtil.getCurrentDate());

		JCoFunction function = null;  
	       
	       List<PurchasingGroupEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
		       res=new ArrayList<PurchasingGroupEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.PURCHASING_GROUP_TABLE_NAME);
	           	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成PurchasingGroupEntity\r\n");
	   			 log.append(T_RESULT.toXML()).append("\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					String EKGRP=T_RESULT.getString(SapTableStructure.PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP_EKGRP);//编码
	 					String EKNAM=T_RESULT.getString(SapTableStructure.PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP_EKNAM);//名称
	 					PurchasingGroupEntity entity =new PurchasingGroupEntity();
	 					entity.setCode(EKGRP);
	 					entity.setName(EKNAM);
	 					res.add(entity);
	    			 }
	            }else{
	            }
	       }catch(Throwable e) { 
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取采购组结束").append(DateUtil.getCurrentDate());

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
