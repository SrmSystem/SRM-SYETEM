package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
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
import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.repository.GroupFactoryRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class GroupFactoryRelService {
	
	@Autowired
	private GroupFactoryRelDao groupFactoryRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<GroupFactoryRelEntity> getGroupFactoryRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<GroupFactoryRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), GroupFactoryRelEntity.class);
		return groupFactoryRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewGroupFactoryRel(GroupFactoryRelEntity groupFactoryRel) {
		groupFactoryRelDao.save(groupFactoryRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<GroupFactoryRelEntity> findAll(){
		return groupFactoryRelDao.findAll();
	}
	
	/**
     * 获取单个对应关系
     */
	public  GroupFactoryRelEntity getGroupFactoryRel(Long id) {
		return groupFactoryRelDao.findOne(id);
	}


	/**
     * 通过采购组和工厂的id获取数据
     */
	public  GroupFactoryRelEntity getGroupFactoryRelByGroupIdAndFactoryId(Long groupId , Long factoryId) {
		return groupFactoryRelDao.getGroupFactoryRelByGroupIdAndFactoryIdAndAbolished(groupId , factoryId,0);
	}
	
	
	
	/**
     * 删除对应关系
     */
	public void deleteGroupFactoryRel(List<GroupFactoryRelEntity> groupFactoryRelEntityList) {
		groupFactoryRelDao.delete(groupFactoryRelEntityList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<GroupFactoryRelEntity> groupFactoryRelEntityList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(GroupFactoryRelEntity groupFactoryRel : groupFactoryRelEntityList){
			groupFactoryRelDao.abolishGroupFactory(groupFactoryRel.getId());
			result++;
		}
		if(result != groupFactoryRelEntityList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
