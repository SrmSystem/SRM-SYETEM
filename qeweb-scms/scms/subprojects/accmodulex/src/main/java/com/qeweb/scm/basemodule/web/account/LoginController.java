package com.qeweb.scm.basemodule.web.account;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.qeweb.scm.basemodule.context.ProjectContextUtil;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value="/public/login")
public class LoginController {
	
	@Autowired
	CookieLocaleResolver resolver;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method=RequestMethod.GET)
	public String login(Model model, HttpServletResponse response){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user!=null && StringUtils.isNotEmpty(user.getName())){
			return "redirect:/manager";
		}
		Map<String,Object> map = setLoginPage(response);
		model.addAllAttributes(map);
		return "font/account/login";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String fail(HttpServletResponse response, Model model){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user!=null && StringUtils.isNotEmpty(user.getName())){
			return "redirect:/manager";
		}
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,userName);
		model.addAllAttributes(setLoginPage(response));
		return "font/account/login";
	}
	
	/**
	 * 设置登录页面标题
	 * @return
	 */
	private Map<String, Object> setLoginPage(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Locale locale = resolver.resolveLocale(request);
		if(locale != null) {
			String language = locale.getLanguage();
			map.put("language", language); 
			if ("zh".equals(language)) {
				resolver.setLocale(request, response, Locale.CHINA);
			} else if ("en".equals(language)) {
				resolver.setLocale(request, response, Locale.ENGLISH);
			} else {
				resolver.setLocale(request, response, Locale.CHINA);
			}
		}
		
		map.put("projectName", ProjectContextUtil.getProjectName()); 
		return map;
	}

}
