/*package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qeweb.scm.baseline.common.service.UserRunAsService;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/manager/common/runas")
public class RunAsController {

	Map<String, Object> map = new HashMap<String, Object>();
    @Autowired
    private UserRunAsService userRunAsService;

	@Autowired
	protected AccountService accountService;

    @RequestMapping
    public String runasList(Model model) {
    	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	UserEntity u = accountService.getUser(user.id);
        model.addAttribute("fromUsers", userRunAsService.findFromUserIds(user.id));
        model.addAttribute("cuser", u);
        model.addAttribute("toUsers", userRunAsService.findToUserIds(user.id));
        List<UserEntity> allUsers = accountService.getAllUser(0, 10).getContent();
//        allUsers.remove(loginUser);
        model.addAttribute("allUsers", allUsers);
        Subject subject = SecurityUtils.getSubject();
        model.addAttribute("isRunas", subject.isRunAs());
        if(subject.isRunAs()) {
            ShiroUser previousUser =
                    (ShiroUser)subject.getPreviousPrincipals().getPrimaryPrincipal();
            model.addAttribute("previousUsername", previousUser.name);
        }
		return "back/common/runas";
    }

    @RequestMapping("/grant/{toUserId}")
    @ResponseBody
    public Map<String,Object> grant(@PathVariable("toUserId") Long toUserId,
            RedirectAttributes redirectAttributes) throws Exception {
    	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(user.id==toUserId) {
            throw new Exception("自己不能切换到自己的身份");
        }
        userRunAsService.grantRunAs(user.id, toUserId);
        map.put("success", true);
		map.put("msg", "成功");
		return map;
    }
    
    @RequestMapping("/gain/{fromUserId}")
    @ResponseBody
    public Map<String,Object> gain(@PathVariable("fromUserId") Long fromUserId,
            RedirectAttributes redirectAttributes) throws Exception {
    	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(user.id==fromUserId) {
            throw new Exception("自己不能切换到自己的身份");
        }
        userRunAsService.grantRunAs(fromUserId,user.id);
        map.put("success", true);
		map.put("msg", "成功");
		return map;
    }

    @RequestMapping("/revoke/{toUserId}")
    @ResponseBody
    public Map<String,Object> revoke(@PathVariable("toUserId") Long toUserId,
            RedirectAttributes redirectAttributes) {
    	ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        userRunAsService.revokeRunAs(user.id, toUserId);
        map.put("success", true);
		map.put("msg", "成功");
		return map;
    }

    @RequestMapping("/switchTo/{switchToUserId}")
    @ResponseBody
    public Map<String,Object> switchTo(@PathVariable("switchToUserId") Long switchToUserId,
            RedirectAttributes redirectAttributes) throws Exception {
    	ShiroUser _user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Subject subject = SecurityUtils.getSubject();

        UserEntity switchToUser = accountService.getUser(switchToUserId);
        if(_user.id==switchToUserId) {
            throw new Exception("自己不能切换到自己的身份");
        }
        if(switchToUser == null || !userRunAsService.exists(switchToUserId, _user.id)) {
            throw new Exception("对方没有授予您身份，不能切换");
        }
        UserEntity user = accountService.getUser(switchToUserId);
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
		ShiroUser user1 = new ShiroUser(user.getId(), user.getLoginName(), user.getName(),
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

    @RequestMapping("/switchBack")
    @ResponseBody
    public Map<String,Object> switchBack(RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();

        if(subject.isRunAs()) {
           subject.releaseRunAs();
        }
        redirectAttributes.addFlashAttribute("needRefresh", "true");
        map.put("success", true);
		map.put("msg", "成功");
		return map;
    }

}
*/