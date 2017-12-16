package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
@ExcelTransfer(start=1, describe = "各维度供应商数量统计") 
public class StatisticsVendorCountTransfer {
	
	@ExcelCol(column = Column.B,desc="省份")
	private String province;
	@ExcelCol(column = Column.C,desc="供应商性质")
	private String phase;
	@ExcelCol(column = Column.D,desc="供应商分类")
	private String classify;
	@ExcelCol(column = Column.E,desc="系统")
	private String materialSystem;
	@ExcelCol(column = Column.F,desc="零部件类别")
	private String partsType;
	@ExcelCol(column = Column.G,desc="业务类型")
	private String bussinessType;
	@ExcelCol(column = Column.H,desc="工厂供货距离")
	private String distance;
	@ExcelCol(column = Column.I,desc="供应商总数")
	private String totalCount;
	
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getMaterialSystem() {
		return materialSystem;
	}
	public void setMaterialSystem(String materialSystem) {
		this.materialSystem = materialSystem;
	}
	public String getPartsType() {
		return partsType;
	}
	public void setPartsType(String partsType) {
		this.partsType = partsType;
	}
	public String getBussinessType() {
		return bussinessType;
	}
	public void setBussinessType(String bussinessType) {
		this.bussinessType = bussinessType;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
