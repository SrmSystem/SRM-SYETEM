package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;
import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.service.GroupFactoryRelService;

@Controller
@RequestMapping("/manager/basedata/groupFactoryRel")
public class GroupFactoryRelController {
	
	@Autowired
	private GroupFactoryRelService groupFactoryRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/groupFactoryRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<GroupFactoryRelEntity> page = groupFactoryRelService.getGroupFactoryRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewGroupFactoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewGroupFactoryRel(@Valid GroupFactoryRelEntity groupFactoryRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		groupFactoryRelService.addNewGroupFactoryRel(groupFactoryRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteGroupFactoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteGroupFactoryRel(@RequestBody List<GroupFactoryRelEntity> groupFactoryRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		groupFactoryRelService.deleteGroupFactoryRel(groupFactoryRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getGroupFactoryRel/{id}")
	@ResponseBody
	public GroupFactoryRelEntity getGroupFactoryRel(@PathVariable("id") Long id){
		return groupFactoryRelService.getGroupFactoryRel(id);
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<GroupFactoryRelEntity> groupFactoryRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = groupFactoryRelService.abolishBatch(groupFactoryRelList);
		return map;
	}


}
