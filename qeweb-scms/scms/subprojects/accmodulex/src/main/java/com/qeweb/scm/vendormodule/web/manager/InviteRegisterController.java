/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.qeweb.scm.vendormodule.web.manager;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;
import com.qeweb.scm.vendormodule.service.VendorInviteEmailService;
import com.qeweb.scm.vendormodule.service.VendorService;

/**
 * 用户注册的Controller.
 * 
 * @author lw
 */
@Controller
@RequestMapping(value = "/public/register/inviteReg")
public class InviteRegisterController {

	@Autowired
	private VendorService vendorService;
	@Autowired
	private OrgService orgService;

	@Autowired
	private VendorInviteEmailService vendorInviteEmailService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		return "font/account/inviteRegister";
	}

	@RequestMapping(value = "toRegister/{inviteMailId}/",method = RequestMethod.GET)
	public String register(@PathVariable("inviteMailId")Long inviteMailId,Model model,HttpServletRequest request) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		VendorInviteEmailEntity v = vendorInviteEmailService.getVendorInviteEmail(inviteMailId);
		Timestamp nowDate = DateUtil.getCurrentTimestamp();
		//邀请记录链接是否点击状态修改
		v.setIsCheck(1);
		vendorInviteEmailService.addNewVendorInviteEmail(v);
		//链接超时失效
		if(nowDate.after(v.getExpiryDate())){
			return "redirect:/public/register/inviteReg/retFail";
		}
		model.addAttribute("orgName", v.getVendorName());
		model.addAttribute("email", v.getVendorEmail());
		model.addAttribute("inviteMailId", v.getId());
		model.addAttribute("backherf", basePath);
		model.addAttribute("buyerId", v.getOrgId());
		return "font/account/inviteRegister";
	}
	
	@RequestMapping(value = "retFail",method = RequestMethod.GET)
	@ResponseBody
	public String retFail(){
		return "<script type='text/javascript'>alert('链接已过期');</script>";
	}
}
