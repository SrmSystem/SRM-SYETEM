package com.qeweb.scm.purchasemodule.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ReplenishmentWebService {
	
	public String readinfo(@WebParam(name="asnCode") String asnCode);

}
