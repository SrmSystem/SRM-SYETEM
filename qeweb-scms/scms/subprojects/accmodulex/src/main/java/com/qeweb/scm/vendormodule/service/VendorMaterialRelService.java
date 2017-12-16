package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.vendormodule.entity.VendorBUEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.repository.VendorBUDao;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.vo.VendorMaterialRelTransfer;
import com.qeweb.scm.vendormodule.vo.VendorMaterialRelTransfer2;
/**
 * 物料关系Service
 * @author lw
 * 创建时间：2015年6月15日09:35:51
 * 最后更新时间：2015年6月15日 11:13:44
 * 最后更新人：lw
 */
@Service(value="vendorMaterialRelService")
@Transactional
public class VendorMaterialRelService {
	
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	@Autowired
	private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;
	@Autowired
	private VendorBUDao vendorBUDao;
	
	@Autowired
	private VendorPhaseService vendorPhaseService;



	public VendorMaterialRelEntity getVendorMaterialRel(Long id) {
		return vendorMaterialRelDao.findOne(id);
	}
	
	public List<VendorMaterialRelEntity> getVendorMaterialRelByFactoryCodeAndMaterialCode(String materialCode,String factoryCode,Integer Abolished) {
		return vendorMaterialRelDao.findByMaterialCodeAndFactoryCodeAndAbolished(materialCode,factoryCode,Abolished);
	}

	public void updateVendorMaterialRel(VendorMaterialRelEntity vendorMaterialRel) {
		vendorMaterialRelDao.save(vendorMaterialRel);
	}

	public void deleteVendorMaterialRelList(List<VendorMaterialRelEntity> vendorMaterialRelList) {
		vendorMaterialRelDao.delete(vendorMaterialRelList);
		
	}

