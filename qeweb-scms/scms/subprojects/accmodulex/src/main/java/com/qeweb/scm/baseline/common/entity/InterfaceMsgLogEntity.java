package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;


@Entity
@Table(name = "QEWEB_INTERFACE_MSG_LOG")
public class InterfaceMsgLogEntity extends BaseEntity {
	
	private String msgId;
	private InterfaceMsgEntity msg;
	private String dmlType;
	private String remark;
	private String logType;
	private String logContent;
	private String sql;
	@Column(name="msg_id",updatable=false,insertable=false)
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	@ManyToOne
	@JoinColumn(name="msg_id")
	public InterfaceMsgEntity getMsg() {
		return msg;
	}
	public void setMsg(InterfaceMsgEntity msg) {
		this.msg = msg;
	}
	public String getDmlType() {
		return dmlType;
	}
	public void setDmlType(String dmlType) {
		this.dmlType = dmlType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

}
