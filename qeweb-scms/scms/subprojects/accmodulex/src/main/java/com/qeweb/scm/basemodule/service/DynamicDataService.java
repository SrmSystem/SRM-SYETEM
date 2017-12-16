package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.DynamicDataEntity;
import com.qeweb.scm.basemodule.entity.DynamicDataSceneEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.DynamicDataDao;
import com.qeweb.scm.basemodule.repository.DynamicDataItemDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.vo.DynamicInfoVO;

@Service
@Transactional
public class DynamicDataService {
	
	@Autowired
	private DynamicDataDao dynamicDataDao;
	
	@Autowired
	private DynamicDataItemDao dynamicDataItemDao;

	/**
	 * 动态配置主数据列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<DynamicDataEntity> getDynamicDataList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DynamicDataEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DynamicDataEntity.class);
		return dynamicDataDao.findAll(spec,pagin);
	}
	
	/**
	 * 动态配置子表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<DynamicDataSceneEntity> getDynamicDataItemList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DynamicDataSceneEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DynamicDataSceneEntity.class);
		return dynamicDataItemDao.findAll(spec,pagin);
	}

	/**
	 * 根据BeanId获取扩展配置
	 * @param beanId
	 * @return
	 */
	public DynamicDataEntity getDynamicData(String beanId) {
		return dynamicDataDao.findByBeanId(beanId);
	}
	
	public DynamicDataEntity getDynamicData(Long id) {
		return dynamicDataDao.findOne(id);
	}
	
	public void delDynamicDataList(List<DynamicDataEntity> list) {
		if(CollectionUtils.isEmpty(list))
			return;
		
		for(DynamicDataEntity entity : list)
			entity.setAbolished(1);
		
		dynamicDataDao.save(list);
	}
	
	
	public void updateDynamicData(DynamicDataEntity dynamicData) {
		dynamicDataDao.save(dynamicData);
	}

	/**
	 * 启用禁用
	 * @param list
	 * @param status 0：禁用 1：启用
	 */
	public void enableDynamicDataList(List<DynamicDataEntity> list, Integer status) {
		if(CollectionUtils.isEmpty(list))
			return;
		
		for(DynamicDataEntity item : list)
			item.setEnable(status);
		dynamicDataDao.save(list);
	}

	/**
	 * 保存动态数据配置
	 * @param dynamicEntity
	 * @param itemList
	 */
	public void saveDynamic(DynamicDataEntity dynamicEntity, List<DynamicDataSceneEntity> itemList) {
		if(dynamicEntity.getId() == 0l ) {
			dynamicDataDao.save(dynamicEntity);
			dynamicDataItemDao.save(itemList);
			return;
		}
		
		DynamicDataEntity savedynamicEntity = getDynamicData(dynamicEntity.getId());
		savedynamicEntity.setObjectName(dynamicEntity.getObjectName());
		savedynamicEntity.setRemark(dynamicEntity.getRemark());
		savedynamicEntity.setEnable(dynamicEntity.getEnable()); 
		Set<DynamicDataSceneEntity> itemSet = savedynamicEntity.getColSceneItem();   
		Map<Long, DynamicDataSceneEntity> itemMap = new HashMap<Long, DynamicDataSceneEntity>();
		for(DynamicDataSceneEntity i : itemSet) {
			itemMap.put(i.getId(), i);
		}
		
		List<DynamicDataSceneEntity> saveItemList = new ArrayList<DynamicDataSceneEntity>();
		DynamicDataSceneEntity itemEntity = null;
		for(DynamicDataSceneEntity vi : itemList) {
			if(vi.getId() != 0l && itemMap.containsKey(vi.getId())) {
				itemEntity = itemMap.get(vi.getId());
				itemEntity.setColCode(vi.getColCode());
				itemEntity.setName(vi.getName());
				itemEntity.setRange(vi.getRange());
				itemEntity.setType(vi.getType());
				itemEntity.setStatusKey(vi.getStatusKey());
				itemEntity.setWay(vi.getWay());
				itemEntity.setFilter(vi.isFilter());
				itemEntity.setRequired(vi.isRequired());
				itemEntity.setShow(vi.isShow());
				saveItemList.add(itemEntity);
				itemMap.remove(vi.getId());
				continue;	
			}
			vi.setId(0l); 
			saveItemList.add(vi);
		}
		dynamicDataDao.save(savedynamicEntity);
		dynamicDataItemDao.save(saveItemList);
		if(CollectionUtils.isEmpty(itemMap))
			return;
		
		dynamicDataItemDao.delete(itemMap.values()); 
	}

	/**
	 * 保单批量导入字段配置
	 * @param list
	 * @param logger
	 * @return
	 */
	public boolean saveDynamicInfo(List<DynamicInfoVO> list, ILogger logger) {
		logger.log("\n->正在准备合并主数据与明细数据\n");
		Map<String, DynamicDataEntity> tmpMap = new HashMap<String, DynamicDataEntity>();
		Map<String, DynamicDataSceneEntity> itemMap = new HashMap<String, DynamicDataSceneEntity>();
		String key = null;
		DynamicDataEntity dynamicEntity = null;
		DynamicDataSceneEntity dynamicItemEntity = null;
		for(DynamicInfoVO trans : list) {
			key = trans.getBeanId();
			if(!tmpMap.containsKey(key)) {
				dynamicEntity = getDynamicData(key);
				if(dynamicEntity == null)
					dynamicEntity = new DynamicDataEntity();
				dynamicEntity.setBeanId(trans.getBeanId());
				dynamicEntity.setObjectName(trans.getObjectName());
				dynamicEntity.setRemark(trans.getRemark());
				dynamicEntity.setEnable("Y".equals(trans.getEnable()) ? StatusConstant.STATUS_YES : StatusConstant.STATUS_NO); 
				tmpMap.put(key, dynamicEntity);
			} else {
				dynamicEntity = tmpMap.get(key);
			}
			key +=(";" + trans.getColCode() + ";" + trans.getWay());
			if(!itemMap.containsKey(key)) {
				dynamicItemEntity = getDynamicItemData(trans.getBeanId(), trans.getColCode(), trans.getWay());
				if(dynamicItemEntity == null)
					dynamicItemEntity = new DynamicDataSceneEntity();
				itemMap.put(key, dynamicItemEntity);
			} else {
				dynamicItemEntity = itemMap.get(key);
			}
			dynamicItemEntity.setDataEx(dynamicEntity);
			dynamicItemEntity.setColCode(trans.getColCode());
			dynamicItemEntity.setName(trans.getName());
			dynamicItemEntity.setRange(trans.getRange());
			dynamicItemEntity.setType(trans.getType());
			dynamicItemEntity.setStatusKey(trans.getStatusKey());
			dynamicItemEntity.setWay(trans.getWay());
			dynamicItemEntity.setFilter("Y".equals(trans.getFilter()));
			dynamicItemEntity.setRequired("Y".equals(trans.getFilter()));
			dynamicItemEntity.setShow("Y".equals(trans.getFilter()));
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		logger.log("-->开始对合并数据进行保存操作....");
		dynamicDataDao.save(tmpMap.values());
		dynamicDataItemDao.save(itemMap.values());
		logger.log("-->保存完成");
		return true;
	}

	private DynamicDataSceneEntity getDynamicItemData(String beanId, String colCode, String way) {
		return dynamicDataItemDao.findDynamicDataItem(beanId, colCode, way);
	}
}
