package com.qeweb.scm.vendorperformancemodule.service;

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

import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.ResultUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforModelEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforModelDao;

@Service
@Transactional
public class VendorPerforModelService extends BaseService {
	
	@Autowired
	private VendorPerforModelDao dao;
	
	public Page<VendorPerforModelEntity> queryList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforModelEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforModelEntity.class);
		Page<VendorPerforModelEntity> page = dao.findAll(spec,pagin);
		return page;
	}
	
	/**
	 * 获得有效的绩效体系
	 * @return
	 */
	public List<VendorPerforModelEntity> getEnableList(){
		return dao.findByEnableStatus(StatusConstant.STATUS_YES);
	}
	/**
	 * 获得有效的绩效体系
	 * @return
	 */
	public List<VendorPerforModelEntity> getEnableByCode(String code){
		return dao.findByEnableStatusAndCode(StatusConstant.STATUS_YES,code);
	}
	
	/**
	 * 新增
	 * @param model 绩效模型
	 * @return
	 */
	public Map<String,Object> add(VendorPerforModelEntity model){
		Map<String,Object> map = ResultUtil.createMap();
		model.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.MODEL));
		VendorPerforModelEntity sameModel = dao.findByName(model.getName()); 
		if(sameModel!=null){
			map.put(ResultUtil.SUCCESS, false);
			map.put(ResultUtil.MSG, "已存在相同名称!");
			return map;
		}
		dao.save(model);
		return map;
	}
	
	/**
	 * 更新绩效模型
	 * @param model 绩效模型
	 * @return
	 */
	public Map<String,Object> update(VendorPerforModelEntity model){
		Map<String,Object> map = ResultUtil.createMap();
		VendorPerforModelEntity sameModel = dao.findByNameAndIdNot(model.getName(),model.getId());
		if(sameModel!=null){
			map.put(ResultUtil.SUCCESS, false);
			map.put(ResultUtil.MSG, "已存在相同名称!");
			return map;
		}
		dao.save(model);
		return map;
	}
	
	/**
	 * 批量删除
	 * @param idList id集合
	 * @return 删除结果
	 */
	public Map<String,Object> batchDelete(List<Long> idList){
		Map<String,Object> map = ResultUtil.createMap();
		for(Long id : idList){
			dao.delete(id);
		}
		return map;
	}

	/**
	 * 根据ID获取绩效模型
	 * @param id 绩效模型ID
	 * @return 绩效模型
	 */
	public VendorPerforModelEntity get(Long id) {
		return dao.findOne(id);
	}
	
	
	
}
