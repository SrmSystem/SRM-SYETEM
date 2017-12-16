package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.entity.InterfaceMsgEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgItemEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgLogEntity;
import com.qeweb.scm.baseline.common.service.InterfaceMsgService;

@Controller
@RequestMapping(value = "/manager/interface/msg")
public class InterfaceMsgController {
	private Log logger = LogFactory.getLog(InterfaceMsgController.class);
	Map<String, Object> map = new HashMap<String, Object>();
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private InterfaceMsgService interfaceService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/interface/msgList";
	}
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<InterfaceMsgEntity> userPage = interfaceService.getAll(pageNumber,pageSize,searchParamMap);
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="/showInfos/{pk}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showInfos(@PathVariable(value="pk") int pk,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<InterfaceMsgLogEntity> userPage = interfaceService.getLogs(pk,pageNumber,pageSize,searchParamMap);
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="/showItems/{pk}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showItems(@PathVariable(value="pk") int pk,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<InterfaceMsgItemEntity> userPage = interfaceService.getItems(pk,pageNumber,pageSize,searchParamMap);
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

}
