package com.qeweb.scm.purchasemodule.webservice.vo;

import java.sql.Timestamp;

public class ReceiveItemVo {

	private String receivecode; // 收货凭证号
	private String deliverycode; // 送货单号
	private String pocode; // 采购订单号
	private String linenumber; // 行号
	private String materialcode; // 物料编码
	private Double receiveqty; // 收货数量
	private Timestamp receivetime; // 收货时间
	private String receiver; // 收货人

	public String getReceivecode() {
		return receivecode;
	}

	public void setReceivecode(String receivecode) {
		this.receivecode = receivecode;
	}

	public String getDeliverycode() {
		return deliverycode;
	}

	public void setDeliverycode(String deliverycode) {
		this.deliverycode = deliverycode;
	}

	public String getPocode() {
		return pocode;
	}

	public void setPocode(String pocode) {
		this.pocode = pocode;
	}

	public String getLinenumber() {
		return linenumber;
	}

	public void setLinenumber(String linenumber) {
		this.linenumber = linenumber;
	}

	public String getMaterialcode() {
		return materialcode;
	}

	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}

	public Double getReceiveqty() {
		return receiveqty;
	}

	public void setReceiveqty(Double receiveqty) {
		this.receiveqty = receiveqty;
	}

	public Timestamp getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(Timestamp receivetime) {
		this.receivetime = receivetime;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
