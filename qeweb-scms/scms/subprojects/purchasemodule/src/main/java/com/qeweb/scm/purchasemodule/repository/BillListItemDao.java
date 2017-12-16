package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.BillListItemEntity;


public interface BillListItemDao extends BaseRepository<BillListItemEntity, Serializable>,JpaSpecificationExecutor<BillListItemEntity>{

	@Override
	public List<BillListItemEntity> findAll();
	
	@Override
	public Page<BillListItemEntity> findAll(Pageable page);
	
	@Query("from BillListItemEntity a where a.billList.billCode = ?1 and a.material.code = ?2")
	public BillListItemEntity getBillListItemByCode(String settlementCode, String itemCode);
	
	@Query("from BillListItemEntity a where a.billList.billCode = ?1")
	public List<BillListItemEntity> getBillListItemByBillCode(String billCode);

	@Query("from BillListItemEntity a where syncStatus = '0' and a.billList.billType = '0' ")
	public List<BillListItemEntity> getNoSyncItemList();

}
