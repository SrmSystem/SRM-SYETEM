package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
/**
 * 需要上传的文件
 * @author u
 *
 */
@Entity
@Table(name = "qeweb_meta_fileup")
public class FileUpEntity extends BaseEntity {
	private FileDescriptEntity descriptEntity;		//上传文件的描述性文本
	private String filePath;						//文件的存储路径
	private String fileName;						//文件的名称
	private Integer fileCount;						//上传文件的个数
	private ReceiveEntity receive;					//收货单信息
	
	@ManyToOne
	@JoinColumn(name="descript_id")
	public FileDescriptEntity getDescriptEntity() {
		return descriptEntity;
	}
	public void setDescriptEntity(FileDescriptEntity descriptEntity) {
		this.descriptEntity = descriptEntity;
	}
	
	@Column(name="file_path")
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Column(name="file_name")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name="file_count")
	public Integer getFileCount() {
		return fileCount;
	}
	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}
	
	@ManyToOne
	@JoinColumn(name="receive_id")
	public ReceiveEntity getReceive() {
		return receive;
	}
	public void setReceive(ReceiveEntity receive) {
		this.receive = receive;
	}	
}