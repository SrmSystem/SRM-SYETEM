package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;

public interface PPMEntityDao extends BaseRepository<PPMEntity, Serializable>,JpaSpecificationExecutor<PPMEntity> {

	@Query("from PPMEntity a where a.month = ?1 and a.vendor.code= ?2 and a.ppmType = ?3")
	PPMEntity getByMonthAndVendorAndType(String time, String code, int type);
	
	@Query("from PPMEntity a where a.month = ?1 and a.vendor.code= ?2 and a.ppmType = ?3 and a.material.code = ?4")
	PPMEntity getByMonthAndVendorAndTypeAndMaterial(String time, String code, int type, String materialCode);

}
