package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
@ExcelTransfer(start=1, describe = "供应商准入表") 
public class VendorAdmittanceTransfer {
	@ExcelCol(column = Column.B, required = true,desc="供应商编码")
    private String orgCode;  //供应商编码
	@ExcelCol(column = Column.C, desc="企业名称")
    private String shortName;  //企业名称
	@ExcelCol(column = Column.D, desc="企业属性")
    private String property;   //企业属性
	@ExcelCol(column = Column.E, desc="国家")
    private String countryText;  //国家
	@ExcelCol(column = Column.F, desc="省份")
    private String provinceText;  //省份
	@ExcelCol(column = Column.G, desc="注册时间")
    private String regtime;   //注册时间
	@ExcelCol(column = Column.H, desc="调查表提交")
    private String surveySubmitInfo; //调查表提交
	@ExcelCol(column = Column.I, desc="调查表审核")
    private String surveyAuditInfo;  //调查表审核
	@ExcelCol(column = Column.J, desc="状态")
    private String enableStatus;  //状态
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getCountryText() {
		return countryText;
	}
	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}
	public String getProvinceText() {
		return provinceText;
	}
	public void setProvinceText(String provinceText) {
		this.provinceText = provinceText;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public String getSurveySubmitInfo() {
		return surveySubmitInfo;
	}
	public void setSurveySubmitInfo(String surveySubmitInfo) {
		this.surveySubmitInfo = surveySubmitInfo;
	}
	public String getSurveyAuditInfo() {
		return surveyAuditInfo;
	}
	public void setSurveyAuditInfo(String surveyAuditInfo) {
		this.surveyAuditInfo = surveyAuditInfo;
	}
	public String getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(String enableStatus) {
		if("1".equals(enableStatus))
			this.enableStatus = "启用";
		else
			this.enableStatus = "禁用";
	}
	
}
