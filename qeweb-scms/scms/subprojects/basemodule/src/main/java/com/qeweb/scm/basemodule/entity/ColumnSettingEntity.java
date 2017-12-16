package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_column_setting")
public class ColumnSettingEntity extends BaseEntity{

	private UserEntity user;
	
	private Long userId;
	
	private String path;
	
	private String table;
	
	private String sortString;

	@ManyToOne
	@JoinColumn(name="USER_ID",insertable=false,updatable=false)
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	@Column(name="USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Column(name="TABLE_ID")
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	@Column(name="SORT_WIDTH_STRING")
	public String getSortString() {
		return sortString;
	}

	public void setSortString(String sortString) {
		this.sortString = sortString;
	}
	
}
