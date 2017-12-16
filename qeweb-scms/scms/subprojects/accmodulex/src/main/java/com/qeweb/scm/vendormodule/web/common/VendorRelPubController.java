package com.qeweb.scm.vendormodule.web.common;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMaterialTypeRelService;

/**
 * 供应商关系公开接口
 * @author pjjxiajun
 * @date 2015年7月28日
 * @path com.qeweb.scm.vendormodule.web.common.VendorRelPubController.java
 */
@Controller
@RequestMapping(value="/public/vendor/rel")
public class VendorRelPubController {
	
	@Autowired
	private VendorMaterialTypeRelService service;
	
	@RequestMapping(value = "getMaterialType/{orgId}")
	@ResponseBody
	public List<EasyuiComboBox> getMaterialTypeByLevel(@PathVariable("orgId")Long orgId,@RequestParam(required=false)Long id, ServletRequest request){
		List<VendorMaterialTypeRelEntity> list = service.getListByOrgId(orgId,id);
		List<EasyuiComboBox> treeList = new LinkedList<EasyuiComboBox>();
		for(VendorMaterialTypeRelEntity entity : list){
			EasyuiComboBox option = new EasyuiComboBox(entity.getId()+"",entity.getMaterialTypeName());
			treeList.add(option);
		}
		return treeList;
	}

	

	@RequestMapping(value = "getYear")
	@ResponseBody
	public List<EasyuiComboBox> getYear(HttpServletRequest request){
		String id=request.getParameter("id");
		List<String> list = service.getYear(id);
		List<EasyuiComboBox> treeList = new LinkedList<EasyuiComboBox>();
		for(String s : list){
			EasyuiComboBox option = new EasyuiComboBox(s+"",s);
			treeList.add(option);
		}
		return treeList;
	}
}
