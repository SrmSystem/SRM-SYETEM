package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
@Entity
@Table(name = "QEWEB_RUNAS")
public class RunAsEntity extends IdEntity{

	private Long fromUserId;
	private Long toUserId;
	private UserEntity fromUser;
	private UserEntity toUser;
	
	@Column(insertable=false,updatable=false,name="from_user_id")
	public Long getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	@Column(insertable=false,updatable=false,name="to_user_id")
	public Long getToUserId() {
		return toUserId;
	}
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	@ManyToOne
	@JoinColumn(name="from_user_id")
	public UserEntity getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserEntity fromUser) {
		this.fromUser = fromUser;
	}
	@ManyToOne
	@JoinColumn(name="to_user_id")
	public UserEntity getToUser() {
		return toUser;
	}
	public void setToUser(UserEntity toUser) {
		this.toUser = toUser;
	}
	
	
}
