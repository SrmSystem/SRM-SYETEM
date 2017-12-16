package com.qeweb.scm.vendormodule.web.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.constants.VendorApplicationProConstant;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.service.VendorSurveyCfgService;
import com.qeweb.scm.vendormodule.service.VendorSurveyService;

/**
 * 作为准入的实际处理类，主要应对controller的覆盖和重写
 * @author pjjxiajun
 * @date 2015年7月18日
 * @path com.qeweb.scm.vendormodule.web.manager.impl.VendorAdmittanceControllerImpl.java
 */
@Component
public class VendorAdmittanceControllerImpl {
	@Autowired
    private VendorBaseInfoService vendorBaseInfoService;
	@Autowired
	private VendorSurveyService vendorSurveyService;
	@Autowired
	private VendorSurveyCfgService vendorSurveyCfgService;
	
	private JsonMapper jsonMapper = new JsonMapper();

	public ModelAndView auditSurveyPage(Long orgId, Model model, HttpServletRequest request) {
		Map<String,Object> map = vendorSurveyService.getSurveyAuditInfo(orgId);
		ModelAndView mv = new ModelAndView("back/vendor/vendorSurveyAudit", map);
		return mv;
	}
	


	public ModelAndView vendorSurveyManagerPage(Model model, ServletRequest request) {
		//获取当前供应商
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = vendorSurveyService.getSurveyManagerInfo(user.orgId);
		ModelAndView modelView = new ModelAndView("back/vendor/vendorSurveyManager",map);
		String attachmentSize = PropertiesUtil.getProperty(VendorApplicationProConstant.SURVEY_ATTACHMENT_SIZE, "0");
		modelView.addObject("attachmentSize", attachmentSize);
		return modelView;
	}

	/**
	 * 供应商维护调查表
	 * @param cfgId 调查表配置ID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getSurvey(Long cfgId) throws Exception {
		Map<String,Object> map = vendorSurveyCfgService.getVendorSurveyByCfg(cfgId);
		ModelAndView modelView = new ModelAndView("back/vendor/component/survey",map);
		modelView.addObject("surveyCfgId", cfgId);
		return modelView;
	}
	
	

	
}
