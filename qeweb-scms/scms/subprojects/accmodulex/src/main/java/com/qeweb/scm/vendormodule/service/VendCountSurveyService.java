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
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.vo.VendCountSurveyTransfer;

@Service
@Transactional
public class VendCountSurveyService {
	
	@Autowired
	private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;


	public Page<VendorMaterialSupplyRelEntity> getVendCountSurveyList(
			int pageNumber, int pageSize, Map<String, Object> searchParamMap,
			HttpServletRequest request) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		Page<VendorMaterialSupplyRelEntity> VendorMaterialSupplyRelPage = vendorMaterialSupplyRelDao.findAll(spec,pagin);
//		request.getSession().setAttribute("VendorMaterialSupplyRelEntitys",VendorMaterialSupplyRelPage.getContent());
		return VendorMaterialSupplyRelPage;
	}

	public List<VendCountSurveyTransfer> getVendCountSurveyVo(Map<String,Object> searchParamMap) {
		List<VendorMaterialSupplyRelEntity> list = new ArrayList<VendorMaterialSupplyRelEntity>();
		
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		list=vendorMaterialSupplyRelDao.findAll(spec);
		if(CollectionUtils.isEmpty(list))
			return null;
		
		List<VendCountSurveyTransfer> ret = new ArrayList<VendCountSurveyTransfer>();
		VendCountSurveyTransfer trans = null;
		for(VendorMaterialSupplyRelEntity entity : list) {
			trans = new VendCountSurveyTransfer();
			if(entity.getVendorBaseInfoEntity()!=null)
			{
				trans.setCode(entity.getVendorBaseInfoEntity().getCode());
				trans.setShortName(entity.getVendorBaseInfoEntity().getShortName());
				trans.setDuns(entity.getVendorBaseInfoEntity().getDuns());
				trans.setProperty(entity.getVendorBaseInfoEntity().getProperty());
				if(entity.getVendorBaseInfoEntity().getIpo()!=null)
				{
					if(entity.getVendorBaseInfoEntity().getIpo()==1)
						trans.setIpo("是");
					else
						trans.setIpo("否");
				}
				trans.setName(entity.getVendorBaseInfoEntity().getName());
				if(entity.getVendorBaseInfoEntity().getVendorPhase()!=null)
				{
					trans.setVendorPhasName(entity.getVendorBaseInfoEntity().getVendorPhase().getName());
				}
				trans.setVendorClassify2(entity.getVendorBaseInfoEntity().getVendorClassify2());
				trans.setVendorLevel(entity.getVendorBaseInfoEntity().getVendorLevel());
			
			}
			trans.setMaterialId(""+entity.getMaterialId());
			trans.setXitong("");
			trans.setBrandName(entity.getBrandName());
			trans.setFactoryName(entity.getFactoryName());
			trans.setSupplyCoefficient(""+entity.getSupplyCoefficient());
			trans.setProductLineName(""+entity.getProductLineName());

			ret.add(trans);
		}
		return ret;
	}
}
