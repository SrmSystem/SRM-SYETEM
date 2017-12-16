package com.qeweb.scm.epmodule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.constans.EpModuleConstans;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;
import com.qeweb.scm.epmodule.service.EpModuleService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 询比价模板controller
 * @author ronnie
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epModule")
public class EpModuleController {
	
	private Map<String,Object> map;

	@Autowired
	private EpModuleService epModuleService;    
	
	/**
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/ep/epModuleList";                 
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询比价模板列表",module="询比价管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG);
		Page<EpModuleEntity> userPage = epModuleService.getEpModuleLists(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getEpModule/{id}/{isUpdate}", method = RequestMethod.GET)
	public String getEpModule(@PathVariable("id") Long id, @PathVariable("isUpdate") boolean isUpdate, Model model){
		EpModuleEntity epModule = epModuleService.getEpModule(id);
		model.addAttribute("isUpdate", isUpdate);  
		model.addAttribute("epModule", epModule);
		return "back/ep/epModuleView";
	}
	
	@RequestMapping(value = "getEpModuleItemDetail/{id}/{isUpdate}", method = RequestMethod.GET)
	public String getEpModuleItemDetail(@PathVariable("id") Long id, @PathVariable("isUpdate") boolean isUpdate, Model model){
		EpModuleItemEntity epModuleItem = epModuleService.getEpModuleItem(id);
		model.addAttribute("isUpdate", isUpdate);  
		model.addAttribute("epModuleItem", epModuleItem);
		return "back/ep/epModuleViewSecond";
	}
	
	/**
	 * 
	 * @param epModuleId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="查看模板详情",module="询比价管理")
	@RequestMapping(value = "getEpModuleItem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long epModuleId, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_module.id", epModuleId+"");
		Page<EpModuleItemEntity> userPage = epModuleService.getEpModuleItems(pageNumber,pageSize,searchParamMap);
		for (int i = 0; i < userPage.getContent().size(); i++) {

		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@LogClass(method="删除模板",module="询比价管理")
	@RequestMapping(value = "deleteEpModule/{id}")
	@ResponseBody
	public Map<String,Object> deleteEpModule(@PathVariable("id") Long epModuleId, ServletRequest request){
		//modify by yao.jin 2017.02.17
		EpModuleEntity epModule = epModuleService.getEpModule(epModuleId);
		Set<EpModuleItemEntity> moduleItems = epModule.getModuleItems();
		if(!CollectionUtils.isEmpty(moduleItems)){
			for(EpModuleItemEntity e : moduleItems){
				List<EpModuleItemEntity> secondItems = epModuleService.getSecondItems(e.getId());
				if(secondItems != null && secondItems.size()>0){
					epModuleService.abolishModuleItems(secondItems);
					epModule.setAbolished(1);
				}
				//epModuleService.deleteModuleItems(secondItems);
			}
			epModuleService.abolishModuleItems(moduleItems);
			//epModuleService.deleteModuleItems(moduleItems);
		}
		//epModuleService.deleteEpModule(epModuleId);
		epModuleService.abolishEpModule(epModuleId);
		//end modify
		map.put("message", "删除模板成功");
		map.put("success",true);
		return map;
	}
	
	/**
	 * 
	 * @param epModuleId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="查看模板详情",module="询比价管理")
	@RequestMapping(value = "getEpModuleItemSecond/{id}")
	@ResponseBody
	public Map<String,Object> getItemListSecond(@PathVariable("id") Long epModuleItemId, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_parentId", epModuleItemId+"");
		Page<EpModuleItemEntity> userPage = epModuleService.getEpModuleItems(pageNumber,pageSize,searchParamMap);
		for (int i = 0; i < userPage.getContent().size(); i++) {
			
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "createEpModule", method = RequestMethod.GET)
	public String createEpModule(Model model,ServletRequest request){
		return "back/ep/epModuleAdd";
	}
	
	@RequestMapping(value = "addSecondItem/{id}", method = RequestMethod.GET)
	public String addSecondItem(@PathVariable("id") Long id, Model model){
		EpModuleItemEntity firstItem = epModuleService.getEpModuleItem(id);
		model.addAttribute("firstItem", firstItem);
		return "back/ep/epModuleAddSecond";
	}
	
	/**
	 * 新增/修改询比价模板
	 * @param epmodule
	 * @param moduleItemDatas
	 * @return
	 * @throws Exception
	 */
	@LogClass(method="新增/修改询比价模板",module="询比价")
	@RequestMapping("addEpModule/{type}")
	@ResponseBody
	public Map<String, Object> addEpModule(EpModuleEntity epmodule, @PathVariable("type") Integer type, @RequestParam(value="datas") String moduleItemDatas) throws Exception{
		map = new HashMap<String, Object>();
		List<EpModuleItemEntity> moduleItemList = new ArrayList<EpModuleItemEntity>();
		JSONObject object = JSONObject.fromObject(moduleItemDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EpModuleItemEntity item = null;
//		EpModuleEntity module = null;
		if(type == 1){
//			module = epModuleService.getEpModule(epmodule.getId());
//			module.setCode(epmodule.getCode());
//			module.setName(epmodule.getName());
//			module.setIsDefault(epmodule.getIsDefault());
//			module.setRemarks(epmodule.getRemarks());
		}else{
//			module = epmodule;
//			epmodule.setTenantId(EpModuleConstans.DEFAULT_TENANT_ID);
		}
//		epmodule.setTenantId(EpModuleConstans.DEFAULT_TENANT_ID);
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
//			if(object.get("id") != null){
//				item = epModuleService.getEpModuleItem(StringUtils.convertToLong(object.get("id")+""));
//			}else{
				item = new EpModuleItemEntity();
//				item.setTenantId(EpModuleConstans.DEFAULT_TENANT_ID);
//			}
//			item.setModule(module);
			item.setName(StringUtils.convertToString(object.get("name")));
			item.setUnitId(StringUtils.convertToString(object.get("unitId")+""));
			item.setIsTop(EpModuleConstans.IS_TOP_YES);
			item.setRemarks(StringUtils.convertToString(object.get("remarks")));
			moduleItemList.add(item);
		}
		epModuleService.addEpModule(epmodule, moduleItemList);
		map.put("epModuleId", epmodule.getId());
		if(type == 0){
			map.put("message", "新增模板成功");
		}
		if(type == 1){
			map.put("message", "修改模板成功");
		}
		map.put("success",true);
		return map;  
	}
	
