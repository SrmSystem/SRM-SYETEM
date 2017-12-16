package com.qeweb.sap;


import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 采购结算
 * @author haiming.huang
 * 20170919
 *
 */
public class NoCheckItemsSAP extends CommonSapFw{

	
	public static List<NoCheckItemsEntity> sync(ILogger iLogge) throws Exception{
		FileWriter fw = initFw(NoCheckItemsSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取采购结算开始").append(DateUtil.getCurrentDate());
		iLogge.log("调用sap接口，获取采购结算开始 "+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
		List<NoCheckItemsEntity> res = null;   
	       try{
	    	   
	    	   JCoDestination destination = SAPConn.connect();  
		       iLogge.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<NoCheckItemsEntity>();
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		       
		       //调用函数  
	            function = destination.getRepository().getFunction(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENT);  
	            
	            //参数
	           JCoTable input = function.getImportParameterList().getTable(SapTableStructure.ORDER_SETTLEMEN_IT_BUDAT);
	            
	            /*input.setValue("EBELN", "");
	            input.setValue("LIFNR", "");*/
	            input.appendRow();
	            input.setValue("SIGN", "I");
	            input.setValue("OPTION", "BT");
	            input.setValue("LOW", "20150101");
	            input.setValue("HIGH",sdf.format(new Date()));
	            
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.ORDER_SETTLEMEN_ET_STATE);
	            iLogge.log("调用接口名称 ： "+ SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENT +" ; 表名："+SapTableStructure.ORDER_SETTLEMEN_ET_STATE+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 //log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成CheckItemEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogge.log("总条数:"+T_RESULT.getNumRows()+"开始转成NoCheckItemEntity\r\n");
	    			 iLogge.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					
	 					String EBELN=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EBELN);//采购凭证号
	 					Integer EBELP=T_RESULT.getInt(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EBELP);//采购凭证的项目编号
	 					String WERKS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WERKS);//工厂
	 					String WERKSMS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WERKSMS);//工厂名称
	 					String EKORG=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKORG);//采购组织
	 					String EKORGMS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKORGMS);//采购组织描述
	 					String LIFNR=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_LIFNR);//供应商或债权人的帐号
	 					String LIFNRMS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_LIFNRMS);//供应商名称
	 					String EKGRP=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKGRP);//采购组 
	 					String EKGRPMS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKGRPMS);//采购组的描述
	 					String MATNR=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_MATNR);//物料号
	 					String TXZ01=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_TXZ01);//短文本
	 					String MEINS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_MEINS);//基本计量单位
	 					Double POQTY=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_POQTY);//订单数量
	 					String BUDAT=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BUDAT);//凭证中的过帐日期
	 					String BELNR=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BELNR);//物料凭证编号
	 					Integer BUZEI=T_RESULT.getInt(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BUZEI);//物料凭证中的项目
	 					Double GRQTY=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_GRQTY);//入库数量
	 					Double IVQTY=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_IVQTY);//开票数量
	 					Double OPQTY=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_OPQTY);//仍要开票数量(grqty - ivqty)
	 					String VBELN=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_VBELN);//DN号
	 					String BOLNR=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BOLNR);//ASN单号
	 					String WAERS=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WAERS);//货币码 
	 					Double PRICE=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_PRICE);//不含税单价
	 					Double VALUE=T_RESULT.getDouble(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_VALUE);//仍要开票的净价值
	 					String AEDAT=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_AEDAT);//更改日期
	 					String RETPO=T_RESULT.getString(SapTableStructure.ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_RETPO);//退货项目
	 					
	 					
	 					NoCheckItemsEntity entity =new NoCheckItemsEntity();
	 					entity.setOrderCode(EBELN);//采购凭证号
	 					entity.setOrderNo(EBELP);//采购凭证的项目编号
	 					entity.setFactoryCode(WERKS);//工厂
	 					entity.setFactoryName(WERKSMS);//工厂名称
	 					entity.setBuyerCode(EKORG);//采购组织
	 					entity.setBuyerName(EKORGMS);//采购组织描述
	 					entity.setVendorCode(LIFNR);//供应商或债权人的帐号
	 					entity.setVendorName(LIFNRMS);//供应商名称
	 					entity.setGroupCode(EKGRP);//采购组 
	 					entity.setGroupName(EKGRPMS);//采购组的描述
	 					entity.setTxz01(TXZ01);//短文本
	 					entity.setMaterialName(TXZ01);//物料名称
	 					entity.setMaterialCode(MATNR);//物料号
	 					entity.setUnitName(MEINS);//基本计量单位
	 					entity.setOrderQty(POQTY);//订单数量
	 					if(BUDAT!=null && !BUDAT.isEmpty()){
	 						entity.setPostDate(Timestamp.valueOf(BUDAT+" 00:00:00"));//凭证中的过帐日期
	 					}
	 					entity.setMaterialNo(BELNR);//物料凭证编号
	 					entity.setMaterialProject(BUZEI);//物料凭证中的项目
	 					entity.setRecQty(GRQTY);//入库数量
	 					entity.setInvoicedQuantity(IVQTY);//开票数量
	 					entity.setConInvoicedQuantity(OPQTY);//仍要开票数量(grqty - ivqty)
	 					entity.setCurrencyCode(WAERS);//货币码 
	 					entity.setDlvCode(BOLNR);//ASN发货单号
	 					entity.setItemNo("".equals(VBELN)?null:Integer.valueOf(VBELN));//DN号
	 					entity.setPrice(PRICE);//不含税单价
	 					entity.setConPrice(VALUE);//仍要开票的净价值
	 					if(AEDAT!=null && !AEDAT.isEmpty()){
	 						entity.setChangeDate(Timestamp.valueOf(AEDAT+" 00:00:00"));//更改日期
	 					}
	 					entity.setCreateTime(DateUtil.getCurrentTimestamp());//创建时间
	 					
	 					if("X".equals(RETPO)){
	 						entity.setDataType(-1);
	 					}else{
	 						entity.setDataType(1);
	 					}
	 					
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
	        	log.append("调用sap接口，获取采购结算结束").append(DateUtil.getCurrentDate());
	        	iLogge.log("调用sap接口，获取采购结算结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
