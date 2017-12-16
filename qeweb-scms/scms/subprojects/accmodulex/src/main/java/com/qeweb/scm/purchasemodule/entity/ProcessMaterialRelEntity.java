package com.qeweb.scm.purchasemodule.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@Entity
@Table(name = "QEWEB_PROCESS_MATERIAL_REL")
public class ProcessMaterialRelEntity extends BaseEntity {
	
	private long processId;
	
	private long materialId;
	
	private ProcessEntity process;
	
	private MaterialEntity material;


	@Column(name="PROCESS_ID")
	public long getProcessId() {
		return processId;
	}
	public void setProcessId(long processId) {
		this.processId = processId;
	}
	@ManyToOne
	@JoinColumn(name="PROCESS_ID",insertable=false,updatable=false)
	public ProcessEntity getProcess() {
		return process;
	}
	public void setProcess(ProcessEntity process) {
		this.process = process;
	}
	@Column(name="MATERIAL_ID")
	public long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}
	
	@ManyToOne
	@JoinColumn(name="MATERIAL_ID",insertable=false,updatable=false)
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	

}
