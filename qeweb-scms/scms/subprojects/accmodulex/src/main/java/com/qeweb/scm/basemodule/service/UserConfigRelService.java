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
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.repository.UserConfigRelDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class UserConfigRelService extends BaseService{
	
	@Autowired
	private UserConfigRelDao userConfigRelDao;
	
	public Page<GroupConFigRelEntity> getUserConfigRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<GroupConFigRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), GroupConFigRelEntity.class);
		return userConfigRelDao.findAll(spec,pagin);
	}

	/**
	 * 根据用户查找关系的人员
	 * @param 
	 */
	public GroupConFigRelEntity getRelByUserId(Long userId) {
		
		return userConfigRelDao.getRelByUserId(userId);
	}
	
	
	/**
	 * 根据采购组模糊查询人员
	 * @param 
	 */
	public List<GroupConFigRelEntity> getRelByGroupId(Long groupId) {
		String id = groupId.toString() + ",";
		return userConfigRelDao.getRelByGroupId(id);
	}
	

	

	/**
	 * 添加关系
	 * @param 
	 */
	public void addGroupConfig(Long useId,String groupIds) {
		GroupConFigRelEntity groupConFigRel = userConfigRelDao.getRelByUserId(useId);
		if(groupConFigRel == null ){
			groupConFigRel = new GroupConFigRelEntity();
			groupConFigRel.setUserId(useId);
			groupConFigRel.setGroupIds(groupIds);
		}else{
			groupConFigRel.setUserId(useId);
			groupConFigRel.setGroupIds(groupIds);
		}
		userConfigRelDao.save(groupConFigRel);
	}


}
