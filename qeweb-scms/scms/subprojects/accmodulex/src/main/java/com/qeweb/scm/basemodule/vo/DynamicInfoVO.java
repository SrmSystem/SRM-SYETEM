package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

/**
 * 导入动态字段配置
 * @author ALEX
 *
 */
@ExcelTransfer(start = 1,describe="动态字段配置")
public class DynamicInfoVO {
	
	@ExcelCol(column=Column.B,required=true)
	private String beanId; 						// beanID
	
	@ExcelCol(column=Column.C,required=true)
	private String objectName; 					// 对象名称
	
	@ExcelCol(column=Column.D,required=true)
	private String enable;						// 启用 N：禁用 Y:启用
	
	@ExcelCol(column=Column.E)
	private String remark; 						// 备注
	
	@ExcelCol(column=Column.F,required=true)
	private String colCode;						//字段编码
	
	@ExcelCol(column=Column.G,required=true)
	private String name;						//显示名称
	
	@ExcelCol(column=Column.H)
	private String range;						//值范围  1=A;2=B 枚举类型时使用
	
	@ExcelCol(column=Column.I,required=true)
	private String type;						//标签类型input,select,checkbox,radio
	
	@ExcelCol(column=Column.J)
	private String statusKey;					//当类型为枚举类型时使用
	
	@ExcelCol(column=Column.K,required=true)
	private String way;							//使用场景 query,edit
	
	@ExcelCol(column=Column.L)
	private String filter;						//是否作为筛选条件
	
	@ExcelCol(column=Column.M)
	private String required;					//是否必须
	
	@ExcelCol(column=Column.N,required=true)
	private String show;						//是否显示

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}
	
}
