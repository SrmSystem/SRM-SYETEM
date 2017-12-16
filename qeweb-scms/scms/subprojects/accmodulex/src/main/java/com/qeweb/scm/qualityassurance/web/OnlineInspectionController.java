package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.OnlineInspectionEntity;
import com.qeweb.scm.qualityassurance.service.OnlineInspectionService;
import com.qeweb.scm.qualityassurance.transfer.OnlineInspectionTransfer;

@Controller
@RequestMapping("/manager/qualityassurance/OnlineInspection")
public class OnlineInspectionController  implements ILog {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private OnlineInspectionService service;
	private Map<String,Object> map;
	private ILogger logger=new FileLogger();
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/onlineInspection/onlineInspectionList";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/onlineInspection/onlineInspectionList";
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
		Page<OnlineInspectionEntity> page = service.getOnlineInspectionEntityList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;	
	}

	@RequestMapping(value="updateSave/{juct}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addOnlineInspection(@PathVariable(value="juct") String juct,OnlineInspectionEntity entity)
	{
		if(juct.equals("add"))
		{
			map=service.addOnlineInspection(entity);
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
				ExcelUtil<OnlineInspectionTransfer> excelutil = new ExcelUtil<OnlineInspectionTransfer>(savefile.getPath(), new ExcelAnnotationReader(OnlineInspectionTransfer.class), getLogger()); 
				List<OnlineInspectionTransfer> list = excelutil.readExcel(0);
				if(excelutil.getErrorNum() > 0 || list.size() == 0) {
					throw new Exception("上传文件为空，或无内容");
				}
				//3、组装并保存数据
				log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
				boolean flag = service.saveOnlineInspectionTransfer(list, getLogger());
				if(flag) {
					map.put("msg", "导入在线稽查不合格信息成功!");
					map.put("success", true);
				} else {
					map.put("msg", "导入在线稽查不合格信息失败!");
					map.put("success", false);
				}
			} catch (Exception e) {
				map.put("msg", "导入在线稽查不合格信息失败!");  
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
	public Map<String,Object> publishItem(@RequestBody List<OnlineInspectionEntity> itemList){
		Map<String,Object> map = new HashMap<String, Object>();
		service.publishItems(itemList); 
		map.put("message", "发布成功");
		map.put("success", true);
		return map;
	}	
	
	@RequestMapping(value = "createEightD",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createEightD(@RequestBody Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		boolean flag = service.createEightD(id);
		if (flag){
			map.put("message", "发起成功");
			map.put("success", true);
		}else{
			map.put("message", "发起失败");
			map.put("success", false);
		}
		return map;
	}
	
	@LogClass(method="导出报表",module="质量管理")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("exportExcel")
	public String exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("质量管理", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<OnlineInspectionTransfer> list = service.getOnlineTransfer((List<OnlineInspectionEntity>) map.get("rows"));
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(OnlineInspectionTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
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
