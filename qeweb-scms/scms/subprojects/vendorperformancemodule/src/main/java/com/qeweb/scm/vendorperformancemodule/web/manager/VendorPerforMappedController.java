package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMappedEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforMappedService;

@Controller
@RequestMapping("/manager/vendor/mapped")
public class VendorPerforMappedController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforMappedService mappedService;
	
	@LogClass(method="查看", module="字符映射设置")
	@RequiresPermissions("perfor:mapped:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/mappedList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> mappedList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforMappedEntity> page = mappedService.getMappedList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@RequiresPermissions("perfor:mapped:add")
	@RequestMapping(value="addmapped",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addmapped(VendorPerforMappedEntity mappedEntity){
		return mappedService.addMapped(mappedEntity);
	}

	@RequiresPermissions("perfor:mapped:upd")
	@RequestMapping(value="updatemapped",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatemapped(VendorPerforMappedEntity mappedEntity){
		return mappedService.updateMapped(mappedEntity);
	}
	
	@RequiresPermissions("perfor:mapped:del")
	@RequestMapping(value = "deletemapped",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletemapped(@RequestBody List<VendorPerforMappedEntity> mappedEntitys){
		return mappedService.deleteMapped(mappedEntitys);
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	@Override
	public void log(Object message) {
		getLogger().log(message); 
	} 
}
