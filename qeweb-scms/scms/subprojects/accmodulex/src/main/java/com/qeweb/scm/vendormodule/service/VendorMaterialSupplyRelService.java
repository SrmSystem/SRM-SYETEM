package com.qeweb.scm.vendormodule.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.persistence.SearchFilterEx.Operator;
import com.qeweb.scm.basemodule.constants.TypeConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.vo.StatisticsVendorBrandOverlapTransfer;
import com.qeweb.scm.vendormodule.vo.VendorMaterialSupplyRelTransfer;
/**
 * 供货关系Service
 * @author lw
 * 创建时间：2015年6月15日 09:43:30
 * 最后更新时间：2015年6月29日10:21:59
 * 最后更新人：lw
 */
@Service
@Transactional
public class VendorMaterialSupplyRelService {
	
	@Autowired
	private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;

	@Autowired
	private BussinessRangeDao bussinessRangeDao;

	
	@Autowired
	private FactoryDao factoryDao;

	public Page<VendorMaterialSupplyRelEntity> getVendorMaterialSupplyRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		return vendorMaterialSupplyRelDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取所有供应商品牌关系
	 * @param searchParamMap
	 * @return
	 */
	public List<VendorMaterialSupplyRelEntity> getVendorMaterialSupplyRelAll(Map<String, Object> searchParamMap) {

		String vendorName = "%"+(searchParamMap.get("LIKE_vendorName")==null?"":searchParamMap.get("LIKE_vendorName"))+"%";
		String materialName = "%"+(searchParamMap.get("LIKE_materialName")==null?"":searchParamMap.get("LIKE_materialName"))+"%";
		List<VendorMaterialSupplyRelEntity> retList = new ArrayList<VendorMaterialSupplyRelEntity>();
		List list= vendorMaterialSupplyRelDao.findlist3(vendorName,materialName);
		
		for (int i = 0 ; i < list.size(); i++) {
			VendorMaterialSupplyRelEntity v = new VendorMaterialSupplyRelEntity();
			Object [] arr = (Object[]) list.get(i);
			v.setBrandName(arr[0]+"");
			v.setVendorName(arr[1]+"");
			VendorBaseInfoEntity vendorBaseInfoEntity=new VendorBaseInfoEntity();
			vendorBaseInfoEntity.setCode(arr[3]+"");
			v.setVendorBaseInfoEntity(vendorBaseInfoEntity);
			retList.add(v);
		}
		return retList;
	}
	
