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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFAllocationEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforFAllocationService;

@Controller
@RequestMapping("/manager/vendor/fAllocation")
public class VendorPerforFAllocationController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforFAllocationService fAllocationService;
	
	@LogClass(method="查看", module="公式元素管理")
	@RequiresPermissions("perfor:falloc:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/fAllocationList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> fAllocationList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforFAllocationEntity> page = fAllocationService.getFAllocationList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@RequiresPermissions("perfor:falloc:add")
	@RequestMapping(value="addFAllocation",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addFAllocation(VendorPerforFAllocationEntity fAllocationEntity){
		return fAllocationService.addFAllocation(fAllocationEntity);
	}
	
	@RequestMapping(value="updateFAllocationStart",method = RequestMethod.POST)
	@ResponseBody
	public String updateFAllocationStart(VendorPerforFAllocationEntity fAllocationEntity){
		return fAllocationService.updateFAllocationStart(fAllocationEntity);
	}
	@RequiresPermissions("perfor:falloc:upd")
	@RequestMapping(value="updateFAllocation",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateFAllocation(VendorPerforFAllocationEntity fAllocationEntity){
		return fAllocationService.updateFAllocation(fAllocationEntity);
	}
	
	@RequiresPermissions("perfor:falloc:del")
	@RequestMapping(value = "deleteFAllocation",method = RequestMethod.POST)
	@ResponseBody
	public String deleteFAllocation(@RequestBody List<VendorPerforFAllocationEntity> fAllocationEntitys){
		return fAllocationService.deleteFAllocation(fAllocationEntitys);
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
