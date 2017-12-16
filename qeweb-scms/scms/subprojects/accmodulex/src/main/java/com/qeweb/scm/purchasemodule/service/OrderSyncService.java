package com.qeweb.scm.purchasemodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.repository.DictItemDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
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
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.GoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseGoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.sap.service.BOHelper;
import com.qeweb.scm.sap.service.DnDelToSapService;
import com.qeweb.scm.sap.service.DnUpdateSyncService;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialRelDao;



@Service
@Transactional
public class OrderSyncService extends BaseService {
	
	@Autowired
	private DeliveryDao deliveryDao;
	
	@Autowired
	private DeliveryItemDao deliveryItemDao;

	@Autowired
	private GoodsRequestDao goodsRequestDao;
	
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
	
	@Autowired
	private PurchaseGoodsRequestDao purchaseGoodsRequestDao;
	
	@Autowired
	private DnUpdateSyncService dnUpdateSyncService;
	
	@Autowired
	private DnDelToSapService dnDelToSapService;
	
	@Autowired
	private PurchaseGoodsRequestService purchaseGoodsRequestService;
	
	@Autowired
	private WarnMainService warnMainService;
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	public List<String> commonReturn(List<String> res,String msg,StringBuilder log){
		res.add(msg);
		res.add(log.toString());
		return res;
	}
	
