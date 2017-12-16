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
import com.qeweb.scm.sap.service.PurchaseGoodsRequestSyncService;
@Component
@Scope(value = "prototype")
@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Transactional(rollbackFor=Exception.class)
public class PurchaseGoodsRequestTask extends AbstractJobBean {

	private PurchaseGoodsRequestSyncService purchaseGoodsRequestSyncService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	protected TaskResult exec() throws Exception {
		TaskResult tr = new TaskResult();
		getPurchaseGoodsRequestSyncService().execute(getTaskLogger());
		tr.isCompleted = true;
		return tr;
	}

	@Override
	protected void destory() {
			
	}
	
	
	

	public PurchaseGoodsRequestSyncService getPurchaseGoodsRequestSyncService() {
		if(purchaseGoodsRequestSyncService == null)
			purchaseGoodsRequestSyncService = SpringContextUtils.getBean("purchaseGoodsRequestSyncService");
		
		return purchaseGoodsRequestSyncService;
	}

	public void setPurchaseGoodsRequestSyncService(
			PurchaseGoodsRequestSyncService purchaseGoodsRequestSyncService) {
		this.purchaseGoodsRequestSyncService = purchaseGoodsRequestSyncService;
	}

	
}
