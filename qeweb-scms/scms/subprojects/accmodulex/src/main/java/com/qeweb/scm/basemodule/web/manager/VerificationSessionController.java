package com.qeweb.scm.basemodule.web.manager;


import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;


/**
 * 验证登录信息
 * 
 */
@Controller
@RequestMapping(value="/verification")
public class VerificationSessionController {

	@RequestMapping(value="/verificationSession")
	@ResponseBody
	public Boolean  verification(){
		Boolean flag = false;
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user != null){
			flag = true;
		}
		return flag;
	}
	

}
