package com.qeweb.scm.vendorperformancemodule.service;

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
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMaterialTypeEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforVendorMatEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforMaterialTypeDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforVendorMatDao;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforMATVo;

@Service
@Transactional
public class VendorPerforMaterialTypeService {
	
	@Autowired
	private VendorPerforMaterialTypeDao materialTypeDao;
	
	@Autowired
	private VendorPerforVendorMatDao vendorMatDao;

	public Page<VendorPerforMaterialTypeEntity> getPage(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforMaterialTypeEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforMaterialTypeEntity.class);
		Page<VendorPerforMaterialTypeEntity> page = materialTypeDao.findAll(spec,pagin);
		return page;
	}

	public Map<String, Object> addUpdateMaterialType(VendorPerforMaterialTypeEntity materialTypeEntity) {
		Map<String, Object>  map=new HashMap<String, Object>();
		materialTypeDao.save(materialTypeEntity);
		map.put("success",true);
		map.put("msg", "执行成功！！");
		return map;
	}
	public String releaseMaterialType(List<VendorPerforMaterialTypeEntity> materialTypeEntitys) {
		for(VendorPerforMaterialTypeEntity materialTypeEntity:materialTypeEntitys)
		{
			materialTypeEntity.setAbolished(0);
			materialTypeDao.save(materialTypeEntity);
		}
		return "1";
	}

	public String delsMaterialType(List<VendorPerforMaterialTypeEntity> materialTypeEntitys) {
		for(VendorPerforMaterialTypeEntity materialTypeEntity:materialTypeEntitys)
		{
			materialTypeDao.abolish(materialTypeEntity.getId());
		}
		return "1";
	}

	public boolean combine(List<VendorPerforMATVo> list, ILogger logger) {
		List<VendorPerforMaterialTypeEntity> materialTypeEntitys=new ArrayList<VendorPerforMaterialTypeEntity>();
		for(VendorPerforMATVo matvo:list)
		{
			VendorPerforMaterialTypeEntity materialTypeEntity=new VendorPerforMaterialTypeEntity();
			materialTypeEntity.setCode(matvo.getMaterialCode());
			materialTypeEntity.setName(matvo.getMaterialName());
			materialTypeEntity.setRemarks(matvo.getRb());
			materialTypeEntity.setAbolished(0);
			materialTypeEntitys.add(materialTypeEntity);
		}
		materialTypeDao.save(materialTypeEntitys);
		return true;
	}

	public String deletesMaterialType(List<VendorPerforMaterialTypeEntity> materialTypeEntitys) {
		for(VendorPerforMaterialTypeEntity materialTypeEntity:materialTypeEntitys)
		{
			List<VendorPerforVendorMatEntity> list=vendorMatDao.findByMaterialtypeId(materialTypeEntity.getId());
			if(list!=null&&list.size()>0)
			{
				vendorMatDao.delete(list);
			}
		}
		materialTypeDao.delete(materialTypeEntitys);
		return "1";
	}
}
