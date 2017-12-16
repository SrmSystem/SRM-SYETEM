package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "qeweb_role_user")
public class RoleUserEntity extends IdEntity{
	
	private long userId;
	private long roleId;
	private UserEntity user;
	private RoleEntity role; 
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
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
	
}