	public List<String> execute(Map<String, List<PurchaseOrderItemEntity>> res,StringBuilder log,List<String> resList){
		resList=new ArrayList<String>();
		String msg="";
		if(res!=null){
			List<PurchaseOrderItemEntity> saveItemList=new ArrayList<PurchaseOrderItemEntity>();
			List<PurchaseOrderEntity> saveList=new ArrayList<PurchaseOrderEntity>();
			
			for(String key:res.keySet()){
				Boolean isMin=false;
				List<PurchaseOrderItemEntity>  itemList=res.get(key);
				
				PurchaseOrderEntity purchaseorder=itemList.get(0).getOrder();
				
				if(StringUtils.isEmpty(purchaseorder.getBsart())){//采购凭证类型
					msg="采购凭证类型为空,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				DictItemEntity dictItem=dictItemDao.findByDictCodeAndCode("PURCHASE_ORDER_TYPE",purchaseorder.getBsart());
				if(dictItem==null){
					msg="采购凭证类型在系统不存在,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				
				if(StringUtils.isEmpty(purchaseorder.getVendorCode())){
					msg="供应商为空,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				
				OrganizationEntity vendor=organizationDao.findByCodeAndAbolished(purchaseorder.getVendorCode(), 0);
				if(vendor==null){
					msg="供应商在系统不存在:"+purchaseorder.getVendorCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				
				if(StringUtils.isEmpty(purchaseorder.getBukrs())){//公司
					msg="公司为空,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				CompanyEntity company=companyDao.findByCodeAndAbolished(purchaseorder.getBukrs(),BOHelper.UNABOLISHED_SINGEL);
				if(company==null){
					msg="公司在系统不存在:"+purchaseorder.getBukrs();
					log.append(msg);
					return commonReturn(resList, msg, log);
				}
				
				if(StringUtils.isEmpty(purchaseorder.getBuyerCode())){
					msg="采购组织为空,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				
				OrganizationEntity buyer=organizationDao.findByCodeAndAbolished(purchaseorder.getBuyerCode(), 0);
				if(buyer==null){
					msg="采购组织在系统不存在:"+purchaseorder.getBuyerCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				
				if(StringUtils.isEmpty(purchaseorder.getEkgrp())){//采购组
					msg="采购组为空,订单号为:"+purchaseorder.getOrderCode();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
				PurchasingGroupEntity purGroup=purchasingGroupDao.findByCodeAndAbolished(purchaseorder.getEkgrp(),BOHelper.UNABOLISHED_SINGEL);
				if(company==null){
					msg="采购组在系统不存在:"+purchaseorder.getEkgrp();
					log.append(msg);
					log.append("\r\n");
					return commonReturn(resList, msg, log);
				}
			    PurchaseOrderEntity oldOrder = purchaseOrderDao.getOrderByCode(purchaseorder.getOrderCode());
			    
			    if(oldOrder==null){
				    if("B".equals(purchaseorder.getFrgke())){//B代表未审批，R代表审批
//				    	msg="未审批新建订单不处理,订单号:"+purchaseorder.getOrderCode();
//				    	log.append("未审批新建订单不处理,订单号:"+purchaseorder.getOrderCode());
//				    	log.append("\r\n");
//				    	return commonReturn(resList, msg, log);
				    	purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				    }else{
				    	purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
						purchaseorder.setPublishTime(DateUtil.getCurrentTimestamp());
				    }
			    	purchaseorder.setPurOrderTypeId(dictItem.getId());
					purchaseorder.setVendor(vendor);
					purchaseorder.setCompanyId(company.getId());
					purchaseorder.setBuyer(buyer);
					purchaseorder.setPurchasingGroupId(purGroup.getId());
					purchaseorder.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
					purchaseorder.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
					purchaseorder.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
					purchaseorder.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
					purchaseorder.setIsOutData(1);
					purchaseorder.setAbolished(0);
					purchaseorder.setAedat(purchaseorder.getAedat());
					purchaseorder.setUdate(purchaseorder.getUdate());
			    }else{
			    	 if("B".equals(purchaseorder.getFrgke())){//B代表未审批，R代表审批
			    			purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
			    	 }else{
			    		 	purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
							purchaseorder.setPublishTime(DateUtil.getCurrentTimestamp());
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
					    	oldOrder.setUdate(purchaseorder.getUdate());
					    	oldOrder.setErnam(purchaseorder.getErnam());
					    	oldOrder.setFrgke(purchaseorder.getFrgke());
					    	oldOrder.setZterm(purchaseorder.getZterm());
					    	oldOrder.setZtermms(purchaseorder.getZtermms());
			    	 }
			    	 oldOrder.setFrgke(purchaseorder.getFrgke());
			    }			
				List<PurchaseOrderItemEntity> lsItemList=new ArrayList<PurchaseOrderItemEntity>();
				for (PurchaseOrderItemEntity purchaseOrderItem : itemList) {
					if(StringUtils.isEmpty(purchaseOrderItem.getMaterialCode())){
						lsItemList=null;
						msg="物料为空:"+purchaseOrderItem.getItemNo();
						log.append(msg);
						log.append("\r\n");
						return commonReturn(resList, msg, log);
					}
					MaterialEntity mat=null;
					List<MaterialEntity> materials=materialDao.findByCodeAndAbolished(purchaseOrderItem.getMaterialCode(), 0);
					if(materials==null||materials.size()<=0){
						lsItemList=null;
						msg="物料在系统不存在:"+purchaseOrderItem.getMaterialCode();
						log.append(msg);
						log.append("\r\n");
						return commonReturn(resList, msg, log);
					}else{
						mat=materials.get(0);
					}
					if(StringUtils.isEmpty(purchaseOrderItem.getWerks())){
						lsItemList=null;
						msg="工厂为空:"+purchaseOrderItem.getItemNo();
						log.append(msg);
						log.append("\r\n");
						return commonReturn(resList, msg, log);
					}
					FactoryEntity factory=factoryDao.findByCodeAndAbolished(purchaseOrderItem.getWerks(), 0);
					if(factory==null){
						msg="工厂在系统不存在:"+purchaseOrderItem.getWerks();
						log.append(msg);
						log.append("\r\n");
						lsItemList=null;
						return commonReturn(resList, msg, log);
					}
					
					PurchaseOrderItemEntity oldItem=purchaseOrderItemDao.findPurchaseOrderItemByOrderCodeAndItemNo(purchaseorder.getOrderCode(),purchaseOrderItem.getItemNo());
					if(oldItem==null){
					    if("B".equals(purchaseorder.getFrgke())){//B代表未审批，R代表审批
//					    	log.append("未审批新建订单明细不处理,订单号:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
//					    	log.append("\r\n");
//					    	continue;
					    	purchaseOrderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
					    }else{
					    	purchaseOrderItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
					    }
					    
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
					
						purchaseOrderItem.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
						purchaseOrderItem.setIsOutData(1);
						purchaseOrderItem.setAbolished(0);
						
						lsItemList.add(purchaseOrderItem);
						log.append("新增订单明细:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
						log.append("\r\n");
					}else{

						
						if(oldOrder==null){
							oldItem.setOrder(purchaseorder);
						}else{
							oldItem.setOrder(oldOrder);
						}
						oldItem.setReceiveOrg(purchaseOrderItem.getReceiveOrg());
						//未审批
						if("B".equals(purchaseorder.getFrgke())){//B代表未审批，R代表审批
							oldItem.setLockStatus("1");//锁定订单行
							//未确认的
							if(oldItem.getConfirmStatus()!=1){
								oldItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);//取消发布
								lsItemList.add(oldItem);
								log.append("订单未审批未确认，取消发布:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
								log.append("\r\n");
							}else{
								//已确认
								//未修改数量
								if(purchaseOrderItem.getIsModify()==0){
									log.append("订单未审批已确认，但未修改数量:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
									log.append("\r\n");
								}else{
									log.append("订单未审批已确认，并且修改数量，锁定行:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
									log.append("\r\n");
									//修改数量，只能是改大
									
								}
								oldItem.setItemNo(purchaseOrderItem.getItemNo());
								oldItem.setKnttp(purchaseOrderItem.getKnttp());
								oldItem.setPstyp(purchaseOrderItem.getPstyp());
								oldItem.setMaterial(mat);
								oldItem.setMenge(purchaseOrderItem.getMenge());
								oldItem.setOrderQty(Double.valueOf(purchaseOrderItem.getMenge()));
								oldItem.setIsModify(purchaseOrderItem.getIsModify());
								oldItem.setMeins(purchaseOrderItem.getMeins());
								oldItem.setUnitName(purchaseOrderItem.getMeins());
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
//								private String BPRME; //基本单位
//								private String BPUMZ; //分母
//								private String BPUMN; //分子
//								private String LFIMG; //基本数量
								oldItem.setBPRME(purchaseOrderItem.getBPRME());
								oldItem.setBPUMZ(purchaseOrderItem.getBPUMZ());
								oldItem.setBPUMN(purchaseOrderItem.getBPUMN());
								oldItem.setElikz(purchaseOrderItem.getElikz());
							
								//add by chao.gu 20170812
								oldItem.setZSFXP(purchaseOrderItem.getZSFXP());//是否新品，X为新品，''为量产
								oldItem.setZWQSL(purchaseOrderItem.getZWQSL());//SAP未清数量
								oldItem.setZSLSX(purchaseOrderItem.getZSLSX());//订单数量上限
								//add end
								oldItem.setIsOutData(1);
								lsItemList.add(oldItem);

							}
							//已审批
						}else{
							oldItem.setLockStatus("");//锁定订单行
							//未确认的
							if(oldItem.getConfirmStatus()!=1){
								log.append("订单已审批未确认:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
								log.append("\r\n");
								oldItem.setItemNo(purchaseOrderItem.getItemNo());
								oldItem.setKnttp(purchaseOrderItem.getKnttp());
								oldItem.setPstyp(purchaseOrderItem.getPstyp());
								oldItem.setMaterial(mat);
								oldItem.setMenge(purchaseOrderItem.getMenge());
								oldItem.setOrderQty(Double.valueOf(purchaseOrderItem.getMenge()));
								oldItem.setIsModify(purchaseOrderItem.getIsModify());
								oldItem.setMeins(purchaseOrderItem.getMeins());
								oldItem.setUnitName(purchaseOrderItem.getMeins());
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
								oldItem.setBPRME(purchaseOrderItem.getBPRME());
								oldItem.setBPUMZ(purchaseOrderItem.getBPUMZ());
								oldItem.setBPUMN(purchaseOrderItem.getBPUMN());
								oldItem.setElikz(purchaseOrderItem.getElikz());
								//add by chao.gu 20170810无供货关系时直接塞传过来的值
								oldItem.setLFIMG(purchaseOrderItem.getLFIMG());//订单基本数量
								oldItem.setSurBaseQty(purchaseOrderItem.getSurBaseQty());
								oldItem.setSurOrderQty(purchaseOrderItem.getSurOrderQty());
								//add end
								//add by chao.gu 20170812
								oldItem.setZSFXP(purchaseOrderItem.getZSFXP());//是否新品，X为新品，''为量产
								oldItem.setZWQSL(purchaseOrderItem.getZWQSL());//SAP未清数量
								oldItem.setZSLSX(purchaseOrderItem.getZSLSX());//订单数量上限
								//add end
								oldItem.setIsOutData(1);
								oldItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
								lsItemList.add(oldItem);
							//已确认	
							}else{
								//未修改数量
								if(purchaseOrderItem.getIsModify()==0){
									log.append("订单已审批已确认，但未修改数量:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
									log.append("\r\n");
									oldItem.setItemNo(purchaseOrderItem.getItemNo());
									oldItem.setKnttp(purchaseOrderItem.getKnttp());
									oldItem.setPstyp(purchaseOrderItem.getPstyp());
									oldItem.setMaterial(mat);
									oldItem.setMenge(purchaseOrderItem.getMenge());
									oldItem.setOrderQty(Double.valueOf(purchaseOrderItem.getMenge()));
									oldItem.setIsModify(purchaseOrderItem.getIsModify());
									oldItem.setMeins(purchaseOrderItem.getMeins());
									oldItem.setUnitName(purchaseOrderItem.getMeins());
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
									oldItem.setBPRME(purchaseOrderItem.getBPRME());
									oldItem.setBPUMZ(purchaseOrderItem.getBPUMZ());
									oldItem.setBPUMN(purchaseOrderItem.getBPUMN());
									oldItem.setElikz(purchaseOrderItem.getElikz());
									oldItem.setKzabs(purchaseOrderItem.getKzabs());
									//add by chao.gu 20170812
									oldItem.setZSFXP(purchaseOrderItem.getZSFXP());//是否新品，X为新品，''为量产
									oldItem.setZWQSL(purchaseOrderItem.getZWQSL());//SAP未清数量
									oldItem.setZSLSX(purchaseOrderItem.getZSLSX());//订单数量上限
									//add end
									oldItem.setIsOutData(1);
									oldItem.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
									//add by chao.gu 20171124
									oldItem.setLFIMG(purchaseOrderItem.getLFIMG());//基本数量
									//add end
									lsItemList.add(oldItem);
								}else{
									//add by chao.gu 20170812
									oldItem.setZSFXP(purchaseOrderItem.getZSFXP());//是否新品，X为新品，''为量产
									oldItem.setZWQSL(purchaseOrderItem.getZWQSL());//SAP未清数量
									oldItem.setZSLSX(purchaseOrderItem.getZSLSX());//订单数量上限
									//add end
									
									Double oldOrderQty=oldItem.getOrderQty();
									Double newOrderQty=purchaseOrderItem.getOrderQty();
									Double  subQty=BigDecimalUtil.sub(newOrderQty, oldOrderQty);
									oldItem.setMenge(purchaseOrderItem.getMenge());
									oldItem.setOrderQty(Double.valueOf(purchaseOrderItem.getMenge()));
									oldItem.setLFIMG(purchaseOrderItem.getLFIMG());
									if(subQty>0){
										//改大  修改订单剩余数量就行
										oldItem.setSurOrderQty(BigDecimalUtil.add(oldItem.getSurOrderQty(), subQty));
										oldItem.setSurBaseQty(orderQtyToBaseQty(oldItem.getBPUMN(), oldItem.getBPUMZ(), oldItem.getSurOrderQty()));
									
										
										log.append("订单已审批已确认，并且修改数量改大:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
										log.append("\r\n");
										lsItemList.add(oldItem);
									}else{
										
										//改小
										subQty=Math.abs(subQty);//改小了多少
										log.append("订单已审批已确认，并且修改数量改小:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
										log.append("\r\n");
										//订单剩余数量大于等于 改小的量
										if(oldItem.getSurOrderQty()>=subQty){
											oldItem.setSurOrderQty(BigDecimalUtil.sub(oldItem.getSurOrderQty(), subQty));
											oldItem.setSurBaseQty(orderQtyToBaseQty(oldItem.getBPUMN(), oldItem.getBPUMZ(), oldItem.getSurOrderQty()));
											lsItemList.add(oldItem);	
											log.append("订单剩余数量大于等于改小的量:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
											log.append("\r\n");
										}else{
											 isMin=true;
											 oldItem.setSurOrderQty(BigDecimalUtil.sub(oldItem.getSurOrderQty(), subQty));
												oldItem.setSurBaseQty(orderQtyToBaseQty(oldItem.getBPUMN(), oldItem.getBPUMZ(), oldItem.getSurOrderQty()));
												lsItemList.add(oldItem);	
											//按时间升序
											List<PurchaseOrderItemPlanEntity> planList = purchaseOrderItemPlanDao.findItemPlanEntitysByItemOrderByRequest(oldItem.getId());
											//已有ASN，但还有待发货数量的供货计划
											List<PurchaseOrderItemPlanEntity> hasDlvPlanList = new ArrayList<PurchaseOrderItemPlanEntity>();
											if(planList!=null&&planList.size()>0){
												for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : planList) {
													if(subQty<=0){//需要处理的量处理完了，结束
														break;
													}
													//add by chao.gu 20170810若没有ASN则去修改供货计划并且要货计划，否则不执行，需要手工回退
													List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(purchaseOrderItemPlanEntity.getId());
													if(null==deliveryList || deliveryList.size()==0){
														Double downQty=0D;
														//需要改小的数量与供货计划数量比较
														Double d =BigDecimalUtil.sub(subQty, purchaseOrderItemPlanEntity.getOrderQty());
	
														if(d>0){//需要改小的数量大，也就是说还需要改下一笔供货计划，当前供货计划删除
															 downQty=purchaseOrderItemPlanEntity.getOrderQty();
															 log= deletePlan(purchaseOrderItemPlanEntity,log);
														}else if(d<0){//需要改小的数量小，就处理当前供货计划就好，当前供货计划的量扣除需要处理的量
															 downQty=subQty;//需要处理的发货单数量
															 log=updatePlan(purchaseOrderItemPlanEntity,downQty,log);
															
														}else{
															//需要改小的数量与当前供货计划数量一样，删除当前供货计划正好，当前供货计划删除
															downQty=purchaseOrderItemPlanEntity.getOrderQty();
															 log=deletePlan(purchaseOrderItemPlanEntity,log);
														}
														subQty=BigDecimalUtil.sub(subQty, downQty);
													}else{
														//有应发数量
														Double shouldQty=BigDecimalUtil.sub(purchaseOrderItemPlanEntity.getOrderQty(), BigDecimalUtil.add(null==purchaseOrderItemPlanEntity.getDeliveryQty()?0:purchaseOrderItemPlanEntity.getDeliveryQty(), null==purchaseOrderItemPlanEntity.getToDeliveryQty()?0:purchaseOrderItemPlanEntity.getToDeliveryQty()));
														if(shouldQty>0){
														   hasDlvPlanList.add(purchaseOrderItemPlanEntity);
														}
													}
												}
												
												//add by chao.gu 20170821
												if(subQty>0){//还有未减的数量
													if(null!=hasDlvPlanList && hasDlvPlanList.size()>0){
														for (PurchaseOrderItemPlanEntity orderItemPlan : hasDlvPlanList) {
															if(subQty<=0){//需要处理的量处理完了，结束
																break;
															}
															Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
															//需要改小的数量与供货计划数量比较
															Double d =BigDecimalUtil.sub(subQty, shouldQty);
															Double downQty=0D;
															if(d>=0){//需要改小的数量大，也就是说还需要改下一笔供货计划，当前供货计划应发数量全部修改
																downQty=shouldQty;
															}else if(d<0){//需要改小的数量小，就处理当前供货计划就好，当前供货计划的量扣除需要处理的量
																downQty=subQty;//需要处理的发货单数量
															}
															updatePlan(orderItemPlan,downQty,log);
															subQty=BigDecimalUtil.sub(subQty,downQty);
														}
													}
												}
												//add end
												
											}else{
												//还没有供货计划 不存在这个逻辑，一定有供货计划才会扣除订单剩余数量
											}
										}
									}
								}
							}
						}
						
						log.append("修改订单明细:"+purchaseorder.getOrderCode()+"-"+purchaseOrderItem.getItemNo());
						log.append("\r\n");
					}
				}
				if((lsItemList!=null&&lsItemList.size()>0)||isMin){
					if(oldOrder==null){
						log.append("新增订单:"+purchaseorder.getOrderCode());
						commOrderPublishStatus(purchaseorder,lsItemList);
						saveList.add(purchaseorder);
					}else{
						log.append("修改订单:"+purchaseorder.getOrderCode());
						commOrderPublishStatus(oldOrder,lsItemList);
						saveList.add(oldOrder);
					}
					saveItemList.addAll(lsItemList);
				}
	}

			
			
			purchaseOrderDao.save(saveList);
			purchaseOrderItemDao.save(saveItemList);
			
			//添加消息和预警
			for(PurchaseOrderEntity order   : saveList ){
				 //采购订单同步(修改)提醒
				 List<Long> userIdList = new ArrayList<Long>();
				 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(order.getPurchasingGroupId());
				 for(GroupConFigRelEntity rel : userOrderList ){
					 userIdList.add(rel.getUserId());
				 }
			     warnMainService.warnMessageSet( order.getId(), WarnConstant.ORDER_MODIFY, userIdList);
			     
			     //采购订单待确认提醒
			     List<Long> ids = new ArrayList<Long>();
			     List<UserEntity> users = userServiceImpl.findByCompany(order.getVendor().getId());
				 for(UserEntity useritem : users){
					 ids.add(useritem.getId());
				 }	
			     warnMainService.warnMessageSet( order.getId(), WarnConstant.ORDER_UN_CONFIRM, ids);
			}
			
			
			
			 
			//add by chao.gu 20170830 判断是否是退货 1退货 0正常
			if(null!=saveList && saveList.size()>0){
				for (PurchaseOrderEntity purchaseOrderEntity : saveList) {
					List<PurchaseOrderItemEntity> poList  = purchaseOrderItemDao.findByOrderId(purchaseOrderEntity.getId(),0);
					int returnStatus= 0;
					List<PurchaseOrderItemEntity>  returnItems = purchaseOrderItemDao.findReturnOrderItemsByOrder(purchaseOrderEntity.getId());
				    if(null!=returnItems && returnItems.size()>0){
				    	//若订单明细全部是退货则主单是退货
				    	if(poList.size() == returnItems.size()){
				    		returnStatus=1;
				    	}
				    }
					purchaseOrderEntity.setReturnStatus(returnStatus);
				}
			}
			//add end
			
			//add by  hao.qin 判断当前明细行的退货标识的数量 与订单的总行数 设置订单的发布和确认状态
			List<PurchaseOrderItemEntity>  deleteList = new ArrayList<PurchaseOrderItemEntity>();
			if(null!=saveList && saveList.size()>0){
				for (PurchaseOrderEntity purchaseOrderEntity : saveList) {
					List<PurchaseOrderItemEntity> poList  = purchaseOrderItemDao.findByOrderId(purchaseOrderEntity.getId(),0);
					deleteList = purchaseOrderItemDao.findDeleteItemsByOrder(purchaseOrderEntity.getId());
				    if(null!=deleteList && deleteList.size()>0){
				    	//若订单明细全部是删除状态的话
				    	if(poList.size() == deleteList.size()){
				    		purchaseOrderEntity.setPublishStatus(0);
				    		purchaseOrderEntity.setConfirmStatus(0);
				    	}
				    	//修改这些删除标识的订单行项目
				    	for(PurchaseOrderItemEntity item : deleteList){
				    		item.setPublishStatus(0);
				    		item.setConfirmStatus(0);
				    	}
				    }
					
				}
			}
			
			
			//add end
			purchaseOrderDao.save(saveList);
			purchaseOrderItemDao.save(deleteList);
			
			
		}
		return  commonReturn(resList, msg, log);
	}
	
	/**
	 * 根据明细发布状态判断订单主表的发布状态
	 * @param purchaseorder
	 * @param lsItemList
	 */
	public void commOrderPublishStatus(PurchaseOrderEntity purchaseorder,List<PurchaseOrderItemEntity> lsItemList){
		int count_all=0;
		int count_part=0;
		for (PurchaseOrderItemEntity purchaseOrderItemEntity : lsItemList) {
			if(null!=purchaseOrderItemEntity.getPublishStatus() && PurchaseConstans.PUBLISH_STATUS_YES.intValue()==purchaseOrderItemEntity.getPublishStatus().intValue()){
				count_all++;
			}else if(null!=purchaseOrderItemEntity.getPublishStatus() && PurchaseConstans.PUBLISH_STATUS_PART.intValue()==purchaseOrderItemEntity.getPublishStatus().intValue()){
				count_part++;
			}
		}
		
		if(count_all>0 && count_all==lsItemList.size()){
			purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			purchaseorder.setPublishTime(DateUtil.getCurrentTimestamp());
		}else if(count_part >0 || count_all>0){
			purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_PART);
		}else{
			purchaseorder.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
		}
	}
	
	/**
	 * 删除供货计划
	 * @param plan
	 */
	public StringBuilder deletePlan(PurchaseOrderItemPlanEntity plan,StringBuilder log){
		//未确认
		if(plan.getConfirmStatus()!=1){
		}else{
			//已确认
			//是否创建asn
			//modify by chao.gu 20170810
			/*List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(plan.getId());
			//已创建asn
			if(deliveryList!=null&&deliveryList.size()>0){
				
				for (DeliveryItemEntity deliveryItemEntity : deliveryList) {
					log=deleteDlv(deliveryItemEntity,log);
				}
				
			}*/
			//modify end
		}
		log=writeQtyByPlan(plan,plan.getOrderQty(),log);//直接删供货计划 回写要货计划数量 释放订单数量
		plan.setAbolished(BOHelper.ABOLISHED_SINGEL);
		purchaseOrderItemPlanDao.save(plan);
		return log;
	}
	
	/**
	 * 修改供货计划数量
	 * @param plan
	 * @param qty 需要减去的数量
	 */
	public StringBuilder updatePlan(PurchaseOrderItemPlanEntity plan,Double qty,StringBuilder log){
		double deliveryQty=qty;
		
		//未发数量大于等于需要处理的数量   减去处理数量
		//Double shouldQty=BigDecimalUtil.sub(plan.getOrderQty(), BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty()));
		plan.setOrderQty(BigDecimalUtil.sub(plan.getOrderQty(), qty));
		plan.setBaseQty(orderQtyToBaseQty(plan.getOrderItem().getBPUMN(), plan.getOrderItem().getBPUMZ(), plan.getOrderQty()));
		Double undeliveryQty = BigDecimalUtil.sub(plan.getOrderQty(), BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty()));
		plan.setUndeliveryQty(undeliveryQty);//未发数量
		//modify by chao.gu 20170810
		/*
		if(shouldQty>=qty){
		}else{
			
			//未确认
			if(plan.getConfirmStatus()!=1){
			}else{
				//已确认
				//是否创建asn
				List<DeliveryItemEntity> deliveryList=deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(plan.getId());
				//已创建asn
				if(deliveryList!=null&&deliveryList.size()>0){
					for (DeliveryItemEntity deliveryItemEntity : deliveryList) {
						DeliveryEntity dlv=deliveryItemEntity.getDelivery();
						Double dd =BigDecimalUtil.sub(deliveryQty, deliveryItemEntity.getDeliveryQty());
						if(dd>0){//还需要处理下一个发货单
							log=deleteDlv(deliveryItemEntity,log);
						}else if(dd<0){//处理当前asn就好
							log=updateDlv(deliveryItemEntity,deliveryQty,log);
						}else{
							log=deleteDlv(deliveryItemEntity,log);
						}
						deliveryQty=BigDecimalUtil.sub(deliveryQty, deliveryItemEntity.getDeliveryQty());
					}
				}
			}
			
		}*/
		//modify end
	log=writeQtyByPlan(plan, qty,log);
	purchaseOrderItemPlanDao.save(plan);
	return log;
}
	
	/**
	 * 删除发货单
	 * @param dlvItem
	 */
	public StringBuilder deleteDlv(DeliveryItemEntity dlvItem,StringBuilder log,ILogger logger){
		DeliveryEntity dlv=dlvItem.getDelivery();
		//是否审批
		if(dlv.getAuditStatus()!=1){
			//未审批 驳回asn 供应商只能在发货单页 面取消发货单
			log=cancelDlv(dlv, log);
		}else{
			//已审批
			//未发货
			if(dlv.getDeliveryStatus()!=1){
				log=cancelDlv(dlv, log);
			}else{
				//已发货 删除srmDN
				//删除SAP DN 
				delDn(dlvItem, log,logger);
			}
		}
		return log;
	}
	
	
	/**
	 * 更新发货单
	 * @param dlvItem
	 */
	public StringBuilder updateDlv(DeliveryItemEntity dlvItem,Double qty,StringBuilder log,ILogger logger){
		DeliveryEntity dlv=dlvItem.getDelivery();
		//是否审批
		if(dlv.getAuditStatus()!=1){
			//未审批 驳回asn 供应商只能在发货单页 面取消发货单
			log=cancelDlv(dlv, log);
		}else{
			//已审批
			if(dlv.getDeliveryStatus()!=1){
				log=cancelDlv(dlv, log);
			}else{
				//已发货
				//更新SAP DN to do
				updateDn(dlvItem,qty, log,logger);
			}
		}
		return log;
	}
	
	/**
	 * 删除dn
	 * @param dlvItem
	 * @param log
	 * @return
	 */
	public StringBuilder delDn(DeliveryItemEntity dlvItem,StringBuilder log,ILogger logger){
		log.append("DN删除"+dlvItem.getDelivery().getDeliveryCode()+"-"+dlvItem.getItemNo());
		log.append("\r\n");
		deliveryItemDao.delete(dlvItem);
		// 调用sap更新dn
		List<DeliveryItemEntity> dlvItemList=new ArrayList<DeliveryItemEntity>();
		dlvItemList.add(dlvItem);
		try {
			dnDelToSapService.execute(dlvItemList, null,logger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}
	
	/**
	 * 更新dn
	 * @param dlvItem
	 * @param qty
	 * @param log
	 * @return
	 */
	public StringBuilder updateDn(DeliveryItemEntity dlvItem,Double qty,StringBuilder log,ILogger logger){
		log.append("DN更新"+dlvItem.getDelivery().getDeliveryCode()+"-"+dlvItem.getItemNo());
		log.append(",原数量"+dlvItem.getDeliveryQty()+",取消数量"+qty);
		log.append("\r\n");
		dlvItem.setDeliveryQty(BigDecimalUtil.sub(dlvItem.getDeliveryQty(),qty));
		deliveryItemDao.save(dlvItem);
		// 调用sap更新dn
		
		List<DeliveryItemEntity> dlvItemList=new ArrayList<DeliveryItemEntity>();
		dlvItemList.add(dlvItem);
		try {
			dnUpdateSyncService.execute(dlvItemList, null,logger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}
	
	
	
	/**
	 * 释放看板的发货数量  取消发货单
	 * @param dlv
	 * @param log
	 * @return
	 */
	public StringBuilder cancelDlv(DeliveryEntity delivery,StringBuilder log){
		delivery.setAuditStatus(-1);
		deliveryDao.save(delivery);
		
		List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDeliveryAndAbolished(delivery,0);   
		for(DeliveryItemEntity item : deliveryItem) {   
			PurchaseOrderItemPlanEntity orderItemPlan = item.getOrderItemPlan();
			Double _deliveryQty = item.getDeliveryQty();	//发货单中的发货数量
			log.append("取消发货单,供货计划发货数量修改:"+orderItemPlan.getOrder().getOrderCode()+"_"+orderItemPlan.getOrderItem().getItemNo()+"-"+orderItemPlan.getItemNo());
			log.append(",原数量"+orderItemPlan.getDeliveryQty()+",取消数量"+_deliveryQty);
			log.append("\r\n");
			if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO) {
				Double delQty = BigDecimalUtil.sub(orderItemPlan.getDeliveryQty(), _deliveryQty);//发货数量=plan的发货数量-发货单中的发货数量
				orderItemPlan.setDeliveryQty(delQty<0?0D:delQty);
				
				Double toDeliveryQty = BigDecimalUtil.sub(orderItemPlan.getToDeliveryQty(), _deliveryQty);	//已创建未发数量=原来的已创建未发数量-发货单中的发货数量
				orderItemPlan.setToDeliveryQty(toDeliveryQty>0?toDeliveryQty:0D);
				
				Double undeliveryQty = BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty()));
				orderItemPlan.setUndeliveryQty(undeliveryQty>orderItemPlan.getOrderQty()?orderItemPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
				
			} 
			purchaseOrderItemPlanDao.save(orderItemPlan);
		}
		return log;
	}

	/**
	 * 回写要货计划数量 释放订单数量
	 * 当完全删除plan时，qty参数就是plan的数量
	 * 当更新plan是，qty是需要处理扣除的量
	 */
	public StringBuilder writeQtyByPlan(PurchaseOrderItemPlanEntity plan,Double qty,StringBuilder log){
		PurchaseOrderItemEntity orderItem=plan.getOrderItem();
		
		PurchaseGoodsRequestEntity request=plan.getPurchaseGoodsRequest();

		//1、释放到要货计划
		qty=orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), qty);
		double surQry =Double.valueOf(request.getSurQry());
		request.setSurQry(""+BigDecimalUtil.add(surQry, qty));
		purchaseGoodsRequestDao.save(request);
		log.append("要货单数量变化，单据为"+request.getMaterialCode()+"-"+request.getRq());
		log.append(",原剩余数量为"+surQry+",处理数量"+qty);
		log.append("\r\n");
		
		
		
		//2、更新行剩余匹配数量更新更新
		orderItem.setSurOrderQty(BigDecimalUtil.add(orderItem.getSurOrderQty(),qty));
		orderItem.setSurBaseQty(orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), orderItem.getSurOrderQty()));
		purchaseOrderItemDao.save(orderItem);
		log.append("订单明细剩余数量变化，订单为"+orderItem.getOrder().getOrderCode()+"-"+orderItem.getItemNo());
		log.append(",原剩余数量为"+orderItem.getSurOrderQty()+",处理数量"+qty);
		log.append("\r\n");
		
		//3、要货计划释放数量匹配订单
		purchaseGoodsRequestService.goodsRequestMatchPo(request.getId());
		return log;
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
	
	
	
	
}
