package com.qeweb.scm.basemodule.web.common;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.service.AreaService;
import com.qeweb.scm.basemodule.service.BussinessRangeService;
import com.qeweb.scm.basemodule.service.MaterialService;

/**
 * 物料或者主要产品选择
 * @author pjjxiajun
 * @date 2015年6月24日
 * @path com.qeweb.scm.basemodule.web.common.MaterialSelectController.java
 */
@Controller
@RequestMapping(value="/public/common/materialselect")
public class MaterialSelectController {
	
	@Autowired
	private MaterialService materialService;
	@Autowired
	private BussinessRangeService bussinessRangeService;
	
	@RequestMapping(value = "getAll",method = RequestMethod.POST)
	@ResponseBody
	public List<MaterialEntity> getAll(ServletRequest request){
		List<MaterialEntity> matList = materialService.getAll();
		return matList;
	}
	
	@RequestMapping(value = "getOther")
	@ResponseBody
	public List<EasyuiComboBox> getOther(@RequestParam(required=false)String other,HttpServletRequest request){
		List<BussinessRangeEntity> list = bussinessRangeService.getOther(other);
		List<EasyuiComboBox> treeList = new LinkedList<EasyuiComboBox>();
		for(BussinessRangeEntity entity : list){
			EasyuiComboBox option = new EasyuiComboBox(entity.getId()+"",entity.getName());
			treeList.add(option);
		}
		return treeList;
	}
	
}
