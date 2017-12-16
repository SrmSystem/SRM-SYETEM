package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.sap.OrderReceiveSAP;
import com.qeweb.sap.vo.OrderReceiveVo;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;

/**
 * 订单收货SAP->SRM
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class OrderReceiveSyncService {
	@Autowired
	private DeliveryItemDao  deliveryItemDao;
	
	@Autowired
	private DeliveryDao  deliveryDao;
	
	@Autowired
	private ReceiveItemDao receiveItemDao;
	
	@Autowired
	private ReceiveDao receiveDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private  PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private 	WarnMainService warnMainService;
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	public boolean execute(ILogger iLogger,String sysTimeS,String sysTimeE)throws Exception {
		   iLogger.log("OrderReceiveSyncService execute method execute start");
		   List<ReceiveItemEntity> recItemList = new ArrayList<ReceiveItemEntity>();
		   List<OrderReceiveVo> res=OrderReceiveSAP.getReceiveByDn(sysTimeS,sysTimeE,iLogger);
		   if(res!=null){
			   iLogger.log("总条数:"+res.size());
			   
			   for (OrderReceiveVo orderReceiveVo : res) {
				   DeliveryItemEntity dlvItem =deliveryItemDao.findDeliveryItemByDnAndShipType(orderReceiveVo.getVBELN(),PurchaseConstans.SHIP_TYPE_NORMAL);
				   if(null!=dlvItem){
				   ReceiveItemEntity recItem = receiveItemDao.getReceiveItemByDlv(dlvItem.getId());
				   PurchaseOrderItemPlanEntity orderPlan = purchaseOrderItemPlanDao.findById(dlvItem.getOrderItemPlan().getId());
				   PurchaseOrderItemEntity orderItem=orderPlan.getOrderItem();
//				   Boolean warnFlag = false;
				   //1、更新收货主表及子表
				   if(recItem!=null){
					   iLogger.log("已存在收货明细，更新操作");
				   }else{
					   ReceiveEntity rec=receiveDao.getByDeliveryId(dlvItem.getDelivery().getId());
					   if(rec!=null){
						   iLogger.log("存在收货单，更新收货明细");
					   }else{
						   iLogger.log("新增收货单，新增收货明细");
						   rec=new ReceiveEntity();
						   rec.setIsOutData(1);
						   rec.setAbolished(0);
						   rec.setReceiveCode(serialNumberService.geneterNextNumberByKeyIsOutData("ORDER_REC"));
						   rec.setDelivery(dlvItem.getDelivery());
						   rec.setVendor(dlvItem.getDelivery().getVendor());
						   rec.setReceiveTime(DateUtil.getCurrentTimestamp());
						   rec.setReceiveStatus(1);
						   rec.setVendor(dlvItem.getOrderItem().getOrder().getVendor());
						   //保存采购组信息
						   DeliveryEntity delivery = deliveryDao.findById(dlvItem.getDelivery().getId());
						   if(delivery!=null){
							   rec.setPurchasingGroupCode(delivery.getPurchasingGroupCode()); //采购组编码
							   rec.setPurchasingGroupId(delivery.getPurchasingGroupId()); 	//采购组Id
						   }
						   
						   receiveDao.save(rec);
					   }
					   recItem=new ReceiveItemEntity();
					   recItem.setIsOutData(1);
					   recItem.setAbolished(0);
					   recItem.setReceive(rec);
					   recItem.setDeliveryItem(dlvItem);
					   recItem.setOrderItemPlan(dlvItem.getOrderItemPlan());
					   recItem.setOrderItem(dlvItem.getOrderItem());
					   recItem.setReplDeliveryStatus(0);
//					   warnFlag = true; //新增必须添加消息的
				   }  
				   
//				   if(!warnFlag){//修改的判断是否有值改变
//					   if(validateData(recItem,orderReceiveVo)){
//						   warnFlag = true;
//					   }
//				   }
				   
				   recItem.setReceiveQty(orderReceiveVo.getLFIMG());//实际已交货量（按销售单位）
				   recItem.setZdjsl(orderReceiveVo.getZDJSL());//待检数量
				   recItem.setZzjbl(orderReceiveVo.getZZJBL());//质检不合格
				   recItem.setZsjhg(orderReceiveVo.getZSJHG());//送检合格
				   recItem.setZllbl(orderReceiveVo.getZLLBL());//来料不合格
				   recItem.setBalanceTime(DateUtil.stringToTimestamp(orderReceiveVo.getBUDAT()+" "+orderReceiveVo.getCPUTM(), "yyyyMMdd HHmmss"));
				   recItem.setIsOutData(1);
				   receiveItemDao.save(recItem);
				   
				   recItemList.add(recItem);
				   
				   //2、更新发货单
				   updateDeliveryReceiveStatus(dlvItem,recItem.getReceiveQty());
					
				   //3、更新供货计划
				   //更新订单计划的收货数量。在途数量,退货数量
				   updateOrderItemPlanReceiveStatus(orderPlan);
				   
				   //4、 更新订单明细
				   updateOrderItemReceiveStatus(orderPlan.getOrderItem());
				   
				   //5、更新订单主表
				   updateOrderReceiveStatus(orderItem.getOrder());
				   
				   //6.添加消息-供应商采购商都有添加
/*				   if(warnFlag){
					   ReceiveEntity temReceive=receiveDao.findOne(recItem.getId());
					   //采购商
					   List<Long> userIdList1 = new ArrayList<Long>();
						 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(temReceive.getPurchasingGroupId());
						 for(GroupConFigRelEntity rel : userOrderList ){
							 userIdList1.add(rel.getUserId());
						 }
					     warnMainService.warnMessageSet(temReceive.getId(), WarnConstant.DN_UPDATE_FOR_PURCHASER, userIdList1);
					     
					     //供应商
						List<UserEntity> users = userServiceImpl.findByCompany(temReceive.getVendor().getId());
					    List<Long> userIdList2 = new ArrayList<Long>();
					    for( UserEntity us :  users){
					    	userIdList2.add(us.getId());
						 }
						warnMainService.warnMessageSet(temReceive.getId(), WarnConstant.DN_UPDATE_FOR_VENDOR, userIdList2);
					     
				   }*/
				   }
				   
				   
				   
                   //////补货的发货明细收货状态为已收货
				   if(StringUtils.isNotEmpty(orderReceiveVo.getREPLCODE())){
					   DeliveryItemEntity replDlvItem =deliveryItemDao.findDeliveryItemByDnAndDeliveryCodeAndShipType(orderReceiveVo.getVBELN(),orderReceiveVo.getREPLCODE(),PurchaseConstans.SHIP_TYPE_REPL);
					  //6.更新发货主表的收货状态
					   if(null!=replDlvItem){
					    updateDeliveryReceiveStatus(replDlvItem,null);
					   }
				   }
				   /////
			   }
				  
				 //若存在退货，则需要修改供货计划进行补货
				updateReceiveItems(recItemList);
				   //补货
		   
		   }else{
			    iLogger.log("SAP连接失败");
				return false;
			}
		    iLogger.log("OrderReceiveSyncService execute method execute end");
			return true;
	}
	

	public void updateReceiveItems(List<ReceiveItemEntity> saveItemList){
		if(null!=saveItemList && saveItemList.size()>0){
			for (ReceiveItemEntity receiveItemEntity : saveItemList) {
				updateReceiveItem(receiveItemEntity);
				//更新原计划的在途数量，未发货数量且更新订单明细的在途数量和未发数量
				updateSourceOnWayUnDlvQty(receiveItemEntity,receiveItemEntity.getOrderItemPlan());
			}
		}
	}
	
	/**
	 * 更新原计划的在途数量，未发货数量且更新订单明细的在途数量和未发数量
	 * @param receiveItemEntity
	 * @param orderPlan
	 */
	public void updateSourceOnWayUnDlvQty(ReceiveItemEntity receiveItemEntity,PurchaseOrderItemPlanEntity orderPlan){
		       ///原供货计划更新///////////
				       //在途=发货（普通的发货+补货的发货）-收货
						List<Long> planIds = new ArrayList<Long>();
						//【1】获取该供货计划下的补货id
						List<Long> replPlanIds=purchaseOrderItemPlanDao.findReplPlanIdsBySourcePlanId(orderPlan.getId());
						if(null!=replPlanIds && replPlanIds.size()>0){
							planIds.addAll(replPlanIds);
						}
						//【2】增加普通供货计划id
						planIds.add(orderPlan.getId());
						//【3】根据所有id查发货数量
						Double hasDeliveryQty=deliveryItemDao.findHasDlvQtyByPlanIds(planIds);
						//【4】在途=发货（普通的发货+补货的发货）-收货(收货数量永远是原单号的收货数量+补货的收货数量是收货状态为1的发货单号)
						Double onWayQty = BigDecimalUtil.sub(null==hasDeliveryQty?0:hasDeliveryQty, receiveItemEntity.getReceiveQty());
						if(null!=replPlanIds && replPlanIds.size()>0){
						Double replHasRecQty =deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanIds.get(0));
						onWayQty=BigDecimalUtil.sub(onWayQty, null==replHasRecQty?0:replHasRecQty);
						}
						orderPlan.setOnwayQty(onWayQty);
						
						//【5】未发数量=orderQty(普通的订单数量+普通的订单数量)-deliveryQty(普通+补货)-toDeliveryQty(普通+补货)
						Double orderQty=purchaseOrderItemPlanDao.findOrderQtyByIds(planIds);
						orderQty=null==orderQty?0:orderQty;
						Double deliveryQty=purchaseOrderItemPlanDao.findDeliveryQtyByIds(planIds);
						deliveryQty=null==deliveryQty?0:deliveryQty;
						Double toDeliveryQty=purchaseOrderItemPlanDao.findToDeliveryQtyByIds(planIds);
						toDeliveryQty=null==toDeliveryQty?0:toDeliveryQty;
						Double undeliveryQty = BigDecimalUtil.sub(orderQty,BigDecimalUtil.add(deliveryQty, toDeliveryQty));
						orderPlan.setUndeliveryQty(undeliveryQty);
						
						//差异数量：orderQty-收货数量
						Double diffQty = BigDecimalUtil.sub(orderPlan.getOrderQty(),receiveItemEntity.getReceiveQty());
						orderPlan.setDiffQty(diffQty);
						purchaseOrderItemPlanDao.save(orderPlan);
						purchaseOrderItemPlanDao.save(orderPlan);
				 ///原供货计划更新end///////////
				
				
				////原订单明细更新//////////////
						PurchaseOrderItemEntity orderItem=orderPlan.getOrderItem();
						//【6】在途=发货（普通的发货+补货的发货）-收货（收货数量永远是原单号的收货数量+补货的收货数量是收货状态为1的发货单号）
						Double orderItemHasDeliveryQty=deliveryItemDao.findAllHasDlvQtyByOrderItemId(orderItem.getId());
						orderItemHasDeliveryQty=null==orderItemHasDeliveryQty?0:orderItemHasDeliveryQty;
						Double orderItemReceiveQty=receiveItemDao.getOrderItemReceiveQty(orderItem.getId());
						orderItemReceiveQty=null==orderItemReceiveQty?0:orderItemReceiveQty;
						Double orderItemOnwayQty=BigDecimalUtil.sub(orderItemHasDeliveryQty,orderItemReceiveQty);
						//补货的收货数量
						Double orderItemReplHasRecQty =deliveryItemDao.findHasDlvQtyRecByItemId(orderItem.getId());
						orderItemOnwayQty=BigDecimalUtil.sub(orderItemOnwayQty,null==orderItemReplHasRecQty?0:orderItemReplHasRecQty);
						orderItem.setOnwayQty(orderItemOnwayQty);
						
						//【7】未发数量=orderQty(orderQty+退货数量)-deliveryQty(普通+补货)-toDeliveryQty(普通+补货)
						Double orderItemOrderQty=BigDecimalUtil.add(null==orderItem.getOrderQty()?0:orderItem.getOrderQty(), null==orderItem.getReturnQty()?0:orderItem.getReturnQty());
						orderItemOrderQty=null==orderItemOrderQty?0:orderItemOrderQty;
						Double orderItemDeliveryQty = purchaseOrderItemPlanDao.findDeliveryQtyByOrderItemId(orderItem.getId());
						orderItemDeliveryQty=null==orderItemDeliveryQty?0:orderItemDeliveryQty;
						Double orderItemToDeliveryQty = purchaseOrderItemPlanDao.findToDeliveryQtyByOrderItemId(orderItem.getId());
						orderItemToDeliveryQty=null==orderItemToDeliveryQty?0:orderItemToDeliveryQty;
						Double orderItemUndeliveryQty=BigDecimalUtil.sub(orderItemOrderQty, BigDecimalUtil.add(orderItemDeliveryQty, orderItemToDeliveryQty));
						orderItem.setUndeliveryQty(orderItemUndeliveryQty);
						
						//差异数量：orderQty-收货数量
						Double orderItemDiffQty = BigDecimalUtil.sub(orderItem.getOrderQty(),orderItem.getReceiveQty());
						orderItem.setDiffQty(orderItemDiffQty);
						purchaseOrderItemPlanDao.save(orderPlan);
						purchaseOrderItemDao.save(orderItem);
				////原订单明细更新end///////////
	}
	
	
	
	public void updateReceiveItem(ReceiveItemEntity recItem){
		PurchaseOrderItemPlanEntity replPlan;
		Double upQty;//增加数量
		Double subQty;//差异数量
		List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
	    //剩余补货数=实际已交货量-待检数量-送检合格
		Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
		if(null!=replPlanList && replPlanList.size()>0){
			//(1)有补货
		     //1、补货数量增加
			replPlan=replPlanList.get(0);
			////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
			Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlan.getId());
			hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
			Double shouldOrderQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
			///////
			subQty=BigDecimalUtil.sub(shouldOrderQty, replPlan.getOrderQty());
		    upQty=subQty;
		}else{
			//还没创建供货计划,则增加的是不良数
			upQty=badNumber;
		}
		
		updatePlan(recItem,upQty,replPlanList);
	}
	
	/**
	 * 更新补货的供货关系
	 * @param recItem 收货明细
	 * @param upQty 不良数增加的个数
	 * @param replPlanList 已存在的补货的供货关系
	 */
	public void updatePlan(ReceiveItemEntity recItem,Double upQty,List<PurchaseOrderItemPlanEntity> replPlanList){
		PurchaseOrderItemPlanEntity oldPlan=recItem.getOrderItemPlan();
		PurchaseOrderItemPlanEntity replPlan = null;
		if(null!=replPlanList && replPlanList.size()>0){
			replPlan=replPlanList.get(0);
		}
		//1、直接在补货供货计划上增加数量
		if(upQty>0){
			if(null==replPlan){
				replPlan =new PurchaseOrderItemPlanEntity();
			    BeanUtil.copyProperties(oldPlan, replPlan);
			    replPlan.setShipType(PurchaseConstans.SHIP_TYPE_REPL);
			    replPlan.setCreateTime(DateUtil.getCurrentTimestamp());
			    replPlan.setDn(recItem.getDeliveryItem().getDn());//DN号
			    replPlan.setSourceOrderItemPlanId(oldPlan.getId());
			    replPlan.setRecItemId(recItem.getId());
			    //基本数据还原
			    replPlan.setRequestTime(null);//清除要求到货时间
			    replPlan.setOrderQty(0D);
			    replPlan.setId(0);
			    replPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			    replPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			    replPlan.setDeliveryQty(0D);//已发数量
			    replPlan.setToDeliveryQty(0D);// 已创建未发数量
			    replPlan.setReceiveQty(0D);// 实收数量
			    replPlan.setReturnQty(0D);//退货数量
			    replPlan.setDiffQty(0D);// 差异数量
			    replPlan.setOnwayQty(0D);// 在途数量
			    replPlan.setUndeliveryQty(0D);// 未发数量
			    ///
			}
			replPlan.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		    replPlan.setAbolished(BOHelper.UNABOLISHED_SINGEL);
		    //供货计划数量
		    Double orderQty=BigDecimalUtil.add(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(), upQty);
		    replPlan.setOrderQty(orderQty);
		    replPlan.setBaseQty(orderQtyToBaseQty(replPlan.getOrderItem().getBPUMN(), replPlan.getOrderItem().getBPUMZ(), replPlan.getOrderQty()));
		    
		    commonPlanStatus(replPlan);
			purchaseOrderItemPlanDao.save(replPlan);
		}else{
			//2、判断是否可以删除
			//判断是否有发货单，无则直接变更，有则不变
			if(null!=replPlan && replPlan.getId()>0){
			List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(replPlan.getId());
				if(null==deliveryList || deliveryList.size()==0){
					 //供货计划数量
				    Double orderQty=BigDecimalUtil.add(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(), upQty);
				    if(orderQty>0){//有数量 则实时更新
					    replPlan.setOrderQty(orderQty);
					    replPlan.setBaseQty(orderQty);
					    
					    commonPlanStatus(replPlan);
				    }else{//数量减小至0则直接删除
				    	replPlan.setAbolished(BOHelper.ABOLISHED_SINGEL);
				    }
					
				}else{
					//有发货单，看有多少待发
					 Double shouldQty =BigDecimalUtil.sub(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(), BigDecimalUtil.add(null==replPlan.getDeliveryQty()?0:replPlan.getDeliveryQty(), null==replPlan.getToDeliveryQty()?0:replPlan.getToDeliveryQty()));
					 Double subQty=BigDecimalUtil.add(null==shouldQty?0:shouldQty, upQty);
					 if(subQty>=0){
						 //说明足够扣减
						 Double orderQty=BigDecimalUtil.add(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(), upQty);
						 replPlan.setOrderQty(orderQty);
						 replPlan.setBaseQty(orderQty);
						 commonPlanStatus(replPlan);
					 }else{
						 //不够扣减，直接先减去待发的
						 Double orderQty=BigDecimalUtil.sub(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(), shouldQty);
						 replPlan.setOrderQty(orderQty);
						 replPlan.setBaseQty(orderQtyToBaseQty(replPlan.getOrderItem().getBPUMN(), replPlan.getOrderItem().getBPUMZ(), replPlan.getOrderQty()));
						 commonPlanStatus(replPlan);
					 }
				}
				purchaseOrderItemPlanDao.save(replPlan);
			}
		}
	}
	
	public void commonPlanStatus(PurchaseOrderItemPlanEntity replPlan ){
		if(replPlan.getId()>0){
			   //发货数量=实时查询
				Double hasDelQty = deliveryItemDao.findDlvQtyByPlanId(replPlan.getId(), PurchaseConstans.DELIVERY_STATUS_YES);
				hasDelQty=null==hasDelQty?0:hasDelQty;
				replPlan.setDeliveryQty(hasDelQty<0?0D:hasDelQty);
				
				//已创建未发数量=实时查询
				Double toDeliveryQty = deliveryItemDao.findDlvQtyByPlanId(replPlan.getId(), PurchaseConstans.DELIVERY_STATUS_NO);
				toDeliveryQty=null==toDeliveryQty?0:toDeliveryQty;
				replPlan.setToDeliveryQty(toDeliveryQty>0?toDeliveryQty:0D);
				
				//在途
				Double onwayQty=deliveryItemDao.findOnWayQtyByPlanId(replPlan.getId());
				onwayQty=null==onwayQty?0:onwayQty;
				replPlan.setOnwayQty(onwayQty);
			}
			
			  //未发
			Double undeliveryQty = BigDecimalUtil.sub(replPlan.getOrderQty(), BigDecimalUtil.add(replPlan.getDeliveryQty(), replPlan.getToDeliveryQty()));
			undeliveryQty=null==undeliveryQty?0:undeliveryQty;
			replPlan.setUndeliveryQty(undeliveryQty>replPlan.getOrderQty()?replPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
			
			//更新订单发货状态
			double deliveryQty = BigDecimalUtil.add(replPlan.getDeliveryQty(), replPlan.getToDeliveryQty());
			if(deliveryQty == 0) {	
				replPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				//部分发货
			} else if(deliveryQty > 0 && deliveryQty < replPlan.getOrderQty()) {
				replPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART); 
			// 供货计划已发货
			} else if(deliveryQty >= replPlan.getOrderQty() || replPlan.getUndeliveryQty()==0){  
				replPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}
			replPlan.setIsOutData(1);
	}
	
	
	/**
	 * 更新发货单收货数据
	 * @param deliverItemMap
	 */
	private void updateDeliveryReceiveStatus(DeliveryItemEntity dlvItem,Double receiveQty) {
			/////更新发货单子表
		if(null!=receiveQty){
		   dlvItem.setReceiveQty(receiveQty);
		}
		   dlvItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
		   deliveryItemDao.save(dlvItem);

			//更新发货主单收货状态
		    int count_all=0;
			int count_part=0;
			DeliveryEntity delivery =dlvItem.getDelivery();
			Set<DeliveryItemEntity> deliveryItemSet =delivery.getDeliveryItem();
			if(null!=deliveryItemSet && deliveryItemSet.size()>0){
				for(DeliveryItemEntity item : deliveryItemSet) {
					if(PurchaseConstans.RECEIVE_STATUS_PART.intValue()==item.getReceiveStatus().intValue()){
						count_part++;
					}else if(PurchaseConstans.RECEIVE_STATUS_YES.intValue()==item.getReceiveStatus().intValue()){
						count_all++;
					}
				}
				
				if(count_all>0 && count_all==deliveryItemSet.size()){
					delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
				}else if(count_all>0 || count_part>0){
					delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
				}else{
					delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				}
			}
			deliveryDao.save(delivery);
	}

	/**
	 * 更新供货计划收货状态---收货数量和在途数据以及退货数量都要实时查询所有的单子，包括补货
	 * @param orderItemPlanIdSet
	 */
	private void updateOrderItemPlanReceiveStatus(PurchaseOrderItemPlanEntity orderPlan) {
		//供货计划的收货数量
		Double receiveQty = receiveItemDao.getOrderItemPlanReceiveQty(orderPlan.getId());
		receiveQty = null==receiveQty?0:receiveQty;
		orderPlan.setReceiveQty(receiveQty);
		
		//退货数量=收货的质检不合格和来料不合格
		//来料不良
		Double zllbl = receiveItemDao.getItemPlanZllblQty(orderPlan.getId());
		//质检不良
		Double zzjbl = receiveItemDao.getItemPlanZzjblQty(orderPlan.getId());
		Double returnQty = BigDecimalUtil.add(null==zllbl?0:zllbl, null==zzjbl?0:zzjbl);
		orderPlan.setReturnQty(returnQty);
		
		//收货状态
		if(receiveQty != null) {
			orderPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES); 
		}
		purchaseOrderItemPlanDao.save(orderPlan);
	}

	/**
	 * 更新订单明细行收货状态
	 * @param orderItemIdSet
	 */
	private void updateOrderItemReceiveStatus(PurchaseOrderItemEntity orderItem) {
		//订单明细的收货数量
		Double receiveQty = receiveItemDao.getOrderItemReceiveQty(orderItem.getId());
		receiveQty = null==receiveQty?0:receiveQty;
		orderItem.setReceiveQty(receiveQty);
		
		//退货数量=收货的质检不合格和来料不合格
		//来料不良
		Double zllbl = receiveItemDao.getOrderItemZllblQty(orderItem.getId());
		//质检不良
		Double zzjbl = receiveItemDao.getOrderItemZzjblQty(orderItem.getId());
		Double returnQty = BigDecimalUtil.add(null==zllbl?0:zllbl, null==zzjbl?0:zzjbl);
		orderItem.setReturnQty(returnQty);
		
		
		//收货状态
		List<PurchaseOrderItemPlanEntity> orderItemPlan = purchaseOrderItemPlanDao.findItemPlanEntitysByItem(orderItem.getId());
		if(null!=orderItemPlan && orderItemPlan.size()>0){
			int count_all=0;
		    int count_part=0;
			for(PurchaseOrderItemPlanEntity itemPlan : orderItemPlan) {
				if(itemPlan.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_YES.intValue()) {
					count_all++;
				}else if(itemPlan.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()) {
					count_part++;
				}
			}
			if(count_all>0 && count_all==orderItemPlan.size()){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			}else{
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			}
		}
		purchaseOrderItemDao.save(orderItem);
	}
	
	/**
	 * 更新订单收货状态
	 * @param orderIdSet
	 */
	private void updateOrderReceiveStatus(PurchaseOrderEntity order) {
		Set<PurchaseOrderItemEntity>  orderItem = null;
		orderItem = order.getOrderItem();
			
		if(null!=orderItem && orderItem.size()>0){
			int count_all=0;
			int count_part=0;
			for(PurchaseOrderItemEntity item : orderItem) {
				if(item.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_YES.intValue()) {
					count_all++;
				}else if(item.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()) {
					count_part++;
				}
			}
			if(count_all>0 && count_all==orderItem.size()){
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			}else{
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			}
		   purchaseOrderDao.save(order);
		}
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
	
	//验证数据是否进行修改
	public Boolean validateData(ReceiveItemEntity item ,OrderReceiveVo vo){
		boolean isChange=false;
		if(null!=item.getReceiveQty() && null!=vo.getLFIMG() && !item.getReceiveQty().equals(vo.getLFIMG())){
			isChange=true;
		}else if(null!=item.getZdjsl() && null!=vo.getZDJSL() && !item.getZdjsl().equals(vo.getZDJSL())){
			isChange=true;
		}else if(null!=item.getZzjbl() && null!=vo.getZZJBL() && !item.getZzjbl().equals(vo.getZZJBL())){
			isChange=true;
		}else if(null!=item.getZsjhg() && null!=vo.getZSJHG() && !item.getZsjhg().equals(vo.getZSJHG())){
			isChange=true;
		}else if(null!=item.getZllbl() && null!=vo.getZLLBL() && !item.getZllbl().equals(vo.getZLLBL())){
			isChange=true;
		}
		return isChange;
	}
		
}
