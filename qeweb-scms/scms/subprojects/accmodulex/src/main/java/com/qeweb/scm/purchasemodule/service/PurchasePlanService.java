package com.qeweb.scm.purchasemodule.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.entity.WarnMessageEntity;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.UserServiceImpl;

import java.math.BigDecimal;

import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanHeadEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityInfoEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityRelEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVenodrItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanHeadEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanHeadDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorCapacityInfoDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanVendorItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanHeadDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanItemDao;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.AddCapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.CapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanDeleteTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseTotalPlanTransfer;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;

 /**
  * 采购计划service
  */
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
	
	@Autowired
	private FactoryDao factoryDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	@Autowired
	private OrganizationDao OrganizationDao;
	  
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private PurchaseTotalPlanItemDao purchaseTotalPlanItemDao;
	
	@Autowired
	private PurchaseTotalPlanHeadDao purchaseTotalPlanHeadDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private PurchasePlanHeadDao purchasePlanHeadDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private DictItemService dictItemService;
	
	@Autowired
	private PurchasePlanVendorCapacityRelService purchasePlanVendorCapacityRelService;
	
	@Autowired
	private PurchasePlanVendorCapacityInfoDao purchasePlanVendorCapacityInfoDao;
	
	@Autowired
	private  VendorMaterialRelDao vendorMaterialRelDao;
	
	@Autowired
	private  WarnMainService warnMainService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private PurchaseTotalPlanDao purchaseTotalPlanDao;
	
	public Page<PurchasePlanEntity> getPurchasePlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanEntity.class);
		Page<PurchasePlanEntity> page=  purchasePlanDao.findAll(spec,pagin);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//修改发布状态(采购商)
		for(PurchasePlanEntity  entity  : page.getContent()){
			  commonOrderPublishStatus(entity, DateUtil.getCurrentTimestamp() ,new UserEntity(user.id));
			  //修改产能表状态
			  commonBuyerPlanUploadStatus(entity);
		}		
		return page;
	}
	
	public Page<PurchasePlanVendorEntity> getPurchaseVenodrPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Timestamp curr=DateUtil.getCurrentTimestamp();
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVendorEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVendorEntity.class);
		Page<PurchasePlanVendorEntity> page = purchasePlanVendorDao.findAll(spec,pagin);
		for(PurchasePlanVendorEntity item : page.getContent() ){
	
			List<PurchasePlanItemEntity> list = purchasePlanItemDao.findPurchasePlanItemEntityByVendorAndPlanId(user.orgId, item.getPlan().getId());
			//修改确认状态
			commonVendorConfirStatus(list ,item);
			
			//修改上传状态
			commonVendorUploadStatus(list ,item);
			
		}
		return page;
	}
	//供应商确认状态（主表）
	public void commonVendorConfirStatus(List<PurchasePlanItemEntity> list,PurchasePlanVendorEntity entity){
		int count_all=0;
		int count_part=0;

		for (PurchasePlanItemEntity planItem : list) {
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==planItem.getConfirmStatus().intValue()){
				count_all++;
			}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==planItem.getConfirmStatus().intValue()){
				count_part++;
			}
		}
		if(count_all>0 && count_all==list.size()){
			entity.setConfirmStatus(PurchaseConstans.PUBLISH_STATUS_YES);
		}else if(count_part >0 || count_all>0){
			entity.setConfirmStatus(PurchaseConstans.PUBLISH_STATUS_PART);
		}else{
			entity.setConfirmStatus(PurchaseConstans.PUBLISH_STATUS_NO);
		}
	}

	//供应商上传状态（主表）
	public void commonVendorUploadStatus(List<PurchasePlanItemEntity> list,PurchasePlanVendorEntity entity){
		int count_all=0;
		int count_part=0;
		
		for (PurchasePlanItemEntity planItem : list) {
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==planItem.getUploadStatus().intValue()){
				count_all++;
			}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==planItem.getUploadStatus().intValue()){
				count_part++;
			}
		}
		if(count_all>0 && count_all==list.size()){
			entity.setUploadStatus(PurchaseConstans.PUBLISH_STATUS_YES);
		}else if(count_part >0 || count_all>0){
			entity.setUploadStatus(PurchaseConstans.PUBLISH_STATUS_PART);
		}else{
			entity.setUploadStatus(PurchaseConstans.PUBLISH_STATUS_NO);
		}
	}
	
	
	public List<PurchasePlanVenodrItemEntity> getPurchasePlanVendorItems(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVenodrItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVenodrItemEntity.class);
		return purchasePlanVendorItemDao.findAll(spec);  
	}
	
	public Page<PurchasePlanVendorCapacityInfoEntity> getCapacityInfo(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVendorCapacityInfoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVendorCapacityInfoEntity.class);
		return purchasePlanVendorCapacityInfoDao.findAll(spec,pagin);
	}
	
	
	public Page<PurchasePlanVenodrItemEntity> getPurchasePlanVendorItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanVenodrItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanVenodrItemEntity.class);
		return purchasePlanVendorItemDao.findAll(spec,pagin);
	}
	
	protected PurchasePlanEntity getPurchasePlanEntity(String month, Long id) {  
		PurchasePlanEntity plan = purchasePlanDao.findPurchasePlanEntityByMonthAndCreateUserId(month, id);
		if(plan == null) {
			return new PurchasePlanEntity();
		}
		return plan;
	}
	
	protected PurchasePlanItemEntity getPurchasePlanItemEntity(PurchasePlanEntity purchasePlan, PurchasePlanTransfer trans) {
		if(purchasePlan.getId() == 0l) {
			return new PurchasePlanItemEntity();                                                                                                
		}
		PurchasePlanItemEntity planItem = purchasePlanItemDao.findPurchasePlanItemEntityByKeys(purchasePlan.getId(),trans.getGroupCode() ,trans.getFactoryCode(),trans.getMaterialCode(),trans.getVendorCode());
		return planItem == null ? new PurchasePlanItemEntity() : planItem;
	}
	
	protected PurchasePlanVendorEntity getPurchasePlanVendorEntity(String month, PurchasePlanTransfer trans) {
		PurchasePlanVendorEntity planVendor = purchasePlanVendorDao.findPurchasePlanVendorEntityByMonthAndVendor(month, trans.getVendorCode());
		return planVendor == null ? new PurchasePlanVendorEntity() : planVendor;
	}
	
	//获取产能表填写的信息
	public PurchasePlanVendorCapacityInfoEntity getCapacityInfoByPlanItem(Long id){
		return purchasePlanVendorCapacityInfoDao.findByPlanItemId(id);
	}
	
	
	/**
	 * 根据供应商的id获取设置的产能表的必填信息
	 * @param orgid
	 * @return
	 */
	public List<CapacityVO> showCapacityInfo(Long orgId) {
		
		//获取设置的产能表
		PurchasePlanVendorCapacityRelEntity purchasePlanVendorCapacityRel = purchasePlanVendorCapacityRelService.getPurchasePlanVendorCapacityRelByVendorId(orgId);
		
		List<CapacityVO> cvList = new ArrayList<CapacityVO>();
		CapacityVO cv = null;
		//格式化数据
		Map<String,String> codeMap = new HashMap<String, String>();
		
		//获取产能配置信息
		List<DictItemEntity> diList =  dictItemService.findListByDictCode("FORECAST_CAPACITY");
		
		//等于空时添加所有的产能表信息
		if(purchasePlanVendorCapacityRel != null){
			String [] codes=purchasePlanVendorCapacityRel.getCapacityCodes().split(",");
			if(null!=codes && codes.length>0){
				for (String str : codes) {
					codeMap.put(str, str);
				}
			}
			//增加配置信息
			for(DictItemEntity dictItem : diList){
				if( codeMap.get(dictItem.getCode()) !=null){
					cv = new CapacityVO();
					cv.setCode(dictItem.getCode());
					cv.setName(dictItem.getName());
					cv.setChecked("required");
					cvList.add(cv);
				}else{
					cv = new CapacityVO();
					cv.setCode(dictItem.getCode());
					cv.setName(dictItem.getName());
					cvList.add(cv);
				}
			}
		}else{
			for(DictItemEntity dictItem : diList){
				cv = new CapacityVO();
				cv.setCode(dictItem.getCode());
				cv.setName(dictItem.getName());
				cvList.add(cv);
			}
		}	
		
		return cvList;
	}

	

	
	
	
	
	
	/**
	 * 获取采购计划明细
	 * @param id
	 * @return
	 */
	public void  addCapacityInfo(AddCapacityVO addCapacityVO) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取采购主计划下此供应商未上传的产能表子项目
		List<PurchasePlanItemEntity>  poItemPlan =purchasePlanItemDao.findPurchasePlanItemEntityByPlanidAndVendor(Long.parseLong(addCapacityVO.getPoPlanid()),user.orgId);
	
		List<PurchasePlanVendorCapacityInfoEntity> capaList = new ArrayList<PurchasePlanVendorCapacityInfoEntity>();
		PurchasePlanVendorCapacityInfoEntity  capa= null;
		
		for(PurchasePlanItemEntity item : poItemPlan){
			item = purchasePlanItemDao.findOne(item.getId());
			item.setUploadStatus(1);
			purchasePlanItemDao.save(item);
			
			//更新采购商的朱采购计划的上传的状态
			Long planId = item.getPlan().getId();
			//查询下面所有的子项目
			PurchasePlanEntity plan = purchasePlanDao.findOne(planId);
			commonBuyerPlanUploadStatus(plan);

			//保存采购计划子项目的产能信息
			capa = new PurchasePlanVendorCapacityInfoEntity();
			capa.setPlanItem(item);
			capa.setCapa1(addCapacityVO.getCapa1());
			capa.setCapa2(addCapacityVO.getCapa2());
			capa.setCapa3(addCapacityVO.getCapa3());
			capa.setCapa4(addCapacityVO.getCapa4());
			capa.setCapa5(addCapacityVO.getCapa5());
			capa.setCapa6(addCapacityVO.getCapa6());
			capa.setCapa7(addCapacityVO.getCapa7());
			capa.setCapa8(addCapacityVO.getCapa8());
			capa.setCapa9(addCapacityVO.getCapa9());
			capa.setCapa10(addCapacityVO.getCapa10());
			capa.setCapa11(addCapacityVO.getCapa11());
			capa.setCapa12(addCapacityVO.getCapa12());
			capa.setCapa13(addCapacityVO.getCapa13());
			capa.setCapa14(addCapacityVO.getCapa14());
			capa.setCapa15(addCapacityVO.getCapa15());
			capa.setCapa16(addCapacityVO.getCapa16());
			capa.setCapa17(addCapacityVO.getCapa17());
			capa.setCapa18(addCapacityVO.getCapa18());
			capa.setCapa19(addCapacityVO.getCapa19());
			capa.setCapa20(addCapacityVO.getCapa20());
			capaList.add(capa);
		}
		
		purchasePlanVendorCapacityInfoDao.save(capaList);
		
		//更新供应商采购计划主表的上传状态
		PurchasePlanVendorEntity purchasePlanVendor = purchasePlanVendorDao.findOne(Long.parseLong(addCapacityVO.getVendorPlanid()));
		purchasePlanVendor.setUploadStatus(1);
		purchasePlanVendorDao.save(purchasePlanVendor);
		
		
	}
	
	
	
	
	
	/**
	 * 获取采购计划明细
	 * @param id
	 * @return
	 */
	public PurchasePlanItemEntity getOrderItemPlan(Long id) {
		return purchasePlanItemDao.findOne(id);
	}
	
	
	/**
	 * 发布采购计划（全）
	 * @param plans
	 */
	public void publishPlans(List<PurchasePlanEntity> plans) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanEntity plan : plans) {
			//发布主预测计划
			plan = purchasePlanDao.findOne(plan.getId());
			plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			plan.setPublishTime(DateUtil.getCurrentTimestamp());
			plan.setPublishUser(new UserEntity(user.id));
			purchasePlanDao.save(plan); 
			
			//发布明细
			Set<PurchasePlanItemEntity> itemSet=plan.getPlanItem();
			if(CommonUtil.isNotNullCollection(itemSet)){
				for (PurchasePlanItemEntity itemEntity : itemSet) {
					if(itemEntity.getIsNew()==1){
						itemEntity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
						itemEntity.setPublishTime(DateUtil.getCurrentTimestamp());;
						itemEntity.setPublishUser(new UserEntity(user.id));;	
					}	
				}
				purchasePlanItemDao.save(itemSet);
			}
		}
		
		//更新供应商主单的产能表上传状态
		for(PurchasePlanEntity plan : plans) {
			plan = purchasePlanDao.findOne(plan.getId());
			commonVendorPlanUploadStatus(plan);
		}
		
		//给供应商发布消息
		for(PurchasePlanEntity plan : plans) {
			plan = purchasePlanDao.findOne(plan.getId());
			Map<Long,Long >  idMap = new HashMap<Long, Long>();
			Set<PurchasePlanItemEntity> itemSet=plan.getPlanItem();
			if(CommonUtil.isNotNullCollection(itemSet)){
				for (PurchasePlanItemEntity itemEntity : itemSet) {
					idMap.put(itemEntity.getVendor().getId(), itemEntity.getVendor().getId());
				}
			}			
			List<Long> userIdList = new ArrayList<Long>();
			for (Long keys : idMap.keySet()) {  
				//查询下面的人员
				 List<UserEntity> users = userServiceImpl.findByCompany(idMap.get(keys));
				 for(UserEntity useritem : users){
					 userIdList.add(useritem.getId());
				 }	
			}
			 warnMainService.warnMessageSet( plan.getId(), WarnConstant.ORDER_PLAN_UN_CONFIRM, userIdList);
			 //把未发布的消息删除
			 warnMainService.delMessageByBillIdAndBillType(plan.getId(), WarnConstant.ORDER_PLAN_UN_PUBLISH,user.id);
		}
		
		
	}
	
	
	/**
	 * 批量发布订单明细[采]
	 * @param orders
	 */
	public void publishOrderPlanItems(List<PurchasePlanItemEntity> orderItems) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanItemEntity item  : orderItems){
			item  = purchasePlanItemDao.findOne(item.getId());
			item.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			item.setPublishTime(curr);;
			item.setPublishUser(new UserEntity(user.id));
			purchasePlanItemDao.save(item);
		}

		//更新主表发布状态
		for(PurchasePlanItemEntity order : orderItems) {
			PurchasePlanEntity entity=purchasePlanDao.findOne(order.getPlan().getId());
			  commonOrderPublishStatus(entity, curr ,new UserEntity(user.id));
			}
		
		//更新供应商主单的产能表上传状态
		for(PurchasePlanItemEntity order : orderItems) {
			PurchasePlanEntity plan=purchasePlanDao.findOne(order.getPlan().getId());
			commonVendorPlanUploadStatus(plan);
			}
		
		
		//给供应商发布消息
		Map<Long,Long >  idMap = new HashMap<Long, Long>();
		for(PurchasePlanItemEntity planItem : orderItems) {
			idMap.put(planItem.getVendor().getId(), planItem.getVendor().getId());
		}
		List<Long> userIdList = new ArrayList<Long>();
		for (Long keys : idMap.keySet()) {  
			//查询下面的人员
			 List<UserEntity> users = userServiceImpl.findByCompany(idMap.get(keys));
			 for(UserEntity useritem : users){
				 userIdList.add(useritem.getId());
			 }	
		}
		 warnMainService.warnMessageSet( orderItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_UN_CONFIRM, userIdList);
		
		 PurchasePlanEntity plan=purchasePlanDao.findOne(orderItems.get(0).getPlan().getId());//所有的预测计划行
		 //已发布的预测计划行
		 List<PurchasePlanItemEntity> warnItemList  =purchasePlanItemDao.findUnPublishByMainId(plan.getId());
		 if( plan.getPlanItem().size() == warnItemList.size() ){//判断是否完全发布计划
			 //把未发布的消息删除
			 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_UN_PUBLISH,user.id);
		 }
		
	
	}
	
	//更新供应商的主表的上传产能表的状态
	public void commonVendorPlanUploadStatus(PurchasePlanEntity plan){
		
          if(plan != null){
  			//获取发布给供应闪的计划的子项目
            Set<PurchasePlanVendorEntity> vendorPlanItem = plan.getVendorPlanItem();
            
			List<PurchasePlanItemEntity> effectList =new  ArrayList<PurchasePlanItemEntity>(); 
			for(PurchasePlanItemEntity item  : effectList){
				if(item.getIsNew() == 1){
					effectList.add(item);
				}
			}
            
			if(effectList != null && effectList.size() >0){
	            for(PurchasePlanVendorEntity vendorTemp : vendorPlanItem){
	            	//每个供应商的预测计划头
	            	int count_all=0;
	    			int count_part=0;
	            	List<PurchasePlanItemEntity>  itemPlans = purchasePlanItemDao.findPurchasePlanItemEntityByPlanidAndVendor(plan.getId(), vendorTemp.getVendor().getId());
	            	for (PurchasePlanItemEntity planItem : itemPlans) {
	    				if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==planItem.getUploadStatus().intValue()){
	    					count_all++;
	    				}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==planItem.getUploadStatus().intValue()){
	    					count_part++;
	    				}
	    			}
	            	if(count_all>0 && count_all==itemPlans.size()){
	            		vendorTemp.setUploadStatus(1);
	    			}else if(count_part >0 || count_all>0){
	    				vendorTemp.setUploadStatus(2);
	    			}else{
	    				vendorTemp.setUploadStatus(0);
	    			}
	            	//更新供应商的预测计划头上传状态
	            	purchasePlanVendorDao.save(vendorTemp);
	            }
			}
            


          }
	}
	
	//更新采购商的主表的上传产能表的状态
	public void commonBuyerPlanUploadStatus(PurchasePlanEntity plan){
          if(plan != null){
        	int count_all=0;
  			int count_part=0;
  			//获取发布给供应闪的计划的子项目
            Set<PurchasePlanItemEntity> buyerPlanItem = plan.getPlanItem();
        	
			List<PurchasePlanItemEntity> effectList =new  ArrayList<PurchasePlanItemEntity>(); 
			for(PurchasePlanItemEntity item  : buyerPlanItem){
				if(item.getIsNew() == 1){
					effectList.add(item);
				}
			}
            
			if(effectList != null && effectList.size() >0){
            	for (PurchasePlanItemEntity planItem : effectList) {
    				if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==planItem.getUploadStatus().intValue()){
    					count_all++;
    				}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==planItem.getUploadStatus().intValue()){
    					count_part++;
    				}
    			}
            	if(count_all>0 && count_all==effectList.size()){
            		plan.setUploadStatus(1);
    			}else if(count_part >0 || count_all>0){
    				plan.setUploadStatus(2);
    			}else{
    				plan.setUploadStatus(0);
    			}
            	//更新供应商的预测计划头上传状态
            	purchasePlanDao.save(plan);
			}
            
            

            }

	}
	
	
	
	
	//获取当前大版本中有多少不同的供应商<应用于下拉框>
	public Map<String,Object > getVendorNumOnplan(PurchasePlanEntity plan){
		Map<String,Object >  vendorMap = new HashMap<String, Object>();
		 if(plan != null){
	  			Set<PurchasePlanItemEntity> orderItemSet=plan.getPlanItem();
	  			for(PurchasePlanItemEntity planItem : orderItemSet){
	  				if(!vendorMap.containsKey(planItem.getVendor().getId())) {
	  					vendorMap.put(planItem.getVendor().getCode(), planItem.getVendor().getName());
	  				} 
	  			}
	          }
		 return vendorMap;  
	}
	
	
	//更新主表的发布状态
	public void commonOrderPublishStatus(PurchasePlanEntity entity,Timestamp curTime,UserEntity user){
		if(entity!=null){
			int count_all=0;
			int count_part=0;
			Set<PurchasePlanItemEntity> orderItemSet=entity.getPlanItem();
			
			List<PurchasePlanItemEntity> effectList =new  ArrayList<PurchasePlanItemEntity>(); 
			for(PurchasePlanItemEntity item  : orderItemSet){
				if(item.getIsNew() == 1){
					effectList.add(item);
				}
			}
			
			if(effectList.size() >0 && effectList  != null){
				for (PurchasePlanItemEntity planItem : effectList) {
					if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==planItem.getPublishStatus().intValue()){
						count_all++;
					}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==planItem.getPublishStatus().intValue()){
						count_part++;
					}
				}
				if(count_all>0 && count_all==effectList.size()){
					entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
				}else if(count_part >0 || count_all>0){
					entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
				}else{
					entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				}
				
				entity.setLastUpdateTime(curTime);
				entity.setUpdateUserId(user.getId());
				entity.setUpdateUserName(user.getCreateUserName());
				purchasePlanDao.save(entity);
			}

		}
	}
	
   /***
	 * 采购商拒绝供应商的驳回采购计划明细
	 * @param plans
	 */
	public void  vetoPlans(List<PurchasePlanItemEntity> planItems,String reason) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanItemEntity planItem : planItems) {
			planItem=purchasePlanItemDao.findOne(planItem.getId());
			//采购商驳回供应商驳回条件
			planItem.setVetoStatus(0);
			planItem.setVetoReason(reason);
			planItem.setVetoUser(new UserEntity(user.id));
			planItem.setVetoTime(curr);
			planItem.setConfirmStatus(-2);
			purchasePlanItemDao.save(planItem);
		}
		//发布状态和供应商的确认状态不改变
		
		//给供应商发布消息
		Map<Long,Long >  idMap = new HashMap<Long, Long>();
		for(PurchasePlanItemEntity planItem : planItems) {
			idMap.put(planItem.getVendor().getId(), planItem.getVendor().getId());
		}
		List<Long> userIdList = new ArrayList<Long>();
		for (Long keys : idMap.keySet()) {  
			//查询下面的人员
			 List<UserEntity> users = userServiceImpl.findByCompany(idMap.get(keys));
			 for(UserEntity useritem : users){
				 userIdList.add(useritem.getId());
			 }	
		}
		 warnMainService.warnMessageSet( planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_VETO, userIdList);

		 //解除驳回待处理的信息
		 List<PurchasePlanItemEntity> warnItemList  =purchasePlanItemDao.findRejectByMainId(planItems.get(0).getId());//查询未处理的驳回记录
		 if( warnItemList.size() == 0 || warnItemList == null  ){//判断是否完全处理完驳回的记录
			 //把未发布的消息删除
			 warnMainService.delMessageByBillIdAndBillType(planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_REJECT,user.id);
		 }
		
		 
		 
		 
	}
	
   /***
	 * 采购商同意供应商的驳回采购计划明细
	 * @param plans
	 */
	public void unVetoPlans(List<PurchasePlanItemEntity> planItems,String reason) {
		List<PurchasePlanItemEntity> vendorItemPlanList = new ArrayList<PurchasePlanItemEntity>(); 
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanItemEntity planItem : planItems) {
			planItem=purchasePlanItemDao.findOne(planItem.getId());
			planItem.setVetoStatus(1);
			planItem.setVetoReason(reason);
			planItem.setVetoUser(new UserEntity(user.id));
			planItem.setVetoTime(curr);
			planItem.setConfirmStatus(0);
			planItem.setPublishStatus(0);
			planItem.setPublishTime(null);
			planItem.setPublishUser(null);
			purchasePlanItemDao.save(planItem);
			vendorItemPlanList.add(planItem);
		}
		//修改发布状态(采购商)
		for(PurchasePlanItemEntity order : planItems) {
			PurchasePlanEntity entity=purchasePlanDao.findOne(order.getPlan().getId());
			  commonOrderPublishStatus(entity, curr ,new UserEntity(user.id));
		}
		//修改供应商的主表的确认状态
		if(vendorItemPlanList.size() != 0){
			commonOrderConfirmStatus(vendorItemPlanList,curr,new UserEntity(user.id));
		}
		
		//给供应商发布消息
		Map<Long,Long >  idMap = new HashMap<Long, Long>();
		for(PurchasePlanItemEntity planItem : planItems) {
			idMap.put(planItem.getVendor().getId(), planItem.getVendor().getId());
		}
		List<Long> userIdList = new ArrayList<Long>();
		for (Long keys : idMap.keySet()) {  
			//查询下面的人员
			 List<UserEntity> users = userServiceImpl.findByCompany(idMap.get(keys));
			 for(UserEntity useritem : users){
				 userIdList.add(useritem.getId());
			 }	
		}
		 warnMainService.warnMessageSet( planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_UN_VETO, userIdList);
	
		 
		 //解除驳回待处理的信息
		 List<PurchasePlanItemEntity> warnItemList  =purchasePlanItemDao.findRejectByMainId(planItems.get(0).getId());//查询未处理的驳回记录
		 if( warnItemList.size() == 0 || warnItemList == null  ){//判断是否完全处理完驳回的记录
			 //把未发布的消息删除
			 warnMainService.delMessageByBillIdAndBillType(planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_REJECT,user.id);
		 }
	}
	
	
	
	
	
	/***
	 * 供应商确认采购计划明细
	 * @param plans
	 */
	public void confirmPlans(List<PurchasePlanItemEntity> planItems) {
		List<PurchasePlanItemEntity> vendorItemPlanList = new ArrayList<PurchasePlanItemEntity>(); 
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanItemEntity planItem : planItems) {
			planItem  = purchasePlanItemDao.findOne(planItem.getId());
			planItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			purchasePlanItemDao.save(planItem);
			vendorItemPlanList.add(planItem);
		}
		
		//更新主表确认状态（供应商）
		if(vendorItemPlanList.size() != 0 ){
			commonOrderConfirmStatus(vendorItemPlanList,curr,new UserEntity(user.id));
		}
		
		//发送确认消息（给主表创建人）
		PurchasePlanEntity plan = planItems.get(0).getPlan();
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(plan.getCreateUserId());
		warnMainService.warnMessageSet( plan.getId(), WarnConstant.ORDER_PLAN_CONFIRM, userIdList);
		
		
	     //通过组织的id获取下面的人员
		 List<UserEntity> users = userServiceImpl.findByCompany(user.orgId);
		 for(UserEntity useritem : users){
			//解除供应商的待确认的消息
			 List<PurchasePlanItemEntity> warnList1 = purchasePlanItemDao.findUnconfimByMainIdForVendor(plan.getId(),user.orgId);
			 List<PurchasePlanItemEntity> warnList2 = purchasePlanItemDao.findVoteByMainIdForVendor(plan.getId(),user.orgId);
		 
			 if(warnList1 == null || warnList1.size()==0 ){
				 //把供应商未确认的信息的消息删除
				 warnMainService.delMessageByBillIdAndBillType(planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_UN_CONFIRM,useritem.getId());
			 }
			 
			 if(warnList2 == null || warnList2.size()==0 ){
				 //把供应商未确认的信息的消息删除
				 warnMainService.delMessageByBillIdAndBillType(planItems.get(0).getPlan().getId(), WarnConstant.ORDER_PLAN_VETO,useritem.getId());
			 }
		 }	
	
		
		
		
	}
	
	/***
	 * 供应商驳回采购计划明细
	 * @param plans
	 */
	public void rejectPlans(List<PurchasePlanItemEntity> planItems,String reason) {
		List<PurchasePlanItemEntity> vendorItemPlanList = new ArrayList<PurchasePlanItemEntity>(); 
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanItemEntity planItem : planItems) {
			planItem  = purchasePlanItemDao.findOne(planItem.getId());
			planItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_REJECT);
			planItem.setRejectReason(reason);
			planItem.setRejectTime(curr);
			planItem.setRejectUser(new UserEntity(user.id));
			purchasePlanItemDao.save(planItem);
			vendorItemPlanList.add(planItem);
		}
		
		//更新主表确认状态（供应商）
		
		if(vendorItemPlanList.size() != 0 ){
			commonOrderConfirmStatus(vendorItemPlanList,curr,new UserEntity(user.id));
		}
		
		//发送确认消息（给主表创建人）
		PurchasePlanEntity plan = planItems.get(0).getPlan();
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(plan.getCreateUserId());
		warnMainService.warnMessageSet( plan.getId(), WarnConstant.ORDER_PLAN_REJECT, userIdList);
		
		
	}
	
	
	
	//更新主表的确认状态
	public void commonOrderConfirmStatus(List<PurchasePlanItemEntity> vendorItemPlanList,Timestamp curTime,UserEntity user){
		if(vendorItemPlanList !=null && vendorItemPlanList.size() != 0){
			
		
			
			//根据子项目获取供应商的采购计划的主表的信息
			PurchasePlanItemEntity  temItemPlan= vendorItemPlanList.get(0);
			
			//获取所有的字表
			PurchasePlanEntity entity=purchasePlanDao.findOne(temItemPlan.getPlan().getId());
			PurchasePlanVendorEntity vendorPlan = purchasePlanVendorDao.findPurchasePlanVendorEntityByVendorAndVendorId(temItemPlan.getPlan().getId(),temItemPlan.getVendor().getId());
			Set<PurchasePlanItemEntity> orderItemSet=entity.getPlanItem();
			List<PurchasePlanItemEntity> effectList =new  ArrayList<PurchasePlanItemEntity>(); 
			for(PurchasePlanItemEntity item  : effectList){
				if(item.getIsNew() == 1){
					effectList.add(item);
				}
			}
			
			if(effectList != null && effectList.size() >0){
				int count_all=0;
				int count_part=0;
				for (PurchasePlanItemEntity item : orderItemSet) {
					if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==item.getConfirmStatus().intValue()){
						count_all++;
					}else if(PurchaseConstans.CONFIRM_STATUS_PART.intValue()==item.getConfirmStatus().intValue()){
						count_part++;
					}
				}
				
				if(count_all>0 && count_all==vendorItemPlanList.size()){
					vendorPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
				}else if(count_part >0 || count_all>0){
					vendorPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
				}else{
					vendorPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				}
				vendorPlan.setConfirmTime(curTime);
				vendorPlan.setLastUpdateTime(curTime);
				vendorPlan.setUpdateUserId(user.getId());
				vendorPlan.setUpdateUserName(user.getCreateUserName());
				purchasePlanVendorDao.save(vendorPlan);
			}


		}
	}
	
	
	
	public Page<PurchasePlanItemEntity> getPurchasePlanItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) throws Exception {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanItemEntity.class);
		Page<PurchasePlanItemEntity> page = purchasePlanItemDao.findAll(spec,pagin);
		List<PurchasePlanItemEntity> purchasePlanItemList=page.getContent();
		
		
		/*PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, null);
		
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" QEWEB_PURCHASE_PLAN_ITEM A");
		sql.append(" LEFT JOIN QEWEB_ORGANIZATION B ON A.VENDOR_ID = B. ID");
		sql.append(" LEFT JOIN QEWEB_MATERIAL C ON A.MATERIAL_ID = C. ID  ");
		sql.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID = D. ID  ");
		sql.append(" LEFT JOIN QEWEB_USER E ON A.CREATE_USER_ID = E. ID");
		sql.append(" LEFT JOIN QEWEB_USER F ON A.PUBLISH_USER_ID = F. ID");
		sql.append(" LEFT JOIN QEWEB_PURCHASING_GROUP G ON A.GROUP_ID = G. ID  ");
		sql.append(" WHERE ");
		sql.append(" A.ABOLISHED = 0");
		sql.append("AND  A.PLAN_ID = "+searchParamMap.get("EQ_plan.id")+"");
		if(searchParamMap.containsKey("EQ_plan.buyer.name")){
			if( !StringUtils.isEmpty(searchParamMap.get("EQ_plan.buyer.name").toString())){
				sql.append(" AND E.NAME = '"+searchParamMap.get("EQ_plan.buyer.name")+"'");//采购员
			}
		}
		
		if(searchParamMap.containsKey("EQ_confirmStatus")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_confirmStatus").toString())){
				sql.append(" AND A.CONFIRM_STATUS = '"+searchParamMap.get("EQ_confirmStatus")+"'");//确认状态
			}
			
		}
		
		if(searchParamMap.containsKey("EQ_group.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_group.code").toString())){
				sql.append(" AND G.CODE = '"+searchParamMap.get("EQ_group.code")+"'");//采购组
			}
			
		}
		
		if(searchParamMap.containsKey("EQ_vendor.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_vendor.code").toString())){
				sql.append(" AND B.CODE = '"+searchParamMap.get("EQ_vendor.code")+"'");//供应商编码
			}
			
		}
		
		if(searchParamMap.containsKey("LIKE_vendor.name")){
			if(!StringUtils.isEmpty(searchParamMap.get("LIKE_vendor.name").toString())){
				sql.append(" AND B.NAME like '%"+searchParamMap.get("LIKE_vendor.name")+"%'");//供应商名称
			}
			
		}
		
		if(searchParamMap.containsKey("EQ_material.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_material.code").toString())){
				sql.append(" AND C.CODE = '"+searchParamMap.get("EQ_material.code")+"'");//物料号
			}
			
		}
		if(searchParamMap.containsKey("EQ_publishStatus")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_publishStatus").toString())){
				sql.append(" AND A.PUBLISH_STATUS = '"+searchParamMap.get("EQ_publishStatus")+"'");//发布状态
			}
			
		}
		
		if(searchParamMap.containsKey("EQ_publishUser")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_publishUser").toString())){
				sql.append(" AND F.NAME like '%"+searchParamMap.get("EQ_publishUser")+"%'");//发布人
			}
			
		}
		
		
		if(searchParamMap.containsKey("EQ_isNew")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_isNew").toString())){
				sql.append(" AND A.IS_NEW = '"+searchParamMap.get("EQ_isNew")+"'");//有效状态
			}
		}else{
			sql.append(" AND A.IS_NEW = 1");//有效状态
		}
		
		
		if(searchParamMap.containsKey("LIKE_versionNumber")){
			if(!StringUtils.isEmpty(searchParamMap.get("LIKE_versionNumber").toString())){
				sql.append(" AND A.VERSION_NUMBER like '%"+searchParamMap.get("LIKE_versionNumber")+"%'");//版本号	
			}
			
		}
		
		
		if(searchParamMap.containsKey("LIKE_factory.name")){
			if(!StringUtils.isEmpty(searchParamMap.get("LIKE_factory.name").toString())){
				sql.append(" AND D.NAME like '%"+searchParamMap.get("LIKE_factory.name")+"%'");//工厂
			}
			
		}
		
		
		if(searchParamMap.containsKey("EQ_factory.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_factory.code").toString())){
				sql.append(" AND D.CODE = '"+searchParamMap.get("EQ_factory.code")+"'");//工厂
			}
			
		}
		
		
		if(searchParamMap.containsKey("EQ_vendor.id")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_vendor.id").toString())){
				sql.append(" AND B.ID = '"+searchParamMap.get("EQ_vendor.id")+"'");//供应商iD
			}
			
		}
		
		sql.append(" ORDER BY  ");
		sql.append(" A.CREATE_TIME desc");
		String sql1="SELECT A.* "+sql.toString();
		String sqlTotalCount="select count(1) "+sql.toString();
		List<?> list = generialDao.querybysql(sql1,pagin);
		List<PurchasePlanItemEntity> purchasePlanItemList=new ArrayList<PurchasePlanItemEntity>();
		int totalCount=generialDao.findCountBySql(sqlTotalCount);
		if(null!=list && list.size()>0){
			for(Object purchasePlanItem : list){
				Map<String,Object> m =  (Map<String, Object>) purchasePlanItem;
				PurchasePlanItemEntity entity=purchasePlanItemDao.findById(((BigDecimal)m.get("ID")).longValue());
				if(entity != null){
					purchasePlanItemList.add(entity);
				}
			
				
			}
		}*/
		
		//获取选择大版本的第一个星期一
    	PurchasePlanEntity purchasePlan = new PurchasePlanEntity();
    	purchasePlan = purchasePlanDao.findOne(Long.parseLong(searchParamMap.get("EQ_plan.id").toString()));
    	String version = purchasePlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
