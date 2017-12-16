package com.qeweb.scm.check.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.CheckExceptionEntity;


public interface CheckExceptionDao extends BaseRepository<CheckExceptionEntity, Serializable>,JpaSpecificationExecutor<CheckExceptionEntity>{
	
	@Override
	public Page<CheckExceptionEntity> findAll(Specification<CheckExceptionEntity> spec, Pageable pageable);
}
