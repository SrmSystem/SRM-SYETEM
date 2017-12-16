package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforDataInService;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforDataInVo;

@Controller
@RequestMapping("/manager/vendor/vendorPerforDataIn")
public class VendorPerforDataInController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforDataInService dataInService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/vendorPerforDataInList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> levelList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforDataInEntity> page = dataInService.getVendorPerforDateInList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="update",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updata()
	{
		String useid=request.getParameter("useid");
		String elementValue=request.getParameter("elementValue");
		return dataInService.update(useid,elementValue);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files,Long cycleId) {
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			VendorPerforCycleEntity vendorPerforCycleEntity=dataInService.getVendorPerforCycleEntity(cycleId);
			logger.log("------------------->开始检查导入时间<---------------------");
			if(!((Boolean) dataInService.juectDataInDate(logger,vendorPerforCycleEntity,DateUtil.getCurrentTimestamp(),1)))
			{
				map.put("msg", "已经不是导入时间!");
				map.put("success", false);
				return map;  
			}
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(VendorPerforDataInVo.class), getLogger()); 
			List<VendorPerforDataInVo> list = (List<VendorPerforDataInVo>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = dataInService.combine(list, getLogger(),vendorPerforCycleEntity);
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
	
	@RequestMapping(value="/getVendorPerforCycle")
	@ResponseBody
	public String getVendorPerforCycle()
	{
		return dataInService.getVendorPerforCycle();
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
