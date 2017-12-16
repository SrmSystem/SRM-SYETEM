package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.qeweb.scm.basemodule.entity.JobDetailEntity;
import com.qeweb.scm.basemodule.entity.JobDetailEntityKey;

public interface JobDetailDao extends PagingAndSortingRepository<JobDetailEntity, Serializable>,JpaSpecificationExecutor<JobDetailEntity>{

	@Override
	public Page<JobDetailEntity> findAll(Pageable page);
	
	@Override
	public List<JobDetailEntity> findAll();
	
	public JobDetailEntity findByKey(JobDetailEntityKey key);
}
