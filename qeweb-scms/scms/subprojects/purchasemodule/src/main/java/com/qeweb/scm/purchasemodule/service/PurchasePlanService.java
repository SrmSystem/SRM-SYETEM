package com.qeweb.scm.purchasemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVenodrItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorItemDao;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;



@Service
@Transactional
public class PurchasePlanService {

	@Autowired
	private PurchasePlanDao purchasePlanDao;
	
	@Autowired
	private PurchasePlanItemDao purchasePlanItemDao;
	
	@Autowired
	private PurchasePlanVendorDao purchasePlanVendorDao;
	
	
	@Autowired
	private PurchasePlanVendorItemDao purchasePlanVendorItemDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	public Page<PurchasePlanEntity> getPurchasePlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanEntity.class);
		return purchasePlanDao.findAll(spec,pagin);
	}
	
	public Page<PurchasePlanVendorEntity> getPurchaseVenodrPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVendorEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVendorEntity.class);
		return purchasePlanVendorDao.findAll(spec,pagin);
	}
	
	public List<PurchasePlanVenodrItemEntity> getPurchasePlanVendorItems(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVenodrItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVenodrItemEntity.class);
		return purchasePlanVendorItemDao.findAll(spec);  
	}
	
	public Page<PurchasePlanVenodrItemEntity> getPurchasePlanVendorItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVenodrItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVenodrItemEntity.class);
		return purchasePlanVendorItemDao.findAll(spec,pagin);
	}
	
	public List<PurchasePlanTransfer> getPurchasePlanVo(Map<String, Object> searchParamMap) {
		List<PurchasePlanVenodrItemEntity> list = getPurchasePlanVendorItems(searchParamMap);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		
		List<PurchasePlanTransfer> ret = new ArrayList<PurchasePlanTransfer>();
		PurchasePlanTransfer trans = null;
		for(PurchasePlanVenodrItemEntity item : list) {
			trans = new PurchasePlanTransfer();
			trans.setMonth(item.getPlan().getMonth());
			trans.setBuyerCode(item.getPlan().getBuyer().getCode());
			trans.setBuyerName(item.getPlan().getBuyer().getName());
			trans.setVendorCode(item.getVendorPlan().getVendor().getCode());
			trans.setVendorName(item.getVendorPlan().getVendor().getName());
			trans.setMaterialCode(item.getMaterial().getCode());
			trans.setMaterialName(item.getMaterial().getName());
			trans.setItemNo(item.getItemNo() + "");
			trans.setTotalPlanQty(item.getPlanQty() + "");
			trans.setPlanRecTime(DateUtil.dateToString(item.getPlanRecTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			ret.add(trans);
		}
		return ret;
	}
	
	public PurchasePlanVendorEntity getVendorPlan(Long id) {
		return purchasePlanVendorDao.findOne(id);
	}

	public PurchasePlanEntity getPlan(Long id) {
		return purchasePlanDao.findOne(id);
	}
	
	/**
	 * 修改采购计划
	 * @param purchasePlan
	 * @param purchasePlanItem
	 * @param userEntity
	 */
	public void savePurchasePlan(PurchasePlanEntity purchasePlan, List<PurchasePlanVenodrItemEntity> purchasePlanItem, UserEntity userEntity) throws Exception {
		PurchasePlanVenodrItemEntity itemEntity = null;
		List<PurchasePlanVenodrItemEntity> list = new ArrayList<PurchasePlanVenodrItemEntity>();
		//key: vendor + material
		Map<String, List<PurchasePlanVenodrItemEntity>> vendorPlanMap = new HashMap<String, List<PurchasePlanVenodrItemEntity>>();
		String key = null;
		for(PurchasePlanVenodrItemEntity item : purchasePlanItem) {
			itemEntity = purchasePlanVendorItemDao.findOne(item.getId());
			key = itemEntity.getVendorPlan().getVendor().getId() + "," + itemEntity.getMaterial().getId();
			itemEntity.setPlanQty(item.getPlanQty());
			list.add(itemEntity);
			if(vendorPlanMap.containsKey(key)) {
				vendorPlanMap.get(key).add(itemEntity);
			} else {
				List<PurchasePlanVenodrItemEntity> vendorPlanItem = new ArrayList<PurchasePlanVenodrItemEntity>();
				vendorPlanItem.add(itemEntity);
				vendorPlanMap.put(key, vendorPlanItem);
			}
		}
		purchasePlanVendorItemDao.save(list);
		PurchasePlanEntity planEntity = getPlan(purchasePlan.getId());
		planEntity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
		planEntity.setPublishTime(null); 
		Set<PurchasePlanItemEntity> planItem = planEntity.getPlanItem();
		for(PurchasePlanItemEntity item : planItem) {
			item.setTotalPlanQty(calcTotal(vendorPlanMap.get(item.getVendor().getId() + "," + item.getMaterial().getId())));
		}
		purchasePlanItemDao.save(planItem);
		//更新供应商计划状态
		Set<PurchasePlanVendorEntity> vendorPlanItem = planEntity.getVendorPlanItem();
		for(PurchasePlanVendorEntity vendorPlan : vendorPlanItem) {
			vendorPlan.setSeeStatus(PurchaseConstans.SEE_STATUS_NO);
			vendorPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
		}
		purchasePlanVendorDao.save(vendorPlanItem);
	}
	
	private Double calcTotal(List<PurchasePlanVenodrItemEntity> list) {
		if(CollectionUtils.isEmpty(list)) {
			return 0d;
		}
		
		double sum = 0d;
		for(PurchasePlanVenodrItemEntity item : list) {
			sum = BigDecimalUtil.add(sum, item.getPlanQty() == null ? 0d : item.getPlanQty());
		}
		
		return sum;
	}

	/**
	 * 发布才计划
	 * @param plans
	 */
	public void publishPlans(List<PurchasePlanEntity> plans) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanEntity plan : plans) {
			plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			plan.setPublishTime(DateUtil.getCurrentTimestamp());
			plan.setPublishUser(new UserEntity(user.id));
		}
		
		purchasePlanDao.save(plans); 
	}
	
	/**
	 * 供应商确认采购计划
	 * @param plans
	 */
	public void confirmPlans(List<PurchasePlanVendorEntity> plans) {
		for(PurchasePlanVendorEntity plan : plans) {
			plan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
		}
		
		purchasePlanVendorDao.save(plans);
	}
	
	/**
	 * 转换并保存
	 * @param planTranList
	 * @param logger
	 * @throws Exception
	 */
	public boolean combinePurchasePlan(List<PurchasePlanTransfer> planTranList, ILogger logger) throws Exception {
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchasePlanTransfer> list = validateTransfers(planTranList, orgMap, materialMap, counts, logger);
		if(list.size() != planTranList.size()) {
			return false;
		}
		
		logger.log("\n->正在准备合并主数据与明细数据\n");
		PurchasePlanEntity purchasePlan = null;
		PurchasePlanItemEntity purchasePlanItem = null;
		PurchasePlanVendorEntity purchasePlanVendor = null;
		PurchasePlanVenodrItemEntity purchasePlanVendorItem = null;
		//供应商计划
		List<PurchasePlanItemEntity> purchasePlanItemList = new ArrayList<PurchasePlanItemEntity>();
		List<PurchasePlanVenodrItemEntity> purchasePlanVendorItemList = new ArrayList<PurchasePlanVenodrItemEntity>();
		Map<String, PurchasePlanEntity> tmpMap = new HashMap<String, PurchasePlanEntity>();
		Map<String, PurchasePlanVendorEntity> tmpvenMap = new HashMap<String, PurchasePlanVendorEntity>();
		String key = null;
		for(PurchasePlanTransfer trans : list) {
			key = trans.getMonth() + ";" + trans.getBuyerCode();
			if(!tmpMap.containsKey(key)) {
				purchasePlan = getPurchasePlanEntity(trans.getMonth(), trans.getBuyerCode());
				purchasePlan.setMonth(trans.getMonth());
				purchasePlan.setBuyer(orgMap.get(trans.getBuyerCode()));
				purchasePlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				tmpMap.put(key, purchasePlan);
			} else {
				purchasePlan = tmpMap.get(key);
			}
			//设置明细单表信息
			purchasePlanItem = getPurchasePlanItemEntity(purchasePlan, trans.getItemNo());
			purchasePlanItem.setPlan(purchasePlan);
			purchasePlanItem.setVendor(orgMap.get(trans.getVendorCode()));
			purchasePlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));
			purchasePlanItem.setItemNo(StringUtils.convertToInteger(trans.getItemNo()));
			purchasePlanItem.setTotalPlanQty(StringUtils.convertToDouble(trans.getTotalPlanQty())); 
			purchasePlanItem.setPlanRecTime(DateUtil.stringToTimestamp(trans.getPlanRecTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)); 
			purchasePlanItemList.add(purchasePlanItem);
			
			key = key + ";" + trans.getVendorCode();
			if(!tmpvenMap.containsKey(key)) {
				purchasePlanVendor = getPurchasePlanVendorEntity(purchasePlan, trans.getVendorCode());
				purchasePlanVendor.setPlan(purchasePlan);
				purchasePlanVendor.setVendor(purchasePlanItem.getVendor());
				purchasePlanVendor.setSeeStatus(PurchaseConstans.SEE_STATUS_NO);
				purchasePlanVendor.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				tmpvenMap.put(key, purchasePlanVendor);
			} else {
				purchasePlanVendor = tmpvenMap.get(key);
			}
			//设置供应商明细
			purchasePlanVendorItem = getPurchasePlanVenodrItemEntity(purchasePlanVendor, trans.getMaterialCode(), trans.getItemNo());
			purchasePlanVendorItem.setPlan(purchasePlan);
			purchasePlanVendorItem.setVendorPlan(purchasePlanVendor);
			purchasePlanVendorItem.setItemNo(purchasePlanItem.getItemNo());
			purchasePlanVendorItem.setMaterial(purchasePlanItem.getMaterial());
			purchasePlanVendorItem.setPlanRecTime(purchasePlanItem.getPlanRecTime());
			purchasePlanVendorItem.setPlanQty(purchasePlanItem.getTotalPlanQty());
			purchasePlanVendorItemList.add(purchasePlanVendorItem);
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		//保存
		purchasePlanDao.save(tmpMap.values());
		purchasePlanItemDao.save(purchasePlanItemList);    
		purchasePlanVendorDao.save(tmpvenMap.values());
		purchasePlanVendorItemDao.save(purchasePlanVendorItemList);
		return true;
	}
	
	private List<PurchasePlanTransfer> validateTransfers(List<PurchasePlanTransfer> planTranList, Map<String, OrganizationEntity> orgMap, Map<String, MaterialEntity> materialMap, Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入的采购计划信息进行预处理,共有[" + (planTranList == null ? 0 : planTranList.size()) + "]条数据";
		logger.log(logMsg);
		counts[0] = planTranList.size();
		List<PurchasePlanTransfer> ret = new ArrayList<PurchasePlanTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<MaterialEntity> materialList = null;
		boolean lineValidat = true;
		int index  = 2; 
		for(PurchasePlanTransfer trans : planTranList) {
			if(!orgMap.containsKey(trans.getBuyerCode())) {
				orgList = organizationDao.findByCode(trans.getBuyerCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + index + "],采方代码[" + trans.getBuyerCode() + "]未在系统中维护,忽略此采购计划");
				} else {
					orgMap.put(trans.getBuyerCode(), orgList.get(0));
				}
			}
			
			if(!orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCode(trans.getVendorCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + index + "],供方代码[" + trans.getVendorCode() + "]未在系统中维护,忽略此采购计划");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			if(!materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findByCode(trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + index + "],物料(" + trans.getMaterialCode() + "], 不存在,忽略此采购计划");
				} else {
					materialMap.put(trans.getMaterialCode(), materialList.get(0));
				}
			}
			if(lineValidat) {
				logMsg = "[SUCCESS]行索引[" + index + "],预处理[" + trans.getVendorCode() + "|" + trans.getMaterialCode() + "|" + trans.getPlanRecTime() + "]成功.";
				logger.log(logMsg);
				ret.add(trans);
			}
			index ++;
			lineValidat = true;
		}
		counts[1] = ret.size();
		logMsg = "<-导入的采购计划主信息预处理完毕,共有[" + ret.size() + "]条有效数据";
		logger.log(logMsg);
		return ret;   
	}

	/**
	 * @param month
	 * @param buyerCode
	 * @return
	 */
	protected PurchasePlanEntity getPurchasePlanEntity(String month, String buyerCode) {  
		PurchasePlanEntity plan = purchasePlanDao.findPurchasePlanEntityByMonthAndBuyer(month, buyerCode);
		if(plan == null) {
			return new PurchasePlanEntity();
		}
		
		return plan;
	}
	
	protected PurchasePlanItemEntity getPurchasePlanItemEntity(PurchasePlanEntity purchasePlan, String itemNo) {
		if(purchasePlan.getId() == 0l) {
			return new PurchasePlanItemEntity();
		}
		
		PurchasePlanItemEntity planItem = purchasePlanItemDao.findPurchasePlanItemEntityByMainId(purchasePlan.getId(), StringUtils.convertToInteger(itemNo));
		return planItem == null ? new PurchasePlanItemEntity() : planItem;
	}
	
	protected PurchasePlanVendorEntity getPurchasePlanVendorEntity(PurchasePlanEntity purchasePlan, String vendorCode) {
		if(purchasePlan.getId() == 0l) {
			return new PurchasePlanVendorEntity();
		}
		
		PurchasePlanVendorEntity planVendor = purchasePlanVendorDao.findPurchasePlanVendorEntityByMain(purchasePlan.getId(), vendorCode);
		return planVendor == null ? new PurchasePlanVendorEntity() : planVendor;
	}
	
	protected PurchasePlanVenodrItemEntity getPurchasePlanVenodrItemEntity(PurchasePlanVendorEntity planVendor, String materialCode, String itemNo) {
		if(planVendor.getId() == 0l) {
			return new PurchasePlanVenodrItemEntity();
		}
		
		PurchasePlanVenodrItemEntity planVendorItem = purchasePlanVendorItemDao.findPurchasePlanVenodrItemEntityByVendorPlan(planVendor.getId(), materialCode, StringUtils.convertToInteger(itemNo));
		return planVendorItem == null ? new PurchasePlanVenodrItemEntity() : planVendorItem;
	}

}
