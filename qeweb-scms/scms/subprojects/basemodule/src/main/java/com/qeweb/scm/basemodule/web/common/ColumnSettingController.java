package com.qeweb.scm.basemodule.web.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.entity.ColumnSettingEntity;
import com.qeweb.scm.basemodule.service.ColumnSettingService;
/**
 * 表格列设置，列头排序和宽度
 * @author VILON
 *
 */
@Controller
@RequestMapping(value = "/common/columnSetting")
public class ColumnSettingController {
	
	@Autowired
	private ColumnSettingService service;
	
	@RequestMapping("setColumnSetting/{userId}/{path}/{table}/{sortString}")
	@ResponseBody
	public Map<String,Object> setColumnSetting(@PathVariable("userId") Long userId,@PathVariable("path") String path,@PathVariable("table") String table,@PathVariable("sortString") String sortString,@RequestBody Map<String,Object> params){
		path = path.replaceAll("\\|", "/");
		ColumnSettingEntity en = service.getColumnSettingByUserAndPathAndTable(userId, path, table);
		if(en == null){
			en = new ColumnSettingEntity();
			en.setUserId(userId);
			en.setPath(path);
			en.setTable(table);
		}
		sortString = params.get("sortString_name").toString();
		en.setSortString(sortString);
		service.update(en);
		return new HashMap<String, Object>();
	}
	
	@RequestMapping("getColumnSetting/{userId}/{path}/{table}")
	@ResponseBody
	public Map<String,Object> getColumnSetting(@PathVariable("userId") Long userId,@PathVariable("path") String path,@PathVariable("table") String table){
		Map<String,Object> map = new HashMap<String, Object>();
		path = path.replaceAll("\\|", "/");
		ColumnSettingEntity en = service.getColumnSettingByUserAndPathAndTable(userId, path, table);
		if(en!=null){
			map.put("sortString", en.getSortString());
		}else{
			map.put("sortString", "");
		}
		return map;
	}

}
