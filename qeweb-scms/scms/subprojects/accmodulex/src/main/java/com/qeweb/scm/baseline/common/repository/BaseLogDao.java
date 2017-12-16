package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.BaseFileEntity;
import com.qeweb.scm.baseline.common.entity.BaseLogEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface BaseLogDao extends BaseRepository<BaseLogEntity, Serializable>,JpaSpecificationExecutor<BaseLogEntity>{
	
}
