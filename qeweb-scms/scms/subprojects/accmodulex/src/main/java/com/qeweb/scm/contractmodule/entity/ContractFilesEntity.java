package com.qeweb.scm.contractmodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
/**
 * 附件
 *
 */
@Entity
@Table(name = "QEWEB_CONTRACT_FILES")
public class ContractFilesEntity extends BaseEntity {
	
	private Long busId;
	private Long busType;
	private String fileName;
	private String filePath;
	public Long getBusId() {
		return busId;
	}
	public void setBusId(Long busId) {
		this.busId = busId;
	}
	public Long getBusType() {
		return busType;
	}
	public void setBusType(Long busType) {
		this.busType = busType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath== null ? "":filePath.replaceAll("\\\\", "/");
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	
	

	
	
	
	
	
	
}
