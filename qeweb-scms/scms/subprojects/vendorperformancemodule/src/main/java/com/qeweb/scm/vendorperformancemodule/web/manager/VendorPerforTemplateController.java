package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforTemplateService;

@Controller
@RequestMapping("/manager/vendorperformance/template")
public class VendorPerforTemplateController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforTemplateService service;
	
	@LogClass(method="查看", module="模版设置")
	@RequiresPermissions("perfor:template:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/template/templateList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforTemplateEntity> page = service.getList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequiresPermissions("perfor:template:add")
	@RequestMapping(value="addNew")
	@ResponseBody
	public Map<String,Object> addNew(VendorPerforTemplateEntity template,Model model,ServletRequest request){
		Map<String,Object> map = service.add(template);
		return map;
	}
	
	@RequestMapping(value="toEdit")
	public String toEdit(Long templateId,Model model,ServletRequest request){
		VendorPerforTemplateEntity tempate = service.getById(templateId);
		model.addAttribute("template", tempate);
		return "back/performance/template/templateEdit";
	}
	
	@RequiresPermissions("perfor:template:upd")
	@RequestMapping("settingUpdate")
	@ResponseBody
	public Map<String,Object> settingUpdate(@ModelAttribute(value="template") VendorPerforTemplateEntity template,Model model,ServletRequest request){
		Map<String,Object> map = service.update(template);
		return map;
	}
	
	@RequestMapping("getSetting")
	@ResponseBody
	public Map<String,Object> getSetting(Long templateId,Model model,ServletRequest request){
		List<VendorPerforTemplateSettingEntity> settingList = service.getSetting(templateId);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", settingList.size());
		map.put("rows", settingList);
		return map;
	}
	
	/**
	 * 批量删除模版（硬删除）
	 * @param ids
	 */
	@RequiresPermissions("perfor:template:del")
	@RequestMapping("deleteList")
	@ResponseBody
	public Map<String,Object> deleteList(@RequestBody List<VendorPerforTemplateEntity> list,Model model,ServletRequest request){
		Map<String,Object> map = service.deleteList(list);
		return map;
	}
	
	/**
	 * 启用
	 * @param list
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"perfor:template:using"},logical=Logical.OR)
	@RequestMapping("enableList")
	@ResponseBody
	public Map<String,Object> abolishList(@RequestBody List<VendorPerforTemplateEntity> list,Model model,ServletRequest request){
		Map<String,Object> map = service.abolishList(list);
		return map;
	}
	
	/**
	 * 禁用
	 * @param list
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"perfor:template:cancel"},logical=Logical.OR)
	@RequestMapping("cancelList")
	@ResponseBody
	public Map<String,Object> cancelList(@RequestBody List<VendorPerforTemplateEntity> list,Model model,ServletRequest request){
		Map<String,Object> map = service.abolishList(list);
		return map;
	}
	
	@RequestMapping("getMappingScores")
	@ResponseBody
	public List<EasyuiComboBox> getMappingScores(@RequestParam(required=false)Long id,Model model){
		List<String> mappingList  = service.getMappingScores(id);
		List<EasyuiComboBox> list = new ArrayList<EasyuiComboBox>();
		for(String mapping : mappingList){
			EasyuiComboBox cb = new EasyuiComboBox(mapping, mapping);
			list.add(cb);
		}
		return list;
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bind(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("template", service.getById(id));
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
