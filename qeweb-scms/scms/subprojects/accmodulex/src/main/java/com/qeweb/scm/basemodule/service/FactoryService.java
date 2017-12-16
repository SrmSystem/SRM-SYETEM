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
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryBrandRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.FactoryInventoryRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.CompanyFactoryRelDao;
import com.qeweb.scm.basemodule.repository.FactoryBrandRelDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.FactoryInventoryRelDao;
import com.qeweb.scm.basemodule.repository.FactoryOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.GroupFactoryRelDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class FactoryService extends BaseService{
	
	@Autowired
	private FactoryDao factoryDao;
	@Autowired
	private FactoryBrandRelDao factoryBrandRelDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;

	@Autowired
	private CompanyFactoryRelDao companyFactoryRelDao;

	@Autowired
	private FactoryInventoryRelDao factoryInventoryRelDao;
	
	@Autowired
	private FactoryOrganizationRelDao factoryOrganizationRelDao;
	
	@Autowired
	private GroupFactoryRelDao groupFactoryRelDao;
	
	
	
	
	
	public Page<FactoryEntity> getFactoryList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FactoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), FactoryEntity.class);
		return factoryDao.findAll(spec,pagin);
	}

	public List<FactoryEntity> findAll(){
		return factoryDao.findAll();
	}
	
	public List<FactoryEntity> findEffective(){
		return factoryDao.findByAbolished(0);
	}
	/**
	 * 添加一个新的工厂
	 * @param factory 工厂类
	 */
	public void addNewFactory(FactoryEntity factory) {
		factoryDao.save(factory);
	}

	/**
	 * 获得工厂
	 * @param id 工厂ID
	 * @return 工厂相关信息
	 */
	public FactoryEntity getFactory(Long id) {
		return  factoryDao.findOne(id);
	}

	public void updateFactory(FactoryEntity factory) {
		factoryDao.save(factory);

	}

	public void deleteFactoryList(List<FactoryEntity> factoryList) {
		for(FactoryEntity factory : factoryList) {
			factoryBrandRelDao.deleteByFactory(factory.getId());
			factoryDao.abolish(factory.getId());
		}
		//factoryDao.delete(factoryList);
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
		
		String msg = "";
		Boolean flag = true;
		
		
		//验证工厂是否绑定公司和采购组织、采购组、库存地点
		for(FactoryEntity factoryEntity:factoryEntitys){
			List<CompanyFactoryRelEntity> cfList = companyFactoryRelDao.findByFactoryIdAndAbolished(factoryEntity.getId(), 0);
			List<FactoryInventoryRelEntity> fiList = factoryInventoryRelDao.findByFactoryIdAndAbolished(factoryEntity.getId(), 0);
			List<FactoryOrganizationRelEntity>  foList = factoryOrganizationRelDao.findByFactoryIdAndAbolished(factoryEntity.getId(), 0);
			List<GroupFactoryRelEntity> gfList = groupFactoryRelDao.findByFactoryIdAndAbolished(factoryEntity.getId(), 0);
			if(cfList  != null && cfList.size() != 0){
				for(CompanyFactoryRelEntity cf : cfList){
					msg = msg + " 公司："+cf.getCompany().getName()+"已于工厂："+cf.getFactory().getName()+"绑定,无法废除！\r\n";
				}
				flag = false;
			}
           if(fiList  != null && fiList.size() != 0){
        	   for(FactoryInventoryRelEntity fi : fiList){
        		   msg = msg + " 工厂："+fi.getFactory().getName()+"已于库存地点："+fi.getInventory().getName()+"绑定,无法废除！\r\n";
        	   }
        		flag = false;
           }
           if(foList  != null && foList.size() != 0){
        	   for(FactoryOrganizationRelEntity fo: foList ){
        		   msg = msg + " 工厂："+fo.getFactory().getName()+"已于采购组织："+fo.getOrg().getName()+"绑定,无法废除！\r\n";
        	   }
				flag = false;
			}
          if(gfList   != null && gfList.size() != 0){
        	 for(GroupFactoryRelEntity gf:  gfList ){
        		   msg = msg + " 采购组："+gf.getGroup().getName()+"已于工厂："+gf.getFactory().getName()+"绑定,无法废除！\r\n";
        	 }
       		flag = false;
          }
			
		}
		
		
		if(!flag){
			map.put("msg", msg);
			map.put("success", false);
		}else{
			//废除
			for(FactoryEntity factoryEntity : factoryEntitys){
				factoryDao.abolish(factoryEntity.getId());
			}
			map.put("success", true);
		}
		return map;
	}
	
	/**
	 * 获取生效工厂
	 * @return
	 */
	
	public Map<String, Object> effectBatch(List<FactoryEntity> factoryEntitys) {
		Map<String,Object> map = new HashMap<String, Object>();
		int i = 0;
		for(FactoryEntity factoryEntity : factoryEntitys){
			factoryDao.effect(factoryEntity.getId());
			i++;
		}
		if( i == factoryEntitys.size()){
			map.put("msg", "操作成功");
			map.put("success", true);
		}else{
			map.put("msg", "操作失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	
	
	
	
	/**
	 * 获取没有绑定的工厂
	 * @return
	 */
	public Map<String, Object>  getFactory(){
		Map<String,Object >  returnMap = new HashMap<String, Object>();
		
		List<CompanyFactoryRelEntity> companyFactoryRelList = new ArrayList<CompanyFactoryRelEntity>();
		List<FactoryEntity> factoryList = new ArrayList<FactoryEntity>();
		
		List<FactoryEntity> dataList = new ArrayList<FactoryEntity>();//查询的list

		companyFactoryRelList =  companyFactoryRelDao.findByAbolished(0);
		factoryList = factoryDao.findAll();
	
		for(CompanyFactoryRelEntity  companyFactoryRel  :  companyFactoryRelList  ){
			for(FactoryEntity  factory  :  factoryList ){
				if(companyFactoryRel.getFactoryId().equals(factory.getId())  ){
					dataList.add(factory);
					break;
				}
			}
		}
		
		factoryList.removeAll(dataList);//移除list
		
		for(FactoryEntity factory :  factoryList ){
			returnMap.put(String.valueOf(factory.getId()),factory.getName() );
		}
		return returnMap;
	}
	
	
	public FactoryEntity getFactoryByCode(String code){
		return factoryDao.findByCode(code);
	}
	
	public FactoryEntity findByCodeAndAbolished(String code,Integer abolished){
		return factoryDao.findByCodeAndAbolished(code,abolished);
	}

}
