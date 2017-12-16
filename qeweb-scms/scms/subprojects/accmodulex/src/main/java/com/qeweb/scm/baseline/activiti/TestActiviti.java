package com.qeweb.scm.baseline.activiti;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestActiviti {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Test
	public void deploymentProcessDefinition(){
		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		TaskService taskService = processEngine.getTaskService();
		ManagementService managementService = processEngine.getManagementService();
		IdentityService identityService = processEngine.getIdentityService();
		HistoryService historyService = processEngine.getHistoryService();
		FormService formService = processEngine.getFormService();
		
		Deployment dep = processEngine.getRepositoryService()
		.createDeployment().name("部署名test")
		.addClasspathResource("diagrams/Test.bpmn")
		.addClasspathResource("diagrams/Test.png").deploy();
		System.out.println(dep.getId());
		System.out.println(dep.getName());
		
	}
	@Test
	public void startProcessInstance(){
		String processDefinitionKey = "test1";
		ProcessInstance ins = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
		System.out.println(ins.getId());
		System.out.println(ins.getProcessDefinitionId());
	}
	
	@Test
	public void findMyPersonalTask(){
		String assignee = "张三";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						.taskAssignee(assignee)//指定个人任务查询，指定办理人
						.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
}