package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

/**
 * 物料分类的Excel导入值对象（是否需要二级供应商）
 */
@ExcelTransfer(start = 1, describe = "物料分类是否二级供应商")
public class MaterialTypeNsvImpVO {
	
	@ExcelCol(column=Column.B,required=true)
	private String code;
	
	@ExcelCol(column=Column.C)
	private String name;
	
	@ExcelCol(column=Column.D,required=true)
	private String needSecondVendor;
	
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
	public String getNeedSecondVendor() {
		return needSecondVendor;
	}
	public void setNeedSecondVendor(String needSecondVendor) {
		this.needSecondVendor = needSecondVendor;
	}
	
}
