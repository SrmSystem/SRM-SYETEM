package com.qeweb.scm.purchasemodule.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;



@Service
@Transactional
public class PurchaseOrderService {

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
	private UserDao userDao;
	
	/**
	 * 获取订单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseOrderEntity> getPurchaseOrders(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderEntity.class);
		return purchaseOrderDao.findAll(spec,pagin);
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
	 * 获取订单明细
	 * @param id
	 * @return
	 */
	public PurchaseOrderItemEntity getOrderItem(Long id) {
		return purchaseOrderItemDao.findOne(id);
	}
	
	/**
	 * 获取订单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseOrderItemEntity> getPurchaseOrderItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemEntity.class);
		return purchaseOrderItemDao.findAll(spec,pagin);
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
		return purchaseOrderItemPlanDao.findAll(spec,pagin);
	}
	
	public Page<PurchaseOrderItemPlanEntity> getPurchaseOrderItemPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse_(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		return purchaseOrderItemPlanDao.findAll(spec,pagin);
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
	 * 批量发布订单
	 * @param orders
	 */
	public void publishOrders(List<PurchaseOrderEntity> orders, UserEntity user) {
		for(PurchaseOrderEntity order : orders) {
			order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			order.setPublishTime(DateUtil.getCurrentTimestamp());
			order.setPublishUser(user);
		}
		purchaseOrderDao.save(orders);
	}
	
	/**
	 * 确认订单
	 * @param orders
	 */
	public void confirmOrders(List<PurchaseOrderEntity> orders, UserEntity user) {
		for(PurchaseOrderEntity order : orders) {
			order.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			order.setConfirmUser(user);
			order.setConfirmTime(DateUtil.getCurrentTimestamp());
			//设置明细确认状态
			confirmOrderItems(order, user);
		}
		purchaseOrderDao.save(orders);
	}
	
	public void closeOrders(List<PurchaseOrderEntity> orders, UserEntity user) {
		for(PurchaseOrderEntity order : orders) {
			order.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);
			order.setCloseUser(user);
			order.setCloseTime(DateUtil.getCurrentTimestamp());
		}
		purchaseOrderDao.save(orders);
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
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Map<String, UserEntity> userMap = new HashMap<String, UserEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchaseOrderTransfer> list = validateTransfers(orderTranList, orgMap, materialMap, userMap, counts, logger);
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
				purchaseorder.setBuyer(orgMap.get(trans.getBuyerCode()));
				purchaseorder.setPurchaseUser(userMap.get(trans.getPurchaseUserCode())); 
				purchaseorder.setVendor(orgMap.get(trans.getVendorCode()));
				purchaseorder.setReceiveOrg(trans.getReceiveOrg()); 
				purchaseorder.setOrderDate(DateUtil.stringToTimestamp(trans.getOrderDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
				purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				purchaseorder.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				purchaseorder.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
				purchaseorder.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				purchaseorder.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				tmpMap.put(key, purchaseorder);
			} else {
				purchaseorder = tmpMap.get(key);
			}
			//设置明细单表信息
			purchaseOrderItem = getPurchaseOrderItemEntity(purchaseorder, trans.getItemNo());
			purchaseOrderItem.setOrder(purchaseorder);
			purchaseOrderItem.setMaterial(materialMap.get(trans.getMaterialCode()));
			purchaseOrderItem.setItemNo(StringUtils.convertToInteger(trans.getItemNo()));
			purchaseOrderItem.setReceiveOrg(trans.getReceiveOrg());
			purchaseOrderItem.setOrderQty(StringUtils.convertToDouble(trans.getOrderQty()));
			purchaseOrderItem.setRequestTime(DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			purchaseOrderItem.setCurrency(trans.getCurrency());
			purchaseOrderItem.setUnitName(trans.getUnitName());
			purchaseOrderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			purchaseOrderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			purchaseOrderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			purchaseOrderItemList.add(purchaseOrderItem);
			//采购计划
			purchaseItemPlan = getPurchaseItemPlan(purchaseOrderItem);
			long id = purchaseItemPlan.getId();
			PropertyUtils.copyProperties(purchaseItemPlan, purchaseOrderItem);
			purchaseItemPlan.setId(id); 
			if(id == 0l) {
				purchaseItemPlan.setReceiveQty(0d);
				purchaseItemPlan.setReturnQty(0d);
				purchaseItemPlan.setToDeliveryQty(0d);
				purchaseItemPlan.setDeliveryQty(0d);
				purchaseItemPlan.setDiffQty(0d);      
			}
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

	protected List<PurchaseOrderTransfer> validateTransfers(List<PurchaseOrderTransfer> orderTranList, Map<String, OrganizationEntity> orgMap, Map<String, MaterialEntity> materialMap, 
			Map<String, UserEntity> userMap, Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入的采购订单信息进行预处理,共有[" + (orderTranList == null ? 0 : orderTranList.size()) + "]条数据";
		logger.log(logMsg); 
		counts[0] = orderTranList.size();
		List<PurchaseOrderTransfer> ret = new ArrayList<PurchaseOrderTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<MaterialEntity> materialList = null;
		UserEntity purchaseUser = null;
		boolean lineValidat = true;
		int index  = 2; 
		for(PurchaseOrderTransfer trans : orderTranList) {
			if(!userMap.containsKey(trans.getPurchaseUserCode())) {
				purchaseUser = userDao.findByLoginName(trans.getPurchaseUserCode());
				if(purchaseUser == null) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],采购员代码[" + trans.getBuyerCode() + "]未在系统中维护,忽略此采购明细");
				} else {
					userMap.put(trans.getPurchaseUserCode(), purchaseUser);
				}
			}
			if(!orgMap.containsKey(trans.getBuyerCode())) {
				orgList = organizationDao.findByCode(trans.getBuyerCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],采方代码[" + trans.getBuyerCode() + "]未在系统中维护,忽略此采购明细");
				} else {
					orgMap.put(trans.getBuyerCode(), orgList.get(0));
				}
			}
			
			if(!orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCode(trans.getVendorCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],供方代码[" + trans.getVendorCode() + "]未在系统中维护,忽略此采购明细");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			if(!materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findByCode(trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + (index) + "],物料(" + trans.getMaterialCode() + "], 不存在,忽略此采购明细");
				} else {
					materialMap.put(trans.getMaterialCode(), materialList.get(0));
				}
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

	protected PurchaseOrderItemPlanEntity getPurchaseItemPlan(PurchaseOrderItemEntity purchaseOrderItem) {
		if(purchaseOrderItem.getId() == 0l)
			return new PurchaseOrderItemPlanEntity();
		
		PurchaseOrderItemPlanEntity itemplan = purchaseOrderItemPlanDao.findPurchaseOrderItemPlanEntityByItem(purchaseOrderItem.getId());
		return itemplan == null ? new PurchaseOrderItemPlanEntity() : itemplan;
	}

	protected PurchaseOrderItemEntity getPurchaseOrderItemEntity(PurchaseOrderEntity purchaseorder, String itemNo) {
		if(purchaseorder.getId() == 0l)
			return new PurchaseOrderItemEntity();
		
		PurchaseOrderItemEntity item = purchaseOrderItemDao.findPurchaseOrderItemEntityByMain(purchaseorder.getId(), StringUtils.convertToInteger(itemNo));
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
}
