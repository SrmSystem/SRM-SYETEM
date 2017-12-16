package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

//@Entity
//@Table(name = "qeweb_button")
public class ButtonEntity extends BaseEntity{
	
	private String buttonCode;
	private String buttonName;
	private String buttonIcon;
	private long viewId;
	private String viewCode;
	public String getButtonCode() {
		return buttonCode;
	}
	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public String getButtonIcon() {
		return buttonIcon;
	}
	public void setButtonIcon(String buttonIcon) {
		this.buttonIcon = buttonIcon;
	}
	public long getViewId() {
		return viewId;
	}
	public void setViewId(long viewId) {
		this.viewId = viewId;
	}
	public String getViewCode() {
		return viewCode;
	}
	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	
	
	

}
