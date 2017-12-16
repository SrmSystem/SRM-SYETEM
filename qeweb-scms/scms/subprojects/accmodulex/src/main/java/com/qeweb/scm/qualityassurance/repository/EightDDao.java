package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.EightDRectification;


public interface EightDDao extends BaseRepository<EightDRectification, Serializable>,JpaSpecificationExecutor<EightDRectification>{

	@Override
	public List<EightDRectification> findAll();
	
	@Override
	public Page<EightDRectification> findAll(Pageable page);

}
