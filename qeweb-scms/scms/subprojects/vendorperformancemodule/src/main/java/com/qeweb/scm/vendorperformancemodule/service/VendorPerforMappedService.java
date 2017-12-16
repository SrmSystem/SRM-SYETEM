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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMappedEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforMappedDao;

@Service
@Transactional
public class VendorPerforMappedService {
	
	@Autowired
	private VendorPerforMappedDao mappedDao;

	public Page<VendorPerforMappedEntity> getMappedList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforMappedEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforMappedEntity.class);
		Page<VendorPerforMappedEntity> mappedEntityPage = mappedDao.findAll(spec,pagin);
		return mappedEntityPage;
	}

	public Map<String,Object> addMapped(VendorPerforMappedEntity mappedEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforMappedEntity vm=mappedDao.findByNameAndMappedName(mappedEntity.getName(),mappedEntity.getMappedName());
		if(vm!=null)
		{
			map.put("success", false);
			map.put("msg", "已经存在此映射名称的字符!");
			return map;
		}
		mappedDao.save(mappedEntity);
		map.put("success", true);
		map.put("msg", "添加成功");
		return map;
	}

	public Map<String,Object> updateMapped(VendorPerforMappedEntity mappedEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforMappedEntity vm=mappedDao.findByNameAndMappedName(mappedEntity.getName(),mappedEntity.getMappedName());
		if(vm!=null)
		{
			if(vm.getId()!=mappedEntity.getId())
			{
				map.put("success", false);
				map.put("msg", "已经存在此映射名称的字符!");
				return map;
			}
		}
		VendorPerforMappedEntity mappedEntity1=mappedDao.findOne(mappedEntity.getId());
		mappedEntity1.setName(mappedEntity.getName());
		mappedEntity1.setDescribe(mappedEntity.getDescribe());
		mappedEntity1.setMappedType(mappedEntity.getMappedType());
		mappedEntity1.setMappedName(mappedEntity.getMappedName());
		mappedEntity1.setMappedValue(mappedEntity.getMappedValue());
		mappedDao.save(mappedEntity1);
		map.put("success", true);
		map.put("msg", "修改成功");
		return map;
	}

	public Map<String,Object> deleteMapped(List<VendorPerforMappedEntity> mappedEntitys) {
		Map<String,Object> map=new HashMap<String, Object>();
		for(VendorPerforMappedEntity mappedEntity:mappedEntitys)
			mappedDao.delete(mappedEntity.getId());
		
		map.put("success", true);
		map.put("msg", "删除成功");
		return map;
	}
}
