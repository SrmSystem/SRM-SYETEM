package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_operation_journal")
public class LogEntity extends BaseEntity {
	
	private String userLogin;	//登录帐号

	private String orgName;		//组织名称
	
	private String operationName;//操作模块
	
	private String content;		//操作内容
	
	private String ip;			//用户IP
	
	private String os;			//用户操作系统
	
	private String exception; 	// 异常信息
	
	private String requestUrl;//请求的URL
	
	private String requestPage;//请求Page

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestPage() {
		return requestPage;
	}

	public void setRequestPage(String requestPage) {
		this.requestPage = requestPage;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
}