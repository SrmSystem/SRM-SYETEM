package com.qeweb.scm.basemodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.sap.service.GroupFactoryRelSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class GroupFactoryRelSyncTask extends AbstractJobBean {

	private GroupFactoryRelSyncService groupFactoryRelSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getGroupFactoryRelSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	
	

	public GroupFactoryRelSyncService getGroupFactoryRelSyncService() {
		if(groupFactoryRelSyncService == null)
			groupFactoryRelSyncService = SpringContextUtils.getBean("groupFactoryRelSyncService");
		
		return groupFactoryRelSyncService;
	}

	public void setGroupFactoryRelSyncService(
			GroupFactoryRelSyncService groupFactoryRelSyncService) {
		this.groupFactoryRelSyncService = groupFactoryRelSyncService;
	}


	
}
