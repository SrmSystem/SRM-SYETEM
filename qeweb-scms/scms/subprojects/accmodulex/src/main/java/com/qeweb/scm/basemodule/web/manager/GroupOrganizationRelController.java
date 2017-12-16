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
import com.qeweb.scm.basemodule.entity.GroupOrganizationRelEntity;
import com.qeweb.scm.basemodule.service.GroupOrganizationRelService;

@Controller
@RequestMapping("/manager/basedata/groupOrganizationRel")
public class GroupOrganizationRelController {
	
	@Autowired
	private GroupOrganizationRelService groupOrganizationRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/groupOrganizationRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<GroupOrganizationRelEntity> page = groupOrganizationRelService.getGroupOrganizationRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewGroupOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewGroupOrganizationRel(@Valid GroupOrganizationRelEntity groupOrganizationRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		groupOrganizationRelService.addNewGroupOrganizationRel(groupOrganizationRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteGroupOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteGroupOrganizationRel(@RequestBody List<GroupOrganizationRelEntity> GroupOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		groupOrganizationRelService.deleteGroupOrganizationRel(GroupOrganizationRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getGroupOrganizationRel/{id}")
	@ResponseBody
	public GroupOrganizationRelEntity getGroupOrganizationRel(@PathVariable("id") Long id){
		return groupOrganizationRelService.getGroupOrganizationRel(id);
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<GroupOrganizationRelEntity> GroupOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = groupOrganizationRelService.abolishBatch(GroupOrganizationRelList);
		return map;
	}


}
