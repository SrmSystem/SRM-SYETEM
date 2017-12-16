package com.qeweb.scm.contractmodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_contract")
public class ContractEntity extends EPBaseEntity{
	
	private Long applyUser;  //申请人 系统带出
	private String applyUserName;
	
	private String signUserPhone; //签订人电话
	
	private String stopContractRemark;//合同终止备注 
	private Timestamp stopContractTime;//合同终止时间       
	private String stopContractTimeStr;
	
	private String deliveryTimeStr;
	private Timestamp deliveryTime;//交货时间 临时合同
	private String place;//产地  临时合同
	
	private String attr_1; //合同款项方向
	private String attr_2; //质保金  年度，临时合同
	private String attr_3; //返利    年度，临时合同
	private String attr_4;//用印种类
	private String attr_5;//招投标
	private String attr_6;//预算及立项
	private String attr_7;//办理授权
	private String attr_8;//合同变更
	private String attr_9;//仓储费
	private String attr_10;//结算方式   年度，临时合同
	private String attr_11; //驳回建议  BPM同步到PBMS
	private String attr_12; //收支条件
	private String attr_13; //收支时间
	private String attr_14;//收支比例
	private String attr_15;//委托代理人 年度，临时合同
	
	private String attr_16;//付款方式
	private String attr_17;//交货期
	private String attr_18;//运费
	private String attr_19;//合同执行时间
	private String attr_20;//保修标准  临时合同

	
	
	
	
	private Long signUserOne;//会签人1  采购主管
	private Long signUserTwo;//核价员
	private Long signUserThree;//采购部长
	private String signUserOneName;//会签人1  采购主管
	private String signUserTwoName;//核价员
	private String signUserThreeName;//采购部长
	
	private UserEntity signUserOneEntity;
	private UserEntity signUserTwoEntity;
	private UserEntity signUserThreeEntity;
	private UserEntity applyUserEntity;

	
	private String pdfPath; //服务器上生成的pdf的路径
	
	private String isPdf;
	
	private Long contractType;
	
	private Long moduleId;
	
	/**
	 * 合同名称
	 */
	 private String contractName;
	 /**
	  * 合同的ID链，记录，比如（合同父类ID-下一级合同ID）依次类推
	  */
    private String levelLink;
    /**
     * 合同编号
     */
    private String code;
    
    private String vendorName;
    
    private Long vendorIdFk;
    
    private OrganizationEntity vendor;
    
  

    /**
     * 发布时间
     */
    private Timestamp publishTime;

    /**
     * 发布状态
     */
    private int publishStatus;
    
    private Long publishUserIdFk;
    
    
    
    /**
     * 审核状态
     */
    private int auditStatus;
    /**
     * 审核时间
     */
    private Timestamp auditTime;
    
    private Long auditUserId;

    /**
     * 签订日期
     */
    private Timestamp signDate;
    /**
     * 有效开始时间
     */
    private Timestamp effrctiveDateStart;
    /**
     * 有效结束时间
     */
    private Timestamp effrctiveDateEnd;
    /**
     * 代理委托人名称
     */
    private String proxyUserName;
    /**
     * 签订人
     */
    private String signUser;
    /**
     * 签订地址
     */
    private String signAddress;
    
    /**
     * 发货地址
     */
    private String deliveryAddress;
    /**
     * 付款方式
     */
    private String payWay;
    
    /**
     * 合同金额
     */
    private Double contractPrice;
    
    /**
     * 合同税率
     */
    private Double wholeTaxRate;

    /**
     * 生效或反生效时间
     */
    private Timestamp enabledTime;
    /**
     * 生效状态
     */
    private int enabledStatus;
    
    private Long enabledUserIdFk;

    /**
     * 确认时间
     */
    private Timestamp confirmTime;
    /**
     * 确认状态
     */
    private int confirmStatus;
    
