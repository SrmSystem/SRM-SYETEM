package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=0, describe = "采购计划") 
public class PurchasePlanTransfer {

	@ExcelCol(column = Column.B,  required = true, desc="工厂编码")   
	private String factoryCode;
	
	
	@ExcelCol(column = Column.C, required = true, desc="采购组编码")
	private String groupCode;
	
	
	@ExcelCol(column = Column.D, required = true, desc="供应商编码")
	private String vendorCode;
	
	
	@ExcelCol(column = Column.E, required = true, desc="物料号")
	private String materialCode;

	@ExcelCol( column = Column.F, desc="当日库存")
	private String inventory;
	
	@ExcelCol( column = Column.G, desc="当日未交po数量")
	private String deliveredCount ;
	
	
	@ExcelCol( column = Column.H, desc="col1")
	private String col1;
	
	@ExcelCol( column = Column.I, desc="col2")
	private String col2;
	
	@ExcelCol( column = Column.J, desc="col3")
	private String col3;
	
	@ExcelCol( column = Column.K, desc="col4")
	private String col4;
	
	@ExcelCol( column = Column.L, desc="col5")
	private String col5;
	
	@ExcelCol( column = Column.M, desc="col6")
	private String col6;
	
	@ExcelCol( column = Column.N, desc="col7")
	private String col7;
	
	@ExcelCol( column = Column.O, desc="col8")
	private String col8;
	
	@ExcelCol( column = Column.P, desc="col9")
	private String col9;
	
	@ExcelCol( column = Column.Q, desc="col10")
	private String col10;
	
	@ExcelCol( column = Column.R, desc="col11")
	private String col11;
	
	@ExcelCol( column = Column.S, desc="col12")
	private String col12;
	
	@ExcelCol( column = Column.T, desc="col13")
	private String col13;
	
	@ExcelCol( column = Column.U, desc="col14")
	private String col14;
	
	@ExcelCol( column = Column.V, desc="col15")
	private String col15;
	
	@ExcelCol( column = Column.W, desc="col16")
	private String col16;
	
	@ExcelCol( column = Column.X, desc="col17")
	private String col17;
	
	@ExcelCol( column = Column.Y, desc="col18")
	private String col18;
	
	@ExcelCol( column = Column.Z, desc="col19")
	private String col19;
	
	@ExcelCol( column = Column.AA, desc="col20")
	private String col20;
	
	@ExcelCol( column = Column.AB, desc="col21")
	private String col21;
	
	@ExcelCol( column = Column.AC, desc="col22")
	private String col22;
	
	@ExcelCol( column = Column.AD, desc="col23")
	private String col23;
	
	@ExcelCol( column = Column.AE, desc="col24")
	private String col24;




	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}



	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}


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

	
	public String getCol11() {
		return col11;
	}

	public void setCol11(String col11) {
		this.col11 = col11;
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

	public String getCol9() {
		return col9;
	}

	public void setCol9(String col9) {
		this.col9 = col9;
	}

	public String getCol10() {
		return col10;
	}

	public void setCol10(String col10) {
		this.col10 = col10;
	}

	public String getCol12() {
		return col12;
	}

	public void setCol12(String col12) {
		this.col12 = col12;
	}

	public String getCol13() {
		return col13;
	}

	public void setCol13(String col13) {
		this.col13 = col13;
	}

	public String getCol14() {
		return col14;
	}

	public void setCol14(String col14) {
		this.col14 = col14;
	}

	public String getCol15() {
		return col15;
	}

	public void setCol15(String col15) {
		this.col15 = col15;
	}

	public String getCol16() {
		return col16;
	}

	public void setCol16(String col16) {
		this.col16 = col16;
	}

	public String getCol17() {
		return col17;
	}

	public void setCol17(String col17) {
		this.col17 = col17;
	}

	public String getCol18() {
		return col18;
	}

	public void setCol18(String col18) {
		this.col18 = col18;
	}

	public String getCol19() {
		return col19;
	}

	public void setCol19(String col19) {
		this.col19 = col19;
	}

	public String getCol20() {
		return col20;
	}

	public void setCol20(String col20) {
		this.col20 = col20;
	}

	public String getCol21() {
		return col21;
	}

	public void setCol21(String col21) {
		this.col21 = col21;
	}

	public String getCol22() {
		return col22;
	}

	public void setCol22(String col22) {
		this.col22 = col22;
	}

	public String getCol23() {
		return col23;
	}

	public void setCol23(String col23) {
		this.col23 = col23;
	}

	public String getCol24() {
		return col24;
	}

	public void setCol24(String col24) {
		this.col24 = col24;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getDeliveredCount() {
		return deliveredCount;
	}

	public void setDeliveredCount(String deliveredCount) {
		this.deliveredCount = deliveredCount;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}


	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	
	

}
