package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.RoleEntity;


@Entity
@Table(name = "QEWEB_WARN_ITEM")
public class WarnItemEntity extends BaseEntity {
	  
	  private Long warnMainId;//
	  
	  private String warnContent;//内容
	  
	  private Integer warnTime;//时间
	  
	  private Long roleId;//角色
	  
	  private RoleEntity role;
	  
	  
	  
	public Long getWarnMainId() {
		return warnMainId;
	}
	public void setWarnMainId(Long warnMainId) {
		this.warnMainId = warnMainId;
	}
	public String getWarnContent() {
		return warnContent;
	}
	public void setWarnContent(String warnContent) {
		this.warnContent = warnContent;
	}
	public Integer getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(Integer warnTime) {
		this.warnTime = warnTime;
	}
@Column(name="ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId2) {
		this.roleId = roleId2;
	}
	@ManyToOne
	@JoinColumn(name="ROLE_ID",insertable=false,updatable=false)
	public RoleEntity getRole() {
		return role;
	}
	public void setRole(RoleEntity role) {
		this.role = role;
	}
	  
	  
}
