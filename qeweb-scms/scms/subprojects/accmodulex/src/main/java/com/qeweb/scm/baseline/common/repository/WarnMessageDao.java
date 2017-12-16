package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.baseline.common.entity.WarnMessageEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface WarnMessageDao extends BaseRepository<WarnMessageEntity, Serializable>,JpaSpecificationExecutor<WarnMessageEntity>{
	
	@Query("from WarnMessageEntity where abolished = ?1")
	List<WarnMessageEntity> findByAbolish(Integer abolished);
	
	
	@Query("from WarnMessageEntity where billId = ?1 and warnMainId = ?2 ")
	List<WarnMessageEntity> findByBillAndMainId(Long billId,Long warnMainId);
	
	@Query("from WarnMessageEntity where warnMainId = ?1 ")
	WarnMessageEntity findByMainId(Long warnMainId);
	
	//ADD BY HAO.QIN 
	//通过主信息的id+billId + billType 查询信息
	@Query("from WarnMessageEntity where warnMainId = ?1  and billId = ?2 and billType = ?3 and  userId = ?4 ")
	WarnMessageEntity findByMainIdAndBillIdAndBillTypeAndUserId(Long warnMainId,Long billId,String billType,Long userId);
	
	 
	//获取显示的预警和晋级预警
	@Query("from WarnMessageEntity a where a.userId = ?1 and  a.warnTime <=  ?2  and a.abolished = 0 and a.billType !='INFO' ")
	List<WarnMessageEntity> getViewWarnList(Long userId,Timestamp nowTime);
	
	
	
	//ADD BY HAO.QIN 
	//通过主信息的id+billId +userid 查询信息
	@Query("from WarnMessageEntity where warnMainId = ?1  and billId = ?2  and  userId = ?3 ")
	List<WarnMessageEntity> findByMainIdAndBillIdAndUserId(Long warnMainId,Long billId,Long userId);
	
	
	
	
	@Query("from WarnMessageEntity where billType != 'INFO'  and  abolished = 0 and isOutTime != 1")
	List<WarnMessageEntity> findWarnForEmail();
	
	
	
	
}
