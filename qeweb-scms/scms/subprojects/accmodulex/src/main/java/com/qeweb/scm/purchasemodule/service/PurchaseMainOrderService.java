package com.qeweb.scm.purchasemodule.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.sap.PurchaseOrderSAP;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgItemEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgLogEntity;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgDao;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgItemDao;
import com.qeweb.scm.baseline.common.repository.InterfaceMsgLogDao;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.FeedbackService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.OrderType;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanRejectEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanRejectDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderItemTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;


/**
 * 采购订单管理service
 */
@Service
@Transactional
public class PurchaseMainOrderService {

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private  ReceiveService receiveService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	InterfaceMsgDao msgDao;
	@Autowired
	InterfaceMsgItemDao msgItemDao;
	@Autowired
	InterfaceMsgLogDao msgLogDao;
	
	@Autowired
	PurchaseOrderItemPlanRejectDao itemPlanRejectDao;
	

	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;

	@Autowired
	private DeliveryItemDao deliveryItemDao;
	
	@Autowired
	private ReceiveItemDao receiveItemDao;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private 	WarnMainService warnMainService;
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	/**
	 * 获取订单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseOrderEntity> getPurchaseOrders(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		//PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderEntity.class);
		return purchaseOrderDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取订单列表(所有的)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderEntity> findAllOrderMain() {
		return purchaseOrderDao.findAll();
	}
	
	
	/**
	 * 获取订单主表
	 * @param id
	 * @return
	 */
	public PurchaseOrderEntity getOrder(Long id) {
		return purchaseOrderDao.findOne(id);
	}
	
	/**
	 * 获取收货數量-实时查询
	 * @param id
	 * @return
	 */
	public Double getOrderItemReceiveQty(Long orderItemId){
		//收货数量
		Double hasReceiveQty= receiveItemDao.getOrderItemReceiveQty(orderItemId);
		return null==hasReceiveQty?0:hasReceiveQty;
	}
	
	/**
	 * 获取在途數量-实时查询（已发-已收）
	 * @author chao.gu 
	 * 20171118
	 * @param id
	 * @return
	 */
	public Double getOrderItemOnwayQty(Long orderItemId){
		//在途数量
		Double onWayQty=deliveryItemDao.findOnWayQtyByItemId(orderItemId);
		onWayQty =null==onWayQty?0:onWayQty;
		return onWayQty;
	}
	
	/**
	 * 获取未發數量
	 * @param id
	 * @return
	 */
	public Double getOrderItemUndeliveryQty(Long orderItemId){
		return purchaseOrderItemPlanDao.getOrderItemUndeliveryQty(orderItemId);
	}
	
	
	
	/**
	 * 获取订单明细
	 * @param id
	 * @return
	 */
	public PurchaseOrderItemEntity getOrderItem(Long id) {
		PurchaseOrderItemEntity orderItem=purchaseOrderItemDao.findOne(id);
		Double onWayQty=deliveryItemDao.findOnWayQtyByItemId(orderItem.getId());//在途数量 add by chao.gu
		orderItem.setOnwayQty(null==onWayQty?0:onWayQty);
		return orderItem;
	}
	
	/**
	 * 获取供货计划
	 * @param id
	 * @return
	 */
	public PurchaseOrderItemPlanEntity getOrderItemPlan(Long id) {
		return purchaseOrderItemPlanDao.findOne(id);
	}
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public UserEntity getUser(Long id) {
		return userDao.findOne(id);
	}
	
	
	
