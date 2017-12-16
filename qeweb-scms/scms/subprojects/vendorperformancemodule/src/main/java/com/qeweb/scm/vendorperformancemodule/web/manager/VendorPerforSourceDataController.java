package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.ResultUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforSourceDataEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforSourceDataService;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforSourceDataVo;

@Controller
@RequestMapping("/manager/vendor/performance/sourcedata")
public class VendorPerforSourceDataController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforSourceDataService service;
	
	@LogClass(method="查看", module="绩效基础数据")
	@RequiresPermissions("perfor:source:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/sourceDataList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + ""); 
		Page<VendorPerforSourceDataEntity> page = service.query(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="get/{id}",method=RequestMethod.GET)
	@ResponseBody
	public VendorPerforSourceDataEntity get(@PathVariable("id")Long id)
	{
		return service.get(id);
	}
	
	@RequiresPermissions("perfor:source:add")
	@RequestMapping(value="add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(VendorPerforSourceDataEntity sourceData,@RequestParam(required=false)MultipartFile attFile) throws Exception
	{
		return service.add(sourceData,attFile);
	}
	
	@RequiresPermissions("perfor:source:add")
	@RequestMapping(value="addEx",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addEx(VendorPerforSourceDataEntity sourceData,@RequestParam(required=false)MultipartFile attFile) throws Exception
	{
		return service.addEx(sourceData,attFile);
	}
	
	@RequiresPermissions("perfor:source:upd")
	@RequestMapping(value="update",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updata(@ModelAttribute("sourceData") VendorPerforSourceDataEntity sourceData,@RequestParam(required=false)MultipartFile attFile) throws Exception
	{
		return service.update(sourceData,attFile);
	}
	
	@RequiresPermissions("perfor:source:del")
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@RequestParam(value="ids[]")List<Long> ids, Model model) throws Exception {
		return service.delete(ids);
	}  
	@RequiresPermissions("perfor:source:lead")
	@RequestMapping(value="imp",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> imp(@RequestParam("impFile")  MultipartFile impFile) throws Exception{
		File savefile = null;
		String logPath = null;
		try {
			logPath = getLogger().init(this);
			savefile = FileUtil.savefile(impFile, null);
			ExcelUtil<VendorPerforSourceDataVo> excelutil = new ExcelUtil<VendorPerforSourceDataVo>(savefile.getPath(), new ExcelAnnotationReader(VendorPerforSourceDataVo.class),getLogger()); 
			List<VendorPerforSourceDataVo> list =  excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				map = ResultUtil.createMap();
				map.put(ResultUtil.SUCCESS, false);
				map.put(ResultUtil.MSG,"上传文件为空，或无内容,请查看日志");
				map.put("logPath", logPath);
				return map;
			}
			map = service.imp(list,getLogger());
			map.put("logPath", logPath);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			getLogger().destory();  
			savefile.deleteOnExit();
		}
		return map;
	}
	
	@ModelAttribute
	public void bind(@RequestParam(value="id",defaultValue="-1")Long id,Model model){
		if(id != -1){
			VendorPerforSourceDataEntity sourceData = service.get(id);
			model.addAttribute("sourceData", sourceData);
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> fileImport(@RequestParam("planfiles") MultipartFile files,Long cycleId) {
		File savefile = null;
		String logpath = null; 
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
