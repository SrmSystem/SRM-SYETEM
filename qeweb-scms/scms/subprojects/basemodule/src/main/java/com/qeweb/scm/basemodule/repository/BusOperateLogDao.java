package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.BusOperateLogEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface BusOperateLogDao extends BaseRepository<BusOperateLogEntity, Serializable>,JpaSpecificationExecutor<BusOperateLogEntity>{
	
}
