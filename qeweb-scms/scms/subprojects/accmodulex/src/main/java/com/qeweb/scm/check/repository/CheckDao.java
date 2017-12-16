package com.qeweb.scm.check.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.CheckEntity;
import com.qeweb.scm.check.entity.CheckInvoiceEntity;
import com.qeweb.scm.check.entity.CheckItemEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;


public interface CheckDao extends BaseRepository<CheckEntity, Serializable>,JpaSpecificationExecutor<CheckEntity>{

	@Query("from CheckItemEntity a where a.check.id = ?1")
	List<CheckItemEntity> findItemsByCheckId(Long id);
	
	@Query("select count(1) from CheckItemEntity a where a.check.id = ?1 and a.exStatus=1 and a.exDealStatus=0")
	Integer findNoDealItemsByCheckId(Long id);
	
	@Query("select count(1) from CheckItemEntity a where a.check.id = ?1 and a.exDealStatus=1 and a.exConfirmStatus=0")
	Integer findNoConfirmItemsByCheckId(Long id);
	
	@Query("from CheckInvoiceEntity a where a.check.id = ?1")
	List<CheckInvoiceEntity> getInvoicesByCheckId(Long id);
	
	@Query("from CheckItemEntity a where a.recItem.id = ?1")
	List<CheckItemEntity> findCheckItemByRecItemId(Long recItemid);
	
	@Query("from CheckItemEntity a where a.recItem.receive.id = ?1")
	List<CheckItemEntity> findCheckItemByRecId(Long recId);
	
	@Query("from CheckItemEntity a where a.id = ?1")
	List<CheckItemEntity> findCheckItemById(Long id);
	
	
	@Query("from CheckItemEntity a where a.invoice.id = ?1")
	List<CheckItemEntity> getItemsByInvoiceId(Long invoiceId);

	//add by zhangjiejun 2015.09.16 start
	/**
	 * 根据code查询对账单对象
	 * @param 	code	对账单code
	 * @return	对账单对象
	 */
	CheckEntity findByCode(String code);
	
	/**
	 * 根据voucher查询对账单对象
	 * @param 	voucher	对账单voucher
	 * @return	对账单对象
	 */
	CheckEntity findByVoucher(int voucher);

	//add by zhangjiejun 2015.09.16 end
	
	//add by zhangjiejun 2015.10.19 start
	/**
	 * 根据QADVoucher和batch查询对账单对象
	 * @param 	QADVoucher	QAD的凭证号
	 * @param 	batch		QAD的batch
	 * @return	对账单对象
	 */
	CheckEntity findByQadVoucherAndBatch(int QADVoucher, String batch);
	//add by zhangjiejun 2015.10.19 end
	
	@Query("from CheckItemEntity a where a.invoice.code = ?1")
	List<CheckItemEntity> getItemsByInvoiceCode(String invoiceCode);

	List<CheckEntity> findCheckEntityByType(int type);
	
	/**
	 * 获取账期内对账单
	 * @param vendorId
	 * @param type
	 * @param checkTime
	 * @return
	 */
	@Query("from CheckEntity where vendor.id = ?1 and col3 = ?2 and  abolished = 0 and buyer.id=?3")
	List<CheckEntity> findCheckEntity(Long vendorId, String checkTime,Long buyerId);
	
	
	public CheckEntity findById(Long id);
}
