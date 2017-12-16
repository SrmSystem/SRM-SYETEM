package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.BlankScrapEntity;

public interface BlankScrapDao extends BaseRepository<BlankScrapEntity, Serializable>,JpaSpecificationExecutor<BlankScrapEntity> {

	@Query("from BlankScrapEntity a where a.month = ?1")
	List<BlankScrapEntity> findByMonth(String time);

}
