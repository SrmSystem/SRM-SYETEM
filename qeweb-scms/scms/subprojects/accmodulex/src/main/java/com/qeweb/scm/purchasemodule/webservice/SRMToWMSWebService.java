package com.qeweb.scm.purchasemodule.webservice;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SRMToWMSWebService {
	
	private static final Logger logger = LoggerFactory.getLogger(SRMToWMSWebService.class);
	
	String endpointAddress="http://172.16.200.130/SRMInterface/SRMSupplyLabel.asmx";
	String nameSpace = "http://tempuri.org/";
	
	public String writeinfo(String orgID,String vendorcode,String vendorname,String asnnolist,
			String asnstatuslist,String ponumberlist,String polinelist,String mitemnamelist,
			String boxlabellist,String qtylist,String mversionlist,String hbrequirelist,
			String wharahouselist,String singlebarcodelist,String usercode) throws Exception{
		logger.info("开始调用wms接口 ....");
		logger.info("orgID数据为：" + orgID);
		logger.info("vendorcode数据为：" + vendorcode);
		logger.info("vendorname数据为：" + vendorname);
		logger.info("asnnolist数据为：" + asnnolist);
		logger.info("asnstatuslist数据为：" + asnstatuslist);
		logger.info("ponumberlist数据为：" + ponumberlist);
		logger.info("polinelist数据为：" + polinelist);
		logger.info("mitemnamelist数据为：" + mitemnamelist);
		logger.info("boxlabellist数据为：" + boxlabellist);
		logger.info("qtylist数据为：" + qtylist);
		logger.info("mversionlist数据为：" + mversionlist);
		logger.info("hbrequirelist数据为：" + hbrequirelist);
		logger.info("wharahouselist数据为：" + wharahouselist);
		logger.info("singlebarcodelist数据为：" + singlebarcodelist);
		logger.info("usercode数据为：" + usercode);
		Service service = new Service();
		Call call=(Call)service.createCall();              
        call.setTargetEndpointAddress(endpointAddress);              
        call.setOperationName(new QName(nameSpace,"SRMwriteinfo")); //设置要调用哪个方法  
        
        call.addParameter( "orgID", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "vendorcode", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "vendorname", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "asnnolist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "asnstatuslist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "ponumberlist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "polinelist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "mitemnamelist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "boxlabellist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "qtylist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "mversionlist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "hbrequirelist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "wharahouselist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "singlebarcodelist", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
        call.addParameter( "usercode", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);  
      
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING); // 要返回的数据类型
         
        call.setUseSOAPAction(true);  
        call.setSOAPActionURI(nameSpace + "SRMwriteinfo");      
        String result =(String) call.invoke(new Object[]{orgID,vendorcode,vendorname,asnnolist,asnstatuslist,ponumberlist,polinelist,mitemnamelist,boxlabellist,qtylist,mversionlist,hbrequirelist,wharahouselist,singlebarcodelist,usercode});
        logger.info("调用结束，返回结果为：" + result);    
        return result;
	}
	
	
	public static void main(String[] args) {
		String orgID ="1000";
		String vendorcode ="VENDOR201603090001";
		String vendorname ="you";
		String asnnolist ="17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001,17216000001";
		String asnstatuslist ="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
		String ponumberlist ="201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022,201701022";
		String polinelist ="5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5";
		String mitemnamelist ="10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001,10001";
		String boxlabellist ="170216000004,170216000004,170216000004,170216000004,170216000004,170216000004,170216000004,170216000004,170216000005,170216000005,170216000005,170216000005,170216000005,170216000005,170216000005,170216000005,170216000006,170216000006,170216000006,170216000006,170216000006,170216000006,170216000006,170216000006,170216000007";
		String qtylist ="8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,8.0,1.0";
		String mversionlist =",,,,,,,,,,,,,,,,,,,,,,,,";
		String hbrequirelist ="含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅,含铅";
		String wharahouselist =",,,,,,,,,,,,,,,,,,,,,,,,";
		String singlebarcodelist ="172160000,172160001,172160002,172160003,172160004,172160005,172160006,172160007,172160008,172160009,172160010,172160011,172160012,172160013,172160014,172160015,172160016,172160017,172160018,172160019,172160020,172160021,172160022,172160023,172160024";
		String usercode ="YOU";
		try {
			String result = new SRMToWMSWebService().writeinfo(orgID, vendorcode, vendorname, asnnolist, asnstatuslist, ponumberlist, polinelist, mitemnamelist, boxlabellist, qtylist, mversionlist, hbrequirelist, wharahouselist, singlebarcodelist, usercode);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