	public List<VendorMaterialRelTransfer2> getVendorMaterialRelTransfer(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialRelEntity.class);
		List<VendorMaterialRelEntity> list = vendorMaterialRelDao.findAll(spec);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		
		List<VendorMaterialRelTransfer2> ret = new ArrayList<VendorMaterialRelTransfer2>();
		VendorMaterialRelTransfer2 trans = null;
		for(VendorMaterialRelEntity entity : list) {
			trans = new VendorMaterialRelTransfer2();
			trans.setVendorCode(entity.getVendor().getCode());
			trans.setVendorName(entity.getVendor().getShortName());
			trans.setPartsCode(entity.getMaterial().getPartsCode());
			trans.setPartsName(entity.getMaterial().getPartsName());
			if(entity.getVendor().getMainBU()!=null)
			{
				VendorBUEntity vbu=vendorBUDao.findByCodes(entity.getVendor().getMainBU()+"");
				if(vbu!=null)
				{
				trans.setBuCode(entity.getVendor().getMainBU()+"");
				trans.setBuCode(vbu.getName());
				}
			}
			List<VendorMaterialSupplyRelEntity> list2 = vendorMaterialSupplyRelDao.findByMaterialRelId(entity.getId());
			
			if(list2!=null&&list2.size()>0)
			{
				for(VendorMaterialSupplyRelEntity v:list2)
				{
					trans.setSupplyCoefficient(v.getSupplyCoefficient()+"");
					if(v.getIsmain()==1)
					{
						trans.setIsmain("Y");
					}
					else
					{
						trans.setIsmain("N");
					}
					if(v.getBussinessBrand()!=null) {
						trans.setBrandCode(v.getBussinessBrand().getCode()+"");
					}
					trans.setBrandName(v.getBrandName());
					trans.setCreateTime(v.getCreateTime()+"");
					if(v.getProductLine()!=null) {
						trans.setProductLineCode(v.getProductLine().getCode()+"");
					}
					trans.setProductLineName(v.getProductLineName());
					ret.add(trans);
					trans = new VendorMaterialRelTransfer2();
					if(entity.getVendor()!=null){
						trans.setVendorCode(entity.getVendor().getCode());
						trans.setVendorName(entity.getVendor().getShortName());
					}
					if(entity.getMaterial()!=null){
						trans.setPartsCode(entity.getMaterial().getPartsCode());
						trans.setPartsName(entity.getMaterial().getPartsName());
					}
					if(entity.getVendor().getMainBU()!=null)
					{
						VendorBUEntity vbu=vendorBUDao.findByCodes(entity.getVendor().getMainBU()+"");
						if(vbu!=null)
						{
						trans.setBuCode(entity.getVendor().getMainBU()+"");
						trans.setBuCode(vbu.getName());
						}
					}
				}
			}
			else
			{
				ret.add(trans);
			}
		}
		return ret;
	}


	
	public Page<VendorMaterialRelEntity> getVendorMaterialRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialRelEntity.class);
		return vendorMaterialRelDao.findAll(spec,pagin);
	}
	
	public Map<String,Object> combineMaterialSupple(List<VendorMaterialRelTransfer> list, ILogger logger) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorMaterialRelEntity> mrEntity = new ArrayList<VendorMaterialRelEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Map<String, VendorBaseInfoEntity> vendorBaseMap = new HashMap<String, VendorBaseInfoEntity>();
		VendorPhaseEntity phase = vendorPhaseService.getVendorPhaseByCode(Constant.VENDOR_PHASE_OFFICIAL);
		boolean flag = true;
		List<MaterialEntity> matList = null;
		VendorBaseInfoEntity ven = null;
		MaterialEntity material = null;
		VendorMaterialRelEntity entity = null;
		int index = 0;
		int succCount = 0;
		int failCount = 0;
		Map<String,Object> yanz = new HashMap<String, Object>();
		for (VendorMaterialRelTransfer trans:list) {
			index ++ ;
			int statr=yanz.size();
			yanz.put(trans.getVendorCode()+","+trans.getMaterialCode(),trans);
			int end=yanz.size();
			if(statr==end){
				logger.log("->[FAILED]行索引[" + index + "],导入数据中，有相同的供应商代码["+ trans.getVendorCode() +"],物料代码[" + trans.getMaterialCode() + "]");
				flag = false;
				failCount++;
				continue;
			}
			if(vendorBaseMap.containsKey(trans.getVendorCode())) {
				ven = vendorBaseMap.get(trans.getVendorCode());
			} else {
				ven = vendorBaseInfoDao.findByCodeAndCurrentVersion(trans.getVendorCode(),StatusConstant.STATUS_YES);
				if(ven == null) {
					logger.log("->[FAILED]行索引[" + index + "],供应商代码[" + trans.getVendorCode() + "]未在系统中维护,忽略此明细");
					flag = false;
					failCount++;
					continue;
				} else {
					if(ven.getPhaseId()!=phase.getId()){
						failCount++;
						logger.log("->[FAILED]行索引[" + index + "],供应商代码[" + trans.getVendorCode() + "]非体系内供应商,忽略此明细");
						continue;
					}else{
						vendorBaseMap.put(trans.getVendorCode(), ven);
					}
				}
			}
				
			if(materialMap.containsKey(trans.getMaterialCode())) {
				material = materialMap.get(trans.getMaterialCode());
			} else {
				matList = materialDao.findByCode(trans.getMaterialCode());
				if(CollectionUtils.isEmpty(matList)) {
					logger.log("->[FAILED]行索引[" + index + "],物料代码[" + trans.getMaterialCode() + "]未在系统中维护,忽略此明细");
					flag = false;
					failCount++;
					continue;
				} else {
					material = matList.get(0);
					materialMap.put(trans.getMaterialCode(), material); 
				}
			}
			if(vendorMaterialRelDao.findByVendorIdAndMaterialId(ven.getId(), material.getId()) != null){
				logger.log("->[FAILED]行索引[" + index + "],供应商代码["+ trans.getVendorCode() +"],物料代码[" + trans.getMaterialCode() + "]系统中已存在,忽略此明细");
				flag = false;
				failCount++;
				continue;
			}
			entity = new VendorMaterialRelEntity();
			entity.setVendorId(ven.getId());
			entity.setOrgId(ven.getOrgId());
			entity.setMaterialId(material.getId());
		/*	entity.setCarModel(trans.getCarModel());*/
			entity.setDataFrom(trans.getDataFrom());
			entity.setStatus(trans.getStatus().equals("是")?1:0);
			entity.setMaterialName(trans.getMaterialName());
			entity.setVendorName(trans.getVendorName());
			mrEntity.add(entity);
			succCount++;
		}
		if(flag){
			vendorMaterialRelDao.save(mrEntity);
			map.put("success", true);
			map.put("msg", "成功导入"+succCount+"条！");
		}else{
			map.put("success", false);
			map.put("msg", "导入数据中"+failCount+"条数据有问题，请查看日志文件！");
		}
		return map;
	}
	
	public Map<String,Object> addNewVendorMaterialRel(List<VendorMaterialRelEntity> vendorMaterialRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorMaterialRelEntity> list = new ArrayList<VendorMaterialRelEntity>();
		List<Long> vendorList = new ArrayList<Long>();
		for(VendorMaterialRelEntity rel : vendorMaterialRelList){
			VendorMaterialRelEntity exsit = vendorMaterialRelDao.findByBuyerIdAndOrgIdAndMaterialId(rel.getBuyerId(),rel.getOrgId(),rel.getMaterialId());
			if(exsit!=null){
				BeanUtil.copyPropertyNotNull(rel, exsit,"id");
				exsit.setCreateTime(DateUtil.getCurrentTimestamp());
				list.add(exsit);continue;
			}
			list.add(rel);
			vendorList.add(rel.getVendorId());
		}
		vendorMaterialRelDao.save(list);
		map.put("success", true);
		return map;
	}
}
