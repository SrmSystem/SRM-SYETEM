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
import com.qeweb.scm.basemodule.entity.FactoryInventoryRelEntity;
import com.qeweb.scm.basemodule.service.FactoryInventoryRelService;

@Controller
@RequestMapping("/manager/basedata/factoryInventoryRel")
public class FactoryInventoryRelController {
	
	@Autowired
	private FactoryInventoryRelService factoryInventoryRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/factoryInventoryRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<FactoryInventoryRelEntity> page = factoryInventoryRelService.getFactoryInventoryRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewFactoryInventoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewCompanyFactoryRel(@Valid FactoryInventoryRelEntity factoryInventoryRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		//查询是否已经建立关系
		FactoryInventoryRelEntity cp =  factoryInventoryRelService.findByFactoryIdAndInventoryId(factoryInventoryRel.getFactoryId(),factoryInventoryRel.getInventoryId());
		if( cp != null) {
			map.put("msg", "已存在生效关系！");
			map.put("success", false);
			return map;
		}
		factoryInventoryRelService.addNewFactoryInventoryRel(factoryInventoryRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteFactoryInventoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCompanyFactoryRel(@RequestBody List<FactoryInventoryRelEntity> factoryInventoryRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		factoryInventoryRelService.deleteFactoryInventoryRel(factoryInventoryRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getFactoryInventoryRel/{id}")
	@ResponseBody
	public FactoryInventoryRelEntity getFactoryInventoryRel(@PathVariable("id") Long id){
		return factoryInventoryRelService.getFactoryInventoryRel(id);
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<FactoryInventoryRelEntity> factoryInventoryRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = factoryInventoryRelService.abolishBatch(factoryInventoryRelList);
		return map;
	}


}
