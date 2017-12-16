package com.qeweb.scm.vendormodule.web.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.service.VendorInviteEmailService;
import com.qeweb.scm.vendormodule.service.VendorService;

@Controller
@RequestMapping(value="/vendor/active")
public class ActiveVendorController {
	@Autowired
	private MailSendService mailSendService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	@Autowired
	private VendorInviteEmailService vendorInviteEmailService ;
	
	@RequestMapping(method = RequestMethod.GET)
	public String active(Model model){
		return "back/account/activeVendor";
	}
	
	@RequestMapping(value="waitingConfirm",method = RequestMethod.GET)
	public String waitingConfirm(Model model){
		return "back/account/successRegister";
	}
	

	/**
	 * 激活后跳转的页面
	 * @return 页面
	 */
	@RequestMapping("activeVendor")
	public String activeVendor(VendorBaseInfoEntity vendorBaseInfo,HttpServletRequest httpServletRequest,Model model){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	    vendorService.activeVendor(vendorBaseInfo,user.id,user.orgId);
	    user.setOrgActiveStatus(StatusConstant.STATUS_YES);
		httpServletRequest.setAttribute("successBoor","注册成功<br>");
		OrganizationEntity org = orgService.getOrg(user.orgId);
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		List<VendorInviteEmailEntity> list = vendorInviteEmailService.getVendorInviteEmailByName(org.getName());
		
		model.addAttribute("isInvite",false);
		if(list!=null&&list.size()>0){
			String basePath = httpServletRequest.getScheme()+"://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort()+httpServletRequest.getContextPath();
			List<VendorBaseInfoEntity> vendorBaseInfoList= new ArrayList<VendorBaseInfoEntity>();
			VendorBaseInfoEntity v = vendorBaseInfoService.getVendorBaseInfoByOrg(org.getId());
			vendorBaseInfoList.add(v);
			vendorBaseInfoService.confirm(vendorBaseInfoList, basePath);
			model.addAttribute("isInvite",true);
			sendRegSuccMailInvite(org,httpServletRequest);
		}else{
			sendRegSuccMail(org,httpServletRequest);
		}
		return "back/account/successRegister";
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
		params.put("curr_date", new Date().toString());
		mo.params = params;
		mo.title = "恭喜您账号注册成功";
		
		mailSendService.sendExForInviteReg(mo, 1);
	}
	/**
	 * 邀请注册成功后发邮件通知
	 */
	public void sendRegSuccMailInvite(OrganizationEntity org,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		MailObject mo = new MailObject();
		mo.toMail = org.getEmail();
		mo.templateName = "defaultTemp";
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorName", org.getName());
		params.put("message", "<a href="+basePath+">点击访问</a>");
		params.put("link", "<a href="+basePath+">"+basePath+"</a>");
		params.put("curr_date", new Date().toString());
		mo.params = params;
		mo.title = "恭喜您账号注册成功";
		mailSendService.sendExForInviteReg(mo, 2);
	}
}
