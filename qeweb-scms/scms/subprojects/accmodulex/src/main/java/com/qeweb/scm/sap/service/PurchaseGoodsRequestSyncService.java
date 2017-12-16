package com.qeweb.scm.sap.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.PurchaseGoodsRequestSAP;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseGoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanTemDao;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.PurchaseGoodsRequestService;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.sap.service.vo.PurchaseGoodsRequestVo;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
/**
 * 要货计划 SAP->SRM
 * @author chao.gu
 * 20170522
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchaseGoodsRequestSyncService extends BaseService{
	@Autowired
	private PurchaseGoodsRequestDao requestDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	@Autowired
	private PurchasingGroupDao groupDao;
	
	@Autowired
	private OrganizationDao vendorDao;
	
	@Autowired 
	private MaterialDao materialDao;
	
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private PurchaseGoodsRequestService PurchaseGoodsRequestService;
	
	@Autowired
	private PurchaseOrderItemPlanTemDao purchaseOrderItemPlanTemDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;
	
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private  DeliveryItemDao deliveryItemDao;
	
	@Autowired
	private DeliveryDao deliveryDao;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private 	WarnMainService warnMainService;
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	@Autowired
	private GenerialDao generalDao;
	/**
	 * 同步sap要货计划数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("PurchaseGoodsRequestSyncService execute method execute start");
	   List<PurchaseGoodsRequestEntity> list;
	   PurchaseGoodsRequestEntity newRequest;
	   Map<String,FactoryEntity> factoryMap;//工厂
	   Map<String,MaterialEntity> materialMap;//物料
	   Map<String,OrganizationEntity> vendorMap;//供应商
	   Map<String,PurchasingGroupEntity> groupMap;//采购组
	   //1、获取工厂
	   List<FactoryEntity> factorys=factoryDao.findByAbolished(BOHelper.UNABOLISHED_SINGEL);
	   if(null!=factorys && factorys.size()>0){
		   factoryMap=new HashMap<String, FactoryEntity>();
		   materialMap=new HashMap<String, MaterialEntity>();
		   vendorMap=new HashMap<String, OrganizationEntity>();
		   groupMap=new HashMap<String, PurchasingGroupEntity>();
		   
		   //工厂map赋值
		   for (FactoryEntity factoryEntity : factorys) {
			   factoryMap.put(factoryEntity.getCode(), factoryEntity);
		   }
		   List<String> materialList=new ArrayList<String>();
		   for (FactoryEntity factoryEntity : factorys) {
			   list =new ArrayList<PurchaseGoodsRequestEntity>();
			   List<VendorMaterialRelEntity> vendorMaterialRelList=vendorMaterialRelDao.findByFactoryIdAndAbolished(factoryEntity.getId(),BOHelper.UNABOLISHED_SINGEL);
				for( VendorMaterialRelEntity vendorMaterialRel :   vendorMaterialRelList ){
					materialList.add(vendorMaterialRel.getMaterial().getCode());
				}

			   //2、塞数据
			   List<PurchaseGoodsRequestEntity> res=PurchaseGoodsRequestSAP.sync(factoryEntity.getCode(),materialList,iLogger);
			   if(res!=null){
					iLogger.log("工厂编码："+"总条数:"+res.size());
					for (PurchaseGoodsRequestEntity re : res) {
						newRequest=validate(re,factoryMap,materialMap,groupMap,vendorMap,iLogger);
						if(null!=newRequest){
							list.add(newRequest);
						}
					}
			   }
			   
			   //3、保存
			   if(null!=list && list.size()>0){
				   requestDao.save(list);
			   }
			   
			   
			   //add by chao.gu 20171125 清理sap已经删除的要货计划
			   if(null!=res){
			   //1、获取该工厂及物料下面当前日期往后的所有要货计划
			   Map<String,Long> effectRequestMap=findEffectRequest(factoryEntity.getId(), materialList);
			   //2、判断所有要货计划不在此次同步的要货计划之中
			   if(res!=null && CommonUtil.isNotNullMap(effectRequestMap)){
					iLogger.log("开始清理sap已经删除的要货计划");
					for (PurchaseGoodsRequestEntity re : res) {
						////KEY=FACTORY_CODE+MATERIAL_CODE+VENDOR_CODE+RQ
						String key=re.getFactoryCode()+"_"+re.getMaterialCode()+"_"+re.getVendorCode()+"_"+re.getRqStr();
						if(effectRequestMap.containsKey(key)){
							effectRequestMap.remove(key);
						}
					}
			   }
			   
			   //3、map只剩下SAP删除的要货计划
			   if(CommonUtil.isNotNullMap(effectRequestMap)){
				   for (Long value : effectRequestMap.values()) {
					   PurchaseGoodsRequestEntity delRequ=requestDao.findById(value);
					   boolean isNotHasDlv=clearPurchaseGoodsRequest(delRequ,iLogger);
					   if(isNotHasDlv){//没有发货单直接作废
						   delRequ.setAbolished(BOHelper.ABOLISHED_SINGEL);
						   delRequ.setIsOutData(1);
						   requestDao.save(delRequ);
						   iLogger.log("作废的要货计划ID:"+delRequ.getId());
					   }else{
						   iLogger.log("SAP已删除但是有发货单的要货计划ID:"+delRequ.getId());
					   }
				    }
				  
			   }
			   }
			  //add end
			   
			   
			   //4.自动匹配订单（所有的剩余数量不等于空）（现在算是所有的）
			   
			   List<PurchaseGoodsRequestEntity>  tempList =requestDao.findAll();
			   
			   //获取当前时间
				  Timestamp curr=DateUtil.getCurrentTimestamp();
				  
				   Calendar cal = Calendar.getInstance();    
			       cal.setTime(curr);    
			       cal.add(Calendar.DATE,-1);
			       
			       int hour = cal.get(Calendar.HOUR_OF_DAY);    
			       int minute = cal.get(Calendar.MINUTE);    
			       int second = cal.get(Calendar.SECOND); 
			     
			       
			        //时分秒（毫秒数）    
			        long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;    
			        //凌晨00:00:00    
			        cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);  
			        
			        cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
				
		        
				
				List<PurchaseGoodsRequestEntity> needMatchList =new ArrayList<PurchaseGoodsRequestEntity>();
			  /*  List<PurchaseGoodsRequestEntity> needClearList =new ArrayList<PurchaseGoodsRequestEntity>();*/
			   
			   for(PurchaseGoodsRequestEntity item : tempList){
				   if(item.getRq().before(cal.getTime())){
					  /* needClearList.add(item);*/
				   }else{
					   needMatchList.add(item);
				   }
				
			   }
			   
			   //清理要货计划(凌晨清理所有的)
			   for(PurchaseGoodsRequestEntity item2 : tempList){
				   clearPurchaseGoodsRequest(item2,iLogger);
			   }
			   
			   //匹配订单
			   Collections.sort(needMatchList, new Comparator<PurchaseGoodsRequestEntity>() {  
		            public int compare(PurchaseGoodsRequestEntity o1, PurchaseGoodsRequestEntity o2) {  
		                return o1.getRq().compareTo(o2.getRq());  
		            }  
		        }); 
			   for(PurchaseGoodsRequestEntity item1 : needMatchList){
				    PurchaseGoodsRequestService.goodsRequestMatchPo(item1.getId());
			   }

			   //向采购员发送待发布的数据（发送需要匹配的数据）
