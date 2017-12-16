package com.qeweb.scm.filemodule.entity;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 文件协同主表实体类
 * @author hao.qin
 *
 */
@Entity
@Table(name = "QEWEB_FILE_COLLABORATION")
public class FileCollaborationEntity extends BaseEntity {
	
    /**
     * 文件协同标题
     */
	private String title;
	
    /**
     * 文件协同类型
     */
	private DictItemEntity  collaborationType;
	
    /**
     * 文件名称
     */
	private String fileName;
	
	
    /**
     * 文件路径
     */
	private String filePath;
	
	
    /**
     * 文件类容
     */
	private Blob	 fileContent;
	
    /**
     * 文件协同描述
     */
	private String describe;
	
    /**
     * 文件通知ids（为空时默认,通知所有供应商）
     */
	private String vendorIds;
	
	
    /**
     * 发布状态(已发布，待发布，已结束)
     */
	private Integer publishStatus;	
	
    /**
     * 发布人
     */
	private UserEntity publishUser;	
	
    /**
     * 发布时间
     */
	private Timestamp publishTime;	
	
    /**
     * 开始时间
     */
	private Timestamp validStartTime;
	
    /**
     * 结束时间
     */
	private Timestamp validEndTime;
	
	
	 /**
     * 反馈明细
     */
	private Set<FileFeedbackEntity> fileFeedback;	
	
	
	
	//非持久化数据
	private String collaborationTypeCode;
	private String vendorNames;
	
	@Transient
	public String getCollaborationTypeCode() {
		return collaborationTypeCode;
	}

	public void setCollaborationTypeCode(String collaborationTypeCode) {
		this.collaborationTypeCode = collaborationTypeCode;
	}

	@Transient
	public String getVendorNames() {
		return vendorNames;
	}

	public void setVendorNames(String vendorNames) {
		this.vendorNames = vendorNames;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ManyToOne
	@JoinColumn(name="COLLABORATION_TYPE_ID")
	public DictItemEntity getCollaborationType() {
		return collaborationType;
	}

	public void setCollaborationType(DictItemEntity collaborationType) {
		this.collaborationType = collaborationType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@JsonIgnore 
	public Blob getFileContent() {
		return fileContent;
	}

	public void setFileContent(Blob fileContent) {
		this.fileContent = fileContent;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}


	@ManyToOne
	@JoinColumn(name="publish_user_id")
	public UserEntity getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(UserEntity publishUser) {
		this.publishUser = publishUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Timestamp validStartTime) {
		this.validStartTime = validStartTime;
	}
	

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Timestamp validEndTime) {
		this.validEndTime = validEndTime;
	}

	public String getVendorIds() {
		return vendorIds;
	}

	public void setVendorIds(String vendorIds) {
		this.vendorIds = vendorIds;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="fileCollaboration")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<FileFeedbackEntity> getFileFeedback() {
		return fileFeedback;
	}

	public void setFileFeedback(Set<FileFeedbackEntity> fileFeedback) {
		this.fileFeedback = fileFeedback;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
