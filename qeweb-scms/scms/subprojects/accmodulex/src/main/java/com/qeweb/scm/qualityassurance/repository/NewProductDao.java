package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.NewProductEntity;

public interface NewProductDao extends BaseRepository<NewProductEntity, Serializable>,JpaSpecificationExecutor<NewProductEntity> {
	@Query("from NewProductEntity a where a.qmTime = ?1 and a.vendor.code = ?2 and a.vendor.name = ?3 and a.material.name = ?4 and a.dataStatus = ?5")
	public List<NewProductEntity> findNewProductEntityList(Timestamp qmTime,String code,String vendorName,String matName,int dataStatus);
	
//	@Query("from NewProductEntity a where a.vendor.code = ?1 and a.material.name = ?2)")
	@Query("from NewProductEntity a where a.vendor.code = ?1 and a.matName = ?2)")
	public NewProductEntity findNewproductEntityByCode(String vendorCode,String  matname);
	
	@Query("select count(id) as ppap from NewProductEntity a where a.deliverTimes = ?1 and a.qualified = ?2 and a.vendor.id=?3)")
	public int findPpap(int deliverTimes,int qualified,long id);
	
	@Query("select count(id) as ppap from NewProductEntity a where a.deliverTimes = ?1 and a.vendor.id=?2)")
	public int findPpap(int deliverTimes,long vendorId);

	@Query("from NewProductEntity a where a.month = ?1")
	public List<NewProductEntity> findByMonth(String time);
	
}