    private Long confirmUserId;
    
    
    /**
     * 是否上传了附件
     */
    private int hasAttachement;
    /**
     * 盖章附件的确认状态
     */
    private int sealAttConfirmStatus;
    /**
     * 是否上传了盖章附件
     */
    private int hasSealAttchement;
  
    /**
     * 审批状态
     */
    private int approveStatus;
    /**
     * 询比价来源ID集合
     */
    private String eqSourceId;
  
    /**
     * 补充协议的数量
     */
    private int addendumCount;
    /**
     * 待审核的补充协议
     */
    private int addendumAuditCount;

    /**
     * 备注
     */
    private String remarks;
    
    
    //冗余
    private String signDateStr;
    private String effrctiveDateStartStr;
    private String effrctiveDateEndStr;
    private String tableData;
    private String fileName;
    private String fileUrl;
    private String fileSealName;
    private String fileSealUrl;
    private String fileConfirmName;
    private String fileConfirmUrl;
    private String isDisable;//判断是否到期了
    
    
	@Transient
	public String getSignDateStr() {
		return signDateStr;
	}

	public void setSignDateStr(String signDateStr) {
		this.signDateStr = signDateStr;
	}

	@Transient
	public String getEffrctiveDateStartStr() {
		return effrctiveDateStartStr;
	}

	public void setEffrctiveDateStartStr(String effrctiveDateStartStr) {
		this.effrctiveDateStartStr = effrctiveDateStartStr;
	}

	@Transient
	public String getEffrctiveDateEndStr() {
		return effrctiveDateEndStr;
	}

