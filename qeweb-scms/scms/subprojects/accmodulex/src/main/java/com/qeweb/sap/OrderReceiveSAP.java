package com.qeweb.sap;  
  
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.sap.vo.OrderReceiveVo;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
 
/**
  * 收货接口
  *
  */
public class OrderReceiveSAP extends CommonSapFw{  
	
	public static void main(String[] args) throws Exception {
		/*String date=DateUtil.dateToString(DateUtil.getCurrentTimestamp(), "yyyyMMdd");
		List<OrderReceiveVo> res=getReceiveByDn(date);
		System.out.println(res.size());*/
	}
	
    public static List<OrderReceiveVo> getReceiveByDn(String dateS,String dateE,ILogger iLogger) throws Exception { 
    	FileWriter fw = initFw(FactorySAP.class);
    	List<OrderReceiveVo> data=null;
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取订单收货信息开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取订单收货信息开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null; 
	       try{
	    	   JCoDestination destination = SAPConn.connect(); 
	    	   data=new ArrayList<OrderReceiveVo>();
	    	 //调用函数  
	            function = destination.getRepository().getFunction("Z_SRM_MM_DNPOST_READ");  
/*	       		JCoTable tableInput = function.getTableParameterList().getTable("IT_INPUT");
	    	      for (DeliveryItemEntity item : list) {
		            	tableInput.appendRow();
		            	tableInput.setValue("VBELN", item.getDn());
	    	      }*/
	            JCoParameterList input = function.getImportParameterList();  
	           	 //参数
	           	input.setValue("I_DATE_F",dateS);
	           	input.setValue("I_DATE_T",dateE);
	 	            function.execute(destination); 
		            JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
		            iLogger.log("调用接口名称 ： "+ "Z_SRM_MM_DNPOST_READ" +" ; 表名： IT_RETURN"+"\r\n");

		            if (T_RESULT != null && !T_RESULT.isEmpty()) {
		            	 log.append("总条数:").append(T_RESULT.getNumRows());
		            	 iLogger.log("总条数:"+T_RESULT.getNumRows()+"\r\n");
		    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
		 					T_RESULT.setRow(i);

		 					 String VBELN=T_RESULT.getString("VBELN");		//交货
		 					 String EBELN=T_RESULT.getString("EBELN");		//采购凭证号 
		 					 Integer EBELP=T_RESULT.getInt("EBELP");		//采购凭证的项目编号 
		 					 String BOLNR=T_RESULT.getString("BOLNR");		//提单
		 					 Double LFIMG=T_RESULT.getDouble("LFIMG");		//实际已交货量（按销售单位）
		 					 String MEINS=T_RESULT.getString("MEINS");		//基本计量单位
		 					 Double ZDJSL=T_RESULT.getDouble("ZDJSL");		//待检数量
		 					 Double ZZJBL=T_RESULT.getDouble("ZZJBL");		//质检不良数量
		 					 Double ZSJHG=T_RESULT.getDouble("ZSJHG");		//送检合格
		 					 Double ZLLBL=T_RESULT.getDouble("ZLLBL");		//来料不良数量
		 					String BUDAT=T_RESULT.getString("BUDAT");		//过账日期
		 					String CPUTM=T_RESULT.getString("CPUTM");		//时间
		 					String REPLCODE=T_RESULT.getString("ASN_SRM"); //补货ASN单号
		 					//增加日志
		 					log.append(i).append("-------:")
		 					.append("VBELN【交货】:").append(VBELN)
		 					.append(";EBELN【采购凭证号】：").append(EBELN)
		 					.append(";EBELP【采购凭证的项目编号】：").append(EBELP)
		 					.append(";BOLNR【提单】：").append(BOLNR)
		 					.append(";LFIMG【实际已交货量（按销售单位）】：").append(LFIMG)
		 					.append(";MEINS【基本计量单位】：").append(MEINS)
		 					.append(";ZDJSL【待检数量】：").append(ZDJSL)
		 					.append(";ZZJBL【质检不良数量】：").append(ZZJBL)
		 					.append(";ZSJHG【送检合格】：").append(ZSJHG)
		 					.append(";ZLLBL【来料不良数量】：").append(ZLLBL)
		 					.append(";BUDAT【过账日期】：").append(BUDAT)
		 					.append(";CPUTM【时间】：").append(CPUTM)
		 					.append(";ASN_SRM【补货ASN单号】：").append(REPLCODE)
		 					.append("-------\r\n");
		 					OrderReceiveVo vo=new OrderReceiveVo();
		 					vo.setVBELN(VBELN);
		 					vo.setEBELN(EBELN);
		 					vo.setEBELP(EBELP);
		 					vo.setBOLNR(BOLNR);
		 					vo.setLFIMG(LFIMG);
		 					vo.setMEINS(MEINS);
		 					vo.setZDJSL(ZDJSL);
		 					vo.setZZJBL(ZZJBL);
		 					vo.setZSJHG(ZSJHG);
		 					vo.setZLLBL(ZLLBL);
		 					vo.setBUDAT(BUDAT);
		 					vo.setCPUTM(CPUTM);
		 					vo.setREPLCODE(REPLCODE);
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
	        	log.append("调用sap接口，获取订单收货信息结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取订单收货信息结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }

	       return data;
    }  
    
 
}  