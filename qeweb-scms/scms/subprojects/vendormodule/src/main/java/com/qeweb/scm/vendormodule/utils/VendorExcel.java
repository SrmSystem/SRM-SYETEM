package com.qeweb.scm.vendormodule.utils;

import java.util.List;

import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;

/**
 * 导出供应商信息用的
 * @author Administrator
 *
 */
public class VendorExcel {

	private String mainPER;
	
	private List<VendorSurveyDataEntity> list;
	
	private double  threeyear;
	
	private double  twoyear;
	
	private double  oneyear;
	
	public List<VendorSurveyDataEntity> getList() {
		return list;
	}

	public void setList(List<VendorSurveyDataEntity> list) {
		this.list = list;
	}

	public String getMainPER() {
		return mainPER;
	}

	public void setMainPER(String mainPER) {
		this.mainPER = mainPER;
	}

	public double getThreeyear() {
		return threeyear;
	}

	public void setThreeyear(double threeyear) {
		this.threeyear = threeyear;
	}

	public double getTwoyear() {
		return twoyear;
	}

	public void setTwoyear(double twoyear) {
		this.twoyear = twoyear;
	}

	public double getOneyear() {
		return oneyear;
	}

	public void setOneyear(double oneyear) {
		this.oneyear = oneyear;
	}
}
