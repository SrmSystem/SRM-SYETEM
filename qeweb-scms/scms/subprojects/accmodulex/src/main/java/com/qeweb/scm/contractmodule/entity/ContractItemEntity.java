package com.qeweb.scm.contractmodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;


@Entity
@Table(name = "qeweb_contract_item")
public class ContractItemEntity extends EPBaseEntity{
	
	private String attr_1; //年差价
	private String attr_2; //上一年度流量
	private String attr_3; //上一年度价格
	private String attr_4;//本年度结算价格
	private String attr_5;//实际降幅
	private String attr_6;//上一年度供货源
	private String attr_7;//订单号
	private String attr_8;//
	private String attr_9;//
	private String attr_10;//
	
	private Long contractId;
	
	/**
	 * 合同数量
	 */
	private Double itemQty;
	/**
	 * 合同单价
	 */
     private Double unitPrice;
 	/**
 	 * 单价数量
 	 */
     private Double unitPriceCount;
 	/**
 	 * 税率
 	 */
     private Double taxRate;
 	/**
 	 * 含税单价
 	 */
     private Double taxPrice;
 	/**
 	 * 加工费
 	 */
     private Double processCost;
 	/**
 	 * 运费
 	 */
     private Double transportCost;
 	/**
 	 * 原单明细ID  目前关联询价单明细
 	 */
     private long sourceItemId;

 	/**
 	 * 原单编号
 	 */
     private String sourceBillCode;
 	/**
 	 * 原单明细数量
 	 */
     private Double sourceItemCount;
 	/**
 	 * 原单明细价格
 	 */
     private Double sourceItemPrice;
 	/**
 	 * 原单类型
 	 */
     private Integer sourceBillType;
 	/**
 	 * 物料编码
 	 */
     private String materialCode;
 	/**
 	 * 物料规格
 	 */
     private String materialSpec;
 	/**
 	 * 物料名称
 	 */
     private String materialName;
     
     private Long materialIdFk;
     
     private MaterialEntity material;

 	/**
 	 * 单位编码
 	 */
     private String unitCode;
 	/**
 	 * 单位名称
 	 */
     private String unitName;
     
     private Long unitIdFk;
 	/**
 	 * 总金额
 	 */
     private Double totalPrice;
     
     /**
      * 合同类型
      */
     private int contractType;//0-合同，1-补充协议
 	/**
 	 * 备注
 	 */
     private String remarks;
	public Double getItemQty() {
		return itemQty;
	}
	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getUnitPriceCount() {
		return unitPriceCount;
	}
	public void setUnitPriceCount(Double unitPriceCount) {
		this.unitPriceCount = unitPriceCount;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(Double taxPrice) {
		this.taxPrice = taxPrice;
	}
	public Double getProcessCost() {
		return processCost;
	}
	public void setProcessCost(Double processCost) {
		this.processCost = processCost;
	}
	public Double getTransportCost() {
		return transportCost;
	}
	public void setTransportCost(Double transportCost) {
		this.transportCost = transportCost;
	}
	public long getSourceItemId() {
		return sourceItemId;
	}
	public void setSourceItemId(long sourceItemId) {
		this.sourceItemId = sourceItemId;
	}
	public String getSourceBillCode() {
		return sourceBillCode;
	}
	public void setSourceBillCode(String sourceBillCode) {
		this.sourceBillCode = sourceBillCode;
	}
	public Double getSourceItemCount() {
		return sourceItemCount;
	}
	public void setSourceItemCount(Double sourceItemCount) {
		this.sourceItemCount = sourceItemCount;
	}
	public Double getSourceItemPrice() {
		return sourceItemPrice;
	}
	public void setSourceItemPrice(Double sourceItemPrice) {
		this.sourceItemPrice = sourceItemPrice;
	}
	public Integer getSourceBillType() {
		return sourceBillType;
	}
	public void setSourceBillType(Integer sourceBillType) {
		this.sourceBillType = sourceBillType;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialSpec() {
		return materialSpec;
	}
	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@Column(name="material_id_fk")
	public Long getMaterialIdFk() {
		return materialIdFk;
	}
	public void setMaterialIdFk(Long materialIdFk) {
		this.materialIdFk = materialIdFk;
	}
	
	@ManyToOne
	@JoinColumn(name = "material_id_fk",insertable = false,updatable=false)
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Long getUnitIdFk() {
		return unitIdFk;
	}
	public void setUnitIdFk(Long unitIdFk) {
		this.unitIdFk = unitIdFk;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getContractType() {
		return contractType;
	}
	public void setContractType(int contractType) {
		this.contractType = contractType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public String getAttr_1() {
		return attr_1;
	}
	public void setAttr_1(String attr_1) {
		this.attr_1 = attr_1;
	}
	public String getAttr_2() {
		return attr_2;
	}
	public void setAttr_2(String attr_2) {
		this.attr_2 = attr_2;
	}
	public String getAttr_3() {
		return attr_3;
	}
	public void setAttr_3(String attr_3) {
		this.attr_3 = attr_3;
	}
	public String getAttr_4() {
		return attr_4;
	}
	public void setAttr_4(String attr_4) {
		this.attr_4 = attr_4;
	}
	public String getAttr_5() {
		return attr_5;
	}
	public void setAttr_5(String attr_5) {
		this.attr_5 = attr_5;
	}
	public String getAttr_6() {
		return attr_6;
	}
	public void setAttr_6(String attr_6) {
		this.attr_6 = attr_6;
	}
	public String getAttr_7() {
		return attr_7;
	}
	public void setAttr_7(String attr_7) {
		this.attr_7 = attr_7;
	}
	public String getAttr_8() {
		return attr_8;
	}
	public void setAttr_8(String attr_8) {
		this.attr_8 = attr_8;
	}
	public String getAttr_9() {
		return attr_9;
	}
	public void setAttr_9(String attr_9) {
		this.attr_9 = attr_9;
	}
	public String getAttr_10() {
		return attr_10;
	}
	public void setAttr_10(String attr_10) {
		this.attr_10 = attr_10;
	}
	
	
	
	
     
     
	
	

}
