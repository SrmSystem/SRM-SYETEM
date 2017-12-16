package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.RoleEntity;


@Entity
@Table(name = "QEWEB_WARN_MAIN")
public class WarnMainEntity extends BaseEntity {
	
	private String moduleName;
	
	private String warnContent;
	
	private String code;
	
	private String name;
	
	private String content;//消息内容
	
	private String warnTime;
	
	private Integer isVendor;//是否供应商
	
	private Integer enableStatus;//是否启用  0 禁用  1启用
	
	private Integer isMail; // 是否发邮件提醒   0 发送邮件提醒      1不发送邮件提醒
	
    private Long roleOneId;
    
    private Long roleTwoId;
    
    private Long roleThreeId;
    
    private RoleEntity roleOne;
    
    private RoleEntity roleTwo;
    
    private RoleEntity roleThree;
    
    
    //add by hao.qin  start
    private Integer isWarning; // 是否开启预警
    
    private String isVendorName;//用于显示供应商和采方名称
    
  //add by hao.qin end
    
	public Integer getIsWarning() {
		return isWarning;
	}

	public void setIsWarning(Integer isWarning) {
		this.isWarning = isWarning;
	}
    
    @Transient
	public String getIsVendorName() {
		return isVendorName;
	}

	public void setIsVendorName(String isVendorName) {
		this.isVendorName = isVendorName;
	}

	@Column(name="ROLE_ONE_ID")
	public Long getRoleOneId() {
		return roleOneId;
	}

	public void setRoleOneId(Long roleOneId) {
		this.roleOneId = roleOneId;
	}
    @Column(name="ROLE_TWO_ID")
	public Long getRoleTwoId() {
		return roleTwoId;
	}
    
	public void setRoleTwoId(Long roleTwoId) {
		this.roleTwoId = roleTwoId;
	}
	@Column(name="ROLE_THREE_ID")
	public Long getRoleThreeId() {
		return roleThreeId;
	}

	public void setRoleThreeId(Long roleThreeId) {
		this.roleThreeId = roleThreeId;
	}
	
	
	@ManyToOne
	@JoinColumn(name="ROLE_ONE_ID",insertable=false,updatable=false)
	public RoleEntity getRoleOne() {
		return roleOne;
	}

	public void setRoleOne(RoleEntity roleOne) {
		this.roleOne = roleOne;
	}
	@ManyToOne
	@JoinColumn(name="ROLE_TWO_ID",insertable=false,updatable=false)
	public RoleEntity getRoleTwo() {
		return roleTwo;
	}

	public void setRoleTwo(RoleEntity roleTwo) {
		this.roleTwo = roleTwo;
	}
	@ManyToOne
	@JoinColumn(name="ROLE_THREE_ID",insertable=false,updatable=false)
	public RoleEntity getRoleThree() {
		return roleThree;
	}

	public void setRoleThree(RoleEntity roleThree) {
		this.roleThree = roleThree;
	}

	public Integer getIsMail() {
		return isMail;
	}

	public void setIsMail(Integer isMail) {
		this.isMail = isMail;
	}

	public String getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(String warnTime) {
		this.warnTime = warnTime;
	}

	public String getWarnContent() {
		return warnContent;
	}

	public void setWarnContent(String warnContent) {
		this.warnContent = warnContent;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}	
	
	

	public Integer getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Integer isVendor) {
		this.isVendor = isVendor;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	

}
