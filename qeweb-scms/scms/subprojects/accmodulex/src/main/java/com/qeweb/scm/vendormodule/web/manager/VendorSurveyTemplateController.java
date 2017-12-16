package com.qeweb.scm.vendormodule.web.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.dom4j.DocumentException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.vendormodule.entity.VendorSurveyTemplateEntity;
import com.qeweb.scm.vendormodule.service.VendorSurveyTemplateService;

@Controller
@RequestMapping("/manager/vendor/vendorSurveyTemplate")
public class VendorSurveyTemplateController {
	
	@Autowired
	private VendorSurveyTemplateService vendorSurveyTemplateService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="调查表模版管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model,HttpServletRequest request) {
		//初始化模版
	    vendorSurveyTemplateService.initTemplate();
		return "back/vendor/vendorSurveyTemplateList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorSurveyTemplateList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		Page<VendorSurveyTemplateEntity> page = vendorSurveyTemplateService.getVendorSurveyTemplateList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="all",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAll(Model model,ServletRequest request){
		//List<VendorSurveyTemplateEntity> rows = vendorSurveyTemplateService.getAll();
		List<Long> buyerIds = buyerOrgPermissionUtil.getBuyerIds();
		List<VendorSurveyTemplateEntity> rows = vendorSurveyTemplateService.getAllByBuyerId(buyerIds);
		map = new HashMap<String, Object>();
		map.put("rows",rows);
		return map;
	}
	
	@RequestMapping(value = "addNewVendorSurveyTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorSurveyTemplate(@RequestParam(required=false) MultipartFile pathFile,@Valid VendorSurveyTemplateEntity vendorSurveyTemplate) throws IOException{
		Map<String,Object> map = vendorSurveyTemplateService.addNewVendorSurveyTemplate(vendorSurveyTemplate,pathFile);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@RequestParam(required=false) MultipartFile pathFile,@Valid @ModelAttribute("vendorSurveyTemplate") VendorSurveyTemplateEntity vendorSurveyTemplate) throws IOException {
		map = new HashMap<String, Object>();
		map = vendorSurveyTemplateService.updateVendorSurveyTemplate(vendorSurveyTemplate,pathFile);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorSurveyTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorSurveyTemplateList(@RequestBody List<VendorSurveyTemplateEntity> vendorSurveyTemplateList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorSurveyTemplateService.deleteVendorSurveyTemplateList(vendorSurveyTemplateList);
		map.put("success", true);
		return map;
	}
	@RequestMapping(value = "loadingVendorSurveyTemplate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loadingVendorSurveyTemplate(@RequestBody List<VendorSurveyTemplateEntity> vendorSurveyTemplateList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorSurveyTemplateService.loadingVendorSurveyTemplateList(vendorSurveyTemplateList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorSurveyTemplate/{id}")
	@ResponseBody
	public VendorSurveyTemplateEntity getVendorSurveyTemplate(@PathVariable("id") Long id){
		return vendorSurveyTemplateService.getVendorSurveyTemplate(id);
	}
	
	
	/**
	 * 根据调查表ID预览调查表
	 * @param id 调查表ID
	 * @param model 
	 * @return
	 * @throws DocumentException
	 */
	@RequestMapping(value="preview/{id}")
	public String getSurvey(@PathVariable(value="id") Long id,Model model) throws DocumentException{
		Map<String,Object> map = vendorSurveyTemplateService.getSurvey(id);
		VendorSurveyTemplateEntity template = (VendorSurveyTemplateEntity) map.get("template");
		map.remove("template");
		model.addAllAttributes(map);
		model.addAttribute("surveyId", id);
		if(template!=null && "base".equals(template.getCode())){
			return "back/vendor/component/basePreview";
		}
		
		return "back/vendor/component/surveyPreview";
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorSurveyTemplate(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorSurveyTemplate", vendorSurveyTemplateService.getVendorSurveyTemplate(id));
		}
	}
	
	

}
