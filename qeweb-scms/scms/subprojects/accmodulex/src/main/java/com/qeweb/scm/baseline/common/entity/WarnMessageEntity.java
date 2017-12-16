package com.qeweb.scm.baseline.common.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;


@Entity
@Table(name = "QEWEB_WARN_MESSAGE")
public class WarnMessageEntity extends BaseEntity {
	
	private Long warnMainId;//预警主信息
	
	private Long userId;//用户
	
	private Long billId;//业务单据id
	
	private String billType;//预留字段 
	
	private String warnTitle;//消息标题
	
	private String warnMessage;//消息内容
	
	private Integer isRead;//是否阅读  0 是  1否
	
	private Integer isOutTime;//是否超时  0 是  1否
	
	private Integer isPromotion;// 是否是晋级提醒 0是 1不是
	
	private Timestamp warnTime;// 预警时间，根据工作日历返回预警时间
	
	
	

	public Timestamp getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Timestamp warnTime) {
		this.warnTime = warnTime;
	}

	public Integer getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(Integer isPromotion) {
		this.isPromotion = isPromotion;
	}

	public Integer getIsOutTime() {
		return isOutTime;
	}

	public void setIsOutTime(Integer isOutTime) {
		this.isOutTime = isOutTime;
	}

	public Long getWarnMainId() {
		return warnMainId;
	}

	public void setWarnMainId(Long warnMainId) {
		this.warnMainId = warnMainId;
	}
   
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getWarnMessage() {
		return warnMessage;
	}

	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getWarnTitle() {
		return warnTitle;
	}

	public void setWarnTitle(String warnTitle) {
		this.warnTitle = warnTitle;
	}

	
	
	
	
	

	

}
