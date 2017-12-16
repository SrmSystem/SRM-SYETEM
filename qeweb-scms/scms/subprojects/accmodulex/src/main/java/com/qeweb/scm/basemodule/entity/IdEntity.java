package com.qeweb.scm.basemodule.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 统一定义Entity的ID
 * 
 * 基类统一定义ID的名称、属性、生成策略、列名映射
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * @author pjjxiajun
 * @date 2015年2月13日
 * @path com.qeweb.scm.basemodule.entity.com.qeweb.scm.basemodule.entity
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3399117443629113861L;
	private long id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
