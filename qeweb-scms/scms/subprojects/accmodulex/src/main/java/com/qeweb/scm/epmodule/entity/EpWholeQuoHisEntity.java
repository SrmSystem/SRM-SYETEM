package com.qeweb.scm.epmodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;

/**
 * 整项报价历史
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_WHOLE_QUO_HIS")
public class EpWholeQuoHisEntity extends EPBaseEntity{
	
	private EpWholeQuoEntity wholeQuo;			//整项报价
	
	private Integer quoteCount;					//报价当前次数
	private Timestamp quoteEndTime;				//报价截止日期
	private Integer applicationStatus;			//报名状态
	private Integer quoteStatus;				//报价状态：0=未报价；1=已报价
	private Double quotePrice;		 			//含税单价
	private Double totalQuotePrice;				//总报价（无税单价）
	private Double negotiatedPrice;				//协商无税价格
	private Double totalNegotiatedPrice;		//总协商含税价格
	private Integer negotiatedStatus;			//采方议价状态
	private Timestamp negotiatedTime;			//采方议价时间
	private Integer negotiatedCheckStatus;		//供方确认议价状态
	private Timestamp negotiatedCheckTime;		//供方确认议价时间
	private Integer closeStatus;			    //报价单关闭状态
	private Timestamp closeTime;		        //报价单关闭时间
	
	private String supplyCycle;					//供货周期
	private String brand;						//品牌
	private String manufacturer;				//生产厂家
	private String materialComposition;			//材质构成
	private String warrantyPeriod;				//保质期
	private String transportationMode;			//运输方式
	private String bearFreightChargesBy;		//由哪方承担运费
	private String paymentMeans;				//支付方式
	private String quoteTemplateUrl;			//报价附件说明
	private String quoteTemplateName;			//报价附件名称
	private String taxCategory;					//税种
	private Double taxRate;						//税率
	private String technologyPromises;			//技术承诺
	private String qualityPromises;				//质量承诺
	private String servicePromises;				//服务承诺
	private String deliveryPromises;			//交期承诺
	private String otherPromises;				//其他承诺
	private Double cooperationQty;				//合作数量
	private Integer cooperationStatus;			//合作状态
	private String remarks;						//备注

	private EpPriceEntity epPrice; 				//询价单
	private EpVendorEntity epVendor;			//询价单供应商
	private EpMaterialEntity epMaterial;		//询价单物料
	private Integer requoteStatus;				//重新报价状态:0=未重新报价；1=重新报价
	private Integer deliveryDays;				//交货期（天）
	private Long negotiatedUserId;			    //采方议价人
	private Long negotiatedCheckUserId;			//供方确认议价人
	private Double mpq;							//最小包装量
	private Double moq;							//最小订货量
	private Integer publishStatus;				//发布状态
	private Timestamp publishTime;				//发布时间
	private Long publishUserId;					//发布人id
	private Integer eipApprovalStatus;			//审核状态
	private Timestamp eipApprovalTime;			//审核时间
	private Long eipApprovalUserId;				//审核人id
	private Long closeUserId;						//报价单关闭人
	
	@ManyToOne
	@JoinColumn(name="WHOLE_QUOTATION_ID")	
	public EpWholeQuoEntity getWholeQuo() {
		return wholeQuo;
	}

	public void setWholeQuo(EpWholeQuoEntity wholeQuo) {
		this.wholeQuo = wholeQuo;
	}

	public Integer getQuoteCount() {
		return quoteCount;
	}

	public void setQuoteCount(Integer quoteCount) {
		this.quoteCount = quoteCount;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getQuoteEndTime() {
		return quoteEndTime;
	}

	public void setQuoteEndTime(Timestamp quoteEndTime) {
		this.quoteEndTime = quoteEndTime;
	}

	public Integer getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(Integer applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public Integer getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(Integer quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public Double getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(Double quotePrice) {
		this.quotePrice = quotePrice;
	}

	public Double getTotalQuotePrice() {
		return totalQuotePrice;
	}

	public void setTotalQuotePrice(Double totalQuotePrice) {
		this.totalQuotePrice = totalQuotePrice;
	}

	public Double getNegotiatedPrice() {
		return negotiatedPrice;
	}

	public void setNegotiatedPrice(Double negotiatedPrice) {
		this.negotiatedPrice = negotiatedPrice;
	}

	public Double getTotalNegotiatedPrice() {
		return totalNegotiatedPrice;
	}

	public void setTotalNegotiatedPrice(Double totalNegotiatedPrice) {
		this.totalNegotiatedPrice = totalNegotiatedPrice;
	}

	public Integer getNegotiatedStatus() {
		return negotiatedStatus;
	}

	public void setNegotiatedStatus(Integer negotiatedStatus) {
		this.negotiatedStatus = negotiatedStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getNegotiatedTime() {
		return negotiatedTime;
	}

	public void setNegotiatedTime(Timestamp negotiatedTime) {
		this.negotiatedTime = negotiatedTime;
	}

	public Integer getNegotiatedCheckStatus() {
		return negotiatedCheckStatus;
	}

	public void setNegotiatedCheckStatus(Integer negotiatedCheckStatus) {
		this.negotiatedCheckStatus = negotiatedCheckStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getNegotiatedCheckTime() {
		return negotiatedCheckTime;
	}

	public void setNegotiatedCheckTime(Timestamp negotiatedCheckTime) {
		this.negotiatedCheckTime = negotiatedCheckTime;
	}

	public Integer getCloseStatus() {
		return closeStatus;
	}

	public void setCloseStatus(Integer closeStatus) {
		this.closeStatus = closeStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
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

	public String getBearFreightChargesBy() {
		return bearFreightChargesBy;
	}

	public void setBearFreightChargesBy(String bearFreightChargesBy) {
		this.bearFreightChargesBy = bearFreightChargesBy;
	}

	public String getPaymentMeans() {
		return paymentMeans;
	}

	public void setPaymentMeans(String paymentMeans) {
		this.paymentMeans = paymentMeans;
	}

	public String getQuoteTemplateUrl() {
		return quoteTemplateUrl;
	}

	public void setQuoteTemplateUrl(String quoteTemplateUrl) {
		this.quoteTemplateUrl = quoteTemplateUrl;
	}

	public String getQuoteTemplateName() {
		return quoteTemplateName;
	}

	public void setQuoteTemplateName(String quoteTemplateName) {
		this.quoteTemplateName = quoteTemplateName;
	}

	public String getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(String taxCategory) {
		this.taxCategory = taxCategory;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
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

	public Double getCooperationQty() {
		return cooperationQty;
	}

	public void setCooperationQty(Double cooperationQty) {
		this.cooperationQty = cooperationQty;
	}

	public Integer getCooperationStatus() {
		return cooperationStatus;
	}

	public void setCooperationStatus(Integer cooperationStatus) {
		this.cooperationStatus = cooperationStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ManyToOne
	@JoinColumn(name="ENQUIRE_PRICE_ID")
	public EpPriceEntity getEpPrice() {
		return epPrice;
	}

	public void setEpPrice(EpPriceEntity epPrice) {
		this.epPrice = epPrice;
	}

	@ManyToOne
	@JoinColumn(name="ENQUIRE_PRICE_VENDOR_ID")
	public EpVendorEntity getEpVendor() {
		return epVendor;
	}

	public void setEpVendor(EpVendorEntity epVendor) {
		this.epVendor = epVendor;
	}

	@ManyToOne
	@JoinColumn(name="ENQUIRE_PRICE_MATERIAL_ID")
	public EpMaterialEntity getEpMaterial() {
		return epMaterial;
	}

	public void setEpMaterial(EpMaterialEntity epMaterial) {
		this.epMaterial = epMaterial;
	}

	public Integer getRequoteStatus() {
		return requoteStatus;
	}

	public void setRequoteStatus(Integer requoteStatus) {
		this.requoteStatus = requoteStatus;
	}

	public Integer getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(Integer deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public Long getNegotiatedUserId() {
		return negotiatedUserId;
	}

	public void setNegotiatedUserId(Long negotiatedUserId) {
		this.negotiatedUserId = negotiatedUserId;
	}

	public Long getNegotiatedCheckUserId() {
		return negotiatedCheckUserId;
	}

	public void setNegotiatedCheckUserId(Long negotiatedCheckUserId) {
		this.negotiatedCheckUserId = negotiatedCheckUserId;
	}

	public Double getMpq() {
		return mpq;
	}

	public void setMpq(Double mpq) {
		this.mpq = mpq;
	}

	public Double getMoq() {
		return moq;
	}

	public void setMoq(Double moq) {
		this.moq = moq;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public Long getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(Long publishUserId) {
		this.publishUserId = publishUserId;
	}

	public Integer getEipApprovalStatus() {
		return eipApprovalStatus;
	}

	public void setEipApprovalStatus(Integer eipApprovalStatus) {
		this.eipApprovalStatus = eipApprovalStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getEipApprovalTime() {
		return eipApprovalTime;
	}

	public void setEipApprovalTime(Timestamp eipApprovalTime) {
		this.eipApprovalTime = eipApprovalTime;
	}

	public Long getEipApprovalUserId() {
		return eipApprovalUserId;
	}

	public void setEipApprovalUserId(Long eipApprovalUserId) {
		this.eipApprovalUserId = eipApprovalUserId;
	}

	public Long getCloseUserId() {
		return closeUserId;
	}

	public void setCloseUserId(Long closeUserId) {
		this.closeUserId = closeUserId;
	}

	

}
