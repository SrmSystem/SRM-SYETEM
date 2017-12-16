package com.qeweb.scm.filemodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.filemodule.task.service.FileCollaborationCheckService;

@Component
@Scope(value = "prototype")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class FileCollaborationCheckTask extends AbstractJobBean{
	
	@Autowired
	private static FileCollaborationCheckService fileCollaborationCheckService;

	@Override
	protected void prepare() {
		
	}

	@Override
	public TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getFileCollaborationCheckService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
		
	}

	@Override
	protected void destory() {
		// TODO Auto-generated method stub
		
	}

	public static FileCollaborationCheckService getFileCollaborationCheckService() {
		if(fileCollaborationCheckService == null) 
			fileCollaborationCheckService = SpringContextUtils.getBean("fileCollaborationCheckService");  
		return fileCollaborationCheckService;
	}

}
