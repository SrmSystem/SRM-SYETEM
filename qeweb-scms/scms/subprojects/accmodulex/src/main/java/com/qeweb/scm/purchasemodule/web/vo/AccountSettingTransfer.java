package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "物料结算配置") 
public class AccountSettingTransfer {

	@ExcelCol(column = Column.B, required=true, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.C, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.D, required=true, desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column = Column.E, desc="物料名称")
	private String materialName;  
	
	@ExcelCol(column = Column.F, required=true, desc="结算类型")
	private String accountType;
	
	@ExcelCol(column = Column.G, desc="修改人")
	private String updateUserName;
	
	@ExcelCol(column = Column.H, desc="修改时间")
	private String updateTime;

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
