package com.qeweb.scm.abnormal.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.abnormal.task.service.AbnormalFeedbackCheckService;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;

@Component
@Scope(value = "prototype")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class AbnormalFeedbackCheckTask extends AbstractJobBean{
	
	@Autowired
	private static AbnormalFeedbackCheckService abnormalFeedbackCheckService;

	@Override
	protected void prepare() {
		
	}

	@Override
	public TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getAbnormalFeedbackCheckService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
		
	}

	@Override
	protected void destory() {
		// TODO Auto-generated method stub
		
	}

	public static AbnormalFeedbackCheckService getAbnormalFeedbackCheckService() {
		if(abnormalFeedbackCheckService == null) 
			abnormalFeedbackCheckService = SpringContextUtils.getBean("abnormalFeedbackCheckService");  
		return abnormalFeedbackCheckService;
	}

}
