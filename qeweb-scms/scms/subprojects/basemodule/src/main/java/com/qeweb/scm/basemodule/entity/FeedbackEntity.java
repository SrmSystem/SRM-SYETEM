package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 反馈信息
 * 
 * @author u
 *
 */
@Entity
@Table(name = "qeweb_feedback")
public class FeedbackEntity extends BaseEntity {

	private Integer billType;			// 反馈表类型
	private long billId;				// 反馈单据主键
	private long feedbackOrgId;			// 反馈组织ID
	private String feedbackOrgName;		// 反馈组织名称
	private String feedbackContent;		// 反馈内容
	private String recvOrgId;			// 组织编码接收
	
	//非持久化字段
	private String vendorId;
	private String buyerId;

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public long getBillId() {
		return billId;
	}

	public void setBillId(long billId) {
		this.billId = billId;
	}

	public long getFeedbackOrgId() {
		return feedbackOrgId;
	}

	public void setFeedbackOrgId(long feedbackOrgId) {
		this.feedbackOrgId = feedbackOrgId;
	}

	public String getFeedbackOrgName() {
		return feedbackOrgName;
	}

	public void setFeedbackOrgName(String feedbackOrgName) {
		this.feedbackOrgName = feedbackOrgName;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getRecvOrgId() {
		return recvOrgId;
	}

	public void setRecvOrgId(String recvOrgId) {
		this.recvOrgId = recvOrgId;
	}

	@Transient
	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	@Transient
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

}
