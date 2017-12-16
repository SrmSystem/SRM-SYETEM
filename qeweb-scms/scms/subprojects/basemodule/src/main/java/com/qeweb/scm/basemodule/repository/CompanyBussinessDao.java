package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.CompanyBussinessEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface CompanyBussinessDao extends BaseRepository<CompanyBussinessEntity, Serializable>,JpaSpecificationExecutor<CompanyBussinessEntity>{

	
	

}
