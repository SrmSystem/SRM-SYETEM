package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.LogEntity;
import com.qeweb.scm.basemodule.service.LogService;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;

@Controller
@RequestMapping("/manager/basedata/log")
public class LogController {

	@Autowired
	private LogService logservice;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="日志管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/logList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> mailSetList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		sort="createTime";
		order="desc";
//		Page<LogEntity> page = logservice.getLogList(pageNumber,pageSize,searchParamMap);
		Page<LogEntity> page = logservice.getLogList(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
}
