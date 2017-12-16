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
import org.springside.modules.utils.Collections3;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryBrandRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.FactoryBrandRelDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Service
@Transactional
public class FactoryService extends BaseService{
	
	@Autowired
	private FactoryDao factoryDao;
	@Autowired
	private FactoryBrandRelDao factoryBrandRelDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	
	public Page<FactoryEntity> getFactoryList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FactoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), FactoryEntity.class);
		return factoryDao.findAll(spec,pagin);
	}

	public List<FactoryEntity> findAll(){
		return factoryDao.findAll();
	}
	
	/**
	 * 添加一个新的工厂
	 * @param factory 工厂类
	 */
	public void addNewFactory(FactoryEntity factory) {
		if(StringUtils.isEmpty(factory.getCode())){
			factory.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.FACOTRY));
		}
		factoryDao.save(factory);
		saveFactoryBrandRel(factory);
	}

	/**
	 * 获得工厂和相关的品牌信息
	 * @param id 工厂ID
	 * @return 工厂相关信息
	 */
	public FactoryEntity getFactory(Long id) {
		FactoryEntity factory = factoryDao.findOne(id);
		factory.setFactoryBrandList(factoryBrandRelDao.findByFactoryId(id));
		return factory;
	}

	public void updateFactory(FactoryEntity factory) {
		FactoryEntity existFactory = factoryDao.findOne(factory.getId());
		BeanUtil.copyPropertyNotNull(factory, existFactory);
		factoryDao.save(existFactory);
		factoryBrandRelDao.deleteByFactory(existFactory.getId());
		saveFactoryBrandRel(existFactory);
	}

	public void deleteFactoryList(List<FactoryEntity> factoryList) {
		for(FactoryEntity factory : factoryList) {
			factoryBrandRelDao.deleteByFactory(factory.getId());
		}
		factoryDao.delete(factoryList);
	}
	
	/**
	 * 保存工厂和品牌的关系
	 * @param factory 工厂对象
	 */
	private void saveFactoryBrandRel(FactoryEntity factory) {
		//工厂类需要关联品牌
		List<FactoryBrandRelEntity> factoryBrandList = factory.getFactoryBrandList();
		if(Collections3.isEmpty(factoryBrandList)) {
			return;
		}
		for(FactoryBrandRelEntity factoryBrand : factoryBrandList){
			factoryBrand.setFactoryId(factory.getId());
		}
		factoryBrandRelDao.save(factoryBrandList);
		
	}

	public List<BussinessRangeEntity> getBussinessRange(Long id) {
		List<FactoryBrandRelEntity> factoryBrandList =factoryBrandRelDao.findByFactoryId(id);
		List<BussinessRangeEntity> brands=new ArrayList<BussinessRangeEntity>();
		for(FactoryBrandRelEntity factoryBrandRelEntity:factoryBrandList)
		{
			BussinessRangeEntity brand=new BussinessRangeEntity();
			brand=bussinessRangeDao.findOne(factoryBrandRelEntity.getBrandId());
			brands.add(brand);
		}
		return brands;
	}

	public Map<String, Object> abolishBatch(List<FactoryEntity> factoryEntitys) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(FactoryEntity factoryEntity:factoryEntitys)
		{
			factoryDao.abolish(factoryEntity.getId());
		}
		map.put("success", true);
		return map;
	}
	
	public FactoryEntity getFactoryByCode(String code){
		return factoryDao.findByCode(code);
	}

}
