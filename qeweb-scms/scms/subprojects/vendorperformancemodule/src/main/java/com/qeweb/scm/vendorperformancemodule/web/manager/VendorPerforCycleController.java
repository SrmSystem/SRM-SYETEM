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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforPurchaseEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforCycleService;

@Controller
@RequestMapping("/manager/vendor/performance/cycle")
public class VendorPerforCycleController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforCycleService cycleService;
	
	@LogClass(method="查看", module="周期设置")
	@RequiresPermissions("perfor:cycle:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/cycleList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cycleList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforCycleEntity> page = cycleService.getCycleList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		
		return map;
	}
	
	@RequestMapping(value="getAll",method = RequestMethod.POST)
	@ResponseBody
	public List<VendorPerforCycleEntity> getAll(Model model,ServletRequest request){
		List<VendorPerforCycleEntity> list = cycleService.getList();
		return list;
	}
	
	@RequestMapping(value="getMaterialtype",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaterialtype(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request,String mts){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialTypeEntity> page = cycleService.getMaterialtype(pageNumber, pageSize, searchParamMap,mts);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@RequiresPermissions("perfor:cycle:add")
	@RequestMapping(value="addCycle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addCycle(VendorPerforCycleEntity cycleEntity){
		return cycleService.addCycle(cycleEntity);
	}
	@RequestMapping(value="getPurchase/{cid}",method = RequestMethod.POST)
	@ResponseBody
	public List<VendorPerforPurchaseEntity> getPurchase(@PathVariable("cid") Long cid){
		return cycleService.getPurchase(cid);
	}
	@RequestMapping(value="updateCycleStart/{cid}",method = RequestMethod.POST)
	@ResponseBody
	public String updateCycleStart(@PathVariable("cid") Long cid){
		return cycleService.updateCycleStart(cid);
	}
	@RequestMapping(value="deletePurchase/{mid}/{cid}",method = RequestMethod.POST)
	@ResponseBody
	public String deletePurchase(@PathVariable("mid") Long mid,@PathVariable("cid") Long cid){
		return cycleService.deletePurchase(mid,cid);
	}
	@RequiresPermissions("perfor:cycle:upd")
	@RequestMapping(value="updateCycle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateCycle(VendorPerforCycleEntity cycleEntity){
		return cycleService.updateCycle(cycleEntity);
	}
	@RequestMapping(value="saveUpdatePCycle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveUpdatePCycle(){
		map = new HashMap<String, Object>();
		Long cid=Long.parseLong(request.getParameter("cid"));
		
		if(request.getParameterValues("materialtypeId")!=null)
		{
			String materialtypeIds[] =request.getParameterValues("materialtypeId");
			String materialtypeName[] =request.getParameterValues("materialtypeName");
			String materialtypeNamef[] =request.getParameterValues("materialtypeNamef");
			String purchaseNumber[] =request.getParameterValues("purchaseNumber");
			cycleService.saveUpdatePCycle(cid,materialtypeIds,materialtypeName,materialtypeNamef,purchaseNumber);
			map.put("success",true);
		}else{
			map.put("success",false);
			map.put("msg","没有选择项");
		}
		return map;
	}
	
	@RequiresPermissions("perfor:cycle:using")
	@RequestMapping(value = "releaseCycle",method = RequestMethod.POST)
	@ResponseBody
	public String releaseCycle(@RequestBody List<VendorPerforCycleEntity> cycleEntitys){
		return cycleService.releaseCycle(cycleEntitys);
	}
	
	@RequiresPermissions("perfor:cycle:cancel")
	@RequestMapping(value = "delsCycle",method = RequestMethod.POST)
	@ResponseBody
	public String delsCycle(@RequestBody List<VendorPerforCycleEntity> cycleEntitys){
		return cycleService.delsCycle(cycleEntitys);
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
