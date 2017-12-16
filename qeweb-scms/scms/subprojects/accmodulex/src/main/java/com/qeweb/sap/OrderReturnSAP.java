package com.qeweb.sap;  
  
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.sap.vo.OrderReturnVo;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
 
/**
  * 退货接口
  *
  */
public class OrderReturnSAP extends CommonSapFw{  

	public static void main(String[] args) throws Exception {
		/*String date=DateUtil.dateToString(DateUtil.getCurrentTimestamp(), "yyyyMMdd");
		List<OrderReturnVo> res=getReturnByOrderItem(date);
		System.out.println(res.size());*/
	}
	
    public static List<OrderReturnVo> getReturnByOrderItem(String date,ILogger iLogger) throws Exception {  
    	FileWriter fw = initFw(FactorySAP.class);
    	List<OrderReturnVo> data=new ArrayList<OrderReturnVo>();
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取订单退货信息开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取订单退货信息开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();
	    	 //调用函数  
	            function = destination.getRepository().getFunction("Z_SRM_MM_POTHREAD");  
	            JCoParameterList input = function.getImportParameterList();  
	           	 //参数
	           	input.setValue("I_DATE",date);
/*	       		JCoTable tableInput = function.getTableParameterList().getTable("IT_INPUT");
	    	      for (PurchaseOrderItemEntity item : list) {
		            	tableInput.appendRow();
		            	tableInput.setValue("EBELN", item.getOrder().getOrderCode());//采购凭证号 
		            	tableInput.setValue("EBELP", item.getItemNo());//采购凭证的项目编号 
	    	      }*/
	 	            function.execute(destination); 
		            JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
		            iLogger.log("调用接口名称 ： "+ "Z_SRM_MM_POTHREAD" +" ; 表名： IT_RETURN"+"\r\n");

		            if (T_RESULT != null && !T_RESULT.isEmpty()) {
		            	 log.append("总条数:").append(T_RESULT.getNumRows());
		            	 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
		    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
		 					T_RESULT.setRow(i);

		 					 String EBELN=T_RESULT.getString("EBELN");		//采购凭证号 
		 					 Integer EBELP=T_RESULT.getInt("EBELP");		//采购凭证的项目编号 
		 					
		 					 String MEINS=T_RESULT.getString("MEINS");		//基本计量单位
		 					 Double MENGE=T_RESULT.getDouble("MENGE");		//数量
		 					 
		 					String BUDAT=T_RESULT.getString("BUDAT");		//过账日期
		 					String CPUTM=T_RESULT.getString("CPUTM");		//时间
		 					
		 					OrderReturnVo vo=new OrderReturnVo();
		 					vo.setEBELN(EBELN);
		 					vo.setEBELP(EBELP);
		 					vo.setMEINS(MEINS);
		 					vo.setMENGE(MENGE);
		 					vo.setBUDAT(BUDAT);
		 					vo.setCPUTM(CPUTM);
		 					data.add(vo);

		    			 }
		            }else{
		            	iLogger.log("IT_RETURN 中未获得到数据"+"\r\n");
	
		            }

	            	
	           
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");

	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取订单退货信息结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取订单退货信息结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return data;
    }
	     
    
 
}  