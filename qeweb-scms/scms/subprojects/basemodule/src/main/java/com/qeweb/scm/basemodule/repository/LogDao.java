package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.LogEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface LogDao extends BaseRepository<LogEntity, Serializable>,JpaSpecificationExecutor<LogEntity>{
	
}