/*			   for(PurchaseGoodsRequestEntity item : needMatchList){
				   if(item.getPublishStatus() != 1){
					   List<Long> userIdList = new ArrayList<Long>();
					   List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(item.getGroup().getId());
					   for(GroupConFigRelEntity rel : userOrderList ){
						 userIdList.add(rel.getUserId());
					   }
				       warnMainService.warnMessageSet(item.getId(), WarnConstant.GOODS_UN_PUBLISH, userIdList);
				   }
			   }*/
			    
			   
	   }
		   }else{
		   iLogger.log("无工厂数据，不调用接口");
	   }
	   
	 
	   iLogger.log("PurchaseGoodsRequestSyncService execute method execute end");
		return true;
   }
	         
	
	/**
	 * 同步sap要货计划数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean manualSync(ILogger iLogger,FactoryEntity factory,List<String> materialList)throws Exception {
		iLogger.log("PurchaseGoodsRequestSyncService manualSync method manualSync start");
		
	   List<PurchaseGoodsRequestEntity> list  = new ArrayList<PurchaseGoodsRequestEntity>();
	   
	   PurchaseGoodsRequestEntity newRequest;
	   Map<String,FactoryEntity> factoryMap = new HashMap<String, FactoryEntity>();//工厂
	   Map<String,MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();//物料
	   Map<String,OrganizationEntity> vendorMap = new HashMap<String, OrganizationEntity>();//供应商
	   Map<String,PurchasingGroupEntity> groupMap =new HashMap<String, PurchasingGroupEntity>();//采购组
	   
	   List<FactoryEntity> factorys=factoryDao.findByAbolished(BOHelper.UNABOLISHED_SINGEL);
	   //工厂map赋值
	   for (FactoryEntity factoryEntity : factorys) {
		   factoryMap.put(factoryEntity.getCode(), factoryEntity);
	   }
	   //2、塞数据
	   List<PurchaseGoodsRequestEntity> res=PurchaseGoodsRequestSAP.sync(factory.getCode(),materialList,iLogger);
	   if(res!=null){
			iLogger.log("工厂编码："+"总条数:"+res.size());
			for (PurchaseGoodsRequestEntity re : res) {
				newRequest=validate(re,factoryMap,materialMap,groupMap,vendorMap,iLogger);
				if(null!=newRequest){
					list.add(newRequest);
				}
			}
	   }
	   
	   //3、保存
	   if(null!=list && list.size()>0){
		   requestDao.save(list);
	   }
	   
	 
	   
	   //add by chao.gu 20171125 清理sap已经删除的要货计划
	   if(null!=res){
	   //1、获取该工厂及物料下面当前日期往后的所有要货计划
	   Map<String,Long> effectRequestMap=findEffectRequest(factory.getId(), materialList);
	   //2、判断所有要货计划不在此次同步的要货计划之中
	   if(res!=null && CommonUtil.isNotNullMap(effectRequestMap)){
			iLogger.log("开始清理sap已经删除的要货计划");
			for (PurchaseGoodsRequestEntity re : res) {
				////KEY=FACTORY_CODE+MATERIAL_CODE+VENDOR_CODE+RQ
				String key=re.getFactoryCode()+"_"+re.getMaterialCode()+"_"+re.getVendorCode()+"_"+re.getRqStr();
				if(effectRequestMap.containsKey(key)){
					effectRequestMap.remove(key);
				}
			}
	   }
	   
	   //3、map只剩下SAP删除的要货计划
	   if(CommonUtil.isNotNullMap(effectRequestMap)){
		   for (Long value : effectRequestMap.values()) {
			   PurchaseGoodsRequestEntity delRequ=requestDao.findById(value);
			   boolean isNotHasDlv=clearPurchaseGoodsRequest(delRequ,iLogger);
			   if(isNotHasDlv){//没有发货单直接作废
				   delRequ.setAbolished(BOHelper.ABOLISHED_SINGEL);
				   delRequ.setIsOutData(1);
				   requestDao.save(delRequ);
			   }
		    }
		  
	   }
	   }
	  //add end
	   
	   
	   //4.匹配订单（获取当前传入的工厂和物料）
	   
	   //获取当前时间
	  Timestamp curr=DateUtil.getCurrentTimestamp();
	  
	   Calendar cal = Calendar.getInstance();    
       cal.setTime(curr);    
       cal.add(Calendar.DATE,-1);
       
       int hour = cal.get(Calendar.HOUR_OF_DAY);    
       int minute = cal.get(Calendar.MINUTE);    
       int second = cal.get(Calendar.SECOND); 
     
       
        //时分秒（毫秒数）    
        long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;    
        //凌晨00:00:00    
        cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);  
        
        cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
	  
	  
	   
	   List<PurchaseGoodsRequestEntity> pgrList  = new ArrayList<PurchaseGoodsRequestEntity>();
	   
	   List<PurchaseGoodsRequestEntity> needMatchList =new ArrayList<PurchaseGoodsRequestEntity>();
	   List<PurchaseGoodsRequestEntity> needClearList =new ArrayList<PurchaseGoodsRequestEntity>();
	   
	   //通过物料和工厂获取当前要货计划
	   for(String material :    materialList){
		   pgrList =  requestDao.findByFatoryCodeAndMaterialCode(factory.getCode(),material);
		   for(PurchaseGoodsRequestEntity item : pgrList){
			   if(item.getRq().before(cal.getTime())){
				  /* needClearList.add(item);*/
			   }else{
				   needMatchList.add(item);
			   }
			   needClearList.add(item);
		   }
	   }
	   
	   

	   //清理要货计划
	   for(PurchaseGoodsRequestEntity item2 : needClearList){
		   clearPurchaseGoodsRequest(item2,iLogger);
	   }
	   
	  
	   
	   //匹配订单
	   //排序
	   Collections.sort(needMatchList, new Comparator<PurchaseGoodsRequestEntity>() {  
            public int compare(PurchaseGoodsRequestEntity o1, PurchaseGoodsRequestEntity o2) {  
                return o1.getRq().compareTo(o2.getRq());  
            }  
        }); 
	   for(PurchaseGoodsRequestEntity item1 : needMatchList){
		    PurchaseGoodsRequestService.goodsRequestMatchPo(item1.getId());
	   }
	   
	   
	   //向采购员发送待发布的数据（发送需要匹配的数据）
/*	   for(PurchaseGoodsRequestEntity item : needMatchList){
		   if(item.getPublishStatus() != 1){
			   List<Long> userIdList = new ArrayList<Long>();
			   List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(item.getGroup().getId());
			   for(GroupConFigRelEntity rel : userOrderList ){
				 userIdList.add(rel.getUserId());
			   }
		       warnMainService.warnMessageSet(item.getId(), WarnConstant.GOODS_UN_PUBLISH, userIdList);
		   }
	   }*/
	   
	   
