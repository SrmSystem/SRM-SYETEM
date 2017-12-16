package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.OnlineInspectionEntity;

public interface OnlineInspectionDao extends BaseRepository<OnlineInspectionEntity, Serializable>,JpaSpecificationExecutor<OnlineInspectionEntity> {

}
