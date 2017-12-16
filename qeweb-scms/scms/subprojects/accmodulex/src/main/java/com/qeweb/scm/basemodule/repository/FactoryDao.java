package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FactoryDao extends BaseRepository<FactoryEntity, Serializable>,JpaSpecificationExecutor<FactoryEntity>{

	@Override
	public List<FactoryEntity> findAll();
	
	FactoryEntity findByCode(String code);

	public List<FactoryEntity> findByAbolished(Integer abolished);

	public FactoryEntity findByNameAndAbolished(String name,Integer abolished);
	
	/**
	 * 根据编码查未作废的工厂
	 * @author chao.gu
	 * 20170510
	 * @param code
	 * @param abolished
	 * @return
	 */
	public FactoryEntity findByCodeAndAbolished(String code,Integer abolished);
	
	@Query("from FactoryEntity a where a.code=?1 and a.abolished = ?2")
	public List<FactoryEntity> findFactoryByCodeAndAbolished(String code,Integer abolished);
		
	@Modifying  
	@Query("update FactoryEntity  a  set  a.abolished = 0  where a.id=?1")
	void effect(Long id);
	
	
    
}
