package com.qeweb.scm.basemodule.web.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.DynamicDataEntity;
import com.qeweb.scm.basemodule.entity.DynamicDataSceneEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.DynamicDataService;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.DynamicInfoVO;

@Controller
@RequestMapping("/manager/basedata/dynamicData")
public class DynamicDataController implements ILog {
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private DynamicDataService dynamicDataService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="动态数据管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/dynamicDataList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dynamicDataList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished","0"); 
		Page<DynamicDataEntity> page = dynamicDataService.getDynamicDataList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getDynamicItem/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dynamicDataItemList(@PathVariable("id") Long mainId, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		map = new HashMap<String, Object>();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_dataEx.id", mainId + "");   
		Page<DynamicDataSceneEntity> page = dynamicDataService.getDynamicDataItemList(pageNumber,pageSize,searchParamMap);
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@LogClass(method="修改", module="动态数据修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("dynamicData") DynamicDataEntity dynamicData) {
		map = new HashMap<String, Object>();
		dynamicDataService.updateDynamicData(dynamicData);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除
	 * @param dynamicDataList
	 * @return
	 */
	@LogClass(method="删除", module="动态数据删除")
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delDynamicData(@RequestBody List<DynamicDataEntity> dynamicDataList){
		map = new HashMap<String, Object>();
		dynamicDataService.delDynamicDataList(dynamicDataList);
		map.put("success", true);  
		map.put("message", "删除成功");  
		return map;
	}
	
	/**
	 * 启用
	 * @param dynamicDataList
	 * @return
	 */
	@LogClass(method="启用", module="动态数据启用")
	@RequestMapping(value = "enable",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> enableDynamicData(@RequestBody List<DynamicDataEntity> dynamicDataList){
		map = new HashMap<String, Object>();
		dynamicDataService.enableDynamicDataList(dynamicDataList, StatusConstant.STATUS_YES);
		map.put("success", true);  
		map.put("message", "启用成功");  
		return map;
	}
	
	/**
	 * 禁用
	 * @param dynamicDataList
	 * @return
	 */
	@LogClass(method="禁用", module="动态数据禁用")
	@RequestMapping(value = "disable",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> disableDynamicData(@RequestBody List<DynamicDataEntity> dynamicDataList){
		map = new HashMap<String, Object>();
		dynamicDataService.enableDynamicDataList(dynamicDataList, StatusConstant.STATUS_NO);
		map.put("success", true);
		map.put("message", "禁用成功");  
		return map;
	}
	
	@RequestMapping(value = "saveDynamic",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveDynamic(@Valid DynamicDataEntity dynamicEntity, @RequestParam(value="datas") String dynamicItemdatas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		String retMsg = "新增动态数据成功";
		if(dynamicEntity.getId() > 0l)
			retMsg = "编辑动态数据成功";
		//明细
		List<DynamicDataSceneEntity> itemList = new ArrayList<DynamicDataSceneEntity>();
		DynamicDataSceneEntity item = null;
		JSONObject object = JSONObject.fromObject(dynamicItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new DynamicDataSceneEntity(); 
			item.setDataEx(dynamicEntity); 
			item.setId(object.get("id") == null ? 0l : StringUtils.convertToLong(object.get("id") + ""));
			item.setColCode(StringUtils.convertToString(object.get("colCode")));
			item.setName(StringUtils.convertToString(object.get("name")));
			item.setRange(StringUtils.convertToString(object.get("range")));
			item.setType(StringUtils.convertToString(object.get("type")));   
			item.setStatusKey(StringUtils.convertToString(object.get("statusKey")));
			item.setWay(StringUtils.convertToString(object.get("way"))); 
			item.setFilter(StringUtils.isEqual("是", object.get("filter") + ""));
			item.setRequired(StringUtils.isEqual("是", object.get("required") + "")); 
			item.setShow(StringUtils.isEqual("是", object.get("show") + ""));
			itemList.add(item);
		}
		dynamicDataService.saveDynamic(dynamicEntity, itemList);
		map.put("success", true);
		map.put("message", retMsg);
		return map;   
	}
	
	@RequestMapping("getDynamicData/{id}")
	@ResponseBody
	public DynamicDataEntity getDynamicData(@PathVariable("id") Long id){
		return dynamicDataService.getDynamicData(id);
	}
	
	@LogClass(method="导入", module="导入字段配置")
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files, HttpServletRequest request) {
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil<DynamicInfoVO> excelutil = new ExcelUtil<DynamicInfoVO>(savefile.getPath(), new ExcelAnnotationReader(DynamicInfoVO.class), getLogger()); 
			List<DynamicInfoVO> list = (List<DynamicInfoVO>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容"); 
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = dynamicDataService.saveDynamicInfo(list, getLogger());
			if(flag) {
				map.put("msg", "导入字段配置成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入字段配置失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入字段配置失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));    
		}
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
