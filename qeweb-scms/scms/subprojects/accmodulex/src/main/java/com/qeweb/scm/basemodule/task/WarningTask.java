package com.qeweb.scm.basemodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.basemodule.service.WarningService;

@Component
@Scope(value = "prototype")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class WarningTask extends AbstractJobBean{
	
	@Autowired
	private static WarningService warningService;

	@Override
	protected void prepare() {
		
	}

	@Override
	public TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		tr.isCompleted = getWarningService().execute(getTaskLogger());
		return tr;
		
	}

	@Override
	protected void destory() {
		// TODO Auto-generated method stub
		
	}

	public static WarningService getWarningService() {
		if(warningService == null) 
			warningService = SpringContextUtils.getBean("warningService");  
		return warningService;
	}

	
	

}
