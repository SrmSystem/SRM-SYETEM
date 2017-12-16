package com.qeweb.sap;  
  
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
 
/**
  * 供应商信息 
  * @author chao.gu
  * 20170510
  *
  */
public class VendorSAP extends CommonSapFw{  
	
	private static ILogger logger = new FileLogger();
	
	public static ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	
	public static void main(String[] args) throws Exception {
/*		OrganizationEntity vendor=new OrganizationEntity();
		vendor.setCode("10114");
		vendor.setName("测试");
		String msg=returnVendor(vendor);
		System.out.println(msg);*/
		
		List<String> res=getVendorByCode("10691");
		System.out.println();
	}
	
	
    public static String returnVendor(OrganizationEntity vendor) throws Exception {  
    	FileWriter fw = initFw(FactorySAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，回传上线供应商").append(DateUtil.getCurrentDate());
		getLogger().log("调用sap接口，回传上线供应商"+DateUtil.getCurrentDate()+"\r\n");

		JCoFunction function = null;  
	       try{
	    	   JCoDestination destination = SAPConn.connect();  
	    	 //调用函数  
		            function = destination.getRepository().getFunction("Z_SRM_MM_VENDOR_GET"); 

		             JCoStructure INPUT = function.getImportParameterList().getStructure("IT_INPUT");
		            	 //参数
		             INPUT.setValue("LIFNR", vendor.getCode());
		             INPUT.setValue("NAME1", vendor.getName()); 
		            	
		 	            function.execute(destination); 
		 	            JCoParameterList T_RESULT = function.getExportParameterList();
		 	            String FLAG=T_RESULT.getString("FLAG");
		 	            String MESS=T_RESULT.getString("MESS");
		 	            if("Y".equals(FLAG)){
		 	            	return "";
		 	            }else{
		 	            	return MESS;
		 	            }
		 	           
//	            }
	           
	       }catch(Throwable e) { 
	    	   getLogger().log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，回传上线供应商").append(DateUtil.getCurrentDate());
	        	getLogger().log("调用sap接口，回传上线供应商"+DateUtil.getCurrentDate()+"\r\n");

	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return "回传上线供应商接口失败";
    } 
	
	//测试使用10114和10002
    public static List<String> getVendorByCode(String code) throws Exception { 
        List<String> res=new ArrayList<String>();
    	FileWriter fw = initFw(FactorySAP.class);
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口，获取供应商信息开始").append(DateUtil.getCurrentDate());
		JCoFunction function = null;  
	       JCoDestination destination = SAPConn.connect();  
	       try{
	    	 //调用函数  
		            function = destination.getRepository().getFunction(SapTableStructure.VENDOR_Z_EIP_MM_GETLIFTXT);  
		            JCoParameterList input = function.getImportParameterList();  
		            	 //参数
		            	input.setValue(SapTableStructure.VENDOR_Z_EIP_MM_GETLIFTXT_I_LIFNR, code);  
		 	            function.execute(destination); 
		 	            JCoParameterList T_RESULT = function.getExportParameterList();
		 	            String E_NAME1=T_RESULT.getString(SapTableStructure.VENDOR_Z_EIP_MM_GETLIFTXT_E_NAME1);//名称
		 	            String E_CHARG=T_RESULT.getString("E_CHARG");//2位短编码
		 	            res.add(E_NAME1);
		 	            res.add(E_CHARG);
//	            }
	           
	       }catch(Throwable e) {  
	    	   getLogger().log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	getLogger().log("调用sap接口，获取供应商信息结束"+DateUtil.getCurrentDate()+"\r\n");
	        	log.append("调用sap接口，获取供应商信息结束").append(DateUtil.getCurrentDate());
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
				return res;
	        }
	      
    }  
    
 
}  