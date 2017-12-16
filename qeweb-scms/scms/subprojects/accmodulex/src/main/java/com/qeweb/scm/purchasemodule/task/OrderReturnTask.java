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
import com.qeweb.scm.sap.service.OrderReturnSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class OrderReturnTask extends AbstractJobBean {

	private OrderReturnSyncService orderReturnSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getOrderReturnSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	

	public OrderReturnSyncService getOrderReturnSyncService() {
		if(orderReturnSyncService == null)
			orderReturnSyncService = SpringContextUtils.getBean("orderReturnSyncService");
		
		return orderReturnSyncService;
	}

	public void setOrderReturnSyncService(
			OrderReturnSyncService orderReturnSyncService) {
		this.orderReturnSyncService = orderReturnSyncService;
	}

	
}
