package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforDimensionsService;

@Controller
@RequestMapping("/manager/vendor/performance/dimensions")
public class VendorPerforDimensionsController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforDimensionsService service;
	
	@LogClass(method="查看", module="维度设置")
	@RequiresPermissions("perfor:dim:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/dimensionsList";
	}
	
	@RequestMapping("getSetting")
	@ResponseBody
	public Map<String,Object> dimensionsList(){
		map = new HashMap<String, Object>();
		List<VendorPerforDimensionsEntity> menuList = service.getDimensionsList();
		map.put("rows", menuList);
		map.put("total", menuList.size());
		return map;
	}

	@RequiresPermissions("perfor:dim:add")
	@RequestMapping(value="addDimensions",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addDimensions(VendorPerforDimensionsEntity dimensionsEntity){
		return service.addDimensions(dimensionsEntity);
	}

	@RequiresPermissions("perfor:dim:upd")
	@RequestMapping(value="updateDimensions",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateDimensions(@RequestBody VendorPerforDimensionsEntity datas){
		return service.updateDimensions(datas);
	}
	@RequiresPermissions("perfor:dim:using")
	@RequestMapping(value = "releaseDimensions",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> releaseDimensions(@RequestBody VendorPerforDimensionsEntity dimensionsEntitys){
		return service.releaseDimensions(dimensionsEntitys);
	}
	@RequiresPermissions("perfor:dim:cancel")
	@RequestMapping(value = "delsDimensions",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delsDimensions(@RequestBody VendorPerforDimensionsEntity dimensionsEntitys){
		return service.delsDimensions(dimensionsEntitys);
	}
	@RequiresPermissions("perfor:dim:del")
	@RequestMapping(value = "deleteDimensions",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteDimensions(@RequestBody VendorPerforDimensionsEntity dimensionsEntitys){
		return service.deleteDimensions(dimensionsEntitys);
	}
	
	@RequestMapping("getList")
	@ResponseBody
	public List<VendorPerforDimensionsEntity> getList(){
		return service.getList();
	}
	
	@RequestMapping(value="getListByMod/{modId}")
	@ResponseBody
	public List<VendorPerforDimensionsEntity> getListByMod(@PathVariable(value="modId") Long modId){
		return service.getListByMod(modId);
	}
	
	/**
	 * 用户配件服务绩效的基础数据新增（多个品牌批量新增） 中的维度combobox初始化
	 * @param code
	 * @return
	 */
	@RequestMapping(value="getListByCodeEx/{code}")
	@ResponseBody
	public List<VendorPerforDimensionsEntity> getListByCodeEx(@PathVariable(value="code") String code){
		return service.getListByModEx(code);
	}
	
	@RequestMapping("getMappingScores")
	@ResponseBody
	public List<EasyuiComboBox> getMappingScores(Model model){
		List<String> mappingList  = service.getMappingScores();
		List<EasyuiComboBox> list = new ArrayList<EasyuiComboBox>();
		for(String mapping : mappingList){
			EasyuiComboBox cb = new EasyuiComboBox(mapping, mapping);
			list.add(cb);
		}
		return list;
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
