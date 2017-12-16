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
 * DN号修改
 * @author chao.gu
 * 20170526
 *
 */
public class DnUpdateSAP extends CommonSapFw{
	
	public static void main(String[] args) throws Exception {
		
	}
	
	
	public static List<DeliveryItemEntity>  dnUpdateToSap(List<DeliveryItemEntity> dlvItemList,ILogger iLogger) throws Exception{
		////验证
		if(null==dlvItemList || dlvItemList.size()==0){
			iLogger.log("dlvItemList is null"+"\r\n");
			return null;
		}
		////
		iLogger.log("dlvItemList size: "+dlvItemList.size()+"\r\n");
		
		FileWriter fw = initFw(DnUpdateSAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，修改DN开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，修改DN开始"+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE);  
	            //参数
	            JCoTable input = function.getTableParameterList().getTable("IT_INPUT"); 
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE +" ; 输入的表名： IT_INPUT"+"\r\n");

	            //////////
	            List<DeliveryItemEntity> returnList=new ArrayList<DeliveryItemEntity>();
	            int j = 0;
	            for (DeliveryItemEntity dlvItem : dlvItemList) {
	            	j++; 
	            	log.append("SRM的DN号:").append(dlvItem.getDn()).append(",修改调用SAP:");
	            	iLogger.log("SRM的DN号:"+dlvItem.getDn()+",修改调用SAP:"+"\r\n");

	            	 input.appendRow();
	            	 StringBuffer strBuf = new StringBuffer();	
	            	 strBuf.append("第 "+j+"行内容为：");
	            	  //DN号
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_VBELN,dlvItem.getDn());
	            	  strBuf.append(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_VBELN+" = "+dlvItem.getDn());

	            	  //交货项目
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_POSNR,dlvItem.getPosnr());
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_POSNR+" = "+dlvItem.getPosnr());

	            	  //发货数量
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_LFIMG,dlvItem.getDeliveryQty());
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_LFIMG+" = "+dlvItem.getDeliveryQty());
	            	  
	            	  //基本计量单位
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_MEINS,dlvItem.getMeins());
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_MEINS+" = "+dlvItem.getMeins());
	            	  
	            	  //批号
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_CHARG,dlvItem.getCharg());
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_CHARG+" = "+dlvItem.getCharg());
	            	  
	            	  //生产日期
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_HSDAT,DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_HSDAT+" = "+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
	            	  
	            	  /*	            	 //交货中总箱数
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_ANZPK,dlvItem.getAnzpk());*/
	            	  //文本行
	            	  input.setValue(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_VERSION,dlvItem.getVersion());
	            	  strBuf.append(" | "+SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_VERSION+" = "+dlvItem.getVersion());
	                  
	            	  iLogger.log(strBuf.toString()+"\r\n");

	            }
	            
          	  	function.execute(destination); 
	              JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.DN_UPDATE_TABLE_NAME);
	              iLogger.log(" 输出的表名： "+SapTableStructure.DN_UPDATE_TABLE_NAME+"\r\n");

	              if (T_RESULT != null && !T_RESULT.isEmpty()) {
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					iLogger.log("第"+i+"条数据：  "+T_RESULT+"\r\n");
	 					String VBELN=T_RESULT.getString(SapTableStructure.DN_UPDATE_Z_SRM_MM_DNCHANGE_VBELN);//DN号
	 					String FLAG=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_FLAG);//标识
	 					String MESS=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_MESS);//信息
	 					DeliveryItemEntity dlvItem=dlvItemList.get(i);
	 					if("Y".equals(FLAG) && VBELN.equals(dlvItem.getDn())){//是YES并且返回的DN号正确
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_SUCCESS);
	 					}else{
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_FAIL);
	 					}
	 					dlvItem.setMessage(MESS);
	 					returnList.add(dlvItem);
	 		
	 				
	    			 }
	            }else{
	            	iLogger.log(SapTableStructure.DN_UPDATE_TABLE_NAME+"中未获得到数据"+"\r\n");
	            }
	            /////////
	            
	            
	          
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，修改DN结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，修改DN结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       
	       return dlvItemList;
	}
	
   

}
