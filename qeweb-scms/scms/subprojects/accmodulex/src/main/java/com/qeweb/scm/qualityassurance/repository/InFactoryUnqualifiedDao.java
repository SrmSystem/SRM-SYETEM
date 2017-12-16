package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.InFactoryUnqualifiedEntity;

public interface InFactoryUnqualifiedDao extends BaseRepository<InFactoryUnqualifiedEntity, Serializable>,JpaSpecificationExecutor<InFactoryUnqualifiedEntity> {

	@Query("from InFactoryUnqualifiedEntity a where a.month = ?1")
	List<InFactoryUnqualifiedEntity> findByMonth(String time);

}
