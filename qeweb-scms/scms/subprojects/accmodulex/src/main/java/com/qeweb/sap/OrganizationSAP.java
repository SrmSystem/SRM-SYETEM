package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 采购组织
 * @author chao.gu
 * 20170510
 *
 */
public class OrganizationSAP extends CommonSapFw{
	
	//private final static Log logger = LogFactory.getLog(OrganizationSAP.class);
	
	public static void main(String[] args) throws Exception {
		/*List<OrganizationEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	public static List<OrganizationEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(OrganizationSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取采购组织开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取采购组织开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	       
	       List<OrganizationEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
		       iLogger.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<OrganizationEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.ORGANIZATION_ZEIP_POINFO_GETEKORG);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.ORGANIZATION_TABLE_NAME);
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.ORGANIZATION_ZEIP_POINFO_GETEKORG+" ; 表名： "+SapTableStructure.ORGANIZATION_TABLE_NAME+"\r\n");

	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成OrganizationEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成OrganizationEntity\r\n");
	    			 iLogger.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);

	 					String EKORG=T_RESULT.getString(SapTableStructure.ORGANIZATION_ZEIP_POINFO_GETEKORG_EKORG);//编码
	 					String EKOTX=T_RESULT.getString(SapTableStructure.ORGANIZATION_ZEIP_POINFO_GETEKORG_EKOTX);//名称
	 					OrganizationEntity entity =new OrganizationEntity();
	 					entity.setCode(EKORG);
	 					entity.setName(EKOTX);
	 					res.add(entity);
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.ORGANIZATION_TABLE_NAME+"中未获得到数据"+"\r\n");

	            }
	       }catch(Throwable e) { 
	    		iLogger.log(e.toString()+"\r\n");

	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取采购组织结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取采购组织结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
