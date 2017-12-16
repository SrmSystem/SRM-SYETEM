package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
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
import com.qeweb.scm.basemodule.entity.DictEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.repository.DictItemDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.sap.service.BOHelper;

@Service
@Transactional
public class DictItemService {
	
	@Autowired
	private DictItemDao dictItemDao;
	
	public Page<DictItemEntity> getList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pageable = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String,SearchFilterEx> filterMap =  SearchFilterEx.parse(searchParamMap);
		Specification<DictItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filterMap.values(), DictItemEntity.class);
		return dictItemDao.findAll(spec, pageable);
	}

	/**
	 * 根据配置的类型获取数据
	 * @return
	 */
	public Map<String, Object>  getVendorDictItem( String  type){
		Map<String,Object >  dictItemMap = new HashMap<String, Object>();
		List<DictItemEntity> dictItemList = new ArrayList<DictItemEntity>();
		dictItemList = dictItemDao.findByDictType(type);
		for(DictItemEntity dictItem :  dictItemList ){
			dictItemMap.put(dictItem.getCode(),dictItem.getName() );
		}
		return dictItemMap;
	}
	
	
	/**
	 * 根据主表编码查所有字段明细
	 * @param dictCode
	 * @return
	 */
	public List<DictItemEntity> findListByDictCode(String dictCode){
		return dictItemDao.findByDictCode(dictCode);
	}
	
	/**
	 * 根据主表ID查所有字段明细
	 * @param dictCode
	 * @return
	 */
	public List<DictItemEntity> findDictItemListByDictId(Long dictId){
		return dictItemDao.findDictItemListByDictId(dictId);
	}

	/**
	 * 根据字表编码查所有字段明细
	 * @param dictCode
	 * @return
	 */
	public DictItemEntity findDictItemByCode(String code){
		return dictItemDao.findDictItemByCode(code);
	}

	/**
	 * 删除子项
	 * @param dictItemList
	 */
	public void deleteDictItemList(List<DictItemEntity> pageList) {
		if(null!=pageList && pageList.size()>0){
			List<DictItemEntity> list = new ArrayList<DictItemEntity>();
			for (DictItemEntity dictItemEntity : pageList) {
				dictItemEntity=dictItemDao.findById(dictItemEntity.getId());
				BOHelper.setBOPublicFields_abolish(dictItemEntity);
				list.add(dictItemEntity);
			}
			dictItemDao.save(list);
		}
		
	}

	/**
	 * 根据编码判断该数据字典的子项中编码是否重复（修改用）
	 * @param dictId
	 * @param code
	 * @param uNABOLISHED_SINGEL
	 * @param id
	 * @return
	 */
	public DictItemEntity getDictItemByCodeAndAbolishedAndNotId(Long dictId,
			String code, Integer abolished, long id) {
	  return dictItemDao.getDictItemByCodeAndAbolishedAndNotId(dictId,code,abolished,id);
	}

	/**
	 *  根据编码判断该数据字典的子项中编码是否重复（新增用）
	 * @param dictId
	 * @param code
	 * @param uNABOLISHED_SINGEL
	 * @return
	 */
	public DictItemEntity getDictItemByCodeAndAbolished(Long dictId,
			String code, Integer abolished) {
		return dictItemDao.getDictItemByCodeAndAbolished(dictId, code, abolished);
	}

	/**
	 * 新增或编辑
	 * @param dictItem
	 */
	public void addOrUpdateDictItem(DictItemEntity dictItem,DictEntity dict) {
		if(dictItem.getId()>0){
			DictItemEntity entity=dictItemDao.findById(dictItem.getId());
			entity.setCode(dictItem.getCode());
			entity.setName(dictItem.getName());
			entity.setDict(dict);
			BOHelper.setBOPublicFields_update(entity);
			dictItemDao.save(entity);
		}else{
			dictItem.setDict(dict);
			BOHelper.setBOPublicFields_insert(dictItem);
			dictItemDao.save(dictItem);
		}
	}

	/**
	 * 根据id查子项
	 * @param id
	 * @return
	 */
	public DictItemEntity findDictItemById(Long id) {
		return dictItemDao.findById(id);
	}
	
	public Map<String, Object> abolishBatch(List<DictItemEntity> dictItemList) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(DictItemEntity dictItemEntity : dictItemList){
			dictItemDao.abolish(dictItemEntity.getId());
		}
		map.put("success", true);
		return map;
	}
	
	/**
	 * 生效
	 * @return
	 */
	
	public Map<String, Object> effect(List<DictItemEntity> dictItemList) {
		Map<String,Object> map = new HashMap<String, Object>();
		int i = 0;
		for(DictItemEntity dictItemEntity : dictItemList){
			dictItemDao.effect(dictItemEntity.getId());
			i++;
		}
		if( i == dictItemList.size()){
			map.put("msg", "操作成功");
			map.put("success", true);
		}else{
			map.put("msg", "操作失败");
			map.put("success", false);
		}
		return map;
	}
	
	

}
