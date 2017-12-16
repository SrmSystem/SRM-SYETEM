package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.InterfaceMsgEntity;
import com.qeweb.scm.baseline.common.entity.InterfaceMsgLogEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface InterfaceMsgLogDao extends BaseRepository<InterfaceMsgLogEntity, Serializable>,JpaSpecificationExecutor<InterfaceMsgLogEntity>{

}
