package com.qeweb.scm.filemodule.entity;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 文件反馈实体类
 * @author hao.qin
 *
 */
@Entity
@Table(name = "QEWEB_FILE_FEEDBACK")
public class FileFeedbackEntity extends BaseEntity {
	
	
	
    /**
     * 文件协同
     */
	private FileCollaborationEntity fileCollaboration;
	
	
    /**
     * 供应商
     */
	private OrganizationEntity vendor;		
	
    /**
     * 回传文件标题
     */
	private String fileName;
	
	
    /**
     * 回传文件标题
     */
	private String filePath;
	
    /**
     * 回传文件类容
     */
	private Blob	fileContent;
	
    /**
     * 回传状态
     */
	private Integer backStatus;	
	
    /**
     * 回传人员
     */
	private UserEntity backUser;	
	
    /**
     * 回传时间
     */
	private Timestamp backTime;
	
	@ManyToOne
	@JoinColumn(name="FILE_COLLABORATION_ID")
	public FileCollaborationEntity getFileCollaboration() {
		return fileCollaboration;
	}

	public void setFileCollaboration(FileCollaborationEntity fileCollaboration) {
		this.fileCollaboration = fileCollaboration;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
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

	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

	@ManyToOne
	@JoinColumn(name="back_user_id")
	public UserEntity getBackUser() {
		return backUser;
	}

	public void setBackUser(UserEntity backUser) {
		this.backUser = backUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getBackTime() {
		return backTime;
	}

	public void setBackTime(Timestamp backTime) {
		this.backTime = backTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}	

}
