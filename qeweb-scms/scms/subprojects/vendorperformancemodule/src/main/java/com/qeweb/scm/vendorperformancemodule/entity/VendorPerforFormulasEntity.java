package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 公式设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_formulas")
public class VendorPerforFormulasEntity  extends IdEntity{
	
	private String content;	
	private long indexId;
	private long cycleId;
	
	private VendorPerforCycleEntity cycleEntity;
	
	private VendorPerforIndexEntity indexEntity;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getIndexId() {
		return indexId;
	}
	public void setIndexId(long indexId) {
		this.indexId = indexId;
	}
	public long getCycleId() {
		return cycleId;
	}
	public void setCycleId(long cycleId) {
		this.cycleId = cycleId;
	}
	
	@ManyToOne
	@JoinColumn(name = "cycleId",insertable = false,updatable = false)
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}
	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	
	@ManyToOne
	@JoinColumn(name = "indexId",insertable = false,updatable = false)
	public VendorPerforIndexEntity getIndexEntity() {
		return indexEntity;
	}
	public void setIndexEntity(VendorPerforIndexEntity indexEntity) {
		this.indexEntity = indexEntity;
	}
}