	/**
	 * 修改订单明细数量
	 * @param id
	 * @return
	 */
	public void editOrderItem(PurchaseOrderItemEntity orderItem) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		//查询订单明细
		PurchaseOrderItemEntity temp = purchaseOrderItemDao.findOne(orderItem.getId());
		if(temp != null){
			//修改此订单明细的订单的状态和数量发布状态修改()
			temp.setOrderQty(orderItem.getOrderQty());
			temp.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO.intValue());
			temp.setConfirmUser(null);
			temp.setConfirmTime(null);
			temp.setPublishStatus(0);
			temp.setPublishTime(null);
			temp.setPublishUser(null);
			temp.setVetoReason(null);
			temp.setVetoStatus(null);
			temp.setVetoTime(null);
			temp.setVetoUser(null);
			temp.setIsModify(1);//添加该订单为修改过的订单
			temp.setModifyTime(curr);//添加该订单为修改过的订单时间
			purchaseOrderItemDao.save(temp);
		}
		
	  //更新主表确认状态
	  ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	  PurchaseOrderEntity entity=purchaseOrderDao.findOne(temp.getOrder().getId());
	  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , new UserEntity(user.id),PurchaseConstans.CONFIRM);
			
	}
	
	
	/**
	 * 获取订单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseOrderItemEntity> getPurchaseOrderItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort){ 		
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemEntity.class);
		Page<PurchaseOrderItemEntity> page=purchaseOrderItemDao.findAll(spec,pagin);
		List<PurchaseOrderItemEntity> list = page.getContent();
		setDefaultFeedBackCount(list);
		return page;
	}
	
	
	/**
	 * 获取订单明细详情List
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	
	public List<PurchaseOrderItemEntity> getPurchaseOrderItemList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemEntity.class);
		return purchaseOrderItemDao.findAll(spec);  
	}
	
	
	/**
	 * 获取主订单详情List
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderEntity> getPurchaseOrderList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderEntity.class);
		return purchaseOrderDao.findAll(spec);  
	}
	/**
	 * 导出订单详情(通过明细导出)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderItemTransfer> getOrderItemVo(Map<String, Object> searchParamMap) {
		List<PurchaseOrderItemEntity> list = getPurchaseOrderItemList(searchParamMap);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
	   List<PurchaseOrderItemTransfer> ret = new ArrayList<PurchaseOrderItemTransfer>();
	   PurchaseOrderItemTransfer trans = null;
	   
	   if(list != null && list.size() != 0){
		   for(PurchaseOrderItemEntity orderItem : list ){

			   
				String col1 = receiveService.getOrderItemZllblQty(orderItem.getId()) == null ? "" :   receiveService.getOrderItemZllblQty(orderItem.getId()).toString() ;
			    String col2 = receiveService.getOrderItemZzjblQty(orderItem.getId()) == null ? "" :   receiveService.getOrderItemZzjblQty(orderItem.getId()).toString() ;
			   
			   
			   
			   trans = new PurchaseOrderItemTransfer();
			   trans.setOrderCode(orderItem.getOrder().getOrderCode() == null ?  "" : orderItem.getOrder().getOrderCode()  );
			   trans.setItemNo(orderItem.getItemNo()== null ?"" : orderItem.getItemNo().toString());
			   trans.setPurOrderType(orderItem.getOrder().getPurOrderType() == null ? "" :  orderItem.getOrder().getPurOrderType().getName());
			   trans.setCompanyCode(orderItem.getOrder().getCompany() == null ? "" : orderItem.getOrder().getCompany().getCode());
			   trans.setCompanyName(orderItem.getOrder().getCompany() == null ? "" :orderItem.getOrder().getCompany().getName());
			   if(orderItem.getFactoryEntity() != null){
				   trans.setFactoryCode(orderItem.getFactoryEntity().getCode());
				   trans.setFactoryName(orderItem.getFactoryEntity().getName());
			   }
			   trans.setPstyp(orderItem.getPstyp() == null ? "" : orderItem.getPstyp());
			   trans.setVendorCode(orderItem.getOrder().getVendor() == null ? "" : orderItem.getOrder().getVendor().getCode());
			   trans.setVendorName(orderItem.getOrder().getVendor() == null ? "" : orderItem.getOrder().getVendor().getName());
			   trans.setZtermms(orderItem.getOrder().getZtermms() == null  ? "" : orderItem.getOrder().getZtermms() );
			   trans.setMaterialCode(orderItem.getMaterial() == null ? "" :  orderItem.getMaterial().getCode());
			   trans.setMaterialDescride(orderItem.getMaterial() == null ? ""  :   orderItem.getMaterial().getDescribe());
			   trans.setOrderQty((String) (orderItem.getOrderQty() == null ? 0.00 : orderItem.getOrderQty().toString()));
			   Double sum = BigDecimalUtil.add(orderItem.getOnwayQty() == null ? 0.00 : orderItem.getOnwayQty(), orderItem.getReceiveQty() ==null ? 0.00 : orderItem.getReceiveQty());
			   trans.setDeQty(sum == null ? "" : sum .toString());
			   trans.setOnwayQty(orderItem.getOnwayQty() == null ? ""  : orderItem.getOnwayQty().toString());
			   trans.setReceiveQty(orderItem.getReceiveQty() == null ? "" : orderItem.getReceiveQty().toString());
			   trans.setIncomBadQty(col1);
			   trans.setInsBadQty(col2);
			   trans.setUndeliveryQty(orderItem.getUndeliveryQty() == null ? "" :  orderItem.getUndeliveryQty().toString());
			   trans.setCurrency(orderItem.getCurrency() == null ? "" : orderItem.getCurrency());
			   trans.setUnitName(orderItem.getUnitName() == null ? "" :  orderItem.getUnitName());
			   trans.setPrice(orderItem.getPrice() == null ?  "" : orderItem.getPrice().toString()); 
			   trans.setOrderDate(orderItem.getOrder().getOrderDate() == null ? "" :  orderItem.getOrder().getOrderDate().toString());
			   trans.setRequestTime(orderItem.getRequestTime() == null  ?  ""  :   orderItem.getRequestTime().toString());
			   trans.setPublishTime(orderItem.getPublishTime()  == null ? "" :    orderItem.getPublishTime().toString());
			   trans.setConfirmTime(orderItem.getConfirmTime()   == null ? "" : orderItem.getConfirmTime().toString());
			   trans.setPublishStatus(orderItem.getPublishStatus() == null ? "" :   orderItem.getPublishStatus().toString());
			   trans.setConfirmStatus(orderItem.getConfirmStatus() == null ? "" : orderItem.getConfirmStatus().toString());
			   trans.setDeliveryStatus(orderItem.getDeliveryStatus() == null ? "" :   orderItem.getDeliveryStatus().toString());
			   trans.setReceiveStatus(orderItem.getReceiveStatus() == null ? "" :    orderItem.getReceiveStatus().toString());
			   trans.setZlock(orderItem.getZlock() == null ? "" : orderItem.getZlock());
			   trans.setCloseStatus(orderItem.getCloseStatus() == null ? "" :  orderItem.getCloseStatus().toString());
			   trans.setReceiveOrg(orderItem.getReceiveOrg() == null ? "" : orderItem.getReceiveOrg());
			   trans.setRejectReason(orderItem.getRejectReason() == null ? "" : orderItem.getRejectReason());
			   ret.add(trans);
		   }
	   }

		return ret;
	}
	
	/**
	 * 导出订单详情(通过主订单的ids导出)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderItemTransfer> getOrderItemVoByMainIds(Map<String, Object> searchParamMap,	String ids) {
		List<PurchaseOrderEntity> orderList = null;
		if(!ids.equals("1") ){
			String [] orderIds=ids.split(",");
			if(null!=orderIds && orderIds.length>0){
				orderList=new ArrayList<PurchaseOrderEntity>();
				for (String str : orderIds) {
					orderList.add(purchaseOrderDao.findOne(Long.valueOf(str)));
				}
			}
		}else{
			orderList = getPurchaseOrderList(searchParamMap);
		}
		
		List<PurchaseOrderItemEntity> list = new ArrayList<PurchaseOrderItemEntity>();
		if(orderList != null && orderList.size() != 0){
			for(PurchaseOrderEntity order  : orderList){
				list.addAll(purchaseOrderItemDao.findByOrderId(order.getId(), 0));
			}
		}
		
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
	   List<PurchaseOrderItemTransfer> ret = new ArrayList<PurchaseOrderItemTransfer>();
	   PurchaseOrderItemTransfer trans = null;
	   
	   if(list != null && list.size() != 0){
		   for(PurchaseOrderItemEntity orderItem : list ){
			   trans = new PurchaseOrderItemTransfer();
			   trans.setOrderCode(orderItem.getOrder().getOrderCode());
			   trans.setItemNo(orderItem.getItemNo().toString());
			   if(orderItem.getOrder().getPurOrderType() != null ){
				   trans.setPurOrderType(orderItem.getOrder().getPurOrderType().getName());
			   }
			   if(orderItem.getOrder().getCompany() != null){
				   trans.setCompanyCode(orderItem.getOrder().getCompany().getCode());
				   trans.setCompanyName(orderItem.getOrder().getCompany().getName());
			   }
			   if(orderItem.getFactoryEntity() != null){
				   trans.setFactoryCode(orderItem.getFactoryEntity().getCode());
				   trans.setFactoryName(orderItem.getFactoryEntity().getName());
			   }
			   trans.setPstyp(orderItem.getPstyp());
			   if(orderItem.getOrder().getVendor() != null){
				   trans.setVendorCode(orderItem.getOrder().getVendor().getCode());
				   trans.setVendorName(orderItem.getOrder().getVendor().getName());
			   }
			   trans.setZtermms(orderItem.getOrder().getZtermms());
			   if(orderItem.getMaterial() != null){
				   trans.setMaterialCode(orderItem.getMaterial().getCode());
				   trans.setMaterialDescride(orderItem.getMaterial().getDescribe());
			   }
			   trans.setOrderQty(orderItem.getOrderQty().toString());
			   Double sum = BigDecimalUtil.add(orderItem.getOnwayQty() == null ? 0 :orderItem.getOnwayQty(), orderItem.getReceiveQty()== null ? 0 : orderItem.getReceiveQty());
			   trans.setDeQty(sum.toString());
			   trans.setOnwayQty(orderItem.getOnwayQty() == null ?  "0" :  orderItem.getOnwayQty().toString() );
			   trans.setReceiveQty( orderItem.getReceiveQty() == null ? "0"   :  orderItem.getReceiveQty().toString() );
			   trans.setIncomBadQty("");//暂未去顶来源
			   trans.setInsBadQty("");//暂未去顶来源
			   trans.setUndeliveryQty(orderItem.getUndeliveryQty() == null ?  "0" :  orderItem.getUndeliveryQty().toString());
			   trans.setCurrency(orderItem.getCurrency());
			   trans.setUnitName(orderItem.getUnitName());
			   trans.setPrice(orderItem.getPrice() == null ?  "0" :  orderItem.getPrice().toString());
			   trans.setOrderDate(orderItem.getOrder().getOrderDate() ==null ? "" :orderItem.getOrder().getOrderDate().toString() );
			   trans.setRequestTime(orderItem.getRequestTime() == null ? "" :  orderItem.getRequestTime().toString() );
			   trans.setPublishTime( orderItem.getPublishTime() == null ? "" :  orderItem.getPublishTime().toString());
			   trans.setConfirmTime( orderItem.getConfirmTime() == null ? "" :  orderItem.getConfirmTime().toString());
			   trans.setPublishStatus(orderItem.getPublishStatus() == null ? "" :  orderItem.getPublishStatus().toString());
			   trans.setConfirmStatus(orderItem.getConfirmStatus() == null ? "" :  orderItem.getConfirmStatus().toString());
			   trans.setDeliveryStatus( orderItem.getDeliveryStatus() == null ? "" :  orderItem.getDeliveryStatus().toString());
			   trans.setReceiveStatus( orderItem.getReceiveStatus() == null ? "" :  orderItem.getReceiveStatus().toString());
			   trans.setZlock(orderItem.getZlock());
			   trans.setCloseStatus(orderItem.getCloseStatus() == null ? "" :  orderItem.getCloseStatus().toString());
			   trans.setReceiveOrg(orderItem.getReceiveOrg());
			   trans.setRejectReason(orderItem.getRejectReason());
			   ret.add(trans);
		   }
	   }

		return ret;
	}
	
	
	
	
	
	
	
	/**
	 * 查反馈次数
	 * @param list
	 */
	private void setDefaultFeedBackCount(List<PurchaseOrderItemEntity> list) {
		 if(null!=list && list.size()>0){
			 for (PurchaseOrderItemEntity entity : list) {
				 entity.setFeedbackCount(feedbackService.findFeedBackCountByOrderItemId(entity.getId()));
			}
		 }
	}

	/**
	 * 获取订单计划详情
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseOrderItemPlanEntity> getPurchaseOrderItemPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "itemNo");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		Page<PurchaseOrderItemPlanEntity> page =purchaseOrderItemPlanDao.findAll(spec,pagin);
		countPlanQnwayQty(page.getContent());//计算在途数量 add by chao.gu
		return page;
	}
	
	public Page<PurchaseOrderItemPlanEntity> getPurchaseOrderItemPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse_(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		Page<PurchaseOrderItemPlanEntity> page=purchaseOrderItemPlanDao.findAll(spec,pagin);
		countPlanQnwayQty(page.getContent());
		return page;
	}
	
	/**
	 * 计算供货计划的在途数量
	 * @param list
	 */
	public void countPlanQnwayQty(List<PurchaseOrderItemPlanEntity> list){
		List<Long> idList ;
		for (PurchaseOrderItemPlanEntity plan : list) {
			idList= new ArrayList<Long>();
			//正常的plan
			idList.add(plan.getId());
      
		 //补货的plan
		List<Long> replList=purchaseOrderItemPlanDao.findReplPlanIdsBySourcePlanId(plan.getId());
		if(null!=replList && replList.size()>0){
			idList.addAll(replList);
		  }
		//在途数量=普通+补货
		Double onWayQty= deliveryItemDao.findOnWayQtyByPlanIds(idList);
		plan.setOnwayQty(null==onWayQty?0:onWayQty);
		}
	}
	
	/**
	 * 发布单个订单
	 * @param order
	 */
	public void publishOrder(PurchaseOrderEntity order) {
		order.setPublishStatus(1); 
		purchaseOrderDao.save(order);
	}
	
	/**
	 * 批量发布订单明细[采]
	 * @param orders
	 */
	public void publishOrderItems(List<PurchaseOrderItemEntity> orderItems, UserEntity user) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemEntity order : orderItems) {
			order=purchaseOrderItemDao.findOne(order.getId());
			order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			order.setPublishTime(curr);
			order.setPublishUser(user);
			purchaseOrderItemDao.save(order);
		}
		
		
		//更新主表发布状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.PUBLISH);
		}
	}
	
	/**
	 * 取消发布采购订单明细【采】
	 * @param orderItems
	 * @param user
	 */
	public void unpublishOrderItem(List<PurchaseOrderItemEntity> orderItems, UserEntity user) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		int count_publish;//供货关系发布的个数
		int count_unpublish;//供货关系未发布的个数
		for(PurchaseOrderItemEntity order : orderItems) {
			count_publish=0;
			count_unpublish=0;
			order=purchaseOrderItemDao.findOne(order.getId());
			
			///////将供货关系中确认都发布
			List<PurchaseOrderItemPlanEntity> planSet =purchaseOrderItemPlanDao.findItemPlanEntitysByItem(order.getId());
			if(CommonUtil.isNotNullCollection(planSet)){
				for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : planSet) {
					if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==purchaseOrderItemPlanEntity.getPublishStatus().intValue()
							&& PurchaseConstans.CONFIRM_STATUS_NO.intValue()==purchaseOrderItemPlanEntity.getConfirmStatus().intValue()){
						purchaseOrderItemPlanEntity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
						purchaseOrderItemPlanEntity.setPublishTime(curr);
						purchaseOrderItemPlanEntity.setPublishUser(user);
						count_unpublish++;
					}else if(PurchaseConstans.PUBLISH_STATUS_NO.intValue()==purchaseOrderItemPlanEntity.getPublishStatus().intValue()){
						count_unpublish++;
					}else if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==purchaseOrderItemPlanEntity.getPublishStatus().intValue()){
						count_publish++;
					}
				}
				purchaseOrderItemPlanDao.save(planSet);
			}
			//////供货关系结束
			if(count_publish>0 && count_unpublish>0){
				order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
			}else if(count_publish>0 && count_unpublish==0){
				order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			}else if(count_publish==0 && count_unpublish>0){
				order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			}
			order.setPublishTime(curr);
			order.setPublishUser(user);
			purchaseOrderItemDao.save(order);
		}
		
		
		//更新主表发布状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.PUBLISH);
		}
	}
	
	/**
	 * 批量确认/驳回订单明细【供】
	 * @param orders
	 * @throws Exception 
	 */
	public void confirmOrderItems(List<PurchaseOrderItemEntity> orderItems, UserEntity user,Integer confirmStatus,String reason,ILogger logger) throws Exception{
		for(PurchaseOrderItemEntity order : orderItems) {
			order=purchaseOrderItemDao.findOne(order.getId());
			if(order.getIsModify() == 1){ //采购商修改订单明细
				//同意（对于修改过后的订单同意后）
				if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){
					modifyOrderItemAndDN(order,user,confirmStatus);
					continue;
				}
				if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
					if(order.getVetoStatus() != null){//采购员拒绝驳回
						if(PurchaseConstans.VETO_STATUS_VETO.intValue() == order.getVetoStatus().intValue()){
							continue;
						}
					}	
				}
				confirmOrderItem(order,user,confirmStatus,reason);
			}else{//正常的驳回和确认
				if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
					if(order.getVetoStatus() != null){//采购员拒绝驳回
						if(PurchaseConstans.VETO_STATUS_VETO.intValue() == order.getVetoStatus().intValue()){
							continue;
						}
					}
				}
				 confirmOrderItem(order,user,confirmStatus,reason);	
			}
		}

		
		//更新主表确认状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.CONFIRM);
		}
		
       //传递sap确认标识
		if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){
			PurchaseOrderSAP.confirmOrder(orderItems,logger);
		}
		
		
		
		//供应商驳回
		if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
			//发消息给采购商
			 List<Long> userIdList = new ArrayList<Long>();
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(orderItems.get(0).getOrder().getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 userIdList.add(rel.getUserId());
			 }
		     warnMainService.warnMessageSet(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_REJECT, userIdList);
		

				//删除采购订单待确认提醒
				 List<UserEntity> users = userServiceImpl.findByCompany(orderItems.get(0).getOrder().getVendor().getId());
				 for(UserEntity useritem : users){
					 //查询未确认的订单行（通过主单的id）
					 List<PurchaseOrderItemEntity> itemList1 = purchaseOrderItemDao.findUnConfirmOrderItemsByOrder(orderItems.get(0).getOrder().getId());
					 
					 List<PurchaseOrderItemEntity> itemList2 = purchaseOrderItemDao.findVoteOrderItemsByOrder(orderItems.get(0).getOrder().getId());
					 
					//解除供应商的待确认的消息
					 if(itemList1 == null || itemList1.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_UN_CONFIRM,useritem.getId());
					 }
					 
					//解除供应商的拒绝驳回的消息
					 if(itemList2 == null || itemList2.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_VETO,useritem.getId());
					 }
					 
				 }	
		}
		
		
		
		
		
	

		//删除订单待确认消息预警
		if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){
			
			//发给确认信息给采购商
			 List<Long> userIdList = new ArrayList<Long>();
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(orderItems.get(0).getOrder().getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 userIdList.add(rel.getUserId());
			 }
		     warnMainService.warnMessageSet(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_CONFIRM, userIdList);
			
			
			
			//删除采购订单待确认提醒
			 List<UserEntity> users = userServiceImpl.findByCompany(orderItems.get(0).getOrder().getVendor().getId());
			 for(UserEntity useritem : users){
				 //查询未确认的订单行（通过主单的id）
				 List<PurchaseOrderItemEntity> itemList1 = purchaseOrderItemDao.findUnConfirmOrderItemsByOrder(orderItems.get(0).getOrder().getId());
				 
				 List<PurchaseOrderItemEntity> itemList2 = purchaseOrderItemDao.findVoteOrderItemsByOrder(orderItems.get(0).getOrder().getId());
				 
				//解除供应商的待确认的消息
				 if(itemList1 == null || itemList1.size()==0 ){
					 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_UN_CONFIRM,useritem.getId());
				 }
				 
				//解除供应商的拒绝驳回的消息
				 if(itemList2 == null || itemList2.size()==0 ){
					 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_VETO,useritem.getId());
				 }
				 
			 }	
		}
		
		
		
		
	}
	
	/**
	 * 批量决绝供应商驳回条件【采】
	 * @param orders
	 */
	public void vetoOrderItems(List<PurchaseOrderItemEntity> orderItems, UserEntity user,Integer confirmStatus,String reason){
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemEntity order : orderItems) {
			order=purchaseOrderItemDao.findOne(order.getId());
			//采购商驳回供应商驳回条件
			if(PurchaseConstans.VETO_STATUS_VETO.intValue()==confirmStatus.intValue()){
				order.setVetoStatus(confirmStatus);
				order.setVetoReason(reason);
				order.setVetoUser(user);
				order.setVetoTime(curr);
				order.setConfirmStatus(-2);
				order.setConfirmTime(curr);
				order.setConfirmUser(user);
			}
			purchaseOrderItemDao.save(order);
		}
		//更新主表确认状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.CONFIRM);
		}
		//更新主表发布状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.PUBLISH);
		}
		
		
		//给供应商发布消息拒绝驳回的消息
		 List<UserEntity> users = userServiceImpl.findByCompany(orderItems.get(0).getOrder().getVendor().getId());
		 List<Long> userIdList = new ArrayList<Long>();
		 for( UserEntity us :  users){
			 userIdList.add(us.getId());
		 }
		warnMainService.warnMessageSet(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_VETO, userIdList);

		 
		 

		 //解除驳回待处理的信息
		 List<PurchaseOrderItemEntity> warnItemList  =purchaseOrderItemDao.findRejctOrderItemsByOrder(orderItems.get(0).getOrder().getId());//查询未处理的驳回记录
		 if( warnItemList.size() == 0 || warnItemList == null  ){//判断是否完全处理完驳回的记录
			 //把驳回的消息删除
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(orderItems.get(0).getOrder().getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_REJECT,rel.getUser().getId());
			 }
		 }
		
	}
	
	
	/**
	 * 批量同意供应商驳回条件【采】
	 * @param orders
	 */
	public void unVetoOrderItem(List<PurchaseOrderItemEntity> orderItems, UserEntity user,Integer confirmStatus,String reason){
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemEntity order : orderItems) {
			order=purchaseOrderItemDao.findOne(order.getId());
			//改变驳回确定的状态(将该订单行成未发布和未确认状态)初始化订单（不能进行发布）
			if(PurchaseConstans.VETO_STATUS_AGREE.intValue()==confirmStatus.intValue()){
				order.setVetoStatus(confirmStatus);
				order.setVetoReason(reason);
				order.setVetoUser(user);
				order.setVetoTime(curr);
				order.setConfirmStatus(0);
				order.setConfirmTime(null);
				order.setConfirmUser(null);
				order.setPublishStatus(0);
				order.setPublishTime(null);
				order.setPublishUser(null);
			}
			purchaseOrderItemDao.save(order);
		}
		//更新主表确认状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.CONFIRM);
		}
		//更新主表发布状态
		for(PurchaseOrderItemEntity order : orderItems) {
		  PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
		  commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.PUBLISH);
		}
		
		//给供应商发布消息拒绝驳回的消息
		 List<UserEntity> users = userServiceImpl.findByCompany(orderItems.get(0).getOrder().getVendor().getId());
		 List<Long> userIdList = new ArrayList<Long>();
		 for( UserEntity us :  users){
			 userIdList.add(us.getId());
		 }
		warnMainService.warnMessageSet(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_UN_VETO, userIdList);

		 
		 

		 //解除驳回待处理的信息
		 List<PurchaseOrderItemEntity> warnItemList  =purchaseOrderItemDao.findRejctOrderItemsByOrder(orderItems.get(0).getOrder().getId());//查询未处理的驳回记录
		 if( warnItemList.size() == 0 || warnItemList == null  ){//判断是否完全处理完驳回的记录
			 //把驳回的消息删除
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(orderItems.get(0).getOrder().getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 warnMainService.delMessageByBillIdAndBillType(orderItems.get(0).getOrder().getId(), WarnConstant.ORDER_REJECT,rel.getUser().getId());
			 }
		 }
		
		
	}
	
	
	
	
	
	
	
	/**
	 * 确认供货关系
	 * @param orders
	 */
	public void confirmOrderItemPlans(List<PurchaseOrderItemPlanEntity> orderItemPlans, UserEntity user,Integer confirmStatus,String reason) {
		Timestamp curTime=DateUtil.getCurrentTimestamp();
		PurchaseOrderItemPlanRejectEntity reject;
		for(PurchaseOrderItemPlanEntity orderItemPlan : orderItemPlans) {
			orderItemPlan.setConfirmStatus(confirmStatus);
			orderItemPlan.setConfirmUser(user);
			orderItemPlan.setConfirmTime(curTime);
			
		   /////驳回写原因start
			if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
				orderItemPlan.setRejectReason(reason);
				orderItemPlan.setRejectUser(user);
				orderItemPlan.setRejectTime(curTime);
				//驳回记录start
				 reject=new PurchaseOrderItemPlanRejectEntity();
				 reject.setReason(reason);
				 reject.setPurchaseOrderItemPlanId(orderItemPlan.getId());
				 itemPlanRejectDao.save(reject);
				//驳回记录end
			}
		    ///驳回原因end
			
		}
		purchaseOrderItemPlanDao.save(orderItemPlans);
		//更新Item表和order表的确认状态
		for(PurchaseOrderItemPlanEntity orderItemPlan : orderItemPlans) {
			orderItemPlan=purchaseOrderItemPlanDao.findOne(orderItemPlan.getId());
			commonOrderItemStatus(orderItemPlan.getOrderItem(), curTime, user, PurchaseConstans.CONFIRM);
			commonOrderStatus(orderItemPlan.getOrder(), curTime, user, PurchaseConstans.CONFIRM);
		}
	}
	
	public void closeOrderItems(List<PurchaseOrderItemEntity> orderItems, UserEntity user) {
		for(PurchaseOrderItemEntity order : orderItems) {
			order.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);
			order.setCloseUser(user);
			order.setCloseTime(DateUtil.getCurrentTimestamp());
		}
		purchaseOrderItemDao.save(orderItems);
		
		//更新主表关闭状态
		for(PurchaseOrderItemEntity order : orderItems) {
			PurchaseOrderEntity entity=purchaseOrderDao.findOne(order.getOrder().getId());
			commonOrderStatus(entity, DateUtil.getCurrentTimestamp() , user, PurchaseConstans.CLOSE);
		}
	}
	
	/**
	 * 合并保存采购订单
	 * @param list
	 * @param logger
	 * @throws NoSuchMethodException  
	 * @throws InvocationTargetException    
	 * @throws IllegalAccessException    
	 */
	public boolean combinePurchaseOrder(List<PurchaseOrderTransfer> orderTranList, ILogger logger) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, OrganizationEntity> buyerMap = new HashMap<String, OrganizationEntity>();
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchaseOrderTransfer> list = validateTransfers(orderTranList, orgMap,buyerMap, materialMap, counts, logger);
		if(list.size() != orderTranList.size())  
			return false;
		
		logger.log("\n->正在准备合并主数据与明细数据\n");
		PurchaseOrderEntity purchaseorder = null;
		PurchaseOrderItemEntity purchaseOrderItem = null;
		PurchaseOrderItemPlanEntity purchaseItemPlan = null;
		Map<String, PurchaseOrderEntity> tmpMap = new HashMap<String, PurchaseOrderEntity>();
		List<PurchaseOrderItemEntity> purchaseOrderItemList = new ArrayList<PurchaseOrderItemEntity>(); 
		List<PurchaseOrderItemPlanEntity> purchaseOrderItemPlanList = new ArrayList<PurchaseOrderItemPlanEntity>();
		String key = null;
		for(PurchaseOrderTransfer trans : list) {
			key = trans.getOrderCode() + ";" + trans.getBuyerCode() + ";" + trans.getVendorCode();
			if(!tmpMap.containsKey(key)) {
				purchaseorder = getPurchaseOrderEntity(trans.getOrderCode(), trans.getBuyerCode(), trans.getVendorCode());
				purchaseorder.setOrderCode(trans.getOrderCode());
				purchaseorder.setBuyer(buyerMap.get(trans.getBuyerCode()));
				purchaseorder.setPurchaseUser(new UserEntity(curruser.id) ); 
				purchaseorder.setVendor(orgMap.get(trans.getVendorCode()));
				purchaseorder.setReceiveOrg(trans.getReceiveOrg()); 
				purchaseorder.setOrderDate(DateUtil.stringToTimestamp(trans.getOrderDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD));
				purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				purchaseorder.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				purchaseorder.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
				purchaseorder.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				purchaseorder.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				////TODO 订单类型 默认是国内
				purchaseorder.setOrderType(OrderType.DOMESTIC);
				////
				tmpMap.put(key, purchaseorder);
			} else {
				purchaseorder = tmpMap.get(key);
			}
			//设置明细单表信息：唯一性=根据采购主单ID+行号+物料编码+要求到货时间
			purchaseOrderItem = getPurchaseOrderItemEntity(purchaseorder, trans);
			purchaseOrderItem.setReceiveQty(0d);
			purchaseOrderItem.setReturnQty(0d);
			purchaseOrderItem.setOnwayQty(0d);
			purchaseOrderItem.setUndeliveryQty(0d);
			purchaseOrderItem.setDiffQty(0d);      
			purchaseOrderItem.setOrder(purchaseorder);
			purchaseOrderItem.setMaterial(materialMap.get(trans.getMaterialCode()));
			purchaseOrderItem.setItemNo(StringUtils.convertToInteger(trans.getItemNo()));
			purchaseOrderItem.setReceiveOrg(trans.getReceiveOrg());
			purchaseOrderItem.setOrderQty(StringUtils.convertToDouble(trans.getOrderQty()));
			purchaseOrderItem.setRequestTime(DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			purchaseOrderItem.setCurrency(trans.getCurrency());
			purchaseOrderItem.setUnitName(trans.getUnitName());
			if(null!=trans.getPrice()){
			purchaseOrderItem.setPrice(Double.parseDouble(trans.getPrice()));
			}
			purchaseOrderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			purchaseOrderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			purchaseOrderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			purchaseOrderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			purchaseOrderItem.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
			purchaseOrderItemList.add(purchaseOrderItem);
			//采购计划
			purchaseItemPlan = getPurchaseItemPlan(purchaseOrderItem,trans);
			long id = purchaseItemPlan.getId();
			PropertyUtils.copyProperties(purchaseItemPlan, purchaseOrderItem);
			purchaseItemPlan.setId(id); 
			purchaseItemPlan.setReceiveQty(0d);
			purchaseItemPlan.setReturnQty(0d);
			purchaseItemPlan.setToDeliveryQty(0d);
			purchaseItemPlan.setDeliveryQty(0d);
			purchaseItemPlan.setDiffQty(0d);      
			purchaseItemPlan.setOrderItem(purchaseOrderItem); 
			purchaseOrderItemPlanList.add(purchaseItemPlan);
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		//保存
		purchaseOrderDao.save(tmpMap.values());
		purchaseOrderItemDao.save(purchaseOrderItemList);    
		purchaseOrderItemPlanDao.save(purchaseOrderItemPlanList);
		return true;	
	}

	protected List<PurchaseOrderTransfer> validateTransfers(List<PurchaseOrderTransfer> orderTranList, Map<String, OrganizationEntity> orgMap,Map<String, OrganizationEntity> buyerMap, Map<String, MaterialEntity> materialMap, 
			 Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入的采购订单信息进行预处理,共有[" + (orderTranList == null ? 0 : orderTranList.size()) + "]条数据";
		logger.log(logMsg); 
		counts[0] = orderTranList.size();
		List<PurchaseOrderTransfer> ret = new ArrayList<PurchaseOrderTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<OrganizationEntity> buyerList=null;
		List<MaterialEntity> materialList = null;
		boolean lineValidat = true;
		int index  = 2; 
		for(PurchaseOrderTransfer trans : orderTranList) {
			//循环后重置start
			orgList = null;
			buyerList=null;
			materialList = null;
			//重置end
			
			if(StringUtils.isEmpty(trans.getOrderCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],采购订单号不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getBuyerCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],采购组织编码不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getVendorCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],供应商编码不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getOrderDate())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],订单日期不能为空,忽略此采购明细");
			}else{
				try{
					Timestamp orderDate=DateUtil.stringToTimestamp(trans.getOrderDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
				    if(null==orderDate){
				    	lineValidat = false;
						logger.log("->[FAILED]行索引[" + (index) + "],订单日期格式有误，不满足yyyy-MM-dd格式,忽略此采购明细");
				    }
				}catch(Exception e){
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],订单日期不满足yyyy-MM-dd格式,忽略此采购明细");
				}
			}
			
			
			if(StringUtils.isEmpty(trans.getMaterialCode())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],物料编码不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getItemNo())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],行号不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getOrderQty())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],订购数量不能为空,忽略此采购明细");
			}
			
			if(StringUtils.isEmpty(trans.getRequestTime())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + (index) + "],要求到货时间不能为空,忽略此采购明细");
			}else{
				try{
					//将时间变成23：59：59格式
					trans.setRequestTime(trans.getRequestTime()+" 23:59:59");
					Timestamp requestTime=DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				    if(null==requestTime){
				    	lineValidat = false;
						logger.log("->[FAILED]行索引[" + (index) + "],要求到货时间格式有误，不满足yyyy-MM-dd格式,忽略此采购明细");
				    }
				}catch(Exception e){
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],要求到货时间不满足yyyy-MM-dd格式,忽略此采购明细");
				}
			}
			
			if(!StringUtils.isEmpty(trans.getPrice())){
				try{
					Double.parseDouble(trans.getPrice());
				}catch(Exception e){
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],价格必须是数字,忽略此采购明细");
				}
			}
			
			
			if(!buyerMap.containsKey(trans.getBuyerCode())) {
				buyerList = organizationDao.findByCodeAndAbolishedAndRoleTypeAndEnableStatus(trans.getBuyerCode(),StatusConstant.STATUS_NO,OrgType.ROLE_TYPE_BUYER,StatusConstant.STATUS_YES);
				if(CollectionUtils.isEmpty(buyerList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],采购组织编码[" + trans.getBuyerCode() + "]未在系统中维护,或被禁用,忽略此采购明细");
				} else {
					buyerMap.put(trans.getBuyerCode(), buyerList.get(0));
				}
			}else{
				buyerList=new ArrayList<OrganizationEntity>();
				buyerList.add(buyerMap.get(trans.getBuyerCode()));
			}
			
			//查询供应商时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCodeAndAbolishedAndRoleTypeAndEnableStatusAndActiveStatusAndBuyerId(trans.getVendorCode(),OrgType.ROLE_TYPE_VENDOR,StatusConstant.STATUS_YES,StatusConstant.STATUS_YES,buyerList.get(0).getId());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],供应商编码[" + trans.getVendorCode() + "]无采购组织["+trans.getBuyerCode()+"]权限，或未在系统中维护,或未生效,忽略此采购明细");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			//查询物料时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findMaterialListByBuyerAndCodeAndAbolished(buyerList.get(0).getId(),trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + (index) + "],物料(" + trans.getMaterialCode() + "], 不存在,忽略此采购明细");
				} else {
					materialMap.put(trans.getMaterialCode(), materialList.get(0));
				}
			}
			
			//判断当前用户有无该采购组织权限时必须已经获得采购组织
			if(null!=buyerList && buyerList.size()>0 && null!=buyerList.get(0) && !buyerOrgPermissionUtil.isContainsBuyerIds(buyerList.get(0).getId())){
				lineValidat = false;
				logger.log("->[FAILED]行索引[" + index + "]当前用户无采购组织["+trans.getBuyerCode()+"]权限,忽略此采购计划");
			}
			
			if(lineValidat) {
				logMsg = "[SUCCESS]行索引[" + (index) + "],预处理[" + trans.getVendorCode() + "|" + trans.getMaterialCode() + "|" + trans.getOrderDate() + "]成功。";
				logger.log(logMsg);
				ret.add(trans);
			}
			index ++;
			lineValidat = true;
		}
		counts[1] = ret.size();
		logMsg = "<-导入的采购订单主信息预处理完毕,共有[" + ret.size() + "]条有效数据";
		logger.log(logMsg);
		return ret;   
	}

	protected PurchaseOrderItemPlanEntity getPurchaseItemPlan(PurchaseOrderItemEntity purchaseOrderItem,PurchaseOrderTransfer trans) {
		if(purchaseOrderItem.getId() == 0l)
			return new PurchaseOrderItemPlanEntity();
		
		PurchaseOrderItemPlanEntity itemplan = purchaseOrderItemPlanDao.findPurchaseOrderItemPlanEntityByItem(purchaseOrderItem.getId(),StringUtils.convertToInteger(trans.getItemNo()),trans.getMaterialCode(),DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
		return itemplan == null ? new PurchaseOrderItemPlanEntity() : itemplan;
	}

	protected PurchaseOrderItemEntity getPurchaseOrderItemEntity(PurchaseOrderEntity purchaseorder,PurchaseOrderTransfer trans) {
		if(purchaseorder.getId() == 0l)
			return new PurchaseOrderItemEntity();
		
		PurchaseOrderItemEntity item = purchaseOrderItemDao.findPurchaseOrderItemEntityByMain(purchaseorder.getId(), StringUtils.convertToInteger(trans.getItemNo()),trans.getMaterialCode(),DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
		return item == null ? new PurchaseOrderItemEntity() : item;
	}

	protected PurchaseOrderEntity getPurchaseOrderEntity(String orderCode, String buyerCode, String vendorCode) {
		PurchaseOrderEntity order = purchaseOrderDao.findPurchaseOrderEntityByCode(orderCode, buyerCode, vendorCode);
		return order == null ?  new PurchaseOrderEntity() : order;
	}
	
	/**
	 * 确认采购明细行
	 * @param order
	 */
	protected void confirmOrderItems(PurchaseOrderEntity order, UserEntity user) {
		List<PurchaseOrderItemEntity> items = purchaseOrderItemDao.findPurchaseOrderItemEntityByOrder(order);
		if(CollectionUtils.isEmpty(items))
			return;
		
		for(PurchaseOrderItemEntity item : items) {
			item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			item.setConfirmUser(user);
			item.setConfirmTime(DateUtil.getCurrentTimestamp()); 
			confirmOrderItemPlans(item, user);
		}
		purchaseOrderItemDao.save(items);
	}
	
	/**
	 * 确认供货计划
	 * @param item
	 */
	protected void confirmOrderItemPlans(PurchaseOrderItemEntity item, UserEntity user) {
		List<PurchaseOrderItemPlanEntity> plans = purchaseOrderItemPlanDao.findPurchaseOrderItemPlanEntityByOrderItem(item);
		if(CollectionUtils.isEmpty(plans))
			return;
		
		for(PurchaseOrderItemPlanEntity itemPlan : plans) {
			itemPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			itemPlan.setConfirmUser(user);
			itemPlan.setConfirmTime(DateUtil.getCurrentTimestamp()); 
		}
		purchaseOrderItemPlanDao.save(plans);
	}
	

	public PurchaseOrderItemPlanEntity getOrderPlanByPlanId(Long planId) {
		return purchaseOrderItemPlanDao.findOne(planId);
	}
	
	/**
	 * 保存拆分计划
	 * @param itemId 采购定明细ID
	 * @param planList
	 * @param userEntity
	 * @param changedFlag 
	 * @param orderType 
	 */
	public void saveSplitOrderPlan(List<PurchaseOrderItemPlanEntity> planList, UserEntity userEntity, boolean changedFlag, Integer orderType,PurchaseOrderItemEntity item) throws Exception {
		PurchaseOrderEntity order  = item.getOrder();
		List<PurchaseOrderItemPlanEntity> saveList = new ArrayList<PurchaseOrderItemPlanEntity>();
		PurchaseOrderItemPlanEntity plan = null;
		boolean hasConfirm = false,  hasNoConfirm = false;
		for(PurchaseOrderItemPlanEntity _plan : planList) {
			if(_plan.getId() > 0) {
				plan = purchaseOrderItemPlanDao.findOne(_plan.getId());
				saveList.add(plan);
				
				if(plan.getConfirmStatus() == PurchaseConstans.CONFIRM_STATUS_YES.intValue()) {
					hasConfirm = true;
				} else {
					hasNoConfirm = true;
				}
				if(plan.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()) {
					plan.setItemNo(_plan.getItemNo());
					continue;
				} else if(plan.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
					if(BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty()) > _plan.getOrderQty()) {
						throw new Exception("供货计划行：" + plan.getItemNo() + " 订购数量不能小于已发数量");
					}
					plan.setUndeliveryQty(BigDecimalUtil.sub(_plan.getOrderQty(), BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty())));
				}else{
					plan.setUndeliveryQty(_plan.getOrderQty());
				}
				
				plan.setItemNo(_plan.getItemNo());
				plan.setOrderQty(_plan.getOrderQty());
				plan.setRequestTime(_plan.getRequestTime());
				continue;
			}
			
			hasNoConfirm = true;
			_plan.setOrder(item.getOrder());
			_plan.setOrderItem(item);
			_plan.setVersion(item.getVersion());
			_plan.setMaterial(item.getMaterial());
			_plan.setReceiveOrg(item.getReceiveOrg());
			_plan.setCurrency(item.getCurrency());
			_plan.setUnitName(item.getUnitName());
			_plan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			_plan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			_plan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			_plan.setOrderStatus(item.getOrderStatus());
			
			_plan.setDeliveryQty(0d);
			_plan.setToDeliveryQty(0d);
			_plan.setReceiveQty(0d);
			_plan.setReturnQty(0d);
			_plan.setDiffQty(0d);
			_plan.setOnwayQty(0d);
			_plan.setUndeliveryQty(_plan.getOrderQty());
			_plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);//默认未发布
			if(orderType == OrderType.DOMESTIC){								//国内订单
				_plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);		//默认未发布
			}else{																//国外，外协
				_plan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);	//默认已发布
				_plan.setPublishTime(DateUtil.getCurrentTimestamp());
				_plan.setPublishUser(userEntity);
			}
			saveList.add(_plan);
		}
		if(hasConfirm && hasNoConfirm) { 
			item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
			order.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
		}else {
			item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			order.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
		}
		
		boolean isConfirm = false;
		boolean isPublish = false;
		
		for(PurchaseOrderItemPlanEntity _plan : planList) {	
			if(_plan.getId() > 0){							
				if(_plan.getConfirmStatus() == PurchaseConstans.CONFIRM_STATUS_YES){	
					isConfirm = true;
				}
				if(orderType == OrderType.DOMESTIC 
						&& _plan.getPublishStatus() == PurchaseConstans.PUBLISH_STATUS_YES){//国内的订单,且存在已发布计划
					isPublish = true;
				}
				continue;
			}else{											
				if(isConfirm){								//存在已确认的供货计划，则订单明细为部分确认
					item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
				}
				if(orderType == OrderType.DOMESTIC && isPublish){//国内的订单，存在新加的供货计划，且存在已发布计划，则修改明细状态为部分发布
					item.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
				}
				break;
			}
		}
		purchaseOrderDao.save(order);
		purchaseOrderItemDao.save(item);
		purchaseOrderItemPlanDao.save(saveList);
		
		if(changedFlag && item.getPublishStatus()==PurchaseConstans.PUBLISH_STATUS_YES){
			sendMailByModifyPlan(item);								//发送邮件给供应商
		}
	}
	
	/**
	 * 发送邮件给供应商
	 * @param order	订单对象
	 */
	private void sendMailByModifyPlan(PurchaseOrderItemEntity orderitem) {
		OrganizationEntity vendor = orderitem.getOrder().getVendor();				//获取供应商
		if(vendor != null){
			String vendorEmail = vendor.getEmail();
			String vendorName = vendor.getName();
			String msg = "";
			if(orderitem.getOrder() != null){
				msg = "订单号: " + orderitem.getOrder().getOrderCode() +
					  "供应商编码: " + vendor.getCode() + 
					  ", 供应商名称: " + vendor.getName() + ", <br/>" +
					  "零件号: " + orderitem.getMaterial().getCode() + 
					  ", 零件名称: " + orderitem.getMaterial().getName() + ", <br/>" +
					  "采购员已修改该订单的供货计划详情，请核查" +
					  "！<br/>";
			}
			CommonUtil.sendMail(vendorEmail, vendorName, "供货计划详情采方变更预警", msg);		//发送邮件
		}
	}
	
	
	
	/**
	 * 删除订单计划
	 */
	public void deletePlan(PurchaseOrderItemPlanEntity plan,UserEntity userentiy){
		plan.setAbolished(StatusConstant.STATUS_YES);//废弃状态
		updatePlan(plan,userentiy);
	}
	
	public void updatePlan(PurchaseOrderItemPlanEntity plan,UserEntity userentiy){
		plan.setUpdateUserId(userentiy.getId());
		plan.setUpdateUserName(userentiy.getName());
		plan.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		purchaseOrderItemPlanDao.save(plan);
	}
	
	
	/**
	 * 批量发布供货计划
	 * @param list
	 */
	public void publishOrderItemPlans(List<PurchaseOrderItemPlanEntity> list, UserEntity user) {
		PurchaseOrderItemPlanEntity itemPlan = null;
		PurchaseOrderEntity entity=null;
		PurchaseOrderItemEntity orderItem=null;
		List<PurchaseOrderItemPlanEntity> savelist = new ArrayList<PurchaseOrderItemPlanEntity>();
		Timestamp curTime = DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity _orderItemPlan : list) {		
			itemPlan = purchaseOrderItemPlanDao.findOne(_orderItemPlan.getId());
			entity=itemPlan.getOrder();
			orderItem=itemPlan.getOrderItem();
			itemPlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			itemPlan.setPublishTime(curTime);
			itemPlan.setPublishUser(user);
			itemPlan.setLastUpdateTime(curTime);
			itemPlan.setUpdateUserId(user.getId());
			itemPlan.setUpdateUserName(user.getCreateUserName());
			savelist.add(itemPlan);
		}
		purchaseOrderItemPlanDao.save(savelist);//供货计划保存
		
		commonOrderItemStatus(orderItem,curTime,user,PurchaseConstans.PUBLISH);
		commonOrderStatus(entity,curTime,user,PurchaseConstans.PUBLISH);
		
	}
	
	
	/**
	 * 批量取消发布供货计划
	 * @param list
	 */
	public void unPublishOrderItemPlans(List<PurchaseOrderItemPlanEntity> list, UserEntity user) {
		PurchaseOrderItemPlanEntity itemPlan = null;
		PurchaseOrderEntity entity=null;
		PurchaseOrderItemEntity orderItem=null;
		List<PurchaseOrderItemPlanEntity> savelist = new ArrayList<PurchaseOrderItemPlanEntity>();
		Timestamp curTime = DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity _orderItemPlan : list) {		
			itemPlan = purchaseOrderItemPlanDao.findOne(_orderItemPlan.getId());
			entity=itemPlan.getOrder();
			orderItem=itemPlan.getOrderItem();
			itemPlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			itemPlan.setPublishTime(curTime);
			itemPlan.setPublishUser(user);
			itemPlan.setLastUpdateTime(curTime);
			itemPlan.setUpdateUserId(user.getId());
			itemPlan.setUpdateUserName(user.getCreateUserName());
			savelist.add(itemPlan);
		}
		purchaseOrderItemPlanDao.save(savelist);//供货计划保存
		
		
		commonOrderItemStatus(orderItem,curTime,user,PurchaseConstans.PUBLISH);
		commonOrderStatus(entity,curTime,user,PurchaseConstans.PUBLISH);
	}
	/**
	 * 更新采购订单主单发布状态
	 * @param entity
	 * @param orderItem
	 * @param curTime
	 * @param user
	 */
	public void commonOrderStatus(PurchaseOrderEntity entity,Timestamp curTime, UserEntity user,String state){
				///订单主表
				if(entity!=null){
					int count_all=0;
					int count_part=0;
					int count_reject = 0;//默认无驳回记录
					int count_delete = 0;//删除记录
					Set<PurchaseOrderItemEntity> orderItemSet=entity.getOrderItem();

					if(CommonUtil.isNotNullCollection(orderItemSet)){
						//发布
						if(state.equals(PurchaseConstans.PUBLISH)){//具有退货标识默认为已发布
								for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
									if(StringUtils.isEmpty(purchaseOrderItemEntity.getRetpo())){
										if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==purchaseOrderItemEntity.getPublishStatus().intValue()){
											count_all++;
										}else if(PurchaseConstans.PUBLISH_STATUS_PART.intValue()==purchaseOrderItemEntity.getPublishStatus().intValue()){
											count_part++;
										}
									}else{
										count_all++;
										count_part++;
									}
								}
								
								for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
									if(purchaseOrderItemEntity.getConfirmStatus() == -2 || purchaseOrderItemEntity.getConfirmStatus() == -1){
										count_reject ++;
								
									}
								}
								if(count_reject > 0 ){
								   entity.setIsReject(1);
								}else{
									entity.setIsReject(0);
								}
								
								
								
								if(count_all>0 && count_all==orderItemSet.size()){
									entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
								}else if(count_part >0 || count_all>0){
									entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
								}else{
									entity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
								}
								
								entity.setLastUpdateTime(curTime);
								entity.setUpdateUserId(user.getId());
								entity.setUpdateUserName(user.getCreateUserName());
								
								//查询是否有驳回记录
								
								
								
								purchaseOrderDao.save(entity);
						}else 	if(state.equals(PurchaseConstans.CLOSE)){//关闭
							for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
								if(StringUtils.isEmpty(purchaseOrderItemEntity.getRetpo())){
									if(PurchaseConstans.CLOSE_STATUS_YES.intValue()==purchaseOrderItemEntity.getCloseStatus().intValue()){
										count_all++;
									}else if(PurchaseConstans.CLOSE_STATUS_PART.intValue()==purchaseOrderItemEntity.getCloseStatus().intValue()){
										count_part++;
									}
									
								}else{
									count_all++;
									count_part++;
								}

							}
							
							for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
								if(purchaseOrderItemEntity.getConfirmStatus() == -2 || purchaseOrderItemEntity.getConfirmStatus() == -1){
									count_reject ++;
							
								}
							}
							if(count_reject > 0 ){
							   entity.setIsReject(1);
							}else{
								entity.setIsReject(0);
							}
							
							if(count_all>0 && count_all==orderItemSet.size()){
								entity.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);	
							}else if(count_part >0 || count_all>0){
								entity.setCloseStatus(PurchaseConstans.CLOSE_STATUS_PART);
							}else{
								entity.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
							}
							entity.setCloseTime(curTime);
							entity.setCloseUser(user);
							entity.setLastUpdateTime(curTime);
							entity.setUpdateUserId(user.getId());
							entity.setUpdateUserName(user.getCreateUserName());
							purchaseOrderDao.save(entity);
					}else 	if(state.equals(PurchaseConstans.CONFIRM)){//确认
						for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
							//对于删除标识的数据过滤，不参与计算状态
							if(StringUtils.isEmpty(purchaseOrderItemEntity.getRetpo())){
								if(!StringUtils.isEmpty(purchaseOrderItemEntity.getLoekz())){
									count_delete++;
								}
								
								if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==purchaseOrderItemEntity.getConfirmStatus().intValue() && StringUtils.isEmpty(purchaseOrderItemEntity.getLoekz()) ){
									count_all++;
								}else if(PurchaseConstans.CONFIRM_STATUS_PART.intValue()==purchaseOrderItemEntity.getConfirmStatus().intValue() &&   StringUtils.isEmpty(purchaseOrderItemEntity.getLoekz())){
									count_part++;
								}
							}else{
								count_all++;
								count_part++;
							}
						}
						
						for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
							if(StringUtils.isEmpty(purchaseOrderItemEntity.getLoekz())){
								if(purchaseOrderItemEntity.getConfirmStatus() == -2 || purchaseOrderItemEntity.getConfirmStatus() == -1){
									count_reject ++;
							
								}
							}	
						}
						if(count_reject > 0 ){
						   entity.setIsReject(1);
						}else{
							entity.setIsReject(0);
						}
						
						int all_orderItemSet_size =orderItemSet.size() - count_delete;
						
						if(count_all>0 && count_all==all_orderItemSet_size){
							entity.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
							entity.setCloseTime(curTime);
							entity.setCloseUser(user);
						}else if(count_reject>0 && count_reject==all_orderItemSet_size){
							entity.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_REJECT);
						}
						else if(count_part >0 || count_all>0 || count_reject>0){
							entity.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
						}else{
							entity.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
						}
						entity.setConfirmTime(curTime);
						entity.setConfirmUser(user);
						entity.setLastUpdateTime(curTime);
						entity.setUpdateUserId(user.getId());
						entity.setUpdateUserName(user.getCreateUserName());
						purchaseOrderDao.save(entity);
				}
						
					}
					
				}
				
	}
	
	/**
	 * 更新采购订单主单明细发布状态
	 * @param entity
	 * @param orderItem
	 * @param curTime
	 * @param user
	 */
	public void commonOrderItemStatus(PurchaseOrderItemEntity orderItem,Timestamp curTime, UserEntity user,String state){
	//////item表中发布状态
			if(null!=orderItem){
			   int count=0;
			    List<PurchaseOrderItemPlanEntity> orderItemPlan =purchaseOrderItemPlanDao.findItemPlanEntitysByItem(orderItem.getId());
				if(CommonUtil.isNotNullCollection(orderItemPlan)){		
					if(state.equals(PurchaseConstans.PUBLISH)){//发布
						for (PurchaseOrderItemPlanEntity plan : orderItemPlan) {
							if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==plan.getPublishStatus().intValue()){
								count++;
							}
						}
						
						if(count>0 && count==orderItemPlan.size()){
							orderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
						}else if(count >0){
							orderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
						}else{
							orderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
						}
						orderItem.setLastUpdateTime(curTime);
						orderItem.setUpdateUserId(user.getId());
						orderItem.setUpdateUserName(user.getCreateUserName());
						orderItem.setPublishTime(curTime);
						orderItem.setPublishUser(user);
						purchaseOrderItemDao.save(orderItem);
					}else if(state.equals(PurchaseConstans.CONFIRM)){//确认
						int count_reject=0;
						for (PurchaseOrderItemPlanEntity plan : orderItemPlan) {
							if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==plan.getConfirmStatus().intValue() ){
								count++;
							}else if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==plan.getConfirmStatus().intValue()){
								count_reject++;
							}
						}
						
						if(count>0 && count==orderItemPlan.size()){
							orderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
						}else if(count_reject>0 && count_reject==orderItemPlan.size()){
							orderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_REJECT);
						}else if(count >0){
							orderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_PART);
						}else{
							orderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
						}
						orderItem.setLastUpdateTime(curTime);
						orderItem.setUpdateUserId(user.getId());
						orderItem.setUpdateUserName(user.getCreateUserName());
						orderItem.setConfirmTime(curTime);
						orderItem.setConfirmUser(user);
						purchaseOrderItemDao.save(orderItem);
					}
				}
			}
	}
	
	/**
	 * 同步erp采购订单
	 * @param itemList
	 */
	public void saveErpPurchaseOrders(List<PurchaseOrderEntity> orderList) {
		for(PurchaseOrderEntity p:orderList){
			if (p != null){
				purchaseOrderDao.save(p);
			}
		}
	}
	
	public PurchaseOrderEntity getOrderByCode(String poNumber) {
		return purchaseOrderDao.getOrderByCode(poNumber);
	}

	public PurchaseOrderEntity getOrderByCodeAndOrgId(String poNumber,String orgId) {
		return purchaseOrderDao.getOrderByCodeAndOrgId(poNumber,orgId);
	}
	
	public void saveOrder(PurchaseOrderEntity order) {
		purchaseOrderDao.save(order);
	}
	
	@org.springframework.transaction.annotation.Transactional(propagation=Propagation.REQUIRES_NEW)
	public InterfaceMsgEntity getMsg(){
		InterfaceMsgEntity msg = new InterfaceMsgEntity();
		msg.setCode("001");
		msg.setName("订单同步接口");
		msg.setOs("erp");
		msg.setStatus("正在执行");
		msg.setBeginTime(new Timestamp(new Date().getTime()));
		msgDao.save(msg);
		return msg;
	}
	@org.springframework.transaction.annotation.Transactional(propagation=Propagation.REQUIRES_NEW)
	public InterfaceMsgItemEntity getMsgItem(InterfaceMsgEntity msg){
		InterfaceMsgItemEntity msgItem = new InterfaceMsgItemEntity();
		msgItem.setMsg(msg);
		msgItem.setFinishFlag("N");
		msgItem.setErrorFlag("Y");
		msgItem.setContent("订单号：PO0001，供应商v001");
		msgItemDao.save(msgItem);
		return msgItem;
	}
	
	@org.springframework.transaction.annotation.Transactional(propagation=Propagation.REQUIRES_NEW)
	public InterfaceMsgLogEntity getMsgLog(InterfaceMsgEntity msg,String dmlType,String logContent,String sql){
		InterfaceMsgLogEntity msgLog = new InterfaceMsgLogEntity();
		msgLog.setMsg(msg);
		msgLog.setDmlType(dmlType);
		msgLog.setLogType("purchaseOrderService.getMsgLog");
		msgLog.setLogContent(logContent);
		msgLog.setSql(sql);
		msgLogDao.save(msgLog);
		return msgLog;
	}

	public void sycOrder() throws Exception {
		InterfaceMsgEntity msg = getMsg();
		getMsgLog(msg, "QUERY", "查询供应商信息", "select * from qeweb_organization where org_type=? and code=?");
		getMsgLog(msg, "QUERY", "查询物料信息", "select * from qeweb_material where code=?");
		getMsgLog(msg, "QUERY", "查询订单信息", "select * from qeweb_purchase_order where code=?");
		getMsgLog(msg, "UPDATE", "更新订单信息", "update qeweb_purchase_order set publishStatus=?,confirmStatus=?");
		getMsgLog(msg, "EXCUTE", "新增订单信息", "insert into qeweb_purchase_order values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		getMsgLog(msg, "QUERY", "查询所有订单", "select * from qeweb_purchase_order");
		for(int i=0;i<2;i++){
			String info = "";
			InterfaceMsgItemEntity item = getMsgItem(msg); 
//			String code = "PO0000" + i;
//			String vendorCode = "v001";
//			OrganizationEntity vendor = organizationDao.findByCodeAndAbolished(vendorCode, 0);
//			if(vendor==null){
				info = "供应商在srm系统中不存在！";
				item.setContent("订单号：PO000"+(i+1)+"，供应商v001");
				item.setErrorInfo(info);
				item.setStatus("ERROR");
				item.setInsId("0000"+i);
				msgItemDao.save(item);
				msg.setResult("失败");
				msg.setStatus("未完成");
				msg.setEndTime(new Timestamp(new Date().getTime()));
				msgDao.save(msg);
//			}
			/*List<PurchaseOrderEntity> orders = purchaseOrderDao.findByOrderCode(code);
			PurchaseOrderEntity order = null;
			if(orders!=null){
				//修改
				order = orders.get(0);
				purchaseOrderDao.save(order);
			}else{
				PurchaseOrderEntity _order = purchaseOrderDao.findAll().get(0);
				order = new PurchaseOrderEntity();
				PropertyUtils.copyProperties(order, _order);
				order.setId(0);
				purchaseOrderDao.save(order);
			}
			for(int j=0;j<2;j++){
				int itemNo = i+1;
				
			}*/
		}
	}
	
	
	/**
	 * 批量发布订单[采]
	 * @param orders
	 */
	public void publishOrders(List<PurchaseOrderEntity> orders, UserEntity user) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderEntity order : orders) {
			//发布主订单
			order=purchaseOrderDao.findOne(order.getId());
			order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			order.setPublishTime(curr);
			order.setPublishUser(user);
			purchaseOrderDao.save(order);
			
			//发布明细
			Set<PurchaseOrderItemEntity> itemSet=order.getOrderItem();
			if(CommonUtil.isNotNullCollection(itemSet)){
				for (PurchaseOrderItemEntity itemEntity : itemSet) {
						itemEntity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
						itemEntity.setPublishTime(curr);
						itemEntity.setPublishUser(user);
				}
				purchaseOrderItemDao.save(itemSet);
			}
			
			//发布供货计划
			if(CommonUtil.isNotNullCollection(itemSet)){
				for (PurchaseOrderItemEntity itemEntity : itemSet) {
					List<PurchaseOrderItemPlanEntity> planSet =purchaseOrderItemPlanDao.findItemPlanEntitysByItem(itemEntity.getId());
					if(CommonUtil.isNotNullCollection(planSet)){
						for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : planSet) {
							if(PurchaseConstans.PUBLISH_STATUS_NO.intValue()==purchaseOrderItemPlanEntity.getPublishStatus().intValue()){
								purchaseOrderItemPlanEntity.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
								purchaseOrderItemPlanEntity.setPublishTime(curr);
								purchaseOrderItemPlanEntity.setPublishUser(user);
							}
						}
						purchaseOrderItemPlanDao.save(planSet);
					}
				}
			}
		}
	}
	
	/**
	 * 关闭订单(采)
	 * @param orders
	 * @param user
	 */
	public void closeOrders(List<PurchaseOrderEntity> orders, UserEntity user) {
		for(PurchaseOrderEntity order : orders) {
			//关闭订单
			order=purchaseOrderDao.findOne(order.getId());
			order.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);
			order.setCloseUser(user);
			order.setCloseTime(DateUtil.getCurrentTimestamp());
			purchaseOrderDao.save(order);
			
			//关闭明细
			Set<PurchaseOrderItemEntity> itemSet=order.getOrderItem();
			if(CommonUtil.isNotNullCollection(itemSet)){
				for (PurchaseOrderItemEntity itemEntity : itemSet) {
						itemEntity.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);
						itemEntity.setCloseUser(user);
						itemEntity.setCloseTime(DateUtil.getCurrentTimestamp());
				}
				purchaseOrderItemDao.save(itemSet);
			}
		}
	}
	
	/**
	 * 批量确认订单【供】
	 * @param orders
	 * @throws Exception 
	 */
	public void confirmOrders(List<PurchaseOrderEntity> orders, UserEntity user,Integer confirmStatus,String reason,ILogger logger) throws Exception{
		
		List<PurchaseOrderItemEntity> itemList = new ArrayList<PurchaseOrderItemEntity>();
		
		List<PurchaseOrderEntity> orderList = new ArrayList<PurchaseOrderEntity>();
		for(PurchaseOrderEntity order : orders) {
			//确认订单明细
			order=purchaseOrderDao.findOne(order.getId());
			orderList.add(order);
			Set<PurchaseOrderItemEntity> itemSet=order.getOrderItem();
				if(CommonUtil.isNotNullCollection(itemSet)){
					for (PurchaseOrderItemEntity itemEntity : itemSet) {	
						
						if( !StringUtils.isEmpty(itemEntity.getLoekz())  ||  !StringUtils.isEmpty(itemEntity.getLockStatus()) || !StringUtils.isEmpty(itemEntity.getZlock()) || !StringUtils.isEmpty(itemEntity.getElikz()) ){
							//对于订单的明细具有删除，冻结，锁定，交货已完成的自动过滤
							continue;
						}
						
						itemList.add(itemEntity);
						
						if(itemEntity.getIsModify() == 1){ //采购商修改订单明细
							//同意（对于修改过后的订单同意后）
							if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){
								modifyOrderItemAndDN(itemEntity,user,confirmStatus);
								continue;
							}
							if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
								if(itemEntity.getVetoStatus() != null){//采购员拒绝驳回
									if(PurchaseConstans.VETO_STATUS_VETO.intValue() == itemEntity.getVetoStatus().intValue()){
										continue;
									}
							}
							confirmOrderItem(itemEntity,user,confirmStatus,reason);	
						}	
					}else{//正常的驳回和确认
						if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
							if(itemEntity.getVetoStatus() != null){//采购员拒绝驳回
								if(PurchaseConstans.VETO_STATUS_VETO.intValue() == itemEntity.getVetoStatus().intValue()){
									continue;
								}
							}
						}
					   confirmOrderItem(itemEntity,user,confirmStatus,reason);	
					}
				}
			}
		}
	
		
		//更新主表确认状态
		for(PurchaseOrderEntity order : orderList) {
		  commonOrderStatus(order, DateUtil.getCurrentTimestamp() , user,PurchaseConstans.CONFIRM);
		}

       //传递sap确认标识
		if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){
			PurchaseOrderSAP.confirmOrder(itemList,logger);
		}
		
		
		//发消息给采购商驳回消息
		if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
			for(PurchaseOrderEntity order : orders) {
				 List<Long> userIdList = new ArrayList<Long>();
				 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(order.getPurchasingGroupId());
				 for(GroupConFigRelEntity rel : userOrderList ){
					 userIdList.add(rel.getUserId());
				 }
			     warnMainService.warnMessageSet(order.getId(), WarnConstant.ORDER_REJECT, userIdList);
			     
					
				 List<UserEntity> users = userServiceImpl.findByCompany(order.getVendor().getId());
				 for(UserEntity useritem : users){
					 
					//查询未确认的订单行（通过主单的id）
					 List<PurchaseOrderItemEntity> itemList1 = purchaseOrderItemDao.findUnConfirmOrderItemsByOrder(order.getId());
					 
					 List<PurchaseOrderItemEntity> itemList2 = purchaseOrderItemDao.findVoteOrderItemsByOrder(order.getId());
					 
					//解除供应商的待确认的消息
					 if(itemList1 == null || itemList1.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(order.getId(), WarnConstant.ORDER_UN_CONFIRM,useritem.getId());
					 }
					 
					//解除供应商的拒绝驳回的消息
					 if(itemList2 == null || itemList2.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(order.getId(), WarnConstant.ORDER_VETO,useritem.getId());
					 }
					 
				 }
			     
			     
			     
			}	
			
		}
		
		
		//删除订单待确认消息预警
		if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==confirmStatus.intValue()){			
			for(PurchaseOrderEntity order : orders) {
				
				 List<Long> userIdList = new ArrayList<Long>();
				 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(order.getPurchasingGroupId());
				 for(GroupConFigRelEntity rel : userOrderList ){
					 userIdList.add(rel.getUserId());
				 }
			     warnMainService.warnMessageSet(order.getId(), WarnConstant.ORDER_CONFIRM, userIdList);
				
				
				
				
				 List<UserEntity> users = userServiceImpl.findByCompany(order.getVendor().getId());
				 for(UserEntity useritem : users){
					 
					//查询未确认的订单行（通过主单的id）
					 List<PurchaseOrderItemEntity> itemList1 = purchaseOrderItemDao.findUnConfirmOrderItemsByOrder(order.getId());
					 
					 List<PurchaseOrderItemEntity> itemList2 = purchaseOrderItemDao.findVoteOrderItemsByOrder(order.getId());
					 
					//解除供应商的待确认的消息
					 if(itemList1 == null || itemList1.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(order.getId(), WarnConstant.ORDER_UN_CONFIRM,useritem.getId());
					 }
					 
					//解除供应商的拒绝驳回的消息
					 if(itemList2 == null || itemList2.size()==0 ){
						 warnMainService.delMessageByBillIdAndBillType(order.getId(), WarnConstant.ORDER_VETO,useritem.getId());
					 }
					 
				 }	
			}
			
			
			
			
			
		}
		
		
		
		
	}
	

	/**
	 * 正常的驳回和确认订单（供应商）
	 * @param orders
	 */
	public void confirmOrderItem(PurchaseOrderItemEntity itemEntity,UserEntity user,Integer confirmStatus,String reason){
		
		PurchaseOrderItemPlanRejectEntity reject;
		Timestamp curr=DateUtil.getCurrentTimestamp();
		itemEntity=purchaseOrderItemDao.findOne(itemEntity.getId());
		itemEntity.setConfirmStatus(confirmStatus);
		itemEntity.setConfirmTime(curr);
		itemEntity.setConfirmUser(user);
	   /////驳回写原因start
		if(PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()==confirmStatus.intValue()){
			itemEntity.setRejectReason(reason);
			itemEntity.setRejectUser(user);
			itemEntity.setRejectTime(curr);
			//驳回记录start
			 reject=new PurchaseOrderItemPlanRejectEntity();
			 reject.setReason(reason);
			 reject.setPurchaseOrderItemId(itemEntity.getId());
			 itemPlanRejectDao.save(reject);
			//驳回记录end
	     }
		purchaseOrderItemDao.save(itemEntity);
	}
	
	
	/**
	 *  对于确认后采购订单的修改做确认和修改dn
	 * @param orders
	 */
	public void modifyOrderItemAndDN(PurchaseOrderItemEntity itemEntity,UserEntity user,Integer confirmStatus){
		
		//保存确认状态
		Timestamp curr=DateUtil.getCurrentTimestamp();
		itemEntity=purchaseOrderItemDao.findOne(itemEntity.getId());
		itemEntity.setConfirmStatus(confirmStatus);
		itemEntity.setConfirmTime(curr);
		itemEntity.setConfirmUser(user);
		purchaseOrderItemDao.save(itemEntity);
		
		//同步sap
		
		//修改dn状态todo
		
	}
	
	
	 
}
