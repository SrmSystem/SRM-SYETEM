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
import com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;
import com.qeweb.scm.vendormodule.service.VendorNavTemplateService;

@Controller
@RequestMapping("/manager/vendor/vendorNavTemplate")
public class VendorNavTemplateController {
	
	@Autowired
	private VendorNavTemplateService vendorNavTemplateService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="向导模版管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorNavTemplateList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorNavTemplateList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorNavTemplateEntity> page = vendorNavTemplateService.getVendorNavTemplateList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="all",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAll(Model model,ServletRequest request){
		List<VendorNavTemplateEntity> rows = vendorNavTemplateService.getAll();
		map = new HashMap<String, Object>();
		map.put("rows",rows);
		return map;
	}
	
	@RequestMapping(value = "addNewVendorNavTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorNavTemplate(@Valid VendorNavTemplateEntity vendorNavTemplate){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorNavTemplateService.addNewVendorNavTemplate(vendorNavTemplate);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorNavTemplate") VendorNavTemplateEntity vendorNavTemplate) {
		map = new HashMap<String, Object>();
		vendorNavTemplateService.updateVendorNavTemplate(vendorNavTemplate);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorNavTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorNavTemplateList(@RequestBody List<VendorNavTemplateEntity> vendorNavTemplateList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorNavTemplateService.deleteVendorNavTemplateList(vendorNavTemplateList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorNavTemplate/{id}")
	@ResponseBody
	public VendorNavTemplateEntity getVendorNavTemplate(@PathVariable("id") Long id){
		return vendorNavTemplateService.getVendorNavTemplate(id);
	}
	
	
	/**
	 * 重设模版的阶段和调查表，暂时不允许删除，只允许重设阶段和调查表
	 * @param templatePhaseList
	 * @return 重设结果
	 */
	@RequestMapping("resetTemplate")
	@ResponseBody
	public Map<String,Object> resetTemplate(@RequestBody List<VendorTemplatePhaseEntity> templatePhaseList){
		Map<String,Object> map =vendorNavTemplateService.reset(templatePhaseList);
		return map;
	}
	

	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorNavTemplate(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorNavTemplate", vendorNavTemplateService.getVendorNavTemplate(id));
		}
	}
	
	

}
