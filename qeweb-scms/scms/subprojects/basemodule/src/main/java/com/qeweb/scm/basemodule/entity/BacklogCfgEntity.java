package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 待办配置实体
 * @author pjjxiajun
 * @date 2015年8月25日
 * @path com.qeweb.scm.basemodule.entity.BacklogCfgEntity.java
 */
@Entity
@Table(name = "qeweb_backlog_cfg")
public class BacklogCfgEntity extends BaseEntity{
	/** 待办内容 */
	private String content;
	/** 待办数量 */
	private String count;
	/** 菜单ID */
	private Long viewId;
	/** 菜单地址 */
	private String viewUrl;
	/** 菜单名称 */
	private String viewName;
	/** 待办类型 */
	private Integer orgRoleType;
	/** 指定用户 */
	private String rightUserIds;
	/** 待办查询-hql */
	private String queryHql;
	/** 待办查询-sql */
	private String querySql;
	/** 变量值 */
	private String params;
	/** 跳转后脚本片段 */
	private String scriptlet;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Long getViewId() {
		return viewId;
	}
	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	public Integer getOrgRoleType() {
		return orgRoleType;
	}
	public void setOrgRoleType(Integer orgRoleType) {
		this.orgRoleType = orgRoleType;
	}
	public String getRightUserIds() {
		return rightUserIds;
	}
	public void setRightUserIds(String rightUserIds) {
		this.rightUserIds = rightUserIds;
	}
	
	public String getQueryHql() {
		return queryHql;
	}
	public void setQueryHql(String queryHql) {
		this.queryHql = queryHql;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getScriptlet() {
		return scriptlet;
	}
	public void setScriptlet(String scriptlet) {
		this.scriptlet = scriptlet;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	

}
