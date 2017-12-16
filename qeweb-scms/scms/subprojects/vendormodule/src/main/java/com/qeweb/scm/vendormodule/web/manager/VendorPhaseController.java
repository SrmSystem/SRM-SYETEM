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

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.service.VendorPhaseService;

@Controller
@RequestMapping("/manager/vendor/vendorPhase")
public class VendorPhaseController {
	
	@Autowired
	private VendorPhaseService vendorPhaseService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="供应商阶段设置")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorPhaseList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorPhaseList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPhaseEntity> page = vendorPhaseService.getVendorPhaseList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "all",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorPhaseListAll(Model model,ServletRequest request){
		List<VendorPhaseEntity> list = vendorPhaseService.getVendorPhaseListAll();
		map = new HashMap<String, Object>();
		map.put("rows", list);
		return map;
	}
	
	@RequestMapping(value = "addNewVendorPhase",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorPhase(@Valid VendorPhaseEntity vendorPhase){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorPhaseService.addNewVendorPhase(vendorPhase);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorPhase") VendorPhaseEntity vendorPhase) {
		map = new HashMap<String, Object>();
		vendorPhaseService.updateVendorPhase(vendorPhase);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorPhase",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorPhaseList(@RequestBody List<VendorPhaseEntity> vendorPhaseList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorPhaseService.deleteVendorPhaseList(vendorPhaseList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorPhase/{id}")
	@ResponseBody
	public VendorPhaseEntity getVendorPhase(@PathVariable("id") Long id){
		return vendorPhaseService.getVendorPhase(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorPhase(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorPhase", vendorPhaseService.getVendorPhase(id));
		}
	}
}
