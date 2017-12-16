package com.qeweb.scm.basemodule.web.common;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * 切换系统语言
 * 
 * @author ALEX
 *
 */
@Controller
@RequestMapping(value = "/public/common/language")
public class LanguageController {

	@Autowired
	CookieLocaleResolver resolver;

//	@Autowired 
//	SessionLocaleResolver resolver;
	
	/**
	 * 语言切换
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String languageChange(@RequestParam("language") String language, HttpServletRequest request, HttpServletResponse response) {
		if (language == null || "".equals(language)) {
			return "redirect:/public/login";
		} else {
			if (language.equals("zh")) {
				resolver.setLocale(request, response, Locale.CHINA);
			} else if (language.equals("en")) {
				resolver.setLocale(request, response, Locale.ENGLISH);
			} else {
				resolver.setLocale(request, response, Locale.CHINA);
			}
		}
		return "redirect:/public/login";
	}
	
	@RequestMapping(value= "/{language}", method = RequestMethod.GET)
	public String language(HttpServletRequest request, HttpServletResponse response, @PathVariable("language") String language) {
		if (language == null || "".equals(language)) {
			return "redirect:/public/login";
		} else {
			if (language.equals("zh")) {
				resolver.setLocale(request, response, Locale.CHINA);
			} else if (language.equals("en")) {
				resolver.setLocale(request, response, Locale.ENGLISH);
			} else {
				resolver.setLocale(request, response, Locale.CHINA);
			}
		}
		return "redirect:/public/login";
	}

}
