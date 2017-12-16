package com.qeweb.scm.abnormal.task.service;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.abnormal.entity.AbnormalFeedbackEntity;
import com.qeweb.scm.abnormal.repository.BuyerAbnormalFeedbackDao;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;

@Service
@Transactional(rollbackOn=Exception.class)
public class AbnormalFeedbackCheckService extends BaseService{
	
	@Autowired
	private BuyerAbnormalFeedbackDao buyerAbnormalFeedbackDao;
	
	public boolean execute(ILogger iLogger) throws Exception {
		iLogger.log("method execute start");
		List<AbnormalFeedbackEntity>  abnormalFeedbackList = buyerAbnormalFeedbackDao.findAll();
		for(AbnormalFeedbackEntity  abnormalFeedback : abnormalFeedbackList){
			if(abnormalFeedback.getAbolished().equals(0) && abnormalFeedback.getPublishStatus() != 3){
				Timestamp curr=DateUtil.getCurrentTimestamp();
				if(curr.before(abnormalFeedback.getEffectiveEndDate())){
					//修改异常反馈一结束
					buyerAbnormalFeedbackDao.closeAbnormalFeedback(abnormalFeedback.getId());
					iLogger.log("close FileCollaboration " + abnormalFeedback.getAbnormalFeedbackName());
				}
			}
		}
		iLogger.log("method execute end");
		return true;
	}

}
