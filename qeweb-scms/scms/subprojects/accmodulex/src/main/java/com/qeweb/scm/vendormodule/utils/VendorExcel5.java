package com.qeweb.scm.vendormodule.utils;

import java.util.List;

import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;

/**
 * 导出供应商信息用的
 * @author Administrator
 *
 */
public class VendorExcel5 {

	private String name;
	
	private List<VendorSurveyDataEntity> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VendorSurveyDataEntity> getList() {
		return list;
	}

	public void setList(List<VendorSurveyDataEntity> list) {
		this.list = list;
	}

}
