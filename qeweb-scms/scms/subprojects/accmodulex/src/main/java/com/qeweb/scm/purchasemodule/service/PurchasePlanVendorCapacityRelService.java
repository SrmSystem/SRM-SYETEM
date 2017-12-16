package com.qeweb.scm.purchasemodule.service;

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
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityRelEntity;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorCapacityRelDao;
import com.qeweb.scm.purchasemodule.web.vo.CapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.CapacityViewVO;


/**
 * 供应商产能关系维护service
 */
@Service
@Transactional
public class PurchasePlanVendorCapacityRelService extends BaseService {
	
	@Autowired
	private PurchasePlanVendorCapacityRelDao purchasePlanVendorCapacityRelDao;
	
	@Autowired
	private DictItemService dictItemService;
	
	@Autowired
	private OrgService orgService;

    /**
     * 获取供应商产能关系维护的列表
     */
	public Page<PurchasePlanVendorCapacityRelEntity> getList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVendorCapacityRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), PurchasePlanVendorCapacityRelEntity.class);
		return purchasePlanVendorCapacityRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增供应商产能关系的信息
     */
	public void addNewPurchasePlanVendorCapacityRel(CapacityViewVO capacityViewVO) {
		
		List<OrganizationEntity> vendorList = null;
		List<PurchasePlanVendorCapacityRelEntity> purchasePlanVendorCapacityRelList = new ArrayList<PurchasePlanVendorCapacityRelEntity>();
		PurchasePlanVendorCapacityRelEntity purchasePlanVendorCapacityRel = null;
		String [] vendorIds=capacityViewVO.getVendorIds().split(",");
		if(null!=vendorIds && vendorIds.length>0){
			vendorList=new ArrayList<OrganizationEntity>();
			for (String str : vendorIds) {
				PurchasePlanVendorCapacityRelEntity temp = getPurchasePlanVendorCapacityRelByVendorId(Long.valueOf(str));
				if(temp == null){
					vendorList.add(orgService.getOrg(Long.valueOf(str)));
				}
			}
		}
		 
		for(OrganizationEntity vendor : vendorList){
			purchasePlanVendorCapacityRel = new PurchasePlanVendorCapacityRelEntity();
			purchasePlanVendorCapacityRel.setAbolished(Integer.parseInt(capacityViewVO.getAbolished()));
			purchasePlanVendorCapacityRel.setVendor(vendor);
			purchasePlanVendorCapacityRel.setCapacityCodes(capacityViewVO.getCodes());
			purchasePlanVendorCapacityRelList.add(purchasePlanVendorCapacityRel);
		}
		purchasePlanVendorCapacityRelDao.save(purchasePlanVendorCapacityRelList);
	}
	
	/**
     * 获取单个供应商产能关系的信息
     */
	public  PurchasePlanVendorCapacityRelEntity getPurchasePlanVendorCapacityRel(Long id) {
		return purchasePlanVendorCapacityRelDao.findOne(id);
	}
	
	/**
     * 通过供应商的id获取供应商产能关系的信息
     */
	public  PurchasePlanVendorCapacityRelEntity getPurchasePlanVendorCapacityRelByVendorId(Long id) {
		return purchasePlanVendorCapacityRelDao.findOneByVendorId(id);
	}
	
	/**
     * 更新供应商产能关系
     */
	
	public void update(CapacityViewVO capacityViewVO) {
		PurchasePlanVendorCapacityRelEntity purchasePlanVendorCapacityRel = getPurchasePlanVendorCapacityRel(Long.valueOf(capacityViewVO.getCapacityId()));
		if(purchasePlanVendorCapacityRel != null){
			purchasePlanVendorCapacityRel.setCapacityCodes(capacityViewVO.getCodes());
		}
		purchasePlanVendorCapacityRelDao.save(purchasePlanVendorCapacityRel);
	}
	
	/**
     * 删除供应商产能关系
     */
	public void deletePurchasePlanVendorCapacityRelList(List<PurchasePlanVendorCapacityRelEntity> purchasePlanVendorCapacityRelList) {
		purchasePlanVendorCapacityRelDao.delete(purchasePlanVendorCapacityRelList);
	}
	
	/**
     *作废关系
     */
	public Map<String, Object> abolishBatch(List<PurchasePlanVendorCapacityRelEntity> purchasePlanVendorCapacityRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		int i =0;
		//废除
		for(PurchasePlanVendorCapacityRelEntity PurchasePlanVendorCapacityRel : purchasePlanVendorCapacityRelList){
			purchasePlanVendorCapacityRelDao.abolish(PurchasePlanVendorCapacityRel.getId());
			i++;
		}
		if(i == purchasePlanVendorCapacityRelList.size()){
			map.put("success", true);
		}else{
			
		}map.put("success", false);
		return map;
	}
	/**
     *获取产能表的信息 List<CapacityVO>
     */
	public List<CapacityVO> getCapacityVOList(Long id) {
		List<CapacityVO> cvList = new ArrayList<CapacityVO>();
		CapacityVO cv = null;
		//获取产能配置信息
		List<DictItemEntity> diList =  dictItemService.findListByDictCode("FORECAST_CAPACITY");
		//获取存储的信息
		PurchasePlanVendorCapacityRelEntity cr = purchasePlanVendorCapacityRelDao.findOne(id);
		//格式化数据
		Map<String,String> codeMap = new HashMap<String, String>();
		if(cr != null){
			String [] codes=cr.getCapacityCodes().split(",");
			if(null!=codes && codes.length>0){
				for (String str : codes) {
					codeMap.put(str, str);
				}
			}
		}
		for(DictItemEntity dictItem : diList){
			if( codeMap.get(dictItem.getCode()) !=null){
				cv = new CapacityVO();
				cv.setChecked("checked");
				cv.setCode(dictItem.getCode());
				cv.setName(dictItem.getName());
				cvList.add(cv);
			}else{
				cv = new CapacityVO();
				cv.setChecked("");
				cv.setCode(dictItem.getCode());
				cv.setName(dictItem.getName());
				cvList.add(cv);
			}
		}
		return cvList;
	}

}

