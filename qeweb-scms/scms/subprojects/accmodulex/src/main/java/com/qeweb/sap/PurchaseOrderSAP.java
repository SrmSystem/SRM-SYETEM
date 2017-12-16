package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.sap.vo.OrderPriceVo;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
/**
 * 采购订单确认与修改
 *
 */
public class PurchaseOrderSAP extends CommonSapFw{
	
	  public static final String ORDER_CONFIRM="Z_SRM_MM_POCONDEM";//采购订单写入确认标识接口名
	  public static final String ORDER_CONFIRM_TABLE_INPUT="IT_INPUT";//采购订单写入确认标识表名
	  public static final String ORDER_CONFIRM_TABLE_RETURN="IT_RETURN";//采购订单写入确认标识表名
	  
	  public static final String ORDER_CHANGE="Z_SRM_MM_POCHANGE";//采购订单数量修改接口名
	  public static final String ORDER_CHANGE_TABLE_INPUT="IT_INPUT";//采购订单数量修表名
	  public static final String ORDER_CHANGE_TABLE_RETURN="IT_RETURN";//采购订单数量修表名

	  public static void main(String[] args) throws Exception {
		 /* List<OrderPriceVo> input=new ArrayList<OrderPriceVo>();
		  OrderPriceVo vo=new OrderPriceVo();
		  vo.setEBELN("4500150709");//采购凭证号 
		  vo.setEBELP(10);
		  input.add(vo);
		  List<OrderPriceVo> res=getOrderItemPrice(input);
		  System.out.println(res.size());*/
	  }
	  
	  	/**
	  	 * 采购订单价格读取，对账使用，比较供应商填写价格
	  	 * @param items
	  	 * @return
	  	 * @throws Exception
	  	 */
	    public static List<OrderPriceVo> getOrderItemPrice(List<OrderPriceVo> input,ILogger iLogger)throws Exception{
			FileWriter fw = initFw(PurchaseOrderSAP.class);
			StringBuffer log =new StringBuffer();
			log.append("调用sap接口，采购订单价格读取开始").append(DateUtil.getCurrentDate());
			iLogger.log("调用sap接口，采购订单价格读取开始"+DateUtil.getCurrentDate()+"\r\n");

			JCoFunction function = null;  
		       
		       List<OrderPriceVo> res=new ArrayList<OrderPriceVo>();
		       try{
		    	   JCoDestination destination = SAPConn.connect();
		            //调用函数  
		            function = destination.getRepository().getFunction("Z_SRM_MM_POPRICE");  
		           // JCoParameterList input = function.getImportParameterList();  
		            JCoTable tableInput = function.getTableParameterList().getTable("IT_PO");
		            iLogger.log("调用接口名称 ： "+ "Z_SRM_MM_POPRICE" +" ; 表名： IT_PO"+"\r\n");

		            for (OrderPriceVo vo : input) {
						tableInput.appendRow();
			            tableInput.setValue("EBELN", vo.getEBELN());  //采购凭证号 
			            tableInput.setValue("EBELP", vo.getEBELP());  //采购凭证的项目编号 
					}
		            function.execute(destination);  
		        	JCoTable T_RETURN = function.getTableParameterList().getTable("IT_RETURN");
		        	 for (int i = 0; i < T_RETURN.getNumRows(); i++) {
		        		 T_RETURN.setRow(i);

		        		 String EBELN=T_RETURN.getString("EBELN");
		        		 Integer EBELP=T_RETURN.getInt("EBELP");
		        		 String MATNR=T_RETURN.getString("MATNR");
		        		 String MAKTX=T_RETURN.getString("MAKTX");
		        		 String WAERS=T_RETURN.getString("WAERS");
		        		 
		        		 Double ZZSDJ=T_RETURN.getDouble("ZZSDJ");
		        		 Double ZHD=T_RETURN.getDouble("ZHD");
		        		 
		        		 OrderPriceVo sapResult=new OrderPriceVo();
		        		 sapResult.setEBELN(EBELN);
		        		 sapResult.setEBELP(EBELP);
		        		 sapResult.setMATNR(MATNR);
		        		 sapResult.setMAKTX(MAKTX);
		        		 sapResult.setWAERS(WAERS);
		        		 sapResult.setZZSDJ(ZZSDJ);
		        		 sapResult.setZHD(ZHD);
		        		 
		        		 res.add(sapResult);
		        	 }
		        	
		       }catch(Throwable e) {  
		    	   iLogger.log(e.toString()+"\r\n");
		            e.printStackTrace();  
		        	log.append(e.getMessage());
		        }finally{
		        	log.append("调用sap接口，采购订单价格读取结束").append(DateUtil.getCurrentDate());
		        	iLogger.log("调用sap接口，采购订单价格读取结束"+DateUtil.getCurrentDate()+"\r\n");

		        	fw.write(log.toString());
					fw.flush();
					fw.close();
		        }
		       return res;
	    }
	  
