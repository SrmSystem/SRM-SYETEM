package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;

/**
 * 物料分类的Excel导入值对象
 * @author pjjxiajun
 * @date 2015年7月15日
 * @path com.qeweb.scm.basemodule.vo.MaterialTypeImpVO.java
 */
@ExcelTransfer(start = 1, describe = "物料分类")
public class MaterialTypeImpVO {
	
	@ExcelCol(column=Column.A,required=true)
	private String code;
	
	@ExcelCol(column=Column.B,required=true)
	private String name;
	
	@ExcelCol(column=Column.C,required=false)
	private String parentCode;
	
	@ExcelCol(column=Column.D,required=false)
	private String remark;
	
	@ExcelCol(column=Column.E,required=false)
	private String col1;
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
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public void convertTarget(MaterialTypeEntity materialType){
		materialType.setCode(this.getCode());
		materialType.setName(this.getName());
	}
	

}
