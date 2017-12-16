package com.qeweb.scm.basemodule.web.common;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.service.AreaService;
/**
 * 区域Controller
 * @author lw
 * 创建时间：2015年6月3日10:39:10
 * 最后更新时间：2015年6月3日14:57:16
 * 最后更新人：lw
 */
@Controller
@RequestMapping(value="/public/common/areaselect")
public class AreaSelectController {
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value = "getAreaSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getAreaSelect(Long id,ServletRequest request){
		List<AreaEntity> areaList = areaService.getAreaByParentId(id);
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		if (areaList != null && areaList.size() > 0){
			for(int i=0;i < areaList.size();i++){
				AreaEntity bo = areaList.get(i);
				EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
				couTreeList.add(option);
			}
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "getAreaSelectByLevel/{level}")
	@ResponseBody
	public List<EasyuiComboBox> getAreaSelectByLevel(@PathVariable("level")Integer level,ServletRequest request){
		List<AreaEntity> areaList = areaService.getAreaByLevel(level);
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < areaList.size();i++){
			AreaEntity bo = areaList.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
}
