package com.qeweb.scm.epmodule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.epmodule.service.EpPriceService;

/**
 * 判断询价单里面时间的自动任务
 * @author u
 */
@Component
@Scope(value = "prototype")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class JudgeEpPriceTimeTask extends AbstractJobBean {
	
	@Autowired
	private static EpPriceService epPriceService;
	
	public JudgeEpPriceTimeTask() throws Exception {
//		log.destory();
//		log.init(this);
	}

	@Override
	protected void prepare() {
		log("JudgeEpPriceTimeTask prepared...");
	}

	/**
	 * 重写继承的exec方法
	 */
	@Override
	public TaskResult exec() throws Exception {
		log("JudgeEpPriceTimeTask exec start");
		TaskResult tr = new TaskResult();
		tr.isCompleted = getService().execute(getTaskLogger());
		log("JudgeEpPriceTimeTask exec end");
		return tr;
	}
	
	/**
	 * 获取EpPriceService，获取不到则重新从Spring容器中获取
	 */
	public static EpPriceService getService() {
		if(epPriceService == null) 
			epPriceService = SpringContextUtils.getBean("epPriceService");  
		return epPriceService;
	}
	
	/**
	 * 重写继承的destory方法
	 */
	@Override
	protected void destory() {
		log("JudgeEpPriceTimeTask destoried...");
	}

}
