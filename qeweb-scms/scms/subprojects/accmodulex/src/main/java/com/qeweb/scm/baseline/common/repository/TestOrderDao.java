package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.TestOrderEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface TestOrderDao extends BaseRepository<TestOrderEntity, Serializable>,JpaSpecificationExecutor<TestOrderEntity>{

}
