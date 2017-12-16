package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
/**
 * 近三年质量体系审核和评优结果
 * @author lw
 * 创建时间：2015年6月29日10:22:19
 * 最后更新时间：2015年6月30日09:42:57
 * 最后更新人：lw
 */
@ExcelTransfer(start=1, describe = "近三年质量体系审核和评优结果") 
public class QSATransfer {
	
	@ExcelCol(column = Column.A, required = true,desc="供应商编号")
	private String code;
	@ExcelCol(column = Column.B, required = true,desc="近三年质量体系审核")
	private String qsa;
	@ExcelCol(column = Column.C, required = true,desc="近三年评优结果")
	private String qsaResult;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getQsa() {
		return qsa;
	}
	public void setQsa(String qsa) {
		this.qsa = qsa;
	}
	public String getQsaResult() {
		return qsaResult;
	}
	public void setQsaResult(String qsaResult) {
		this.qsaResult = qsaResult;
	}
}
