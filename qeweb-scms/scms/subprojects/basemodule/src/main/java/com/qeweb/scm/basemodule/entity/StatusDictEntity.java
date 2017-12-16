package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 状态管理 是，未发布，已审核等
 * 
 * @author lw 创建时间：2015年6月8日15:45:15 最后更新时间：2015年6月8日15:45:20 最后更新人：lw
 */

@Entity
@Table(name = "qeweb_status_dict")
public class StatusDictEntity extends IdEntity {

	private String statusName; 		// 状态名称
	private Integer statusValue;	// 状态值
	private String statusCode; 		// 状态国际化编码key
	private String statusText; 		// 状态文本值
	private String statusIcon; 		// 状态图标url
	private String statusType; 		// 状态类型

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getStatusIcon() {
		return statusIcon;
	}

	public void setStatusIcon(String statusIcon) {
		this.statusIcon = statusIcon;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

}
