package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.MailSetEntity;
import com.qeweb.scm.basemodule.service.MailSetService;

@Controller
@RequestMapping("/manager/basedata/mailSet")
public class MailSetController {
	
	@Autowired
	private MailSetService mailSetService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="邮箱设置")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/mailSetList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> mailSetList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MailSetEntity> page = mailSetService.getMailSetList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@LogClass(method="新增", module="邮箱设置新增")
	@RequestMapping(value = "addNewMailSet",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewMailSet(@Valid MailSetEntity mailSet){
		Map<String,Object> map = new HashMap<String, Object>();
		MailSetEntity m = mailSetService.getMailSetByTempId(mailSet.getMailTemplateId());
		if(m!=null){
			map.put("success", false);
			map.put("msg", "已存在该模板的有效邮箱了!");
			return map;
		}
		mailSetService.addNewMailSet(mailSet);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="修改", module="邮箱设置修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("mailSet") MailSetEntity mailSet) {
		map = new HashMap<String, Object>();
		mailSetService.updateMailSet(mailSet);
		map.put("success", true);
		return map;
	}
	
	
	@LogClass(method="作废", module="邮箱设置作废")
	@RequestMapping(value = "abolishMailSet",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishMailSetList(@RequestBody List<MailSetEntity> mailSetList){
		Map<String,Object> map = new HashMap<String, Object>();
		mailSetService.abolishMailSetList(mailSetList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getMailSet/{id}")
	@ResponseBody
	public MailSetEntity getMailSet(@PathVariable("id") Long id){
		return mailSetService.getMailSet(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindmailSet(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("mailSet", mailSetService.getMailSet(id));
		}
	}
	
}
