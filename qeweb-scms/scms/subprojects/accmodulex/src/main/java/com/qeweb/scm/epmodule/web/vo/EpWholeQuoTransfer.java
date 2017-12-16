package com.qeweb.scm.epmodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.utils.StringUtils;

@ExcelTransfer(start=1,describe = "报价单")
public class EpWholeQuoTransfer {

	@ExcelCol(column = Column.B ,desc ="报价当前次数")
	private String quoteCount;
	
	@ExcelCol(type = ColType.DATE,column = Column.C ,desc ="报价截止日期")
	private String quoteEndTime;
	
	@ExcelCol(column = Column.D ,desc ="报名状态")
	private String applicationStatus;
	
	@ExcelCol(column = Column.E ,desc ="报价状态")
	private String quoteStatus;
	
	@ExcelCol(column = Column.F ,desc ="含税单价")
	private String quotePrice;
	
	@ExcelCol(column = Column.G ,desc ="协商无税价格")
	private String negotiatedPrice;
	
	@ExcelCol(column = Column.H ,desc ="采方议价状态")
	private String negotiatedStatus;
	
	@ExcelCol(type = ColType.DATE,column = Column.I ,desc ="采方议价时间")
	private String negotiatedTime;
	
	@ExcelCol(column = Column.J ,desc ="供方确认议价状态")
	private String negotiatedCheckStatus;
	
	@ExcelCol(type = ColType.DATE,column = Column.K ,desc ="供方确认议价时间")
	private String negotiatedCheckTime;
	
	@ExcelCol(type = ColType.DATE,column = Column.L ,desc ="报价单关闭状态")
	private String closeStatus;
	
	@ExcelCol(column = Column.M ,desc ="供货周期")
	private String supplyCycle;
	
	@ExcelCol(column = Column.N ,desc ="品牌")
	private String brand;
	
	@ExcelCol(column = Column.O ,desc ="生产厂家")
	private String manufacturer;
	
	@ExcelCol(column = Column.P ,desc ="材质构成")
	private String materialComposition;
	
	@ExcelCol(column = Column.Q ,desc ="保质期")
	private String warrantyPeriod;
	
	@ExcelCol(column = Column.R ,desc ="运输方式")
	private String transportationMode;
	
	@ExcelCol(column = Column.S ,desc ="支付方式")
	private String paymentMeans;
	
	@ExcelCol(column = Column.T ,desc ="税种")
	private String taxCategory;
	
	@ExcelCol(column = Column.U ,desc ="税率")
	private String taxRate;
	
	@ExcelCol(column = Column.V ,desc ="技术承诺")
	private String technologyPromises;
	
	@ExcelCol(column = Column.W ,desc ="质量承诺")
	private String qualityPromises;
	
	@ExcelCol(column = Column.X ,desc ="服务承诺")
	private String servicePromises;
	
	@ExcelCol(column = Column.Y ,desc ="交期承诺")
	private String deliveryPromises;
	
	@ExcelCol(column = Column.Z ,desc ="其他承诺")
	private String otherPromises;
	
	@ExcelCol(column = Column.AA ,desc ="合作数量")
	private String cooperationQty;
	
	@ExcelCol(column = Column.AB ,desc ="合作状态")
	private String cooperationStatus;
	
	@ExcelCol(column = Column.AC ,desc ="询价单号")
	private String enquirePriceCode;
	
	@ExcelCol(column = Column.AD ,desc ="询价单供应商编号")
	private String vendorCode;
	
