package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 业务单据操作记录
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_bus_operate_log")
public class BusOperateLogEntity extends BaseEntity {

	private long record; 		// 记录ID
	private String classz; 		// 操作类
	private String operate;		// 方法
	private String objClassz;	// 参数类
	private String content; 	// 单据内容

	public long getRecord() {
		return record;
	}

	public void setRecord(long record) {
		this.record = record;
	}

	public String getClassz() {
		return classz;
	}

	public void setClassz(String classz) {
		this.classz = classz;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getObjClassz() {
		return objClassz;
	}

	public void setObjClassz(String objClassz) {
		this.objClassz = objClassz;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}