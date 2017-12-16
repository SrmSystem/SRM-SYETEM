package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
/**
 * 分类/等级 
 * @author lw
 * 创建时间：2015年6月29日10:22:19
 * 最后更新时间：2015年6月30日09:42:57
 * 最后更新人：lw
 */
@ExcelTransfer(start=1, describe = "分类/等级 ") 
public class VendorLevelTransfer {
	
	@ExcelCol(column = Column.A, required = true,desc="供应商编号")
	private String code;
	@ExcelCol(column = Column.B, required = true,desc="供应商等级")
	private String vendorLevel;
	@ExcelCol(column = Column.C, required = true,desc="供应商分类")
	private String vendorClassify;
	@ExcelCol(column = Column.D, required = true,desc="供应商分类（A,B,C）")
	private String vendorClassify2;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVendorLevel() {
		return vendorLevel;
	}
	public void setVendorLevel(String vendorLevel) {
		this.vendorLevel = vendorLevel;
	}
	public String getVendorClassify() {
		return vendorClassify;
	}
	public void setVendorClassify(String vendorClassify) {
		this.vendorClassify = vendorClassify;
	}
	public String getVendorClassify2() {
		return vendorClassify2;
	}
	public void setVendorClassify2(String vendorClassify2) {
		this.vendorClassify2 = vendorClassify2;
	}
	
}
