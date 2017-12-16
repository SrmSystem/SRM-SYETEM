package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyBaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyDataDao;
import com.qeweb.scm.vendormodule.vo.VendorExampleTransfer;

@Service
@Transactional
public class VendorExampleService {

	@Autowired
	private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;
	@Autowired
	private VendorSurveyBaseDao vendorSurveyBaseDao;
	@Autowired
	private VendorSurveyDataDao vendorSurveyDataDao;
	@Autowired
	private VendorMaterialTypeRelDao vendorMaterialTypeRelDao;
	
	public Page<VendorMaterialSupplyRelEntity> getVendorExampleList(int pageNumber, int pageSize, Map<String, Object> searchParamMap,HttpServletRequest httpServletRequest) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		Page<VendorMaterialSupplyRelEntity> vendorMaterialSupplyRelPage = vendorMaterialSupplyRelDao.findAll(spec,pagin);
		for(VendorMaterialSupplyRelEntity vendorSupplyRelEntity:vendorMaterialSupplyRelPage.getContent())
		{
		    VendorSurveyBaseEntity base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
					"Benchmarking-information", vendorSupplyRelEntity.getOrgId(), 1);
		    if(null!=base)
		    {
		    	VendorMaterialTypeRelEntity vcc=vendorMaterialTypeRelDao.findByOrgIdAndMaterialTypeId(vendorSupplyRelEntity.getOrgId(),vendorSupplyRelEntity.getMaterialEntity().getMaterialType().getId());
		    	if(null!=vcc)
		    	{
				    VendorSurveyDataEntity data = vendorSurveyDataDao.findByBaseIdAndCtIdAndCol1(base.getId(),"biaogan-tb1",""+vcc.getId());
				    if(null!=data)
				    {
				    	vendorSupplyRelEntity.setBiaoganSupplyCoefficient(data.getCol4()+","+data.getCol5());
				    }
		    	}
		    }
		}
		return vendorMaterialSupplyRelPage;
	}

	public List<VendorExampleTransfer> getVendorExampleVo(Map<String, Object> searchParamMap) {
		List<VendorMaterialSupplyRelEntity> list = new ArrayList<VendorMaterialSupplyRelEntity>();
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		list= vendorMaterialSupplyRelDao.findAll(spec);
		if(CollectionUtils.isEmpty(list))
			return null;
		for(VendorMaterialSupplyRelEntity vendorSupplyRelEntity:list)
		{
			 VendorSurveyBaseEntity base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
						"Benchmarking-information", vendorSupplyRelEntity.getOrgId(), 1);
			    if(null!=base)
			    {
			    	VendorMaterialTypeRelEntity vcc=vendorMaterialTypeRelDao.findByOrgIdAndMaterialTypeId(vendorSupplyRelEntity.getOrgId(),vendorSupplyRelEntity.getMaterialEntity().getMaterialTypeId());
				    VendorSurveyDataEntity data = vendorSurveyDataDao.findByBaseIdAndCtIdAndCol1(base.getId(),"biaogan-tb1",""+vcc.getId());
				    if(null!=data)
				    {
				    	vendorSupplyRelEntity.setBiaoganSupplyCoefficient(data.getCol4()+","+data.getCol5());
				    }
			    }
		}
		List<VendorExampleTransfer> ret = new ArrayList<VendorExampleTransfer>();
		VendorExampleTransfer trans = null;
		for(VendorMaterialSupplyRelEntity entity : list) {
			trans = new VendorExampleTransfer();
			if(null!=entity.getVendorBaseInfoEntity())
			{
				trans.setVendorCode(entity.getVendorBaseInfoEntity().getCode());
			}
			if(null!=entity.getVendorName())
			{
				trans.setVendorName(entity.getVendorName());
			}
			if(null!=entity.getBussinessName())
			{
				trans.setBussiness(entity.getBussinessName());
			}
			if(null!=entity.getBrandName())
			{
				trans.setBrandName(entity.getBrandName());
			}
			if(null!=entity.getProductLineName())
			{
				trans.setProductLineName(entity.getProductLineName());
			}
			if(null!=entity.getMaterialEntity())
			{
				trans.setPartsName(entity.getMaterialEntity().getPartsName());
			}
			if(null!=entity.getMaterialEntity())
			{
				trans.setMaterialCode(entity.getMaterialEntity().getCode());
			}
			if(null!=entity.getMaterialEntity())
			{
				trans.setMaterialName(entity.getMaterialEntity().getName());
			}
			trans.setSupplyCoefficient(""+entity.getSupplyCoefficient());
			trans.setBiaoganSupplyCoefficient(""+entity.getBiaoganSupplyCoefficient());
			ret.add(trans);
		}
		return ret;
	}

}