	/**
	 * 新增询比价模板二级明细
	 * @param epmoduleItem
	 * @param moduleItemDatas
	 * @return
	 * @throws Exception
	 */
	@LogClass(method="新增询比价模板二级明细",module="询比价")
	@RequestMapping("saveSecondItem")
	@ResponseBody
	public Map<String, Object> saveSecondItem(EpModuleItemEntity epmoduleItem,@RequestParam(value="datas") String moduleItemDatas) throws Exception{
		map = new HashMap<String, Object>();
		List<EpModuleItemEntity> moduleItemList = new ArrayList<EpModuleItemEntity>();
		JSONObject object = JSONObject.fromObject(moduleItemDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EpModuleItemEntity item = null;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			if(object.get("id") != null){
				item = epModuleService.getEpModuleItem(StringUtils.convertToLong(object.get("id")+""));
			}else{
				item = new EpModuleItemEntity();
//				item.setTenantId(EpModuleConstans.DEFAULT_TENANT_ID);
			}
			item.setParentId(epmoduleItem.getId());
			item.setName(StringUtils.convertToString(object.get("name")));
			item.setUnitId(StringUtils.convertToString(object.get("unitId")+""));
			item.setIsTop(EpModuleConstans.IS_TOP_NO);
			item.setRemarks(StringUtils.convertToString(object.get("remarks")));
			moduleItemList.add(item);
		}	
		epModuleService.addEpModuleItemSecond(moduleItemList);
		map.put("message", "新增模板二级明细成功");
		map.put("success",true);
		return map;  
	}
	
}
