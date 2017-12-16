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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.SerialNumberEntity;
import com.qeweb.scm.basemodule.service.SerialNumberService;

@Controller
@RequestMapping("/manager/basedata/serial")
public class SerialNumberController {
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)  
	public String list(Model model) {
		return "back/basedata/serialNumberList";
	}
	
	@LogClass(method="查看", module="流水单号管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> materialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + "");
		Page<SerialNumberEntity> userPage = serialNumberService.getSerialNumberList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getSerial/{id}")
	@ResponseBody
	public SerialNumberEntity getSerial(@PathVariable("id") Long id){
		return serialNumberService.getSerialNumberById(id);
	}

	/**
	 * 新增
	 * @param serialNumber
	 * @return
	 */
	@RequestMapping(value = "addNewSerial",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewSerial(@Valid SerialNumberEntity serialNumber){   
		Map<String,Object> map = new HashMap<String, Object>();
		SerialNumberEntity entity = serialNumberService.getSerialNumberEntityByKey(serialNumber.getKey());
		if(entity != null) {
			map.put("success", true);
			map.put("message", "新增失败，前缀" + serialNumber.getPrefix() + " 流水号生成配置已存在");
			return map;
		}
		serialNumberService.addNewSerial(serialNumber);
		map.put("success", true);
		map.put("message", "新增成功");
		return map;
	}
	
	/**
	 * 删除
	 * @param serialNumberList
	 * @return
	 */
	@RequestMapping(value = "deleteSerial",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteSerial(@RequestBody List<SerialNumberEntity> serialNumberList){
		Map<String,Object> map = new HashMap<String, Object>();
		serialNumberService.deleteSerials(serialNumberList); 
		map.put("success", true);
		map.put("message", "删除成功");
		return map;
	}
	
	/**
	 * 修改
	 * @param serialNumber
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid SerialNumberEntity serialNumber) {
		map = new HashMap<String, Object>();
		serialNumberService.updateSerialNumber(serialNumber);
		map.put("success", true);
		map.put("message", "更新成功");
		return map;
	}
}
