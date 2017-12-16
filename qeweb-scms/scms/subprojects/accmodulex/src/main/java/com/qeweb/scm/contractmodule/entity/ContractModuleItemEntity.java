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
@Table(name = "qeweb_contract_module_item")
public class ContractModuleItemEntity extends EPBaseEntity{
	
	/**
	 * 模板id
	 */
	private Long moduleId; 
	
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
	
	private Long _parentId;
	private List<ContractModuleItemEntity> itemList;
	private Long beforeId;

	
	
	@Column(name="module_id")
	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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
	public List<ContractModuleItemEntity> getItemList() {
		return itemList;
	}

	public void setItemList(List<ContractModuleItemEntity> itemList) {
		this.itemList = itemList;
	}
	
	@Transient
	public Long getBeforeId() {
		return beforeId;
	}
	public void setBeforeId(Long beforeId) {
		this.beforeId = beforeId;
	}
}
