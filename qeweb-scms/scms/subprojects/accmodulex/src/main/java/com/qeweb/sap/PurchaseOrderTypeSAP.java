package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 采购订单类型
 * @author chao.gu
 * 20170511
 *
 */
public class PurchaseOrderTypeSAP extends CommonSapFw{

	public static void main(String[] args) throws Exception {
		/*List<DictItemEntity> res=sync();
		System.out.println(res.size());*/
	}
	
	public static List<DictItemEntity> sync(ILogger iLogger) throws Exception{
		FileWriter fw = initFw(PurchaseOrderTypeSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取采购订单类型开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取采购订单类型开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	       
	       List<DictItemEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();
		       res=new ArrayList<DictItemEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE);  
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.PURCHASE_ORDER_TABLE_NAME);

	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成DictItemEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成DictItemEntity\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);

	 					String BSART=T_RESULT.getString(SapTableStructure.PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE_BSART);//编码
	 					String BATXT=T_RESULT.getString(SapTableStructure.PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE_BATXT);//名称
	 					DictItemEntity entity =new DictItemEntity();
	 					entity.setCode(BSART);
	 					entity.setName(BATXT);
	 					res.add(entity);
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.PURCHASE_ORDER_TABLE_NAME+"中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取采购订单类型结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取采购订单类型结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
