package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.entity.BaseAuditEntity;
import com.qeweb.scm.baseline.common.entity.TestOrderEntity;
import com.qeweb.scm.baseline.common.service.CommonAuditService;
import com.qeweb.scm.baseline.common.service.TestOrderService;

@Controller
@RequestMapping(value = "/manager/common/audit")
public class CommonAuditController {
	Map<String, Object> map = new HashMap<String, Object>();
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private CommonAuditService commonAuditService;
	
	@RequestMapping(value="/{entityName}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="entityName") String entityName,
			 Model model,@RequestBody String pks) throws Exception{
		commonAuditService.submit(entityName,pks);
		map.put("success",true);
		map.put("msg","操作成功");
		return map;
	}
	
	@RequestMapping(value="/{entityName}/{auditType}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="entityName") String entityName,
			@PathVariable(value="auditType") Integer auditType, Model model,@RequestBody String pks) throws Exception{
		commonAuditService.doAudit(entityName, auditType,pks);
		map.put("success",true);
		map.put("msg","操作成功");
		return map;
	}

}
