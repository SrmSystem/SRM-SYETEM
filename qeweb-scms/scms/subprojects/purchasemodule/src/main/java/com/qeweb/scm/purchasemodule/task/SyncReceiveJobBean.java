package com.qeweb.scm.purchasemodule.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbsWebserviceJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.vo.ReceiveTransfer;

/**
 * 同步收货单JobBean
 * @author ALEX
 *
 */
//@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncReceiveJobBean extends AbsWebserviceJobBean {

	private static final Logger logger = LoggerFactory.getLogger(SyncReceiveJobBean.class);
	
	private ReceiveService receiveService;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected TaskResult exec() throws Exception {
		logger.info("start sync receive ....");
		log("start sync receive ....");
		TaskResult result = new TaskResult(true);
		//TODO:::
		String reuslt = getWsDate(null, null, new Object[]{});
		log("get webserveice data" + result);
		List<ReceiveTransfer> list = convertToObject(reuslt);
		boolean flag = getReceiveService().saveReceives(list, getTaskLogger());
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
	protected List<ReceiveTransfer> convertToObject(String data) {
		if(StringUtils.isEmpty(data))
			return null;
		
		//TODO::
		return null;
	}

	public ReceiveService getReceiveService() {
		if(receiveService == null)
			receiveService = SpringContextUtils.getBean("receiveService");
		
		return receiveService;
	}

	public void setReceiveService(ReceiveService receiveService) {
		this.receiveService = receiveService;
	}

}
