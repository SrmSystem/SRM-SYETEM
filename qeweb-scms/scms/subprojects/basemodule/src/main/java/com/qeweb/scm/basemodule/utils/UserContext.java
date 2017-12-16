package com.qeweb.scm.basemodule.utils;

import org.apache.shiro.SecurityUtils;

import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

public class UserContext {

	public static String getUserTheme(){
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return shiroUser.getTheme()==null?"default":shiroUser.getTheme();
	}
	
}
