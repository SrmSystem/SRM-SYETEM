package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;


@ExcelTransfer(start = 1,describe="物料导入")
public class MaterialImpVO {
	
	@ExcelCol(column=Column.A,required=true)
	private String code;
	
	@ExcelCol(column=Column.B,required=true)
	private String name;
	
	@ExcelCol(column=Column.C,required=true)
	private String technician; //技术人员
	
	@ExcelCol(column=Column.D,required=true)
	private String picStatus;
	
	@ExcelCol(column=Column.E)
	private String partsType;           // 零部件类别
	
	@ExcelCol(column=Column.F)
	private String partsCode;			// 零部件编码
	
	@ExcelCol(column=Column.G)
	private String partsName;			// 零部件名称
	
	@ExcelCol(column=Column.H)
	private String describe;
	
	@ExcelCol(column=Column.I)
	private String remark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getPicStatus() {
		return picStatus;
	}

	public void setPicStatus(String picStatus) {
		this.picStatus = picStatus;
	}

	public String getPartsType() {
		return partsType;
	}

	public void setPartsType(String partsType) {
		this.partsType = partsType;
	}

	public String getPartsCode() {
		return partsCode;
	}

	public void setPartsCode(String partsCode) {
		this.partsCode = partsCode;
	}

	public String getPartsName() {
		return partsName;
	}

	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
