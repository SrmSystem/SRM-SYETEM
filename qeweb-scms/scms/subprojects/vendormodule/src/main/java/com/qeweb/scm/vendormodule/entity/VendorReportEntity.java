package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name="QEWEB_VENDOR_REPORT")
public class VendorReportEntity  extends BaseEntity{

	private String reportName;		
	private String reportUrl;
	private Long orgid;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}	
	
}
