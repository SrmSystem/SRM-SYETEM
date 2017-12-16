package com.qeweb.scm.basemodule.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "qeweb_view")
public class ViewEntity extends BaseEntity {
	
	private String viewCode;
	private String viewName;
	private String viewNameZH;	//菜单中文名称
	private String viewIcon;
	private String viewUrl;
	private String permission;	//权限标实
	 
	/** 视图类型（0-菜单，1-非菜单） */
	private int viewType;
	private Long parentId;
	private int isLeaf;
	/** 菜单的顺序号，排序 */
    private Integer menuSn;
    private String remark;
    
    private Long _parentId;
    private Long beforeId;
    
    private List<ViewEntity> itemList;
    
    private String isVendor;//是否供应商 N采购商 Y供应商
    
    @Transient
    public Long get_parentId() {
		return getParentId();
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	@Transient
	public Long getBeforeId() {
		return beforeId;
	}
	public void setBeforeId(Long beforeId) {
		this.beforeId = beforeId;
	}
	public String getViewCode() {
		return viewCode;
	}
	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	@Column(name="view_name_zh")
	public String getViewNameZH() {
		return viewNameZH;
	}
	public void setViewNameZH(String viewNameZH) {
		this.viewNameZH = viewNameZH;
	}
	public String getViewIcon() {
		return viewIcon;
	}
	public void setViewIcon(String viewIcon) {
		this.viewIcon = viewIcon;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	public int getViewType() {
		return viewType;
	}
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public int getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Integer getMenuSn() {
		return menuSn;
	}
	public void setMenuSn(Integer menuSn) {
		this.menuSn = menuSn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Transient
	public List<ViewEntity> getItemList() {
		return itemList;
	}
	public void setItemList(List<ViewEntity> itemList) {
		this.itemList = itemList;
	}
	public String getIsVendor() {
		return isVendor;
	}
	public void setIsVendor(String isVendor) {
		this.isVendor = isVendor;
	}

	
	
	 
}
