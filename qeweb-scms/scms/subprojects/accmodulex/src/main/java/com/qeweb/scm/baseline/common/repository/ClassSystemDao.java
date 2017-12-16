package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.baseline.common.entity.ClassSystemEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface ClassSystemDao extends BaseRepository<ClassSystemEntity, Serializable>,JpaSpecificationExecutor<ClassSystemEntity>{
	
	@Override
	public List<ClassSystemEntity> findAll();
	
	
	@Query("from ClassSystemEntity where abolished = 0 ")
	List<ClassSystemEntity> findEffective();
}
