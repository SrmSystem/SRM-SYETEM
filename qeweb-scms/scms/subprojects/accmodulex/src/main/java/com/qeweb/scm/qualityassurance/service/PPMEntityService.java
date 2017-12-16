package com.qeweb.scm.qualityassurance.service;

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
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer1;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer2;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer3;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer4;
import com.qeweb.scm.qualityassurance.web.vo.PPMTransfer5;

@Service
@Transactional
public class PPMEntityService {

	@Autowired
	private PPMEntityDao pPmEntityDao;

	public Page<PPMEntity> getPPmList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		return pPmEntityDao.findAll(spec, pagin);
	}

	public List<PPMTransfer1> getPPMVo1(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		List<PPMEntity> list = pPmEntityDao.findAll(spec);
		List<PPMTransfer1> ret = new ArrayList<PPMTransfer1>();
		PPMTransfer1 trans = null;
		for(PPMEntity p : list) {
			trans = new PPMTransfer1();
			trans.setMonth(p.getMonth());
			trans.setVendorCode(p.getVendor().getCode());
			trans.setVendorName(p.getVendor().getName());
			trans.setPpm(p.getPpm()+"");
			ret.add(trans);
		}
		return ret;
	}

	public List<PPMTransfer2> getPPMVo2(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		List<PPMEntity> list = pPmEntityDao.findAll(spec);
		List<PPMTransfer2> ret = new ArrayList<PPMTransfer2>();
		PPMTransfer2 trans = null;
		for(PPMEntity p : list) {
			trans = new PPMTransfer2();
			trans.setMonth(p.getMonth());
			trans.setVendorCode(p.getVendor().getCode());
			trans.setVendorName(p.getVendor().getName());
			trans.setMaterialCode(p.getMaterial().getCode());
			trans.setMaterialName(p.getMaterial().getName());
			trans.setRate(p.getRate()+"");
			ret.add(trans);
		}
		return ret;
	}

	public List<PPMTransfer3> getPPMVo3(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		List<PPMEntity> list = pPmEntityDao.findAll(spec);
		List<PPMTransfer3> ret = new ArrayList<PPMTransfer3>();
		PPMTransfer3 trans = null;
		for(PPMEntity p : list) {
			trans = new PPMTransfer3();
			trans.setMonth(p.getMonth());
			trans.setVendorCode(p.getVendor().getCode());
			trans.setVendorName(p.getVendor().getName());
			trans.setMaterialCode(p.getMaterial().getCode());
			trans.setMaterialName(p.getMaterial().getName());
			trans.setPpm(p.getPpm()+"");
			ret.add(trans);
		}
		return ret;
	}

	public List<PPMTransfer4> getPPMVo4(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		List<PPMEntity> list = pPmEntityDao.findAll(spec);
		List<PPMTransfer4> ret = new ArrayList<PPMTransfer4>();
		PPMTransfer4 trans = null;
		for(PPMEntity p : list) {
			trans = new PPMTransfer4();
			trans.setMonth(p.getMonth());
			trans.setVendorCode(p.getVendor().getCode());
			trans.setVendorName(p.getVendor().getName());
			trans.setMaterialCode(p.getMaterial().getCode());
			trans.setMaterialName(p.getMaterial().getName());
			trans.setPpm(p.getPpm()+"");
			ret.add(trans);
		}
		return ret;
	}

	public List<PPMTransfer5> getPPMVo5(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PPMEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PPMEntity.class);
		List<PPMEntity> list = pPmEntityDao.findAll(spec);
		List<PPMTransfer5> ret = new ArrayList<PPMTransfer5>();
		PPMTransfer5 trans = null;
		for(PPMEntity p : list) {
			trans = new PPMTransfer5();
			trans.setMonth(p.getMonth());
			trans.setVendorCode(p.getVendor().getCode());
			trans.setVendorName(p.getVendor().getName());
			trans.setRate(p.getRate()+"");
			ret.add(trans);
		}
		return ret;
	}
	
}
