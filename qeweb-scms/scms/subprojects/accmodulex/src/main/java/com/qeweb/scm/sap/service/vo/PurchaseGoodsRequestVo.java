package com.qeweb.scm.sap.service.vo;

/**
 *修改要货计划的VO
 */
public class PurchaseGoodsRequestVo {
	
	private String  dshl;	//排产数量
	private Double surQty;//剩余数量
	public String getDshl() {
		return dshl;
	}
	public void setDshl(String dshl) {
		this.dshl = dshl;
	}
	public Double getSurQty() {
		return surQty;
	}
	public void setSurQty(Double surQty) {
		this.surQty = surQty;
	}
	
	
}
