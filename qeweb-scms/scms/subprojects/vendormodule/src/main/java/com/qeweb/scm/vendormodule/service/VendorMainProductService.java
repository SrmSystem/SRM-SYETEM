package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorMainProductDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;
/**
 * 主要产品维护
 */
@Service
@Transactional
public class VendorMainProductService {
	
	@Autowired
	protected VendorMainProductDao vendorMainProductDao;
	@Autowired
	protected VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	VendorMaterialTypeRelDao vendorMaterialTypeRelDao;
	
	public Page<VendorMaterialTypeRelEntity> getMainProductList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		
		Specification<VendorMaterialTypeRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialTypeRelEntity.class);
	
		Page<VendorMaterialTypeRelEntity> pageArea = vendorMainProductDao.findAll(spec,pagin);
		return pageArea;
	}
	
	public void saveMainProduct(String mainPt){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<VendorMaterialTypeRelEntity> res=vendorMaterialTypeRelDao.findByOrgId(user.orgId);
		vendorMaterialTypeRelDao.delete(res);
		
		VendorBaseInfoEntity vendorBaseInfo= vendorBaseInfoDao.findByOrgIdAndCurrentVersion(user.orgId,StatusConstant.STATUS_YES);
		List<VendorMaterialTypeRelEntity> venMatTypeList = convertVenMatTypeList(vendorBaseInfo,mainPt);
		vendorBaseInfoDao.save(vendorBaseInfo);
		vendorMainProductDao.save(venMatTypeList);
	}
	
	/**
	 * 转换成主供分类
	 * @param vendorBaseInfo 基本信息
	 * @return 转换后信息
	 */
	private List<VendorMaterialTypeRelEntity> convertVenMatTypeList(VendorBaseInfoEntity vendorBaseInfo,String mainPt) {
		List<VendorMaterialTypeRelEntity> vmtrList = new ArrayList<VendorMaterialTypeRelEntity>();
		String[] matTypeIdArray = mainPt.split(",");
		StringBuilder mainProBui = new StringBuilder();
		for(String matTypeId : matTypeIdArray){
			if(StringUtils.isEmpty(matTypeId)) {
				continue;
			}
			MaterialTypeEntity matType = materialTypeDao.findOne(Long.parseLong(matTypeId));
			
			VendorMaterialTypeRelEntity vmtr = new VendorMaterialTypeRelEntity();
			vmtr.setMaterialTypeCode(matType.getCode());
			vmtr.setMaterialTypeId(matType.getId());
			vmtr.setMaterialTypeName(matType.getName());
			vmtr.setOrgCode(vendorBaseInfo.getOrg().getCode());
			vmtr.setOrgId(vendorBaseInfo.getOrg().getId());
			vmtr.setOrgName(vendorBaseInfo.getOrg().getName());
			vmtrList.add(vmtr);
			mainProBui.append(",").append(matType.getName());
		}
		vendorBaseInfo.setMainProduct(mainProBui.toString().substring(1));
		return vmtrList;
	}

}
