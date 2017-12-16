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
import com.qeweb.scm.sap.service.CompanyFactoryRelSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class CompanyFactoryRelSyncTask extends AbstractJobBean {

	private CompanyFactoryRelSyncService companyFactoryRelSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getCompanyFactoryRelSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	
	

	public CompanyFactoryRelSyncService getCompanyFactoryRelSyncService() {
		if(companyFactoryRelSyncService == null)
			companyFactoryRelSyncService = SpringContextUtils.getBean("companyFactoryRelSyncService");
		
		return companyFactoryRelSyncService;
	}

	public void setCompanyFactoryRelSyncService(
			CompanyFactoryRelSyncService companyFactoryRelSyncService) {
		this.companyFactoryRelSyncService = companyFactoryRelSyncService;
	}


	
}
