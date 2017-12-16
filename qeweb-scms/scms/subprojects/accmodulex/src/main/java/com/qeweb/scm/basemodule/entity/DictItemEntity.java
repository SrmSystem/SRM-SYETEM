package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 数据字典子表
 * @author chao.gu
 *
 */
@Entity
@Table(name = "QEWEB_DICT_ITEM")
public class DictItemEntity extends BaseEntity{
	
	private String code;
	
	private String name;
	
	private String fullName;
	
	private String dictType;

	private DictEntity dict;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	@ManyToOne
	@JoinColumn(name="dict_id")
	public DictEntity getDict() {
		return dict;
	}

	public void setDict(DictEntity dict) {
		this.dict = dict;
	}
	
	
	
	
}
