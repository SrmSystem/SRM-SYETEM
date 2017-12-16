package com.qeweb.scm.purchasemodule.entity;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "QEWEB_ORDER_PROCESS")
public class PurchaseOrderProcessEntity extends BaseEntity {
	
	private Long orderItemId;//外协组件物料ID PurchaseOrderItemMatEntity
	
	private Long processId;
	
	private String processName;
	
	private Double orderNum;
	
	private Timestamp orderTime;
	
	private String fileName;
	
	private String filePath;
	
	//冗余
	private Double orderCount;
	private String tableDatas;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Double getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
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

	@Transient
	public Double getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Double orderCount) {
		this.orderCount = orderCount;
	}

	@Transient
	public String getTableDatas() {
		return tableDatas;
	}

	public void setTableDatas(String tableDatas) {
		this.tableDatas = tableDatas;
	}
	
	
	
	

	

}
