package com.qeweb.scm.vendorperformancemodule.service;

import java.util.HashMap;
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

import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFAllocationEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFAllocationDao;

@Service
@Transactional
public class VendorPerforFAllocationService {
	
	@Autowired
	private VendorPerforFAllocationDao fAllocationDao;

	public Page<VendorPerforFAllocationEntity> getFAllocationList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
//		searchParamMap.put("EQ_currentVersion",""+StatusConstant.STATUS_YES);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforFAllocationEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforFAllocationEntity.class);
		Page<VendorPerforFAllocationEntity> fAllocationEntityPage = fAllocationDao.findAll(spec,pagin);
		return fAllocationEntityPage;
	}

	public Map<String,Object> addFAllocation(VendorPerforFAllocationEntity fAllocationEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforFAllocationEntity vfa=fAllocationDao.findByName(fAllocationEntity.getName());
		if(vfa!=null)
		{
			map.put("success", false);
			map.put("msg", "已经存在此名称!");
			return map;
		}
		fAllocationDao.save(fAllocationEntity);
		map.put("success", true);
		map.put("msg", "添加成功");
		return map;
	}

	public String updateFAllocationStart(VendorPerforFAllocationEntity fAllocationEntity) {
		fAllocationEntity=fAllocationDao.findOne(fAllocationEntity.getId());
		String data="";
		data=fAllocationEntity.getId()+",";
		data=data+fAllocationEntity.getName()+",";
		data=data+fAllocationEntity.getDescribe()+",";
		data=data+fAllocationEntity.getFallValue();
		return data;
	}

	public Map<String,Object> updateFAllocation(VendorPerforFAllocationEntity fAllocationEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforFAllocationEntity vfa=fAllocationDao.findByName(fAllocationEntity.getName());
		if(vfa!=null)
		{
			if(vfa.getId()!=fAllocationEntity.getId())
			{
				map.put("success", false);
				map.put("msg", "已经存在此名称!");
				return map;
			}
		}
		VendorPerforFAllocationEntity fAllocationEntity1=fAllocationDao.findOne(fAllocationEntity.getId());
		fAllocationEntity1.setName(fAllocationEntity.getName());
		fAllocationEntity1.setDescribe(fAllocationEntity.getDescribe());
		fAllocationEntity1.setFallValue(fAllocationEntity.getFallValue());
		fAllocationDao.save(fAllocationEntity1);
		map.put("success", true);
		map.put("msg", "修改成功");
		return map;
	}

	public String deleteFAllocation(List<VendorPerforFAllocationEntity> fAllocationEntitys) {
			fAllocationDao.delete(fAllocationEntitys);
		return "删除成功";
	}
}
