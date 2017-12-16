package com.qeweb.scm.epmodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 询价单实体类
 * @author ronnie
 *
 */
@Entity
@Table(name = "qeweb_ep_price")
public class EpPriceEntity extends EPBaseEntity {
	private String enquirePriceCode; // 询价单号
	private Long orderId; // 采购单ID
	private String projectName; // 该询价单所属项目
	private Timestamp applicationDeadline; // 报名截止日期
	private Timestamp quoteEndTime; // 报价截止时间
	private Integer materialType; // 物料类型
	private Integer quoteWay; // 报价方式：0 = 分项报价；1= 整体报价
	private Integer joinWay; // 报价参与方式:0=邀请；1=公开
	private Integer resultOpenWay; // 报价结果公开方式
	private Integer auditStatus; // 审核状态：0=未提交审核；1=审核通过；-1=审核驳回；2=审核进行中
	private Timestamp auditTime; // 审核时间
	private Long auditUserId; // 审核人
	private String auditComments; // 审批意见

	private Integer publishStatus; // 发布状态:0=未发布；1=已发布
	private Timestamp publishTime; // 发布时间
	private Long publishUserId; // 发布人
	private Integer applicationStatus; // 报名状态
	private Integer quoteStatus; // 报价状态
	private Integer negotiationStatus; // 议价状态：0=未协商；1=协商完成（Robam使用扩展字段delete）
	private Integer closeStatus; // 关闭状态
	private Timestamp closeTime; // 关闭时间
	private Long closeUserId; // 关闭人

	private String quoteSpecificationUrl; // 报价说明书
	private String quoteTemplateUrl; // 报价模板
	private String remarks; // 备注(询价公告)
	private String quoteSpecFileName; // 报价说明书文件名称
	private String quoteTemplateFileName; // 报价模板文件名称
	
	private Integer isTop; // 报价级别 :1=一级；2=二级
	private Integer isVendor; // 分类定义方:0=采购商；1=供应商
	
	private Integer quoteType;	//报价类型:0=新产品报价;1=商改产品报价
	
	private UserEntity signPerson1;	//会签人1
	private UserEntity signPerson2;	//会签人2
	private UserEntity signPerson3;	//会签人3
	private UserEntity signPerson4;	//会签人4
	private UserEntity signPerson5;	//会签人5
	
	private Long signPerson1Id;
	private Long signPerson2Id;
	private Long signPerson3Id;	
	private Long signPerson4Id;	
	private Long signPerson5Id;
	
	private Timestamp priceStartTime;	//价格有效开始时间
	private Timestamp priceEndTime;		//价格有效结束时间
	private String factory;		//工厂
	
	private Integer checkDep; // 审批部门  1 技术审批、0工艺审批
	
	//add by yao.jin
	private Integer isDim;	// 是否按供应商维度进行询价    0=否 、1=是
	//end add

	public String getEnquirePriceCode() {
		return enquirePriceCode;
	}

