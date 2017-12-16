package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.qeweb.scm.qualityassurance.entity.ZeroKilometersEntity;
import com.qeweb.scm.qualityassurance.service.ZeroKilometersService;
import com.qeweb.scm.qualityassurance.transfer.ZeroKilometersTransfer;

@Controller
@RequestMapping("/manager/qualityassurance/zeroKilometers")
public class ZeroKilometersController implements ILog {
	private Map<String, Object> map;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ZeroKilometersService zeroKilometersService;

	private ILogger logger = new FileLogger();

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/zeroKilometers/zeroKilometersList";
	}

	@RequestMapping(value = "vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/zeroKilometers/zeroKilometersVenList";
	}

	@RequestMapping(value = "/{vendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(@PathVariable(value = "vendor") boolean vendor,
			@RequestParam(value = "page") int pageNumber, @RequestParam(value = "rows") int pageSize, Model model,
			ServletRequest request) {
		Map<String, Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if (vendor) {
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendorBaseInfoEntity.orgId", user.orgId + ""); 
			searchParamMap.put("EQ_vendorBaseInfoEntity.currentVersion", 1);
			searchParamMap.put("EQ_status", 1);
		}
		Page<ZeroKilometersEntity> userPage = zeroKilometersService.getZeroKilometersList(pageNumber, pageSize,
				searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", userPage.getContent());
		map.put("total", userPage.getTotalElements());
		return map;
	}

	@RequestMapping(value = "saveAdd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveAdd(@Valid ZeroKilometersEntity zkilo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String year = zkilo.getEndTime().getYear()+1900+"";
		int month = zkilo.getEndTime().getMonth() +1;
		String mon = "";
		if(month <10){
			mon = "0"+month;
		}else{
			mon = month +"";
		}
		String time = year+mon;
		zkilo.setMonth(time);
		zeroKilometersService.saveAdd(zkilo);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value = "getZkList/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ZeroKilometersEntity getMaterial(@PathVariable("id") Long id) {
		return zeroKilometersService.getZkListById(id);
	}

	/**
	 * 批量发布
	 * 
	 * @param zeroKilometersList
	 * @return
	 */
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publish(@RequestBody List<ZeroKilometersEntity> zeroKilometersList) {
		Map<String, Object> map = new HashMap<String, Object>();
		zeroKilometersService.publish(zeroKilometersList);
		;
		map.put("success", true);
		return map;
	}
	
	/**
	 * 计算PPM
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "calculate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> calculate() throws Exception{
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1;
		String time = "";
		if(month < 10){
			time = year +"0"+month;
		}else{
			time = year +""+month;
		}
		zeroKilometersService.calculate(time); 
		map.put("message", "计算成功!");
		map.put("success", true);
		return map;
	}

	// 批量导入
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String, Object> filesUpload(@RequestParam("impFile") MultipartFile files) {
		File savefile = null;
		String logpath = null;
		try {
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			// 1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if (savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			// 2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(),
					new ExcelAnnotationReader(ZeroKilometersTransfer.class), getLogger());
			List<ZeroKilometersTransfer> list = excelutil.readExcel(0);
			if (excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			// 3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = zeroKilometersService.imp(list, getLogger());
			if (flag) {
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
