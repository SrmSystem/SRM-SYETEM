package com.qeweb.scm.purchasemodule.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.entity.PurchaseMaterialPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseMaterialVendorPlanEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseMaterialPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseMaterialVendorPlanDao;

/**
 * 预测计划管理service
 */
@Service
@Transactional(rollbackOn=Exception.class)  
public class PurchaseMaterialPlanService extends BaseService {

	@Autowired
	private PurchaseMaterialPlanDao purchaseMaterialPlanDao;
	
	@Autowired
	private PurchaseMaterialVendorPlanDao purchaseMaterialVendorPlanDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	

	public Page<PurchaseMaterialPlanEntity> getMaterialPlanList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseMaterialPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseMaterialPlanEntity.class);
		Page<PurchaseMaterialPlanEntity> page = purchaseMaterialPlanDao.findAll(spec, pagin);
		return page;
	}
	
	public Page<PurchaseMaterialVendorPlanEntity> getMaterialVendorPlanList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseMaterialVendorPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseMaterialVendorPlanEntity.class);
		Page<PurchaseMaterialVendorPlanEntity> page = purchaseMaterialVendorPlanDao.findAll(spec, pagin);
		return page;
	}

	public void saveMaterialPlanVendor(PurchaseMaterialPlanEntity plan,
			List<PurchaseMaterialVendorPlanEntity> itemList) {
		List<PurchaseMaterialVendorPlanEntity> oldPlans=purchaseMaterialVendorPlanDao.findByPlanId(plan.getId());

		Set<Long> ids=new HashSet<Long>();
		if(oldPlans!=null){
			for (PurchaseMaterialVendorPlanEntity purchaseMaterialVendorPlanEntity : oldPlans) {
				ids.add(purchaseMaterialVendorPlanEntity.getId());
			}
		}
		
		for (PurchaseMaterialVendorPlanEntity item : itemList) {
			OrganizationEntity vendor=organizationDao.findOne(item.getVendorId());
			item.setVendor(vendor);
			if(item.getId()==0){
				item.setPlan(plan);
				purchaseMaterialVendorPlanDao.save(item);
			}else{
				ids.remove(item.getId());
				PurchaseMaterialVendorPlanEntity old=purchaseMaterialVendorPlanDao.findOne(item.getId());
				old.setPlanNum(item.getPlanNum());
				purchaseMaterialVendorPlanDao.save(old);
			}
		}
		
		for (Long long1 : ids) {
			purchaseMaterialVendorPlanDao.delete(long1);
		}
		Integer publishStatus=plan.getPublishStatus();
		plan=purchaseMaterialPlanDao.findOne(plan.getId());
		plan.setPublishStatus(publishStatus);
		if(publishStatus==1){
			plan.setPublishTime(DateUtil.getCurrentTimestamp());
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			plan.setPublishUserId(user.id);
	    }
		purchaseMaterialPlanDao.save(plan);
		
	}
	

	
	
	

	
}