	public void setEnquirePriceCode(String enquirePriceCode) {
		this.enquirePriceCode = enquirePriceCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getApplicationDeadline() {
		return applicationDeadline;
	}

	public void setApplicationDeadline(Timestamp applicationDeadline) {
		this.applicationDeadline = applicationDeadline;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getQuoteEndTime() {
		return quoteEndTime;
	}

	public void setQuoteEndTime(Timestamp quoteEndTime) {
		this.quoteEndTime = quoteEndTime;
	}

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public Integer getQuoteWay() {
		return quoteWay;
	}

	public void setQuoteWay(Integer quoteWay) {
		this.quoteWay = quoteWay;
	}

	public Integer getJoinWay() {
		return joinWay;
	}

	public void setJoinWay(Integer joinWay) {
		this.joinWay = joinWay;
	}

	public Integer getResultOpenWay() {
		return resultOpenWay;
	}

	public void setResultOpenWay(Integer resultOpenWay) {
		this.resultOpenWay = resultOpenWay;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditComments() {
		return auditComments;
	}

	public void setAuditComments(String auditComments) {
		this.auditComments = auditComments;
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

	public Integer getNegotiationStatus() {
		return negotiationStatus;
	}

	public void setNegotiationStatus(Integer negotiationStatus) {
		this.negotiationStatus = negotiationStatus;
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

	public Long getCloseUserId() {
		return closeUserId;
	}

	public void setCloseUserId(Long closeUserId) {
		this.closeUserId = closeUserId;
	}

	public String getQuoteSpecificationUrl() {
		return quoteSpecificationUrl;
	}

	public void setQuoteSpecificationUrl(String quoteSpecificationUrl) {
		this.quoteSpecificationUrl = quoteSpecificationUrl;
	}

	public String getQuoteTemplateUrl() {
		return quoteTemplateUrl;
	}

	public void setQuoteTemplateUrl(String quoteTemplateUrl) {
		this.quoteTemplateUrl = quoteTemplateUrl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getQuoteSpecFileName() {
		return quoteSpecFileName;
	}

	public void setQuoteSpecFileName(String quoteSpecFileName) {
		this.quoteSpecFileName = quoteSpecFileName;
	}

	public String getQuoteTemplateFileName() {
		return quoteTemplateFileName;
	}

	public void setQuoteTemplateFileName(String quoteTemplateFileName) {
		this.quoteTemplateFileName = quoteTemplateFileName;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Integer isVendor) {
		this.isVendor = isVendor;
	}

	public Integer getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(Integer quoteType) {
		this.quoteType = quoteType;
	}

	@ManyToOne
	@JoinColumn(name="SIGN_PERSON1_ID",insertable=false,updatable=false)
	public UserEntity getSignPerson1() {
		return signPerson1;
	}

	public void setSignPerson1(UserEntity signPerson1) {
		this.signPerson1 = signPerson1;
	}

	@ManyToOne
	@JoinColumn(name="SIGN_PERSON2_ID",insertable=false,updatable=false)
	public UserEntity getSignPerson2() {
		return signPerson2;
	}

	public void setSignPerson2(UserEntity signPerson2) {
		this.signPerson2 = signPerson2;
	}

	@ManyToOne
	@JoinColumn(name="SIGN_PERSON3_ID",insertable=false,updatable=false)
	public UserEntity getSignPerson3() {
		return signPerson3;
	}

	public void setSignPerson3(UserEntity signPerson3) {
		this.signPerson3 = signPerson3;
	}

	@ManyToOne
	@JoinColumn(name="SIGN_PERSON4_ID",insertable=false,updatable=false)
	public UserEntity getSignPerson4() {
		return signPerson4;
	}

	public void setSignPerson4(UserEntity signPerson4) {
		this.signPerson4 = signPerson4;
	}

	@ManyToOne
	@JoinColumn(name="SIGN_PERSON5_ID",insertable=false,updatable=false)
	public UserEntity getSignPerson5() {
		return signPerson5;
	}

	public void setSignPerson5(UserEntity signPerson5) {
		this.signPerson5 = signPerson5;
	}
	
	@Column(name="SIGN_PERSON1_ID")
	public Long getSignPerson1Id() {
		return signPerson1Id;
	}

	public void setSignPerson1Id(Long signPerson1Id) {
		this.signPerson1Id = signPerson1Id;
	}

	@Column(name="SIGN_PERSON2_ID")
	public Long getSignPerson2Id() {
		return signPerson2Id;
	}

	public void setSignPerson2Id(Long signPerson2Id) {
		this.signPerson2Id = signPerson2Id;
	}

	@Column(name="SIGN_PERSON3_ID")
	public Long getSignPerson3Id() {
		return signPerson3Id;
	}

	public void setSignPerson3Id(Long signPerson3Id) {
		this.signPerson3Id = signPerson3Id;
	}

	@Column(name="SIGN_PERSON4_ID")
	public Long getSignPerson4Id() {
		return signPerson4Id;
	}

	public void setSignPerson4Id(Long signPerson4Id) {
		this.signPerson4Id = signPerson4Id;
	}

	@Column(name="SIGN_PERSON5_ID")
	public Long getSignPerson5Id() {
		return signPerson5Id;
	}

	public void setSignPerson5Id(Long signPerson5Id) {
		this.signPerson5Id = signPerson5Id;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@Column(name="PRICE_START_TIME")
	public Timestamp getPriceStartTime() {
		return priceStartTime;
	}

	public void setPriceStartTime(Timestamp priceStartTime) {
		this.priceStartTime = priceStartTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@Column(name="PRICE_END_TIME")
	public Timestamp getPriceEndTime() {
		return priceEndTime;
	}

	public void setPriceEndTime(Timestamp priceEndTime) {
		this.priceEndTime = priceEndTime;
	}

	@Column(name="FACTORY")
	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public Integer getCheckDep() {
		return checkDep;
	}

	public void setCheckDep(Integer checkDep) {
		this.checkDep = checkDep;
	}

	//add by yao.jin 是否按供应商维度进行询价
	@Column(name="IS_DIM")
	public Integer getIsDim() {
		return isDim;
	}

	public void setIsDim(Integer isDim) {
		this.isDim = isDim;
	}	
	//end add
	
	
	
}
