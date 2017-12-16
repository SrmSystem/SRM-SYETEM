package com.qeweb.scm.basemodule.convert;


public class EasyuiComboBox {
	
	private String value;
	private String text;
	private Boolean selected;
	
	public EasyuiComboBox(String value,String text){
		this.value = value;
		this.text = text;
	}
	public EasyuiComboBox(String value,String text,Boolean selected){
		this.value = value;
		this.text = text;
		this.selected = selected;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	

	

}