	/**
	 * 根据物料关系ID获取供货关系列表
	 * @param id
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<VendorMaterialSupplyRelEntity> getVendorMaterialSupplyRelByRelId(Long id,int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		SearchFilterEx filter = new SearchFilterEx("materialRelId", Operator.EQ, id);
		filters.put("materialRelId", filter);
		Specification<VendorMaterialSupplyRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialSupplyRelEntity.class);
		return vendorMaterialSupplyRelDao.findAll(spec,pagin);
	}
	public List<VendorMaterialSupplyRelEntity> getVendorMaterialSupplyRelByMaterialRelId(Long materialRelId) {
		return vendorMaterialSupplyRelDao.findByMaterialRelId(materialRelId);  
	}
	public void addNewVendorMaterialSupplyRel(VendorMaterialSupplyRelEntity vendorMaterialSupplyRel) {
		vendorMaterialSupplyRelDao.save(vendorMaterialSupplyRel);
	}

	public VendorMaterialSupplyRelEntity getVendorMaterialSupplyRel(Long id) {
		return vendorMaterialSupplyRelDao.findOne(id);
	}

	public void updateVendorMaterialSupplyRel(VendorMaterialSupplyRelEntity vendorMaterialSupplyRel) {
		vendorMaterialSupplyRelDao.save(vendorMaterialSupplyRel);
	}

	public void deleteVendorMaterialSupplyRelList(List<VendorMaterialSupplyRelEntity> vendorMaterialSupplyRelList) {
		vendorMaterialSupplyRelDao.delete(vendorMaterialSupplyRelList);
		
	}

	public Map<String, Object> combineMaterialSupple(
			List<VendorMaterialSupplyRelTransfer> list, ILogger logger,Long materialRelId) {
		List<VendorMaterialSupplyRelEntity> msrEntity = new ArrayList<VendorMaterialSupplyRelEntity>();
		VendorMaterialRelEntity materialRel = vendorMaterialRelDao.findOne(materialRelId);
		Map<String, Object> map = new HashMap<String, Object>();
		//原有系数总和
		List<VendorMaterialSupplyRelEntity> supplyRelEntities = vendorMaterialSupplyRelDao.findByMaterialRelId(materialRelId);
		Double total = 0d;
		for (int i = 0; i < supplyRelEntities.size(); i++) {
			total += supplyRelEntities.get(i).getSupplyCoefficient();
		}
		//原系数与导入数据中的系数总和
		for (VendorMaterialSupplyRelTransfer trans:list) {
			total += Double.parseDouble(trans.getSupplyCoefficient());
		}
		
		//大于1或小于0的
		if (total>1||total<0) {
			map.put("success", true);
			map.put("msg", "导入失败，供货系数总和需大于0且不大于1");
			return map;
		}
		
		int succCount = 0;
		int failCount = 0;
		for (VendorMaterialSupplyRelTransfer trans:list) {
			VendorMaterialSupplyRelEntity entity = new VendorMaterialSupplyRelEntity();
			List<BussinessRangeEntity> range = bussinessRangeDao.findByCodeAndBussinessType(trans.getBussinessRangeCode(),TypeConstant.BUSSINESSRANGE_TYPE_0);
			List<BussinessRangeEntity> buss = bussinessRangeDao.findByCodeAndBussinessType(trans.getBussinessCode(),TypeConstant.BUSSINESSRANGE_TYPE_1);
			List<BussinessRangeEntity> brand = bussinessRangeDao.findByCodeAndBussinessType(trans.getBrandCode(),TypeConstant.BUSSINESSRANGE_TYPE_2);
			List<BussinessRangeEntity> product = bussinessRangeDao.findByCodeAndBussinessType(trans.getProductLineCode(),TypeConstant.BUSSINESSRANGE_TYPE_3);
			
			entity.setMaterialRelId(materialRelId);
			if(range!=null&&range.size()>0){
				entity.setBussinessRangeId(range.get(0).getId());
				entity.setBussinessRangeName(range.get(0).getName());
			}else {
				failCount++;
				continue;
			}
			if(buss!=null&&buss.size()>0){
				entity.setBussinessId(buss.get(0).getId());
				entity.setBussinessName(buss.get(0).getName());
			}else {
				failCount++;
				continue;
			}
			if(brand!=null&&brand.size()>0){
				entity.setBrandId(brand.get(0).getId());
				entity.setBrandName(brand.get(0).getName());
			}else {
				failCount++;
				continue;
			}
			if(product!=null&&product.size()>0){
				entity.setProductLineId(product.get(0).getId());
				entity.setProductLineName(product.get(0).getName());
			}else {
				failCount++;
				continue;
			}
			FactoryEntity fac = factoryDao.findByCode(trans.getFactoryCode());
			if(fac!=null){
				entity.setFactoryId(fac.getId());
				entity.setFactoryName(fac.getName());
			}else {
				failCount++;
				continue;
			}
			if("是".equals(trans.getIsmain())){
				entity.setIsmain(TypeConstant.BUSSINESSRANGE_TYPE_1);
			}else{
				entity.setIsmain(TypeConstant.BUSSINESSRANGE_TYPE_0);
			}
			succCount++;
			entity.setSupplyCoefficient(Double.parseDouble(trans.getSupplyCoefficient()));
			entity.setOrgId(materialRel.getOrgId());
			entity.setVendorId(materialRel.getVendorId());
			entity.setMaterialId(materialRel.getMaterialId());
			entity.setVendorName(materialRel.getVendorName());
			entity.setMaterialName(materialRel.getMaterialName());
			entity.setEffectiveTime(Timestamp.valueOf(trans.getEffectiveTime()));
			msrEntity.add(entity);
		}
		vendorMaterialSupplyRelDao.save(msrEntity);
		map.put("success", true);
		map.put("msg", "成功导入"+succCount+"条，"+failCount+"条错误数据被忽略！");
		return map;
	}

	/**
	 * 供货系数导出
	 * @param materialRelId 供货关系ID
	 * @return 导出模板Transfer
	 */
	public List<VendorMaterialSupplyRelTransfer> getVendorMaterialSupplyRelVo(Long materialRelId) {
		List<VendorMaterialSupplyRelEntity> list = getVendorMaterialSupplyRelByMaterialRelId(materialRelId);
		if(CollectionUtils.isEmpty(list))
			return null;
		
		List<VendorMaterialSupplyRelTransfer> ret = new ArrayList<VendorMaterialSupplyRelTransfer>();
		VendorMaterialSupplyRelTransfer trans = null;
		for(VendorMaterialSupplyRelEntity entity : list) {
			trans = new VendorMaterialSupplyRelTransfer();
			if(entity.getMaterialEntity()!=null){
				trans.setMaterialName(entity.getMaterialEntity().getName());
				trans.setMaterialCode(entity.getMaterialEntity().getCode());
			}
			trans.setBrandName(entity.getBrandName());
			if(entity.getBussinessBrand()!=null){
				trans.setBrandCode(entity.getBussinessBrand().getCode());
			}
			trans.setBussinessName(entity.getBussinessName());
			if(entity.getBussinessType()!=null){
				trans.setBussinessCode(entity.getBussinessType().getCode());
			}
			trans.setBussinessRangeName(entity.getBussinessRangeName());
			if(entity.getBussinessRange()!=null){
				trans.setBussinessRangeCode(entity.getBussinessRange().getCode());
			}
			trans.setFactoryName(entity.getFactoryName());
			if(entity.getFactory()!=null){
				
				trans.setFactoryCode(entity.getFactory().getCode());
			}
			trans.setIsmain(entity.getIsmain()+"");
			trans.setProductLineName(entity.getProductLineName());
			if(entity.getProductLine()!=null){
				
				trans.setProductLineCode(entity.getProductLine().getCode());
			}
			trans.setSupplyCoefficient(entity.getSupplyCoefficient()+"");
			ret.add(trans);
		}
		return ret;
	}
	
	
	
