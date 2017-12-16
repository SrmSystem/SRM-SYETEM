package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.baseline.common.service.InterfaceMsgService;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Controller
@RequestMapping(value = "/manager/test/test")
public class TestController {
	private Log logger = LogFactory.getLog(TestController.class);
	Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	protected AccountService accountService;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private InterfaceMsgService interfaceService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/test/test";
	}
	
	@RequestMapping(value="/test1",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> test1(){
		Subject subject = SecurityUtils.getSubject();
		UserEntity user = accountService.findUserEntityByLoginName(StringUtils.toUpperCase("SXL"));
		if(user==null) {
			return null;
		}
		OrganizationEntity org = user.getCompany();
		//如果组织为空而ID却不为空
		if(org==null && user.getCompanyId()!=null){
			org = accountService.findOrg(user.getCompanyId());
		}
		//如果组织被禁用，无法登录
		if(org!=null && org.getEnableStatus()!=null && org.getEnableStatus().intValue()!=StatusConstant.STATUS_YES){
			throw new DisabledAccountException("用户已被禁用!");
		}
		//load user data right
		Map<String, Set<Long>> dataPermission = accountService.findUserDataRight(user.getId());
		ShiroUser user1 = new ShiroUser(user.getId(), user.getLoginName(), user.getName(),user.getRoles(),
				org!=null?org.getId():null,
				org!=null?org.getCode():null,
				org!=null?org.getName():null,
				org!=null?org.getRoleType():null,
				org!=null?org.getActiveStatus():null,
				org!=null?org.getEnableStatus():null,				
				org!=null?org.getConfirmStatus():null,
				dataPermission
				);
		subject.runAs(new SimplePrincipalCollection(user1, ""));
		map.put("success", true);
		map.put("msg", "成功");
		return map;
	}

}
