package com.qeweb.scm.epmodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 询价供应商实体类
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_VENDOR")
public class EpVendorEntity extends EPBaseEntity{
	
	private EpPriceEntity epPrice;				//询价单
	private Long vendorId;						//供应商id
	private String vendorCode;					//供应商编码
	private String vendorName;					//供应商名称
	private String address;						//地址
	private String legalRep;					//联系人
	private String linkPhone;					//联系电话
	private String businessLinkPhone;			//业务联系人手机号码
	private Integer accessStatus;				//准入状态
	private String orgEmail;					//email
	private String sapCode;						//SAP代码

	private Integer cooperatStatus;				//合作状态
	private Integer applicationStatus; 			// 报名状态(0=未报名；1=已报名)
	
	private Integer eipApprovalStatus;			//审核状态  1 审核通过  0 未审核    2 提交审核   -1 驳回
	private Timestamp eipApprovalTime;			//审核时间
	private Long eipApprovalUserId;				//审核人id
	
	private String eipApproveRemark; //审批意见
	private String instanceId; //流程实例号
	
	private Integer quoteStatus; // 报价状态  0 未报价  1已报价
	
	private String specialPurchaseType;	//特殊采购类型：0 = null(标准)；1 = L(外协)；2 = k(寄售)
	
	//冗余
	private String tableDatas;
	
	private Long signPerson1Id;
	private Long signPerson2Id;
	private Long signPerson3Id;
	private Long signPerson4Id;
	private Integer checkDep;
	private UserEntity signPerson1;	//会签人1
	private UserEntity signPerson2;	//会签人2
	private UserEntity signPerson3;	//会签人3
	private UserEntity signPerson4;	//会签人4
	
	
	
	@Transient
	public UserEntity getSignPerson1() {
		return signPerson1;
	}
	public void setSignPerson1(UserEntity signPerson1) {
		this.signPerson1 = signPerson1;
	}
	@Transient
	public UserEntity getSignPerson2() {
		return signPerson2;
	}
	public void setSignPerson2(UserEntity signPerson2) {
		this.signPerson2 = signPerson2;
	}
	@Transient
	public UserEntity getSignPerson3() {
		return signPerson3;
	}
	public void setSignPerson3(UserEntity signPerson3) {
		this.signPerson3 = signPerson3;
	}
	@Transient
	public UserEntity getSignPerson4() {
		return signPerson4;
	}
	public void setSignPerson4(UserEntity signPerson4) {
		this.signPerson4 = signPerson4;
	}
	@Transient
	public Long getSignPerson1Id() {
		return signPerson1Id;
	}
	public void setSignPerson1Id(Long signPerson1Id) {
		this.signPerson1Id = signPerson1Id;
	}
	@Transient
	public Long getSignPerson2Id() {
		return signPerson2Id;
	}
	public void setSignPerson2Id(Long signPerson2Id) {
		this.signPerson2Id = signPerson2Id;
	}
	@Transient
	public Long getSignPerson3Id() {
		return signPerson3Id;
	}
	public void setSignPerson3Id(Long signPerson3Id) {
		this.signPerson3Id = signPerson3Id;
	}
	@Transient
	public Long getSignPerson4Id() {
		return signPerson4Id;
	}
	public void setSignPerson4Id(Long signPerson4Id) {
		this.signPerson4Id = signPerson4Id;
	}
	@Transient
	public Integer getCheckDep() {
		return checkDep;
	}
	public void setCheckDep(Integer checkDep) {
		this.checkDep = checkDep;
	}
	@Transient
	public String getTableDatas() {
		return tableDatas;
	}
	public void setTableDatas(String tableDatas) {
		this.tableDatas = tableDatas;
	}
	
	@ManyToOne
	@JoinColumn(name="ENQUIRE_PRICE_ID")
	public EpPriceEntity getEpPrice() {
		return epPrice;
	}

	public void setEpPrice(EpPriceEntity epPrice) {
		this.epPrice = epPrice;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLegalRep() {
		return legalRep;
	}

	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getBusinessLinkPhone() {
		return businessLinkPhone;
	}

	public void setBusinessLinkPhone(String businessLinkPhone) {
		this.businessLinkPhone = businessLinkPhone;
	}

	public Integer getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(Integer accessStatus) {
		this.accessStatus = accessStatus;
	}

	public String getOrgEmail() {
		return orgEmail;
	}

	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public Integer getCooperatStatus() {
		return cooperatStatus;
	}

	public void setCooperatStatus(Integer cooperatStatus) {
		this.cooperatStatus = cooperatStatus;
	}

	public Integer getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(Integer applicationStatus) {
		this.applicationStatus = applicationStatus;
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

	public String getEipApproveRemark() {
		return eipApproveRemark;
	}

	public void setEipApproveRemark(String eipApproveRemark) {
		this.eipApproveRemark = eipApproveRemark;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(Integer quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public String getSpecialPurchaseType() {
		return specialPurchaseType;
	}

	public void setSpecialPurchaseType(String specialPurchaseType) {
		this.specialPurchaseType = specialPurchaseType;
	}
	
	
	
	
	
	
}
