package com.qeweb.scm.abnormal.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
@Entity
@Table(name = "QEWEB_ABNORMAL_FEEDBACK")
public class AbnormalFeedbackEntity extends BaseEntity {
	/**
	 * 采方
	 */
	/**
	 * 异常反馈名称
	 */
	private String abnormalFeedbackName;
	/**
	 * 是否评论
	 */
	private String commentYn;
	/**
	 * 是否置顶
	 */
	private String topYn;
	/**
	 * 有效起始日期
	 */
	private Timestamp effectiveStartDate;
	/**
	 * 有效结束日期
	 */
	private Timestamp effectiveEndDate;
	/**
	 * 是否供应商(现在分表创建，所以暂时不用)
	 */
	private Integer isVendor;
	/**
	 * 文本域(让客户写评论的)
	 */
	private String commentArea;
	/**
	 * 非持久化对象 选择供应商
	 */
	private String vendorNames;
	/**
	 * 发布人员
	 */
	private Long publishUserId;
	/**
	 * 选择供应商的ID
	 */
	private String vendorIds;
	/**
	 * 发布时间
	 */
	private Timestamp publishTime;
	/**
	 *	发布状态
	 */
	private Integer publishStatus; 
	/**
	 * 发布人名称 
	 */
	private String publishName;
	
	
	
	public Integer getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Integer isVendor) {
		this.isVendor = isVendor;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}


	
	
	public Long getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(Long publishUserId) {
		this.publishUserId = publishUserId;
	}

	public String getVendorIds() {
		return vendorIds;
	}

	public void setVendorIds(String vendorIds) {
		this.vendorIds = vendorIds;
	}

	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}



	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	@Transient
	public String getVendorNames() {
		return vendorNames;
	}

	public void setVendorNames(String vendorNames) {
		this.vendorNames = vendorNames;
	}

	public String getAbnormalFeedbackName() {
		return abnormalFeedbackName;
	}

	public void setAbnormalFeedbackName(String abnormalFeedbackName) {
		this.abnormalFeedbackName = abnormalFeedbackName;
	}

	public String getCommentYn() {
		return commentYn;
	}

	public void setCommentYn(String commentYn) {
		this.commentYn = commentYn;
	}

	public String getTopYn() {
		return topYn;
	}

	public void setTopYn(String topYn) {
		this.topYn = topYn;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Timestamp getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Timestamp effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Timestamp getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Timestamp effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getCommentArea() {
		return commentArea;
	}

	public void setCommentArea(String commentArea) {
		this.commentArea = commentArea;
	}

}