	@ExcelCol(column = Column.AE ,desc ="询价单供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.AF ,desc ="询价单物料编号")
	private String materialCode;
	
	@ExcelCol(column = Column.AG ,desc ="询价单物料名称")
	private String materialName;
	
	@ExcelCol(column = Column.AH ,desc ="重新报价状态")
	private String requoteStatus;

	public String getQuoteCount() {
		return quoteCount;
	}

	public void setQuoteCount(String quoteCount) {
		this.quoteCount = quoteCount;
	}

	public String getQuoteEndTime() {
		return quoteEndTime;
	}

	public void setQuoteEndTime(String quoteEndTime) {
		this.quoteEndTime = quoteEndTime;
	}

	public String getApplicationStatus() {
		String appStr = "未报名";
		if(("1").equals(applicationStatus)){
			appStr = "已报名";
		}
		return appStr;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getQuoteStatus() {
		String quoStr = "未报价";
		if(("1").equals(quoteStatus)){
			quoStr = "已报价";
		}
		return quoStr;
	}

	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public String getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(String quotePrice) {
		this.quotePrice = quotePrice;
	}

	public String getNegotiatedPrice() {
		return negotiatedPrice;
	}

	public void setNegotiatedPrice(String negotiatedPrice) {
		this.negotiatedPrice = negotiatedPrice;
	}

	public String getNegotiatedStatus() {
		String negoStr = "未议价";
		if(("1").equals(negotiatedStatus)){
			negoStr = "已议价";
		}
		return negoStr;
	}

	public void setNegotiatedStatus(String negotiatedStatus) {
		this.negotiatedStatus = negotiatedStatus;
	}

	public String getNegotiatedTime() {
		return negotiatedTime;
	}

	public void setNegotiatedTime(String negotiatedTime) {
		this.negotiatedTime = negotiatedTime;
	}

	public String getNegotiatedCheckStatus() {
		String checkStr = "未确认";
		if(("1").equals(negotiatedCheckStatus)){
			checkStr = "接受议价";
		}else if(("-1").equals(negotiatedCheckStatus)){
			checkStr = "拒接议价";
		}
		return checkStr;
	}

	public void setNegotiatedCheckStatus(String negotiatedCheckStatus) {
		this.negotiatedCheckStatus = negotiatedCheckStatus;
	}

	public String getNegotiatedCheckTime() {
		return negotiatedCheckTime;
	}

	public void setNegotiatedCheckTime(String negotiatedCheckTime) {
		this.negotiatedCheckTime = negotiatedCheckTime;
	}

	public String getCloseStatus() {
		String closeStr = "未关闭";
		if(("1").equals(closeStatus)){
			closeStr = "已关闭";
		}
		return closeStr;
	}

	public void setCloseStatus(String closeStatus) {
		this.closeStatus = closeStatus;
	}

	public String getSupplyCycle() {
		return supplyCycle;
	}

	public void setSupplyCycle(String supplyCycle) {
		this.supplyCycle = supplyCycle;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getMaterialComposition() {
		return materialComposition;
	}

	public void setMaterialComposition(String materialComposition) {
		this.materialComposition = materialComposition;
	}

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getTransportationMode() {
		return transportationMode;
	}

	public void setTransportationMode(String transportationMode) {
		this.transportationMode = transportationMode;
	}

	public String getPaymentMeans() {
		return paymentMeans;
	}

	public void setPaymentMeans(String paymentMeans) {
		this.paymentMeans = paymentMeans;
	}

	public String getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(String taxCategory) {
		this.taxCategory = taxCategory;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTechnologyPromises() {
		return technologyPromises;
	}

	public void setTechnologyPromises(String technologyPromises) {
		this.technologyPromises = technologyPromises;
	}

	public String getQualityPromises() {
		return qualityPromises;
	}

	public void setQualityPromises(String qualityPromises) {
		this.qualityPromises = qualityPromises;
	}

	public String getServicePromises() {
		return servicePromises;
	}

	public void setServicePromises(String servicePromises) {
		this.servicePromises = servicePromises;
	}

	public String getDeliveryPromises() {
		return deliveryPromises;
	}

	public void setDeliveryPromises(String deliveryPromises) {
		this.deliveryPromises = deliveryPromises;
	}

	public String getOtherPromises() {
		return otherPromises;
	}

	public void setOtherPromises(String otherPromises) {
		this.otherPromises = otherPromises;
	}

	public String getCooperationQty() {
		return cooperationQty;
	}

	public void setCooperationQty(String cooperationQty) {
		this.cooperationQty = cooperationQty;
	}

	public String getCooperationStatus() {
		String cooperStr = "未合作";
		if(("1").equals(cooperationStatus)){
			cooperStr = "已合作";
		}
		return cooperStr;
	}

	public void setCooperationStatus(String cooperationStatus) {
		this.cooperationStatus = cooperationStatus;
	}

	public String getEnquirePriceCode() {
		return enquirePriceCode;
	}

	public void setEnquirePriceCode(String enquirePriceCode) {
		this.enquirePriceCode = enquirePriceCode;
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

	public String getRequoteStatus() {
		String requoteStr = "未重新报价";
		if(("1").equals(requoteStatus)){
			requoteStr = "重新报价";
		}
		return requoteStr;
	}

	public void setRequoteStatus(String requoteStatus) {
		this.requoteStatus = requoteStatus;
	}
}
