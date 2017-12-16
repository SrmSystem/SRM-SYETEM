package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.service.AreaService;
/**
 * 区域Controller
 * @author lw
 * 创建时间：2015年6月2日14:24:38
 * 最后更新时间：2015年6月3日14:57:59
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/basedata/area")
public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/areaList";
	}
	
	@LogClass(method="查看", module="区域管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> areaList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<AreaEntity> page = areaService.getAreaList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewArea",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewArea(@Valid AreaEntity area){
		Map<String,Object> map = new HashMap<String, Object>();
		areaService.addNewArea(area);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("area") AreaEntity area) {
		map = new HashMap<String, Object>();
		areaService.updateArea(area);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteArea",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteAreaList(@RequestBody List<AreaEntity> areaList){
		Map<String,Object> map = new HashMap<String, Object>();
		areaService.deleteAreaList(areaList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getArea/{id}")
	@ResponseBody
	public AreaEntity getArea(@PathVariable("id") Long id){
		return areaService.getArea(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindArea(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("area", areaService.getArea(id));
		}
	}
	
	

}
