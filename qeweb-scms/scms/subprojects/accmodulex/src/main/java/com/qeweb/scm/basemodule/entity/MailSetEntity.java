package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_mailset")
public class MailSetEntity extends BaseEntity {
	
	private Integer mailTemplateId;
	private String mailAddress;
	private String serverAddress;
	private String account;
	private String password;
	private String mailContent;
	private String signature;
	
	@Column(name="mail_template_id")
	public Integer getMailTemplateId() {
		return mailTemplateId;
	}
	public void setMailTemplateId(Integer mailTemplateId) {
		this.mailTemplateId = mailTemplateId;
	}
	
	@Column(name="mail_address")
	public String getMailAddress() {
		return mailAddress;
	}	
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	@Column(name="server_address")
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	@Column(name="account")
	public String getAccount() {
		return account;
	}	
	public void setAccount(String account) {
		this.account = account;
	}
	
	@Column(name="password")
	public String getPassword() {
		return password;
	}	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="mail_content")
	public String getMailContent() {
		return mailContent;
	}	
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	
	@Column(name="signature")
	public String getSignature() {
		return signature;
	}	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