/*    	//过滤查询条件（时间）
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(searchParamMap.containsKey("EQ_startDate") && searchParamMap.get("EQ_startDate") != null && searchParamMap.get("EQ_startDate") != ""){
    		start = sdf.parse(searchParamMap.get("EQ_startDate").toString());  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(searchParamMap.containsKey("EQ_endDate") && searchParamMap.get("EQ_endDate") != null && searchParamMap.get("EQ_endDate") != "" ){
        	end   = sdf.parse(searchParamMap.get("EQ_endDate").toString());  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}
    	*/
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
        /*    if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){*/
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
        /*    }*/
            date=getSeventDay(date);
        }
		
 

		//重新加载list
		List<PurchasePlanItemEntity> viewPurchasePlanItemList=new ArrayList<PurchasePlanItemEntity>();
		for(PurchasePlanItemEntity purchasePlanItem : purchasePlanItemList){
			
			purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
			PurchasePlanItemHeadVO tmpPurchasePlanItemHeadVO = purchasePlanItemHeadVO;
			Class clazz1 = tmpPurchasePlanItemHeadVO.getClass();  
			Object object1 = clazz1.newInstance();
			Field[] fields1 = clazz1.getDeclaredFields(); 
			
			
			PurchasePlanItemEntity item = purchasePlanItem;
			Set<PurchasePlanHeadEntity> headList= purchasePlanItem.getPurchasePlanHeadEntity();
			for(PurchasePlanHeadEntity purchasePlanHead : headList ){	
			    for(int i = 0 ; i < fields1.length; i++){ 
			    	Field f1 = fields1[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(tmpPurchasePlanItemHeadVO);//得到此属性的值  
			        if(purchasePlanHead.getHeaderName().equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz1.getMethod(methodStr,new Class[]{f1.getType()});
		            	 method.invoke(object1,purchasePlanHead.getHeaderValues());
		            	 break;
					}
			    }			
			}
			
			PurchasePlanItemHeadVO tempVo = new PurchasePlanItemHeadVO();
			BeanUtil.copyPropertyNotNull((PurchasePlanItemHeadVO) object1,tempVo);
			item.setPurchasePlanItemHeadVO(tempVo);
			item.setHeadVO(purchasePlanItemHeadVO);
			viewPurchasePlanItemList.add(item);
		}
    	/*Page<PurchasePlanItemEntity> page=new PageImpl<PurchasePlanItemEntity>(viewPurchasePlanItemList,pagin,totalCount);*/
 		return page;	
	}
	

	//表头数据
	public PurchasePlanItemHeadVO getItemHead(Long id,String startDate,String endDate) throws Exception {
		//获取选择大版本的第一个星期一
    	PurchasePlanEntity purchasePlan = new PurchasePlanEntity();
    	purchasePlan = purchasePlanDao.findOne(id);
    	String version = purchasePlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
    	//过滤查询条件（时间）
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(startDate != null && startDate!="" ){
    		start = sdf.parse(startDate);  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(endDate != null && endDate !="" ){
        	end   = sdf.parse(endDate);  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}
    	
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
            if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
            }
            date=getSeventDay(date);
        }
		
		purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
		
		return purchasePlanItemHeadVO;
	}
	
	
	
	/**
	 * 转换并删除
	 * @param planTranList
	 * @param logger
	 * @throws Exception
	 */
	public boolean combinePurchaseDeletePlan(List<PurchasePlanDeleteTransfer> planTranList, ILogger logger) throws Exception {	
		//工厂
		Map<String, FactoryEntity> factoryMap = new HashMap<String, FactoryEntity>();
		//采购员
		Map<String, UserEntity> buyerMap = new HashMap<String, UserEntity>();
		//供应商
		Map<String, OrganizationEntity> vendorMap = new HashMap<String, OrganizationEntity>();
		//物料
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		//采购组
		Map<String, PurchasingGroupEntity> groupMap = new HashMap<String, PurchasingGroupEntity>();
		//验证当前数据
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchasePlanDeleteTransfer> list = validateDeleteTransfers(planTranList,factoryMap, vendorMap,buyerMap,materialMap,groupMap ,counts, logger);
		if(list.size() != planTranList.size()) {
			logger.log("<b><font color='red'>导入的预测删除计划失败！有效数据条数["+list.size()+"]与导入数据条数["+planTranList.size()+"]不匹配！请修改预测计划！</font></b>");
			return false;
		}
		
		boolean flag = true;
		List<PurchasePlanItemEntity> planItemList = new  ArrayList<PurchasePlanItemEntity>();
		
		Date date = new Date();
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);
	    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
		int index  = 2; 
		//正式导入删除计划
		for(PurchasePlanDeleteTransfer tran : list){
			/*String logMsg= "->现在对导入的预测删除信息进行处理,共有[" + (planTranList == null ? 0 : planTranList.size()) + "]条数据";*/
			String logMsg="";
			//获取唯一的数据源
			PurchasePlanItemEntity planItem = purchasePlanItemDao.findPurchasePlanItemEntityByDeleteKeys(tran.getGroupCode(),tran.getFactoryCode(),tran.getMaterialCode(),tran.getVendorCode(),month);
			if(planItem == null){
				logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],数据无法删除！请检验此数据是否存在,已删除失效或者已发布！</font></b>");
				index++;
				flag = false;
			}else{
				planItemList.add(planItem);
			}
		}
		//进行作废处理
		if(flag == true){
			for( PurchasePlanItemEntity item  : planItemList){
				purchasePlanItemDao.invalidPurchasePlanItem(item.getId());
				purchasePlanHeadDao.invalidPurchasePlanHead(item.getId());                           
			}
		}
		return flag;
	}
	
	
	/**
	 * 转换并保存
	 * @param planTranList
	 * @param logger
	 * @throws Exception
	 */
	public boolean combinePurchasePlan(List<PurchasePlanTransfer> planTranList, ILogger logger) throws Exception {	
		List<PurchasePlanTransfer> tempplanTranList = new ArrayList<PurchasePlanTransfer>();
		
		for(PurchasePlanTransfer tr : planTranList){
			PurchasePlanTransfer tf = new PurchasePlanTransfer();
			BeanUtil.copyPropertyNotNull((PurchasePlanTransfer) tr,tf);
			tempplanTranList.add(tf);
		}
		
		//工厂
		Map<String, FactoryEntity> factoryMap = new HashMap<String, FactoryEntity>();
		//采购员
		Map<String, UserEntity> buyerMap = new HashMap<String, UserEntity>();
		//供应商
		Map<String, OrganizationEntity> vendorMap = new HashMap<String, OrganizationEntity>();
		//物料
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		//采购组
		Map<String, PurchasingGroupEntity> groupMap = new HashMap<String, PurchasingGroupEntity>();
		//验证当前数据
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchasePlanTransfer> list = validateTransfers(planTranList,factoryMap, vendorMap,buyerMap,materialMap,groupMap ,counts, logger);
		if(list.size() != planTranList.size()) {
			int listCount = list.size() - 1;
			int planCount = planTranList.size() - 1;
			logger.log(" -> <b><font color='red'>导入的预测计划失败！有效数据条数["+listCount+"]与导入数据条数["+planCount+"]不匹配！请修改预测计划！ </font></b>");
			return false;
		}
		
		//验证当前数据是否重复
		boolean flag1 =  checkDataSource(list,logger);
		if(!flag1){
			return false;
		}
		
		
		//验证当前数据与计划员头是否匹配
		boolean flag =  matchDataSource(planTranList,logger);
		if(!flag){
			return false;
		}

		//正式开始导入
		Date date = new Date();
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);
	    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
		PurchasePlanEntity plan = null;
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String version  ="";
		plan = purchasePlanDao.findPurchasePlanEntityByMonthAndCreateUserId(month,user.id);
		if(plan == null){
			//添加新的版本
			addPurchasePlan(tempplanTranList,month,version,factoryMap,vendorMap,materialMap, groupMap,logger);
		}else{
			//修改大版本信息
			updatePurchasePlanItemHead(tempplanTranList,month,version,factoryMap,vendorMap,materialMap,groupMap,plan,logger);
		}
		return true;
	}
	
	/**
	 * 新增采购计划
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void addPurchasePlan(List<PurchasePlanTransfer> planTranList,String month,String version,Map<String, FactoryEntity> factoryMap,Map<String, OrganizationEntity> vendorMap, Map<String, MaterialEntity> materialMap, Map<String, PurchasingGroupEntity> groupMap, ILogger logger)  throws Exception {
 
		List<PurchasePlanTransfer> headList = new ArrayList<PurchasePlanTransfer>();//表头
		headList.add(planTranList.get(0));
		planTranList.removeAll(headList);//数据项
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();

	/*	logger.log("\n->正在准备新增主数据与明细数据\n");*/
		
		PurchasePlanEntity purchasePlan = new PurchasePlanEntity();
		PurchasePlanItemEntity purchasePlanItem = new PurchasePlanItemEntity();
		PurchasePlanVendorEntity purchasePlanVendor = new PurchasePlanVendorEntity();
		
		//供应商计划
		List<PurchasePlanItemEntity> purchasePlanItemList = new ArrayList<PurchasePlanItemEntity>();
		List<PurchasePlanVenodrItemEntity> purchasePlanVendorItemList = new ArrayList<PurchasePlanVenodrItemEntity>();
		List<PurchasePlanHeadEntity> purchasePlanHeadList = new ArrayList<PurchasePlanHeadEntity>();
		
		Map<String, PurchasePlanEntity> tmpMap = new HashMap<String, PurchasePlanEntity>();
		Map<String, PurchasePlanVendorEntity> tmpvenMap = new HashMap<String, PurchasePlanVendorEntity>();
	
		String key = null;
		
		for(PurchasePlanTransfer trans : planTranList) {
			key = month + ";" + user.id;
			if(!tmpMap.containsKey(key)) {
				purchasePlan = getPurchasePlanEntity(month, user.id);   //获取采购计划、
				if(purchasePlan.getId() != 0){
					purchasePlan.setUploadStatus(2);//上传产能表状态（部分）
					purchasePlan.setPublishStatus(2);//发布状态（部分）
				}else{
					purchasePlan.setUploadStatus(0);//上传产能表状态
					purchasePlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);//发布状态
				}
				purchasePlan.setMonth(month);//大版本号
				tmpMap.put(key, purchasePlan);
			} else {
				purchasePlan = tmpMap.get(key);
			}
			
			//操作人+物料+工厂+采购组+供应商+key
			String versionKey = user.id+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode()+trans.getVendorCode()+"poItemPlan"+",";
		    String  allVersion =  serialNumberService.geneterPHNextNumberByKey(versionKey);
		    String [] strs = allVersion.split(",");
			version =strs[1];
			//设置明细单表信息
			purchasePlanItem = getPurchasePlanItemEntity(purchasePlan, trans);  //获取采购计划的子项目
			purchasePlanItem.setPlan(purchasePlan);
			purchasePlanItem.setVendor(vendorMap.get(trans.getVendorCode()));//供应商
			purchasePlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
			purchasePlanItem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
			purchasePlanItem.setIsNew(1);
			purchasePlanItem.setVersionNumber(version);
			purchasePlanItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			purchasePlanItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			purchasePlanItem.setUploadStatus(0);
			purchasePlanItem.setDayStock(trans.getInventory());
			purchasePlanItem.setUnpaidQty(trans.getDeliveredCount());
			purchasePlanItem.setGroup(groupMap.get(trans.getGroupCode()));//采购组
			purchasePlanItemList.add(purchasePlanItem);
			
			
			//设置供应商头信息（共用字表信息）
			key = key + ";" + trans.getVendorCode();
			if(!tmpvenMap.containsKey(key)) {
				purchasePlanVendor = getPurchasePlanVendorEntity(month, trans);
				purchasePlanVendor.setMonth(month);
				purchasePlanVendor.setPlan(purchasePlan);
				purchasePlanVendor.setVendor(vendorMap.get(trans.getVendorCode()));
				purchasePlanVendor.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				purchasePlanVendor.setUploadStatus(0);//上传产能表状态
				tmpvenMap.put(key, purchasePlanVendor);
			} else {
				purchasePlanVendor = tmpvenMap.get(key);
			}
			
			
			//设置表头信息
			PurchasePlanTransfer headerTransfer = headList.get(0);
		
			
			Class clazz1 = trans.getClass();  
			Class clazz2 = headerTransfer.getClass();
          
			Field[] fs1 = clazz1.getDeclaredFields();  
			Field[] fs2 = clazz2.getDeclaredFields();  
	        for(int i = 6 ; i < 30; i++){ 
	        	Field f1 = fs1[i];
	        	f1.setAccessible(true); //设置些属性是可以访问的  
	            Object val1 = f1.get(trans);//得到此属性的值  
	        	for(int j = 0; j < fs2.length;j++){
	        		Field f2 = fs1[j];
	        		f2.setAccessible(true); //设置些属性是可以访问的  
		            Object val2 = f2.get(headerTransfer);//得到此属性的值  
		            if(f1.getName().equals(f2.getName())){
		            	PurchasePlanHeadEntity purchasePlanHead=new PurchasePlanHeadEntity() ;
		            	purchasePlanHead.setPlanItem(purchasePlanItem);
		            	purchasePlanHead.setHeaderName(val2 == null ? "0" : val2.toString());
						purchasePlanHead.setHeaderValues( val1 == null ? "0" : val1.toString());
						purchasePlanHead.setVersionNumber(version);
						purchasePlanHead.setIsNew(1);
						purchasePlanHeadList.add(purchasePlanHead);
						break;
		            }
	        	}
	        }   	
		}
		String logMsg = "添加成功[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		//保存
		purchasePlanDao.save(tmpMap.values());
		purchasePlanItemDao.save(purchasePlanItemList);   
		purchasePlanHeadDao.save(purchasePlanHeadList);
		purchasePlanVendorDao.save(tmpvenMap.values());
		
		//添加预警信息
		 List<Long> userIdList = new ArrayList<Long>();
		 userIdList.add(user.id);
		 for (String keys : tmpMap.keySet()) {  
			 warnMainService.warnMessageSet( tmpMap.get(keys).getId(), WarnConstant.ORDER_PLAN_UN_PUBLISH, userIdList);
	      }  
	}

	/**
	 * 修改采购计划表头表
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void updatePurchasePlanItemHead(List<PurchasePlanTransfer> planTranList,String month,String version,Map<String, FactoryEntity> factoryMap,Map<String, OrganizationEntity> vendorMap,Map<String, MaterialEntity> materialMap, Map<String, PurchasingGroupEntity> groupMap,PurchasePlanEntity plan, ILogger logger)  throws Exception {
		
		List<PurchasePlanTransfer> headList = new ArrayList<PurchasePlanTransfer>();//表头
		headList.add(planTranList.get(0));
		planTranList.removeAll(headList);//数据项
		
/*		logger.log("\n->正在准备更新数据主数据与明细数据\n");*/
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		List<PurchasePlanItemEntity> purchasePlanItemList = new ArrayList<PurchasePlanItemEntity>();//重新插入的(更新一下，不需升级)
		List<PurchasePlanItemEntity> upPurchasePlanItemList = new ArrayList<PurchasePlanItemEntity>();//升级的
		
		
		//失效的list集合
		List<Long> invalidPlanItemIds = new ArrayList<Long>();
		List<Long> invalidVenPlanItemIds = new ArrayList<Long>();
		
		//重新填入的数据项
		List<PurchasePlanTransfer> newUpdateList = new ArrayList<PurchasePlanTransfer>();//更新表头的
		List<PurchasePlanTransfer> newInsertList = new ArrayList<PurchasePlanTransfer>();//重新插入的
		List<PurchasePlanTransfer> newUpgradeList = new ArrayList<PurchasePlanTransfer>();//升级的
		
		//导入的中有上次未导入的数据（新添入的需要加入头部）
		newInsertList.addAll(headList);
		
		//组合新的集合插入
		Map<String, PurchasePlanHeadEntity> oldHeadMap = new HashMap<String, PurchasePlanHeadEntity>();
    	Map<String, PurchasePlanHeadEntity> newHeadMap = new HashMap<String, PurchasePlanHeadEntity>();
    	
		//插入新的list和作废老版本
		for(PurchasePlanTransfer trans : planTranList) {
		
			//根据当前数据确定唯一采购计划的行（采购组id和物料的id，供应商id）后续添加相应的数据查询匹配的数据要是we发布的)
			List<PurchasePlanItemEntity> tempPlanList =purchasePlanItemDao.findPurchasePlanItemEntityByMainKeys(trans.getGroupCode(),trans.getFactoryCode(),trans.getMaterialCode(),trans.getVendorCode(),plan.getId());
			

			if(tempPlanList == null || tempPlanList.size() <= 0){
				newInsertList.add(trans);
				continue;
			}
			
/*			//采购计划的行为未发布的时候 --进行修改和升级此项目动作
			if(tempPlanList.get(0).getPublishStatus() != 0 ){
				continue;
			}*/
	
			//设置表头信息
			PurchasePlanTransfer headerTransfer = headList.get(0);
			
			//获取盖子项目的表头
			List<PurchasePlanHeadEntity> oldPurchasePlanHeadList =new  ArrayList<PurchasePlanHeadEntity>();
			List<PurchasePlanHeadEntity> newPurchasePlanHeadList = new ArrayList<PurchasePlanHeadEntity>();
			
		     //进行数据的处理（添加相应的数据）
			oldPurchasePlanHeadList = purchasePlanHeadDao.findNewPurchasePlanHeadByplanItemId(tempPlanList.get(0).getId());//获取老的数据项的数据

			Class clazz1 = trans.getClass();  
			Class clazz2 = headerTransfer.getClass();
          
			Field[] fs1 = clazz1.getDeclaredFields();  
			Field[] fs2 = clazz2.getDeclaredFields();  
	        for(int i = 6 ; i < 30; i++){ 
	        	Field f1 = fs1[i];
	        	f1.setAccessible(true); //设置些属性是可以访问的  
	            Object val1 = f1.get(trans);//得到此属性的值  
	        	for(int j = 0; j < fs2.length;j++){
	        		Field f2 = fs1[j];
	        		f2.setAccessible(true); //设置些属性是可以访问的  
		            Object val2 = f2.get(headerTransfer);//得到此属性的值  
		            if(f1.getName().equals(f2.getName())){
		            	PurchasePlanHeadEntity purchasePlanHead=new PurchasePlanHeadEntity() ;
		            	purchasePlanHead.setHeaderName(val2 == null ? "0" : val2.toString());
						purchasePlanHead.setHeaderValues( val1 == null ? "0" : val1.toString());
						purchasePlanHead.setVersionNumber(version);
						purchasePlanHead.setIsNew(1);
						newPurchasePlanHeadList.add(purchasePlanHead);
						break;
		            }
	        	}
	        }
	        
	        //整理数据结构进行对比
	        for(PurchasePlanHeadEntity item1 :    oldPurchasePlanHeadList){
	        	if(item1.getHeaderValues() == null || item1.getHeaderValues() == ""){
	        		item1.setHeaderValues("0");
	        	}
	        }
	        
	        for(PurchasePlanHeadEntity item2 :    newPurchasePlanHeadList){
	        	if(item2.getHeaderValues() == null || item2.getHeaderValues() == ""){
	        		item2.setHeaderValues("0");
	        	}
	        }

	    	if(isActualVersion(oldPurchasePlanHeadList,newPurchasePlanHeadList)){//更新数据升级版本
	    		
	    		//操作人+物料+工厂+采购组+供应商+key
	    		String versionKey = user.id+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode()+trans.getVendorCode()+"poItemPlan"+",";
			    String  allVersion =  serialNumberService.geneterPHNextNumberByKey(versionKey);
			    String [] strs = allVersion.split(",");
			    version =strs[1];
				//修改预测计划的子项目的类容(添加)
			    PurchasePlanItemEntity tem = new PurchasePlanItemEntity();//设置明细单表信息
				tem.setPlan(plan);
				tem.setVendor(vendorMap.get(trans.getVendorCode()));//供应商
				tem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
				tem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
				tem.setIsNew(1);
				tem.setVersionNumber(version);
				tem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				tem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				tem.setUploadStatus(0);
				tem.setDayStock(trans.getInventory());
				tem.setUnpaidQty(trans.getDeliveredCount());
				tem.setGroup(groupMap.get(trans.getGroupCode()));//采购组
				upPurchasePlanItemList.add(tem);
	    		newUpgradeList.add(trans);
	    		
	    		//将原来的版本失效ids集合(表头表)
	    		if(oldPurchasePlanHeadList != null && oldPurchasePlanHeadList.size() >0 ){
	    			invalidPlanItemIds.add(oldPurchasePlanHeadList.get(0).getPlanItem().getId());
	    		}
	    	
	    		
	    		//获取要重新添加的数据项
		    	for (PurchasePlanHeadEntity oldPurchasePlanHead : oldPurchasePlanHeadList) {  
		    		oldHeadMap.put(oldPurchasePlanHead.getHeaderName()+trans.getMaterialCode()+trans.getFactoryCode()+trans.getVendorCode()+trans.getGroupCode(), oldPurchasePlanHead);  
		        }
		    	for (PurchasePlanHeadEntity newPurchasePlanHead : newPurchasePlanHeadList) {  
		    		newPurchasePlanHead.setPlanItem(tem);
		    		newHeadMap.put(newPurchasePlanHead.getHeaderName()+trans.getMaterialCode()+trans.getFactoryCode()+trans.getVendorCode()+trans.getGroupCode(), newPurchasePlanHead);  
		        }
		    	//去除老版本的中的新版本的覆盖的元素
		    	List<String> keys =new  ArrayList<String>();
		    	 for (Map.Entry<String, PurchasePlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) {
		    		 for (Map.Entry<String, PurchasePlanHeadEntity> newHeadMapEntry : newHeadMap.entrySet()) {
			    		 if(oldHeadMaprntry.getKey().equals(newHeadMapEntry.getKey())){
			    			 keys.add(oldHeadMaprntry.getKey());
			    		 }
			    	}
		    	}
		    	 for(String s : keys){
		    		 oldHeadMap.remove(s);
		    	 }
		    	 
		    	//合并剩下的老版本的额数据
		    	if(oldHeadMap.size() != 0){
		    		 for (Map.Entry<String, PurchasePlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) { 
		    			 oldHeadMaprntry.getValue().setVersionNumber(version);
			    	}
		    		newHeadMap.putAll(oldHeadMap);
		    	}	
	    	}else{
	    		//更新数据不升级
				//设置明细单表信息
	    		PurchasePlanItemEntity _purchaseTotalPlanItem = new PurchasePlanItemEntity();
	    		List<PurchasePlanItemEntity> temp1PlanList =purchasePlanItemDao.findPurchasePlanItemEntityByMainKeys(trans.getGroupCode(),trans.getFactoryCode(),trans.getMaterialCode(),trans.getVendorCode(),plan.getId());
				_purchaseTotalPlanItem = temp1PlanList.get(0);  //获取预测计划总量的子项目
				_purchaseTotalPlanItem.setPlan(plan);
				_purchaseTotalPlanItem.setVendor(vendorMap.get(trans.getVendorCode()));//供应商
				_purchaseTotalPlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
				_purchaseTotalPlanItem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
				_purchaseTotalPlanItem.setGroup(groupMap.get(trans.getGroupCode()));//采购组
				_purchaseTotalPlanItem.setIsNew(1);
				_purchaseTotalPlanItem.setDayStock(trans.getInventory());
				_purchaseTotalPlanItem.setUnpaidQty(trans.getDeliveredCount());
				purchasePlanItemList.add(_purchaseTotalPlanItem);
	    		newUpdateList.add(trans);

	    	}	
		}
		
		String logMsg = ""; 
		
		//添加新的数据
		if(newInsertList.size() > 1){
			addPurchasePlan(newInsertList,month,version,factoryMap,vendorMap,materialMap, groupMap,logger);
		}
		//更新现有的数据
		if(newUpdateList.size() > 0 && newUpdateList != null){
			//只是单纯的更新行数据不做升级的操作
			purchasePlanItemDao.save(purchasePlanItemList);
			logMsg = "更新未升级[" + newUpdateList.size() + "]条有效的数据";
		}
		//升级当前的版本（失效老版本并添加新的数据）
        if(newUpgradeList.size() > 0 && newUpgradeList != null){
        	//失效表头-预测计划总量字表
    		if(invalidPlanItemIds.size() > 0 && invalidPlanItemIds != null){
    			for(Long planItemId : invalidPlanItemIds){//失效表头表
    				purchasePlanHeadDao.invalidPurchasePlanHead(planItemId);
    			}
    			for(Long planItemId : invalidPlanItemIds){//失效字表
    				purchasePlanItemDao.invalidPurchasePlanItem(planItemId);
    			}
    		}
    		purchasePlanItemDao.save(upPurchasePlanItemList);
    		List<PurchasePlanHeadEntity> temList = new ArrayList<PurchasePlanHeadEntity>();
    		
    		Iterator<Entry<String, PurchasePlanHeadEntity>> iter = newHeadMap.entrySet().iterator();  //获得map的Iterator
    		while(iter.hasNext()) {
    			Entry entry = (Entry)iter.next();
    			temList.add((PurchasePlanHeadEntity) entry.getValue());
    		}
    		
    		purchasePlanHeadDao.save(newHeadMap.values());
    		
    		logMsg = "更新并升级[" + newUpgradeList.size() + "]条有效的数据";
    		
	    	//添加预警信息
	   		 List<Long> userIdList = new ArrayList<Long>();
	   		 userIdList.add(user.id);
	   		 for (PurchasePlanItemEntity PurchasePlanItem : upPurchasePlanItemList) {  
	   			 warnMainService.warnMessageSet( PurchasePlanItem.getPlan().getId(), WarnConstant.ORDER_PLAN_UN_PUBLISH, userIdList);
	   	      }  
    		
		}
		
	    //添加当前的版本
	/*	String logMsg = "合并主数据与明细数据结束,共有[" + planTranList.size() + "]条有效的数据";*/
		logger.log(logMsg);

	}
	
	/**
	 * 判断是否需要升级版本
	 * @param plans
	 */
	public Boolean isActualVersion(List<PurchasePlanHeadEntity> oldHeadList ,List<PurchasePlanHeadEntity> newHeadList) {
		 //整合list为map
    	Map<String, PurchasePlanHeadEntity> oldHeadMap = new HashMap<String, PurchasePlanHeadEntity>();
    	Map<String, PurchasePlanHeadEntity> newHeadMap = new HashMap<String, PurchasePlanHeadEntity>();
    	Map<String, PurchasePlanHeadEntity> invariableHeadMap = new HashMap<String, PurchasePlanHeadEntity>();
		
    	for (PurchasePlanHeadEntity oldPurchasePlanHead : oldHeadList) {  
    		oldHeadMap.put(oldPurchasePlanHead.getHeaderName()+"-"+oldPurchasePlanHead.getHeaderValues(), oldPurchasePlanHead);  
        }
        
    	for (PurchasePlanHeadEntity newPurchasePlanHead : newHeadList) {  
    		newHeadMap.put(newPurchasePlanHead.getHeaderName()+"-"+newPurchasePlanHead.getHeaderValues(), newPurchasePlanHead);  
        }
        
    	//判断是否需要升级版本
    	 for (Map.Entry<String, PurchasePlanHeadEntity> newHeadMapEntry : newHeadMap.entrySet()) {
    		 for (Map.Entry<String, PurchasePlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) {
	    		 if(newHeadMapEntry.getKey().equals(oldHeadMaprntry.getKey())){
	    			 invariableHeadMap.put(oldHeadMaprntry.getKey(), oldHeadMaprntry.getValue());
	    		 }  
	    	}
    	}
    	if(invariableHeadMap.size() != 24){
    		return true;
    	}
		return false;
	}
	
	
	
	
	
	
	
	/**
	 * 预处理预测计划数据(增加)
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private List<PurchasePlanTransfer> validateTransfers(List<PurchasePlanTransfer> planTranList,Map<String, FactoryEntity> factoryMap,Map<String, OrganizationEntity> vendorMap,Map<String, UserEntity> buyerMap, Map<String, MaterialEntity> materialMap, Map<String, PurchasingGroupEntity> groupMap, Integer     [] counts, ILogger logger) {
			String logMsg = "->现在对导入的预测计划信息进行预处理,共有[" + (planTranList == null ? 0 : planTranList.size() - 1) + "]条数据";
			logger.log(logMsg);
			
			//获取当前人员的采购组的权限
			  List<Long> ids = buyerOrgPermissionUtil.getGroupIds();
			  Map<Long, Long> idsMap = new HashMap<Long, Long>();
			  if(ids != null){
				  for(Long id : ids){
					  idsMap.put(id, id);
				   }
			  }
			 
			
			
			counts[0] = planTranList.size();
			List<PurchasePlanTransfer> ret = new ArrayList<PurchasePlanTransfer>();
			
			List<UserEntity> buyerList=null; //采购员
			List<MaterialEntity> materialList = null; //物料
			List<FactoryEntity> factoryList = null ; //工厂
			List<PurchasingGroupEntity> groupList = null ; //采购组
			List<OrganizationEntity> vendorList = null ; //供应商
			
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			
			boolean lineValidat = true;
			int index  = 2; 
			int size= 1;
			for(PurchasePlanTransfer trans : planTranList) {
				////每次循环之后重置start
				buyerList=null;
				materialList = null;
				factoryList = null;
				groupList = null;
				vendorList = null;
				////重置end
				
				//自动过滤添加表头的数据增加
				if(size ==1){
					ret.add(trans);
					size++;
					continue;
				}
				
				if(StringUtils.isEmpty(trans.getFactoryCode())){
					lineValidat = false;
					logger.log("-> <b><font color='red'>[FAILED]行索引[" + index + "],工厂编码不能为空,忽略此数据 </font></b>");
				}
				
				
				if(StringUtils.isEmpty(trans.getGroupCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],采购组编码为空,忽略此数据 </font></b>");
				}
				

				
				if(StringUtils.isEmpty(trans.getMaterialCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],物料编码不能为空,忽略此数据 </font></b>");
				}
				
				
				if(StringUtils.isEmpty(trans.getVendorCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],供应商编码不能为空,忽略此数据 </font></b>");
				}
				
				//查询工厂
				if(!factoryMap.containsKey(trans.getFactoryCode())) {
					factoryList = factoryDao.findFactoryByCodeAndAbolished(trans.getFactoryCode(),0);
					if(CollectionUtils.isEmpty(factoryList)) {
						lineValidat = false;
						logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],工厂编码[" + trans.getFactoryCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						factoryMap.put(trans.getFactoryCode(), factoryList.get(0));
					}
				}
				
				
				
				//查询物料
				if(!materialMap.containsKey(trans.getMaterialCode())) {
					materialList = materialDao.findByCodeAndAbolished(trans.getMaterialCode(),0);
					if(CollectionUtils.isEmpty(materialList)) {
						lineValidat = false;
						logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],物料[" + trans.getMaterialCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						materialMap.put(trans.getMaterialCode(), materialList.get(0));
					}
				}
				
				//查询采购组
				if(!groupMap.containsKey(trans.getGroupCode())) {
					groupList = purchasingGroupDao.findByCodeAndAbolisheds(trans.getGroupCode(),0);
					if(CollectionUtils.isEmpty(groupList)) {
						lineValidat = false;
						logger.log(logMsg = "-> <b><font color='red'>[FAILED]行索引[" + index + "],采购组[" + trans.getGroupCode() + "]未在系统中维护,或被禁用,忽略此预测计划</font></b> ");
					} else {
							if(!idsMap.containsKey( groupList.get(0).getId())){
								lineValidat = false;
								logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],采购组[" + trans.getGroupCode() + "]的权限未在系统维护,忽略此预测计划 </font></b>");
							}					
						groupMap.put(trans.getGroupCode(), groupList.get(0));
					}
				}
				
	
				//查询供应商
				if(!vendorMap.containsKey(trans.getVendorCode())) {
					vendorList = organizationDao.findByCodeAndAbolisheds(trans.getVendorCode(),0);
					if(CollectionUtils.isEmpty(vendorList)) {
						lineValidat = false;
						logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],供应商[" + trans.getVendorCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						vendorMap.put(trans.getVendorCode(), vendorList.get(0));
					}
				}
				
				//添加查询此工厂下面是否有这个物料
				List<VendorMaterialRelEntity>  vmRel =  vendorMaterialRelDao.findByMaterialCodeAndFactoryCodeAndAbolished(trans.getMaterialCode(),trans.getFactoryCode(),0);
				if(vmRel == null || vmRel.size() == 0){
					lineValidat = false;
					logger.log(logMsg = "-> <b><font color='red'>[FAILED]行索引[" + index + "],工厂[" + trans.getFactoryCode() + "]  ，物料[" + trans.getMaterialCode() + "] 为维护此关系,忽略此预测计划 </font></b>");
				}

		
				if(lineValidat) {
				/*	logMsg = "[SUCCESS]行索引[" + index + "],预处理[ "+trans.getFactoryCode() +" | " + trans.getGroupCode() + "|" + trans.getMaterialCode() + "|]成功.";*/
				/*	logger.log(logMsg);*/
					ret.add(trans);
				}
				index ++;
				
				lineValidat = true;
			}
			int count= ret.size()-1;
			counts[1] = count;
			logMsg = "<-导入的预测计划信息预处理完毕,共有[" + count+ "]条有效数据";
			logger.log(logMsg);
			return ret;   
   }
	
	
	/**
	 * 预处理预测计划数据(增加)
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private List<PurchasePlanDeleteTransfer> validateDeleteTransfers(List<PurchasePlanDeleteTransfer> planTranList,Map<String, FactoryEntity> factoryMap,Map<String, OrganizationEntity> vendorMap,Map<String, UserEntity> buyerMap, Map<String, MaterialEntity> materialMap, Map<String, PurchasingGroupEntity> groupMap, Integer     [] counts, ILogger logger) {
			String logMsg = "->现在对导入的预测删除信息进行预处理,共有[" + (planTranList == null ? 0 : planTranList.size()) + "]条数据";
			logger.log(logMsg);
			
			
			//获取当前人员的采购组的权限
			  List<Long> ids = buyerOrgPermissionUtil.getGroupIds();
			  Map<Long, Long> idsMap = new HashMap<Long, Long>();
			  if(ids != null){
				  for(Long id : ids){
					  idsMap.put(id, id);
				   }
			  }
			
			
			counts[0] = planTranList.size();
			List<PurchasePlanDeleteTransfer> ret = new ArrayList<PurchasePlanDeleteTransfer>();
			
			List<UserEntity> buyerList=null; //采购员
			List<MaterialEntity> materialList = null; //物料
			List<FactoryEntity> factoryList = null ; //工厂
			List<PurchasingGroupEntity> groupList = null ; //采购组
			List<OrganizationEntity> vendorList = null ; //供应商
			
			boolean lineValidat = true;
			int index  = 2; 
			for(PurchasePlanDeleteTransfer trans : planTranList) {
				////每次循环之后重置start
				buyerList=null;
				materialList = null;
				factoryList = null;
				groupList = null;
				vendorList = null;
				////重置end

				if(StringUtils.isEmpty(trans.getFactoryCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],工厂编码不能为空,忽略此数据</font></b>");
				}
				
				
				if(StringUtils.isEmpty(trans.getGroupCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],采购组编码为空,忽略此数据</font></b>");
				}
				

			
				
				if(StringUtils.isEmpty(trans.getMaterialCode())){
					lineValidat = false;
					logger.log("-> <b><font color='red'>[FAILED]行索引[" + index + "],物料编码不能为空,忽略此数据</font></b>");
				}
				
				if(StringUtils.isEmpty(trans.getVendorCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],供应商编码不能为空,忽略此数据</font></b>");
				}
				
				
				//查询工厂
				if(!factoryMap.containsKey(trans.getFactoryCode())) {
					factoryList = factoryDao.findFactoryByCodeAndAbolished(trans.getFactoryCode(),0);
					if(CollectionUtils.isEmpty(factoryList)) {
						lineValidat = false;
						logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],工厂编码[" + trans.getFactoryCode() + "]未在系统中维护,或被禁用,忽略此预测计划</font></b>");
					} else {
						factoryMap.put(trans.getFactoryCode(), factoryList.get(0));
					}
				}
				
				
				
				//查询物料
				if(!materialMap.containsKey(trans.getMaterialCode())) {
					materialList = materialDao.findByCodeAndAbolished(trans.getMaterialCode(),0);
					if(CollectionUtils.isEmpty(materialList)) {
						lineValidat = false;
						logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],物料[" + trans.getMaterialCode() + "]未在系统中维护,或被禁用,忽略此预测计划</font></b>");
					} else {
						materialMap.put(trans.getMaterialCode(), materialList.get(0));
					}
				}
				
				//查询采购组
				if(!groupMap.containsKey(trans.getGroupCode())) {
					groupList = purchasingGroupDao.findByCodeAndAbolisheds(trans.getGroupCode(),0);
					if(CollectionUtils.isEmpty(groupList)) {
						lineValidat = false;
						logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],采购组[" + trans.getGroupCode() + "]未在系统中维护,或被禁用,忽略此预测计划</font></b>");
					} else {
						if(!idsMap.containsKey( groupList.get(0).getId())){
							lineValidat = false;
							logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],采购组[" + trans.getGroupCode() + "]的权限未在系统维护,忽略此预测计划 </font></b>");
						}
						groupMap.put(trans.getGroupCode(), groupList.get(0));
					}
				}
				
				//查询供应商
				if(!vendorMap.containsKey(trans.getVendorCode())) {
					vendorList = organizationDao.findByCodeAndAbolisheds(trans.getVendorCode(),0);
					if(CollectionUtils.isEmpty(vendorList)) {
						lineValidat = false;
						logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],供应商[" + trans.getVendorCode() + "]未在系统中维护,或被禁用,忽略此预测计划</font></b>");
					} else {
						vendorMap.put(trans.getVendorCode(), vendorList.get(0));
					}
				}
				
				
				//添加查询此工厂下面是否有这个物料
				List<VendorMaterialRelEntity>  vmRel =  vendorMaterialRelDao.findByMaterialCodeAndFactoryCodeAndAbolished(trans.getMaterialCode(),trans.getFactoryCode(),0);
				if(vmRel == null || vmRel.size() == 0){
					lineValidat = false;
					logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],工厂[" + trans.getFactoryCode() + "]  ，物料[" + trans.getMaterialCode() + "] 为维护此关系,忽略此预测计划</font></b>");
				}
		
				if(lineValidat) {
/*					logMsg = "[SUCCESS]行索引[" + index + "],预处理[ "+trans.getFactoryCode() +" | " + trans.getGroupCode() + "|" + trans.getMaterialCode() + "|]成功.";
					logger.log(logMsg);*/
					ret.add(trans);
				}
				index ++;
				
				lineValidat = true;
			}
			int count= ret.size();
			counts[1] = count;
			logMsg = "<-导入的预测删除信息预处理完毕,共有[" + count+ "]条有效数据";
			logger.log(logMsg);
			return ret;   
   }
	
	
	
	//匹配计划员
	public   Boolean matchDataSource(List<PurchasePlanTransfer> list,ILogger logger) throws IllegalArgumentException, IllegalAccessException{
		/*String logMsg = "->现在对导入的预测信息进行匹配处理,共有[" + (list == null ? 0 : list.size() - 1) + "]条数据";*/
		String logMsg="";
		
		//表头处理
		List<PurchasePlanTransfer> headList = new ArrayList<PurchasePlanTransfer>();//表头
		headList.add(list.get(0));
		list.removeAll(headList);//数据项
		
		//数据处理
		boolean flag = true;
		int index  = 2; 
		
		//将导入数据物料相同的物料进行组合
		Map<String,PurchasePlanTransfer> planMap = new HashMap<String,PurchasePlanTransfer>();//导入物料的集合map
		
		for(PurchasePlanTransfer tr : list){

			String key = tr.getFactoryCode() + tr.getMaterialCode() + tr.getGroupCode();
			if(planMap.containsKey(key)){
				//把数据相加
				PurchasePlanTransfer trTemp = planMap.get(key);
				if(tr.getCol1() == null || tr.getCol1() == "" ){
					tr.setCol1("0");
				}
				if(trTemp.getCol1() == null || trTemp.getCol1() ==""){
					trTemp.setCol1("0");
				}
				Double col1 = BigDecimalUtil.add(Double.valueOf(tr.getCol1()),Double.valueOf( trTemp.getCol1()));
				tr.setCol1(col1.toString());
				
				
				if(tr.getCol2() == null  || tr.getCol2() == ""){
					tr.setCol2("0");
				}
				if(trTemp.getCol2() == null || trTemp.getCol2() == ""){
					trTemp.setCol2("0");
				}
				Double col2 = BigDecimalUtil.add(Double.valueOf(tr.getCol2()),Double.valueOf( trTemp.getCol2()));
				tr.setCol2(col2.toString());
				
				
				if(tr.getCol3() == null || tr.getCol3() == ""){
					tr.setCol3("0");
				}
				if(trTemp.getCol3() == null || trTemp.getCol3() == ""){
					trTemp.setCol3("0");
				}
				Double col3 = BigDecimalUtil.add(Double.valueOf(tr.getCol3()),Double.valueOf( trTemp.getCol3()));
				tr.setCol3(col3.toString());
				
				if(tr.getCol4() == null || tr.getCol4() == ""){
					tr.setCol4("0");
				}
				if(trTemp.getCol4() == null || trTemp.getCol4() == ""){
					trTemp.setCol4("0");
				}
				Double col4 = BigDecimalUtil.add(Double.valueOf(tr.getCol4()),Double.valueOf( trTemp.getCol4()));
				tr.setCol4(col4.toString());
				
				
				
				if(tr.getCol5() == null || tr.getCol5() == ""){
					tr.setCol5("0");
				}
				if(trTemp.getCol5() == null || trTemp.getCol5() == ""){
					trTemp.setCol5("0");
				}
				Double col5 = BigDecimalUtil.add(Double.valueOf(tr.getCol5()),Double.valueOf( trTemp.getCol5()));
				tr.setCol5(col5.toString());
				
				if(tr.getCol6() == null || tr.getCol6() == ""){
					tr.setCol6("0");
				}
				if(trTemp.getCol6() == null || trTemp.getCol6() == ""){
					trTemp.setCol6("0");
				}
				Double col6 = BigDecimalUtil.add(Double.valueOf(tr.getCol6()),Double.valueOf( trTemp.getCol6()));
				tr.setCol6(col6.toString());
				
				if(tr.getCol7() == null || tr.getCol7() == ""){
					tr.setCol7("0");
				}
				if(trTemp.getCol7() == null || trTemp.getCol7() == ""){
					trTemp.setCol7("0");
				}
				Double col7 = BigDecimalUtil.add(Double.valueOf(tr.getCol7()),Double.valueOf( trTemp.getCol7()));
				tr.setCol7(col7.toString());
				
				
				if(tr.getCol8() == null || tr.getCol8() == ""){
					tr.setCol8("0");
				}
				if(trTemp.getCol8() == null || trTemp.getCol8() == ""){
					trTemp.setCol8("0");
				}
				Double col8 = BigDecimalUtil.add(Double.valueOf(tr.getCol8()),Double.valueOf( trTemp.getCol8()));
				tr.setCol8(col8.toString());
				
				if(tr.getCol9() == null || tr.getCol9() == ""){
					tr.setCol9("0");
				}
				if(trTemp.getCol9() == null || trTemp.getCol9() == ""){
					trTemp.setCol9("0");
				}
				Double col9 = BigDecimalUtil.add(Double.valueOf(tr.getCol9()),Double.valueOf( trTemp.getCol9()));
				tr.setCol9(col9.toString());
				
				
				if(tr.getCol10() == null || tr.getCol10() == ""){
					tr.setCol10("0");
				}
				if(trTemp.getCol10() == null || trTemp.getCol10() == ""){
					trTemp.setCol10("0");
				}
				Double col10 = BigDecimalUtil.add(Double.valueOf(tr.getCol10()),Double.valueOf( trTemp.getCol10()));
				tr.setCol10(col10.toString());
				
				
				if(tr.getCol11() == null || tr.getCol11() == ""){
					tr.setCol11("0");
				}
				if(trTemp.getCol11() == null || trTemp.getCol11() == ""){
					trTemp.setCol11("0");
				}
				Double col11 = BigDecimalUtil.add(Double.valueOf(tr.getCol11()),Double.valueOf( trTemp.getCol11()));
				tr.setCol11(col11.toString());
				
				if(tr.getCol12() == null || tr.getCol12() == ""){
					tr.setCol12("0");
				}
				if(trTemp.getCol12() == null || trTemp.getCol12() == ""){
					trTemp.setCol12("0");
				}
				Double col12 = BigDecimalUtil.add(Double.valueOf(tr.getCol12()),Double.valueOf( trTemp.getCol12()));
				tr.setCol12(col12.toString());
				
				
				if(tr.getCol13() == null || tr.getCol13() == ""){
					tr.setCol13("0");
				}
				if(trTemp.getCol13() == null || trTemp.getCol13() == "") {
					trTemp.setCol13("0");
				}
				Double col13 = BigDecimalUtil.add(Double.valueOf(tr.getCol13()),Double.valueOf( trTemp.getCol13()));
				tr.setCol13(col13.toString());
				
				
				if(tr.getCol14() == null || tr.getCol14() == ""){
					tr.setCol14("0");
				}
				if(trTemp.getCol14() == null || trTemp.getCol14() == ""){
					trTemp.setCol14("0");
				}
				Double col14 = BigDecimalUtil.add(Double.valueOf(tr.getCol14()),Double.valueOf( trTemp.getCol14()));
				tr.setCol14(col14.toString());
				
				if(tr.getCol15() == null || tr.getCol15() == ""){
					tr.setCol15("0");
				}
				if(trTemp.getCol15() == null || trTemp.getCol15() == ""){
					trTemp.setCol15("0");
				}
				Double col15 = BigDecimalUtil.add(Double.valueOf(tr.getCol15()),Double.valueOf( trTemp.getCol15()));
				tr.setCol15(col15.toString());
				
				
				if(tr.getCol16() == null || tr.getCol16() == ""){
					tr.setCol16("0");
				}
				if(trTemp.getCol16() == null || trTemp.getCol16() == ""){
					trTemp.setCol16("0");
				}
				Double col16 = BigDecimalUtil.add(Double.valueOf(tr.getCol16()),Double.valueOf( trTemp.getCol16()));
				tr.setCol16(col16.toString());
				
				if(tr.getCol17() == null || tr.getCol17() == ""){
					tr.setCol17("0");
				}
				if(trTemp.getCol17() == null || trTemp.getCol17() == ""){
					trTemp.setCol17("0");
				}
				Double col17= BigDecimalUtil.add(Double.valueOf(tr.getCol17()),Double.valueOf( trTemp.getCol17()));
				tr.setCol17(col17.toString());
				
				if(tr.getCol18() == null || tr.getCol18() == ""){
					tr.setCol18("0");
				}
				if(trTemp.getCol18() == null || trTemp.getCol18() == ""){
					trTemp.setCol18("0");
				}
				Double col18 = BigDecimalUtil.add(Double.valueOf(tr.getCol18()),Double.valueOf( trTemp.getCol18()));
				tr.setCol18(col18.toString());
				
				
				if(tr.getCol19() == null || tr.getCol19() == ""){
					tr.setCol19("0");
				}
				if(trTemp.getCol19() == null || trTemp.getCol19() == ""){
					trTemp.setCol19("0");
				}
				Double col19 = BigDecimalUtil.add(Double.valueOf(tr.getCol19()),Double.valueOf( trTemp.getCol19()));
				tr.setCol19(col19.toString());
				
				
				if(tr.getCol20() == null || tr.getCol20() == ""){
					tr.setCol20("0");
				}
				if(trTemp.getCol20() == null || trTemp.getCol20() == ""){
					trTemp.setCol20("0");
				}
				Double col20 = BigDecimalUtil.add(Double.valueOf(tr.getCol20()),Double.valueOf( trTemp.getCol20()));
				tr.setCol20(col20.toString());
				
				
				if(tr.getCol21() == null || tr.getCol21() == ""){
					tr.setCol21("0");
				}
				if(trTemp.getCol21() == null || trTemp.getCol21() == ""){
					trTemp.setCol21("0");
				}
				Double col21 = BigDecimalUtil.add(Double.valueOf(tr.getCol21()),Double.valueOf( trTemp.getCol21()));
				tr.setCol21(col21.toString());
				
				
				if(tr.getCol22() == null || tr.getCol22() == ""){
					tr.setCol22("0");
				}
				if(trTemp.getCol22() == null || trTemp.getCol22() == ""){
					trTemp.setCol22("0");
				}
				Double col22 = BigDecimalUtil.add(Double.valueOf(tr.getCol22()),Double.valueOf( trTemp.getCol22()));
				tr.setCol22(col22.toString());
				
				if(tr.getCol23() == null || tr.getCol23() == ""){
					tr.setCol23("0");
				}
				if(trTemp.getCol23() == null || trTemp.getCol23() == ""){
					trTemp.setCol23("0");
				}
				Double col23 = BigDecimalUtil.add(Double.valueOf(tr.getCol23()),Double.valueOf( trTemp.getCol23()));
				tr.setCol23(col23.toString());
				
				if(tr.getCol24() == null || tr.getCol24() == ""){
					tr.setCol24("0");
				}
				if(trTemp.getCol24() == null || trTemp.getCol24() == "") {
					trTemp.setCol24("0");
				}
				Double col24 = BigDecimalUtil.add(Double.valueOf(tr.getCol24()),Double.valueOf( trTemp.getCol24()));
				tr.setCol23(col24.toString());
				
				planMap.put(key, tr);
			}else{
				planMap.put(key, tr);
			}
			
			
		}
		
		// 将map 装换为list 
		List<PurchasePlanTransfer> newList =new  ArrayList<PurchasePlanTransfer>();
		Iterator it = planMap.keySet().iterator();
		while (it.hasNext()) {  
	           String key = it.next().toString();  
	           newList.add(planMap.get(key));  
	       }  
		
		//获取大版本
		Date date = new Date();
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);
	    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
		
		
		for(PurchasePlanTransfer tr : newList){
			List<PurchaseTotalPlanHeadEntity>  sourceHeadList = purchaseTotalPlanHeadDao.findPurchaseTotalPlanItemEntityByFactoryAndMaterialAndGroup(tr.getFactoryCode(),tr.getMaterialCode(),tr.getGroupCode(),month);
			Map<String,String> newMap = new HashMap<String,String>();
			if(sourceHeadList != null && sourceHeadList.size() != 0){
				//数据的处理（校验数据的总量）
				for(PurchaseTotalPlanHeadEntity head :    sourceHeadList ){
					if(newMap.containsKey(head.getHeaderName())){
						String tmp = head.getHeaderValues();
						if(tmp ==null || tmp==""){
							tmp = "0";
						}else{
							tmp =  head.getHeaderValues();
						}
						
						Double newValues =  BigDecimalUtil.add(Double.valueOf(tmp),Double.valueOf(newMap.get(head.getHeaderName())));
						newMap.put(head.getHeaderName(), newValues.toString());
					}else{
						String tmp = head.getHeaderValues();
						if(tmp ==null || tmp==""){
							tmp = "0";
						}else{
							tmp =  head.getHeaderValues();
						}
						newMap.put(head.getHeaderName(), tmp);
					}
				}
				//重组校验
				flag = checkImportData(tr,headList,newMap,logger,index,logMsg);
				if(!flag){
					break;
				}
			}else{
				logger.log(logMsg = "-><b><font color='red'> 工厂[" + tr.getFactoryCode() + "]和物料[" + tr.getMaterialCode() + "] 未在系统预测计划员中找到，请联系计划员！</font></b>");
				flag = false;
				break;
			}
			index ++;
		}
		logger.log(logMsg);
		return flag;
	}
	
	//重组校验
	public Boolean checkImportData(PurchasePlanTransfer data,List<PurchasePlanTransfer> headList,Map<String, String> newMap,ILogger logger,int index,String logMsg) throws IllegalArgumentException, IllegalAccessException{
		PurchasePlanTransfer headerTransfer = headList.get(0);
		List<PurchasePlanHeadEntity> newPurchasePlanHeadList = new ArrayList<PurchasePlanHeadEntity>();
		boolean flag = true;
		
		Class clazz1 = data.getClass();  
		Class clazz2 = headerTransfer.getClass();
      
		Field[] fs1 = clazz1.getDeclaredFields();  
		Field[] fs2 = clazz2.getDeclaredFields();  
        for(int i = 6 ; i < 30; i++){ 
        	Field f1 = fs1[i];
        	f1.setAccessible(true); //设置些属性是可以访问的  
            Object val1 = f1.get(data);//得到此属性的值  
        	for(int j = 0; j < fs2.length;j++){
        		Field f2 = fs1[j];
        		f2.setAccessible(true); //设置些属性是可以访问的  
	            Object val2 = f2.get(headerTransfer);//得到此属性的值  
	            if(f1.getName().equals(f2.getName())){
	            	PurchasePlanHeadEntity purchasePlanHead=new PurchasePlanHeadEntity();
	            	purchasePlanHead.setHeaderName(val2 == null   || val2 == ""  ? "0" : val2.toString());
					purchasePlanHead.setHeaderValues( val1 == null || val1 == "" ? "0" : val1.toString());
					newPurchasePlanHeadList.add(purchasePlanHead);
					break;
	            }
        	}
        }
        //对比数据
        for(PurchasePlanHeadEntity head  : newPurchasePlanHeadList){
        	if(newMap.containsKey(head.getHeaderName())){
        		String we = newMap.get(head.getHeaderName());
        		String er = head.getHeaderValues();
        		if(( !StringUtils.isEmpty(we)  && !StringUtils.isEmpty(er))){
        			//判断数据是否相同
            		if(BigDecimalUtil.sub(Double.valueOf(newMap.get(head.getHeaderName())), Double.valueOf(head.getHeaderValues())) == 0){
                         continue;
            		}else{
            			logger.log(logMsg = "-><b><font color='red'>工厂[" + data.getFactoryCode()+ "]和物料[" + data.getMaterialCode() + "]日期为["+head.getHeaderName()+"]的数量["+Double.valueOf(head.getHeaderValues())+"]与计划员数量["+Double.valueOf(newMap.get(head.getHeaderName()))+"]不匹配,请修改预测计划</font></b>");
            			flag = false;
            			break;
            		}
        		}
        	}
        }
        return flag;
	}
	
	
	
	/**
	 * 获取当前大版本的第一个星期一
	 * @param plans
	 */
	protected Date getVersionMonday(String dateString) {  
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
    		date = sdf.parse(dateString);  
    	} catch (Exception   e) {
    	    e.printStackTrace();
    	}
		while(true){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int week = c.get(Calendar.DAY_OF_WEEK);		
			if(week == 2){
				return date;
			}
			Date d = new Date();
			d.setTime(date.getTime() + 24*60*60*1000);
			date = d;
		}
	}
	
    //获取七天后的日期
    public  Date getSeventDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +7);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

	
    
	/**
	 * 删除计划(作废当前数据)
	 * @param plans
	 */
	public void deletePlanItem(Long id) {
		purchasePlanItemDao.invalidPurchasePlanItem(id); 
		purchasePlanHeadDao.invalidPurchasePlanHead(id);
	}
    
    
	/**
	 *导出
	 * @param plans
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(Map<String, Object> searchParamMap,Long planid,HttpServletResponse response) throws Exception {
		//获取表头的数据
		PurchasePlanItemHeadVO head = getItemHead(planid,null,null);
		//获取内容数据
		List<PurchasePlanItemEntity>  contentList= exportExcelList(searchParamMap);
		
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("预测计划");  
        
		for(int i = 0 ; i < 39 ; i++ ){
			sheet.setColumnWidth(i, 120*35);
		}
		
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        style.setWrapText(true);  
  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("工厂编码");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 1);
        cell.setCellValue("工厂名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("采购组编码");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("采购组名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("供应商简称"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("供应商编码");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("物料号");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("物料描述"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("单位");  
        cell.setCellStyle(style);
        
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("当日库存"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 10);  
        cell.setCellValue("当日未交PO数");  
        cell.setCellStyle(style);
        
        
        //动态表头加载项
        
        cell = row.createCell((short) 11);  
        cell.setCellValue(head.getCol1());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 12);  
        cell.setCellValue(head.getCol2()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 13);  
        cell.setCellValue(head.getCol3());  
        cell.setCellStyle(style);
        
        
        cell = row.createCell((short) 14);  
        cell.setCellValue(head.getCol4());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 15);  
        cell.setCellValue(head.getCol5()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 16);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 17);  
        cell.setCellValue(head.getCol7());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 18);  
        cell.setCellValue(head.getCol8()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 19);  
        cell.setCellValue(head.getCol9());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 20);  
        cell.setCellValue(head.getCol10());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 21);  
        cell.setCellValue(head.getCol11()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 22);  
        cell.setCellValue(head.getCol12());  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 23);  
        cell.setCellValue(head.getCol13());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 24);  
        cell.setCellValue(head.getCol4()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 25);  
        cell.setCellValue(head.getCol15());  
        cell.setCellStyle(style);
          
        
        cell = row.createCell((short) 26);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 27);  
        cell.setCellValue(head.getCol17()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 28);  
        cell.setCellValue(head.getCol18());  
        cell.setCellStyle(style);
        
        
        
        
        
        cell = row.createCell((short) 29);  
        cell.setCellValue(head.getCol19());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 30);  
        cell.setCellValue(head.getCol20()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 31);  
        cell.setCellValue(head.getCol21());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 32);  
        cell.setCellValue(head.getCol22());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 33);  
        cell.setCellValue(head.getCol23()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 34);  
        cell.setCellValue(head.getCol24());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 35);  
        cell.setCellValue(head.getCol25());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 36);  
        cell.setCellValue(head.getCol26()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 37);  
        cell.setCellValue(head.getCol27());  
        cell.setCellStyle(style);
        
        
        
        cell = row.createCell((short) 38);  
        cell.setCellValue(head.getCol28());  
        cell.setCellStyle(style);  
        

        
        
        
        
  
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
  
        for (int i = 0; i < contentList.size(); i++){  
            row = sheet.createRow((int) i + 1);  
            PurchasePlanItemEntity data =contentList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(data.getFactory().getCode());  
            row.createCell((short) 1).setCellValue(data.getFactory().getName());  
            row.createCell((short) 2).setCellValue(data.getGroup() == null  ? ""  : data.getGroup().getCode());
            row.createCell((short) 3).setCellValue(data.getGroup()  == null ? "":  data.getGroup().getName());  
            row.createCell((short) 4).setCellValue(data.getVendor().getName());  
            row.createCell((short) 5).setCellValue(data.getVendor().getCode());
            row.createCell((short) 6).setCellValue(data.getMaterial().getCode());  
            row.createCell((short) 7).setCellValue(data.getMaterial().getName());  
            row.createCell((short) 8).setCellValue(data.getMaterial().getUnit()); 
            row.createCell((short) 9).setCellValue(data.getDayStock()); 
            row.createCell((short) 10).setCellValue(data.getUnpaidQty());  
            
            row.createCell((short) 11).setCellValue(data.getPurchasePlanItemHeadVO().getCol1()); 
            row.createCell((short) 12).setCellValue(data.getPurchasePlanItemHeadVO().getCol2());  
            row.createCell((short) 13).setCellValue(data.getPurchasePlanItemHeadVO().getCol3());  
            row.createCell((short) 14).setCellValue(data.getPurchasePlanItemHeadVO().getCol4());  
            row.createCell((short) 15).setCellValue(data.getPurchasePlanItemHeadVO().getCol5()); 
            row.createCell((short) 16).setCellValue(data.getPurchasePlanItemHeadVO().getCol6());  
            row.createCell((short) 17).setCellValue(data.getPurchasePlanItemHeadVO().getCol7());  
            row.createCell((short) 18).setCellValue(data.getPurchasePlanItemHeadVO().getCol8());  
            row.createCell((short) 19).setCellValue(data.getPurchasePlanItemHeadVO().getCol9()); 
            row.createCell((short) 20).setCellValue(data.getPurchasePlanItemHeadVO().getCol10());  
            row.createCell((short) 21).setCellValue(data.getPurchasePlanItemHeadVO().getCol11());  
            row.createCell((short) 22).setCellValue(data.getPurchasePlanItemHeadVO().getCol12());  
            row.createCell((short) 23).setCellValue(data.getPurchasePlanItemHeadVO().getCol13()); 
            row.createCell((short) 24).setCellValue(data.getPurchasePlanItemHeadVO().getCol14());  
            row.createCell((short) 25).setCellValue(data.getPurchasePlanItemHeadVO().getCol15());  
            row.createCell((short) 26).setCellValue(data.getPurchasePlanItemHeadVO().getCol16());  
            row.createCell((short) 27).setCellValue(data.getPurchasePlanItemHeadVO().getCol17()); 
            row.createCell((short) 28).setCellValue(data.getPurchasePlanItemHeadVO().getCol18());  
            row.createCell((short) 29).setCellValue(data.getPurchasePlanItemHeadVO().getCol19());  
            row.createCell((short) 30).setCellValue(data.getPurchasePlanItemHeadVO().getCol20());  
            row.createCell((short) 31).setCellValue(data.getPurchasePlanItemHeadVO().getCol21()); 
            row.createCell((short) 32).setCellValue(data.getPurchasePlanItemHeadVO().getCol22());  
            row.createCell((short) 33).setCellValue(data.getPurchasePlanItemHeadVO().getCol23());  
            row.createCell((short) 34).setCellValue(data.getPurchasePlanItemHeadVO().getCol24());  
            row.createCell((short) 35).setCellValue(data.getPurchasePlanItemHeadVO().getCol25()); 
            row.createCell((short) 36).setCellValue(data.getPurchasePlanItemHeadVO().getCol26());  
            row.createCell((short) 37).setCellValue(data.getPurchasePlanItemHeadVO().getCol27());  
            row.createCell((short) 38).setCellValue(data.getPurchasePlanItemHeadVO().getCol28());  

        }
		wb.write(response.getOutputStream());

	}
	
    
	public List<PurchasePlanItemEntity> exportExcelList(Map<String, Object> searchParamMap) throws Exception{
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" QEWEB_PURCHASE_PLAN_ITEM A");
		sql.append(" LEFT JOIN QEWEB_ORGANIZATION B ON A.VENDOR_ID = B. ID");
		sql.append(" LEFT JOIN QEWEB_MATERIAL C ON A.MATERIAL_ID = C. ID  ");
		sql.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID = D. ID  ");
		sql.append(" LEFT JOIN QEWEB_USER E ON A.CREATE_USER_ID = E. ID");
		sql.append(" WHERE ");
		sql.append(" A.ABOLISHED = 0");
		sql.append(" AND  A.VENDOR_ID = "+user.orgId+" ");
		sql.append(" AND  A.IS_NEW = 1 ");
		sql.append("AND  A.PLAN_ID = "+searchParamMap.get("EQ_plan.id")+"");
		if(searchParamMap.containsKey("EQ_plan.buyer.name")){
			sql.append(" AND E.NAME like '%"+searchParamMap.get("EQ_plan.buyer.name")+"%'");//采购员
		}
		if(searchParamMap.containsKey("EQ_confirmStatus")){
			sql.append(" AND A.CONFIRM_STATUS like '%"+searchParamMap.get("EQ_confirmStatus")+"%'");//确认状态
		}
		if(searchParamMap.containsKey("LIKE_vendor.code")){
			sql.append(" AND B.CODE like '%"+searchParamMap.get("LIKE_vendor.code")+"%'");//供应商编码
		}
		if(searchParamMap.containsKey("LIKE_vendor.name")){
			sql.append(" AND B.NAME like '%"+searchParamMap.get("LIKE_vendor.name")+"%'");//供应商名称
		}
		if(searchParamMap.containsKey("LIKE_material.code")){
			sql.append(" AND C.CODE like '%"+searchParamMap.get("LIKE_material.code")+"%'");//物料号
		}
		if(searchParamMap.containsKey("EQ_publishStatus")){
			sql.append(" AND A.PUBLISH_STATUS like '%"+searchParamMap.get("EQ_publishStatus")+"%'");//发布状态
		}
		if(searchParamMap.containsKey("EQ_isNew")){
			sql.append(" AND A.IS_NEW like '%"+searchParamMap.get("EQ_isNew")+"%'");//有效状态
		}
		if(searchParamMap.containsKey("EQ_versionNumber")){
			sql.append(" AND A.VERSION_NUMBER like '%"+searchParamMap.get("EQ_versionNumber")+"%'");//版本号
		}
		if(searchParamMap.containsKey("EQ_factory.name")){
			sql.append(" AND D.NAME like '%"+searchParamMap.get("EQ_factory.name")+"%'");//工厂
		}
		if(searchParamMap.containsKey("EQ_vendor.id")){
			sql.append(" AND B.ID = "+searchParamMap.get("EQ_vendor.id")+"");//供应商iD
		}
		
		//add by chao.gu 20170829 待办ID
		if(searchParamMap.containsKey("IN_id") && !StringUtils.isEmpty(searchParamMap.get("IN_id").toString())){
			List<Long> idList =(List<Long>) searchParamMap.get("IN_id");
			StringBuffer idStr = new StringBuffer();
			for (Long long1 : idList) {
				idStr.append(long1+"").append(",");
			}
			if(idStr.length()>0){
				sql.append(" AND A.ID IN ("+idStr.substring(0, idStr.length()-1).toString()+")");
			}
		}
		//add end
		
		sql.append(" ORDER BY  ");
		sql.append(" A.CREATE_TIME ASC");
		String sql1="SELECT A.* "+sql.toString();
		String sqlTotalCount="select count(1) "+sql.toString();
		List<?> list = generialDao.queryBySql_map(sql1);
		List<PurchasePlanItemEntity> purchasePlanItemList=new ArrayList<PurchasePlanItemEntity>();
		int totalCount=generialDao.findCountBySql(sqlTotalCount);
		if(null!=list && list.size()>0){
			for(Object purchasePlanItem : list){
				Map<String,Object> m =  (Map<String, Object>) purchasePlanItem;
				PurchasePlanItemEntity entity=purchasePlanItemDao.findOne(((BigDecimal)m.get("ID")).longValue());
				if( entity != null){
					purchasePlanItemList.add(entity);
				}
			}
		}
		
		//获取选择大版本的第一个星期一
    	PurchasePlanEntity purchasePlan = new PurchasePlanEntity();
    	purchasePlan = purchasePlanDao.findOne(Long.parseLong(searchParamMap.get("EQ_plan.id").toString()));
    	String version = purchasePlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
    	//过滤查询条件（时间）
/*    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(searchParamMap.containsKey("EQ_startDate") && searchParamMap.get("EQ_startDate") != null && searchParamMap.get("EQ_startDate") != ""){
    		start = sdf.parse(searchParamMap.get("EQ_startDate").toString());  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(searchParamMap.containsKey("EQ_endDate") && searchParamMap.get("EQ_endDate") != null && searchParamMap.get("EQ_endDate") != "" ){
        	end   = sdf.parse(searchParamMap.get("EQ_endDate").toString());  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}
    	*/
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
        /*    if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){*/
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
          /*  }*/
            date=getSeventDay(date);
        }
		


		//重新加载list
		List<PurchasePlanItemEntity> viewPurchasePlanItemList=new ArrayList<PurchasePlanItemEntity>();
		for(PurchasePlanItemEntity purchasePlanItem : purchasePlanItemList){
			
			purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
			
			
			PurchasePlanItemHeadVO tmpPurchasePlanItemHeadVO = purchasePlanItemHeadVO;
			Class clazz1 = tmpPurchasePlanItemHeadVO.getClass();  
			Object object1 = clazz1.newInstance();
			Field[] fields1 = clazz1.getDeclaredFields();  
			
			PurchasePlanItemEntity item = purchasePlanItem;
			Set<PurchasePlanHeadEntity> headList= purchasePlanItem.getPurchasePlanHeadEntity();
			for(PurchasePlanHeadEntity purchasePlanHead : headList ){	
			    for(int i = 0 ; i < fields1.length; i++){ 
			    	Field f1 = fields1[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(tmpPurchasePlanItemHeadVO);//得到此属性的值  
			        if(purchasePlanHead.getHeaderName().equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz1.getMethod(methodStr,new Class[]{f1.getType()});
		            	 method.invoke(object1,purchasePlanHead.getHeaderValues());
		            	 break;
					}
			    }			
			}
			
			PurchasePlanItemHeadVO tempVo = new PurchasePlanItemHeadVO();
			BeanUtil.copyPropertyNotNull((PurchasePlanItemHeadVO) object1,tempVo);
			item.setPurchasePlanItemHeadVO(tempVo);
			item.setHeadVO(purchasePlanItemHeadVO);
			viewPurchasePlanItemList.add(item);
		}
		return viewPurchasePlanItemList;
	}
    
	
	
	
	
	
	
	
	
	
	
	public   Boolean checkDataSource(List<PurchasePlanTransfer> list,ILogger logger) throws IllegalArgumentException, IllegalAccessException{
		String logMsg = "->现在对导入的预测信息进行有效数据检验,共有[" + (list == null ? 0 : list.size() - 1) + "]条数据";
		
		//表头处理
		List<PurchasePlanTransfer> headList = new ArrayList<PurchasePlanTransfer>();//表头
		headList.add(list.get(0));
		list.removeAll(headList);//数据项
		
		//数据处理
		boolean flag = true;
		//将导入数据物料相同的物料进行组合
		Map<String,PurchasePlanTransfer> planMap = new HashMap<String,PurchasePlanTransfer>();//导入物料的集合map
		
		String key = null;
		for(PurchasePlanTransfer plan  : list){
			key = plan.getFactoryCode()+"," + plan.getMaterialCode()+","+plan.getGroupCode()+","+plan.getVendorCode();
			if(!planMap.containsKey(key)){
				planMap.put(key, plan);
			}else{
				flag = false;
				logger.log(logMsg = "-><b><font color='red'>[FAILED] 数据存在，工厂[" + plan.getFactoryCode() + "]，物料[" + plan.getMaterialCode() + "] ,采购组["+plan.getGroupCode()+"],供应商["+plan.getVendorCode()+"]有重复的数据，请修改预测计划！</font></b>");
			    break;
			}
		}
		return flag;
	}
	
	/**
	 * 根据明细ID列表获取大版本ID(待办用)【采】
	 * @author chao.gu
	 * @param itemIds
	 * @return
	 */
	public List<Long> findPurchasePlanIdsByItemIds(List<Long> itemIds){
		return purchasePlanItemDao.findPurchasePlanIdsByItemIds(itemIds);
	}
	
	/**
	 * 根据明细ID列表获取供应商大版本ID(待办用)【供】
	 * @author chao.gu
	 * @param itemIds
	 * @return
	 */
	public List<Long> findPurchasePlanVendorIdsByItemIds(List<Long> itemIds){
		List<Long> planIds = findPurchasePlanIdsByItemIds(itemIds);
		if(null!=planIds && planIds.size()>0){
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			return purchasePlanVendorDao.findPurchasePlanVendorIdsByPlanIds(planIds,user.orgId);
		}
		return null;
	}
	
	
	/**
	 * 根据供应商预测计划
	 * @param purchasePlanVendorId
	 * @return
	 */
	public Long findPlanIdByPlanVendorId(Long purchasePlanVendorId){
		PurchasePlanVendorEntity planVendor = purchasePlanVendorDao.findById(purchasePlanVendorId);
	    if(null!=planVendor){
	    	return planVendor.getPlan().getId();
	    }
	    return null;
	}
	
	
