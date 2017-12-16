package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
/**
 * 要货计划
 * @author chao.gu
 * 20170522
 *
 */
public class PurchaseGoodsRequestSAP extends CommonSapFw{
	
	public static void main(String[] args) throws Exception {
		/*List<MaterialEntity> materialList=new ArrayList<MaterialEntity>();
		MaterialEntity m=new MaterialEntity();
		m.setCode("1000007197");
		materialList.add(m);
		List<PurchaseGoodsRequestEntity> res=sync("2050",materialList);
		System.out.println(res.size());*/
	}
	
	
	public static List<PurchaseGoodsRequestEntity> sync(String factoryCode,List<String> materialList,ILogger iLogger) throws Exception{
		FileWriter fw = initFw(PurchaseGoodsRequestSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取要货计划开始").append(DateUtil.getCurrentDate());

		   JCoFunction function = null;  
	       
	       String result="";//调用接口返回状态  
	       String message="";//调用接口返回信息  
	       List<PurchaseGoodsRequestEntity> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
		       res=new ArrayList<PurchaseGoodsRequestEntity>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction("Z_SRM_DELIVERY_SPLIT");  
	            JCoParameterList input = function.getImportParameterList();  
	            //参数
	            input.setValue(SapTableStructure.PURCHASE_REQUEST_IMPORT_I_SEL, "1");
	            if(!StringUtils.isEmpty(factoryCode)){
	            input.setValue(SapTableStructure.PURCHASE_REQUEST_IMPORT_I_WERKS, factoryCode);
	            }
        		JCoTable tableInput = function.getTableParameterList().getTable("IT_MATNR");
	            for (String code : materialList) {
	            	tableInput.appendRow();
	            	tableInput.setValue("MATNR", code);
				}
	            
	            function.execute(destination); 
	            result= function.getExportParameterList().getString(SapTableStructure.RETURN_E_FLAG);//调用接口返回状态  
	            message= function.getExportParameterList().getString(SapTableStructure.RETURN_E_MESS);//调用接口返回信息  
	            log.append("状态:").append(result).append("\r\n");
	            log.append("信息:").append(message).append("\r\n");
	            
	            
	            JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成PurchaseGoodsRequestEntity\r\n");
	            	 log.append(T_RESULT.toXML()).append("\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);

	 					String WERKS=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_WERKS);//工厂编码
	 					String MATNR=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MATNR);//物料号
	 					String MAKTX=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MAKTX);//物料描述
	 					String EKGRP=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_EKGRP);//采购组
	 					String LIFNR=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_LIFNR);//供应商编码
	 					String RQ=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_RQ);//日期
	 					String ZB=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_ZB);//占比
	 					String JHZZT=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_JHZZT);//计划周转天数
	 					String ZLJYT=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_ZLJYT);//质量检验天数
	 					String SHPL=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_SHPL);//送货频率
	 					String YSTS=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_YSTS);//运输天数
	 					String PCSL=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_PCSL);//排产数量
	 					String DHSL=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_DHSL);//到货数量
	 					String KCSL=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_KCSL);//库存数量
	 					String FHSL=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_FHSL);//发货数量
	 					//后期新增
	 					String FLAG=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_FLAG);//标识新品/量产标识（X-新品，空白 - 量产）
	 					String MEINS=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MEINS);//单位
	 					String BISMT=T_RESULT.getString(SapTableStructure.PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_BISMT);//旧物料号
	 					
	 					
	 					PurchaseGoodsRequestEntity entity =new PurchaseGoodsRequestEntity();
	 					entity.setFactoryCode(WERKS);
	 					entity.setMaterialCode(MATNR);
	 					entity.setMaterialName(MAKTX);
	 					entity.setGroupCode(EKGRP);
	 					entity.setVendorCode(LIFNR);
	 					entity.setRqStr(RQ);
	 					entity.setZb(ZB);
	 					entity.setJhzzt(JHZZT);
	 					entity.setZljyt(ZLJYT);
	 					entity.setShpl(SHPL);
	 					entity.setYsts(YSTS);
	 					entity.setPcsl(PCSL);
	 					entity.setIsFull(0);//默认未满足
	 					entity.setSurQry(DHSL);//默认剩余数量默认到货数量(修改为到货数量)
	 					entity.setDhsl(DHSL);
	 					entity.setKcsl(KCSL);
	 					entity.setFhsl(FHSL);
	 					//add by chao.gu 20171106要货计划行转列便于统计 ,量产设定为O
	 					if(StringUtils.isEmpty(FLAG)){
	 						FLAG="O";
	 					}
	 					//add end
	 					entity.setFlag(FLAG);
	 					entity.setMeins(MEINS);
	 					entity.setDrawingNumber(BISMT);
	 					res.add(entity);
	    			 }
	            }
	       }catch(Throwable e) {  
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取要货计划结束").append(DateUtil.getCurrentDate());

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
