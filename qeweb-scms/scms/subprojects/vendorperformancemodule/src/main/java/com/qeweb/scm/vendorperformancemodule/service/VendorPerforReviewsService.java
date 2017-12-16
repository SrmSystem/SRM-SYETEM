package com.qeweb.scm.vendorperformancemodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforReviewsEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforReviewsDao;

@Service
@Transactional
public class VendorPerforReviewsService {

	@Autowired
	private VendorPerforReviewsDao perforReviewsDao;

	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	
	@Autowired
	private VendorPerforCycleDao cycleDao;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private OrganizationDao orgDao;

	public Page<VendorPerforReviewsEntity> getVendorPerforReviewsList(
			int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforReviewsEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforReviewsEntity.class);
		Page<VendorPerforReviewsEntity>  page= perforReviewsDao.findAll(spec,pagin);
		return page;
	}

	public String releaseReviews(String ids,List<VendorBaseInfoEntity> vendorBaseInfoEntitys) {
		String[] isd=ids.split(",");
		List<VendorPerforReviewsEntity> list=new ArrayList<VendorPerforReviewsEntity>();
		for(VendorBaseInfoEntity v:vendorBaseInfoEntitys)
		{
			
			OrganizationEntity org = orgDao.findOne(v.getOrgId());
			for(int i=1;i<isd.length;i++)
			{
				VendorPerforReviewsEntity vfr=perforReviewsDao.findByVendorIdAndCycleId(v.getId(),Long.parseLong(isd[i]));
				if(null!=vfr)
				{
					return "选择中有存在的参评供应商："+v.getName()+"，周期："+vfr.getCycleEntity().getCycleName();
				}
				VendorPerforReviewsEntity vr=new VendorPerforReviewsEntity();
				vr.setCycleId(Long.parseLong(isd[i]));
				vr.setVendorId(v.getId());
				vr.setOrgId(org.getId());
				vr.setOrgName(org.getName());
				vr.setOrgCode(org.getCode());
				vr.setJoinStatus(StatusConstant.STATUS_YES);
				list.add(vr);
			}
		}
		perforReviewsDao.save(list);
		return "1";
	}

	public String delsReviews(List<VendorPerforReviewsEntity> VendorPerforReviewsEntity) {
		for(VendorPerforReviewsEntity vo:VendorPerforReviewsEntity)
		{
			vo=perforReviewsDao.findOne(vo.getId());
			vo.setJoinStatus(StatusConstant.STATUS_NO);
			perforReviewsDao.save(vo);
		}
		return "1";
	}
	
	public String removeReviews(List<VendorPerforReviewsEntity> VendorPerforReviewsEntity) {
		perforReviewsDao.delete(VendorPerforReviewsEntity);
		return "1";
	}

	public String getVendorPerforCycle() {
		List<VendorPerforCycleEntity> iterable=(List<VendorPerforCycleEntity>) cycleDao.findAll();
		String data="[";
		int i=0;
		for(VendorPerforCycleEntity vendorCycleEntity:iterable){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+vendorCycleEntity.getId()+"\",\"text\":\""+vendorCycleEntity.getCycleName()+"\"}";
					i++;
		}
		data=data+"]";
		return data;  
	}

	public String releaseReviews2(String ids, Map<String, Object> searchParamMap) {
		searchParamMap.put("EQ_currentVersion", "" + StatusConstant.STATUS_YES);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecifications
				.bySearchFilter(filters.values(), VendorBaseInfoEntity.class);
		List<VendorBaseInfoEntity> vendorBaseInfoPage = vendorBaseInfoDao.findAll(spec);
		String[] isd=ids.split(",");
		List<VendorPerforReviewsEntity> list=new ArrayList<VendorPerforReviewsEntity>();
		for(VendorBaseInfoEntity v:vendorBaseInfoPage)
		{
			OrganizationEntity org = orgDao.findOne(v.getOrgId());
			for(int i=1;i<isd.length;i++)
			{
				VendorPerforReviewsEntity vfr=perforReviewsDao.findByVendorIdAndCycleId(v.getId(),Long.parseLong(isd[i]));
				if(null!=vfr)
				{
					VendorPerforCycleEntity vce= cycleDao.findOne(Long.parseLong(isd[i]));
					return "选择中有存在的参评供应商："+v.getName()+"，周期："+vce.getCycleName();
				}
				VendorPerforReviewsEntity vr=new VendorPerforReviewsEntity();
				vr.setCycleId(Long.parseLong(isd[i]));
				vr.setVendorId(v.getId());
				vr.setOrgId(org.getId());
				vr.setOrgName(org.getName());
				vr.setOrgCode(org.getCode());
				vr.setJoinStatus(StatusConstant.STATUS_YES);
				list.add(vr);
				
			}
		}
		perforReviewsDao.save(list);
		return "1";
	}

	public String releaseLevelj(List<VendorPerforReviewsEntity> VendorPerforReviewsEntitys) {
		for(VendorPerforReviewsEntity vo:VendorPerforReviewsEntitys)
		{
			VendorPerforReviewsEntity perforReviews=perforReviewsDao.findOne(vo.getId());
			perforReviews.setJoinStatus(StatusConstant.STATUS_YES);
			perforReviewsDao.save(perforReviews);
		}
		return "1";
	}
}
