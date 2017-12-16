package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
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

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionSolutionEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforCorrectionService;

@Controller
@RequestMapping("/manager/vendor/VendorPerforCorrection")
public class VendorPerforCorrectionController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforCorrectionService correctionService;
	
	@LogClass(method="查看", module="供应商整改管理")
	@RequiresPermissions("perfor:correct:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/correctionList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> correctionList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforCorrectionEntity> page = correctionService.getcorrectionList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="correctionList2",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> correctionList2(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforCorrectionEntity> page = correctionService.getcorrectionList2(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequiresPermissions("perfor:correct:audit")
	@RequestMapping(value="getSolutionSubmit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> solutionSubmit(){
		String uid=request.getParameter("uid");
		String typee=request.getParameter("typee");
		String sContent=request.getParameter("sContent");
		return correctionService.solutionSubmit(uid,typee,sContent);
	}
	
	@RequiresPermissions("perfor:correct:share")
	@RequestMapping(value="getQrs",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> qrs(@RequestBody List<VendorPerforCorrectionEntity> vendorPerforCorrectionEntitys){
		return correctionService.qrs(vendorPerforCorrectionEntitys);
	}
	@RequiresPermissions("perfor:correct:see")
	@RequestMapping(value="getLookSolution/{id}")
	@ResponseBody
	public Map<String,Object> lookSolution(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforCorrectionSolutionEntity> page = correctionService.lookSolution(pageNumber, pageSize, searchParamMap,id);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequiresPermissions("perfor:correct:close")
	@RequestMapping(value="getEndSolution/{usContent}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> endSolution(@PathVariable("usContent") String usContent,@RequestBody List<VendorPerforCorrectionEntity> vendorPerforCorrectionEntitys) throws Exception{
		return correctionService.endSolution(vendorPerforCorrectionEntitys,usContent);
	}
	
	@LogClass(method="查看", module="供应商整改管理(供)")
	@RequestMapping(value="getCorrectionSolution")
	public String getCorrectionSolution(){
		return "back/performance/vendorPerforCorrectionSolution";
	}
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files,String uid,String solutionContent) {
		File savefile = null;
		String logpath = null; 
		map = new HashMap<String, Object>();
		try{ 
			String url="";
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				map.put("msg", "提交失败!请上传文件。");  
				map.put("success", false);
				return map;   
			}
			//2、组装并保存数据			
			url=savefile.getPath().replaceAll("\\\\", "/");
			boolean flag = correctionService.combine(uid,solutionContent,url);
			if(flag) {
				map.put("msg", "提交成功!");
				map.put("success", true);
			} else {
				map.put("msg", "提交失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "提交失败!");  
			map.put("success", false);
			e.printStackTrace();
		}
		return map;   
	}
	@RequestMapping(value="getEnable3",method = RequestMethod.POST)
	@ResponseBody
	public String getEnable3(Model model,ServletRequest request){
		return correctionService.getEnableList3();
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
