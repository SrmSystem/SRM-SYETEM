package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Service
@Transactional
public class BussinessRangeService {
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	
	@Resource
	private GenerialDao dao;
	
	private final static String SERIALNUMBER_KEY = "BUS_RANGE_CODE";//业务范围流水号标记
	private final static String SERIALNUMBER_TYPE_KEY = "BUS_TYPE_CODE";//业务类型流水号标记
	private final static String SERIALNUMBER_BRAND_KEY = "BUS_BRAND_CODE";//业务品牌流水号标记
	private final static String SERIALNUMBER_LINE_KEY = "BUS_LINE_CODE";//产品线流水号标记


	public Page<BussinessRangeEntity> getBussinessRangeList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BussinessRangeEntity> spec= null;
		if("2".equals(searchParamMap.get("EQ_bussinessType")+"")){
			spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BussinessRangeEntity.class);
		}else{
			spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), BussinessRangeEntity.class);
		}
		return bussinessRangeDao.findAll(spec,pagin);
	}

    /**
     * 新增业务范围
     * @param bussinessRange
     */
	public void addNewBussinessRange(BussinessRangeEntity bussinessRange) {
		if(StringUtils.isEmpty(bussinessRange.getCode())){
			bussinessRange.setCode(serialNumberService.geneterNextNumberByKey(SERIALNUMBER_KEY));
		}
		bussinessRangeDao.save(bussinessRange);
	}

	public BussinessRangeEntity getBussinessRange(Long id) {
		return bussinessRangeDao.findOne(id);
	}

	/**
	 * 更新业务范围
	 * @param bussinessRange
	 */
	public void updateBussinessRange(BussinessRangeEntity bussinessRange) {
		bussinessRangeDao.save(bussinessRange);
	}

	public void deleteBussinessRangeList(List<BussinessRangeEntity> bussinessRangeList) {
		//bussinessRangeDao.delete(bussinessRangeList);
		for (BussinessRangeEntity bussinessRange : bussinessRangeList) {
			bussinessRangeDao.abolish(bussinessRange.getId());
		}
	}

    /**
     * 作废的方法，统一处理
     * @param bussinessRangeList
     * @return 
     */
	public Map<String, Object> abolishBatch(List<BussinessRangeEntity> bussinessRangeList) {
		for(BussinessRangeEntity bussniessRange : bussinessRangeList){
			bussinessRangeDao.abolish(bussniessRange.getId());
			List<BussinessRangeEntity> list=bussinessRangeDao.findByAbolishedAndParentId(StatusConstant.STATUS_NO, bussniessRange.getId());
			abolishBatch(list);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	/**
	 * 获取所有的未作废的业务范围
	 * @param id 
	 * @return
	 */
	public List<BussinessRangeEntity> getByBussinessRange(Long id) {
		return bussinessRangeDao.findByAbolishedAndParentId(StatusConstant.STATUS_NO,id);
	}

	
	/**
	 * 新增一个业务类型，为了区别业务范围
	 * @param bussinessRange
	 */
	public void addNewBussinessRangeType(BussinessRangeEntity bussinessRange) {
		
		if(StringUtils.isEmpty(bussinessRange.getCode())){
			bussinessRange.setCode(serialNumberService.geneterNextNumberByKey(SERIALNUMBER_TYPE_KEY));
		}
		bussinessRangeDao.save(bussinessRange);
		
	}

	/**
	 * 更新一个业务类型，为了区别业务范围
	 * @param bussinessRange
	 */
	public void updateBussinessRangeType(BussinessRangeEntity bussinessRange) {
		bussinessRangeDao.save(bussinessRange);
	}
	
	
	/**
	 * 新增一个品牌
	 * @param bussinessRange
	 */
	public void addNewBussinessRangeBrand(BussinessRangeEntity bussinessRange) {
		if(StringUtils.isEmpty(bussinessRange.getCode())){
			bussinessRange.setCode(serialNumberService.geneterNextNumberByKey(SERIALNUMBER_BRAND_KEY));
		}
		bussinessRangeDao.save(bussinessRange);
		
	}
	
	/**
	 * 更新一个品牌
	 * @param bussinessRange
	 */
	public void updateBussinessRangeBrand(BussinessRangeEntity bussinessRange) {
		bussinessRangeDao.save(bussinessRange);
	}
	
	/**
	 * 新增一个产品线
	 * @param bussinessRange
	 */
	public void addNewBussinessRangeLine(BussinessRangeEntity bussinessRange) {
		if(StringUtils.isEmpty(bussinessRange.getCode())){
			bussinessRange.setCode(serialNumberService.geneterNextNumberByKey(SERIALNUMBER_LINE_KEY));
		}
		bussinessRangeDao.save(bussinessRange);
		
	}
	
	/**
	 * 更新一个产品线
	 * @param bussinessRange
	 */
	public void updateBussinessRangeLine(BussinessRangeEntity bussinessRange) {
		bussinessRangeDao.save(bussinessRange);
	}

	public List<BussinessRangeEntity> getBussinessRangeByParentIdAndBussinessType(
			Long id, Integer type) {
		List<BussinessRangeEntity> lists = new ArrayList<BussinessRangeEntity>();
		List<BussinessRangeEntity> list = bussinessRangeDao.findByParentIdAndBussinessType(id,type);
		for(BussinessRangeEntity range : list){
			if(range.getAbolished()==0)
			{
				lists.add(range);
			}
		}
		return lists;
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly=true)  
	public List<BussinessRangeEntity> getBussinessRangeByBussinessType(
			Integer type) {
		List<BussinessRangeEntity> list = bussinessRangeDao.findByBussinessType(type);
		Map<String, Object> map=new HashMap<String, Object>();
		List<BussinessRangeEntity> lists = new ArrayList<BussinessRangeEntity>();
		for(BussinessRangeEntity range : list){
			map.put(range.getCode(), range);
//			range.setName(range.getName()+"(" + (range.getRange() == null ? "" : range.getRange().getName()) + ")");   
			if(range.getAbolished()==0)
			{
				lists.add(range);
			}
		}
		return lists;    
	}
	
	public List<BussinessRangeEntity> getBussinessRangeByCodeAndBussinessType(String code,
			Integer type) {
		return bussinessRangeDao.findByCodeAndBussinessType(code,type);
	}

	public List<BussinessRangeEntity> getBussinessRangeTypeList(Long id) {
		if(id!=null && id.longValue()>0l){
			return Lists.newArrayList(bussinessRangeDao.findOne(id));
		}
		return bussinessRangeDao.findByBussinessTypeAndAbolished(1, StatusConstant.STATUS_NO);
	}

	public List<BussinessRangeEntity> getOther(String other) {
		List<BussinessRangeEntity> list = bussinessRangeDao.findByBussinessTypeAndAbolished(1, StatusConstant.STATUS_NO);
		int count = dao.query(other);
		if(count<=0){
			return null;
		}
		return list;
	}

}
