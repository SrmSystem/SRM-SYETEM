package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.sap.service.BOHelper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 *工厂对应采购组织信息
 * @author chao.gu
 * 20170511
 *
 */
public class OrgFactoryRelSAP extends CommonSapFw{
	
	//private final static Log logger = LogFactory.getLog(OrgFactoryRelSAP.class);
	
	public static void main(String[] args) throws Exception {
		/*List<FactoryOrganizationRelEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	
	public static List<FactoryOrganizationRelEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(OrgFactoryRelSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取工厂对应采购组织开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取工厂对应采购组织开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	       
	       List<FactoryOrganizationRelEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect(); 
		       iLogger.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<FactoryOrganizationRelEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.GROUP_FACTORY_TABLE_NAME);
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO +" ; 表名： "+SapTableStructure.GROUP_FACTORY_TABLE_NAME+"\r\n");

	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成GroupFactoryRelEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成GroupFactoryRelEntity\r\n");
	    			 iLogger.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					String EKORG=T_RESULT.getString(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_EKORG);//采购组织编码
	 					String EKOTX=T_RESULT.getString(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_EKOTX);//采购组织名称
	 					String WERKS=T_RESULT.getString(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_WERKS);//工厂编码
	 					String NAME1=T_RESULT.getString(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_NAME1);//工厂名称
	 					FactoryOrganizationRelEntity entity =new FactoryOrganizationRelEntity();
	 					entity.setFactoryCode(WERKS);
	 					entity.setFactoryName(NAME1);
	 					entity.setOrgCode(EKORG);
	 					entity.setOrgName(EKOTX);
	 					entity.setAbolished(BOHelper.UNABOLISHED_SINGEL);
	 					res.add(entity);
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.GROUP_FACTORY_TABLE_NAME+"中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) { 
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取工厂对应采购组织结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取工厂对应采购组织结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
