package com.qeweb.scm.purchasemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.constans.AccountType;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.BillListEntity;
import com.qeweb.scm.purchasemodule.entity.BillListItemEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;
import com.qeweb.scm.purchasemodule.repository.BillListDao;
import com.qeweb.scm.purchasemodule.repository.BillListItemDao;
import com.qeweb.scm.purchasemodule.repository.InStorageDao;
import com.qeweb.scm.purchasemodule.repository.InStorageItemDao;
import com.qeweb.scm.purchasemodule.repository.InvoiceDao;

/**
 *弃用
 */
@Service
@Transactional
public class InStorageService {

	@Autowired
	private InStorageDao inStorageDao;
	
	@Autowired
	private InStorageItemDao inStorageItemDao;
	
	@Autowired
	private BillListDao billListDao;
	
	@Autowired
	private BillListItemDao billListItemDao;
	
	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 获取入库单主表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<InStorageEntity> getInStorages(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InStorageEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InStorageEntity.class);
		return inStorageDao.findAll(spec, pagin);
	}
	
	public  Page<InStorageItemEntity> getInStorageItems(boolean boo, int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return null;
		}
		if(boo)
		{
			searchParamMap.put("EQ_inStorage.vendor.code", user.orgCode);
		}
		else
		{
			searchParamMap.put("EQ_inStorage.buyer.code", user.orgCode);
		}
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InStorageItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InStorageItemEntity.class);
		return inStorageItemDao.findAll(spec, pagin);
	}
	
	/**
	 * 获取入库主信息
	 * @param id
	 * @return
	 */
	public InStorageEntity getInStorageById(Long id) {
		return inStorageDao.findOne(id);
	}

	/**
	 * 根据ID获取入库明细
	 * @param ids
	 * @return
	 */
	public List<InStorageItemEntity> getInstorageItmes(List<Long> ids) {
		List<InStorageItemEntity> list = inStorageItemDao.findByIdIn(ids); 
		for(InStorageItemEntity item : list) {
			if(item.getInAccountQty()!=null&&item.getInStorageQty()!=null)
			{
				item.setAccountQty(BigDecimalUtil.sub(item.getInStorageQty(), item.getInAccountQty()));
			}
		}
		return list; 
	}

	/**
	 * 保存应开票清单
	 * @param billEntity
	 * @param billItem
	 * @param invoiceList
	 */
	public void saveBill(BillListEntity billEntity, List<BillListItemEntity> billItem, List<InvoiceEntity> invoiceList) {
		InStorageItemEntity instorageItem = null;
		billEntity.setBillType(AccountType.ACCOUNT_TYPE_IN);
		billEntity.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);  
		int index = 0;
		for(BillListItemEntity billitem : billItem) {
			instorageItem = inStorageItemDao.findOne(billitem.getAccountItemId());
			if(index == 0) {
				billEntity.setBuyer(instorageItem.getInStorage().getBuyer());
				billEntity.setVendor(instorageItem.getInStorage().getVendor());
				billEntity.setPoNumber(instorageItem.getInStorage().getPoNumber());
			}
			billitem.setMaterial(instorageItem.getMaterial());
			billitem.setLineNumber(instorageItem.getItemNo());
			Double inAccountQty = instorageItem.getInAccountQty();
			instorageItem.setInAccountQty(BigDecimalUtil.add(inAccountQty==null?0:inAccountQty, billitem.getAccountQty())); 
			if(instorageItem.getInAccountQty() >= instorageItem.getInStorageQty())
				instorageItem.setInvoiceStatus(PurchaseConstans.INVOICE_MAKE_OUT_YES); 
			inStorageItemDao.save(instorageItem);
			index ++;
		}
		billListDao.save(billEntity);
		billListItemDao.save(billItem);
		invoiceDao.save(invoiceList);
	}

	/**
	 * 初始化清单主信息
	 * @param billIdList
	 * @param bill
	 */
	public void initBillInfo(List<Long> billIdList, BillListEntity bill) {
		List<InStorageItemEntity> list = getInstorageItmes(billIdList);
		
		Double totalPrice = 0d;//合计
		String poNumber = "";
		for(InStorageItemEntity item: list) {
			totalPrice = BigDecimalUtil.add(totalPrice, BigDecimalUtil.mul(item.getAccountQty() == null ? 0d : item.getAccountQty(), item.getPrice() == null ? 0d : item.getPrice()));
			poNumber = item.getInStorage().getPoNumber();
		}
		bill.setTotalPrice(totalPrice);
		bill.setPoNumber(poNumber);
		bill.setTax(0.17);
		bill.setTotalTaxPrice(BigDecimalUtil.mul(bill.getTax() + 1, bill.getTotalPrice()));   
	}

	
	/**
	 * 从erp同步入库单数据到srm
	 * @param entityList
	 */
	public void saveInstorageFromErp(List<InStorageItemEntity> entityList) {
		for (InStorageItemEntity itemEntity : entityList) {
			InStorageEntity inStorageEntity = itemEntity.getInStorage();
			inStorageDao.save(inStorageEntity);
			itemEntity.setInStorage(inStorageEntity);
			inStorageItemDao.save(itemEntity);
		}
	}
	
	public InStorageEntity getEntityByCode(String inStorageCode){
		return inStorageDao.findByinStorageCode(inStorageCode);
	}
	
	public InStorageEntity getEntityByCodeAndOrgId(String inStorageCode,String orgId){
		return inStorageDao.findByinStorageCodeAndOrgID(inStorageCode,orgId);
	}
	
	public InStorageItemEntity getEntityByInStorageIdAndMaterialId(Long inStaId,Long matId){
		return inStorageItemDao.findByInStorageIdAndMaterialId(inStaId,matId);
	}

	public InStorageItemEntity getInStorageItemById(long instorageId) {
		return inStorageItemDao.findById(instorageId);
	}

}
