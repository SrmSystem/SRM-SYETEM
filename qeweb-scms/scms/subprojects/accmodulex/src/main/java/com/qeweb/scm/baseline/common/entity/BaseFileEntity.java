package com.qeweb.scm.baseline.common.entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



import com.qeweb.scm.basemodule.entity.BaseEntity;


@Entity
@Table(name = "QEWEB_BASE_FILE")
public class BaseFileEntity extends BaseEntity {
	
	private Long billId;
	private String billType;
	private String fileName;
	private String filePath;
	private Blob fileContent;
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Blob getFileContent() {
		return fileContent;
	}
	public void setFileContent(Blob fileContent) {
		this.fileContent = fileContent;
	}

	

}
