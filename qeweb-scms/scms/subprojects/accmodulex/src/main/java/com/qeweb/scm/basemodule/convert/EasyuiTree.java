package com.qeweb.scm.basemodule.convert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * easyui的树结构对象
 * @author pjjxiajun
 * @date 2015年3月17日
 * @path com.qeweb.scm.baseweb.web.convert.EasyuiTree.java
 */
public class EasyuiTree implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3033170337872876151L;
	private String id;
	private String text;
	private String state;
	private String checked;
	private String iconCls;
	private List<EasyuiTree> children;
	private Map<String,Object> attributes;
	
	private String isParent;//ztree，同state
	private String chkDisabled;//ztree，checkbox是否有效
	private String nocheck;//ztree，checkbox是否显示
	
	/**
	 * 构造函数
	 * @param id 节点ID
	 * @param text 节点文本
	 * @param state 节点状态(closed,open)
	 * @param checked 是否选中
	 * @param iconCls 图标
	 * @param children 子节点集合
	 * @param attributes 属性集合
	 */
	public EasyuiTree(String id,String text,String state,String checked,String iconCls,List<EasyuiTree> children,Map<String,Object> attributes){
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checked;
		this.children = children;
		this.attributes = attributes;
		this.iconCls = iconCls;
		if("open".equals(state)){
			this.isParent = "false";
			this.chkDisabled = "false";
			this.nocheck = "false";
		}else{
			this.isParent = "true";
			this.chkDisabled = "true";
			this.nocheck = "true";
		}
	}
	public EasyuiTree(String id,String text,String state,String checked,List<EasyuiTree> children,Map<String,Object> attributes){
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checked;
		this.children = children;
		this.attributes = attributes;
		if("open".equals(state)){
			this.isParent = "false";
			this.chkDisabled = "false";
			this.nocheck = "false";
		}else{
			this.isParent = "true";
			this.chkDisabled = "true";
			this.nocheck = "true";
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public List<EasyuiTree> getChildren() {
		return children;
	}
	public void setChildren(List<EasyuiTree> children) {
		this.children = children;
	}


	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getChkDisabled() {
		return chkDisabled;
	}
	public void setChkDisabled(String chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	public String getNocheck() {
		return nocheck;
	}
	public void setNocheck(String nocheck) {
		this.nocheck = nocheck;
	}
	
	
	
	
	
}
