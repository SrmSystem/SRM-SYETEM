package com.qeweb.scm.check.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.NoCheckItemEntity;


public interface NoCheckItemDao extends BaseRepository<NoCheckItemEntity, Serializable>,JpaSpecificationExecutor<NoCheckItemEntity>{

}
