package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.qeweb.scm.basemodule.entity.JobCronEntity;
import com.qeweb.scm.basemodule.entity.JobTriggersEntityKey;

public interface JobCronDao extends PagingAndSortingRepository<JobCronEntity, Serializable>,JpaSpecificationExecutor<JobCronEntity>{

	@Override
	public Page<JobCronEntity> findAll(Pageable page);
	
	@Override
	public List<JobCronEntity> findAll();
	
	public JobCronEntity findByKey(JobTriggersEntityKey key);
	
}