package com.qeweb.scm.vendormodule.web.manager.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
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

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.vo.VendorAdmittanceTransfer;

@Component
public class VendorBaseInfoControllerImpl {
	
	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	
	private Map<String,Object> map;
	
	
	@RequestMapping(value = "vendorPromote")
	@ResponseBody
	public Map<String,Object> vendorPromote(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList,String auditMsg,Model model,ServletRequest request) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return vendorBaseInfoService.promteList(vendorBaseInfoList,auditMsg,user.getName());
	}
	

	

}
