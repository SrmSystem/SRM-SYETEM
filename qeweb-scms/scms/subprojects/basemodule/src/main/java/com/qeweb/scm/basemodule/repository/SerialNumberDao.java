package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.SerialNumberEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface SerialNumberDao extends BaseRepository<SerialNumberEntity, Serializable>,JpaSpecificationExecutor<SerialNumberEntity>{

	public SerialNumberEntity getSerialNumberEntityByKey(String key);
	
	@Override
	public List<SerialNumberEntity> findAll(); 
}
