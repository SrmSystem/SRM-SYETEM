package com.qeweb.scm.basemodule.service;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.UserWarnRelEntity;
import com.qeweb.scm.basemodule.repository.UserWarnRelDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class UserWarnRelService extends BaseService{
	
	@Autowired
	private UserWarnRelDao userWarnRelDao;
	
	public Page<UserWarnRelEntity> getUserWarnRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<UserWarnRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), UserWarnRelEntity.class);
		return userWarnRelDao.findAll(spec,pagin);
	}

	/**
	 * 根据用户查找预警的角色的人员
	 * @param 
	 */
	public List<UserWarnRelEntity> getRelByUserId(Long userId) {
		return userWarnRelDao.getRelByUserId(userId);
	}
	

	/**
	 * 添加关系
	 * @param 
	 */
	public void add(UserWarnRelEntity rel) {
		userWarnRelDao.save(rel);
	}

	
	
	/**
	 * 删除关系
	 * @param 
	 */
	public void deleteUserWarnRelList(List<UserWarnRelEntity> list) {
		userWarnRelDao.delete(list);
	}

}
