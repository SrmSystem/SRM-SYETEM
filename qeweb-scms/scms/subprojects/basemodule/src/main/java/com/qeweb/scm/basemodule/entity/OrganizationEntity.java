package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.constants.OrgType;

@Entity
@Table(name="QEWEB_ORGANIZATION")
public class OrganizationEntity extends BaseEntity{

	private String code;
	
	private String midCode;
	
	private String name;
	
	private String legalPerson;
	
	private String phone;
	
	private String email;
	
	private Integer enableStatus;//启用状态
	
	private Integer activeStatus;//是否已激活
	
	private Integer confirmStatus;//是否被确认
	
	private Integer auditStatus;//所有的调查表的审核状态
	
	private Integer submitStatus;//所有调查表的提交状态
	
	private Integer auditSn;//审核顺序，已提交-已审核
	
	/** 组织类型{@link OrgType} */
	private Integer orgType;
	
	/** 组织的角色类型{@link OrgType} */
	private Integer roleType;
	
	private Timestamp registerTime;
	/** 顶级父类ID */
	private Long topParentId;
	/** 父ID */
	private Long parentId;
	
	private String parentOrgName;
	/** 级别描述（类似 父ID-父ID） */
	private String levelDescribe;
	/**扩展字段*/
	private String col1;		// vendorSiteCode			
	private String col2;
	private String col3;					
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10; //体系外供应商标示   1为体系外
	
	private String syncIamParentId;
	private String syncIamId;
	
	//非持久化字段
	private String _orgType;
	private String _roleType;
	
	private List<OrganizationEntity> itemList;
	
	public OrganizationEntity() {
		
	}
	
	public OrganizationEntity(long id) {
		setId(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMidCode() {
		return midCode;
	}

	public void setMidCode(String midCode) {
		this.midCode = midCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public Integer getOrgType() {
		return orgType;
	}

	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8:00")
	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public Long getTopParentId() {
		return topParentId;
	}

	public void setTopParentId(Long topParentId) {
		this.topParentId = topParentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getLevelDescribe() {
		return levelDescribe;
	}

	public void setLevelDescribe(String levelDescribe) {
		this.levelDescribe = levelDescribe;
	}
	
	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}

	public String getCol7() {
		return col7;
	}

	public void setCol7(String col7) {
		this.col7 = col7;
	}

	public String getCol8() {
		return col8;
	}

	public void setCol8(String col8) {
		this.col8 = col8;
	}

	public String getCol9() {
		return col9;
	}

	public void setCol9(String col9) {
		this.col9 = col9;
	}

	public String getCol10() {
		return col10;
	}

	public void setCol10(String col10) {
		this.col10 = col10;
	}

	@Transient
	public String get_orgType() {
		_orgType = OrgType.getOrgType(getOrgType());
		return _orgType;
	}

	public void set_orgType(String _orgType) {
		this._orgType = _orgType;
	}

	@Transient
	public String get_roleType() {
		_roleType = OrgType.getRoleType(getRoleType());
		return _roleType;
	}

	public void set_roleType(String _roleType) {
		this._roleType = _roleType;
	}

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Integer getAuditSn() {
		return auditSn;
	}

	public void setAuditSn(Integer auditSn) {
		this.auditSn = auditSn;
	}

	public String getSyncIamParentId() {
		return syncIamParentId;
	}

	public void setSyncIamParentId(String syncIamParentId) {
		this.syncIamParentId = syncIamParentId;
	}

	public String getSyncIamId() {
		return syncIamId;
	}

	public void setSyncIamId(String syncIamId) {
		this.syncIamId = syncIamId;
	}

	@Transient
	public List<OrganizationEntity> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrganizationEntity> itemList) {
		this.itemList = itemList;
	}
	@Transient
	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	
}
