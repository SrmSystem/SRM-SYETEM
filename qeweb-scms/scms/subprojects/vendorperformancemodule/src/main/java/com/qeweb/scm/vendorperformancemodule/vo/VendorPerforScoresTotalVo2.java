package com.qeweb.scm.vendorperformancemodule.vo;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.utils.DateUtil;

/***
 * 麦特达因
 * @author Administrator
 *
 */
@ExcelTransfer(start=1, describe = "评估结果") 
public class VendorPerforScoresTotalVo2{
	
	@ExcelCol(column = Column.A, required = true,desc="周期")
	private String col1;
	@ExcelCol(column = Column.B, required = true,desc="评估时间")
	private String col2;
	@ExcelCol(column = Column.C, required = true,desc="供应商代码")
	private String col3;
	@ExcelCol(column = Column.D, required = true,desc="供应商名称")
	private String col4;
	@ExcelCol(column = Column.E, required = true,desc="等级")
	private String col5;
	@ExcelCol(column = Column.F, required = true,desc="总得分")
	private String col6;
	@ExcelCol(column = Column.G, required = true,desc="排名")
	private String col7;
	@ExcelCol(column = Column.H, required = true,desc="价格")
	private String col8;
	
	//modify by zhangjiejun 2016.04.29 start
//	@ExcelCol(column = Column.I, required = true,desc="服务")
//	private String col9;
//	@ExcelCol(column = Column.J, required = true,desc="交付")
//	private String col10;
//	@ExcelCol(column = Column.K, required = true,desc="质量")
//	private String col11;
	
	@ExcelCol(column = Column.I, desc="8D")
	private String use8D;
	@ExcelCol(column = Column.J, desc="RFQ")
	private String useRFQ;
	@ExcelCol(column = Column.K, required = true,desc="交付")
	private String col10;
	@ExcelCol(column = Column.L, required = true,desc="质量")
	private String col11;
	//modify by zhangjiejun 2016.04.29 end
	
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	public String getCol4() {
		return col4;
	}
	public void setCol4(String col4) {
		this.col4 = col4;
	}
	public String getCol5() {
		return col5;
	}
	public void setCol5(String col5) {
		this.col5 = col5;
	}
	public String getCol6() {
		return col6;
	}
	public void setCol6(String col6) {
		this.col6 = col6;
	}
	public String getCol7() {
		return col7;
	}
	public void setCol7(String col7) {
		this.col7 = col7;
	}
	public String getCol8() {
		return col8;
	}
	public void setCol8(String col8) {
		this.col8 = col8;
	}
//	public String getCol9() {
//		return col9;
//	}
//	public void setCol9(String col9) {
//		this.col9 = col9;
//	}
	public String getCol10() {
		return col10;
	}
	public void setCol10(String col10) {
		this.col10 = col10;
	}
	public String getCol11() {
		return col11;
	}
	public void setCol11(String col11) {
		this.col11 = col11;
	}
	public String getUse8D() {
		return use8D;
	}
	public void setUse8D(String use8d) {
		use8D = use8d;
	}
	public String getUseRFQ() {
		return useRFQ;
	}
	public void setUseRFQ(String useRFQ) {
		this.useRFQ = useRFQ;
	}
	
}
