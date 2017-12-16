package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

/**
 * 订单明细导出类
 * @author zhangjiejun
 */
@ExcelTransfer(start=1, describe = "采购订单明细") 
public class PurchaseOrderItemTransfer {

	@ExcelCol(column = Column.B, required = true, desc="PO")   
	private String orderCode;
	
	@ExcelCol(column = Column.C, required = true, desc="Release ID")
	private String version;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.D, desc="Line")
	private String itemNo;
	
	@ExcelCol(column = Column.E, required = true, desc="Item Number")
	private String materialCode;
	
	@ExcelCol(column = Column.F, desc="Discription")
	private String col5;
	
	@ExcelCol(column = Column.G, desc="UM")
	private String unitName;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.H, desc="QTY")
	private String orderQty;
	
	@ExcelCol(type = ColType.DATE, column = Column.I, desc="Require P/U Date")
	private String requestTime;
	
	@ExcelCol(column = Column.J, desc="Confirm Status")
	private String confirmStatus;
	
	@ExcelCol(column = Column.K, desc="Supplier Code")
	private String vendorCode;  
	
	@ExcelCol(column = Column.L, desc="Supplier")
	private String vendorName;
	
	@ExcelCol(column = Column.M, desc="Contact Person")
	private String contactPerson;
	
	@ExcelCol(column = Column.N, desc="Contact Number")
	private String contactNumber;
	
	@ExcelCol(column = Column.O, desc="PO Status")
	private String orderStatus;
	
	@ExcelCol(column = Column.P, desc="Country")
	private String col6;
	
	@ExcelCol(column = Column.Q, desc="City")
	private String col7;
	
	@ExcelCol(column = Column.R, desc="Site")
	private String col3;
	
	@ExcelCol(column = Column.S, desc="Buyer")
	private String col4;
	
	@ExcelCol(column = Column.T, desc="Publish Status")
	private String publishStatus;
	
	@ExcelCol(column = Column.U, desc="Check  Status")
	private String verifyStatus;

	@ExcelCol(column = Column.V, desc="P/u  Status")
	private String takeStatus;
	
	@ExcelCol(type = ColType.DATE, column = Column.W, desc="Acctually P/u date")
	private String tcol2;
	
	@ExcelCol(type = ColType.DATE, column = Column.X, desc="Actually ETA port date")
	private String tcol3;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.Y, desc="receiving Qty")
	private String receiveQty;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.Z, desc="Undelivery Qty")
	private String dcol1;
	
	@ExcelCol(column = Column.AA, desc="Close Status")
	private String closeStatus;
	
	@ExcelCol(column = Column.AB, desc="Invoice")
	private String col9;
	
	@ExcelCol(column = Column.AC, desc="Create Time")
	private String createTime;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getRequestTime() {
		if(requestTime.isEmpty()){
			requestTime = "";
		}
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}


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

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getOrderStatus() {
		String status = "";
		if(orderStatus.equals("0")){
			status = "P";
		}else if(orderStatus.equals("1")){
			status = "F";
		}else{
			status = "";
		}
		return status;
//		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getPublishStatus() {
		String status = "";
		if(publishStatus.equals("0")){
			status = "待发布";
		}else if(publishStatus.equals("1")){
			status = "已发布";
		}else{
			status = "";
		}
		return status;
//		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getVerifyStatus() {
		String status = "";
		if(verifyStatus.equals("0")){
			status = "待审核";
		}else if(verifyStatus.equals("1")){
			status = "审核通过";
		}else if(verifyStatus.equals("2")){
			status = "审核驳回";
		}else{
			status = "";
		}
		return status;
//		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getConfirmStatus() {
		String status = "";
		if(confirmStatus.equals("0")){
			status = "待确认";
		}else if(confirmStatus.equals("1")){
			status = "已确认";
		}else if(confirmStatus.equals("2")){
			status = "部分确认";
		}else if(confirmStatus.equals("-1")){
			status = "已驳回";
		}else{
			status = "";
		}
		return status;
//		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

//	public String getCol8() {
//		String status = "";
//		if(col8.equals("0")){
//			status = "待发货";
//		}else if(col8.equals("1")){
//			status = "已发货";
//		}else if(col8.equals("2")){
//			status = "部分发货";
//		}else{
//			status = "";
//		}
//		return status;
////		return col8;
//	}
//
//	public void setCol8(String col8) {
//		this.col8 = col8;
//	}

	public String getTcol2() {
		if(tcol2 == null || tcol2.equals("null")){
			tcol2 = "";
		}
		return tcol2;
	}

	public void setTcol2(String tcol2) {
		this.tcol2 = tcol2;
	}

	public String getTcol3() {
		if(tcol3 == null || tcol3.equals("null")){
			tcol3 = "";
		}
		return tcol3;
	}

	public void setTcol3(String tcol3) {
		this.tcol3 = tcol3;
	}

	public String getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(String receiveQty) {
		this.receiveQty = receiveQty;
	}

	public String getDcol1() {
		if(dcol1 == null || dcol1.equals("null")){
			dcol1 = "";
		}
		return dcol1;
	}

	public void setDcol1(String dcol1) {
		this.dcol1 = dcol1;
	}

	public String getCloseStatus() {
		String[] closeStatuss = closeStatus.split(",");
		int status = Integer.parseInt(closeStatuss[0]);
		int orderType = Integer.parseInt(closeStatuss[1]);
		if(status == 0 && orderType == 2)
			return "open";
		else if(status == 1 && orderType == 2)
			return "close";
		else if(status == 0)
			return "未关闭";
		else if(status == 1)
			return "关闭";
		else 
			return "";
//		return closeStatus;
	}

	public void setCloseStatus(String closeStatus) {
		this.closeStatus = closeStatus;
	}

	public String getCol9() {
		return col9;
	}

	public void setCol9(String col9) {
		this.col9 = col9;
	}

	public String getTakeStatus() {
		String status = "";
		if(takeStatus.equals("0")){
			status = "待提货";
		}else if(takeStatus.equals("1")){
			status = "已提货";
		}else if(takeStatus.equals("2")){
			status = "部分提货";
		}else{
			status = "";
		}
		return status;
//		return takeStatus;
	}

	public void setTakeStatus(String takeStatus) {
		this.takeStatus = takeStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
