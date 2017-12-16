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
import com.qeweb.scm.sap.service.OrganizationSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class OrgSyncTask extends AbstractJobBean {

	private OrganizationSyncService organizationSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getOrganizationSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	
	

	public OrganizationSyncService getOrganizationSyncService() {
		if(organizationSyncService == null)
			organizationSyncService = SpringContextUtils.getBean("organizationSyncService");
		
		return organizationSyncService;
	}

	public void setOrganizationSyncService(
			OrganizationSyncService organizationSyncService) {
		this.organizationSyncService = organizationSyncService;
	}


	
}
