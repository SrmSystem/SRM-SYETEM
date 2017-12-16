package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforVendorMatEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforVendorMatService;

@Controller
@RequestMapping("/manager/vendor/vendorPerforVendorMat")
public class VendorPerforVendorMatController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforVendorMatService vendorMatService;
	
	@LogClass(method="查看", module="供应商物料管理")
	@RequiresPermissions("perfor:vendormat:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/vendorMatList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pageList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforVendorMatEntity> page = vendorMatService.getPage(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequiresPermissions(value={"perfor:vendormat:add", "perfor:vendormat:upd"}, logical=Logical.OR)
	@RequestMapping(value="addUpdateVendorMat",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addUpdateVendorMat(VendorPerforVendorMatEntity vendorMatEntity){
		return vendorMatService.addUpdateVendorMat(vendorMatEntity);
	}
	
	@RequiresPermissions("perfor:vendormat:using")
	@RequestMapping(value = "releaseVendorMat",method = RequestMethod.POST)
	@ResponseBody
	public String releaseVendorMat(@RequestBody List<VendorPerforVendorMatEntity> vendorMatEntitys){
		return vendorMatService.releaseVendorMat(vendorMatEntitys);
	}
	
	@RequiresPermissions("perfor:vendormat:cancel")
	@RequestMapping(value = "delsVendorMat",method = RequestMethod.POST)
	@ResponseBody
	public String delsVendorMat(@RequestBody List<VendorPerforVendorMatEntity> vendorMatEntitys){
		return vendorMatService.delsVendorMat(vendorMatEntitys);
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
