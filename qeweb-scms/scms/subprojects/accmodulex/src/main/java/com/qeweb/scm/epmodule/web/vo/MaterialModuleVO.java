package com.qeweb.scm.epmodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start = 1,describe="物料报价模型组关系导入")
public class MaterialModuleVO {
	
	@ExcelCol(column=Column.B,desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column=Column.C,desc="物料名称")
	private String materialName;
	
	@ExcelCol(column=Column.D,desc="报价模型编码")
	private String moduleCode;
	
	@ExcelCol(column=Column.E,desc="报价模型名称")
	private String moduleName;
	
	@ExcelCol(column=Column.F,desc="工厂")
	private String werk;

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getWerk() {
		return werk;
	}

	public void setWerk(String werk) {
		this.werk = werk;
	}
	
	
	


	
	

}
