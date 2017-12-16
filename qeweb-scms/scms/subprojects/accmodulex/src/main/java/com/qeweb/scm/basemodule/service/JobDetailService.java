package com.qeweb.scm.basemodule.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.JobDetailEntity;
import com.qeweb.scm.basemodule.entity.JobDetailEntityKey;
import com.qeweb.scm.basemodule.repository.JobDetailDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Component
@Transactional
public class JobDetailService {
	
private static Logger logger = LoggerFactory.getLogger(JobDetailService.class);
	
	@Autowired
	private JobDetailDao jobDetailDao;
	
	public Page<JobDetailEntity> getTaskList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<JobDetailEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), JobDetailEntity.class);
		return jobDetailDao.findAll(spec,pagin);
	}
	
	public JobDetailEntity getTask(JobDetailEntityKey key) {
		logger.info("get job info --> schedName:" + key.getSchedName() + " jobName:" + key.getJobName() + " jobGroup:" + key.getJobGroup()); 
		return jobDetailDao.findOne(key);  
	}
	
	public void updatejobDetail(JobDetailEntity taskDetail) {
		JobDetailEntityKey key = new JobDetailEntityKey(taskDetail.getSchedName(), taskDetail.getJobName(), taskDetail.getJobGroup());
		JobDetailEntity entity = jobDetailDao.findByKey(key);
		entity.setDescription(taskDetail.getDescription()); 
		jobDetailDao.save(entity);
	}

	public void deleteTask(JobDetailEntity taskDetail) {
		jobDetailDao.delete(taskDetail);
	}

}
