package com.qeweb.scm.vendorperformancemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.entity.BaseEntity;

@ExcelTransfer(start=1, describe = "参评设置导入") 
public class VendorPerforReviewsVo extends BaseEntity{

	@ExcelCol(column = Column.B, required = true,desc="绩效模式")
	private String performanceModelName;
	
	@ExcelCol(column = Column.C, required = true,desc="供应商编码")
	private String orgCode;
	
	@ExcelCol(column = Column.D, desc="供应商名称")	
	private String orgName;
	
	@ExcelCol(column = Column.E, required = true, desc="周期")	
	private String cycleName;
	
	@ExcelCol(column = Column.F, required = true, desc="是否参评")	
	private String joinStatus;

	public String getPerformanceModelName() {
		return performanceModelName;
	}

	public void setPerformanceModelName(String performanceModelName) {
		this.performanceModelName = performanceModelName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(String joinStatus) {
		this.joinStatus = joinStatus;
	}
	
}
