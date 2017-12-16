package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.BaseFileEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface BaseFileDao extends BaseRepository<BaseFileEntity, Serializable>,JpaSpecificationExecutor<BaseFileEntity>{
	
	 BaseFileEntity findByBillIdAndBillType(Long billId,String billType);
}
