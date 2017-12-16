package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 公司
 * @author chao.gu
 * 20170510
 *
 */
public class CompanySAP extends CommonSapFw{

	
	public static void main(String[] args) throws Exception {
		/*List<CompanyEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	
	public static List<CompanyEntity> sync(ILogger iLogge) throws Exception{
		FileWriter fw = initFw(CompanySAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取公司开始").append(DateUtil.getCurrentDate());
		iLogge.log("调用sap接口，获取公司开始 "+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
		List<CompanyEntity> res = null;   
	       try{
	    	   
	    	   JCoDestination destination = SAPConn.connect();  
		       iLogge.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<CompanyEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.COMPANY_ZEIP_GET_COM_NAM);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.COMPANY_TABLE_NAME);
	            iLogge.log("调用接口名称 ： "+ SapTableStructure.COMPANY_ZEIP_GET_COM_NAM +" ; 表名："+SapTableStructure.COMPANY_TABLE_NAME+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成CompanyEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogge.log("总条数:"+T_RESULT.getNumRows()+"开始转成CompanyFactoryRelEntity\r\n");
	    			 iLogge.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					String BUKRS=T_RESULT.getString(SapTableStructure.COMPANY_ZEIP_GET_COM_NAM_BUKRS);//编码
	 					String BUTXT=T_RESULT.getString(SapTableStructure.COMPANY_ZEIP_GET_COM_NAM_BUTXT);//名称
	 					CompanyEntity entity =new CompanyEntity();
	 					iLogge.log("创建新 CompanyEntity 数据"+"\r\n");
	 					entity.setCode(BUKRS);
	 					entity.setName(BUTXT);
	 					res.add(entity);
	 					iLogge.log("创建新 entity 成功， id 为：" +entity.getId()+"\r\n");
	    			 }
	            }else{
	            	iLogge.log("ET_T001 中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	    iLogge.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取公司结束").append(DateUtil.getCurrentDate());
	        	iLogge.log("调用sap接口，获取公司结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
