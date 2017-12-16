package com.qeweb.scm.epmodule.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpModuleMaterialRelEntity;
import com.qeweb.scm.epmodule.service.EpModuleMaterialRelService;
import com.qeweb.scm.epmodule.web.vo.MaterialModuleVO;

@Controller
@RequestMapping(value = "/manager/ep/epModuleMaterialRel")
public class EpModuleMaterialRelController implements ILog  {
	
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private EpModuleMaterialRelService relService;

	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/ep/epModuleMaterialRelList";                 
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="物料报价模型关系列表",module="询比价管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<EpModuleMaterialRelEntity> userPage = relService.getEpModuleMaterialRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value="getMaterialList")
	@ResponseBody
	public Map<String,Object> getMaterialList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialEntity> userPage = relService.getMaterialList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value="getModuleList")
	@ResponseBody
	public Map<String,Object> getModuleList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", 0);
		Page<EpModuleEntity> userPage = relService.getModuleList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "batchDelete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> batchDelete(@RequestBody List<EpModuleMaterialRelEntity> epModuleMaterialRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		relService.batchDelete(epModuleMaterialRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "saveRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveRel(@Valid EpModuleMaterialRelEntity rel){
		Map<String,Object> map = new HashMap<String, Object>();
		List<EpModuleMaterialRelEntity> olds=relService.findMaterialId(rel.getMaterialId());
		if(olds!=null&&olds.size()>0){
			map.put("success", false);
			map.put("msg", "此关系记录已存在，不能重复添加");
			return map;
		}
		relService.saveRel(rel);
		map.put("success", true);
		return map;
	}
	
	
	@LogClass(method="导入物料报价模型关系",module="物料报价模型关系")
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) {
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
			ExcelUtil<MaterialModuleVO> excelutil = new ExcelUtil<MaterialModuleVO>(savefile.getPath(), new ExcelAnnotationReader(MaterialModuleVO.class), getLogger()); 
			List<MaterialModuleVO> list = (List<MaterialModuleVO>) excelutil.readExcelFilterHeader(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = relService.saveSplit(list, logger);
			if(flag) {
				map.put("msg", "导入成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入失败!");  
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
	
	@Override
	public void log(Object message) {
		
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
}
