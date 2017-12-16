package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
/**
 * 需要上传的文件的描述性文本及类别(配置)
 * @author u
 *
 */
@Entity
@Table(name = "qeweb_meta_filedescript")
public class FileDescriptEntity extends BaseEntity {
	private String descript;		//描述字段
	private Integer saveType;		//存储类别
	private Integer saveLevel;			//级别
	
	//非持久化字段
	private String filePath;
	private String fileName;

	public FileDescriptEntity() {
	}
	
	public FileDescriptEntity(long id) {
		setId(id); 
	}
	
	@Column(name="descript")
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	@Column(name="save_type")
	public Integer getSaveType() {
		return saveType;
	}
	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}
	
	@Column(name="save_level")
	public Integer getSaveLevel() {
		return saveLevel;
	}
	public void setSaveLevel(Integer saveLevel) {
		this.saveLevel = saveLevel;
	}

	@Transient
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}