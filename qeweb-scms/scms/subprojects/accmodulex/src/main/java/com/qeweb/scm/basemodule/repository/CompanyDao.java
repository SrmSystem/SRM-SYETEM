package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface CompanyDao extends BaseRepository<CompanyEntity, Serializable>,JpaSpecificationExecutor<CompanyEntity>{

	 /**
     * 根据公司代码公司信息
     */
	CompanyEntity findByCode(String code);
	
	@Override
	public List<CompanyEntity> findAll();
	
	public List<CompanyEntity> findByAbolished(Integer abolished);

	/**
	 * 根据公司代码和作废状态查公司
	 * @author chao.gu 
	 * 20170510
	 * @param code
	 * @param abolished
	 * @return
	 */
	CompanyEntity findByCodeAndAbolished(String code,Integer abolished);
}
