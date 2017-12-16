package com.qeweb.scm.purchasemodule.task;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbsWebserviceJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.service.PurchaseOrderService;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;

/**
 * 同步采购订单JobBean
 * @author ALEX
 *
 */
//@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncPurchaseOrderJobBean extends AbsWebserviceJobBean {

	private static final Logger logger = LoggerFactory.getLogger(SyncPurchaseOrderJobBean.class);

	private PurchaseOrderService purchaseOrderService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected TaskResult exec() throws Exception {
		logger.info("start sync order ....");
		log("start sync order ....");
		TaskResult result = new TaskResult(true);
		//TODO:::
		String reuslt = getWsDate(null, null, new Object[]{});
		log("get webserveice data" + result);
		List<PurchaseOrderTransfer> list = convertToObject(reuslt);
		boolean flag = getPurchaseOrderService().combinePurchaseOrder(list, getTaskLogger());
		if(flag) {
			log("sync order success finish ...");	
		}else {
			log("sync order error ...");	
		}
		log("end sync order ....");
		return result;
	}

	@Override
	protected void destory() {
		
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List<PurchaseOrderTransfer> convertToObject(String data) {
		if(StringUtils.isEmpty(data))
			return null;
		
		//TODO:::
		return null;
	}

	public PurchaseOrderService getPurchaseOrderService() {
		if(purchaseOrderService == null)
			purchaseOrderService = SpringContextUtils.getBean("purchaseOrderService");
		
		return purchaseOrderService;
	}

	public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
		this.purchaseOrderService = purchaseOrderService;
	}

}
