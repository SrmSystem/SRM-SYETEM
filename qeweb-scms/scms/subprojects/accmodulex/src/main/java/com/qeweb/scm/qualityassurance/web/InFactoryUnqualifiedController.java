package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
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
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.InFactoryUnqualifiedEntity;
import com.qeweb.scm.qualityassurance.service.InFactoryUnqualifiedService;
import com.qeweb.scm.qualityassurance.transfer.InFactoryUnqualifiedTransfer;

@Controller
@RequestMapping("/manager/qualityassurance/InFactoryUnqualified")
public class InFactoryUnqualifiedController  implements ILog {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private InFactoryUnqualifiedService service;
	private Map<String,Object> map;
	private ILogger logger=new FileLogger();
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/inFactoryUnqualified/inFactoryUnqualifiedList";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/inFactoryUnqualified/inFactoryUnqualifiedList";
	}
		
	@RequestMapping(value="/{vendor}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {		
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendorBaseInfoEntity.orgId", user.orgId + ""); 
			searchParamMap.put("EQ_vendorBaseInfoEntity.currentVersion", 1);
			searchParamMap.put("EQ_qualityStatus", 1); 
		}
		Page<InFactoryUnqualifiedEntity> page = service.getInFactoryUnqualifiedEntityList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;	
	}

	@RequestMapping(value="updateSave/{juct}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addInFactoryUnqualified(@PathVariable(value="juct") String juct,InFactoryUnqualifiedEntity entity)
	{
		if(juct.equals("add"))
		{
			map=service.addInFactoryUnqualified(entity);
		}
		else
		{
			map = new HashMap<String, Object>();
			map.put("success", true);
			map.put("msg", "无效连接");
		}
		return map;
	}  
	
	/**
	 * 导入
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) throws Exception {
		File savefile = null;
		map = new HashMap<String, Object>();
		String logpath = null; 
			try {
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
				ExcelUtil<InFactoryUnqualifiedTransfer> excelutil = new ExcelUtil<InFactoryUnqualifiedTransfer>(savefile.getPath(), new ExcelAnnotationReader(InFactoryUnqualifiedTransfer.class), getLogger()); 
				List<InFactoryUnqualifiedTransfer> list = excelutil.readExcel(0);
				if(excelutil.getErrorNum() > 0 || list.size() == 0) {
					throw new Exception("上传文件为空，或无内容");
				}
				//3、组装并保存数据
				log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
				boolean flag = service.saveInFactoryUnqualifiedTransfer(list, getLogger());
				if(flag) {
					map.put("msg", "导入入厂检验不合格信息成功!");
					map.put("success", true);
				} else {
					map.put("msg", "导入入厂检验不合格信息失败!");
					map.put("success", false);
				}
			} catch (Exception e) {
				map.put("msg", "导入入厂检验不合格信息失败!");  
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
	
	/**
	 * 批量发布
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "publishItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishItem(@RequestBody List<InFactoryUnqualifiedEntity> itemList){
		Map<String,Object> map = new HashMap<String, Object>();
		service.publishItems(itemList); 
		map.put("msg", "发布成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 计算PPM
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "calculate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> calculate(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1;
		String time = "";
		if(month < 10){
			time = year +"0"+month;
		}else{
			time = year +""+month;
		}
		service.calculate(time); 
		map.put("message", "计算成功!");
		map.put("success", true);
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
		// TODO Auto-generated method stub
		
	}
}
