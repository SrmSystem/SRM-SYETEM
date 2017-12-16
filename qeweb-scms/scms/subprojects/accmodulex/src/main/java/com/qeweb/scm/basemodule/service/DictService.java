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
import com.qeweb.scm.basemodule.repository.DictDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.sap.service.BOHelper;


@Service
@Transactional
public class DictService {
	
	@Autowired
	private DictDao dictDao;
	
	
	public Page<DictEntity> getDictList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DictEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DictEntity.class);
		return dictDao.findAll(spec,pagin);
	}

	/**
	 * 根据配置的code获取数据
	 * @return
	 */
	public Map<String, Object>  getDict( String  type){
		Map<String,Object >  dictMap = new HashMap<String, Object>();
		DictEntity dict = dictDao.findByDictCodeAndAbolished(type,0);
		if(dict != null ){
			if(!dict.getDictItem().isEmpty()){
				for (DictItemEntity str : dict.getDictItem()) {  
					dictMap.put(str.getCode(),str.getName());
				}
			}
		}
		return dictMap;
	}
	
	/**
	 * 根据编码和废弃状态查数据字典
	 * @param dictCode
	 * @param abolished
	 * @return
	 */
	public DictEntity getDictByCodeAndAbolished(String dictCode,Integer abolished){
		return dictDao.findByDictCodeAndAbolished(dictCode, abolished);
	}
	
	/**
	 * 判断修改时编码是否重复
	 * @param dictCode
	 * @param abolished
	 * @param id
	 * @return
	 */
	public DictEntity getDictByCodeAndAbolishedAndNotId(String dictCode,Integer abolished,Long id){
		return dictDao.getDictByCodeAndAbolishedAndNotId(dictCode, abolished,id);
	}
	
	/**
	 * 根据id返回数据字典对象
	 * @param id
	 * @return
	 */
	public DictEntity findDictById(Long id){
		return dictDao.findById(id);
	}
	
	/**
	 * 新增数据字典
	 * @param dict
	 */
	public void addOrUpdateDict(DictEntity dict){
		if(dict.getId()>0){
			DictEntity entity=dictDao.findById(dict.getId());
			entity.setDictCode(dict.getDictCode());
			entity.setDictName(dict.getDictName());
			entity.setDictDesc(dict.getDictDesc());
			BOHelper.setBOPublicFields_update(entity);
			dictDao.save(entity);
		}else{
			BOHelper.setBOPublicFields_insert(dict);
			dictDao.save(dict);
		}
	}
	
	/**
	 * 删除
	 * @param pageList
	 */
	public void deleteDictList(List<DictEntity> pageList){
		if(null!=pageList && pageList.size()>0){
			List<DictEntity> list = new ArrayList<DictEntity>();
			for (DictEntity dictEntity : pageList) {
				dictEntity=dictDao.findById(dictEntity.getId());
				BOHelper.setBOPublicFields_abolish(dictEntity);
				list.add(dictEntity);
			}
			dictDao.save(list);
		}
	}
}
