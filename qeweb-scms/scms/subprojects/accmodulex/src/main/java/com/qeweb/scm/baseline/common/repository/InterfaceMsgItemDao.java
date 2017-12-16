package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.InterfaceMsgItemEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface InterfaceMsgItemDao extends BaseRepository<InterfaceMsgItemEntity, Serializable>,JpaSpecificationExecutor<InterfaceMsgItemEntity>{

}
