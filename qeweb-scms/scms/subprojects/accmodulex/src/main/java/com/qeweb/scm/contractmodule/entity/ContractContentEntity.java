package com.qeweb.scm.contractmodule.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;

/**
 * 合同条款
 * @author u
 *
 */

@Entity
@Table(name = "qeweb_contract_content")
public class ContractContentEntity extends EPBaseEntity{
	
	/**
	 * 模板id
	 */
	private Long contractId; 
	
	/**
	 * 上级条款
	 */
	private Long parentId;
	
	/**
	 * 条款编号
	 */
	private String code;
	
	/**
	 * 条款内容
	 */
	private String content;
	
	/**
	 * 条款名称
	 */
	private String name;
	
	/**
	 * 条款的顺序号，用于排序
	 */
	private Integer sqenum;
	
	private Long feedbackContent;
	
	private Long _parentId;
	private List<ContractContentEntity> itemList;
	private Long beforeId;

	
	

	
	
	@Column(name="contract_id")
	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	@Column(name="parent_id")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="sqenum")
	public Integer getSqenum() {
		return sqenum;
	}

	public void setSqenum(Integer sqenum) {
		this.sqenum = sqenum;
	}

	@Transient
	public Long get_parentId() {
		return getParentId();
	}

	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}

	@Transient
	public List<ContractContentEntity> getItemList() {
		return itemList;
	}

	public void setItemList(List<ContractContentEntity> itemList) {
		this.itemList = itemList;
	}
	
	@Transient
	public Long getBeforeId() {
		return beforeId;
	}
	public void setBeforeId(Long beforeId) {
		this.beforeId = beforeId;
	}

	public Long getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(Long feedbackContent) {
		this.feedbackContent = feedbackContent;
	}
	
	
}
