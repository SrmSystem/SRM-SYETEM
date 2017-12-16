package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.LinkedList;
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

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.InventoryLocationEntity;
import com.qeweb.scm.basemodule.service.InventoryLocationService;

@Controller
@RequestMapping("/manager/basedata/inventoryLocation")
public class InventoryLocationController {
	
	@Autowired
	private InventoryLocationService inventoryLocationService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/inventoryLocationList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> inventoryLocationList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<InventoryLocationEntity> page = inventoryLocationService.getInventoryLocationList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewInventoryLocation",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewInventoryLocation(@Valid InventoryLocationEntity inventoryLocation){
		
		Map<String,Object> map = new HashMap<String, Object>();
		InventoryLocationEntity Inv  =  inventoryLocationService.getInventoryLocationByCode(inventoryLocation.getCode());
		if(  Inv != null) {
			map.put("msg", "编码已存在！");
			map.put("success", false);
			return map;
		}
		inventoryLocationService.addNewInventoryLocation(inventoryLocation);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("inventoryLocation") InventoryLocationEntity inventoryLocation) {
		map = new HashMap<String, Object>();
		inventoryLocationService.updateInventoryLocation(inventoryLocation);
		map.put("success", true);
		return map;
		
	}
	
	
	@RequestMapping(value = "deleteInventoryLocation",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteInventoryLocationList(@RequestBody List<InventoryLocationEntity> InventoryLocationList){
		Map<String,Object> map = new HashMap<String, Object>();
		inventoryLocationService.deleteInventoryLocationList(InventoryLocationList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getInventoryLocation/{id}")
	@ResponseBody
	public InventoryLocationEntity getInventoryLocation(@PathVariable("id") Long id){
		return inventoryLocationService.getInventoryLocation(id);
	}
	
	@RequestMapping(value = "getInventoryLocationSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getInventoryLocationSelect(ServletRequest request){
		List<InventoryLocationEntity> list = inventoryLocationService.findAll();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			InventoryLocationEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "getEffectiveInventoryLocationSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getEffectiveInventoryLocationSelect(ServletRequest request){
		List<InventoryLocationEntity> list = inventoryLocationService.findEffective();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			InventoryLocationEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "abolishBatchInventoryLocation",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchBrand(@RequestBody List<InventoryLocationEntity> inventoryLocationList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = inventoryLocationService.abolishBatch(inventoryLocationList);
		return map;
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindInventoryLocationEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("inventoryLocation", inventoryLocationService.getInventoryLocation(id));
		}
	}
	

}
