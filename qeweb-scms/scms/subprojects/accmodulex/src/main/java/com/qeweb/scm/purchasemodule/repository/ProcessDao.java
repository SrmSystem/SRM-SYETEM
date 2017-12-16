package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.ProcessEntity;


public interface ProcessDao extends BaseRepository<ProcessEntity, Serializable>,JpaSpecificationExecutor<ProcessEntity>{

	ProcessEntity findByCodeAndAbolished(String code,Integer abolished);
}
