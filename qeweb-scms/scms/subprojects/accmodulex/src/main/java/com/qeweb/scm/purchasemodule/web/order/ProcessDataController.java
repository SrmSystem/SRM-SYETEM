package com.qeweb.scm.purchasemodule.web.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.purchasemodule.entity.ProcessEntity;
import com.qeweb.scm.purchasemodule.entity.ProcessMaterialRelEntity;
import com.qeweb.scm.purchasemodule.repository.ProcessDao;
import com.qeweb.scm.purchasemodule.repository.ProcessMaterialRelDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.service.ProcessService;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialRelEntity;

/**
 * 工序基础数据（弃用）
 *
 */
@Controller
@RequestMapping(value = "/manager/order/processData")
public class ProcessDataController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private ProcessMaterialRelDao processMaterialRelDao;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private ProcessDao processDao;
	

	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/order/processList";  
	}
	
	@RequestMapping(value="getProcessList" ,method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getProcessList(@RequestParam(value="page") int pageNumber,@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", 0);
		Page<ProcessEntity> groupPage = processService.getProcessList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",groupPage.getContent());
		map.put("total",groupPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="saveSubmit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveSubmit(@Valid ProcessEntity entity){
		ProcessEntity process=processDao.findByCodeAndAbolished(entity.getCode(), 0);
		if(process!=null){
			map.put("msg", "工序已存在");
			map.put("success", false);
			return map;
		}
		entity.setAbolished(Constant.UNDELETE_FLAG);
		processService.saveProcess(entity);
		map = new HashMap<String, Object>();
		map.put("msg", "新增成功");
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value="deleteOpt",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteOpt(@RequestBody List<ProcessEntity> entityList){
		processService.deleteProcessList(entityList);
		map = new HashMap<String, Object>();
		map.put("msg", "删除成功");
		map.put("success",true);
		return map;
	}
	
	
	
	@RequestMapping(value="getProcessMaterialRelList/{processId}")
	public String displayMaterial(@PathVariable("processId") Long processId,Model model){
		model.addAttribute("processId",processId);
		return "back/order/processMaterialRelList";
	}
	
	/**
	 * 工序下物料关系
	 * @param processId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getProcessMaterialRelList/{processId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getProcessMaterialRelList(@PathVariable("processId")Long processId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_processId", processId);
		Page<ProcessMaterialRelEntity> page = processService.getProcessMaterialRelList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	/**
	 * 给工序加物料关系
	 * @param processId
	 * @param materialList
	 * @return
	 */
	@RequestMapping(value = "selMaterial/{processId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selMaterial(@PathVariable("processId") Long processId,@RequestBody List<MaterialEntity> materialList){
		Map<String,Object> map = new HashMap<String, Object>();
		processService.saveProcessMaterialRel(processId, materialList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "delProcessMaterialRelList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delProcessMaterialRelList(@RequestBody List<ProcessMaterialRelEntity> relList){
		Map<String,Object> map = new HashMap<String, Object>();
		processService.deleteProcessMaterialRelList(relList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value="getMaterialList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaterialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialEntity> userPage = materialService.getMaterialList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
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
