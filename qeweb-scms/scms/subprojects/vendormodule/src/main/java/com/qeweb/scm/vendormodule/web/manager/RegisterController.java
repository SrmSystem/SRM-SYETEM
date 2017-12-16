/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.qeweb.scm.vendormodule.web.manager;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.service.VendorInviteEmailService;
import com.qeweb.scm.vendormodule.service.VendorService;

/**
 * 用户注册的Controller.
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/public/register")
public class RegisterController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private MailSendService mailSendService;
	@Autowired
	private VendorInviteEmailService vendorInviteEmailService;
	@Autowired
	private VendorService vendorService;
	
	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	
	@Autowired
	private OrgService orgService;

	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user!=null && StringUtils.isNotEmpty(user.getName())){
			return "redirect:/manager";
		}
		return "font/account/register";
	}
	@RequestMapping(value="backPassword",method = RequestMethod.GET)
	public String backPassword() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user!=null && StringUtils.isNotEmpty(user.getName())){
			return "redirect:/manager";
		}
		return "font/account/backPassword";
	}
	@RequestMapping(value="successRegister",method = RequestMethod.GET)
	public String successRegister(HttpServletRequest httpServletRequest) {
		httpServletRequest.setAttribute("successBoor","注册成功<br>");
		return "font/account/successRegister";
	}
	@RequestMapping(value="successbackPass",method = RequestMethod.GET)
	public String successbackPass(HttpServletRequest request) {
		
		request.setAttribute("successBoor","已经发送到邮箱");
		return "font/account/successbackPassword";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String register(@Valid UserEntity user,String orgName,String inviteMailId,String orgEmail,String orgPhone,Model model,HttpServletRequest request) {
		//受邀注册后邀请记录中注册状态更改
		if(inviteMailId!=null){
			VendorInviteEmailEntity v = vendorInviteEmailService.getVendorInviteEmail(Long.parseLong(inviteMailId));
			v.setIsRegister(1);
			vendorInviteEmailService.addNewVendorInviteEmail(v);
		}
		OrganizationEntity org = new OrganizationEntity();
		org.setName(orgName);
		org.setEmail(orgEmail);
		org.setPhone(orgPhone);
		org.setActiveStatus(StatusConstant.STATUS_NO);
		org.setConfirmStatus(StatusConstant.STATUS_NO);
		org.setSubmitStatus(StatusConstant.STATUS_NO);
		org.setAuditStatus(StatusConstant.STATUS_NO);
		org.setEnableStatus(StatusConstant.STATUS_YES);
		org.setAuditSn(StatusConstant.STATUS_NO);
		org.setAbolished(Constant.UNDELETE_FLAG);
		String password = user.getPassword();
		vendorService.registerVendorAccount(user,org);
		//模拟登录后的情况
		UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(),password); 
		SecurityUtils.getSubject().login(token);
		model.addAttribute("username", user.getLoginName());
		model.addAttribute("password", password);
		sendRegSuccMail(org,request);
		return "back/account/activeVendor";
	}
	
	/**
	 * 注册成功后发邮件通知
	 */
	public void sendRegSuccMail(OrganizationEntity org,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		MailObject mo = new MailObject();
		mo.toMail = org.getEmail();
		mo.templateName = "defaultTemp";
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorName", org.getName());
		params.put("message", "<a href="+basePath+">点击访问</a>");
		params.put("link", "<a href="+basePath+">"+basePath+"</a>");
		mo.params = params;
		mo.title = "恭喜您账号注册成功";
		mailSendService.send(mo, 1);
	}
	
	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName) {
		if (accountService.findUserEntityByLoginName(loginName) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkOrgName")
	@ResponseBody
	public String checkOrgName(@RequestParam("orgName") String orgName) {
		if (orgService.getByName(orgName) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkOrgEmail")
	@ResponseBody
	public String checkOrgEmail(@RequestParam("orgEmail") String orgEmail) {
		if (orgService.getByEmail(orgEmail) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	@RequestMapping(value = "backPasswordTIJ")
	@ResponseBody
	public String backPasswordTIJ(HttpServletRequest request) {
		String loginName=request.getParameter("loginName");
		String email=request.getParameter("email");
		
		return orgService.backPasswordTIJ(loginName, email);
	}
}
