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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.entity.CompanyBussinessEntity;
import com.qeweb.scm.basemodule.service.CompanyBussinessService;

@Controller
@RequestMapping("/manager/basedata/companyBussiness")
public class CompanyBussinessController {
	
	@Autowired
	private CompanyBussinessService companyBussinessService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
//		List<UserEntity> users = accountService.getAllUser();
//		model.addAttribute("users", users);
		return "back/basedata/companyBussinessList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> companyBussinessList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<CompanyBussinessEntity> page = companyBussinessService.getCompanyBussinessList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewCompanyBussiness",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewCompanyBussiness(@Valid CompanyBussinessEntity companyBussiness){
		Map<String,Object> map = new HashMap<String, Object>();
		companyBussinessService.addNewCompanyBussiness(companyBussiness);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("companyBussiness") CompanyBussinessEntity companyBussiness) {
		map = new HashMap<String, Object>();
		companyBussinessService.updateCompanyBussiness(companyBussiness);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteCompanyBussiness",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCompanyBussinessList(@RequestBody List<CompanyBussinessEntity> companyBussinessList){
		Map<String,Object> map = new HashMap<String, Object>();
		companyBussinessService.deleteCompanyBussinessList(companyBussinessList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getCompanyBussiness/{id}")
	@ResponseBody
	public CompanyBussinessEntity getCompanyBussiness(@PathVariable("id") Long id){
		return companyBussinessService.getCompanyBussiness(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindCompanyBussiness(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("companyBussiness", companyBussinessService.getCompanyBussiness(id));
		}
	}
	
	

}
