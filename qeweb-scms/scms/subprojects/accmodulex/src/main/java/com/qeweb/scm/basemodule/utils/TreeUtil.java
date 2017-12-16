package com.qeweb.scm.basemodule.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;

public class TreeUtil {
	
	private final static String OPEN = "open";
	private final static String CLOSED = "closed";

	/**
	 * 将组织转换成easyuitree
	 * @param orgList 组织集合
	 * @return 树集合
	 */
	public static List<EasyuiTree> toEasyuiTree(List<OrganizationEntity> orgList) {
		if(CollectionUtils.isEmpty(orgList)){
			return null;
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(OrganizationEntity org : orgList){
			EasyuiTree treeNode = toEasyuiTreeNode(org);
			nodeList.add(treeNode);
		}
		return nodeList;
	}
	/**
	 * 将区域转换成easyuitree
	 * @param  area 区域集合
	 * @return 树集合
	 */
	public static List<EasyuiTree> toEasyuiTree_area(List<AreaEntity> areaList) {
		if(CollectionUtils.isEmpty(areaList)){
			return null;
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(AreaEntity area : areaList){
			//定义icon 
			String icon = "icon-sitemap_color";
			if(area.getParentId()!=null && area.getParentId()!=0l) {
				icon = "icon-group";
			}
			Map<String,Object> attributes = new HashMap<String,Object>();
			attributes.put("id", area.getId());
//			attributes.put("parentId", area.getParentId());
			EasyuiTree treeNode = new EasyuiTree(area.getId()+"", area.getName(), CLOSED, "true",icon, null, attributes);
			nodeList.add(treeNode);
		}
		return nodeList;
	}
	
	public static EasyuiTree toEasyuiTreeNode(OrganizationEntity org){
		//定义icon 
		String icon = "icon-sitemap_color";
		if(org.getParentId()!=null && org.getParentId()!=0l) {
			icon = "icon-group";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("orgType", org.getOrgType());
		attributes.put("parentId", org.getParentId());
		EasyuiTree treeNode = new EasyuiTree(org.getId()+"", org.getName(), CLOSED, "true",icon, null, attributes);
		return treeNode;
	}

	/**
	 * 
	 * @param materialTypeList
	 * @return
	 */
	public static List<EasyuiTree> toEasyuiTree_materialType(List<MaterialTypeEntity> materialTypeList) {
		if(CollectionUtils.isEmpty(materialTypeList)){
			return new ArrayList<EasyuiTree>();
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(MaterialTypeEntity materialType : materialTypeList){
			EasyuiTree treeNode = toEasyuiTreeNode_materialType(materialType);
			nodeList.add(treeNode);
		}
		return nodeList;
	}
	public static List<EasyuiTree> toEasyuiTree_materialType2(List<MaterialTypeEntity> materialTypeList) {
		if(CollectionUtils.isEmpty(materialTypeList)){
			return new ArrayList<EasyuiTree>();
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(MaterialTypeEntity materialType : materialTypeList){
			EasyuiTree treeNode = toEasyuiTreeNode_materialType2(materialType);
			nodeList.add(treeNode);
		}
		return nodeList;
	}
	private static EasyuiTree toEasyuiTreeNode_materialType2(MaterialTypeEntity materialType) {
		//定义icon 
		String icon = "icon-sitemap_color";
		if(materialType.getParentId()!=null && materialType.getParentId()!=0l) {
			icon = "icon-image";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("remark", materialType.getRemark());
		attributes.put("parentId", materialType.getParentId());
		attributes.put("code", materialType.getCode());
		attributes.put("importance", materialType.getImportance());
		attributes.put("levelLayer", materialType.getLevelLayer());
		attributes.put("needSecondVendor", materialType.getNeedSecondVendor());
		String leafLevel = "3";
		String status = CLOSED;
		if(StringUtils.isNotEmpty(leafLevel) && leafLevel.equals(materialType.getLevelLayer()+"")){
			status = OPEN;
		}
		EasyuiTree treeNode = new EasyuiTree(materialType.getId()+"", materialType.getName(), status, null,icon, null, attributes);
		return treeNode;
	}
	
	private static EasyuiTree toEasyuiTreeNode_materialType(MaterialTypeEntity materialType) {
		//定义icon 
		String icon = "icon-sitemap_color";
		if(materialType.getParentId()!=null && materialType.getParentId()!=0l) {
			icon = "icon-image";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("remark", materialType.getRemark());
		attributes.put("parentId", materialType.getParentId());
		attributes.put("code", materialType.getCode());
		attributes.put("importance", materialType.getImportance());
		attributes.put("levelLayer", materialType.getLevelLayer());
		attributes.put("needSecondVendor", materialType.getNeedSecondVendor());
		String leafLevel = PropertiesUtil.getProperty(ApplicationProConstant.MATERIALTYPE_LEAFLEVEL, "");
		String status = CLOSED;
		if(StringUtils.isNotEmpty(leafLevel) && leafLevel.equals(materialType.getLevelLayer()+"")){
			status = OPEN;
		}
		EasyuiTree treeNode = new EasyuiTree(materialType.getId()+"", materialType.getName(), status, null,icon, null, attributes);
		return treeNode;
	}

	public static List<EasyuiTree> toEasyuiTree_menu(List<ViewEntity> menuList) {
		if(CollectionUtils.isEmpty(menuList)){
			return null;
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(ViewEntity menu : menuList){
			EasyuiTree treeNode = toEasyuiTreeNodeAll(menu);
			nodeList.add(treeNode);
		}
		return nodeList;
	}

	/**
	 * 递归子节点
	 * @param menu 菜单
	 * @return
	 */
	public static EasyuiTree toEasyuiTreeNodeAll(ViewEntity menu) {
		//定义icon 
		String icon = "";
		if(menu.getParentId()!=null && menu.getParentId()!=0l) {
			icon = "";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("parentId", menu.getParentId());
		EasyuiTree treeNode = new EasyuiTree(menu.getId()+"", menu.getViewName(), OPEN, "",icon, null, attributes);
		if(!CollectionUtils.isEmpty(menu.getItemList())){
			List<EasyuiTree> children = new ArrayList<EasyuiTree>();
			for(ViewEntity menuNode : menu.getItemList()){
				EasyuiTree treeItem = toEasyuiTreeNodeAll(menuNode);
				children.add(treeItem);
			}
			treeNode.setChildren(children);
		}
		return treeNode;
	}

	public static EasyuiTree toEasyuiTreeNode(ViewEntity menu) {
		//定义icon 
		String icon = "icon-group";
		if(menu.getParentId()!=null && menu.getParentId()!=0l) {
			icon = "icon-user";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("parentId", menu.getParentId());
		EasyuiTree treeNode = new EasyuiTree(menu.getId()+"", menu.getViewName(), OPEN, "",icon, null, attributes);
		return treeNode;
	}
	
	/**
	 * 批量转换业务范围类型为easyUi树
	 * @param bussinessRangeList 业务范围集合
	 * @param type 业务类型
	 * @return 业务范围树
	 */
	public static List<EasyuiTree> toEasyuiTree_bussiness(List<BussinessRangeEntity> bussinessRangeList, Integer type) {
		if(CollectionUtils.isEmpty(bussinessRangeList)){
			return null;
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(BussinessRangeEntity bussinessRange : bussinessRangeList){
			EasyuiTree treeNode = toEasyuiTree_bussiness(bussinessRange,type);
			nodeList.add(treeNode);
		}
		return nodeList;
	}
	
	/**
	 * 转换业务范围类型为easyUi树节点
	 * @param bussinessRange 业务范围类型
	 * @return
	 */
	private static EasyuiTree toEasyuiTree_bussiness(BussinessRangeEntity bussinessRange,Integer type) {
		
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("remark", bussinessRange.getRemark());
		attributes.put("parentId", bussinessRange.getParentId());
		attributes.put("code", bussinessRange.getCode());
		attributes.put("bussinessType", bussinessRange.getBussinessType());
		//定义icon和状态，如果类型相等，为最后一级，即叶子节点
		String icon = "icon-image";
		String status = OPEN;
		if(bussinessRange.getBussinessType().intValue()!=type.intValue()){
			status = CLOSED;
			icon = "icon-sitemap_color";
		}
		EasyuiTree treeNode = new EasyuiTree(bussinessRange.getId()+"", bussinessRange.getName(), status, null,icon, null, attributes);
		return treeNode;
	}
	
}
