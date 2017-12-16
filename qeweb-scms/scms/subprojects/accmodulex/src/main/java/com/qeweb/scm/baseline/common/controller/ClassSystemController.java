package com.qeweb.scm.baseline.common.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.baseline.common.entity.ClassSystemEntity;
import com.qeweb.scm.baseline.common.entity.HolidaysEntity;
import com.qeweb.scm.baseline.common.service.IClassSystemService;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
@Controller
@RequestMapping(value = "/manager/common/classSystem")
public class ClassSystemController {

	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	

	
	@Autowired
	private IClassSystemService classService ;
	
	
	@RequestMapping(method=RequestMethod.GET)
    public String list(Model model){
		return "back/common/classSystem";
	}
	@RequestMapping(value="toHolidays")
	public String toHolidays(Model model){
		return "back/common/holidays";
	}
	
	@RequestMapping(value="getHolidays")
	@ResponseBody
	public Map<String,Object> getHolidays(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		Page<HolidaysEntity> userPage = classService.getHolidays(pageNumber,pageSize,searchParamMap,request.getParameter("sort"),request.getParameter("order"));
		map = new HashMap<String, Object>();
		map.put("rows", userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	/**
	 * 保存节假日
	 * @param holidays
	 * @return
	 */
	@RequestMapping(value="addHolidays")
	@ResponseBody
	public Map<String,Object> addHolidays(@Valid HolidaysEntity holidays){
		classService.saveHolidays(holidays);
		map.put("success", true);
		map.put("msg", "操作成功");
		return map;
	}
	
	@RequestMapping(value="deleteHolidays/{id}")
	@ResponseBody
	public Map<String,Object> deleteHolidays(@PathVariable(value="id") Long id){
		
			classService.deleteHolidays(id);
			map.put("success", true);
			map.put("msg", "操作成功");
		return map;
	}
	@RequestMapping(value="getClassSystem")
	@ResponseBody
	public Map<String,Object> getClassSystem(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		Page<ClassSystemEntity> userPage = classService.getClassSystemEntity(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
        map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		
		return map;
	}
	@RequestMapping(value="getClassSystemById/{id}")
	@ResponseBody
	public ClassSystemEntity getClassSystemById(@PathVariable(value="id") Long id){
		ClassSystemEntity classSystem = classService.findClassSystem(id);
		
		return classSystem;
	}
	
	@RequestMapping(value="findAllClassSystem")
	@ResponseBody
	public List<ClassSystemEntity> findAllClassSystem(){
		List<ClassSystemEntity> classSystemList = classService.findAllClassSystem();
		return classSystemList;
	}
	
	
	@RequestMapping(value="findEffective")
	@ResponseBody
	public List<ClassSystemEntity> findEffective(){
		List<ClassSystemEntity> classSystemList = classService.findEffective();
		return classSystemList;
	}
	
	@RequestMapping(value="setTime")
	@ResponseBody
	public List<SetTime> setTime(){
		List<SetTime> setTimes = new ArrayList<SetTime>();
		double [] values = new double [48] ;
		int j = 0;
		for(int i = 0;i <= 23;i++){
			values[j] = i;
			values[++j] = i+0.3;
			j++;
		}
		DecimalFormat df = new DecimalFormat("######0.00");
      for(double d:values){
    	  SetTime setTime = new SetTime();
    	  String format = df.format(d);
    	  setTime.value = Double.parseDouble(format);
    	  setTime.text  =  format.replace('.', ':');
    	  setTimes.add(setTime);
      }
      
		return setTimes;
		
	}
	@RequestMapping(value="deleteClassSystem/{id}")
	@ResponseBody
	public Map<String,Object> deleteClassSystem(@PathVariable(value="id") Long id){
		
			classService.deleteClassSystem(id);
			map.put("success", true);
			map.put("msg", "操作成功");
		return map;
	}
	@RequestMapping(value="addClassSystem")
	@ResponseBody
	public Map<String,Object> addClassSystemEntity(@Valid ClassSystemEntity classSystem){
		    if(classSystem.getMorningStart()== null){
		    	classSystem.setMorningStart(0.0);
		    }
			 if(classSystem.getMorningEnd()== null){
				 classSystem.setMorningEnd(0.0);	
			 }
			 if(classSystem.getAfterStart()== null){
				 classSystem.setAfterStart(0.0);
			 }
			 if(classSystem.getAfterEnd()== null){
				 classSystem.setAfterEnd(0.0);
			 }
		
		
			classService.saveClassSystem(classSystem);
			map.put("success", true);
			map.put("msg", "操作成功");
		return map;
	}
	
	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	class SetTime{
		double value;
		String text;
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
	};
	
	
	
	

}
