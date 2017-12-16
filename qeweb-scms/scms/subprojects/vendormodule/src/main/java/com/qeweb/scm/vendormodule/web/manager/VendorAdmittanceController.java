package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.mapper.JsonMapper;

import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.service.VendorSurveyCfgService;
import com.qeweb.scm.vendormodule.service.VendorSurveyService;
import com.qeweb.scm.vendormodule.web.manager.impl.VendorAdmittanceControllerImpl;


@Controller
@RequestMapping(value = "/manager/vendor/admittance")
public class VendorAdmittanceController {
	@Autowired
    private VendorBaseInfoService vendorBaseInfoService;
	@Autowired
	private VendorSurveyService vendorSurveyService;
	@Autowired
	private VendorSurveyCfgService vendorSurveyCfgService;
	
	private JsonMapper jsonMapper = new JsonMapper();
	
	/** controller的覆盖实现类 */
	@Autowired
	private VendorAdmittanceControllerImpl vendorAdmittanceControllerImpl;
	
	/**
	 * 供应商维护自己的调查表
	 * @param model 
	 * @param request
	 * @return
	 */
	@RequestMapping("/vendorSurveyManager")
	public ModelAndView vendorSurveyManagerPage(Model model,ServletRequest request){
		//获取当前供应商
		return vendorAdmittanceControllerImpl.vendorSurveyManagerPage(model,request);
	}
	
