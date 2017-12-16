package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.ViewService;

/**
 * 系统管理
 * @author pjjxiajun
 * @date 2015年3月9日
 * @path com.qeweb.scm.baseweb.web.sys.com.qeweb.scm.baseweb.web.sys
 */
@Controller
@RequestMapping(value="/manager")
public class ManagerController {
	
	@Autowired
	private ViewService viewService;
	@Autowired
	private AccountService accountService;
	@Autowired
	HttpSession session;
	
	@LogClass(method="登录系统",module="系统操作")
	@RequestMapping(method=RequestMethod.GET)
	public String manager(Model model){
		//获取当前用户
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//根据组织状态来判断,具有一定的耦合
		if(user.orgRoleType != OrgType.ROLE_TYPE_BUYER.intValue() && user.orgRoleType != OrgType.ROLE_TYPE_BUYER_DEPART.intValue()) {
			if(user.orgActiveStatus==null || user.orgActiveStatus==StatusConstant.STATUS_NO){
				return "redirect:vendor/active";
			}
			if(user.orgConfirmStatus==null || user.orgConfirmStatus==StatusConstant.STATUS_NO){
				return "redirect:vendor/active/waitingConfirm";
			}
		}
		//获取用户菜单
		List<ViewEntity> menuList = viewService.getUserMenu(user);
		model.addAttribute("menuList", menuList);  
		return "back/manager";
	}
	
	@RequestMapping(value="setUserTheme",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setUserTheme(@RequestParam(value="userTheme") String userTheme){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.setTheme(userTheme);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

}
