package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
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
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;


/**
 * 供应商物料分类关系
 * @author pjjxiajun
 * @date 2015年7月28日
 * @path com.qeweb.scm.vendormodule.service.VendorMaterialTypeRelService.java
 */
@Service
@Transactional
public class VendorMaterialTypeRelService {

	@Autowired
	private VendorMaterialTypeRelDao dao;
	
	@Autowired
	private MaterialTypeDao materialTypeDao;
	
	@Autowired
	private OrganizationDao orgDao;

	/**
	 * 根据orgId获取对应的数据
	 * @param orgId 供应商ID
	 * @param id 指定ID
	 * @return 
	 */
	public List<VendorMaterialTypeRelEntity> getListByOrgId(Long orgId, Long id) {
		if(id!=null && id>0l){
			return dao.findById(id);
		}
		return dao.findByOrgId(orgId);
	}

	/**
	 * 获取供应商物料类型关系列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<VendorMaterialTypeRelEntity> getVendorMaterialTypeRelList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialTypeRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialTypeRelEntity.class);
		return dao.findAll(spec,pagin);
	}
	
	/**
	 * 获取物料类型列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<MaterialTypeEntity> getMaterialTypeList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialTypeEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialTypeEntity.class);
		return materialTypeDao.findAll(spec,pagin);
	}

	/**
	 * 删除
	 * @param list
	 */
	public void delRel(List<VendorMaterialTypeRelEntity> list){
		dao.delete(list);
	}
	
	/**
	 * 选择物料类型保存关系
	 * @param list
	 */
	public void saveRel(Long vendorId,List<MaterialTypeEntity> list){
		for (MaterialTypeEntity materialTypeEntity : list) {
			VendorMaterialTypeRelEntity po =dao.findByOrgIdAndMaterialTypeId(vendorId, materialTypeEntity.getId());
			if(po==null){
				po=new VendorMaterialTypeRelEntity();
				MaterialTypeEntity parent=materialTypeDao.findOne(materialTypeEntity.getParentId());
				if(parent!=null){
					po.setTopMaterialTypeName(parent.getName());
				}
				OrganizationEntity org=orgDao.findOne(vendorId);
				po.setOrgId(vendorId);
				po.setOrgCode(org.getCode());
				po.setOrgName(org.getName());
				po.setMaterialTypeId(materialTypeEntity.getId());
				po.setMaterialTypeCode(materialTypeEntity.getCode());
				po.setMaterialTypeName(materialTypeEntity.getName());
				dao.save(po);
			}
		}
	}

	public List<String> getYear(String styil) {
		List<String> list=new ArrayList<String>();
		if(styil!=null&&!(styil.equals("")))
		{
			list.add(styil);
		}
		else
		{
			int yes=DateUtil.getCurrentYear()-10;
			for(int i=0;i<=20;i++)
			{
				list.add((yes+i)+"");
			}
		}
		return list;
	}

}
