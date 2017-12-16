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
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.entity.JobTriggersEntity;
import com.qeweb.scm.basemodule.entity.JobTriggersEntityKey;
import com.qeweb.scm.basemodule.repository.JobCronDao;
import com.qeweb.scm.basemodule.repository.JobTriggersDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Component
@Transactional
public class JobTriggersService {
	
private static Logger logger = LoggerFactory.getLogger(JobTriggersService.class);
	
	@Autowired
	private JobTriggersDao jobTriggersDao;
	
	@Autowired
	private JobCronDao jobCronDao;
	
	public Page<JobTriggersEntity> getTriggersList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<JobTriggersEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), JobTriggersEntity.class);
		Page<JobTriggersEntity> page = jobTriggersDao.findAll(spec,pagin);
		return page;             
	}
	
	public JobTriggersEntity getTriggers(JobTriggersEntityKey key) {
		return jobTriggersDao.findOne(key);
	}
	
	public JobTriggersEntity getTriggersList(String schedName, String triggerName, String triggerGroup) {
		JobTriggersEntityKey key = new JobTriggersEntityKey(schedName, triggerName, triggerGroup);
		return jobTriggersDao.findByKey(key);
	}
	
}
