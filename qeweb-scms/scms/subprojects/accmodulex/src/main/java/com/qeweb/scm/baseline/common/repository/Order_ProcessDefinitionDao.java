package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.Order_ProcessDefinitionEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface Order_ProcessDefinitionDao extends BaseRepository<Order_ProcessDefinitionEntity, Serializable>,JpaSpecificationExecutor<Order_ProcessDefinitionEntity>{

	Order_ProcessDefinitionEntity findByEntityName(String beanName);
}
