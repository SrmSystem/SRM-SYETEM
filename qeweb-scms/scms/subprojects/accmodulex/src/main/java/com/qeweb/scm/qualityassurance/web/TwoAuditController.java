package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.TwoAuditEntity;
import com.qeweb.scm.qualityassurance.entity.TwoAuditSolutionEntity;
import com.qeweb.scm.qualityassurance.service.TwoAuditService;


@Controller
@RequestMapping("/manager/qualityassurance/twoAudit")
public class TwoAuditController implements ILog{
	

	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private TwoAuditService twoAuditService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/twoaudit/twoAuditList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> correctionList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<TwoAuditEntity> page = twoAuditService.getcorrectionList(pageNumber, pageSize, searchParamMap);
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
		Page<TwoAuditEntity> page = twoAuditService.getcorrectionList2(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getSolutionSubmit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> solutionSubmit(){
		String uid=request.getParameter("uid");
		String typee=request.getParameter("typee");
		String sContent=request.getParameter("sContent");
		return twoAuditService.solutionSubmit(uid,typee,sContent);
	}
	
	@RequestMapping(value="getQrs",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> qrs(@RequestBody List<TwoAuditEntity> twoAuditEntities){
		return twoAuditService.qrs(twoAuditEntities);
	}
	@RequestMapping(value="getLookSolution/{id}")
	@ResponseBody
	public Map<String,Object> lookSolution(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<TwoAuditSolutionEntity> page = twoAuditService.lookSolution(pageNumber, pageSize, searchParamMap,id);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getEndSolution/{usContent}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> endSolution(@PathVariable("usContent") String usContent,@RequestBody List<TwoAuditEntity> twoAuditEntitys){
		return twoAuditService.endSolution(twoAuditEntitys,usContent);
	}
	@RequestMapping(value="getCorrectionSolution")
	public String getCorrectionSolution(){
		return "back/twoaudit/twoAuditVenList";
	}
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files,String uid,String solutionContent) {
		File savefile = null;
		map = new HashMap<String, Object>();
		try{ 
			String url="";
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，上传失败");
				throw new Exception();
			}
			//2、组装并保存数据			
			url=savefile.getPath().replaceAll("\\\\", "/");
			boolean flag = twoAuditService.combine(uid,solutionContent,url);
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
	//审核  计划  附件上传	
	@RequestMapping("planUpload")
	@ResponseBody
	public Map<String,Object> fileUpload(@RequestParam("planfiles") MultipartFile files,TwoAuditEntity PlanEntity ) {
		File savefile = null;
		String logpath = null; 
		String fName = null;
		String Millis = ""+System.currentTimeMillis();
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，上传失败");
				throw new Exception();
			}
			//2、组装并保存数据			
			fName = files.getOriginalFilename();
			map = twoAuditService.planUpLoad(savefile,getLogger(),PlanEntity,Millis,fName);
		} catch (Exception e) {
			map.put("msg", "上传附件失败!");  
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
