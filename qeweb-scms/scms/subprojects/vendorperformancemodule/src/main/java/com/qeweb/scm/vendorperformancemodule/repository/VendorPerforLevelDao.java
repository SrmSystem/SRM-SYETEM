package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforLevelEntity;

/**
 * 等级设置Dao
 * @author sxl
 * @date 2015年8月10日
 */
public interface VendorPerforLevelDao extends BaseRepository<VendorPerforLevelEntity, Serializable>,JpaSpecificationExecutor<VendorPerforLevelEntity>{

	VendorPerforLevelEntity findByUpperLimitAndAbolished(Integer upperLimit,Integer abolished);
	
	VendorPerforLevelEntity findByIdAndAbolished(long id,Integer abolished);
	
	VendorPerforLevelEntity findByFatherIdAndAbolished(long fatherId,Integer abolished);
	
	VendorPerforLevelEntity findByLowerLimitAndAbolished(Integer lowerLimit,Integer abolished);
	
	@Modifying  
	@Query("update VendorPerforLevelEntity set code=?1,levelName=?2,upperLimit=?3,quadrant=?4,remarks=?5,lastUpdateTime=?6 where id=?7")
	void updateLevel(String code,String levelName,int upperLimit,int quadrant,String remarks,
			Timestamp lastUpdateTime, long id);
	@Modifying  
	@Query("update VendorPerforLevelEntity set lowerLimit=?1,lastUpdateTime=?2 where id=?3")
	void updateLevel(Integer lowerLimit,Timestamp lastUpdateTime, long id);
	@Modifying  
	@Query("update VendorPerforLevelEntity set abolished=?1,lastUpdateTime=?2 where id=?3")
	void updateLevelabolished(Integer abolished,Timestamp lastUpdateTime, long id);
	
	List<VendorPerforLevelEntity> findByAbolishedOrderByIdAsc(Integer abolished);
	
	@Query(value="SELECT max(UPPER_LIMIT) FROM QEWEB_ASSESS_LEVEL WHERE ABOLISHED=?1",nativeQuery=true)
	Integer getLevelEntityMax(Integer abolished);

	VendorPerforLevelEntity findByLowerLimitLessThanAndUpperLimitGreaterThanEqual(int totalScoreV, int totalScoreV2);

	@Modifying  
	@Query("update VendorPerforLevelEntity set levelName=?1,upperLimit=?2,quadrant=?3,remarks=?4,lastUpdateTime=?5 where id=?6")
	void updateLevel2(String levelName, Integer upperLimit,Integer quadrant, String remarks, Timestamp currentTimestamp, long id);

}
