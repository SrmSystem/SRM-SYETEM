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
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.FactoryService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforParameterEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforParameterDao;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforParamVo;

@Service
@Transactional
public class VendorPerforParameterService {
	
	@Autowired
	private VendorPerforParameterDao vendorPerforParameterDao;
	@Autowired
	private FactoryDao factoryDao;
	@Autowired
	private VendorPerforCycleDao cycleDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	@Autowired
	private FactoryService factoryService;
	@Autowired
	private OrganizationDao organizationDao;

	public Page<VendorPerforParameterEntity> getVendorPerforParameterList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
//		searchParamMap.put("EQ_currentVersion",""+StatusConstant.STATUS_YES);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforParameterEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforParameterEntity.class);
		Page<VendorPerforParameterEntity> vendorPerforParameterEntityPage = vendorPerforParameterDao.findAll(spec,pagin);
		return vendorPerforParameterEntityPage;
	}

	public Map<String,Object> addVendorPerforParameter(VendorPerforParameterEntity param) {
		Map<String,Object> map=new HashMap<String,Object>();
		BussinessRangeEntity br=null;
		br=bussinessRangeDao.findOne(param.getBrandId());
		param.setBrandName(br.getName());
		param.setJoinStatus(StatusConstant.STATUS_YES);
		List<VendorPerforParameterEntity> vpps=vendorPerforParameterDao.findByOrgIdAndCycleIdAndBrandIdAndFactoryId(param.getOrgId(), param.getCycleId(), param.getBrandId(), param.getFactoryId());
		if(vpps!=null&&vpps.size()>0)
		{
			map.put("success", false);
			map.put("msg", "添加失败！！同一个供应商同一周期一个品牌同一个工厂的装车量只能有一条数据");
			return map;
		}
		vendorPerforParameterDao.save(param);
		map.put("success", true);
		map.put("msg", "添加成功");
		return map;
	}

	public VendorPerforParameterEntity updateVendorPerforParameterStart(Long id) {
		return vendorPerforParameterDao.findOne(id);
	}

	public Map<String,Object> updateVendorPerforParameter(VendorPerforParameterEntity vendorPerforParameterEntity) {
		Map<String,Object> map=new HashMap<String,Object>();
		BussinessRangeEntity br=bussinessRangeDao.findOne(vendorPerforParameterEntity.getBrandId());
		
		VendorPerforParameterEntity param = vendorPerforParameterDao.findOne(vendorPerforParameterEntity.getId());
		param.setOrgId(vendorPerforParameterEntity.getOrgId());
		param.setCycleId(vendorPerforParameterEntity.getCycleId()); 
		param.setAssessDate(vendorPerforParameterEntity.getAssessDate());
		param.setBrandId(br.getId());
		param.setBrandName(br.getName());
		param.setFactoryId(vendorPerforParameterEntity.getFactoryId());
		param.setParameter(vendorPerforParameterEntity.getParameter());
		param.setParameterValue(vendorPerforParameterEntity.getParameterValue());
		param.setJoinStatus(StatusConstant.STATUS_YES);
		List<VendorPerforParameterEntity> vpps= vendorPerforParameterDao.findByOrgIdAndCycleIdAndBrandIdAndFactoryId(param.getOrgId(), param.getCycleId(), param.getBrandId(), param.getFactoryId());
		if(vpps!=null&&vpps.size()>1)
		{
			map.put("success", false);
			map.put("msg", "添加失败！！同一供应商同周期一个品牌同一个工厂的装车量只能有一条数据");
			return map;
		}
		vendorPerforParameterDao.save(param);
		map.put("success", true);
		map.put("msg", "修改成功");
		return map;
	}

	public Map<String,Object> delesVendorPerforParameter(List<VendorPerforParameterEntity> vendorPerforParameterEntitys) {
		Map<String,Object> map=new HashMap<String,Object>();
		for(VendorPerforParameterEntity vendorPerforParameterEntity:vendorPerforParameterEntitys)
		{
			VendorPerforParameterEntity vendorPerforParameterEntity1=vendorPerforParameterDao.findOne(vendorPerforParameterEntity.getId());
			vendorPerforParameterEntity1.setJoinStatus(StatusConstant.STATUS_NO);
			vendorPerforParameterDao.save(vendorPerforParameterEntity1);
		}
		map.put("success", true);
		map.put("msg", "作废成功");
		return map;
	}
	public Map<String,Object> deleteVendorPerforParameter(List<VendorPerforParameterEntity> vendorPerforParameterEntitys) {
		Map<String,Object> map=new HashMap<String,Object>();
		for(VendorPerforParameterEntity vendorPerforParameterEntity:vendorPerforParameterEntitys) {
			vendorPerforParameterDao.abolish(vendorPerforParameterEntity.getId());
		}
		map.put("success", true);
		map.put("msg", "删除成功");
		return map;
	}

	public boolean imp(List<VendorPerforParamVo> list, ILogger logger) {
		Map<String, OrganizationEntity> orgMap = Maps.newHashMap();
		Map<String, VendorPerforCycleEntity> cycleMap = getPerforCycleMap();
		Map<String, BussinessRangeEntity> brandMap = getBrandMap();
		Map<String, FactoryEntity> factoryMap = getFactoryMap();
		boolean flag = validationPerforParam(list, orgMap, cycleMap, brandMap, factoryMap, logger);
		if(!flag) {
			logger.log("--> 预处理数据存在错误，请检查文件数据有效性..."); 
			return false;
		}
		VendorPerforParameterEntity params = null;
		logger.log("--> 开始保存导入的参评设置..."); 
		List<VendorPerforParameterEntity> saveList = Lists.newArrayList();
		Map<String,Object> maps=new HashMap<String, Object>();
		for(VendorPerforParamVo vo : list) {
			params = new VendorPerforParameterEntity();
			params.setOrgId(orgMap.get(vo.getOrgCode()).getId()); 
			params.setOrgName(params.getOrg().getName());
			params.setCycleId(cycleMap.get(vo.getCycleName()).getId());
			params.setFactoryId(factoryMap.get(vo.getFactoryName()).getId());
			params.setBrandId(brandMap.get(vo.getBrandCode()).getId());
			params.setBrandName(vo.getBrandName());
			params.setParameter("装车量");
			params.setParameterValue(vo.getParameterValue());
			params.setAssessDate(vo.getAssessDate());
			params.setJoinStatus("Y".equals(vo.getJoinStatus()) ? StatusConstant.STATUS_YES : StatusConstant.STATUS_NO);
			params.setAbolished(0);
			int ik=maps.size();
			String string=params.getOrg()+"-"+params.getCycleId()+"-"+params.getFactoryId()+"-"+params.getBrandId()+"-"+params.getBrandName()+"-"+params.getParameter()+"-"+params.getAssessDate()+"-"+params.getJoinStatus()+"-"+params.getAbolished();
			maps.put(string,params);
			if(ik==maps.size())
			{
				logger.log("-->数据忽略------..."+params.getOrgId()+","+params.getParameterValue()); 
			}
			else
			{
				List<VendorPerforParameterEntity> pps=vendorPerforParameterDao.findByOrgIdAndCycleIdAndBrandIdAndFactoryIdAndParameterAndAssessDateAndJoinStatusAndAbolished(params.getOrgId(), params.getCycleId(), params.getBrandId(), params.getFactoryId(),params.getParameter(),params.getAssessDate(),params.getJoinStatus(),params.getAbolished());
				if(pps.size()==0)
				{
					saveList.add(params);
				}
				else
				{
					logger.log("-->数据忽略------..."+params.getOrgId()+","+params.getParameterValue()); 
				}
			}
		}
		vendorPerforParameterDao.save(saveList);
		logger.log("--> 保存参评设置完成...");
		return true;
	}
	
	private boolean validationPerforParam(List<VendorPerforParamVo> list, Map<String, OrganizationEntity> orgMap,
			Map<String, VendorPerforCycleEntity> cycleMap, Map<String, BussinessRangeEntity> brandMap,
			Map<String, FactoryEntity> factoryMap, ILogger logger) {
		boolean flag = true;
		int index = 0;
		
		OrganizationEntity org = null;
		for(VendorPerforParamVo vo : list) {
			index ++ ;
			//验证
			if(!orgMap.containsKey(vo.getOrgCode())) {
				org = organizationDao.findByCodeAndAbolished(vo.getOrgCode(),0);
				if(org == null) {
					flag = false;
					logger.log("->[FAILED]行索引[" + index + "],供应商编码[" + vo.getOrgCode() + "]未在系统中维护,忽略此参数设置");
				} else {
					orgMap.put(vo.getOrgCode(), org);
				}
			}
			if(cycleMap.get(vo.getCycleName()) == null) {
				flag = false;
				logger.log("->[FAILED]行索引[" + index + "],周期[" + vo.getCycleName() + "]未在系统中维护,忽略此参数设置");
			}
			if(brandMap.get(vo.getBrandCode()) == null) {
				flag = false;
				logger.log("->[FAILED]行索引[" + index + "],品牌[" + vo.getBrandCode() + "]未在系统中维护,忽略此参数设置");
			}
			if(factoryMap.get(vo.getFactoryName()) == null) {
				flag = false;
				logger.log("->[FAILED]行索引[" + index + "],工厂[" + vo.getFactoryName() + "]未在系统中维护,忽略此参数设置");
			}
			if(!"Y".equals(vo.getJoinStatus()) && !"N".equals(vo.getJoinStatus())) {
				flag = false;
				logger.log("->[FAILED]行索引[" + index + "],是否启用[" + vo.getJoinStatus() + "]不正确，启用填写[Y]否则填[N],忽略此参数设置");
			}
		}
		return flag;
	}

	/**
	 * 获取评估周期
	 * @return
	 */
	private Map<String, VendorPerforCycleEntity> getPerforCycleMap() {
		List<VendorPerforCycleEntity> list = cycleDao.findByAbolished(StatusConstant.STATUS_NO);
		Map<String, VendorPerforCycleEntity> retMap = Maps.newHashMap(); 
		if(CollectionUtils.isEmpty(list))
			return retMap;
		
		for(VendorPerforCycleEntity cycle : list) {
			retMap.put(cycle.getCycleName(), cycle);
		}
		return retMap;
	}
	
	private Map<String, BussinessRangeEntity> getBrandMap() {
		Map<String, BussinessRangeEntity> retMap = Maps.newHashMap();
		List<BussinessRangeEntity> list =  bussinessRangeDao.findByBussinessType(2);
		for(BussinessRangeEntity range : list) {
			if(range.getAbolished()==0)
			{
				retMap.put(range.getCode(), range);
			}
		}
		return retMap;
	}
	
	private Map<String, FactoryEntity> getFactoryMap() {
		Map<String, FactoryEntity> facMap = Maps.newHashMap();
		List<FactoryEntity> list = factoryService.findAll();
		for(FactoryEntity f : list) {
			facMap.put(f.getName(), f);
		}
		return facMap;
	}
}