/*	   if(null!=list && list.size()>0){
		   //排序
		   Collections.sort(list, new Comparator<PurchaseGoodsRequestEntity>() {  
	            public int compare(PurchaseGoodsRequestEntity o1, PurchaseGoodsRequestEntity o2) {  
	                return o1.getRq().compareTo(o2.getRq());  
	            }  
	        }); 
		   
		  //一个一个的来（对于当前返回的订单）
		   for(PurchaseGoodsRequestEntity item : list){
			   PurchaseGoodsRequestService.goodsRequestMatchPo(item.getId());
		   }
		  
	   }*/
	   
	   
	   iLogger.log("PurchaseGoodsRequestSyncService manualSync method manualSync end");
		return true;
   }

	
	/**
	 *清理要货计划
	 * @param 
	 */
	public boolean clearPurchaseGoodsRequest(PurchaseGoodsRequestEntity Item,ILogger iLogger) throws Exception{
		boolean isNotHasDlv=true;//是否不存在发货单
		iLogger.log("正在清理要货计划："+Item.getFactory().getCode()+","+Item.getMaterial().getCode()+","+Item.getRq()+".");
		//释放未发布的供货计划
		List<PurchaseOrderItemPlanEntity> notPunlishPlanList = purchaseOrderItemPlanDao.getUnPublishPoItemplanListBygoodsId(Item.getId());
		if(notPunlishPlanList != null &&  notPunlishPlanList.size() != 0){
			for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : notPunlishPlanList){
				//释放订单的数量
				writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);
				//释放要货计划数量
				writeGoodsQtyByPlan(purchaseOrderItemPlanEntity,iLogger);
				//删除
				purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
			}
		}
		
		
		//释放已发布未确认的供货计划（订单和要货计划数据）
		List<PurchaseOrderItemPlanEntity> notConfirmPlanList = purchaseOrderItemPlanDao.getNotConfirmPoItemplanListBygoodsId(Item.getId());
		if(notConfirmPlanList != null &&  notConfirmPlanList.size() != 0){
			for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : notConfirmPlanList){
				//释放订单的数量
				writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);	
				//释放要货计划数量
				writeGoodsQtyByPlan(purchaseOrderItemPlanEntity,iLogger);
				//删除
				purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
			}
		}
		
		//释放已发布已确认的供货计划（带发货看板 ，未审核和已审核的发货单）（订单和要货计划数据）
		List<DeliveryItemEntity> deList = new ArrayList<DeliveryItemEntity>();
		List<PurchaseOrderItemPlanEntity> otherList = new ArrayList<PurchaseOrderItemPlanEntity>();
		
		List<PurchaseOrderItemPlanEntity> confirmPlanList = purchaseOrderItemPlanDao.getConfirmPoItemplanListBygoodsId(Item.getId());
		

		if(confirmPlanList != null && confirmPlanList.size() != 0){
			for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : confirmPlanList){
				List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(purchaseOrderItemPlanEntity.getId());
				if(deliveryList != null && deliveryList.size() != 0){
					isNotHasDlv=false;
					for(DeliveryItemEntity item : deliveryList){
						deList.add(item);
					}
				}else{
					//获取待发货看班数据
					otherList.add(purchaseOrderItemPlanEntity);
				}
			}
			
			//释放待发货看班数据
			for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : otherList){
				//释放订单的数量
				writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);
				//释放要货计划数量
				writeGoodsQtyByPlan(purchaseOrderItemPlanEntity,iLogger);
				//删除
				purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
			}
			
			//释放部分发货（待发货看板上的数据（过期的））
			  Timestamp curr=DateUtil.getCurrentTimestamp();
			  Calendar cal = Calendar.getInstance();    
		      cal.setTime(curr);    
		      cal.add(Calendar.DATE,-1);

		      int hour = cal.get(Calendar.HOUR_OF_DAY);    
		      int minute = cal.get(Calendar.MINUTE);    
		      int second = cal.get(Calendar.SECOND); 
		      //时分秒（毫秒数）    
		      long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;    
		      //凌晨00:00:00    
		      cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);  
		      cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
		      for(DeliveryItemEntity item : deList){
		    	  PurchaseOrderItemPlanEntity plan = item.getOrderItemPlan();
		    	  //清楚过期的要货计划
		    	  if(plan.getPurchaseGoodsRequest().getRq().before(cal.getTime())){
 		    				    		  
		    		//创建数量 = 已发数量+ 已创建未发数量
		    		Double count =BigDecimalUtil.add(plan.getDeliveryQty() , plan.getToDeliveryQty());  
		    		
		    		 //释放的数量
		    		Double number =BigDecimalUtil.sub(plan.getOrderQty(),count);
		    		  
		    		plan.setOrderQty(count);
		    		plan.setBaseQty(orderQtyToBaseQty(plan.getOrderItem().getBPUMN(), plan.getOrderItem().getBPUMZ(), plan.getOrderQty()));
					Double undeliveryQty = 0d;
					plan.setUndeliveryQty(undeliveryQty);//未发数量
					
					
					
					PurchaseOrderItemEntity orderItem=plan.getOrderItem();
					PurchaseGoodsRequestEntity request=plan.getPurchaseGoodsRequest();
					
					purchaseOrderItemPlanDao.save(plan);
	
					//1、释放到要货计划
					number=orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), number);
					double surQry =Double.valueOf(request.getSurQry());
					request.setSurQry(""+BigDecimalUtil.add(surQry, number));
					requestDao.save(request);
					iLogger.log("要货单数量变化，单据为"+request.getMaterialCode()+"-"+request.getRq());
					iLogger.log(",原剩余数量为"+surQry+",处理数量"+number);
					iLogger.log("\r\n");
					
					
					//2、更新行剩余匹配数量更新更新
					orderItem.setSurBaseQty(BigDecimalUtil.add(orderItem.getSurBaseQty(),number));
					orderItem.setSurOrderQty(baseQtyToOrderQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), orderItem.getSurBaseQty()));
					purchaseOrderItemDao.save(orderItem);
					iLogger.log("订单明细剩余数量变化，订单为"+orderItem.getOrder().getOrderCode()+"-"+orderItem.getItemNo());
					iLogger.log(",原剩余数量为"+orderItem.getSurOrderQty()+",处理数量"+baseQtyToOrderQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), number));
					iLogger.log("\r\n");
					
					
		    	  }
		      }

		}
		
		//修改要货计划的
		//修改要货计划的确认状态
		PurchaseGoodsRequestService.modifyGoodsRequestConfirmStatus(Item.getId());
		//修改发布的状态
		PurchaseGoodsRequestService.modifyGoodsRequestPublishStatus(Item.getId());
		
		return isNotHasDlv;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 验证，过滤错误数据
	 * @param re
	 * @param factoryMap
	 * @param materialMap
	 * @param groupMap
	 * @param vendorMap
	 * @param iLogger
	 * @return
	 * @throws Exception 
	 */
	public PurchaseGoodsRequestEntity validate(PurchaseGoodsRequestEntity re, Map<String,FactoryEntity> factoryMap,Map<String,MaterialEntity> materialMap,Map<String,PurchasingGroupEntity> groupMap,Map<String,OrganizationEntity> vendorMap,ILogger iLogger) throws Exception{
		Timestamp rq;
		FactoryEntity factory;
		MaterialEntity material;
		PurchasingGroupEntity group;
		OrganizationEntity vendor;
		PurchaseGoodsRequestEntity newRequest;
		//验证是否为空
		if(StringUtils.isEmpty(re.getFactoryCode()) || StringUtils.isEmpty(re.getGroupCode()) || StringUtils.isEmpty(re.getVendorCode()) || StringUtils.isEmpty(re.getMaterialCode()) || StringUtils.isEmpty(re.getRqStr())){
			 iLogger.log("工厂编码或物料编码或采购组编码或供应商编码或日期为空，忽略该数据");
			 return null;
		}
		
		//验证日期
		try{
		    rq=DateUtil.stringToTimestamp(re.getRqStr(), "yyyyMMdd");
		}catch(Exception e){
			iLogger.log(re.getRqStr()+"日期格式不正确，必须是yyyyMMdd，忽略该数据");
			return null;
		}
		
		//验证工厂
		if(factoryMap.containsKey(re.getFactoryCode())){
			factory=factoryMap.get(re.getFactoryCode());
		}else{
			iLogger.log(re.getFactoryCode()+"工厂不存在，忽略该数据");
			return null;
		}
		//验证物料
		if(materialMap.containsKey(re.getMaterialCode())){
			material=materialMap.get(re.getMaterialCode());
		}else{
			List<MaterialEntity> materials=materialDao.findByCodeAndAbolished(re.getMaterialCode(), BOHelper.UNABOLISHED_SINGEL);
		    if(null!=materials && materials.size()>0){
		    	material=materials.get(0);
		    }else{
		    	iLogger.log(re.getMaterialCode()+"物料号不存在，忽略该数据");
				return null;
		    }
		}
		//验证采购组
		if(groupMap.containsKey(re.getGroupCode())){
			group=groupMap.get(re.getGroupCode());
		}else{
			group=groupDao.findByCodeAndAbolished(re.getGroupCode(), BOHelper.UNABOLISHED_SINGEL);
		    if(null==group){
		    	iLogger.log(re.getMaterialCode()+"采购组不存在，忽略该数据");
				return null;
		    }
		}
		//验证供应商
		if(vendorMap.containsKey(re.getVendorCode())){
			vendor=vendorMap.get(re.getVendorCode());
		}else{
			vendor=vendorDao.findByCodeAndAbolished(re.getVendorCode(), BOHelper.UNABOLISHED_SINGEL);
			if(null==vendor){
				iLogger.log(re.getVendorCode()+"供应商不存在，忽略该数据");
				return null;
			}
		}
		
		//判断是否存在
		List<PurchaseGoodsRequestEntity> requests=null;
		if(StringUtils.isEmpty(re.getFlag()) ){
			requests=requestDao.findByFactoryAndMaterialAndVendorAndRQAndFlagIsNull(re.getFactoryCode(),re.getMaterialCode(),re.getVendorCode(),rq);
		}else{
			requests=requestDao.findByFactoryAndMaterialAndVendorAndRQ(re.getFactoryCode(),re.getMaterialCode(),re.getVendorCode(),re.getFlag(),rq);
		}
		
		if(null!=requests && requests.size()>0){
			System.out.println("edit"+re.getFactoryCode()+"-"+re.getMaterialCode()+"-"+re.getVendorCode()+"-"+re.getFlag()+"-"+rq);
			//覆盖
			newRequest=requests.get(0);
			//是否有修改
			if(valIsChange(re,newRequest)){
				//判断修改与要货计划相关的文件
				return handlePurchaseGoodsRequest(re,newRequest,iLogger,false);
			}
	
		}else{
			System.out.println("add"+re.getFactoryCode()+"-"+re.getMaterialCode()+"-"+re.getVendorCode()+"-"+re.getFlag()+"-"+rq);
			//新增
			newRequest=new PurchaseGoodsRequestEntity();
			newRequest.setFactory(factory);
			newRequest.setMaterial(material);
			newRequest.setGroup(group);
			newRequest.setVendor(vendor);
			newRequest.setRq(rq);
			newRequest.setZb(re.getZb());
			newRequest.setJhzzt(re.getJhzzt());
			newRequest.setZljyt(re.getZljyt());
			newRequest.setShpl(re.getShpl());
			newRequest.setYsts(re.getYsts());
			newRequest.setPcsl(re.getPcsl());
			newRequest.setDhsl(re.getDhsl());
			newRequest.setKcsl(re.getKcsl());
			newRequest.setFhsl(re.getFhsl());
			newRequest.setIsFull(0);
			newRequest.setSurQry(re.getDhsl());
			newRequest.setSyncTime(DateUtil.getCurrentTimestamp());
			newRequest.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			newRequest.setCreateTime(DateUtil.getCurrentTimestamp());
			newRequest.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			newRequest.setIsOutData(1);
			newRequest.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			newRequest.setVendorConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			newRequest.setBuyerConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			newRequest.setFlag(re.getFlag());
			newRequest.setMeins(re.getMeins());
			newRequest.setDrawingNumber(re.getDrawingNumber());
			System.out.println(re.getDrawingNumber());
			return newRequest;
		}
		return null;
	}
	
	
	/**
	 * 验证是否发生改变
	 * @param re
	 * @param newRequest
	 * @return
	 */
	public boolean valIsChange(PurchaseGoodsRequestEntity re,PurchaseGoodsRequestEntity newRequest){
		boolean isChange=false;
		if(null!=re.getZb() && null!=newRequest.getZb() && !re.getZb().equals(newRequest.getZb())){//1、占比
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getZb()) && StringUtils.isEmpty(re.getZb()))){
			isChange=true;
		}else if(null!=re.getJhzzt() && null!=newRequest.getJhzzt() && !re.getJhzzt().equals(newRequest.getJhzzt())){//2、计划周转天
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getJhzzt()) && StringUtils.isEmpty(re.getJhzzt()))){
			isChange=true;
		}else if(null!=re.getZljyt() && null!=newRequest.getZljyt() && !re.getZljyt().equals(newRequest.getZljyt())){//3、质量检验天数
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getZljyt()) && StringUtils.isEmpty(re.getZljyt()))){
			isChange=true;
		}else if(null!=re.getShpl() && null!=newRequest.getShpl() && !re.getShpl().equals(newRequest.getShpl())){//4、送货频率（天数）
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getShpl()) && StringUtils.isEmpty(re.getShpl()))){
			isChange=true;
		}else if(null!=re.getYsts() && null!=newRequest.getYsts() && !re.getYsts().equals(newRequest.getYsts())){//5.运输天数
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getYsts()) && StringUtils.isEmpty(re.getYsts()))){
			isChange=true;
		}else if(null!=re.getPcsl() && null!=newRequest.getPcsl() && !re.getPcsl().equals(newRequest.getPcsl())){//6、排产数量
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getPcsl()) && StringUtils.isEmpty(re.getPcsl()))){
			isChange=true;
		}else if(null!=re.getDhsl() && null!=newRequest.getDhsl() && !re.getDhsl().equals(newRequest.getDhsl())){//7、到货数量
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getDhsl()) && StringUtils.isEmpty(re.getDhsl()))){
			isChange=true;
		}else if(null!=re.getKcsl() && null!=newRequest.getKcsl() && !re.getKcsl().equals(newRequest.getKcsl())){//8、库存数量
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getKcsl()) && StringUtils.isEmpty(re.getKcsl()))){
			isChange=true;
		}else if(null!=re.getFhsl() && null!=newRequest.getFhsl() && !re.getFhsl().equals(newRequest.getFhsl())){//9、发货数量
			isChange=true;
		}else if(!(StringUtils.isEmpty(re.getFhsl()) && StringUtils.isEmpty(re.getFhsl()))){
			isChange=true;
		}
		
		return isChange;
		
	}
	
	
	
	//要货计划的处理逻辑
	public  PurchaseGoodsRequestEntity handlePurchaseGoodsRequest(PurchaseGoodsRequestEntity re,PurchaseGoodsRequestEntity newRequest,ILogger iLogger,Boolean isManual) throws Exception{
		String msg="";
		//判断当前的要货计划是否被修改
		newRequest.setGroupCode(re.getGroupCode());
		newRequest.setAbolished(0);
		newRequest.setDrawingNumber(re.getDrawingNumber());
		if(newRequest.getIsModify()== null){
			newRequest.setIsModify(0);
		}
		if(newRequest.getIsModify() == 1 &&  !isManual ){
			//不做任何处理
			msg="对于工厂"+newRequest.getFactory().getCode()+",物料"+newRequest.getMaterial().getCode()+",采购组"+newRequest.getGroup().getCode()+" ,供应商"+newRequest.getVendor().getCode()+"要货计划已被修改，忽略修改!";
			iLogger.log(msg);
			return newRequest;
		}else{
			Double oldOrderQty=Double.valueOf(newRequest.getDhsl());
			Double newOrderQty=Double.valueOf(re.getDhsl()) ;
			Double surQry = Double.valueOf(newRequest.getSurQry());//剩余数量

			Double  subQty=BigDecimalUtil.sub(newOrderQty, oldOrderQty);
		
			//修改变大
			if(subQty > 0){
				//要货计划的数量改大
				Double qty =  BigDecimalUtil.add(surQry, subQty);
				newRequest.setIsFull(0);
				newRequest.setSurQry(qty.toString());//设置剩余剩余数量
				newRequest.setZb(re.getZb());
				newRequest.setJhzzt(re.getJhzzt());
				newRequest.setZljyt(re.getZljyt());
				newRequest.setShpl(re.getShpl());
				newRequest.setYsts(re.getYsts());
				newRequest.setPcsl(re.getPcsl());
				newRequest.setDhsl(re.getDhsl());
				newRequest.setKcsl(re.getKcsl());
				newRequest.setFhsl(re.getFhsl());
				newRequest.setSyncTime(DateUtil.getCurrentTimestamp());
				newRequest.setLastUpdateTime(DateUtil.getCurrentTimestamp());
				newRequest.setIsOutData(1);
				newRequest.setFlag(re.getFlag());
				newRequest.setMeins(re.getMeins());
			}else if(subQty < 0){
				//要货计划的数量改小
				PurchaseGoodsRequestVo vo = null;
				subQty=Math.abs(subQty);//改小了多少
				iLogger.log("要货计划改小了数量"+subQty+"。");
				//要货计划的剩余数量大于等于 改小的量
				if(surQry >= subQty){
					Double qty =  BigDecimalUtil.sub(surQry, subQty);
					newRequest.setSurQry(qty.toString());//设置剩余剩余数量
					newRequest.setZb(re.getZb());
					newRequest.setJhzzt(re.getJhzzt());
					newRequest.setZljyt(re.getZljyt());
					newRequest.setShpl(re.getShpl());
					newRequest.setYsts(re.getYsts());
					newRequest.setPcsl(re.getPcsl());
					newRequest.setDhsl(re.getDhsl());
					newRequest.setKcsl(re.getKcsl());
					newRequest.setFhsl(re.getFhsl());
					newRequest.setSyncTime(DateUtil.getCurrentTimestamp());
					newRequest.setLastUpdateTime(DateUtil.getCurrentTimestamp());
					newRequest.setIsOutData(1);
					newRequest.setFlag(re.getFlag());
					newRequest.setMeins(re.getMeins());
				}else{
					Double qty = newOrderQty;
					
					//释放未发布的供货计划
					List<PurchaseOrderItemPlanEntity> notPunlishPlanList = purchaseOrderItemPlanDao.getUnPublishPoItemplanListBygoodsId(newRequest.getId());
					if(notPunlishPlanList != null &&  notPunlishPlanList.size() != 0){
						for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : notPunlishPlanList){
							//释放订单的数量
							writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);	
							//删除
							purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
						}
					}
					
					
					//释放已发布未确认的供货计划（订单和要货计划数据）
					List<PurchaseOrderItemPlanEntity> notConfirmPlanList = purchaseOrderItemPlanDao.getNotConfirmPoItemplanListBygoodsId(newRequest.getId());
					if(notConfirmPlanList != null &&  notConfirmPlanList.size() != 0){
						for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : notConfirmPlanList){
							//释放订单的数量
							writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);	
							//删除
							purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
						}
					}
					
					//释放已发布已确认的供货计划（带发货看板 ，未审核和已审核的发货单）（订单和要货计划数据）
					List<DeliveryItemEntity> deList = new ArrayList<DeliveryItemEntity>();
					List<PurchaseOrderItemPlanEntity> otherList = new ArrayList<PurchaseOrderItemPlanEntity>();
					
					List<PurchaseOrderItemPlanEntity> confirmPlanList = purchaseOrderItemPlanDao.getConfirmPoItemplanListBygoodsId(newRequest.getId());
					

					if(confirmPlanList != null && confirmPlanList.size() != 0){
						
						for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : confirmPlanList){
							List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(purchaseOrderItemPlanEntity.getId());
							if(deliveryList != null && deliveryList.size() != 0){
								for(DeliveryItemEntity item : deliveryList){
									deList.add(item);
								}
							}else{
								//获取待发货看班数据
								otherList.add(purchaseOrderItemPlanEntity);
							}
						}
						
						//释放待发货看班数据
						for(PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : otherList){
							//释放订单的数量
							writeQtyByPlan(purchaseOrderItemPlanEntity,iLogger);	
							//删除
							purchaseOrderItemPlanDao.abolish(purchaseOrderItemPlanEntity.getId());
						}
						
						
