package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qeweb_dynamicdata_scene")
public class DynamicDataSceneEntity extends BaseEntity {
	
	private DynamicDataEntity dataEx;
	private String colCode;			//字段编码
	private String name;			//显示名称
	private String range;			//值范围  1=A;2=B 枚举类型时使用
	private String type;			//标签类型input,select,checkbox,radio
	private String statusKey;		//当类型为枚举类型时使用
	private String way;				//使用场景 query,edit
	private boolean filter;			//是否作为筛选条件
	private boolean required;		//是否必须
	private boolean show;			//是否显示

	@ManyToOne
	@JoinColumn(name="data_ex_id")
	public DynamicDataEntity getDataEx() {
		return dataEx;
	}

	public void setDataEx(DynamicDataEntity dataEx) {
		this.dataEx = dataEx;
	}

	@Column(name="col_code")
	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="range")
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="status_key")
	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	@Column(name="way")
	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	@Column(name="filter")
	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	@Column(name="required")
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Column(name="is_show")
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
