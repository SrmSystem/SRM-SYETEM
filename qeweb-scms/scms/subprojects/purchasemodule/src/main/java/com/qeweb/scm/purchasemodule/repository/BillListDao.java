package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.BillListEntity;


public interface BillListDao extends BaseRepository<BillListEntity, Serializable>,JpaSpecificationExecutor<BillListEntity>{

	@Override
	public List<BillListEntity> findAll();
	
	@Override
	public Page<BillListEntity> findAll(Pageable page);

	@Query("from BillListEntity a where a.billCode = ?1")
	public BillListEntity getBillByCode(String billCode);

	@Query("from BillListEntity a where a.syncStatus = '0' and a.billType = '0' and a.receiveStatus = '1'")
	public List<BillListEntity> getNoSyncList();

}