	@RequestMapping("saveSurvey")
	@ResponseBody
	public Map<String,Object> saveSurvey(@RequestParam(required=false)MultipartFile[] trFiles,
			String surveyBase,Model model,ServletRequest request) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		VendorSurveyBaseEntity survey = jsonMapper.fromJson(surveyBase, VendorSurveyBaseEntity.class);
		Map<String,Object> map = vendorSurveyService.save(survey,trFiles,user.orgId);
		return map;
		
	}
	
	@RequestMapping("saveBaseInfo")
	@ResponseBody
	public Map<String,Object> saveBaseInfo(@RequestParam(required=false)MultipartFile[] files,String baseInfo,Model model,ServletRequest request){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		VendorBaseInfoEntity survey = jsonMapper.fromJson(baseInfo, VendorBaseInfoEntity.class);
		Map<String,Object> map = vendorSurveyService.save(survey,user.orgId);
		return map;
	}
	
	@RequestMapping("submitVendorSurvey")
	@ResponseBody
	public Map<String,Object> submitSurvey(@RequestParam(required=false)MultipartFile[] trFiles,
			String surveyBase,Long surveyCfgId,Model model,ServletRequest request) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		VendorSurveyBaseEntity survey = jsonMapper.fromJson(surveyBase, VendorSurveyBaseEntity.class);
		Map<String,Object> map = vendorSurveyService.submit(survey,trFiles,surveyCfgId,user.orgId);
		return map;
		
	}
	
	@RequestMapping("submitVendorBase")
	@ResponseBody
	public Map<String,Object> submitBaseInfo(@RequestParam(required=false)MultipartFile[] files,
			String baseInfo,Model model,ServletRequest request){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		VendorBaseInfoEntity survey = jsonMapper.fromJson(baseInfo, VendorBaseInfoEntity.class);
		Map<String,Object> map = vendorSurveyService.submit(survey,user.orgId);
		return map;
	}
	
	/**
	 * 进入调查表审核页面
	 * @param id 供应商的baseInfo的ID
	 */
	@RequestMapping("auditSurveyPage/{orgId}")
	public ModelAndView auditSurveyPage(@PathVariable("orgId") Long orgId,Model model,HttpServletRequest request){
		ModelAndView mv = vendorAdmittanceControllerImpl.auditSurveyPage(orgId,model,request);
		return mv;
	}
	
	/**
	 * 进入基本调查表查看页面
	 * @param id 供应商的baseInfo的ID
	 */
	@RequestMapping("viewBaseSurveyPage/{id}")
	public String viewBaseSurveyPage(@PathVariable("id") Long id,Model model,HttpServletRequest request){
		Map<String,Object> surveyInfo = vendorSurveyService.getBaseSurveyInfo(id);
		model.addAttribute("vendor", surveyInfo.get("vendor"));
		model.addAttribute("vendorBU", surveyInfo.get("vendorBU"));
		return "back/vendor/component/baseView";
	}
	
	
	@RequestMapping("auditSurvey")
	@ResponseBody
	public Map<String,Object> auditSurvey(@ModelAttribute("vendorSurveyCfg") VendorSurveyCfgEntity surveyCfg){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = vendorSurveyService.auditSurvey(surveyCfg,user.name);
		return map;
	}
	
	/**
	 * 获取基本信息调查表的历史
	 * @param currentId 调查表当前ID
	 * @param orgId 供应商组织ID
	 */
	@RequestMapping(value="getBaseInfoHis",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorBaseInfoHisList(@RequestParam(value="currentId") Long currentId,
			@RequestParam(value="ctId") Long orgId
			){
		List<VendorBaseInfoEntity> list = vendorBaseInfoService.getVendorBaseInfoHisList(orgId,currentId);
		Map<String,Object> map = new HashMap<String, Object>();
		map = new HashMap<String, Object>();
		map.put("rows",list);
		return map;
	}
	
	/**
	 * 获取调查表历史
	 * @param currentId 调查表当前ID
	 * @param cfgId 供应商调查表配置ID
	 */
	@RequestMapping(value="getSurveyInfoHis",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorSurveyInfoHisList(@RequestParam(value="currentId") Long currentId,
			@RequestParam(value="ctId") Long cfgId
			){
		List<VendorSurveyBaseEntity> list = vendorSurveyService.getVendorSurveyInfoHisList(cfgId,currentId);
		Map<String,Object> map = new HashMap<String, Object>();
		map = new HashMap<String, Object>();
		map.put("rows",list);
		return map;
	}
	
	/**
	 * 获取基本信息调查表的历史-审核时的历史
	 * @param currentId 调查表当前ID
	 * @param orgId 供应商组织ID
	 */
	@RequestMapping(value="getBaseInfoAuditHis",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorBaseInfoAuditHisList(@RequestParam(value="currentId") Long currentId,
			@RequestParam(value="ctId") Long orgId
			){
		List<VendorBaseInfoEntity> list = vendorBaseInfoService.getVendorBaseInfoAuditHisList(orgId,currentId);
		Map<String,Object> map = new HashMap<String, Object>();
		map = new HashMap<String, Object>();
		map.put("rows",list);
		return map;
	}
	
	/**
	 * 获取调查表历史-审核时的历史
	 * @param currentId 调查表当前ID
	 * @param cfgId 供应商调查表配置ID
	 */
	@RequestMapping(value="getSurveyInfoAuditHis",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorSurveyInfoAuditHisList(@RequestParam(value="currentId") Long currentId,
			@RequestParam(value="ctId") Long cfgId
			){
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorSurveyBaseEntity> list = vendorSurveyService.getVendorSurveyInfoAuditHisList(cfgId,currentId);
		map = new HashMap<String, Object>();
		map.put("rows",list);
		return map;
	}
	
	/**
	 * 根据模版配置获取供应商模版,供应商维护调查表
	 * @param id 模版配置ID
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="getSurvey/{id}")
	public ModelAndView getSurvey(@PathVariable(value="id") Long cfgId,Model model) throws Exception{
		return vendorAdmittanceControllerImpl.getSurvey(cfgId);
	}
	
	/**
	 * 查看调查表信息
	 * @param id 调查表信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="getSurveyView/{id}")
	public String getSurveyView(@PathVariable(value="id") Long id,Model model) throws Exception{
		Map<String,Object> map = vendorSurveyCfgService.getVendorSurvey(id);
		model.addAttribute("ctList", map.get("ctList"));
		model.addAttribute("surveyBase", map.get("surveyBase"));
		model.addAttribute("templateId", map.get("templateId"));
		model.addAttribute("templatePath", map.get("templatePath"));
		model.addAttribute("templateCode", map.get("templateCode"));
		return "back/vendor/component/surveyView";
	}
	
	/**
	 * 获取调查表的审核信息，并进入审核页面
	 * @param cfgId 模版配置ID
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="getSurveyAudit/{id}")
	public String getSurveyAudit(@PathVariable(value="id") Long cfgId,Model model) throws Exception{
		Map<String,Object> map = vendorSurveyCfgService.getVendorSurveyAuditByCfg(cfgId);
		model.addAttribute("ctList", map.get("ctList"));
		model.addAttribute("surveyBase", map.get("surveyBase"));
		model.addAttribute("surveyCfgId", cfgId);
		model.addAttribute("vendorSurveyCfg", map.get("vendorSurveyCfg"));
		model.addAttribute("templateId", map.get("templateId"));
		model.addAttribute("templatePath", map.get("templatePath"));
		model.addAttribute("templateCode", map.get("templateCode"));
		return "back/vendor/component/surveyAudit";
	}
	
	@ModelAttribute
	public void bindVendorSurveyCfg(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model){
		if(id.intValue()!=-1){
			model.addAttribute("vendorSurveyCfg", vendorSurveyCfgService.getVendorSurveyCfg(id));
		}
	}
	
}
