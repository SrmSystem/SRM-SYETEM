package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.sap.service.BOHelper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 公司对应工厂信息
 * @author chao.gu
 * 20170510
 *
 */
public class CompanyFactoryRelSAP extends CommonSapFw{
	
	public static void main(String[] args) throws Exception {
		/*List<CompanyFactoryRelEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	
	public static List<CompanyFactoryRelEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(CompanyFactoryRelSAP.class);
		
		StringBuffer log =new StringBuffer();
		iLogger.log("调用sap接口，获取公司对应工厂开始"+DateUtil.getCurrentDate());
		log.append("调用sap接口，获取公司对应工厂开始").append(DateUtil.getCurrentDate());
		List<CompanyFactoryRelEntity> res=null;
		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	   iLogger.log("与sap接口建立连接");
	    	   res=new ArrayList<CompanyFactoryRelEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable("PT_RETURN");
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS +" ; 表名： PT_RETURN");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成CompanyFactoryRelEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成CompanyFactoryRelEntity\r\n");
	    			 iLogger.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					String BUKRS=T_RESULT.getString(SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS_BUKRS);//公司编码
	 					String BUTXT=T_RESULT.getString(SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS_BUTXT);//公司名称
	 					String WERKS=T_RESULT.getString(SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS_WERKS);//工厂编码
	 					String NAME1=T_RESULT.getString(SapTableStructure.COMPANY_FACTORY_Z_BI_BUKRS_WERKS_NAME1);//工厂名称
	 					CompanyFactoryRelEntity entity =new CompanyFactoryRelEntity();
	 					iLogger.log("创建新 CompanyFactoryRelEntity 数据");
	 					entity.setCompanyCode(BUKRS);
	 					entity.setCompanyName(BUTXT);
	 					entity.setFactoryCode(WERKS);
	 					entity.setFactoryName(NAME1);
	 					entity.setAbolished(BOHelper.UNABOLISHED_SINGEL);
	 					res.add(entity);
	 					iLogger.log("创建新 CompanyFactoryRelEntity 成功， id 为：" +entity.getId());
	    			 }
	            }else{
	            	iLogger.log("PT_RETURN 中未获得到数据");
	            }
	       }catch(Throwable e) { 
	    	    iLogger.log(e.getMessage());
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取公司对应工厂结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取公司对应工厂结束"+DateUtil.getCurrentDate());
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
