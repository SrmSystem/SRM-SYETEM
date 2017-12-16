package com.qeweb.scm.purchasemodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.sap.service.NoCheckedItemsSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class NoCheckedItemsTask extends AbstractJobBean {

	private NoCheckedItemsSyncService noCheckedItemsSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getNoCheckedItemsSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}
	
	public NoCheckedItemsSyncService getNoCheckedItemsSyncService() {
		if(noCheckedItemsSyncService == null){
			noCheckedItemsSyncService = SpringContextUtils.getBean("noCheckedItemsSyncService");
		}
		
		return noCheckedItemsSyncService;
	}
	
	
	public void setNoCheckedItemsSyncService(
			NoCheckedItemsSyncService noCheckedItemsSyncService) {
		this.noCheckedItemsSyncService = noCheckedItemsSyncService;
	}

	@Override
	protected void destory() {
			
	}
	
	
	
}
