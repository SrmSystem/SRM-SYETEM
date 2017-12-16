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
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.NewProductEntity;
import com.qeweb.scm.qualityassurance.service.NewProductService;
import com.qeweb.scm.qualityassurance.transfer.NewProductTransfer;






@Controller
@RequestMapping("/manager/qualityassurance/Newproduct")
public class NewProductController  implements ILog {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private NewProductService newProductService;
	private Map<String,Object> map;
	private ILogger logger=new FileLogger();
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/newproduct/newproductList";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/newproduct/newproductList";
	}
		
	@RequestMapping(value="/{vendor}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Integer publishStatus=1;
		Integer PPAP1=0;
		Integer PPAP2=0;

		if(vendor) {		
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("EQ_dataStatus", publishStatus + "");
			PPAP1=newProductService.getPpap(1, 1,user.orgId);
			PPAP2=newProductService.getPpap(1, user.orgId);			
		}
		Page<NewProductEntity> page = newProductService.getNewProductEntityList(pageNumber,pageSize,searchParamMap);
		for (NewProductEntity newProductEntity : page) {
			if (PPAP2==0) newProductEntity.setPpap(0);
			else newProductEntity.setPpap(PPAP1/PPAP2);
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;	
	}

	
	/**
	 * 导入
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
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
				ExcelUtil<NewProductTransfer> excelutil = new ExcelUtil<NewProductTransfer>(savefile.getPath(), new ExcelAnnotationReader(NewProductTransfer.class), getLogger()); 
				List<NewProductTransfer> list = excelutil.readExcel(0);
				if(excelutil.getErrorNum() > 0 || list.size() == 0) {
					throw new Exception("上传文件为空，或无内容");
				}
				//3、组装并保存数据
				log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
				boolean flag = newProductService.saveNewProductTransfer(list, getLogger());
				if(flag) {
					map.put("msg", "导入新产品开发质量成功!");
					map.put("success", true);
				} else {
					map.put("msg", "导入新产品开发质量失败!");
					map.put("success", false);
				}
			} catch (Exception e) {
				map.put("msg", "导入新产品开发质量失败!");  
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
	public Map<String,Object> publishItem(@RequestBody List<NewProductEntity> itemList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		newProductService.publishItems(itemList, new UserEntity(user.id));
		map.put("message", "发布成功");
		map.put("success", true);
		return map;
	}	
	
	/**
	 * 计算PPAP一次通过率
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "calculate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> calculate() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1;
		String time = "";
		if(month < 10){
			time = year +"0"+month;
		}else{
			time = year +""+month;
		}
		newProductService.calculate(time); 
		map.put("message", "计算成功!");
		map.put("success", true);
		return map;
	}
	
	@Override
	public void log(Object message) {
		// TODO Auto-generated method stub
		
	}
}
