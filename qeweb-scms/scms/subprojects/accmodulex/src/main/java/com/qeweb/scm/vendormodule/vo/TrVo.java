package com.qeweb.scm.vendormodule.vo;

import java.util.List;

public class TrVo {
	
	private String id;
	private String name;
	private String type;
	private String fixed;
	private List<String> tdList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFixed() {
		return fixed;
	}
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	public List<String> getTdList() {
		return tdList;
	}
	public void setTdList(List<String> tdList) {
		this.tdList = tdList;
	}
	

}