public List<PurchaseTotalPlanItemEntity> exportExcelPlanList(Map<String, Object> searchParamMap) throws Exception {
		
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseTotalPlanItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseTotalPlanItemEntity.class);
		List<PurchaseTotalPlanItemEntity> purchasePlanItemList = purchaseTotalPlanItemDao.findAll(spec);  
		
		
	
		
		
		//获取选择大版本的第一个星期一
    	String version = searchParamMap.get("EQ_plan.month").toString();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
        	 if(field.getType().getSimpleName().equals("String")){
                 method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
             }
            date=getSeventDay(date);
        }
		


		//重新加载list
		List<PurchaseTotalPlanItemEntity> purchaseTotalPlanItemList=new ArrayList<PurchaseTotalPlanItemEntity>();
		for(PurchaseTotalPlanItemEntity purchaseTotalPlanItem : purchasePlanItemList){
			
			purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
			
			
			PurchasePlanItemHeadVO tmpPurchasePlanItemHeadVO =purchasePlanItemHeadVO ;
			Class clazz1 = tmpPurchasePlanItemHeadVO.getClass();  
			Object object1 = clazz1.newInstance();
			Field[] fields1 = clazz1.getDeclaredFields();  
			
			PurchaseTotalPlanItemEntity item = purchaseTotalPlanItem;
			List<PurchaseTotalPlanHeadEntity>   headList =    purchaseTotalPlanHeadDao.findPurchasePlanHeadByplanItemId(purchaseTotalPlanItem.getId());
			tmpPurchasePlanItemHeadVO = purchasePlanItemHeadVO;
			for(PurchaseTotalPlanHeadEntity purchaseTotalPlanHead : headList ){	
			    for(int i = 0 ; i < fields1.length; i++){ 
			    	Field f1 = fields1[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(tmpPurchasePlanItemHeadVO);//得到此属性的值  
			        if(purchaseTotalPlanHead.getHeaderName().equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz1.getMethod(methodStr,new Class[]{f1.getType()});
		            	 method.invoke(object1,purchaseTotalPlanHead.getHeaderValues());
		            	 break;
					}
			    }	
			}
			PurchasePlanItemHeadVO tempVo = new PurchasePlanItemHeadVO();
		
			BeanUtil.copyPropertyNotNull((PurchasePlanItemHeadVO) object1,tempVo);

			item.setPurchasePlanItemHeadVO(tempVo);
			item.setHeadVO(purchasePlanItemHeadVO);
			purchaseTotalPlanItemList.add(item);
		}
 		return purchaseTotalPlanItemList;	
	}
	
	
	
	
	public void exportExcelForPlanner(Map<String, Object> searchParamMap,String month,HttpServletResponse response) throws Exception {
		//获取表头的数据

    	String version = searchParamMap.get("EQ_plan.month").toString();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO head = new PurchasePlanItemHeadVO();    	
		Class clazz = head.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});

            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
            date=getSeventDay(date);
        }
		
		head =(PurchasePlanItemHeadVO) object;
		
		
		
		//获取内容数据
		List<PurchaseTotalPlanItemEntity>  contentList= exportExcelPlanList(searchParamMap);
		
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        
        HSSFSheet sheet = wb.createSheet("预测计划总量"); 
		for(int i = 0 ; i < 37 ; i++ ){
			sheet.setColumnWidth(i, 120*35);
		}
		
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        style.setWrapText(true);  
  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("工厂编码");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("工厂名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("采购组编码"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("采购组名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("物料号");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("物料描述"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("单位"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("当日库存"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("当日未交PO数");  
        cell.setCellStyle(style);
        
        

        //动态表头加载项
        
        cell = row.createCell((short) 9);  
        cell.setCellValue(head.getCol1());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 10);  
        cell.setCellValue(head.getCol2()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 11);  
        cell.setCellValue(head.getCol3());  
        cell.setCellStyle(style);
        
        
        cell = row.createCell((short) 12);  
        cell.setCellValue(head.getCol4());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 13);  
        cell.setCellValue(head.getCol5()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 14);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 15);  
        cell.setCellValue(head.getCol7());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 16);  
        cell.setCellValue(head.getCol8()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 17);  
        cell.setCellValue(head.getCol9());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 18);  
        cell.setCellValue(head.getCol10());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 19);  
        cell.setCellValue(head.getCol11()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 20);  
        cell.setCellValue(head.getCol12());  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 21);  
        cell.setCellValue(head.getCol13());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 22);  
        cell.setCellValue(head.getCol4()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 23);  
        cell.setCellValue(head.getCol15());  
        cell.setCellStyle(style);
          
        
        cell = row.createCell((short) 24);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 25);  
        cell.setCellValue(head.getCol17()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 26);  
        cell.setCellValue(head.getCol18());  
        cell.setCellStyle(style);
        
        
        
        
        
        cell = row.createCell((short) 27);  
        cell.setCellValue(head.getCol19());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 28);  
        cell.setCellValue(head.getCol20()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 29);  
        cell.setCellValue(head.getCol21());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 30);  
        cell.setCellValue(head.getCol22());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 31);  
        cell.setCellValue(head.getCol23()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 32);  
        cell.setCellValue(head.getCol24());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 33);  
        cell.setCellValue(head.getCol25());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 34);  
        cell.setCellValue(head.getCol26()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 35);  
        cell.setCellValue(head.getCol27());  
        cell.setCellStyle(style);
        
        
        
        cell = row.createCell((short) 36);  
        cell.setCellValue(head.getCol28());  
        cell.setCellStyle(style);  
        

        
        
        
        
  
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
  
        for (int i = 0; i < contentList.size(); i++){  
            row = sheet.createRow((int) i + 1);  
            PurchaseTotalPlanItemEntity data =contentList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(data.getFactory().getCode());  
            row.createCell((short) 1).setCellValue(data.getFactory().getName());  
            
            row.createCell((short) 2).setCellValue(data.getPurchasingGroup()  == null ? "" :    data.getPurchasingGroup().getCode());  
            row.createCell((short) 3).setCellValue(data.getPurchasingGroup()  == null ? "" :    data.getPurchasingGroup().getName());  

            row.createCell((short) 4).setCellValue(data.getMaterial().getCode()); 
            row.createCell((short) 5).setCellValue(data.getMaterial().getName()); 
            row.createCell((short) 6).setCellValue(data.getMaterial().getUnit()); 
            
            row.createCell((short) 7).setCellValue(data.getDayStock()); 
            row.createCell((short) 8).setCellValue(data.getUnpaidQty());  
            
            row.createCell((short) 9).setCellValue(data.getPurchasePlanItemHeadVO().getCol1()); 
            row.createCell((short) 10).setCellValue(data.getPurchasePlanItemHeadVO().getCol2());  
            row.createCell((short) 11).setCellValue(data.getPurchasePlanItemHeadVO().getCol3());  
            row.createCell((short) 12).setCellValue(data.getPurchasePlanItemHeadVO().getCol4());  
            row.createCell((short) 13).setCellValue(data.getPurchasePlanItemHeadVO().getCol5()); 
            row.createCell((short) 14).setCellValue(data.getPurchasePlanItemHeadVO().getCol6());  
            row.createCell((short) 15).setCellValue(data.getPurchasePlanItemHeadVO().getCol7());  
            row.createCell((short) 16).setCellValue(data.getPurchasePlanItemHeadVO().getCol8());  
            row.createCell((short) 17).setCellValue(data.getPurchasePlanItemHeadVO().getCol9()); 
            row.createCell((short) 18).setCellValue(data.getPurchasePlanItemHeadVO().getCol10());  
            row.createCell((short) 19).setCellValue(data.getPurchasePlanItemHeadVO().getCol11());  
            row.createCell((short) 20).setCellValue(data.getPurchasePlanItemHeadVO().getCol12());  
            row.createCell((short) 21).setCellValue(data.getPurchasePlanItemHeadVO().getCol13()); 
            row.createCell((short) 22).setCellValue(data.getPurchasePlanItemHeadVO().getCol14());  
            row.createCell((short) 23).setCellValue(data.getPurchasePlanItemHeadVO().getCol15());  
            row.createCell((short) 24).setCellValue(data.getPurchasePlanItemHeadVO().getCol16());  
            row.createCell((short) 25).setCellValue(data.getPurchasePlanItemHeadVO().getCol17()); 
            row.createCell((short) 26).setCellValue(data.getPurchasePlanItemHeadVO().getCol18());  
            row.createCell((short) 27).setCellValue(data.getPurchasePlanItemHeadVO().getCol19());  
            row.createCell((short) 28).setCellValue(data.getPurchasePlanItemHeadVO().getCol20());  
            row.createCell((short) 29).setCellValue(data.getPurchasePlanItemHeadVO().getCol21()); 
            row.createCell((short) 30).setCellValue(data.getPurchasePlanItemHeadVO().getCol22());  
            row.createCell((short) 31).setCellValue(data.getPurchasePlanItemHeadVO().getCol23());  
            row.createCell((short) 32).setCellValue(data.getPurchasePlanItemHeadVO().getCol24());  
            row.createCell((short) 33).setCellValue(data.getPurchasePlanItemHeadVO().getCol25()); 
            row.createCell((short) 34).setCellValue(data.getPurchasePlanItemHeadVO().getCol26());  
            row.createCell((short) 35).setCellValue(data.getPurchasePlanItemHeadVO().getCol27());  
            row.createCell((short) 36).setCellValue(data.getPurchasePlanItemHeadVO().getCol28());  

        }
		wb.write(response.getOutputStream());

	}
	
	
	
	
	
	
	
	
	
