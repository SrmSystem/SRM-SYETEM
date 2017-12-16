package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;


@Entity
@Table(name = "QEWEB_INTERFACE_MSG_ITEM")
public class InterfaceMsgItemEntity extends BaseEntity {
	
	private String msgId;
	private InterfaceMsgEntity msg;
	private String insId;
	private String remark;
	private String status;
	private String finishFlag;
	private String errorFlag;
	private String errorInfo;
	private String content;
	
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
	public String getInsId() {
		return insId;
	}
	public void setInsId(String insId) {
		this.insId = insId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFinishFlag() {
		return finishFlag;
	}
	public void setFinishFlag(String finishFlag) {
		this.finishFlag = finishFlag;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

}
