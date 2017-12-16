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
import com.qeweb.scm.purchasemodule.task.service.CreateCheckService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class CreateCheckTask extends AbstractJobBean {

	private CreateCheckService createCheckService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getCreateCheckService().create(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}

	public CreateCheckService getCreateCheckService() {
		if(createCheckService == null)
			createCheckService = SpringContextUtils.getBean("createCheckService");
		
		return createCheckService;
	}

	public void setCreateCheckService(CreateCheckService createCheckService) {
		this.createCheckService = createCheckService;
	}
	
}
