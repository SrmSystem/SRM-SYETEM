package com.qeweb.scm.basemodule.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 数据字典主表
 * @author chao.gu
 *
 */
@Entity
@Table(name = "QEWEB_DICT")
public class DictEntity extends BaseEntity{
	
	private String dictCode;
	
	private String dictName;
	
	private String dictDesc;
	
	
	private Set<DictItemEntity> DictItem;	//数据明细

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictDesc() {
		return dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}
	
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="dict")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<DictItemEntity> getDictItem() {
		return DictItem;
	}

	public void setDictItem(Set<DictItemEntity> dictItem) {
		DictItem = dictItem;
	}


}
