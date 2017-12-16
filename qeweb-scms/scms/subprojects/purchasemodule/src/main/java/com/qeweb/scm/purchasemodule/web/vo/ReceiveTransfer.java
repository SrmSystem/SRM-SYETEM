package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "收货单") 
public class ReceiveTransfer {

	@ExcelCol(column = Column.B, required = true)
	private String receiveCode;
	
	@ExcelCol(column = Column.C)
	private String receiveUserCode;
	
	@ExcelCol(column = Column.D, required = true)
	private String materialCode;
	
	@ExcelCol(column = Column.E)
	private String materialName; 
	
	@ExcelCol(column = Column.F, required = true)
	private String deliveryCode;  
	
	@ExcelCol(type = ColType.NUMBER, column = Column.G, required = true)
	private String itemNo;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.H, required = true)
	private String receiveQty;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.I, required = true)
	private String inStoreQty;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.J, required = true)
	private String returnQty;
	
	@ExcelCol(type = ColType.DATE, column = Column.K, required = true)
	private String receiveTime;

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public String getReceiveUserCode() {
		return receiveUserCode;
	}

	public void setReceiveUserCode(String receiveUserCode) {
		this.receiveUserCode = receiveUserCode;
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

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(String receiveQty) {
		this.receiveQty = receiveQty;
	}

	public String getInStoreQty() {
		return inStoreQty;
	}

	public void setInStoreQty(String inStoreQty) {
		this.inStoreQty = inStoreQty;
	}

	public String getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(String returnQty) {
		this.returnQty = returnQty;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	
}
