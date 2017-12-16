package com.qeweb.scm.basemodule.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.service.DynamicDataService;
import com.qeweb.scm.basemodule.service.SerialNumberService;

/**
 * 抽象服务类，提出公共的东西
 * @author pjjxiajun
 * @date 2015年6月18日
 * @path com.qeweb.scm.basemodule.service.common.BaseService.java
 */
@Service
public abstract class BaseService {
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private DynamicDataService dynamicDataService;

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public DynamicDataService getDynamicDataService() {
		return dynamicDataService;
	}

	public void setDynamicDataService(DynamicDataService dynamicDataService) {
		this.dynamicDataService = dynamicDataService;
	}
	
}
