package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.JobDetailEntity;
import com.qeweb.scm.basemodule.entity.JobDetailEntityKey;
import com.qeweb.scm.basemodule.quartz.service.SchedulerServiceImpl;
import com.qeweb.scm.basemodule.service.JobDetailService;

@Controller
@RequestMapping("/manager/basedata/task")
public class TaskController {
	
	@Autowired
	private JobDetailService jobDetailService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)  
	public String list(Model model) {
		return "back/basedata/taskList";
	}
	
	@LogClass(method="查看", module="任务管理")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> materialList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<JobDetailEntity> userPage = jobDetailService.getTaskList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getTask/{schedName}/{jobName}/{jobGroup}")
	@ResponseBody
	public JobDetailEntity getTask(@PathVariable("schedName") String schedName, 
			@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup){
		JobDetailEntity jobDetail =  jobDetailService.getTask(new JobDetailEntityKey(schedName, jobName, jobGroup));
		jobDetail.setSchedName(jobDetail.getKey().getSchedName());
		jobDetail.setJobName(jobDetail.getKey().getJobName());
		jobDetail.setJobGroup(jobDetail.getKey().getJobGroup());
		return jobDetail;
	}
	
	/**
	 * 修改
	 * @param serialNumber
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("taskDetail") JobDetailEntity taskDetail) {
		map = new HashMap<String, Object>();
		jobDetailService.updatejobDetail(taskDetail);
		map.put("success", true);
		map.put("message", "更新成功");
		return map;
	}
	
	/**
	 * 删除任务
	 * @param schedName
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@RequestMapping(value = "deleteTask",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteTask(@RequestParam("schedName") String schedName, 
			@RequestParam("jobName") String jobName,@RequestParam("jobGroup") String jobGroup){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			jobDetailService.deleteTask(new JobDetailEntity(new JobDetailEntityKey(schedName, jobName, jobGroup)));  
		}catch(Exception e) {
			map.put("success", false);
			map.put("message", "删除任务失败");
			return map;
		}
		map.put("success", true);
		map.put("message", "删除成功");
		return map;
	}
	
	/**
	 * 立即触发
	 * @param schedName
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	@RequestMapping(value = "execTask", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> execTask(@RequestParam("schedName") String schedName, 
			@RequestParam("jobName") String jobName,@RequestParam("jobGroup") String jobGroup){
		map = new HashMap<String, Object>();
		SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
		try {
			scheduler.getScheduler().triggerJob(new JobKey(jobName));
		} catch (SchedulerException e) {
			e.printStackTrace();
			map.put("success", false); 
			map.put("message", "执行失败" + e.getMessage());    
			return map;
		}
		map.put("success", true);
		map.put("message", "执行成功");
		return map;
	}
}
