package com.qeweb.scm.sap.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.PurchaseOrderSyncSAP;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.repository.DictItemDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialRelDao;
/**
 * 采购组 SAP->SRM
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchaseOrderSyncService extends BaseService{
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
	private DictItemDao dictItemDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	@Autowired
	private BuyerMaterialRelDao buyerMaterialRelDao;
	/**
	 * 同步sap采购订单数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger,String vendorCodes,String orderCodes)throws Exception {
	   iLogger.log("PurchaseOrderSyncService execute method execute start");
		Timestamp end  = DateUtil.getCurrentTimestamp();
		Date start = DateUtil.addDay(DateUtil.getCurrentTimestamp(), -1);
		Map<String, List<PurchaseOrderItemEntity>> res=PurchaseOrderSyncSAP.sync("20170427", "20170528", vendorCodes, orderCodes,iLogger);
		if(res!=null){
			iLogger.log("总条数:"+res.size());
			List<PurchaseOrderItemEntity> saveItemList=new ArrayList<PurchaseOrderItemEntity>();
			List<PurchaseOrderEntity> saveList=new ArrayList<PurchaseOrderEntity>();

			
			int i=0;
			for(String key:res.keySet()){
				List<PurchaseOrderItemEntity>  itemList=res.get(key);
				
				PurchaseOrderEntity purchaseorder=itemList.get(0).getOrder();
				
				if(StringUtils.isEmpty(purchaseorder.getBsart())){//采购凭证类型
					i++;
					iLogger.log(i+"采购凭证类型为空,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				DictItemEntity dictItem=dictItemDao.findByDictCodeAndCode("PURCHASE_ORDER_TYPE",purchaseorder.getBsart());
				if(dictItem==null){
					i++;
					iLogger.log(i+"采购凭证类型在系统不存在,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				
				if(StringUtils.isEmpty(purchaseorder.getVendorCode())){
					i++;
					iLogger.log(i+"供应商为空,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				
				OrganizationEntity vendor=organizationDao.findByCodeAndAbolished(purchaseorder.getVendorCode(), 0);
				if(vendor==null){
					i++;
					iLogger.log(i+"供应商在系统不存在:"+purchaseorder.getVendorCode());
					continue;
				}
				
				if(StringUtils.isEmpty(purchaseorder.getBukrs())){//公司
					i++;
					iLogger.log(i+"公司为空,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				CompanyEntity company=companyDao.findByCodeAndAbolished(purchaseorder.getBukrs(),BOHelper.UNABOLISHED_SINGEL);
				if(company==null){
					i++;
					iLogger.log(i+"公司在系统不存在:"+purchaseorder.getBukrs());
					continue;
				}
				
				if(StringUtils.isEmpty(purchaseorder.getBuyerCode())){
					i++;
					iLogger.log(i+"采购组织为空,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				
				OrganizationEntity buyer=organizationDao.findByCodeAndAbolished(purchaseorder.getBuyerCode(), 0);
				if(buyer==null){
					i++;
					iLogger.log(i+"采购组织在系统不存在:"+purchaseorder.getBuyerCode());
					continue;
				}
				
				if(StringUtils.isEmpty(purchaseorder.getEkgrp())){//采购组
					i++;
					iLogger.log(i+"采购组为空,订单号为:"+purchaseorder.getOrderCode());
					continue;
				}
				PurchasingGroupEntity purGroup=purchasingGroupDao.findByCodeAndAbolished(purchaseorder.getEkgrp(),BOHelper.UNABOLISHED_SINGEL);
				if(company==null){
					i++;
					iLogger.log(i+"采购组在系统不存在:"+purchaseorder.getEkgrp());
					continue;
				}
			    PurchaseOrderEntity oldOrder = purchaseOrderDao.getOrderByCode(purchaseorder.getOrderCode());
			    if(oldOrder==null){
			    	purchaseorder.setPurOrderTypeId(dictItem.getId());
					purchaseorder.setVendor(vendor);
					purchaseorder.setCompanyId(company.getId());
					purchaseorder.setBuyer(buyer);
					purchaseorder.setPurchasingGroupId(purGroup.getId());
					purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
					purchaseorder.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
					purchaseorder.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
					purchaseorder.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
					purchaseorder.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
					purchaseorder.setIsOutData(1);
					purchaseorder.setAbolished(0);
			    }else{
			    	purchaseorder.setPurOrderTypeId(dictItem.getId());
			    	oldOrder.setBsart(purchaseorder.getBsart());
			    	oldOrder.setZterm(purchaseorder.getZterm());
			    	oldOrder.setWaers(purchaseorder.getWaers());
			    	oldOrder.setWaersms(purchaseorder.getWaersms());
			    	oldOrder.setCompanyId(company.getId());
			    	oldOrder.setBuyer(buyer);
			    	oldOrder.setPurchasingGroupId(purGroup.getId());
			    	oldOrder.setIsOutData(1);
			    	oldOrder.setAedat(purchaseorder.getAedat());
			    	oldOrder.setErnam(purchaseorder.getErnam());
			    	oldOrder.setFrgke(purchaseorder.getFrgke());
			    }			
				List<PurchaseOrderItemEntity> lsItemList=new ArrayList<PurchaseOrderItemEntity>();
				for (PurchaseOrderItemEntity purchaseOrderItem : itemList) {
					if(StringUtils.isEmpty(purchaseOrderItem.getMaterialCode())){
						i++;
						iLogger.log(i+"物料为空:"+purchaseOrderItem.getItemNo());
						lsItemList=null;
						break;
					}
					MaterialEntity mat=null;
					List<MaterialEntity> materials=materialDao.findByCodeAndAbolished(purchaseOrderItem.getMaterialCode(), 0);
					if(materials==null||materials.size()<=0){
						i++;
//						iLogger.log(i+"物料在系统不存在,新增物料:"+purchaseOrderItem.getMaterialCode());
						iLogger.log(i+"物料为空:"+purchaseOrderItem.getItemNo());
						lsItemList=null;
						break;
/*						MaterialEntity material=new MaterialEntity();
						material.setCode(purchaseOrderItem.getMaterialCode());
						material.setName(purchaseOrderItem.getMaterialName());
						material.setAbolished(0);
						material.setIsOutData(1);
						material.setMaterialTypeId(null);
						materialDao.save(material);
						BuyerMaterialRelEntity rel=new BuyerMaterialRelEntity();
						rel.setBuyerId(buyer.getId());
						rel.setMaterialId(material.getId());
						rel.setAbolished(0);
						rel.setIsOutData(1);
						buyerMaterialRelDao.save(rel);
						
						mat=material;*/
					}else{
						mat=materials.get(0);
					}
					if(StringUtils.isEmpty(purchaseOrderItem.getWerks())){
						i++;
						iLogger.log(i+"工厂为空:"+purchaseOrderItem.getItemNo());
						lsItemList=null;
						break;
					}
					FactoryEntity factory=factoryDao.findByCodeAndAbolished(purchaseOrderItem.getWerks(), 0);
					if(factory==null){
						i++;
						iLogger.log(i+"工厂在系统不存在:"+purchaseOrderItem.getWerks());
						lsItemList=null;
						break;
					}
					
					PurchaseOrderItemEntity oldItem=purchaseOrderItemDao.findPurchaseOrderItemByOrderCodeAndItemNo(purchaseorder.getOrderCode(),purchaseOrderItem.getItemNo());
					if(oldItem==null){
						if(oldOrder==null){
							purchaseOrderItem.setOrder(purchaseorder);
						}else{
							purchaseOrderItem.setOrder(oldOrder);
						}
						purchaseOrderItem.setFactoryId(factory.getId());
						purchaseOrderItem.setMaterial(mat);
						purchaseOrderItem.setReceiveQty(0d);
						purchaseOrderItem.setReturnQty(0d);
						purchaseOrderItem.setOnwayQty(0d);
						purchaseOrderItem.setUndeliveryQty(0d);
						purchaseOrderItem.setDiffQty(0d);      
						purchaseOrderItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
						purchaseOrderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
						purchaseOrderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
						purchaseOrderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
						purchaseOrderItem.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
						purchaseOrderItem.setIsOutData(1);
						purchaseOrderItem.setAbolished(0);
						lsItemList.add(purchaseOrderItem);
						iLogger.log("新增订单明细:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
					}else{
						iLogger.log("修改订单明细:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
						//修改
						if(oldItem.getConfirmStatus()!=1){//确认后不修改
							if(oldOrder==null){
								oldItem.setOrder(purchaseorder);
							}else{
								oldItem.setOrder(oldOrder);
							}
							oldItem.setItemNo(purchaseOrderItem.getItemNo());
							oldItem.setKnttp(purchaseOrderItem.getKnttp());
							oldItem.setPstyp(purchaseOrderItem.getPstyp());
							oldItem.setMaterial(mat);
							oldItem.setMenge(purchaseOrderItem.getMenge());
							oldItem.setMeins(purchaseOrderItem.getMeins());
							oldItem.setRequestTime(purchaseOrderItem.getRequestTime());
							oldItem.setFactoryId(factory.getId());
							oldItem.setMatkl(purchaseOrderItem.getMatkl());
							oldItem.setMatklms(purchaseOrderItem.getMatklms());
							oldItem.setIdnlf(purchaseOrderItem.getIdnlf());
							oldItem.setRetpo(purchaseOrderItem.getRetpo());
							oldItem.setZfree(purchaseOrderItem.getZfree());
							oldItem.setLoekz(purchaseOrderItem.getLoekz());
							oldItem.setZlock(purchaseOrderItem.getZlock());
							oldItem.setBstae(purchaseOrderItem.getBstae());//add by chao.gu 20170929
							oldItem.setKzabs(purchaseOrderItem.getKzabs());
							oldItem.setIsOutData(1);
							lsItemList.add(oldItem);
						}
					}
				}
				if(lsItemList!=null&&lsItemList.size()>0){
					if(oldOrder==null){
						iLogger.log("新增订单:"+purchaseorder.getOrderCode());
						saveList.add(purchaseorder);
					}else{
						iLogger.log("修改订单:"+purchaseorder.getOrderCode());
						saveList.add(oldOrder);
					}
					saveItemList.addAll(lsItemList);
				}
	}

			purchaseOrderDao.save(saveList);
			purchaseOrderItemDao.save(saveItemList);
			
			iLogger.log("PurchaseOrderSyncService execute method execute end");
			return true;
		}else{
			 iLogger.log("SAP连接失败");
			 return false;
		}
	   
   }
	
	
}
