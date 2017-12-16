package com.qeweb.scm.basemodule.web.common;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.service.BussinessRangeService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;

/**
 * 基础数据公共接口
 * @author pjjxiajun
 * @date 2015年6月27日
 * @path com.qeweb.scm.basemodule.web.common.BaseDataController.java
 */
@Controller
@RequestMapping(value="/public/basedata")
public class BaseDataController {
	
	@Autowired
	private MaterialTypeService materialTypeService;
	@Autowired
	private BussinessRangeService bussinessRangeService;
	
	@RequestMapping(value = "getMaterialTypeByLevel/{level}")
	@ResponseBody
	public List<EasyuiComboBox> getMaterialTypeByLevel(@PathVariable("level")Integer level,@RequestParam(required=false) Long id,ServletRequest request){
		List<MaterialTypeEntity> list = materialTypeService.getMaterialTypeListByLevel(level,id);
		List<EasyuiComboBox> treeList = new LinkedList<EasyuiComboBox>();
		for(MaterialTypeEntity entity : list){
			EasyuiComboBox option = new EasyuiComboBox(entity.getId()+"",entity.getName()+"("+entity.getCode()+")");
			treeList.add(option);
		}
		return treeList;
	}
	
	@RequestMapping(value = "getBussinessRangeTypeList")
	@ResponseBody
	public List<EasyuiComboBox> getBussinessRangeTypeList(@RequestParam(required=false)Long id,HttpServletRequest request){
		List<BussinessRangeEntity> list = bussinessRangeService.getBussinessRangeTypeList(id);
		List<EasyuiComboBox> treeList = new LinkedList<EasyuiComboBox>();
		for(BussinessRangeEntity entity : list){
			EasyuiComboBox option = new EasyuiComboBox(entity.getId()+"",entity.getName());
			treeList.add(option);
		}
		return treeList;
	}

	
}
