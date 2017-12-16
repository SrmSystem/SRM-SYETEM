package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.listener.PersistenceObjectListener;

@MappedSuperclass
@EntityListeners({PersistenceObjectListener.class})
public class BaseEntity extends IdEntity{
	
	private Timestamp createTime;
	private Timestamp lastUpdateTime;
	private Long createUserId;
	private Long updateUserId;
	private String createUserName;
	private String updateUserName;
	private Integer abolished;
	
	private int isOutData;//是否来源于外部数据
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}
	@Transient
	public int getIsOutData() {
		return isOutData;
	}
	public void setIsOutData(int isOutData) {
		this.isOutData = isOutData;
	}
	
	
	
	
	

}
