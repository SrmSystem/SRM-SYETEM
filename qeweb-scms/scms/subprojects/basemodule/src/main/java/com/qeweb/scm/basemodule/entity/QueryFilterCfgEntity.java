package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "qeweb_query_filter_cfg")
public class QueryFilterCfgEntity extends IdEntity {

	private String clazz; 		// 过滤类
	private String method;	    // 方法(暂不使用)
	private String dataNames; 	// 数据权限名称
	private String dataTypes; 	// 类型
	private String remark; 		// 备注

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Transient
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDataNames() {
		return dataNames;
	}

	public void setDataNames(String dataNames) {
		this.dataNames = dataNames;
	}

	public String getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
