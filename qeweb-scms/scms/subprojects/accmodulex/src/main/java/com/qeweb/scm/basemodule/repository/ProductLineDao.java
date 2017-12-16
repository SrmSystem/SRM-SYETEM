package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.ProductLineEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface ProductLineDao extends BaseRepository<ProductLineEntity, Serializable>,JpaSpecificationExecutor<ProductLineEntity>{

	
	

}
