package com.qeweb.sap;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * DN号创建
 * @author chao.gu
 * 20170526
 *
 */
public class DnCreateSAP extends CommonSapFw{
	
	public static void main(String[] args) throws Exception {
		
	}
	
	
	public static List<DeliveryItemEntity>  dnDelToSap(List<DeliveryItemEntity> dlvItemList,ILogger iLogger) throws Exception{
		////验证
		if(null==dlvItemList || dlvItemList.size()==0){
			iLogger.log("dlvItemList is null"+"\r\n");
			return null;
		}
		////
		iLogger.log("dlvItemList size: "+dlvItemList.size()+"\r\n");
		
		FileWriter fw = initFw(DnCreateSAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，创建DN开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，创建DN开始"+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	 //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE);  
	            //参数
	            JCoTable input = function.getTableParameterList().getTable("IT_INPUT"); 
	            
	            iLogger.log("调用接口名称 ： "+ SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE +" ; 输入的表名： IT_INPUT");
	            
	            List<DeliveryItemEntity> returnList=new ArrayList<DeliveryItemEntity>();
	            int j=0;
	            for (DeliveryItemEntity dlvItem : dlvItemList) {
	            	j++;
	            	StringBuffer strBuf = new StringBuffer();
	            	strBuf.append("第 "+j+"行内容为：");
	            	input.appendRow();
	            	//供应商账号
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_LIFNR,dlvItem.getDelivery().getVendor().getCode());
	            	  strBuf.append(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_LIFNR+" = "+dlvItem.getDelivery().getVendor().getCode());
	            	  //采购订单号
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_EBELN,dlvItem.getOrderItem().getOrder().getOrderCode());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_EBELN+" = "+dlvItem.getOrderItem().getOrder().getOrderCode());
	            	  //行号
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_EBELP,dlvItem.getOrderItem().getItemNo());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_EBELP+" = "+dlvItem.getOrderItem().getItemNo());
	            	  //发货数量
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_LFIMG,dlvItem.getDeliveryQty());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_LFIMG+" = "+dlvItem.getDeliveryQty());
	            	  //基本单位
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_MEINS,dlvItem.getMeins());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_MEINS+" = "+dlvItem.getMeins());
	            	  //批号
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_CHARG,dlvItem.getCharg());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_CHARG+" = "+dlvItem.getCharg());
	            	  //生产日期
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_HSDAT,DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_HSDAT+" = "+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
	            	  //文本行
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_VERSION,dlvItem.getVersion());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_VERSION+" = "+dlvItem.getVersion());
	            	  //发货单号
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_BOLNR,dlvItem.getDelivery().getDeliveryCode());
	            	  strBuf.append(" | "+SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_BOLNR+" = "+dlvItem.getDelivery().getDeliveryCode());
/*	            	 //交货中总箱数
	            	  input.setValue(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_ANZPK,dlvItem.getAnzpk());*/
	            	  iLogger.log(strBuf.toString()+"\r\n");
	            	  log.append(strBuf.toString()+"\r\n");
	            }
	            
	            	function.execute(destination); 
	              JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
	              iLogger.log(" 输出的表名： IT_RETURN"+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows());
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					iLogger.log("第"+i+"条数据：  "+T_RESULT+"\r\n");
	 					String VBELN=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_VBELN);//DN号
	 					String POSNR=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_POSNR);//交货项目
	 					String FLAG=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_FLAG);//标识
	 					String MESS=T_RESULT.getString(SapTableStructure.DN_CREATE_Z_SRM_MM_DNCREATE_MESS);//信息
	 					
	 					DeliveryItemEntity dlvItem=dlvItemList.get(i);
	 					if("Y".equals(FLAG)&&!StringUtils.isEmpty(VBELN)){
	 						dlvItem.setSapDn(VBELN);
		 					dlvItem.setPosnr(POSNR);
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_SUCCESS);
	 					}else{
	 						dlvItem.setSyncStatus(PurchaseConstans.SYNC_FAIL);
	 					}
	 					dlvItem.setMessage(MESS);
	 					returnList.add(dlvItem);
	    			 }
	            }else{
	            	iLogger.log("IT_RETURN 中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	    iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，创建DN结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，创建DN结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return dlvItemList;
	}
	
   

}