package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "qeweb_message")
public class MessageEntity extends BaseEntity {

	private String title; 		// 消息标题
	private String msg; 		// 消息详情
	private long moduleId;		// 模块ID
	private long fromUserId; 	// 消息发送人
	private long toUserId;		// 接收人
	private int isRead;			// 已读状态0:否 1：是

	@Column(name="title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="msg")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Column(name="module_id")
	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	@Column(name="from_user_id")
	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	@Column(name="to_user_id")
	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	@Column(name="is_read")
	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

}
