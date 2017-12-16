package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.sap.service.BOHelper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 *工厂对应采购组信息
 * @author chao.gu
 * 20170511
 *
 */
public class GroupFactoryRelSAP extends CommonSapFw{
	
	private final static Log logger = LogFactory.getLog(GroupFactoryRelSAP.class);
	
	public static void main(String[] args) throws Exception {
		/*List<GroupFactoryRelEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	
	public static List<GroupFactoryRelEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(GroupFactoryRelSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取工厂对应采购组开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取工厂对应采购组开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
		List<GroupFactoryRelEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	   res=new ArrayList<GroupFactoryRelEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.GROUP_FACTORY_TABLE_NAME);
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.GROUP_FACTORY_ZEIP_POINFO_GETWEREKO +" ; 表名："+SapTableStructure.GROUP_FACTORY_TABLE_NAME+"\r\n");
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
	 					GroupFactoryRelEntity entity =new GroupFactoryRelEntity();
	 					entity.setFactoryCode(WERKS);
	 					entity.setFactoryName(NAME1);
	 					entity.setGroupCode(EKORG);
	 					entity.setGroupName(EKOTX);
	 					entity.setAbolished(BOHelper.UNABOLISHED_SINGEL);
	 					res.add(entity);
	 					iLogger.log("创建新 GroupFactoryRelEntity 成功， id 为：" +entity.getId()+"\r\n");

	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.GROUP_FACTORY_TABLE_NAME+"中未获得到数据"+"\r\n");

	            }
	       }catch(Throwable e) { 
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取工厂对应采购组结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取工厂对应采购组结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
