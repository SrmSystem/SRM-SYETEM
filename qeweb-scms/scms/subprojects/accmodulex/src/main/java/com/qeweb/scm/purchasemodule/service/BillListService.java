package com.qeweb.scm.purchasemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.constans.AccountType;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.BillListEntity;
import com.qeweb.scm.purchasemodule.entity.BillListItemEntity;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;
import com.qeweb.scm.purchasemodule.repository.BillListDao;
import com.qeweb.scm.purchasemodule.repository.BillListItemDao;
import com.qeweb.scm.purchasemodule.repository.InvoiceDao;


@Service
@Transactional
public class BillListService {

	@Autowired
	private BillListDao billListDao;
	
	@Autowired
	private BillListItemDao billListItemDao;
	
	@Autowired
	private InvoiceDao invoiceDao;
	
	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<BillListEntity> getBillLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BillListEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BillListEntity.class);
		return billListDao.findAll(spec, pagin);
	}

	public BillListEntity getBillListById(Long id) {
		return billListDao.findOne(id);
	}

	/**
	 * 获取开票清单明细
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<BillListItemEntity> getBillListItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BillListItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BillListItemEntity.class);
		Page<BillListItemEntity> itemPage = billListItemDao.findAll(spec, pagin);
		/*List<BillListItemEntity> retList = itemPage.getContent();
		List<Long> list = new ArrayList<Long>();
		for(BillListItemEntity item: retList) {   
			list.add(item.getAccountItemId());       
		}
		List<InStorageItemEntity> InstorageItem = inStorageItemDao.findByIdIn(list);
		List<OnlineSettlementItemEntity> onlineSettlementItem = onlineSettlementItemDao.findByIdIn(list);
		Map<Long, InStorageItemEntity> InstorageItemMap = convertToMap(InstorageItem);
		Map<Long, OnlineSettlementItemEntity> onlineSettlementItemMap = convertToMap(onlineSettlementItem);
		
		for(BillListItemEntity billitem : retList) {    
			if(billitem.getBillList().getBillType() == AccountType.ACCOUNT_TYPE_IN.intValue()) {
				billitem.setInstorageItem(InstorageItemMap.get(billitem.getAccountItemId()));
			} else if(billitem.getBillList().getBillType() == AccountType.ACCOUNT_TYPE_ONLINE.intValue()) {
				billitem.setOnlineItem(onlineSettlementItemMap.get(billitem.getAccountItemId()));    
			}
		} */
		return itemPage;
	}
	
	/**
	 * 清单接收
	 * @param billId 清单ID
	 * @param receiveStatus 接收装
	 * @param userEntity
	 */
	public void receiveInvoice(long billId, Integer receiveStatus, UserEntity userEntity) {
		BillListEntity bill = getBillListById(billId);
		bill.setReceiveStatus(receiveStatus);
		bill.setReceiveUser(userEntity);
		bill.setReceiveTime(DateUtil.getCurrentTimestamp()); 
		billListDao.save(bill);
	}
	
	private <T extends BaseEntity> Map<Long, T> convertToMap(List<T> list) {
		 Map<Long, T>  map = new HashMap<Long, T>();
		 if(CollectionUtils.isEmpty(list)) {
			return map;
		}
		 
		 for(T t : list) {
			 map.put(t.getId(), t);
		 }
		return map;
	}

	/**
	 * 开票
	 * @param billEntity
	 * @param billItem
	 * @param invoiceList
	 * @param user 开票人
	 */
	public void saveBill(BillListEntity billEntity, List<BillListItemEntity> billItem, List<InvoiceEntity> invoiceList, UserEntity user) throws Exception {
		BillListEntity bill = getBillListById(billEntity.getId());
		Set<BillListItemEntity> billItemSet = bill.getBillListItem();
		Set<InvoiceEntity> invoiceSet = bill.getInvoiceItem();
		bill.setTotalPrice(billEntity.getTotalPrice());
		bill.setTax(billEntity.getTax());
		bill.setTotalTaxPrice(billEntity.getTotalTaxPrice());
		bill.setInvoiceStatus(PurchaseConstans.INVOICE_MAKE_OUT_YES);
		bill.setInvoiceTime(DateUtil.getCurrentTimestamp());
		bill.setInvoiceUser(user);
		
//		InStorageItemEntity instorageItem = null;		//入库单
		BillListItemEntity tempItem = null;
		for(BillListItemEntity item : billItemSet) {
			for(BillListItemEntity vitem : billItem) {
				if(vitem.getId() == item.getId()) {
					tempItem = vitem;
					break;
				}
			}
			//入库单结算数量处理
			if(bill.getBillType() == AccountType.ACCOUNT_TYPE_IN) {
				double d = BigDecimalUtil.sub(item.getAccountQty(), tempItem.getAccountQty());
//				instorageItem = inStorageItemDao.findOne(item.getAccountItemId());
//				instorageItem.setInAccountQty(BigDecimalUtil.sub(instorageItem.getInAccountQty(), d)); 
//				if(instorageItem.getInAccountQty() >= instorageItem.getInStorageQty())
//					instorageItem.setInvoiceStatus(PurchaseConstans.INVOICE_MAKE_OUT_YES); 
//				else
//					instorageItem.setInvoiceStatus(PurchaseConstans.INVOICE_MAKE_OUT_NO); 
//				inStorageItemDao.save(instorageItem);
			}
			item.setAccountQty(tempItem.getAccountQty());
			item.setPrice(tempItem.getPrice());
		}
		InvoiceEntity tempInvoice = null;
		for(InvoiceEntity invoice : invoiceSet) {
			for(InvoiceEntity vinvoice : invoiceList) {
				if(invoice.getId() == vinvoice.getId()) {
					tempInvoice = vinvoice;
					break;
				}
			}
			invoice.setInvoiceCode(tempInvoice.getInvoiceCode());
			invoice.setInvoiceMoney(tempInvoice.getInvoiceMoney());
		}
		
		billListDao.save(bill);
		billListItemDao.save(billItemSet);
		invoiceDao.save(invoiceSet);
	}

	public BillListEntity getBillByCode(String billCode) {
		return billListDao.getBillByCode(billCode);
	}

	public void saveErpBills(List<BillListEntity> billList) {
		billListDao.save(billList);
	}

	public void saveErpBillListItems(List<BillListItemEntity> itemList) {
		billListItemDao.save(itemList);
	}

	public BillListItemEntity getBillListItemByCode(String settlementCode, String itemCode) {
		return billListItemDao.getBillListItemByCode(settlementCode, itemCode);
	}
	
	public List<BillListItemEntity> getBillListItemByBillCode(String billCode) {
		return billListItemDao.getBillListItemByBillCode(billCode);
	}

	public List<BillListEntity> getNoSyncList() {
		return billListDao.getNoSyncList();
	}

	public void save(BillListEntity bill) {
		billListDao.save(bill);
		
	}

	public List<BillListItemEntity> getNoSyncItemList() {
		return billListItemDao.getNoSyncItemList();
	}

	public void saveBillItem(BillListItemEntity bill) {
		billListItemDao.save(bill);
		
	}

	public List<InvoiceEntity> getInvoiceListByBillCode(String billCode) {
		return invoiceDao.getInvoiceByBillCode(billCode);
	}
	
}
