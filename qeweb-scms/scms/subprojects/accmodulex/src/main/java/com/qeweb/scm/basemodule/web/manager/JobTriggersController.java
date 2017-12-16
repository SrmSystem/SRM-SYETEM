package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.JobTriggersEntity;
import com.qeweb.scm.basemodule.quartz.service.SchedulerServiceImpl;
import com.qeweb.scm.basemodule.service.JobTriggersService;

@Controller
@RequestMapping("/manager/basedata/jobTriggers")
public class JobTriggersController {
	
	@Autowired
	private JobTriggersService jobTriggersService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)  
	public String list(Model model) {
		return "back/basedata/taskList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> triggersList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<JobTriggersEntity> userPage = jobTriggersService.getTriggersList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}  

	/**
	 * 新增
	 * @param trigger
	 * @return
	 */
	@RequestMapping(value = "addNewTrigger",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewTrigger(@Valid JobTriggersEntity trigger){   
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
			scheduler.schedule(trigger.getTriggerName(), trigger.getJobName(), trigger.getTriggerGroup(), trigger.getCronExpression()); 
		}catch(Exception e) {  
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "新增失败" + e.getMessage());      
			return map;    
		}
		map.put("success", true);
		map.put("message", "新增成功");
		return map;
	}   
	
	/**
	 * 删除
	 * @param triggersList
	 * @return
	 */
	@RequestMapping(value = "deleteTrigger",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteTrigger(@RequestParam("schedName") String schedName, 
			@RequestParam("triggerName") String triggerName, @RequestParam("triggerGroup") String triggerGroup){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
			scheduler.removeTrigdger(triggerName, triggerGroup);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "删除失败" + e.getMessage());
		}	
		map.put("success", true);
		map.put("message", "删除成功");
		return map;
	}
	
	@RequestMapping(value = "pauseTrigger", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pauseTrigger(@RequestParam("schedName") String schedName, 
			@RequestParam("triggerName") String triggerName, @RequestParam("triggerGroup") String triggerGroup) {
		map = new HashMap<String, Object>();
		try{
			SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
			scheduler.pauseTrigger(triggerName, triggerGroup);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "触发器暂停失败" + e.getMessage());
		}
		map.put("success", true);
 		map.put("message", "触发器暂停成功");
		return map;
	}
	
	@RequestMapping(value = "resumeTrigger", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> resumeTrigger(@RequestParam("schedName") String schedName, 
			@RequestParam("triggerName") String triggerName, @RequestParam("triggerGroup") String triggerGroup) {
		map = new HashMap<String, Object>();
		try{
			SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
			scheduler.resumeTrigger(triggerName, triggerGroup);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "触发器启动失败" + e.getMessage());
		}
		map.put("success", true);
		map.put("message", "触发器启动成功");
		return map;
	}
	
	/**
	 * 修改
	 * @param triggersList
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@RequestParam("schedName") String schedName, 
			@RequestParam("triggerName") String triggerName, @RequestParam("triggerGroup") String triggerGroup, @Valid JobTriggersEntity trigger) {
		map = new HashMap<String, Object>();
		
		map.put("success", true);
		map.put("message", "更新成功");
		return map;
	}
	
	@RequestMapping(value = "runTrigger", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> runTrigger(@Valid JobTriggersEntity runTrigger) {
		map = new HashMap<String, Object>();
		map.put("success", true);
		
		map.put("message", "触发器运行成功");
		return map;
	}

}
