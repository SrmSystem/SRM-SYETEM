package com.qeweb.scm.baseline.common.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.entity.TestOrderEntity;
import com.qeweb.scm.baseline.common.repository.TestOrderDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;



@Service
@Transactional
public class TestOrderService extends BaseService{

	@Autowired
	private TestOrderDao orderDao;
	
	public Page<TestOrderEntity> getAll(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<TestOrderEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), TestOrderEntity.class);
		return orderDao.findAll(spec,pagin);
	}
	
}
