package com.qeweb.scm.qualityassurance.web;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.service.PPMEntityService;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer1;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer2;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer3;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer4;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer5;

@Controller
@RequestMapping(value = "/manager/qualityassurance/ppm")
public class PPMEntityController  implements ILog {

	private Map<String,Object> map;
	
	private ILogger logger=new FileLogger();
	
	@Autowired
	private PPMEntityService ppmEntityService;
	
	@RequestMapping(value="1",method = RequestMethod.GET)
	public String list1(Model model) {
		model.addAttribute("ppmType", 1);
		return "back/ppm/ppmList";      
	}
	
	@RequestMapping(value="2",method = RequestMethod.GET)
	public String list2(Model model) {
		model.addAttribute("ppmType", 2);
		return "back/ppm/ppmList";      
	}
	
	@RequestMapping(value="3",method = RequestMethod.GET)
	public String list3(Model model) {
		model.addAttribute("ppmType", 3);
		return "back/ppm/ppmList";      
	}
	
	@RequestMapping(value="4",method = RequestMethod.GET)
	public String list4(Model model) {
		model.addAttribute("ppmType", 4);
		return "back/ppm/ppmList";      
	}
	
	@RequestMapping(value="5",method = RequestMethod.GET)
	public String list5(Model model) {
		model.addAttribute("ppmType", 5);
		return "back/ppm/ppmList";      
	}
	
	@RequestMapping(value="/{ppmType}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="ppmType") Integer ppmType,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_ppmType", ppmType); 
		Page<PPMEntity> userPage = ppmEntityService.getPPmList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@LogClass(method="导出报表",module="质量管理")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("exportExcel/{ppmType}")
	public String exportExcel(@PathVariable(value="ppmType") Integer ppmType, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("质量管理", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_ppmType", ppmType); 
		if(ppmType ==1){
			List<PPMTransfer1> list = ppmEntityService.getPPMVo1(searchParamMap);
			ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PPMTransfer1.class), null);  
			excelUtil.export(list);  
		}
		if(ppmType ==2){
			List<PPMTransfer2> list = ppmEntityService.getPPMVo2(searchParamMap);
			ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PPMTransfer2.class), null);  
			excelUtil.export(list);  
		}
		if(ppmType ==3){
			List<PPMTransfer3> list = ppmEntityService.getPPMVo3(searchParamMap);
			ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PPMTransfer3.class), null);  
			excelUtil.export(list);  
		}
		if(ppmType ==4){
			List<PPMTransfer4> list = ppmEntityService.getPPMVo4(searchParamMap);
			ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PPMTransfer4.class), null);  
			excelUtil.export(list);  
		}
		if(ppmType ==5){
			List<PPMTransfer5> list = ppmEntityService.getPPMVo5(searchParamMap);
			ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PPMTransfer5.class), null);  
			excelUtil.export(list);  
		}
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
		getLogger().log(message); 
	} 

}
