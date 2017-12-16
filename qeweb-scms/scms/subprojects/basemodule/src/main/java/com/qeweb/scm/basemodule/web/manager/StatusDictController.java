package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.StatusDictEntity;
import com.qeweb.scm.basemodule.service.StatusDictService;
/**
 * 状态Controller
 * @author lw
 * 创建时间：2015年6月8日16:21:31
 * 最后更新时间：2015年6月24日14:13:23
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/database/statusDict")
public class StatusDictController {
	
	@Autowired
	private StatusDictService statusDictService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/statusDictList";
	}
	
	@LogClass(method="查看", module="状态管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> statusDictList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<StatusDictEntity> page = statusDictService.getStatusDictList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewStatusDict",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewStatusDict(@Valid StatusDictEntity statusDict){
		Map<String,Object> map = new HashMap<String, Object>();
		statusDictService.addNewStatusDict(statusDict);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("statusDict") StatusDictEntity statusDict) {
		map = new HashMap<String, Object>();
		statusDictService.updateStatusDict(statusDict);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteStatusDict",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteStatusDictList(@RequestBody List<StatusDictEntity> statusDictList){
		Map<String,Object> map = new HashMap<String, Object>();
		statusDictService.deleteStatusDictList(statusDictList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getStatusDict/{id}")
	@ResponseBody
	public StatusDictEntity getStatusDict(@PathVariable("id") Long id){
		return statusDictService.getStatusDict(id);
	}
	
	@RequestMapping(value = "getStatusDictByType/{type}",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getBussinessRange(@PathVariable("type")String type,ServletRequest request){
		List<StatusDictEntity> list = statusDictService.getStatusDictByType(type);
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			StatusDictEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getStatusValue()+"",bo.getStatusText());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value="getStatusByStatusTypeCombobox/{type}")
	@ResponseBody
	public String getStatusByStatusType(@PathVariable("type")String type){
		return statusDictService.getStatusByType(type);
	}
	
	
	/**
	 * 获取所有状态
	 * @return Map<statusDict.statusType,Map<statusDict.statusValue,StatusDictEntity>>
	 */
	@RequestMapping("getAllStatusDict")
	@ResponseBody
	public Map<String, Map<String, StatusDictEntity>> getAllStatusDict(){
		Map<String, Map<String, StatusDictEntity>> map = new HashMap<String, Map<String,StatusDictEntity>>();
		Map<String, StatusDictEntity> subMap = null;
		Iterable<StatusDictEntity> statusIt = statusDictService.findAll();
		Iterator<StatusDictEntity> it = statusIt.iterator();
		while(it.hasNext()) { 
			StatusDictEntity s = it.next();
			subMap = map.get(s.getStatusType());
			if(subMap == null){
				subMap = new HashMap<String, StatusDictEntity>();
			}
			subMap.put(s.getStatusValue()+"", s);
			map.put(s.getStatusType(), subMap);
		} 
		return map;
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindStatusDict(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("statusDict", statusDictService.getStatusDict(id));
		}
	}
	
	

}
