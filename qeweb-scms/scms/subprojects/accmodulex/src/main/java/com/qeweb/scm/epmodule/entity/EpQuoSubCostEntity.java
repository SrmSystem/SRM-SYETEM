package com.qeweb.scm.epmodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;



@Entity
@Table(name = "QEWEB_EP_QUO_SUB_COST")
public class EpQuoSubCostEntity extends EPBaseEntity {
	
	private Long epQuoWholeId;
	private Long epQuoSubId;
	private String name;			//名称
	private Double qty; 			//数量
	private String unit;			//单位
	private Double price;			//单价 
	private Double totalPrice;			//单价 
	private Integer isVendor;					//用于区分采购商、供应商
	
	public Long getEpQuoSubId() {
		return epQuoSubId;
	}
	public void setEpQuoSubId(Long epQuoSubId) {
		this.epQuoSubId = epQuoSubId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getIsVendor() {
		return isVendor;
	}
	public void setIsVendor(Integer isVendor) {
		this.isVendor = isVendor;
	}
	public Long getEpQuoWholeId() {
		return epQuoWholeId;
	}
	public void setEpQuoWholeId(Long epQuoWholeId) {
		this.epQuoWholeId = epQuoWholeId;
	}
	
	
	
	
	
	
	


	



}
