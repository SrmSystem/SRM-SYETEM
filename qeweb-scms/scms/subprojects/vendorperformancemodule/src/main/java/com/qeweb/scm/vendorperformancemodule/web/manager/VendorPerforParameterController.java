package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforParameterEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforParameterService;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforParamVo;

@Controller
@RequestMapping("/manager/vendor/vendorPerforParameter")
public class VendorPerforParameterController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforParameterService vendorPerforParameterService;
	
	@LogClass(method="查看", module="装车量导入")
	@RequiresPermissions("perfor:param:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/vendorPerforParameterList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorPerforParameterList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforParameterEntity> page = vendorPerforParameterService.getVendorPerforParameterList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@RequiresPermissions("perfor:param:add")
	@RequestMapping(value="addVendorPerforParameter",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addVendorPerforParameter(VendorPerforParameterEntity vendorPerforParameterEntity){
		return vendorPerforParameterService.addVendorPerforParameter(vendorPerforParameterEntity);
	}
	@RequestMapping(value="updateVendorPerforParameterStart/{id}",method = RequestMethod.GET)
	@ResponseBody
	public VendorPerforParameterEntity updateVendorPerforParameterStart(@PathVariable("id") Long id){
		VendorPerforParameterEntity entity = vendorPerforParameterService.updateVendorPerforParameterStart(id);
		entity.setOrgName(entity.getOrg()!= null ? entity.getOrg().getName() : "");
		return entity;
	}
	@RequiresPermissions("perfor:param:upd")
	@RequestMapping(value="updateVendorPerforParameter",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateVendorPerforParameter(VendorPerforParameterEntity vendorPerforParameterEntity){
		return vendorPerforParameterService.updateVendorPerforParameter(vendorPerforParameterEntity);
	}
	@RequiresPermissions("perfor:param:del")
	@RequestMapping(value = "deleteVendorPerforParameter",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorPerforParameter(@RequestBody List<VendorPerforParameterEntity> vendorPerforParameterEntitys){
		return vendorPerforParameterService.deleteVendorPerforParameter(vendorPerforParameterEntitys);
	}
	@RequiresPermissions("perfor:param:cancel")
	@RequestMapping(value = "delesVendorPerforParameter",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delesVendorPerforParameter(@RequestBody List<VendorPerforParameterEntity> vendorPerforParameterEntitys){
		return vendorPerforParameterService.delesVendorPerforParameter(vendorPerforParameterEntitys);
	}
	@RequestMapping(value = "getParameter")
	@ResponseBody
	public List<EasyuiComboBox> getParameter(){
		List<EasyuiComboBox> list = new ArrayList<EasyuiComboBox>();
		for (String str : Constant.VENDORPERFORPARAMETER_PARAMETER) {
			EasyuiComboBox box = new EasyuiComboBox(str, str);
			list.add(box);
		}
		return list;
	}
	
	@RequiresPermissions("perfor:param:imp")
	@RequestMapping(value="imp",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> imp(@RequestParam("files") MultipartFile files) throws Exception{
		File savefile = null;
		String logpath = null; 
		map = new HashMap<String, Object>();
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
			ExcelUtil<VendorPerforParamVo> excelutil = new ExcelUtil<VendorPerforParamVo>(savefile.getPath(), new ExcelAnnotationReader(VendorPerforParamVo.class), getLogger()); 
			List<VendorPerforParamVo> list = (List<VendorPerforParamVo>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = vendorPerforParameterService.imp(list, getLogger());
			if(flag) {
				map.put("msg", "导入参数设置成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入参数设置失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入参数设置失败!");  
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
