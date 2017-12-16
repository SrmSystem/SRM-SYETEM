package com.qeweb.scm.basemodule.web.manager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.DictEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.DictService;
import com.qeweb.scm.sap.service.BOHelper;
/**
 * 字典表Controller
 * @author eleven
 * 创建时间：2017年5月45日17：21
 *  最后更新时间2017年5月45日17：21
 * 最后更新人：eleven
 */
@Controller
@RequestMapping("/manager/basedata/dict")
public class DictController {
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private DictItemService dictItemService;
	
	private Map<String,Object> map;
	/**
	 * 数据字典
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="dictList",method = RequestMethod.GET)
	public String pendingList(Model model) {
		return "back/basedata/dictList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dictList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", BOHelper.UNABOLISHED_SINGEL);
		searchParamMap.put("NEQ_dictCode", "PURCHASE_ORDER_TYPE");
		Page<DictEntity> page = dictService.getDictList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	//获取字典表配置选项
	@RequestMapping("getDict/{type}")
	@ResponseBody
	public List<EasyuiComboBox> getDict(@PathVariable("type") String type){
		Map<String, Object> map = dictService.getDict(type);
		List<EasyuiComboBox> propertyList = new ArrayList<EasyuiComboBox>();
		for(String key : map.keySet()){
			EasyuiComboBox box = new EasyuiComboBox(key, (String) map.get(key));
			propertyList.add(box);
		}
		return propertyList;
	}
	
	/**
	 * 根据字典编码获取字典子表信息
	 * @param request
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "getDictItemSelect/{dictCode}",method = RequestMethod.POST)
	@ResponseBody
	public List<DictItemEntity> getDictItemSelect(ServletRequest request,@PathVariable("dictCode") String dictCode){
		List<DictItemEntity> list = dictItemService.findListByDictCode(dictCode);
		return list;
	}
	
	
	
	
	/**
	 * 根据id查数据字典
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "findDictById/{id}")
	@ResponseBody
	public DictEntity findDictById(@PathVariable("id") Long id){
		return dictService.findDictById(id);
	}
	
	/**
	 * 新增/修改数据字典主表
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "addOrUpdateDict",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addOrUpdateDict(@Valid DictEntity dict){
		Map<String,Object> map = new HashMap<String, Object>();
		DictEntity fa = null ;
		if(dict.getId()>0){
			fa = dictService.getDictByCodeAndAbolishedAndNotId(dict.getDictCode(),BOHelper.UNABOLISHED_SINGEL,dict.getId());
		}else{
		    fa = dictService.getDictByCodeAndAbolished(dict.getDictCode(),BOHelper.UNABOLISHED_SINGEL);
		}
		if(fa!=null){
			map.put("msg", "编码重复！");
			map.put("success", false);
			return map;
		}
		dictService.addOrUpdateDict(dict);
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	

	/**
	 * 删除数据字典主表
	 * @param dictList
	 * @return
	 */
	@RequestMapping(value = "deleteDict",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteDict(@RequestBody List<DictEntity> dictList){
		Map<String,Object> map = new HashMap<String, Object>();
		dictService.deleteDictList(dictList);
		map.put("success", true);
		return map;
	}
	
	
	
	/**
	 * 增加子项
	 * @param dictId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="showDictItemList/{dictId}",method = RequestMethod.GET)
	public String getDictItem(@PathVariable("dictId") String dictId,Model model){
		model.addAttribute("dictId", dictId);
		return "back/basedata/dictItemList";
	}
	
	/**
	 * 根据字典id获取字典子表信息
	 * @param request
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "getDictItemByDictId/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDictItemByDictId(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,ServletRequest request,@PathVariable("id") Long id){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", BOHelper.UNABOLISHED_SINGEL);
		searchParamMap.put("EQ_dict.id", id);
		Page<DictItemEntity> page = dictItemService.getList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	/**
	 * 删除子项
	 * @param dictList
	 * @return
	 */
	@RequestMapping(value = "deleteDictItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteDictItem(@RequestBody List<DictItemEntity> dictItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		dictItemService.deleteDictItemList(dictItemList);
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 新增/修改子项
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "addOrUpdateDictItem/{dictId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addOrUpdateDictItem(@Valid DictItemEntity dictItem,@PathVariable("dictId") Long dictId){
		Map<String,Object> map = new HashMap<String, Object>();
		DictItemEntity fa = null ;
		if(dictItem.getId()>0){
			fa = dictItemService.getDictItemByCodeAndAbolishedAndNotId(dictId,dictItem.getCode(),BOHelper.UNABOLISHED_SINGEL,dictItem.getId());
		}else{
		    fa = dictItemService.getDictItemByCodeAndAbolished(dictId,dictItem.getCode(),BOHelper.UNABOLISHED_SINGEL);
		}
		if(fa!=null){
			map.put("msg", "编码重复！");
			map.put("success", false);
			return map;
		}else if(0==dictId){
			map.put("msg", "获取数据字典主表出错，请重新操作！");
			map.put("success", false);
			return map;
		}
		DictEntity dict=dictService.findDictById(dictId);
		dictItemService.addOrUpdateDictItem(dictItem,dict);
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 根据id子项
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "findDictItemById/{id}")
	@ResponseBody
	public DictItemEntity findDictItemById(@PathVariable("id") Long id){
		return dictItemService.findDictItemById(id);
	}
	
}
