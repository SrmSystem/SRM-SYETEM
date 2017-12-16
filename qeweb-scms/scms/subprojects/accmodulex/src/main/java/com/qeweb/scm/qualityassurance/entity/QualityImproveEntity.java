package com.qeweb.scm.qualityassurance.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;


/**
 * 质量改进管理
 * @author xs
 *
 */
@Entity
@Table(name = "qeweb_qm_quality_improve")
public class QualityImproveEntity extends BaseEntity{
//	private Long vendorId;	//供应商ID
	private String vendorCode;	//供应商代码
	private String vendorName;	//供应商名称
	private OrganizationEntity vendor;	
	private String informFileName;  //改进通知文件名
	private String informFilePath;	//改进通知文件路径
	private String improveFileName;	//改进方案文件名
	private String improveFilePath;	//改进方案文件路径
	private Integer dataStatus;	//0未发布,1发布,-1关闭
	private Integer improveStatus;	//0等待整改,1改进通过，-1整改驳回，-2驳回反馈
	
	
//	private UserEntity correctiveUser; 
//	
//	
//	@ManyToOne
//	@JoinColumn(name = "correctiveUserId",insertable = false,updatable=false)
//	public UserEntity getCorrectiveUser() {
//		return correctiveUser;
//	}
//	public void setCorrectiveUser(UserEntity correctiveUser) {
//		this.correctiveUser = correctiveUser;
//	}
	
	
//	public Long getVendorId() {
//		return vendorId;
//	}
//	public void setVendorId(Long vendorId) {
//		this.vendorId = vendorId;
//	}
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
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	public String getInformFileName() {
		return informFileName;
	}
	public void setInformFileName(String informFileName) {
		this.informFileName = informFileName;
	}
	public String getInformFilePath() {
		return informFilePath;
	}
	public void setInformFilePath(String informFilePath) {
		this.informFilePath = informFilePath;
	}
	public String getImproveFileName() {
		return improveFileName;
	}
	public void setImproveFileName(String improveFileName) {
		this.improveFileName = improveFileName;
	}
	public String getImproveFilePath() {
		return improveFilePath;
	}
	public void setImproveFilePath(String improveFilePath) {
		this.improveFilePath = improveFilePath;
	}
	public Integer getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}
	public Integer getImproveStatus() {
		return improveStatus;
	}
	public void setImproveStatus(Integer improveStatus) {
		this.improveStatus = improveStatus;
	}
	

	
	
}