	/**
	 * 供应商品牌重合度统计导出
	 * @return 导出模板Transfer
	 */
	public List<StatisticsVendorBrandOverlapTransfer> getStatisticsVendorBrandOverlapVo(Map<String, Object> searchParamMap) {
		
		List<VendorMaterialSupplyRelEntity> list = getVendorMaterialSupplyRelAll(searchParamMap);
		Map<String,VendorMaterialSupplyRelEntity> lMap = new HashMap<String, VendorMaterialSupplyRelEntity>();
		for (VendorMaterialSupplyRelEntity vmsrEntity : list) {
			VendorMaterialSupplyRelEntity entity = lMap.get(vmsrEntity.getVendorName());
			if(entity==null){
				entity = vmsrEntity;
				entity.setAllBrand(entity.getBrandName());
				entity.setBrandCount(1);
			}else{
				entity.setAllBrand(entity.getAllBrand()+","+vmsrEntity.getBrandName());
				entity.setBrandCount(entity.getBrandCount()+1);
			}
			lMap.put(entity.getVendorName(), entity);
		}
		List<VendorMaterialSupplyRelEntity> retList = new ArrayList<VendorMaterialSupplyRelEntity>();
		for (String key : lMap.keySet()) {
			retList.add(lMap.get(key));
		}
		if(CollectionUtils.isEmpty(retList))
			return null;
		
		List<StatisticsVendorBrandOverlapTransfer> ret = new ArrayList<StatisticsVendorBrandOverlapTransfer>();
		StatisticsVendorBrandOverlapTransfer trans = null;
		for(VendorMaterialSupplyRelEntity entity : retList) {
			trans = new StatisticsVendorBrandOverlapTransfer();
			trans.setBrandName(entity.getAllBrand());
			trans.setVendorName(entity.getVendorName());
			trans.setBrandCount(entity.getBrandCount()+"");
			ret.add(trans);
		}
		return ret;
	}


	public int getBrandCountByVendor(Long vendorId) {
		return vendorMaterialSupplyRelDao.getBrandCountByVendor(vendorId);
	}

	public Double getTotalSupplyCoeFFicientByRelId(Long id) {
		return vendorMaterialSupplyRelDao.getTotalSupplyCoeFFicientByRelId(id);
	}
	
	public Page<BussinessRangeEntity> getMSRelList(
			int pageNumber, int pageSize, Long id,Map<String, Object> searchParamMap) {
		// TODO Auto-generated method stub
		List<?> list =  vendorMaterialSupplyRelDao.getOrgIdAndBrandId(id);
		List<Long> listlong=new ArrayList<Long>();
		if(list!=null&&list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				listlong.add(Long.parseLong(""+list.get(i)));
			}
		}
		else
		{
			listlong.add((long) -1);
		}
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		searchParamMap.put("IN_id", listlong);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BussinessRangeEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BussinessRangeEntity.class);
		return bussinessRangeDao.findAll(spec,pagin);
	}
}
 