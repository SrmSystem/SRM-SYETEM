package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMaterialTypeEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforMaterialTypeService;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforMATVo;

@Controller
@RequestMapping("/manager/vendor/vendorPerforMaterialType")
public class VendorPerforMaterialTypeController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforMaterialTypeService materialTypeService;
	
	@LogClass(method="查看", module="物料类别管理")
	@RequiresPermissions("perfor:mattype:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/materialTypeList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pageList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforMaterialTypeEntity> page = materialTypeService.getPage(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="matypageList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> matypageList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished","0");
		Page<VendorPerforMaterialTypeEntity> page = materialTypeService.getPage(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequiresPermissions(value={"perfor:mattype:add", "perfor:mattype:upd"}, logical=Logical.OR)
	@RequestMapping(value="addUpdateMaterialType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addUpdateMaterialType(VendorPerforMaterialTypeEntity materialTypeEntity){
		return materialTypeService.addUpdateMaterialType(materialTypeEntity);
	}
	
	@RequiresPermissions("perfor:mattype:using")
	@RequestMapping(value = "releaseMaterialType",method = RequestMethod.POST)
	@ResponseBody
	public String releaseLevel(@RequestBody List<VendorPerforMaterialTypeEntity> materialTypeEntity){
		return materialTypeService.releaseMaterialType(materialTypeEntity);
	}
	
	@RequiresPermissions("perfor:mattype:cancel")
	@RequestMapping(value = "delsMaterialType",method = RequestMethod.POST)
	@ResponseBody
	public String delsLevel(@RequestBody List<VendorPerforMaterialTypeEntity> materialTypeEntity){
		return materialTypeService.delsMaterialType(materialTypeEntity);
	}
	
	@RequiresPermissions("perfor:mattype:del")
	@RequestMapping(value = "deletesMaterialType",method = RequestMethod.POST)
	@ResponseBody
	public String deletesMaterialType(@RequestBody List<VendorPerforMaterialTypeEntity> materialTypeEntity){
		return materialTypeService.deletesMaterialType(materialTypeEntity);
	}
	
	@RequiresPermissions("perfor:mattype:lead")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) {
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(VendorPerforMATVo.class), getLogger()); 
			List<VendorPerforMATVo> list = (List<VendorPerforMATVo>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = materialTypeService.combine(list, getLogger());
			if(flag) {
				map.put("msg", "导入数据成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入数据失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入数据失败!");  
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
