package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "qeweb_delivery_dt")
public class DeliveryDTEntity extends BaseEntity {

	private DeliveryPCEntity pcEntity;
	private String asnCode;//条码号
	private Integer printStatus;//打印状态
	
	public String getAsnCode() {
		return asnCode;
	}
	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}
	@ManyToOne
	@JoinColumn(name="delivery_pc_id")
	public DeliveryPCEntity getPcEntity() {
		return pcEntity;
	}
	public void setPcEntity(DeliveryPCEntity pcEntity) {
		this.pcEntity = pcEntity;
	}
	public Integer getPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(Integer printStatus) {
		this.printStatus = printStatus;
	}
	
	
}
