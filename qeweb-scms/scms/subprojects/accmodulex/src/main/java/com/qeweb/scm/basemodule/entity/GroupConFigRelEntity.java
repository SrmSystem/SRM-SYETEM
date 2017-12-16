package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户配置采购组的关系表
 * @author ELEVEN
 * @date 2017年9月14日
 * @path com.qeweb.scm.basemodule.entity.GroupConFigRelEntity.java
 */
@Entity
@Table(name="QEWEB_GROUP_CONFIG_REL")
public class GroupConFigRelEntity extends IdEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
     * 用户
     */
	private Long userId;
	
	/**
     * 用户
     */
	private UserEntity user;
	
	/**
     * 采购组ids
     */
	private String   groupIds;
	

	@ManyToOne
	@JoinColumn(name="userId",insertable=false,updatable=false)
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}


}
