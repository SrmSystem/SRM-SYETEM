package com.qeweb.sap;  
  
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
 
/**
  * 物料信息 
  *
  */
public class MaterialSAP extends CommonSapFw{  

	public static void main(String[] args) throws Exception {

	}
	
    public static List<VendorMaterialRelEntity> getMaterialByVendorCode(List<OrganizationEntity> list,ILogger iLogger) throws Exception {  
    	FileWriter fw = initFw(FactorySAP.class);
    	List<VendorMaterialRelEntity> data=null;
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取供应商物料信息开始").append(DateUtil.getCurrentDate());
		iLogger.log("调用sap接口，获取供应商物料信息开始"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null; 

	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	   data=new ArrayList<VendorMaterialRelEntity>();
		       String result="";//调用接口返回状态  
		       String message="";//调用接口返回信息  
	    	 //调用函数  
	    	   function = destination.getRepository().getFunction("Z_SRM_MM_VENDOR_MATREAD"); 
	    	   JCoTable tableInput = function.getTableParameterList().getTable("IT_INPUT");
	    	   iLogger.log("调用接口名称 ： "+ "Z_SRM_MM_VENDOR_MATREAD" +" ; 输入的表名： IT_INPUT"+"\r\n");

	            for (OrganizationEntity m : list) {
	            	tableInput.appendRow();
	            	tableInput.setValue("LIFNR", m.getCode());
				}
	            function.execute(destination); 
	            result= function.getExportParameterList().getString("FLAG");//调用接口返回状态  
	            message= function.getExportParameterList().getString("MESS");//调用接口返回信息  
	            log.append("状态:").append(result).append("\r\n");
	            log.append("信息:").append(message).append("\r\n");
	            
	            iLogger.log("状态: "+result +" \r\n");
	            iLogger.log("信息: "+message +" \r\n");
	            
	            JCoTable T_RESULT = function.getTableParameterList().getTable("IT_RETURN");
	            iLogger.log("输出的表名：  IT_RETURN"+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成MaterialEntity\r\n");
	            	 iLogger.log("总条数:"+T_RESULT.getNumRows()+"开始转成MaterialEntity\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	    				 T_RESULT.setRow(i);

	    				String LIFNR=T_RESULT.getString("LIFNR");//供应商编号
		 	            String MATNR=T_RESULT.getString("MATNR");//物料号
		 	            String MAKTX=T_RESULT.getString("MAKTX");//物料描述
		 	            String WERKS=T_RESULT.getString("WERKS");//工厂
		 	            String NAME2=T_RESULT.getString("NAME2");//工厂名称
		 	            
		 	            String MEINS=T_RESULT.getString("MEINS");//单位
		 	            String CDQWL=T_RESULT.getString("CDQWL");//X长周期 空白短周期
		 	            VendorMaterialRelEntity m=new VendorMaterialRelEntity();
		 	            m.setVendorCode(LIFNR);
		 	            m.setMaterialCode(MATNR);
		 	            m.setMaterialName(MAKTX);
		 	            m.setFactoryCode(WERKS);
		 	            m.setMaterialUnit(MEINS);
		 	            m.setCdqwl(CDQWL);
		 	            data.add(m);
	    			 }
	            }else{
	            	iLogger.log("IT_RETURN 中未获得到数据"+"\r\n");

	            }  
 
	       }catch(Throwable e) {  
	    	   iLogger.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，获取供应商物料信息结束").append(DateUtil.getCurrentDate());
	        	iLogger.log("调用sap接口，获取供应商物料信息结束"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return data;
    }  
    
 
}  