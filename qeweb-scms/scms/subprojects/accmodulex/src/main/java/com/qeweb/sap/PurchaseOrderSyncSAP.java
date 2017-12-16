package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
/**
 * 采购订单同步
 *
 */
public class PurchaseOrderSyncSAP extends CommonSapFw{
	
	  public static final String ORDER_LIST="Z_SRM_MM_POINFO";//采购订单

	  public static void main(String[] args) throws Exception {
		 /* Map<String, List<PurchaseOrderItemEntity>> map=sync("20170427", "20170528", null, null);
		  System.out.println(map.size());*/
	  }
	
	
	public static Map<String, List<PurchaseOrderItemEntity>> sync(String startTime,String endTime,String vendorCodes,String orderCodes,ILogger iLogger) throws Exception{
		FileWriter fw = initFw(PurchaseOrderSyncSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取采购订单开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取采购订单开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	      
	       Map<String, List<PurchaseOrderItemEntity>> res=null;
	       try{
	    	   JCoDestination destination = SAPConn.connect();
		       res=new HashMap<String, List<PurchaseOrderItemEntity>>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction(ORDER_LIST); 
	            iLogger.log("调用接口名称 ： "+ ORDER_LIST+"\r\n");

	            JCoParameterList input = function.getImportParameterList();  
	            if(!StringUtils.isEmpty(startTime)){
	            	input.setValue("I_DATEF", startTime);  
	  	            input.setValue("I_DATET", endTime);
	            }
	            JCoTable tableInput = function.getTableParameterList().getTable("IT_LIFNR");
	            JCoTable tableInput2 = function.getTableParameterList().getTable("IT_EBELN");
	            if(!StringUtils.isEmpty(vendorCodes)){
	            	for (String code : vendorCodes.split(",")) {
	        			tableInput.appendRow();
	        			tableInput.setValue("LIFNR", code);
					}
	            }
	            if(!StringUtils.isEmpty(orderCodes)){
	            	for (String code : orderCodes.split(",")) {
	        			tableInput2.appendRow();
	        			tableInput2.setValue("EBELN", code);
					}
	            }
	            
	            function.execute(destination); 
	            JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows());
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
		    		String BSART=T_RESULT.getString("BSART");	 //BSART		采购凭证类型
		    		String EBELN=T_RESULT.getString("EBELN");	 //EBELN		采购凭证号 
		    		String LIFNR=T_RESULT.getString("LIFNR");	// LIFNR			供应商或债权人的帐号
		    		String LIFNRMS=T_RESULT.getString("LIFNRMS");	// LIFNRMS			供应商描述 
		    		String ZTERM=T_RESULT.getString("ZTERM");	// ZTERM		付款条件代码
		    		String ZTERMMS=T_RESULT.getString("ZTERMMS");	// ZTERMMS		付款条件描述
		    		String WAERS=T_RESULT.getString("WAERS");	// WAERS			货币码 
		    		String WAERSMS=T_RESULT.getString("WAERSMS");	// WAERSMS			货币码描述 
		    		String BUKRS=T_RESULT.getString("BUKRS");	// BUKRS		公司代码
		    		String BUKRSMS=T_RESULT.getString("BUKRSMS");	// BUKRSMS			公司描述
		    		String EKORG=T_RESULT.getString("EKORG");	// EKORG			采购组织
		    		String EKORGMS=T_RESULT.getString("EKORGMS");	// EKORGMS			采购组织描述
		    		String EKGRP=T_RESULT.getString("EKGRP");	 //EKGRP			采购组 
		    		String EKGRPMS=T_RESULT.getString("EKGRPMS");	 //EKGRPMS			采购组描述 
		    		
		    		String EBELP=T_RESULT.getString("EBELP");	// EBELP			采购凭证的项目编号 
		    		String KNTTP=T_RESULT.getString("KNTTP");	// KNTTP		科目分配类别
		    		String PSTYP=T_RESULT.getString("PSTYP");	// PSTYP			采购凭证中的项目类别
		    		String MATNR=T_RESULT.getString("MATNR");	// MATNR			物料号 
		    		String MAKTX=T_RESULT.getString("MAKTX");	// MAKTX			物料描述（短文本） 
		    		Double MENGE=T_RESULT.getDouble("MENGE");	// MENGE		采购订单数量
		    		String MEINS=T_RESULT.getString("MEINS");	 //MEINS			采购订单的计量单位 
		    		String EEIND=T_RESULT.getString("EINDT");	// EINDT			交货日期
		    		String WERKS=T_RESULT.getString("WERKS");	// WERKS			工厂
		    		String WERKSMS=T_RESULT.getString("WERKSMS");	// WERKSMS			工厂描述
		    		String MATKL=T_RESULT.getString("MATKL");	// MATKL			物料组 
		    		String MATKLMS=T_RESULT.getString("MATKLMS");	// MATKLMS			物料组描述 
		    		String IDNLF=T_RESULT.getString("IDNLF");	// IDNLF			供应商使用的物料编号
		    		String RETPO=T_RESULT.getString("RETPO");	// RETPO			退货项目 
		    		String ZFREE=T_RESULT.getString("ZFREE");	 //ZFREE			免费标识
		    		String AEDAT=T_RESULT.getString("AEDAT");	// AEDAT			记录的创建日期 
		    		String ERNAM=T_RESULT.getString("ERNAM");	// ERNAM			创建对象的人员名称 
		    		String FRGKE=T_RESULT.getString("FRGKE");	// FRGKE			批准标识：采购凭证 B代表未审批，R代表审批
		    		
		    		
		    		String LOEKZ=T_RESULT.getString("LOEKZ");// LOEKZ			采购凭证中的删除标识
		    		String ZLOCK=T_RESULT.getString("ZLOCK"); //ZLOCK			锁定标识
		    		String BSTAE=T_RESULT.getString("BSTAE");//BSTAE            订单确认状态（003或者004为需推送srm的数据）
		    		String KZABS=T_RESULT.getString("KZABS");	// KZABS		订单回执需求 
		    		
		    		String LGORT=T_RESULT.getString("LGORT");	// 库存地点
		    		String LGORTMS=T_RESULT.getString("LGORTMS");	// 仓储地点的描述 
		    		String ADDRESS=T_RESULT.getString("ADDRESS");	// 送货地址
		    		PurchaseOrderItemEntity item=new PurchaseOrderItemEntity();
		    		item.setItemNo(Integer.parseInt(EBELP));
		    		item.setKnttp(KNTTP);
		    		item.setPstyp(PSTYP);
		    		item.setMaterialCode(MATNR);
		    		item.setMaterialName(MAKTX);
		    		item.setMenge(MENGE+"");
		    		item.setOrderQty(MENGE);
		    		item.setSurOrderQty(MENGE);//add by eleven (默认把剩余数量=总数量)
		    		item.setIsModify(0);//add by eleven (默认没有被修改)
		    		item.setMeins(MEINS);
		    		item.setRequestTime(DateUtil.stringToTimestamp(EEIND, "yyyyMMdd"));
		    		item.setWerks(WERKS);
		    		item.setWerksms(WERKSMS);
		    		item.setMatkl(MATKL);
		    		item.setMatklms(MATKLMS);
		    		item.setIdnlf(IDNLF);
		    		item.setRetpo(RETPO);
		    		item.setZfree(ZFREE);
		    		item.setLoekz(LOEKZ);
		    		item.setZlock(ZLOCK);
		    		item.setBstae(BSTAE);//add by chao.gu 20170929
		    		item.setKzabs(KZABS);
		    		item.setReceiveOrg(ADDRESS);
		    		item.setLgort(LGORT);
		    		item.setLgortms(LGORTMS);
		    		if(res.get(EBELN)==null){
		    			List<PurchaseOrderItemEntity> list=new ArrayList<PurchaseOrderItemEntity>();
		    			PurchaseOrderEntity order=new PurchaseOrderEntity();
		    			order.setBsart(BSART);
		    			order.setOrderCode(EBELN);
		    			order.setVendorCode(LIFNR);
		    			order.setVendorName(LIFNRMS);
		    			order.setZterm(ZTERM);
		    			order.setZtermms(ZTERMMS);
		    			order.setWaers(WAERS);
		    			order.setWaersms(WAERSMS);
		    			order.setBukrs(BUKRS);
		    			order.setBukrsms(BUKRSMS);
		    			order.setBuyerCode(EKORG);
		    			order.setBuyerName(EKORGMS);
		    			order.setEkgrp(EKGRP);
		    			order.setEkgrpms(EKGRPMS);
		    			order.setAedat(DateUtil.stringToTimestamp(AEDAT, "yyyyMMdd"));
		    			order.setErnam(ERNAM);
		    			order.setFrgke(FRGKE);//B代表未审批，R代表审批
		    			item.setOrder(order);
		    			list.add(item);
		    			res.put(EBELN, list);
		    		}else{
		    			List<PurchaseOrderItemEntity> list=res.get(EBELN);
		    			list.add(item);
		    			res.put(EBELN, list);
		    		}
	            }else{
	            	iLogger.log("IT_RETURN 中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取采购订单结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取采购订单结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
