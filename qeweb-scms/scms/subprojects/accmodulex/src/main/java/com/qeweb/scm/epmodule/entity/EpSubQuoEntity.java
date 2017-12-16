package com.qeweb.scm.epmodule.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;

/**
 * 分项报价实体类
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_SUB_QUO")
public class EpSubQuoEntity extends EPBaseEntity{
	
	private EpWholeQuoEntity wholeQuo;		//整项报价
	private Long wholeQuoId;
	private Long moduleItemId;

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
	private Integer isVendor;					//用于区分采购商、供应商
	private Double negotiatedSubPrice;	
	private Double negotiatedSubTotalPrice;	//协商无税价格
	
	private Set<EpSubQuoHisEntity> subQuoHises;
	
	//冗余
	private String tableCostDatas;

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
	
	

	@ManyToOne
	@JoinColumn(name = "WHOLE_QUOTATION_ID",insertable = false,updatable=false)
	public EpWholeQuoEntity getWholeQuo() {
		return wholeQuo;
	}

	public void setWholeQuo(EpWholeQuoEntity wholeQuo) {
		this.wholeQuo = wholeQuo;
	}
	
	@Column(name="WHOLE_QUOTATION_ID")
	public Long getWholeQuoId() {
		return wholeQuoId;
	}

	public void setWholeQuoId(Long wholeQuoId) {
		this.wholeQuoId = wholeQuoId;
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

	

	public Long getModuleItemId() {
		return moduleItemId;
	}

	public void setModuleItemId(Long moduleItemId) {
		this.moduleItemId = moduleItemId;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "subQuo")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<EpSubQuoHisEntity> getSubQuoHises() {
		return subQuoHises;
	}

	public void setSubQuoHises(Set<EpSubQuoHisEntity> subQuoHises) {
		this.subQuoHises = subQuoHises;
	}

	public Integer getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Integer isVendor) {
		this.isVendor = isVendor;
	}

	@Transient
	public String getTableCostDatas() {
		return tableCostDatas;
	}

	public void setTableCostDatas(String tableCostDatas) {
		this.tableCostDatas = tableCostDatas;
	}

	public Double getNegotiatedSubPrice() {
		return negotiatedSubPrice;
	}

	public void setNegotiatedSubPrice(Double negotiatedSubPrice) {
		this.negotiatedSubPrice = negotiatedSubPrice;
	}

	public Double getNegotiatedSubTotalPrice() {
		return negotiatedSubTotalPrice;
	}

	public void setNegotiatedSubTotalPrice(Double negotiatedSubTotalPrice) {
		this.negotiatedSubTotalPrice = negotiatedSubTotalPrice;
	}
	
	
	
	
	
	
	
	
	

}
