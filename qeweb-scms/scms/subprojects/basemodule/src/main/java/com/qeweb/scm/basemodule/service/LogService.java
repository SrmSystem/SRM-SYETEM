package com.qeweb.scm.basemodule.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.LogEntity;
import com.qeweb.scm.basemodule.repository.LogDao;
import com.qeweb.scm.basemodule.utils.PageUtil;


@Service
@Transactional
public class LogService {
	
	@Autowired
	private LogDao logDao;

	public Page<LogEntity> getLogList(int pageNumber, int pageSize,Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<LogEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), LogEntity.class);
		return logDao.findAll(spec,pagin);
	}

	public Page<LogEntity> getLogList(int pageNumber, int pageSize, Map<String, Object> searchParamMap, Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<LogEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), LogEntity.class);
		return logDao.findAll(spec,pagin);
	}
	
}
