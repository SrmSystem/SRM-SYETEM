package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.service.FactoryService;

@Controller
@RequestMapping("/manager/basedata/factory")
public class FactoryController {
	
	@Autowired
	private FactoryService factoryService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/factoryList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> factoryList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<FactoryEntity> page = factoryService.getFactoryList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewFactory",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewFactory(@RequestBody FactoryEntity factory){
		Map<String,Object> map = new HashMap<String, Object>();
		FactoryEntity fa = factoryService.getFactoryByCode(factory.getCode());
		if(fa!=null){
			map.put("msg", "编码已存在！");
			map.put("success", false);
			return map;
		}
		factoryService.addNewFactory(factory);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@RequestBody FactoryEntity factory) {
		map = new HashMap<String, Object>();

		factoryService.updateFactory(factory);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteFactory",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteFactoryList(@RequestBody List<FactoryEntity> factoryList){
		Map<String,Object> map = new HashMap<String, Object>();
		factoryService.deleteFactoryList(factoryList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getFactory/{id}")
	@ResponseBody
	public FactoryEntity getFactory(@PathVariable("id") Long id){
		return factoryService.getFactory(id);
	}
	@RequestMapping(value = "getFactorySelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getFactorySelect(ServletRequest request){
		List<FactoryEntity> list = factoryService.findAll();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			FactoryEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindFactory(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("factory", factoryService.getFactory(id));
		}
	}
	@RequestMapping("getBussinessRange/{id}")
	@ResponseBody
	public List<BussinessRangeEntity> getBussinessRange(@PathVariable("id") Long id){
		return factoryService.getBussinessRange(id);
	}
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<FactoryEntity> factoryList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = factoryService.abolishBatch(factoryList);
		return map;
	}

}
