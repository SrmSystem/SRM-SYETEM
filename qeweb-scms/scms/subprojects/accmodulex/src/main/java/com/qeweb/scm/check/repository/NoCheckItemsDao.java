package com.qeweb.scm.check.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;


public interface NoCheckItemsDao extends BaseRepository<NoCheckItemsEntity, Serializable>,JpaSpecificationExecutor<NoCheckItemsEntity>{
	
	@Modifying
	@Query("update NoCheckItemsEntity set type=1 where id = ?1")
	void updateCheckOpt(Long id);

	@Query("from NoCheckItemsEntity where id = ?1")
	NoCheckItemsEntity getReceiveByItemId(long id);

	
	@Query("from NoCheckItemsEntity a where a.createTime >= ?1 and a.createTime < ?2 and a.buyerId = ?3 and a.vendorId=?4 and a.state=0")
	List<NoCheckItemsEntity> findItems(Timestamp t0, Timestamp t1,
			Long buyerId, Long vendorId);

}
