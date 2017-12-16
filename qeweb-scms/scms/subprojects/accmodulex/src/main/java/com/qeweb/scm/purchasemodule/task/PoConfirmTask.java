package com.qeweb.scm.purchasemodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.purchasemodule.task.service.PoConfirmService;


@Component
@Scope(value = "prototype")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class PoConfirmTask extends AbstractJobBean {
	
	@Autowired
	private static PoConfirmService poConfirmService;
	
	public PoConfirmTask() throws Exception {
//		log.destory();
//		log.init(this);
	}

	@Override
	protected void prepare() {
		log("PoConfirmTask prepared...");
	}

	/**
	 * 重写继承的exec方法
	 */
	@Override
	public TaskResult exec() throws Exception {
		log("MaterialSyncTask exec start");
		TaskResult tr = new TaskResult();
		tr.isCompleted = getService().execute(getTaskLogger());
		log("MaterialSyncTask exec end");
		return tr;
	}
	
	/**
	 * 获取poConfirmService，获取不到则重新从Spring容器中获取
	 */
	public static PoConfirmService getService() {
		if(poConfirmService == null) 
			poConfirmService = SpringContextUtils.getBean("poConfirmService");  
		return poConfirmService;
	}
	
	/**
	 * 重写继承的destory方法
	 */
	@Override
	protected void destory() {
		log("PoConfirmTask destoried...");
	}

}
