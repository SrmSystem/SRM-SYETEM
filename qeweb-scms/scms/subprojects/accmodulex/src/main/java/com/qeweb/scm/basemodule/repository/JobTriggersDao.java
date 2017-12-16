package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.qeweb.scm.basemodule.entity.JobTriggersEntity;
import com.qeweb.scm.basemodule.entity.JobTriggersEntityKey;

public interface JobTriggersDao extends PagingAndSortingRepository<JobTriggersEntity, Serializable>,JpaSpecificationExecutor<JobTriggersEntity>{

	@Override
	public Page<JobTriggersEntity> findAll(Pageable page);
	
	@Override
	public List<JobTriggersEntity> findAll();
	
	public JobTriggersEntity findByKey(JobTriggersEntityKey key);
	
}