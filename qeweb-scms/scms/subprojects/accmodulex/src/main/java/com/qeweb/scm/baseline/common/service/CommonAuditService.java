package com.qeweb.scm.baseline.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.baseline.common.entity.BaseAuditEntity;
import com.qeweb.scm.baseline.common.entity.Order_ProcessDefinitionEntity;
import com.qeweb.scm.baseline.common.repository.BaseDao;
import com.qeweb.scm.baseline.common.repository.Order_ProcessDefinitionDao;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.StringUtils;



@Service
@Transactional(rollbackOn=Exception.class)
public class CommonAuditService extends BaseService{
	
	public static final String submit_status_string = "提交审批";

	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private Order_ProcessDefinitionDao defDao;
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 审核
	 * @param entityName
	 * @param audit
	 * @param pks
	 */
	public void doAudit(String entityName,int audit,String pks) throws Exception{
		String [] pkss = pks.split(",");
		Class<?> clazz = SpringContextUtils.getBean(entityName).getClass();
		for(String pk:pkss){
			if(StringUtils.isEmpty(pk)){
				continue;
			}
			Object o = baseDao.findOne(clazz, Long.valueOf(pk.trim()));
			BaseAuditEntity ae = (BaseAuditEntity) o;
			if(ae.getAuditStatus()!=ae.auditStauts_submit){
				throw new Exception("单据的审核状态必须为待审核。");
			}
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			List<Task> list = processEngine.getTaskService()
					.createTaskQuery()//.taskAssignee(user.loginName)
					.processInstanceId(ae.getProcessIns())
					.list();
			if(list.isEmpty()){
				throw new Exception("未找到该流程实例对应的任务。");
			}
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("auditStatus", (long)audit);
			processEngine.getTaskService().complete(list.get(0).getId(),variables);
			List<Task> list2 = processEngine.getTaskService()
					.createTaskQuery()//.taskAssignee(user.loginName)
					.processInstanceId(ae.getProcessIns())
					.list();
			if(list2.isEmpty()){
				//说明流程已经走完
				ae.setCurrentAuditUser(null);
				ae.setAuditStatus(audit);
			}else{
				if(submit_status_string.equals(list2.get(0).getName())){
					//驳回到提交人，单据状态改为已驳回
					ae.setCurrentAuditUser(null);
					ae.setAuditStatus(BaseAuditEntity.auditStauts_reject);
				}else{
					String userCodes = ",";
					for(Task t:list2){
						userCodes += t.getAssignee() + ",";
					}
					ae.setCurrentAuditUser(userCodes);
				}
			}
			baseDao.save(ae);
		}
	}
	/**
	 * 提交审核
	 * 需要同时触发审核流程的提交任务节点
	 * @param entityName
	 * @param pks
	 */
	public void submit(String entityName,String pks) throws Exception{
		String [] pkss = pks.split(",");
		Class<?> clazz = SpringContextUtils.getBean(entityName).getClass();
		for(String pk:pkss){
			if(StringUtils.isEmpty(pk)){
				continue;
			}
			Object o = baseDao.findOne(clazz, Long.valueOf(pk.trim()));
			BaseAuditEntity ae = (BaseAuditEntity) o;
			if(ae.getAuditStatus()!=ae.auditStauts_no&&ae.getAuditStatus()!=ae.auditStauts_reject){
				throw new Exception("单据的审核状态必须为驳回或未提交。");
			}
			ae.setAuditStatus(BaseAuditEntity.auditStauts_submit);
			//如果未启动流程，则先启动流程。如果已启动流程，则直接提交任务。
			if(ae.getProcessIns()==null){
				Order_ProcessDefinitionEntity def = defDao.findByEntityName(entityName);
				if(def == null){
					throw new Exception("当前单据没有部署对应的审核流程！");
				}
				ProcessInstance ins = processEngine.getRuntimeService().startProcessInstanceByKey(def.getProcessDefinitionId());
				ae.setProcessIns(ins.getId());
			}
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			List<Task> list = processEngine.getTaskService()
					.createTaskQuery()
					.processInstanceId(ae.getProcessIns())//指定个人任务查询，指定办理人
					.list();
			if(list.isEmpty()){
				throw new Exception("未找到该流程实例对应的任务。");
			}
			if(!submit_status_string.equals(list.get(0).getName())){
				throw new Exception("任务名称必须为提交审批。");
			}
			//if(list.get(0).getAssignee()==null){
				list.get(0).setAssignee(user.loginName);
			//}
			if(!user.loginName.equals(list.get(0).getAssignee())){
				throw new Exception("任务的办理人必须为："+user.name);
			}
			processEngine.getTaskService().complete(list.get(0).getId());
			List<Task> list2 = processEngine.getTaskService()
					.createTaskQuery()
					.processInstanceId(ae.getProcessIns())//指定个人任务查询，指定办理人
					.list();
			if(list2.isEmpty()){
				throw new Exception("未找到该流程实例对应的任务。");
			}
			String userCodes = ",";
			for(Task t:list2){
				userCodes += t.getAssignee() + ",";
			}
			ae.setCurrentAuditUser(userCodes);
			baseDao.save(ae);
		}
	}
	
}
