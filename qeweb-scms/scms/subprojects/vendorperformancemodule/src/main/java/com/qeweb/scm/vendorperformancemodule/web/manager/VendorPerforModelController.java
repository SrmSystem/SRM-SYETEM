package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforModelEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforModelService;

@Controller
@RequestMapping("/manager/vendor/performance/model")
public class VendorPerforModelController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforModelService service;
	
	@LogClass(method="查看", module="绩效模型管理")
	@RequiresPermissions("perfor:model:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/model/modelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforModelEntity> page = service.queryList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getEnable",method = RequestMethod.POST)
	@ResponseBody
	public List<VendorPerforModelEntity> getEnable(Model model,ServletRequest request){
		List<VendorPerforModelEntity> list = service.getEnableList();
		VendorPerforModelEntity vis=new VendorPerforModelEntity();
		vis.setName("请选择");
		list.add(0,vis);
		return list;
	}
	
	/**
	 * 用户配件服务绩效的基础数据新增（多个品牌批量新增） 中的绩效类型combobox初始化
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getEnableEx",method = RequestMethod.POST)
	@ResponseBody
	public List<VendorPerforModelEntity> getEnableEx(Model model,ServletRequest request){
		List<VendorPerforModelEntity> list = service.getEnableByCode("001");
		return list;
	}
	
	
	
	@RequestMapping(value="getEnableX",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getEnableX(Model model,ServletRequest request){
		List<VendorPerforModelEntity> list = service.getEnableList();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(VendorPerforModelEntity bo : list){
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}

	@RequiresPermissions("perfor:model:add")
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(VendorPerforModelEntity model){
		return service.add(model);
	}
	
	@RequestMapping(value="get/{id}",method = RequestMethod.GET)
	@ResponseBody
	public VendorPerforModelEntity get(@PathVariable("id") Long id){
		return service.get(id);
	}
	
	@RequiresPermissions(value={"perfor:model:upd","perfor:model:unuse","perfor:model:using"},logical=Logical.OR)
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@ModelAttribute("model") VendorPerforModelEntity model){
		return service.update(model);
	}
	
	@RequiresPermissions("perfor:model:del")
	@RequestMapping(value="batchDelete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> batchDelete(@RequestParam("ids[]")List<Long> ids){
		return service.batchDelete(ids);
	}
	
	@ModelAttribute
	public void bind(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("model", service.get(id));
		}
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
