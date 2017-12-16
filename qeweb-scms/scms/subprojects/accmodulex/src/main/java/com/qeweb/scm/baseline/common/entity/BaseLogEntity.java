package com.qeweb.scm.baseline.common.entity;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;


@Entity
@Table(name = "QEWEB_BASE_LOG")
public class BaseLogEntity extends BaseEntity {
	
	private Long billId;//业务单据ID
	
	private String billType;//类型 目前记录用户信息修改日志  10，后续扩展
	
	private Clob logContent;
	
	private String optUserName;		//操作人名称
	
	private String pcName;			//操作电脑主机名
	
	private String roleType;//区分采购商供应商
	
	private String ip;
	
	//冗余字段
	private String contentStr;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@JsonIgnore
	public Clob getLogContent() {
		return logContent;
	}

	public void setLogContent(Clob logContent) {
		this.logContent = logContent;
	}

	@Column(name="OPTUSER_NAME")
	public String getOptUserName() {
		return optUserName;
	}

	public void setOptUserName(String optUserName) {
		this.optUserName = optUserName;
	}

	@Column(name="PC_NAME")
	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	@Transient
	public String getContentStr() {
		return contentStr;
	}

	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
	
	
	
	
}
