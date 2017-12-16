package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="qeweb_notice")
public class NoticeEntity extends BaseEntity{
	
	private String title;		
	private String content;
	private Integer noticeType;
	private Integer lookNumber;
	private Integer commentPower;
	private Timestamp validStartTime;
	private Timestamp validEndTime;
	private String noticeTypeNames;
	
	private String addids;//非数据库字段
	
	private String spkeaNumber;//非数据库字段
	
	private Integer ntype;//非数据库字段

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCommentPower() {
		return commentPower;
	}

	public void setCommentPower(Integer commentPower) {
		this.commentPower = commentPower;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Timestamp validStartTime) {
		this.validStartTime = validStartTime;
	}
	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public Integer getLookNumber() {
		return lookNumber;
	}

	public void setLookNumber(Integer lookNumber) {
		this.lookNumber = lookNumber;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Timestamp validEndTime) {
		this.validEndTime = validEndTime;
	}
	@Transient
	public String getAddids() {
		return addids;
	}

	public void setAddids(String addids) {
		this.addids = addids;
	}

	public String getNoticeTypeNames() {
		return noticeTypeNames;
	}

	public void setNoticeTypeNames(String noticeTypeNames) {
		this.noticeTypeNames = noticeTypeNames;
	}

	@Transient
	public String getSpkeaNumber() {
		return spkeaNumber;
	}

	public void setSpkeaNumber(String spkeaNumber) {
		this.spkeaNumber = spkeaNumber;
	}

	@Transient
	public Integer getNtype() {
		return ntype;
	}

	public void setNtype(Integer ntype) {
		this.ntype = ntype;
	}
	
}
