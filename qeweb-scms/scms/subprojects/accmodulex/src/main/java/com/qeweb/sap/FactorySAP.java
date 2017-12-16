package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 工厂
 * @author chao.gu
 * 20170510
 *
 */
public class FactorySAP extends CommonSapFw{
	
	//private final static Log logger = LogFactory.getLog(FactorySAP.class);
	
	public static void main(String[] args) throws Exception {
		/*List<FactoryEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	
	public static List<FactoryEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(FactorySAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取工厂开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取工厂开始"+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
		List<FactoryEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
		       iLogger.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<FactoryEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.FACTORY_ZEIP_POINFO_GETWERKS);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.FACTORY_TABLE_NAME);
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.FACTORY_ZEIP_POINFO_GETWERKS +" ; 表名： "+SapTableStructure.FACTORY_TABLE_NAME+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成FactoryEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成FactoryEntity\r\n");
	    			 iLogger.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);

	 					String WERKS=T_RESULT.getString(SapTableStructure.FACTORY_ZEIP_POINFO_GETWERKS_WERKS);//编码
	 					String NAME1=T_RESULT.getString(SapTableStructure.FACTORY_ZEIP_POINFO_GETWERKS_NAME1);//名称
	 					FactoryEntity entity =new FactoryEntity();
	 					entity.setCode(WERKS);
	 					entity.setName(NAME1);
	 					res.add(entity);
	 					iLogger.log("创建新 FactoryEntity 成功， id 为：" +entity.getId()+"\r\n");
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.FACTORY_TABLE_NAME+"中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取工厂结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取工厂结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
