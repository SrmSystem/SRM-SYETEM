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
import com.qeweb.scm.baseline.common.entity.InterfaceMsgEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgItemEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgLogEntity;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgDao;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgItemDao;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgLogDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;



@Service
@Transactional
public class InterfaceMsgService extends BaseService{

	@Autowired
	private InterfaceMsgDao interfaceDao;
	@Autowired
	private InterfaceMsgLogDao logDao;
	@Autowired
	private InterfaceMsgItemDao itemDao;
	
	public Page<InterfaceMsgEntity> getAll(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InterfaceMsgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InterfaceMsgEntity.class);
		return interfaceDao.findAll(spec,pagin);
	}
	
	public Page<InterfaceMsgLogEntity> getLogs(int pk,int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_msgId", pk);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InterfaceMsgLogEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InterfaceMsgLogEntity.class);
		return logDao.findAll(spec,pagin);
	}
	
	public Page<InterfaceMsgItemEntity> getItems(int pk,int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_msgId", pk);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InterfaceMsgItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InterfaceMsgItemEntity.class);
		return itemDao.findAll(spec,pagin);
	}
	
}
