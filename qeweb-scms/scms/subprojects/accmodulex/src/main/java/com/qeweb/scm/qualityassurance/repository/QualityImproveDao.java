package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.QualityImproveEntity;

public interface QualityImproveDao extends BaseRepository<QualityImproveEntity, Serializable>,JpaSpecificationExecutor<QualityImproveEntity> {

}
