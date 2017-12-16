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
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.entity.GroupOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.repository.GroupFactoryRelDao;
import com.qeweb.scm.basemodule.repository.GroupOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class PurchasingGroupService {
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	@Autowired
	private GroupFactoryRelService   groupFactoryRelService;
	
	@Autowired
	private GroupOrganizationRelService   groupOrganizationRelService;
	
	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private GroupFactoryRelDao groupFactoryRelDao;
	
	@Autowired
	private GroupOrganizationRelDao groupOrganizationRelDao;

    /**
     * 获取采购组的列表
     */
	public Page<PurchasingGroupEntity> getPurchasingGroupList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasingGroupEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), PurchasingGroupEntity.class);
		return purchasingGroupDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增采购组的信息
     */
	public void addNewPurchasingGroup(PurchasingGroupEntity purchasingGroupEntity) {
		//获取工厂的信息 和采购组织的信息
		FactoryEntity factory =  factoryService.getFactory(Long.parseLong( purchasingGroupEntity.getFactoryId()) );
		OrganizationEntity org = orgService.getOrg(Long.parseLong(purchasingGroupEntity.getOrgId()));
		
		GroupFactoryRelEntity groupFactoryRel  = new GroupFactoryRelEntity();
		GroupOrganizationRelEntity groupOrganizationRel = new GroupOrganizationRelEntity();
		
		PurchasingGroupEntity ps  =  purchasingGroupDao.findByCode(purchasingGroupEntity.getCode());
		if(ps == null){
			PurchasingGroupEntity purchasingGroup = new PurchasingGroupEntity();
			purchasingGroup.setCode(purchasingGroupEntity.getCode());
			purchasingGroup.setName(purchasingGroupEntity.getName());
			purchasingGroup.setRemark(purchasingGroupEntity.getRemark());
			purchasingGroupDao.save(purchasingGroup);
			//添加采购组和工厂的关系
			groupFactoryRel.setGroupId(purchasingGroup.getId());
			groupFactoryRel.setFactoryId(factory.getId());
			groupFactoryRel.setRemark(purchasingGroupEntity.getRemark());
			groupFactoryRel.setAbolished(0);
			groupFactoryRelService.addNewGroupFactoryRel(groupFactoryRel);
			//添加采购组和采购组织的关系
			groupOrganizationRel.setGroupId(purchasingGroup.getId());
			groupOrganizationRel.setOrgId(org.getId());
			groupOrganizationRel.setRemark(purchasingGroupEntity.getRemark());
			groupOrganizationRel.setAbolished(0);
			groupOrganizationRelService.addNewGroupOrganizationRel(groupOrganizationRel);	
		}else{
			//添加采购组和工厂的关系
			GroupFactoryRelEntity groupFactoryRelTemp  =  groupFactoryRelService.getGroupFactoryRelByGroupIdAndFactoryId(ps.getId(), Long.parseLong( purchasingGroupEntity.getFactoryId()));
			if(groupFactoryRelTemp == null){
				groupFactoryRel.setGroupId(ps.getId());
				groupFactoryRel.setFactoryId(factory.getId());
				groupFactoryRel.setRemark(purchasingGroupEntity.getRemark());
				groupFactoryRel.setAbolished(0);
				groupFactoryRelService.addNewGroupFactoryRel(groupFactoryRel);
			}
			//添加采购组和采购组织的关系
			GroupOrganizationRelEntity groupOrganizationRelTemp  =  groupOrganizationRelService.getGroupOrganizationRelByGroupIdAndOrgId(ps.getId(), Long.parseLong(purchasingGroupEntity.getOrgId()));
			if(groupOrganizationRelTemp == null){
				groupOrganizationRel.setGroupId(ps.getId());
				groupOrganizationRel.setOrgId(org.getId());
				groupOrganizationRel.setRemark(purchasingGroupEntity.getRemark());
				groupOrganizationRel.setAbolished(0);
				groupOrganizationRelService.addNewGroupOrganizationRel(groupOrganizationRel);
			}
		}

	}
	
	/**
     * 查詢采购组的信息
     */
	public List<PurchasingGroupEntity> findAll(){
		return purchasingGroupDao.findAll();
	}
	
	/**
     * 查詢采购组的信息(有效写的e)
     */
	public List<PurchasingGroupEntity> findByAbolished(Integer abolished ){
		return purchasingGroupDao.findByAbolished(abolished);
	}
	
    /**
     * 获取采购组的列表
     */
	public List<PurchasingGroupEntity> getPurchasingGroupListForCheck(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasingGroupEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), PurchasingGroupEntity.class);
		return purchasingGroupDao.findAll(spec);
	}
	
	
	
	/**
     * 获取采购组的信息
     */
	public  PurchasingGroupEntity getPurchasingGroup(Long id) {
		return purchasingGroupDao.findOne(id);
	}
	
	/**
     * 更新公司
     */
	
	public void update(PurchasingGroupEntity purchasingGroup) {
		purchasingGroupDao.save(purchasingGroup);
	}
	
	
	/**
     * 采购组的信息
     */
	public void deletePurchasingGroup(List<PurchasingGroupEntity> PurchasingGroupList) {
		//purchasingGroupDao.delete(PurchasingGroupList);
		for (PurchasingGroupEntity purchasingGroupEntity : PurchasingGroupList) {
			purchasingGroupDao.abolish(purchasingGroupEntity.getId());
		}
	}
	
	/**
     *作废
     */
	public Map<String, Object> abolishBatch(List<PurchasingGroupEntity> PurchasingGroupList) {
		Map<String,Object> map = new HashMap<String, Object>();
		String msg = "";
		Boolean flag = true;
		
		//验证采购组与工厂、采购组织的关系
		for(PurchasingGroupEntity purchasingGroupEntity : PurchasingGroupList){
			List<GroupFactoryRelEntity> gfList = groupFactoryRelDao.findByGroupIdAndAbolished(purchasingGroupEntity.getId(), 0);
			List<GroupOrganizationRelEntity> goList  =groupOrganizationRelDao.findByGroupIdAndAbolished(purchasingGroupEntity.getId(), 0);
			if(gfList != null && gfList.size() != 0){
				for(GroupFactoryRelEntity gf :gfList ){
					msg = msg + " 采购组："+gf.getGroup().getName()+"已于工厂："+gf.getFactory().getName()+"绑定,无法废除！\r\n";
				}
				flag = false;
			}
           if(goList  != null && goList.size() != 0 ){
        	   for(GroupOrganizationRelEntity go :goList ){
        		   msg = msg + " 采购组："+go.getGroup().getName()+"已于采购组织："+go.getOrg().getName()+"绑定,无法废除！\r\n";
        	   }
        		flag = false;
           }
			
			
		
		}
		if(!flag){
			map.put("msg", msg);
			map.put("success", false);
		}else{
			//废除
			for(PurchasingGroupEntity purchasingGroupEntity : PurchasingGroupList){
				purchasingGroupDao.abolish(purchasingGroupEntity.getId());
			}
			map.put("success", true);
		}
		return map;
	}
	
	/**
	 * 获取生效工厂
	 * @return
	 */
	
	public Map<String, Object> effectBatch(List<PurchasingGroupEntity> purchasingGroupEntitys) {
		Map<String,Object> map = new HashMap<String, Object>();
		int i = 0;
		for(PurchasingGroupEntity purchasingGroupEntity : purchasingGroupEntitys){
			purchasingGroupDao.effect(purchasingGroupEntity.getId());
			i++;
		}
		if( i == purchasingGroupEntitys.size()){
			map.put("msg", "操作成功");
			map.put("success", true);
		}else{
			map.put("msg", "操作失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	
	public PurchasingGroupEntity findByCodeAndName(String code,String name){
		return purchasingGroupDao.findByCodeAndName(code,name);
	}
	
	public PurchasingGroupEntity findByCode(String code){
		return purchasingGroupDao.findByCode(code);
	}
	
}
