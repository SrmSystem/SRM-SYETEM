package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
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

import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;
import com.qeweb.scm.vendormodule.service.VendorTemplatePhaseService;

@Controller
@RequestMapping("/manager/vendor/vendorTemplatePhase")
public class VendorTemplatePhaseController {
	
	@Autowired
	private VendorTemplatePhaseService vendorTemplatePhaseService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorTemplatePhaseList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorTemplatePhaseList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorTemplatePhaseEntity> page = vendorTemplatePhaseService.getVendorTemplatePhaseList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "all",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorTemplatePhaseListAll(Model model,ServletRequest request){
		List<VendorTemplatePhaseEntity> list = vendorTemplatePhaseService.getVendorTemplatePhaseListAll();
		map = new HashMap<String, Object>();
		map.put("rows", list);
		return map;
	}
	@RequestMapping(value = "getByTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getByTemplate(@RequestParam("templateId") Long templateId,
			Model model,ServletRequest request){
		List<VendorTemplatePhaseEntity> list = vendorTemplatePhaseService.getByTemplate(templateId);
		map = new HashMap<String, Object>();
		map.put("rows", list);
		return map;
	}
	
	@RequestMapping(value = "addNewVendorTemplatePhase",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorTemplatePhase(@Valid VendorTemplatePhaseEntity vendorTemplatePhase){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorTemplatePhaseService.addNewVendorTemplatePhase(vendorTemplatePhase);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorTemplatePhase") VendorTemplatePhaseEntity vendorTemplatePhase) {
		map = new HashMap<String, Object>();
		vendorTemplatePhaseService.updateVendorTemplatePhase(vendorTemplatePhase);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorTemplatePhase",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorTemplatePhaseList(@RequestBody List<VendorTemplatePhaseEntity> vendorTemplatePhaseList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorTemplatePhaseService.deleteVendorTemplatePhaseList(vendorTemplatePhaseList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorTemplatePhase/{id}")
	@ResponseBody
	public VendorTemplatePhaseEntity getVendorTemplatePhase(@PathVariable("id") Long id){
		return vendorTemplatePhaseService.getVendorTemplatePhase(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorTemplatePhase(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorTemplatePhase", vendorTemplatePhaseService.getVendorTemplatePhase(id));
		}
	}
}
