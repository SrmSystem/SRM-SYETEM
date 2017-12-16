package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "QEWEB_AUDIT_ORDER_DEF")
public class Order_ProcessDefinitionEntity extends BaseEntity{

	private String entityName;
	
	private String processDefinitionId;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Column(name="DEF_KEY")
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
}
