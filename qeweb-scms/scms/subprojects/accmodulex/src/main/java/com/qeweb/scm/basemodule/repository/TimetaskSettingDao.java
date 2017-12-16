package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.TimetaskSettingEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface TimetaskSettingDao extends BaseRepository<TimetaskSettingEntity, Serializable>,JpaSpecificationExecutor<TimetaskSettingEntity>{
	
	public List<TimetaskSettingEntity> findByTaskNameAndVendorIdAndAbolished(String taskName,Long vendorId,int abolished);

	@Modifying  
	@Query("update TimetaskSettingEntity set abolished=?1,lastUpdateTime=?2 where id=?3")
	void updateTimetaskSettingabolished(Integer abolished,Timestamp lastUpdateTime, long id);
	
	public List<TimetaskSettingEntity> findByVendorId(Long vendorId);
	
	
	public TimetaskSettingEntity findByTaskNameAndAbolishedAndIdAndDay(String taskName,Integer abolished,Long id,String day);
	
	/**
	 * 获取不是今天对账的特殊供应商ID
	 * @param taskName
	 * @param day
	 * @param abolished
	 * @return
	 */
	@Query("select a.vendorId from TimetaskSettingEntity a where a.taskName=?1 and a.day!=?2 and abolished=?3 and a.id>0")
	public List<Long> findNotCurrCheckSpecialVendors(String taskName,String day,Integer abolished);
	
	
	/**
	 * 获取今天对账的特殊供应商ID
	 * @param taskName
	 * @param day
	 * @param abolished
	 * @return
	 */
	@Query("select a.vendorId from TimetaskSettingEntity a where a.taskName=?1 and a.day=?2 and abolished=?3 and a.id>0")
	public List<Long> findCurrCheckSpecialVendors(String taskName,String day,Integer abolished);
}