/*	public List<PurchasePlanTransfer> getPurchasePlanVo(Map<String, Object> searchParamMap) {
		List<PurchasePlanVenodrItemEntity> list = getPurchasePlanVendorItems(searchParamMap);
		if(CollectionUtils.isEmpty(list)) {
+			return null;
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
			trans.setUnitName(item.getUnitName());
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
	
	*//**
	 * 修改采购计划
	 * @param purchasePlan
	 * @param purchasePlanItem
	 * @param userEntity
	 *//*
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
			itemEntity.setPlanRecTime(item.getPlanRecTime());
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

	*//**
	 * 发布才计划
	 * @param plans
	 *//*
	public void publishPlans(List<PurchasePlanEntity> plans) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(PurchasePlanEntity plan : plans) {
			plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			plan.setPublishTime(DateUtil.getCurrentTimestamp());
			plan.setPublishUser(new UserEntity(user.id));
		}
		
		purchasePlanDao.save(plans); 
	}
	

	
	*//**
	 * 转换并保存
	 * @param planTranList
	 * @param logger
	 * @throws Exception
	 *//*
	public boolean combinePurchasePlan(List<PurchasePlanTransfer> planTranList, ILogger logger) throws Exception {
		//供应商
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		//采购商
		Map<String, OrganizationEntity> buyerMap = new HashMap<String, OrganizationEntity>();
		//物料
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchasePlanTransfer> list = validateTransfers(planTranList, orgMap,buyerMap, materialMap, counts, logger);
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
				purchasePlan.setBuyer(buyerMap.get(trans.getBuyerCode()));
				purchasePlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				tmpMap.put(key, purchasePlan);
			} else {
				purchasePlan = tmpMap.get(key);
			}
			//设置明细单表信息
			purchasePlanItem = getPurchasePlanItemEntity(purchasePlan, trans);
			purchasePlanItem.setPlan(purchasePlan);
			purchasePlanItem.setVendor(orgMap.get(trans.getVendorCode()));
			purchasePlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));
			purchasePlanItem.setItemNo(StringUtils.convertToInteger(trans.getItemNo()));
			purchasePlanItem.setTotalPlanQty(StringUtils.convertToDouble(trans.getTotalPlanQty())); 
			purchasePlanItem.setPlanRecTime(DateUtil.stringToTimestamp(trans.getPlanRecTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)); 
			purchasePlanItemList.add(purchasePlanItem);
			
			key = key + ";" + trans.getVendorCode();
			if(!tmpvenMap.containsKey(key)) {
				purchasePlanVendor = getPurchasePlanVendorEntity(purchasePlan, trans);
				purchasePlanVendor.setPlan(purchasePlan);
				purchasePlanVendor.setVendor(purchasePlanItem.getVendor());
				purchasePlanVendor.setSeeStatus(PurchaseConstans.SEE_STATUS_NO);
				purchasePlanVendor.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				tmpvenMap.put(key, purchasePlanVendor);
			} else {
				purchasePlanVendor = tmpvenMap.get(key);
			}
			//设置供应商明细
			purchasePlanVendorItem = getPurchasePlanVenodrItemEntity(purchasePlanVendor, trans.getMaterialCode(), trans.getItemNo(),DateUtil.stringToTimestamp(trans.getPlanRecTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			purchasePlanVendorItem.setPlan(purchasePlan);
			purchasePlanVendorItem.setVendorPlan(purchasePlanVendor);
			purchasePlanVendorItem.setItemNo(purchasePlanItem.getItemNo());
			purchasePlanVendorItem.setUnitName(trans.getUnitName());
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
	
	private List<PurchasePlanTransfer> validateTransfers(List<PurchasePlanTransfer> planTranList, Map<String, OrganizationEntity> orgMap,Map<String, OrganizationEntity> buyerMap, Map<String, MaterialEntity> materialMap, Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入的采购计划信息进行预处理,共有[" + (planTranList == null ? 0 : planTranList.size()) + "]条数据";
		logger.log(logMsg);
		counts[0] = planTranList.size();
		List<PurchasePlanTransfer> ret = new ArrayList<PurchasePlanTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<OrganizationEntity> buyerList=null;
		List<MaterialEntity> materialList = null;
		boolean lineValidat = true;
		int index  = 2; 
		for(PurchasePlanTransfer trans : planTranList) {
			////每次循环之后重置start
			orgList = null;
			buyerList=null;
			materialList = null;
			////重置end
			
			if(StringUtils.isEmpty(trans.getMonth())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],月份不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getBuyerCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],采购组织编码不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getVendorCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],供应商编码不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getMaterialCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],物料编码不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getUnitName())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],单位不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getItemNo())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],行号不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getTotalPlanQty())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],需求数量不能为空,忽略此采购计划");
			}
			
			if(StringUtils.isEmpty(trans.getPlanRecTime())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "],需求时间不能为空,忽略此采购计划");
			}else{
				//将需求时间转成23：59：59
				trans.setPlanRecTime(trans.getPlanRecTime()+" 23:59:59");
				try{
					Timestamp planRecTime=DateUtil.stringToTimestamp(trans.getPlanRecTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				    if(null==planRecTime){
				    	lineValidat = false;
						logger.log("->[FAILED]行索引[" + index + "],需求时间格式有误，不匹配yyyy-MM-dd,忽略此采购计划");
				    }
				} catch(Exception e){
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + index + "],需求时间格式不匹配yyyy-MM-dd,忽略此采购计划");
				}
				
			}
			
			if(!buyerMap.containsKey(trans.getBuyerCode())) {
				buyerList = organizationDao.findByCodeAndAbolishedAndRoleTypeAndEnableStatus(trans.getBuyerCode(),StatusConstant.STATUS_NO,OrgType.ROLE_TYPE_BUYER,StatusConstant.STATUS_YES);
				if(CollectionUtils.isEmpty(buyerList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + index + "],采购组织编码[" + trans.getBuyerCode() + "]未在系统中维护,或被禁用,忽略此采购计划");
				} else {
					buyerMap.put(trans.getBuyerCode(), buyerList.get(0));
				}
			}else{
				buyerList=new ArrayList<OrganizationEntity>();
				buyerList.add(buyerMap.get(trans.getBuyerCode()));
			}
			
			//判断当前用户有无该采购组织权限时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !buyerOrgPermissionUtil.isContainsBuyerIds(buyerList.get(0).getId())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "]当前用户无采购组织["+trans.getBuyerCode()+"]权限,忽略此采购计划");
			}
			
			//判断当前用户有无该采购组织权限时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCodeAndAbolishedAndRoleTypeAndEnableStatusAndActiveStatusAndBuyerId(trans.getVendorCode(),OrgType.ROLE_TYPE_VENDOR,StatusConstant.STATUS_YES,StatusConstant.STATUS_YES,buyerList.get(0).getId());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + index + "]供应商编码[" + trans.getVendorCode() + "]无采购组织["+trans.getBuyerCode()+"]权限，或未在系统中维护,或未生效,忽略此采购计划");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			//查询物料时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findMaterialListByBuyerAndCodeAndAbolished(buyerList.get(0).getId(),trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + index + "],物料[" + trans.getMaterialCode() + "],无采购组织["+trans.getBuyerCode()+"]权限， 不存在,忽略此采购计划");
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

	*//**
	 * @param month
	 * @param buyerCode
	 * @return
	 *//*

	

	
	protected PurchasePlanVendorEntity getPurchasePlanVendorEntity(PurchasePlanEntity purchasePlan, PurchasePlanTransfer trans) {
		if(purchasePlan.getId() == 0l) {
			return new PurchasePlanVendorEntity();
		}
		
		PurchasePlanVendorEntity planVendor = purchasePlanVendorDao.findPurchasePlanVendorEntityByMain(purchasePlan.getId(), trans.getVendorCode());
		return planVendor == null ? new PurchasePlanVendorEntity() : planVendor;
	}
	
	protected PurchasePlanVenodrItemEntity getPurchasePlanVenodrItemEntity(PurchasePlanVendorEntity planVendor, String materialCode, String itemNo,Timestamp planRecTime) {
		if(planVendor.getId() == 0l) {
			return new PurchasePlanVenodrItemEntity();
		}
		
		PurchasePlanVenodrItemEntity planVendorItem = purchasePlanVendorItemDao.findPurchasePlanVenodrItemEntityByVendorPlan(planVendor.getId(), materialCode, StringUtils.convertToInteger(itemNo),planRecTime);
		return planVendorItem == null ? new PurchasePlanVenodrItemEntity() : planVendorItem;
	}
	
	*//**
	 * 删除计划
	 * @param plans
	 *//*
	public void deletePlans(List<PurchasePlanEntity> plans) {
		for (PurchasePlanEntity purchasePlanEntity : plans) {
			List<PurchasePlanItemEntity> planItem = purchasePlanItemDao.findByPlanId(purchasePlanEntity.getId());
			List<PurchasePlanVendorEntity> vendorPlan = purchasePlanVendorDao.findByPlanId(purchasePlanEntity.getId());
			List<PurchasePlanVenodrItemEntity> vendorPlanItem = purchasePlanVendorItemDao.findByPlanId(purchasePlanEntity.getId());
			purchasePlanVendorItemDao.delete(vendorPlanItem);
			purchasePlanVendorDao.delete(vendorPlan);
			purchasePlanItemDao.delete(planItem);
			purchasePlanDao.delete(purchasePlanEntity); 
		}
	}*/
	
}