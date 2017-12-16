package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.BacklogCfgEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface BacklogCfgDao extends BaseRepository<BacklogCfgEntity, Serializable>,JpaSpecificationExecutor<BacklogCfgEntity>{



	
}
