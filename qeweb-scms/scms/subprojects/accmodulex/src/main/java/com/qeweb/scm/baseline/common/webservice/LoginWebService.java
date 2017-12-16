package com.qeweb.scm.baseline.common.webservice;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.SYMesWebService;
import org.tempuri.SYMesWebServiceLocator;
import org.tempuri.SYMesWebServiceSoapStub;

public class LoginWebService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginWebService.class);
	

	
	public Boolean login(String username,String password) {
	
		try {
			SYMesWebService ss=new SYMesWebServiceLocator();
			SYMesWebServiceSoapStub s=(SYMesWebServiceSoapStub)ss.getSYMesWebServiceSoap();
			boolean bool=s.isAuthenticated(username, password);
	        return bool;
		} catch (Exception e) {
			return false;
		}              
       
	}
	
	
	public static void main(String[] args) {
			String name="sz100423";
			String password="Sap.12345678";
			Boolean result = new LoginWebService().login("","");
			System.out.println(result);
			System.out.println();
		
	}
}
