package com.qeweb.scm.vendorperformancemodule.vo;

import java.sql.Timestamp;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.entity.BaseEntity;

@ExcelTransfer(start=1, describe = "参数设置导入") 
public class VendorPerforParamVo extends BaseEntity{

	@ExcelCol(column = Column.B, required = true, desc="供应商编码")	
	private String orgCode;
	
	@ExcelCol(column = Column.C, desc="供应商名称")	
	private String orgName;
	
	@ExcelCol(column = Column.D, required = true, desc="周期")	
	private String cycleName;
	
	@ExcelCol(column = Column.E, desc="评估时间")	
	private Timestamp assessDate; 	//评估时间
	
	@ExcelCol(column = Column.F, required = true, desc="品牌编码")	
	private String brandCode;		//品牌编码
	
	@ExcelCol(column = Column.G, desc="品牌名称")	
	private String brandName;		//品牌名称
	
	@ExcelCol(column = Column.H, required = true, desc="工厂名称")	
	private String factoryName;		//工厂名称
	
	@ExcelCol(column = Column.I, required = true, desc="参数值")	
	private String parameterValue;	//参数值
	
	@ExcelCol(column = Column.J, required = true, desc="状态")	
	private String joinStatus;		//状态

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

	public Timestamp getAssessDate() {
		return assessDate;
	}

	public void setAssessDate(Timestamp assessDate) {
		this.assessDate = assessDate;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(String joinStatus) {
		this.joinStatus = joinStatus;
	}
	
}
