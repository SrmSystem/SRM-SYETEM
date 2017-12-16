package com.qeweb.scm.vendormodule.web.manager;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity;
import com.qeweb.scm.vendormodule.service.VendorNavTemplateService;

@Controller
@RequestMapping("/manager/vendor/vendorNav")
public class VendorNavController {
	
	@Autowired
	private VendorNavTemplateService vendorNavTemplateService;
	
	@Autowired 
	private OrganizationDao organizationDao;
	
	@Autowired 
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	 

	@LogClass(method="查看", module="设置向导")
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(required=false)Long id,Model model) {
		if(id!=null){
			VendorNavTemplateEntity vendorNavTemplate = vendorNavTemplateService.getVendorNavTemplate(id);
			model.addAttribute("vendorNavTemplate",vendorNavTemplate);
		}
		return "back/vendor/vendorNav";
	}
	
	@RequestMapping(value="/createNavTemplate",method = RequestMethod.POST)
	public String createNavTemplate(@Valid VendorNavTemplateEntity vendorNavTemplate,Model model){
		//保存该模版
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		vendorNavTemplate.setOrgId(user.orgId); 
		Map<String,Object> map = vendorNavTemplateService.createNavTemplate(vendorNavTemplate);
		model.addAttribute("vendorNavTemplate", map.get("vendorNavTemplate"));
		boolean flag = (Boolean) map.get("success");
		if(!flag){
			model.addAttribute("error", map);
			return "back/vendor/vendorNav";
		}
		return "back/vendor/vendorNavPhase";
	}
	
	@RequestMapping(value="/createTemplatePhase",method = RequestMethod.POST)
	public String createTemplatePhase(VendorNavTemplateEntity navTemplate,@RequestParam(value="templateId") Long templateId,Model model){
		//保存阶段关系
		VendorNavTemplateEntity vendorNavTemplate = vendorNavTemplateService.createTemplatePhase(navTemplate.getPhaseList(),templateId);
		model.addAttribute("vendorNavTemplate", vendorNavTemplate);
		return "back/vendor/vendorNavType";
	}
	
	/**
	 * 创建导航模版并跳转到调查表界面
	 * @param navType 导航模版类型
	 * @param templateId 模版ID
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/createNavType")
	public String createNavType(VendorNavTemplateEntity navTemplate,
			Model model,HttpServletRequest request){
		Map<String,Object> map = vendorNavTemplateService.createNavType(navTemplate);
		model.addAttribute("vendorNavTemplate", map.get("vendorNavTemplate"));
		//下一步设定调查表
		return "back/vendor/vendorNavSurvey";
	}
	
	@RequestMapping(value="createTemplateSurvey")
	@ResponseBody
	public Map<String,Object> createTemplateSurvey(@RequestBody List<VendorTemplateSurveyEntity> templateSurveyList){
		Map<String,Object> map = vendorNavTemplateService.createTemplateSurvey(templateSurveyList);
		return map;
	}
	
	@RequestMapping(value="getTemplateSurveyByPhase")
	@ResponseBody
	public Map<String,Object> getTemplateSurveyByPhase(Long id,Model model){
		Map<String,Object> map = vendorNavTemplateService.getTemplateSurveyByPhase(id);
		return map;
	}
	
	@RequestMapping(value = "getOrgCombo",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getOrgCombo(ServletRequest request){
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		List<Long>  listx=buyerOrgPermissionUtil.getBuyerIds();
		List<OrganizationEntity> list=organizationDao.findByOrgTypeAndRoleTypeAndAbolished(0, 0, 0);
		for(int i=0;i < list.size();i++){
			OrganizationEntity bo=list.get(i);
			if(bo!=null){
				if(listx.contains(bo.getId())){
					EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
					couTreeList.add(option);
				}
			}
		}
		return couTreeList;
	}
	
	
}
