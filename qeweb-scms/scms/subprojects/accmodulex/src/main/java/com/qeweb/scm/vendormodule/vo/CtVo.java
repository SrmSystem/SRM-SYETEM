package com.qeweb.scm.vendormodule.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模版容器一级的值对象
 * @author pjjxiajun
 * @date 2015年6月1日
 * @path com.qeweb.scm.vendormodule.vo.CtVo.java
 */
public class CtVo {
	public final static String TYPE_F = "0";
	public final static String TYPE_T = "1";
	
	private String id;//id
	private String type;//类型，0：Form.1:table
	private String title;//标题
	
	List<ColVo> colList = new ArrayList<ColVo>();
	List<ColVo> tfList = new ArrayList<ColVo>();
	List<TrVo> trList = new ArrayList<TrVo>();
	
	public CtVo(String id,String type,String title){
		this.id = id;
		this.type = type;
		this.title = title;
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public List<ColVo> getColList() {
		return colList;
	}


	public void setColList(List<ColVo> colList) {
		this.colList = colList;
	}


	public List<TrVo> getTrList() {
		return trList;
	}


	public void setTrList(List<TrVo> trList) {
		this.trList = trList;
	}


	public List<ColVo> getTfList() {
		return tfList;
	}


	public void setTfList(List<ColVo> tfList) {
		this.tfList = tfList;
	}
	
	
	
	
	
	
	

}
