package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.TwoAuditSolutionEntity;

public interface TwoAuditSolutionDao extends BaseRepository<TwoAuditSolutionEntity, Serializable>,JpaSpecificationExecutor<TwoAuditSolutionEntity> {

	TwoAuditSolutionEntity findByTwoauditIdAndCurrentVersion(long id, int i);

}
