package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户预警的关系表
 * @author ELEVEN
 * @date 2017年9月7日
 * @path com.qeweb.scm.basemodule.entity.UserWarnRelEntity.java
 */
@Entity
@Table(name="QEWEB_USER_WARN_REL")
public class UserWarnRelEntity extends IdEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
     * 用户
     */
	private Long userId;
	
    /**
     * 角色
     */
	private Long roleId;
	
    /**
     *   角色用户
     */
	private Long roleUserId;
	

	/**
     * 用户
     */
	private UserEntity user;
	
    /**
     * 角色
     */
	private RoleEntity role;
	
    /**
     *   角色用户
     */
	private UserEntity roleUser;

	@ManyToOne
	@JoinColumn(name="userId",insertable=false,updatable=false)
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name="roleId",insertable=false,updatable=false)
	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	@ManyToOne
	@JoinColumn(name="roleUserId",insertable=false,updatable=false)
	public UserEntity getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(UserEntity roleUser) {
		this.roleUser = roleUser;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleUserId() {
		return roleUserId;
	}

	public void setRoleUserId(Long roleUserId) {
		this.roleUserId = roleUserId;
	}

}
