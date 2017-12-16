package com.qeweb.scm.vendormodule.service;

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
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialTypeRelDao;
import com.qeweb.scm.vendormodule.repository.BuyerVendorRelDao;


@Service
@Transactional
public class BuyerVendorRelService {

	@Autowired
	private BuyerVendorRelDao buyerVendorRelDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private BuyerMaterialTypeRelDao buyerMaterialTypeRelDao;
	
	@Autowired
	private BuyerMaterialRelDao buyerMaterialRelDao;	
	
	@Autowired
	private MaterialTypeDao materialTypeDao;
	
	@Autowired
	private MaterialDao materialDao;


	/**
	 * 获取供应商采购组织关系列表
	 */
	public Page<BuyerVendorRelEntity> getBuyerVendorRelList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerVendorRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerVendorRelEntity.class);
		return buyerVendorRelDao.findAll(spec,pagin);
	}
	
	public Page<BuyerMaterialRelEntity> getBuyerMaterialRelList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerMaterialRelEntity.class);
		return buyerMaterialRelDao.findAll(spec,pagin);
	}
	
	public Page<BuyerMaterialTypeRelEntity> getBuyerMaterialTypeRelList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerMaterialTypeRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerMaterialTypeRelEntity.class);
		return buyerMaterialTypeRelDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取采购组织列表
	 */
	public Page<OrganizationEntity> getOrgList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<OrganizationEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), OrganizationEntity.class);
		return organizationDao.findAll(spec,pagin);
	}
	
	
	public void saveBuyerVendorRel(Long buyerId,List<OrganizationEntity> vendorList){
		for (OrganizationEntity vendor : vendorList) {
			BuyerVendorRelEntity rel=buyerVendorRelDao.findByBuyerIdAndVendorId(buyerId,vendor.getId());
			if(rel==null){
				rel=new BuyerVendorRelEntity();
				rel.setBuyerId(buyerId);
				rel.setVendorId(vendor.getId());
				buyerVendorRelDao.save(rel);
			}
		}
	}

	public void deleteBuyerVendorRelList(List<BuyerVendorRelEntity> relList) {
		buyerVendorRelDao.delete(relList);
	}
	
	public void saveBuyerMaterialRel(Long buyerId,List<MaterialEntity> materialList){
		for (MaterialEntity material : materialList) {
			BuyerMaterialRelEntity rel=buyerMaterialRelDao.findByBuyerIdAndMaterialId(buyerId,material.getId());
			if(rel==null){
				rel=new BuyerMaterialRelEntity();
				rel.setBuyerId(buyerId);
				rel.setMaterialId(material.getId());
				buyerMaterialRelDao.save(rel);
			}
		}
	}

	public void deleteBuyerMaterialRelList(List<BuyerMaterialRelEntity> relList) {
		buyerMaterialRelDao.delete(relList);
	}
	
	public void saveBuyerMaterialTypeRel(Long buyerId,List<MaterialTypeEntity> materialList){
		for (MaterialTypeEntity material : materialList) {
			BuyerMaterialTypeRelEntity rel=buyerMaterialTypeRelDao.findByBuyerIdAndMaterialTypeId(buyerId,material.getId());
			if(rel==null){
				rel=new BuyerMaterialTypeRelEntity();
				rel.setBuyerId(buyerId);
				rel.setMaterialTypeId(material.getId());
				buyerMaterialTypeRelDao.save(rel);
				saveBuyerMaterialTypeRel(buyerId,material.getParentId());
			}
		}
	}
	
	public void saveBuyerMaterialTypeRel(Long buyerId,Long parentId){
		if(parentId!=null&&parentId!=0){
			BuyerMaterialTypeRelEntity rel=buyerMaterialTypeRelDao.findByBuyerIdAndMaterialTypeId(buyerId,parentId);
			if(rel==null){
				rel=new BuyerMaterialTypeRelEntity();
				rel.setBuyerId(buyerId);
				rel.setMaterialTypeId(parentId);
				buyerMaterialTypeRelDao.save(rel);
				MaterialTypeEntity material=materialTypeDao.findOne(parentId);
				saveBuyerMaterialTypeRel(buyerId,material.getParentId());
			}
		}
	}
	
	

	public void deleteBuyerMaterialTypeRelList(List<BuyerMaterialTypeRelEntity> relList) {
		for (BuyerMaterialTypeRelEntity buyerMaterialTypeRelEntity : relList) {
			deleteBuyerMaterialTypeRelList(buyerMaterialTypeRelEntity.getMaterialTypeId());
		}
		buyerMaterialTypeRelDao.delete(relList);
	}
	
	public void deleteBuyerMaterialTypeRelList(Long typeId){
		List<MaterialTypeEntity> list=materialTypeDao.findByParentIdAndAbolished(typeId, 0);
		if(list!=null){
			for (MaterialTypeEntity materialTypeEntity : list) {
				deleteBuyerMaterialTypeRelList( materialTypeEntity.getId());
				List<BuyerMaterialTypeRelEntity> rels=buyerMaterialTypeRelDao.findByMaterialTypeId(materialTypeEntity.getId());
				if(rels!=null){
					for (BuyerMaterialTypeRelEntity buyerMaterialTypeRelEntity : rels) {
						deleteBuyerMaterialTypeRelList(buyerMaterialTypeRelEntity.getMaterialTypeId());
					}
					buyerMaterialTypeRelDao.delete(rels);
				}
			}
			
		}
		
	}
	
	//根据采购供应商关系获得采购
	public OrganizationEntity getBuyerByVendor(Long vendorId){
		List<Long> buyerIds = buyerVendorRelDao.findBuyerIdsByVendorId(vendorId);
		List<BuyerVendorRelEntity> relList =  buyerVendorRelDao.findByBuyerIdIn(buyerIds);
		return relList.get(0).getBuyer();

	}


}
