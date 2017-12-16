package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;
import com.qeweb.scm.vendormodule.service.VendorInviteEmailService;
import com.qeweb.scm.vendormodule.service.VendorService;
/**
 * 邀请注册Controller
 * @author lw
 * 创建时间：2015年7月1日15:02:23
 * 最后更新时间：2015年7月3日10:28:26
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/vendor/inviteReg")
public class VendorInviteRegController {
	@Autowired
	private VendorInviteEmailService vendorInviteEmailService;
	
	@Autowired
	private VendorService vendorService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="邀请注册")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorInviteRegList";
	}
	
	/**
	 * 邮件邀请注册记录列表
	 * @param pageNumber 页数
	 * @param pageSize 每页数量
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorInviteRegList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorInviteEmailEntity> page = vendorInviteEmailService.getVendorInviteEmailList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addInviteReg",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addInviteReg(@Valid VendorInviteEmailEntity vendorInviteEmail,HttpServletRequest request){
		map = new HashMap<String, Object>();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		vendorInviteEmail.setIsCheck(0);
		vendorInviteEmail.setIsRegister(0);
		List<VendorInviteEmailEntity> list = vendorInviteEmailService.findByVendorNameAndVendorEmail(vendorInviteEmail.getVendorName(),vendorInviteEmail.getVendorEmail());
		if(list != null && list.size() > 0){
			map.put("success", false);
			map.put("msg", "已邀请过该企业，请勿重复邀请！");  
			return map;
		}
		vendorInviteEmailService.addNewAndSendMail(vendorInviteEmail,basePath);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "getUsername",method = RequestMethod.POST)
	@ResponseBody
	public String getUserName(){
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return shiroUser.name;
	}
	
}
