package com.qeweb.scm.purchasemodule.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface OrderUpdateWebService {
	
	public String writeinfo(@WebParam(name="xml") String xml);

}