/*						//2017 0809 修改（改小的数量不能小于所有的asn单的数量）
						if(deList != null && deList.size() > 0){
							vo = setPurchaseGoodsRequest(deList,qty,iLogger,newOrderQty);
					    }*/
						//处理数据（对于在代发货看办中，创建asn时维权部发出）
						if(deList != null && deList.size() > 0){
							vo = updatePlan(deList,qty,iLogger,newOrderQty);
						}
						
					}
					
					//修改要货计划
					if(vo != null){
						newRequest.setSurQry(vo.getSurQty().toString());//设置剩余剩余数量
						newRequest.setDhsl(vo.getDshl());
					}else{
						newRequest.setSurQry(re.getDhsl());//设置剩余剩余数量
						newRequest.setDhsl(re.getDhsl());
					}
					
					newRequest.setZb(re.getZb());
					newRequest.setJhzzt(re.getJhzzt());
					newRequest.setZljyt(re.getZljyt());
					newRequest.setShpl(re.getShpl());
					newRequest.setYsts(re.getYsts());
					newRequest.setPcsl(re.getPcsl());
					newRequest.setKcsl(re.getKcsl());
					newRequest.setFhsl(re.getFhsl());
					newRequest.setSyncTime(DateUtil.getCurrentTimestamp());
					newRequest.setLastUpdateTime(DateUtil.getCurrentTimestamp());
					newRequest.setIsOutData(1);
					newRequest.setFlag(re.getFlag());
					newRequest.setMeins(re.getMeins());

				}

			}
		}
	
		return newRequest;
	}
	
	
	//处理简单的逻辑
	public PurchaseGoodsRequestVo setPurchaseGoodsRequest(List<DeliveryItemEntity> deliveryList, Double  qty ,ILogger iLogger,Double newOrderQty) throws Exception{	
		PurchaseGoodsRequestVo vo = new PurchaseGoodsRequestVo();
		if(deliveryList!=null && deliveryList.size()>0){
		    Double count = 0d;
			for(DeliveryItemEntity deItem : deliveryList){
				Double ty1 =   orderQtyToBaseQty(deItem.getOrderItem().getBPUMN() , deItem.getOrderItem().getBPUMZ(),deItem.getOrderItemPlan().getOrderQty());
				count = BigDecimalUtil.add(count,ty1);
			}
			Double  subQty11=BigDecimalUtil.sub(newOrderQty, count);
			Double subQty1=Math.abs(subQty11);
			if(subQty11  >= 0 ){
				vo.setDshl(newOrderQty.toString());
				vo.setSurQty(subQty1);
			}else{
				vo.setDshl(count.toString());
				vo.setSurQty(0d);
			}
		}
		return vo;
	}
	