	public void setEffrctiveDateEndStr(String effrctiveDateEndStr) {
		this.effrctiveDateEndStr = effrctiveDateEndStr;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getLevelLink() {
		return levelLink;
	}

	public void setLevelLink(String levelLink) {
		this.levelLink = levelLink;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="vendor_id_fk")
	public Long getVendorIdFk() {
		return vendorIdFk;
	}

	public void setVendorIdFk(Long vendorIdFk) {
		this.vendorIdFk = vendorIdFk;
	}
	
	@ManyToOne
	@JoinColumn(name = "vendor_id_fk",insertable = false,updatable=false)
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	


	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Long getPublishUserIdFk() {
		return publishUserIdFk;
	}

	public void setPublishUserIdFk(Long publishUserIdFk) {
		this.publishUserIdFk = publishUserIdFk;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
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

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getSignDate() {
		return signDate;
	}

	public void setSignDate(Timestamp signDate) {
		this.signDate = signDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getEffrctiveDateStart() {
		return effrctiveDateStart;
	}

	public void setEffrctiveDateStart(Timestamp effrctiveDateStart) {
		this.effrctiveDateStart = effrctiveDateStart;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getEffrctiveDateEnd() {
		return effrctiveDateEnd;
	}

	public void setEffrctiveDateEnd(Timestamp effrctiveDateEnd) {
		this.effrctiveDateEnd = effrctiveDateEnd;
	}

	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	public String getSignUser() {
		return signUser;
	}

	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}

	public String getSignAddress() {
		return signAddress;
	}

	public void setSignAddress(String signAddress) {
		this.signAddress = signAddress;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public Double getContractPrice() {
		return contractPrice;
	}

	public void setContractPrice(Double contractPrice) {
		this.contractPrice = contractPrice;
	}

	public Double getWholeTaxRate() {
		return wholeTaxRate;
	}

	public void setWholeTaxRate(Double wholeTaxRate) {
		this.wholeTaxRate = wholeTaxRate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getEnabledTime() {
		return enabledTime;
	}

	public void setEnabledTime(Timestamp enabledTime) {
		this.enabledTime = enabledTime;
	}

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int enabledStatus) {
		this.enabledStatus = enabledStatus;
	}



	public Long getEnabledUserIdFk() {
		return enabledUserIdFk;
	}

	public void setEnabledUserIdFk(Long enabledUserIdFk) {
		this.enabledUserIdFk = enabledUserIdFk;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	public int getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(int confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Long getConfirmUserId() {
		return confirmUserId;
	}

	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	public int getHasAttachement() {
		return hasAttachement;
	}

	public void setHasAttachement(int hasAttachement) {
		this.hasAttachement = hasAttachement;
	}



	public int getSealAttConfirmStatus() {
		return sealAttConfirmStatus;
	}

	public void setSealAttConfirmStatus(int sealAttConfirmStatus) {
		this.sealAttConfirmStatus = sealAttConfirmStatus;
	}

	public int getHasSealAttchement() {
		return hasSealAttchement;
	}

	public void setHasSealAttchement(int hasSealAttchement) {
		this.hasSealAttchement = hasSealAttchement;
	}

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getEqSourceId() {
		return eqSourceId;
	}

	public void setEqSourceId(String eqSourceId) {
		this.eqSourceId = eqSourceId;
	}

	public int getAddendumCount() {
		return addendumCount;
	}

	public void setAddendumCount(int addendumCount) {
		this.addendumCount = addendumCount;
	}

	public int getAddendumAuditCount() {
		return addendumAuditCount;
	}

	public void setAddendumAuditCount(int addendumAuditCount) {
		this.addendumAuditCount = addendumAuditCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Transient
	public String getTableData() {
		return tableData;
	}

	public void setTableData(String tableData) {
		this.tableData = tableData;
	}

	@Transient
	public String getVendorName() {
		return vendorName;
	}

	
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Transient
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Transient
	public String getFileSealName() {
		return fileSealName;
	}

	public void setFileSealName(String fileSealName) {
		this.fileSealName = fileSealName;
	}

	@Transient
	public String getFileSealUrl() {
		return fileSealUrl;
	}

	public void setFileSealUrl(String fileSealUrl) {
		this.fileSealUrl = fileSealUrl;
	}
	
	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public Long getContractType() {
		return contractType;
	}

	public void setContractType(Long contractType) {
		this.contractType = contractType;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Transient
	public String getFileConfirmName() {
		return fileConfirmName;
	}

	public void setFileConfirmName(String fileConfirmName) {
		this.fileConfirmName = fileConfirmName;
	}

	@Transient
	public String getFileConfirmUrl() {
		return fileConfirmUrl;
	}

	public void setFileConfirmUrl(String fileConfirmUrl) {
		this.fileConfirmUrl = fileConfirmUrl;
	}



	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getSignUserPhone() {
		return signUserPhone;
	}

	public void setSignUserPhone(String signUserPhone) {
		this.signUserPhone = signUserPhone;
	}

	public String getStopContractRemark() {
		return stopContractRemark;
	}

	public void setStopContractRemark(String stopContractRemark) {
		this.stopContractRemark = stopContractRemark;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getStopContractTime() {
		return stopContractTime;
	}

	public void setStopContractTime(Timestamp stopContractTime) {
		this.stopContractTime = stopContractTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
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

	public String getAttr_11() {
		return attr_11;
	}

	public void setAttr_11(String attr_11) {
		this.attr_11 = attr_11;
	}

	public String getAttr_12() {
		return attr_12;
	}

	public void setAttr_12(String attr_12) {
		this.attr_12 = attr_12;
	}

	public String getAttr_13() {
		return attr_13;
	}

	public void setAttr_13(String attr_13) {
		this.attr_13 = attr_13;
	}

	public String getAttr_14() {
		return attr_14;
	}

	public void setAttr_14(String attr_14) {
		this.attr_14 = attr_14;
	}

	public String getAttr_15() {
		return attr_15;
	}

	public void setAttr_15(String attr_15) {
		this.attr_15 = attr_15;
	}


	public String getSignUserOneName() {
		return signUserOneName;
	}

	public void setSignUserOneName(String signUserOneName) {
		this.signUserOneName = signUserOneName;
	}

	public String getSignUserTwoName() {
		return signUserTwoName;
	}

	public void setSignUserTwoName(String signUserTwoName) {
		this.signUserTwoName = signUserTwoName;
	}

	public String getSignUserThreeName() {
		return signUserThreeName;
	}

	public void setSignUserThreeName(String signUserThreeName) {
		this.signUserThreeName = signUserThreeName;
	}

	@Transient
	public String getStopContractTimeStr() {
		return stopContractTimeStr;
	}

	public void setStopContractTimeStr(String stopContractTimeStr) {
		this.stopContractTimeStr = stopContractTimeStr;
	}

	@Transient
	public String getDeliveryTimeStr() {
		return deliveryTimeStr;
	}

	public void setDeliveryTimeStr(String deliveryTimeStr) {
		this.deliveryTimeStr = deliveryTimeStr;
	}

	public String getAttr_16() {
		return attr_16;
	}

	public void setAttr_16(String attr_16) {
		this.attr_16 = attr_16;
	}

	public String getAttr_17() {
		return attr_17;
	}

	public void setAttr_17(String attr_17) {
		this.attr_17 = attr_17;
	}

	public String getAttr_18() {
		return attr_18;
	}

	public void setAttr_18(String attr_18) {
		this.attr_18 = attr_18;
	}

	public String getAttr_19() {
		return attr_19;
	}

	public void setAttr_19(String attr_19) {
		this.attr_19 = attr_19;
	}

	public String getAttr_20() {
		return attr_20;
	}

	public void setAttr_20(String attr_20) {
		this.attr_20 = attr_20;
	}

	@Transient
	public String getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(String isDisable) {
		this.isDisable = isDisable;
	}
	
	
	@Column(name="SIGN_USER_ONE")
	public Long getSignUserOne() {
		return signUserOne;
	}

	public void setSignUserOne(Long signUserOne) {
		this.signUserOne = signUserOne;
	}

	@Column(name="SIGN_USER_TWO")
	public Long getSignUserTwo() {
		return signUserTwo;
	}

	public void setSignUserTwo(Long signUserTwo) {
		this.signUserTwo = signUserTwo;
	}

	@Column(name="SIGN_USER_THREE")
	public Long getSignUserThree() {
		return signUserThree;
	}

	public void setSignUserThree(Long signUserThree) {
		this.signUserThree = signUserThree;
	}

	
	@ManyToOne
	@JoinColumn(name = "SIGN_USER_ONE",insertable = false,updatable=false)
	public UserEntity getSignUserOneEntity() {
		return signUserOneEntity;
	}

	public void setSignUserOneEntity(UserEntity signUserOneEntity) {
		this.signUserOneEntity = signUserOneEntity;
	}

	@ManyToOne
	@JoinColumn(name = "SIGN_USER_TWO",insertable = false,updatable=false)
	public UserEntity getSignUserTwoEntity() {
		return signUserTwoEntity;
	}

	public void setSignUserTwoEntity(UserEntity signUserTwoEntity) {
		this.signUserTwoEntity = signUserTwoEntity;
	}

	@ManyToOne
	@JoinColumn(name = "SIGN_USER_THREE",insertable = false,updatable=false)
	public UserEntity getSignUserThreeEntity() {
		return signUserThreeEntity;
	}

	public void setSignUserThreeEntity(UserEntity signUserThreeEntity) {
		this.signUserThreeEntity = signUserThreeEntity;
	}

	public String getIsPdf() {
		return isPdf;
	}

	public void setIsPdf(String isPdf) {
		this.isPdf = isPdf;
	}
	
	@Column(name="APPLY_USER")
	public Long getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(Long applyUser) {
		this.applyUser = applyUser;
	}

	@ManyToOne
	@JoinColumn(name = "APPLY_USER",insertable = false,updatable=false)
	public UserEntity getApplyUserEntity() {
		return applyUserEntity;
	}

	public void setApplyUserEntity(UserEntity applyUserEntity) {
		this.applyUserEntity = applyUserEntity;
	}
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
    
    
    
	
	

}