	    public static List<SapResult> changeOrder(List<PurchaseOrderItemEntity> items,ILogger iLogger)throws Exception{
			FileWriter fw = initFw(PurchaseOrderSAP.class);
			StringBuffer log =new StringBuffer();
			log.append("调用sap接口，修改采购订单开始").append(DateUtil.getCurrentDate());
			iLogger.log("调用sap接口，修改采购订单开始"+DateUtil.getCurrentDate()+"\r\n");
			JCoFunction function = null;  
		         
		       List<SapResult> res=new ArrayList<SapResult>();
		       try{
		    	   JCoDestination destination = SAPConn.connect();
		            //调用函数  
		            function = destination.getRepository().getFunction(ORDER_CHANGE);  
		           // JCoParameterList input = function.getImportParameterList();  
		            JCoTable tableInput = function.getTableParameterList().getTable("IT_INPUT");
		            iLogger.log("调用接口名称 ： "+ ORDER_CHANGE +"\r\n");

		            for (PurchaseOrderItemEntity item : items) {
						tableInput.appendRow();
			            tableInput.setValue("EBELN", item.getOrder().getOrderCode());  //采购凭证号 
			            tableInput.setValue("EBELP", item.getItemNo());  //采购凭证的项目编号 
			            tableInput.setValue("MENGE", item.getOrderQty());//数量
			            
					}
		            function.execute(destination);  
		        	JCoTable T_RETURN = function.getTableParameterList().getTable("IT_RETURN");
		        	 for (int i = 0; i < T_RETURN.getNumRows(); i++) {
		        		 T_RETURN.setRow(i);
		        		 String EBELN=T_RETURN.getString("EBELN");
		        		 String EBELP=T_RETURN.getString("EBELP");
		        		 String FLAG=T_RETURN.getString("FLAG");
		        		 String MESS=T_RETURN.getString("MESS");
		        		 SapResult sapResult=new SapResult();
		        		 sapResult.setFlag(FLAG);
		        		 sapResult.setMsg(MESS);
		        		 res.add(sapResult);
		        	 }
		        	
		       }catch(Throwable e) { 
		    	   iLogger.log(e.toString()+"\r\n");
		            e.printStackTrace();  
		        	log.append(e.getMessage());
		        }finally{
		        	log.append("调用sap接口，修改采购订单结束").append(DateUtil.getCurrentDate());
		        	iLogger.log("调用sap接口，修改采购订单结束"+DateUtil.getCurrentDate()+"\r\n");

		        	fw.write(log.toString());
					fw.flush();
					fw.close();
		        }
		       return res;
	    }
	
    public static List<SapResult> confirmOrder(List<PurchaseOrderItemEntity> items,ILogger iLogger)throws Exception{
		FileWriter fw = initFw(PurchaseOrderSAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，确认采购订单开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，确认采购订单开始"+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
	      
	       List<SapResult> res=new ArrayList<SapResult>();
	       try{
	    	   JCoDestination destination = SAPConn.connect(); 
	            //调用函数  
	            function = destination.getRepository().getFunction(ORDER_CONFIRM);  
	           // JCoParameterList input = function.getImportParameterList();  
	            JCoTable tableInput = function.getTableParameterList().getTable(ORDER_CONFIRM_TABLE_INPUT);
	            iLogger.log("调用接口名称 ： "+ ORDER_CONFIRM +"\r\n");
	            for (PurchaseOrderItemEntity item : items) {
					tableInput.appendRow();
		            tableInput.setValue("EBELN", item.getOrder().getOrderCode());  //采购凭证号 
		            tableInput.setValue("EBELP", item.getItemNo());  //采购凭证的项目编号 
				}
	            function.execute(destination);  
	        	JCoTable T_RETURN = function.getTableParameterList().getTable("IT_RETURN");
	        	 for (int i = 0; i < T_RETURN.getNumRows(); i++) {
	        		 T_RETURN.setRow(i);
	        		 iLogger.log("第"+i+"条数据：  "+T_RETURN+"\r\n");

	        		 String EBELN=T_RETURN.getString("EBELN");
	        		 String EBELP=T_RETURN.getString("EBELP");
	        		 String FLAG=T_RETURN.getString("FLAG");
	        		 String MESS=T_RETURN.getString("MESS");
	        		 SapResult sapResult=new SapResult();
	        		 sapResult.setFlag(FLAG);
	        		 sapResult.setMsg(MESS);
	        		 res.add(sapResult);
	        	 }
	        	
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，确认采购订单结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，确认采购订单结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
    }
    		
	
	
	
	
   

}