/*-------------------------------------------------------------	*/
	//处理发货单的数据逻辑
	public PurchaseGoodsRequestVo updatePlan(List<DeliveryItemEntity> deliveryList, Double  qty ,ILogger iLogger,Double newOrderQty) throws Exception{		
		PurchaseGoodsRequestVo vo = new PurchaseGoodsRequestVo();
		
		
		//去重发货单的中的供货计划
		Map<Long, PurchaseOrderItemPlanEntity> orderItemPlanMap = new HashMap<Long, PurchaseOrderItemPlanEntity>();
		
		
		if(deliveryList!=null && deliveryList.size()>0){
			Double  shouldQty = 0d; //应发数量
			Double realQty = 0d;//实发数量
			
			List<PurchaseOrderItemPlanEntity> asnOrderItemPlanList =  new ArrayList<PurchaseOrderItemPlanEntity>();//发货单对应的供货计划
			for (DeliveryItemEntity deliveryItemEntity : deliveryList) {
			    if(!orderItemPlanMap.containsKey(deliveryItemEntity.getOrderItemPlan().getId())){
					//应发的数量
					Double ty0 =   orderQtyToBaseQty(deliveryItemEntity.getOrderItem().getBPUMN() , deliveryItemEntity.getOrderItem().getBPUMZ(),deliveryItemEntity.getOrderItemPlan().getOrderQty());
					shouldQty = BigDecimalUtil.add(shouldQty, ty0);
					asnOrderItemPlanList.add(deliveryItemEntity.getOrderItemPlan());
					orderItemPlanMap.put(deliveryItemEntity.getOrderItemPlan().getId(),deliveryItemEntity.getOrderItemPlan());
				}
				//实发的数量
				Double ty1 =   orderQtyToBaseQty(deliveryItemEntity.getOrderItem().getBPUMN() , deliveryItemEntity.getOrderItem().getBPUMZ(),deliveryItemEntity.getDeliveryQty());
				realQty = BigDecimalUtil.add(realQty, ty1);
			}
			
			
			//改小的数量大于应发的数量
			if(newOrderQty > shouldQty){
				//不做任何的变化
				qty = BigDecimalUtil.sub(newOrderQty,shouldQty);//剩余的数量 = 新的数量 - 应发数量 
				vo.setDshl(newOrderQty.toString());
				vo.setSurQty(qty);
			}
			//改小的数量小于应发数量大于实发数量
			if(realQty <newOrderQty  &&   newOrderQty < shouldQty){
				//循环修改供货计划，并且修改剩余数量（0）
				qty = 0d;
				vo.setDshl(newOrderQty.toString());
				vo.setSurQty(qty);
				//修改供货计划
				Double  subQty11=BigDecimalUtil.sub(shouldQty,newOrderQty );//需要修改的数量
				Double subQty1=Math.abs(subQty11);//需要修改多少
				modifyPlan(asnOrderItemPlanList,subQty1,iLogger);
			}
			//改小的数量小于实发的数量 按照 实发的数量来
			if(realQty >= newOrderQty ){
				//循环修改供货计划，并且修改剩余数量（0）
				qty = 0d;
				vo.setDshl(realQty.toString());
				vo.setSurQty(qty);
				//修改供货计划
				Double  subQty11=BigDecimalUtil.sub(shouldQty,realQty );//需要修改的数量
				Double subQty1=Math.abs(subQty11);//需要修改多少
				modifyPlan(asnOrderItemPlanList,subQty1,iLogger);
				
			}
			
		}

		return vo;
	}
	
	public void modifyPlan(List<PurchaseOrderItemPlanEntity> list,Double qty,ILogger iLogger) throws Exception{
		//将list排序按时间的降序排列
	   Collections.sort(list, new Comparator<PurchaseOrderItemPlanEntity>() {  
            public int compare(PurchaseOrderItemPlanEntity o1, PurchaseOrderItemPlanEntity o2) {  
                return o2.getPurchaseGoodsRequest().getRq().compareTo( o1.getPurchaseGoodsRequest().getRq());  
            }  
        });
	   
	   
	   for (PurchaseOrderItemPlanEntity orderItemPlan : list) {
			if(qty<=0){//需要处理的量处理完了，结束
				break;
			}
			Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
			//需要改小的数量与供货计划数量比较
			Double d =BigDecimalUtil.sub(qty, shouldQty);
			Double downQty=0D;
			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔供货计划，当前供货计划应发数量全部修改
				downQty=shouldQty;
			}else if(d<0){//需要改小的数量小，就处理当前供货计划就好，当前供货计划的量扣除需要处理的量
				downQty=qty;//需要处理的发货单数量
			}
			
			//修改要货计划
			
			//未发数量大于等于需要处理的数量   减去处理数量<修改要货计划开始>
			orderItemPlan.setOrderQty(BigDecimalUtil.sub(orderItemPlan.getOrderQty(), downQty));
			orderItemPlan.setBaseQty(orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(), orderItemPlan.getOrderItem().getBPUMZ(), orderItemPlan.getOrderQty()));
			Double undeliveryQty = BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty()));
			orderItemPlan.setUndeliveryQty(undeliveryQty);//未发数量
			writeQtyByPlanAndOrder(orderItemPlan, downQty,iLogger);
			purchaseOrderItemPlanDao.save(orderItemPlan);
			//<修改要货计划开始结束>
			qty=BigDecimalUtil.sub(qty,downQty);
		}  
	   
	}
	
	
	
	
	
	
	
	
	/*-------------------------------------------------------------	*/
	
	
	
	//处理发货单的数据逻辑
	public PurchaseGoodsRequestVo deletePlan(List<DeliveryItemEntity> deliveryList, Double  qty ,ILogger iLogger,Double newOrderQty) throws Exception{		
		PurchaseGoodsRequestVo vo = new PurchaseGoodsRequestVo();
		//已确认
		//是否创建asn
		if(deliveryList!=null && deliveryList.size()>0){
			Double unAuditeQty = 0d;//未审核的数量
			Double auditedeQty = 0d;//已审核的数量
			List<DeliveryItemEntity> unAuditeList = new ArrayList<DeliveryItemEntity>();
			List<DeliveryItemEntity> auditeList =  new ArrayList<DeliveryItemEntity>();
			for (DeliveryItemEntity deliveryItemEntity : deliveryList) {
				if(deliveryItemEntity.getDelivery().getAuditStatus() !=1){//未审批的发货单的数量
					//发货单的数量为订单的数量需要转换
					Double ty1 =   orderQtyToBaseQty(deliveryItemEntity.getOrderItem().getBPUMN() , deliveryItemEntity.getOrderItem().getBPUMZ(),deliveryItemEntity.getOrderItemPlan().getOrderQty());
					unAuditeQty = BigDecimalUtil.add(unAuditeQty, ty1);
					unAuditeList.add(deliveryItemEntity);
				}else{//已审批的发货单的数量
					//发货单的数量为订单的数量需要转换
					Double ty2=   orderQtyToBaseQty(deliveryItemEntity.getOrderItem().getBPUMN() , deliveryItemEntity.getOrderItem().getBPUMZ() ,deliveryItemEntity.getOrderItemPlan().getOrderQty());
					auditedeQty = BigDecimalUtil.add(auditedeQty, ty2);
					auditeList.add(deliveryItemEntity);
				}	
			}
			//
			Double  subQty11=BigDecimalUtil.sub(newOrderQty, BigDecimalUtil.add(unAuditeQty, auditedeQty));
			Double subQty1=Math.abs(subQty11);//改小了多少
			//判断是否有已发货的asn
			if(auditedeQty == 0 && auditeList.size() == 0){//只有未审核的asn情况
				if(subQty11 >= 0){
					//剩余数量等于修改数量-存在的发货单的数量保留发货单
					qty = subQty1;
					
					vo.setDshl(newOrderQty.toString());
					vo.setSurQty(qty);
				}else{
					qty = newOrderQty;
					
					vo.setDshl(newOrderQty.toString());
					vo.setSurQty(qty);
					//取消发货单
					for(DeliveryItemEntity   deliveryItemEntity :   unAuditeList){
						PurchaseOrderItemPlanEntity plan = deliveryItemEntity.getOrderItemPlan();
						cancelDlv(deliveryItemEntity,iLogger,plan);
						writeQtyByPlan(plan,iLogger);
						plan.setAbolished(1);
						purchaseOrderItemPlanDao.save(plan);
						
					}
				}
			}else if(unAuditeQty == 0 && unAuditeList.size() == 0 ){//只有已审核的asn 情况
				if(subQty11 >= 0){
					//剩余数量等于修改数量-存在的发货单的数量保留发货单
					qty = subQty1;
					vo.setDshl(newOrderQty.toString());
					vo.setSurQty(qty);
					
				}else{
					qty = 0d;
					vo.setDshl(auditedeQty.toString());
					vo.setSurQty(qty);	
				}

			}else{//混合的情况
				if(subQty11 >= 0){
					//当已审核的大于等于修改数量 切 未审核的数量部位0
					Double  num1=BigDecimalUtil.sub(newOrderQty, auditedeQty);
					Double num2=Math.abs(num1);//改小了多少
					if( num1 >=  unAuditeQty){
						qty = subQty1;
						vo.setDshl(newOrderQty.toString());
						vo.setSurQty(qty);
					}else{
						qty = num2;
						vo.setDshl(newOrderQty.toString());
						vo.setSurQty(qty);
						//取消发货单
						for(DeliveryItemEntity   deliveryItemEntity :   unAuditeList){
							PurchaseOrderItemPlanEntity plan = deliveryItemEntity.getOrderItemPlan();
							cancelDlv(deliveryItemEntity,iLogger,plan);
							writeQtyByPlan(plan,iLogger);
							plan.setAbolished(1);
							purchaseOrderItemPlanDao.save(plan);
						}
					}
				}else{
					//修改量小于等于已审核的数量
					if(newOrderQty <= auditedeQty ){
						qty = 0d;
						vo.setDshl(auditedeQty.toString());
						vo.setSurQty(qty);
					}else{//修改量大于已审核的数量
						//当已审核的大于等于修改数量 切 未审核的数量部位0
						Double  num11=BigDecimalUtil.sub(newOrderQty, auditedeQty);
						Double num22=Math.abs(num11);//改小了多少
						qty = num22;
						vo.setDshl(newOrderQty.toString());
						vo.setSurQty(qty);
						
						//取消发货单
						for(DeliveryItemEntity   deliveryItemEntity :   unAuditeList){
							PurchaseOrderItemPlanEntity plan = deliveryItemEntity.getOrderItemPlan();
							cancelDlv(deliveryItemEntity,iLogger,plan);
							writeQtyByPlan(plan,iLogger);
							plan.setAbolished(1);
							purchaseOrderItemPlanDao.save(plan);
							
						}
						
					}

				}
			}
	
		}
		return vo;
		
	}
	/**
	 * 回写释放订单数量
	 */
	public void writeQtyByPlan(PurchaseOrderItemPlanEntity plan,ILogger iLogger){
		PurchaseOrderItemEntity orderItem=plan.getOrderItem();
		//old Data 
		Double oldOrderQty = orderItem.getSurOrderQty();
		Double oldBaseQty = orderItem.getSurBaseQty();
		
		Double orderQty = BigDecimalUtil.add(orderItem.getSurOrderQty(), plan.getOrderQty());
		orderItem.setSurOrderQty(orderQty);
		if(!StringUtils.isEmpty( orderItem.getBPUMZ())   &&   ! StringUtils.isEmpty( orderItem.getBPUMN()) ){			
			Double  number = orderQtyToBaseQty(orderItem.getBPUMN(),orderItem.getBPUMZ(),plan.getOrderQty());
			orderItem.setSurBaseQty(BigDecimalUtil.add(orderItem.getSurBaseQty(), number));
		}
		purchaseOrderItemDao.save(orderItem);
		iLogger.log("订单明细剩余数量变化，订单为"+orderItem.getOrder().getOrderCode()+"-"+orderItem.getItemNo());
		iLogger.log(",原剩余订单数量为"+oldOrderQty+",改为数量"+orderItem.getSurOrderQty());
		iLogger.log(",原剩余基本数量为"+oldBaseQty+",改为数量"+orderItem.getSurBaseQty());
		iLogger.log("\r\n");	
	}
	
	
	/**
	 * 回写要货计划
	 */
	public void writeGoodsQtyByPlan(PurchaseOrderItemPlanEntity plan,ILogger iLogger){
		
		PurchaseGoodsRequestEntity goods = plan.getPurchaseGoodsRequest();
		
		//old Data 
		Double oldBaseQty =Double.valueOf(goods.getSurQry());

		//now Data 
		if(!StringUtils.isEmpty( plan.getOrderItem().getBPUMZ())   &&   ! StringUtils.isEmpty( plan.getOrderItem().getBPUMN()) ){			
			Double  number = orderQtyToBaseQty( plan.getOrderItem().getBPUMN(), plan.getOrderItem().getBPUMZ(),plan.getOrderQty());
			Double  count = BigDecimalUtil.add(Double.valueOf(goods.getSurQry()), number);
			goods.setSurQry(count.toString());
		} 
		
		requestDao.save(goods);
		iLogger.log("要货计划数量余量变，");
		iLogger.log(",原剩余基本数量为"+oldBaseQty+",改为数量"+goods.getSurQry());
		iLogger.log("\r\n");	
	}
	
	
	
	
	
	
	//订单单位转基本单位
	public Double orderQtyToBaseQty(String fenzi ,String fenmu ,Double qty ){
		Double ty = qty;
		if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
			Double   fennzi  =  Double.valueOf(fenzi);//分子
			Double   fennmu  =  Double.valueOf(fenmu);  //分母
			ty =BigDecimalUtil.div(  BigDecimalUtil.mul(qty, fennmu, 3)  , fennzi, 3) ;
		}
		return ty;
	}
	
	//基本单位转转订单单位
	public Double baseQtyToOrderQty(String fenzi ,String fenmu ,Double qty ){
		Double ty = qty;
		if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
			Double   fennzi  =  Double.valueOf(fenzi);//分子
			Double   fennmu  =  Double.valueOf(fenmu);  //分母
			ty = BigDecimalUtil.mul( qty, BigDecimalUtil.div(fennzi, fennmu, 3) ,3);
		}
		return ty;
	}
	
	
	
	/**
	 * 释放看板的发货数量  取消发货单
	 * @param dlv
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public void cancelDlv(DeliveryItemEntity Item,ILogger iLogger,PurchaseOrderItemPlanEntity plan) throws Exception{
		DeliveryEntity delivery=Item.getDelivery();
		List<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
		deliveryList.add(delivery);
		deliveryService.cancelUnAuditDelivery(deliveryList);
		

	}
	
	
	
	
	/**
	 * 回写要货计划数量 释放订单数量
	 * 当完全删除plan时，qty参数就是plan的数量
	 * 当更新plan是，qty是需要处理扣除的量
	 */
	public void writeQtyByPlanAndOrder(PurchaseOrderItemPlanEntity plan,Double qty,ILogger log){
		PurchaseOrderItemEntity orderItem=plan.getOrderItem();
		
/*		PurchaseGoodsRequestEntity request=plan.getPurchaseGoodsRequest();

		//1、释放到要货计划
		qty=orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), qty);
		double surQry =Double.valueOf(request.getSurQry());
		request.setSurQry(""+BigDecimalUtil.add(surQry, qty));
		purchaseGoodsRequestDao.save(request);
		log.log("要货单数量变化，单据为"+request.getMaterialCode()+"-"+request.getRq());
		log.log(",原剩余数量为"+surQry+",处理数量"+qty);
		log.log("\r\n");*/
		
		
		
		//2、更新行剩余匹配数量更新更新
		orderItem.setSurOrderQty(BigDecimalUtil.add(orderItem.getSurOrderQty(),qty));
		orderItem.setSurBaseQty(orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), orderItem.getSurOrderQty()));
		purchaseOrderItemDao.save(orderItem);
		log.log("订单明细剩余数量变化，订单为"+orderItem.getOrder().getOrderCode()+"-"+orderItem.getItemNo());
		log.log(",原剩余数量为"+orderItem.getSurOrderQty()+",处理数量"+qty);
		log.log("\r\n");

	}
	
	/**
	 * 获取当前及以后有效的要货计划
	 * @param factoryId
	 * @param materialCodeList
	 * @return
	 */
	public Map<String,Long> findEffectRequest(Long factoryId,List<String> materialCodeList){
		StringBuffer str = new StringBuffer();
		str.append(" SELECT");
		//KEY=FACTORY_CODE+MATERIAL_CODE+VENDOR_CODE+RQ
		str.append(" D.CODE||'_'||B.CODE||'_'||C.CODE||'_'||TO_CHAR (A.RQ, 'YYYYMMDD')||'_'||A.FLAG AS KEY,A.ID AS VALUE");
		str.append(" FROM");
		str.append(" PURCHASE_GOODS_REQUEST A");
		str.append(" LEFT JOIN QEWEB_MATERIAL B ON A .MATERIAL_ID = B. ID");
		str.append(" LEFT JOIN QEWEB_ORGANIZATION C ON A.VENDOR_ID=C.ID");
		str.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID=D.ID");
		str.append(" WHERE");
		str.append(" A .ABOLISHED = 0");
		str.append(" AND A .RQ >= TO_DATE (");
		str.append(" TO_CHAR (SYSDATE, 'YYYY-MM-DD'),");
		str.append(" 'YYYY-MM-DD'");
		str.append(" )");
		if(null!=factoryId){
			str.append(" AND A .FACTORY_ID =").append(factoryId); 
		}
		if(CommonUtil.isNotNullCollection(materialCodeList)){
			StringBuffer materialStr =new StringBuffer();
			for (String code : materialCodeList) {
				materialStr.append("'").append(code).append("'").append(",");
			}
			if(materialStr.length()>0){
				str.append(" AND B.CODE IN (").append(materialStr.substring(0, materialStr.length()-1).toString()).append(")");
			}
			
		}
		
		List<?> data=generalDao.queryBySql_map(str.toString());
		if(CommonUtil.isNotNullCollection(data)){
			Map<String,Long> map =new HashMap<String, Long>();
			for(Object o:data){
				Map<String,Object> m =  (Map<String, Object>) o;
				String key =m.get("KEY").toString();
				Long value =((BigDecimal)m.get("VALUE")).longValue();
				map.put(key, value);
			}
			return map;
		}
		return null;
	}
	
}