package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.TwoAuditEntity;


public interface TwoAuditFileDao extends BaseRepository<TwoAuditEntity, Serializable>,JpaSpecificationExecutor<TwoAuditEntity>{
}



