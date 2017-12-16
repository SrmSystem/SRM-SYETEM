package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * DN号删除
 * @author chao.gu
 * 20170510
 *
 */
public class DnDelSAP extends CommonSapFw{
	
	public static void main(String[] args) throws Exception {
		
	}
	
	
	public static List<DeliveryItemEntity>  dnDelToSap(List<DeliveryItemEntity> dlvItemList,ILogger iLogger) throws Exception{
		/////验证start
		
		if(null==dlvItemList||dlvItemList.size()==0){
			iLogger.log("dlvItemList is null"+"\r\n");
			return null;
		}
		////end
		iLogger.log("dlvItemList size: "+dlvItemList.size()+"\r\n");
		
		FileWriter fw = initFw(DnDelSAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，删除DN开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，删除DN开始"+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE);  
	            //参数
	            JCoTable input = function.getTableParameterList().getTable("IT_INPUT"); 
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE +" ; 输入的表名： IT_INPUT"+"\r\n");

	            List<DeliveryItemEntity> returnList=new ArrayList<DeliveryItemEntity>();
	            for (DeliveryItemEntity dlvItem : dlvItemList) {
	            	log.append("SRM的DN号:").append(dlvItem.getDn()).append(",删除调用SAP:");
	            	iLogger.log("SRM的DN号:"+dlvItem.getDn()+",删除调用SAP:"+"\r\n");

	            	input.appendRow();
	            	//DN号
	            	input.setValue(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE_VBELN, dlvItem.getDn());
	            	iLogger.log(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE_VBELN+dlvItem.getDn()+"\r\n");
	            }
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.DN_DEL_TABLE_NAME);
	            iLogger.log("输出的表名： "+ SapTableStructure.DN_DEL_TABLE_NAME+"\r\n");

	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows());
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					iLogger.log("第"+i+"条数据：  "+T_RESULT+"\r\n");

	 					String VBELN=T_RESULT.getString(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE_VBELN);//编码
	 					String FALG=T_RESULT.getString(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE_FLAG);//标识
	 					String MESS=T_RESULT.getString(SapTableStructure.DN_DEL_Z_SRM_MM_DNDELETE_MESS);//信息
	 					DeliveryItemEntity dlvItem=dlvItemList.get(i);
	 					if("Y".equals(FALG)){
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_SUCCESS);
	 					}else{
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_FAIL);
	 					}
	 					dlvItem.setMessage(MESS);
	 					returnList.add(dlvItem);
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.DN_DEL_TABLE_NAME+" 中未获得到数据"+"\r\n");

	            }
	            
	       }catch(Throwable e) {
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，删除DN结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，删除DN结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return dlvItemList;
	}
	
   

}
