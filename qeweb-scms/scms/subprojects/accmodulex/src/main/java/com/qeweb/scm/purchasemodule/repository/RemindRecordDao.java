package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.RemindRecordEntity;

/**
 * 提醒记录DAO
 * @author zhangjiejun
 */
public interface RemindRecordDao extends BaseRepository<RemindRecordEntity, Serializable>, JpaSpecificationExecutor<RemindRecordEntity>{

	@Modifying
	@Query(value="delete from RemindRecordEntity where createTime >= ?1 and createTime <= ?2")
	void deleteByTimes(Timestamp startTime, Timestamp endTime);
	
}
