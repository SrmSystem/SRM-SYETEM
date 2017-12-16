package com.qeweb.scm.epmodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;


/**
 * 分项报价历史实体类
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_SUB_QUO_HIS")
public class EpSubQuoHisEntity extends EPBaseEntity{
	
	private Long wholeQuoId;
	private EpSubQuoEntity subQuo;			//分项报价
	
	private Double managementFeeRate;		//管理费率%
	private Double profitRate;				//利率%
	private Double profit;					//利润
	private Double taxRate;					//税率%
	private Double taxFee;					//税费
	private Double carriageCharges;			//运输费
	private Double subtotal;                //分项报价小计(单价)
	private String remarks;					//备注
	
	private String materialName;			//部件名称
	private String materialSpec;			//型号规格
	private Double qty; 					//数量
	private String materialUnit;			//单位
	private Double totalQuotePrice;			//含税单价
	private Double quotePrice;				//无税单价
	private String brand;					//品牌/生产厂家

	@ManyToOne
	@JoinColumn(name="SUBITEM_QUOTATION_ID")
	public EpSubQuoEntity getSubQuo() {
		return subQuo;
	}

	public void setSubQuo(EpSubQuoEntity subQuo) {
		this.subQuo = subQuo;
	}

	public Double getManagementFeeRate() {
		return managementFeeRate;
	}

	public void setManagementFeeRate(Double managementFeeRate) {
		this.managementFeeRate = managementFeeRate;
	}

	public Double getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(Double profitRate) {
		this.profitRate = profitRate;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}

	public Double getCarriageCharges() {
		return carriageCharges;
	}

	public void setCarriageCharges(Double carriageCharges) {
		this.carriageCharges = carriageCharges;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialSpec() {
		return materialSpec;
	}

	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getMaterialUnit() {
		return materialUnit;
	}

	public void setMaterialUnit(String materialUnit) {
		this.materialUnit = materialUnit;
	}

	public Double getTotalQuotePrice() {
		return totalQuotePrice;
	}

	public void setTotalQuotePrice(Double totalQuotePrice) {
		this.totalQuotePrice = totalQuotePrice;
	}

	public Double getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(Double quotePrice) {
		this.quotePrice = quotePrice;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Long getWholeQuoId() {
		return wholeQuoId;
	}

	public void setWholeQuoId(Long wholeQuoId) {
		this.wholeQuoId = wholeQuoId;
	}
	
	
	
	

}
