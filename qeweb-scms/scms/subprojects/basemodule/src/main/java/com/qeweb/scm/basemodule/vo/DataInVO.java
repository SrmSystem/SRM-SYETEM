package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start = 1,describe="物料导入")
public class DataInVO {
	
	@ExcelCol(column=Column.G)
	private String col1;
	
	@ExcelCol(column=Column.H)
	private String col2;
	
	@ExcelCol(column=Column.J)
	private String col3;
	
	@ExcelCol(column=Column.K)
	private String col4;

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
}
