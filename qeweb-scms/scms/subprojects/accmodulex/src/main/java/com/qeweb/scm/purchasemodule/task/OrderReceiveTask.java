package com.qeweb.scm.purchasemodule.task;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.sap.service.OrderReceiveSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class OrderReceiveTask extends AbstractJobBean {

	private OrderReceiveSyncService orderReceiveSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		Date date =DateUtil.getCurrentTimestamp();
		date.setDate(date.getDate()-1);
		getOrderReceiveSyncService().execute(getTaskLogger(),DateUtil.dateToString(date, "yyyyMMdd"),DateUtil.dateToString(DateUtil.getCurrentTimestamp(), "yyyyMMdd"));
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	
	

	public OrderReceiveSyncService getOrderReceiveSyncService() {
		if(orderReceiveSyncService == null)
			orderReceiveSyncService = SpringContextUtils.getBean("orderReceiveSyncService");
		
		return orderReceiveSyncService;
	}

	public void setOrderReceiveSyncService(
			OrderReceiveSyncService orderReceiveSyncService) {
		this.orderReceiveSyncService = orderReceiveSyncService;
	}


	
}
