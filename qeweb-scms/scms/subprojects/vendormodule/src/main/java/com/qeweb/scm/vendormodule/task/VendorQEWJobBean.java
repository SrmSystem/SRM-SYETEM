package com.qeweb.scm.vendormodule.task;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.basemodule.service.MessageService;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.service.VendorSurveyService;

@JobBean
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class VendorQEWJobBean extends AbstractJobBean {
	private static final Logger logger = LoggerFactory.getLogger(VendorQEWJobBean.class);
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected TaskResult exec() throws Exception {
		
		logger.info("start to get qualification information ....");
		TaskResult result = new TaskResult(true);
		VendorSurveyService vendorSurveyService = SpringContextUtils.getBean("vendorSurveyService");
		MessageService messageService = SpringContextUtils.getBean("messageService");
		Page<VendorSurveyDataEntity> page = vendorSurveyService.getVendorSurveyDataList(null,null,null);
		List<VendorSurveyDataEntity> lists = page.getContent();
//		1）	XX公司XX证书即将到期，请进行邮件提醒
//		3）	XX证书即将到期，请注意更新
		for (VendorSurveyDataEntity v : lists) {
			messageService.sendToUsers(5435l, v.getOrganizationEntity().getName()+"公司"+v.getCol1()+"证书即将到期，请进行邮件提醒",1l, new Long[]{1l});
			messageService.sendToUsers(1940l, v.getCol1()+"证书即将到期，请注意更新",1l, new Long[]{v.getOrgId()});
		}
		log("FINISHED.");   
		return result;
	}

	@Override
	protected void destory() {
		
	}

}
