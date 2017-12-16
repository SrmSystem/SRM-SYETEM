package com.qeweb.scm.check.vo;

import java.sql.Timestamp;


public class NoCheckedItemsVo {

	private Long id;
	private String recCode;
	private Timestamp createTime;
	private Double recQty;
	private Double returnQty;
	private Double inQty;
	private Long orderItemId;
	private String orderCode;
	private Integer itemNo; 
	private String vCode;
	private String vName;
	private String bCode;
	private String bName;
	private String mName;
	private String mCode;
	private String mUnit;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRecCode() {
		return recCode;
	}
	public void setRecCode(String recCode) {
		this.recCode = recCode;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Double getRecQty() {
		return recQty;
	}
	public void setRecQty(Double recQty) {
		this.recQty = recQty;
	}
	public Double getReturnQty() {
		return returnQty;
	}
	public void setReturnQty(Double returnQty) {
		this.returnQty = returnQty;
	}
	public Double getInQty() {
		return inQty;
	}
	public void setInQty(Double inQty) {
		this.inQty = inQty;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getvName() {
		return vName;
	}
	public void setvName(String vName) {
		this.vName = vName;
	}
	public String getbCode() {
		return bCode;
	}
	public void setbCode(String bCode) {
		this.bCode = bCode;
	}
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmCode() {
		return mCode;
	}
	public void setmCode(String mCode) {
		this.mCode = mCode;
	}
	public String getmUnit() {
		return mUnit;
	}
	public void setmUnit(String mUnit) {
		this.mUnit = mUnit;
	}
	
}
