package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.entity.TestOrderEntity;
import com.qeweb.scm.baseline.common.service.TestOrderService;

@Controller
@RequestMapping(value = "/manager/order/order")
public class TestOrderController {
	Map<String, Object> map = new HashMap<String, Object>();
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TestOrderService testOrderService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/test/testOrder";
	}
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<TestOrderEntity> userPage = testOrderService.getAll(pageNumber,pageSize,searchParamMap);
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

}
