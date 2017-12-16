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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforLevelEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforLevelService;

@Controller
@RequestMapping("/manager/vendor/level")
public class VendorPerforLevelController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforLevelService levelService;
	
	@LogClass(method="查看", module="等级设置")
	@RequiresPermissions("perfor:level:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/levelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> levelList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforLevelEntity> page = levelService.getLevelList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getminAndmax",method = RequestMethod.POST)
	@ResponseBody
	public String getminAndmax(){
		
		return levelService.getminAndmax();
	}
	
	@RequiresPermissions("perfor:level:add")
	@RequestMapping(value="addLevel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addLevel(VendorPerforLevelEntity levelEntity){
		return levelService.addLevel(levelEntity);
	}
	
	@RequestMapping(value="updateLevelStart",method = RequestMethod.POST)
	@ResponseBody
	public String updateLevelStart(VendorPerforLevelEntity levelEntity){
		return levelService.updateLevelStart(levelEntity);
	}
	
	@RequiresPermissions("perfor:level:upd")
	@RequestMapping(value="updateLevel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateLevel(VendorPerforLevelEntity levelEntity){
		return levelService.updateLevel(levelEntity);
	}

	@RequiresPermissions("perfor:level:using")
	@RequestMapping(value = "releaseLevel",method = RequestMethod.POST)
	@ResponseBody
	public String releaseLevel(@RequestBody List<VendorPerforLevelEntity> levelEntitys){
		return levelService.releaseLevel(levelEntitys);
	}
	
	@RequiresPermissions("perfor:level:cancel")
	@RequestMapping(value = "delsLevel",method = RequestMethod.POST)
	@ResponseBody
	public String delsLevel(@RequestBody List<VendorPerforLevelEntity> levelEntitys){
		return levelService.delsLevel(levelEntitys);
	}
	@RequestMapping(value="/getVendorPerforLevel")
	@ResponseBody
	public String getVendorPerforLevel()
	{
		return levelService.getVendorPerforLevel();
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
