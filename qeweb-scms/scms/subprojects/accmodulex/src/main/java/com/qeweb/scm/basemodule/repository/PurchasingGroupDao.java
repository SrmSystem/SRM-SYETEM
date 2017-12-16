package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface PurchasingGroupDao extends BaseRepository<PurchasingGroupEntity, Serializable>,JpaSpecificationExecutor<PurchasingGroupEntity>{

	 /**
     * 根据代码和名称采购组信息
     */
	PurchasingGroupEntity findByCodeAndName(String code,String name);
	
	 /**
     * 根据代码采购组信息
     */
	PurchasingGroupEntity findByCode(String code);
	
	@Override
	public List<PurchasingGroupEntity> findAll();

	/**
	 * 根据编码和作废状态查采购组
	 * @param code
	 * @param abolished
	 * @return
	 */
	PurchasingGroupEntity findByCodeAndAbolished(String code,Integer abolished);
	
	@Query("from PurchasingGroupEntity a where a.code = ?1 and a.abolished = ?2 ")
	List<PurchasingGroupEntity> findByCodeAndAbolisheds(String code,Integer abolished);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	PurchasingGroupEntity findById(Long id);
	
	
	@Modifying  
	@Query("update PurchasingGroupEntity  a  set  a.abolished = 0  where a.id=?1")
	void effect(Long id);
	
	@Query("from PurchasingGroupEntity a where a.abolished = ?2 ")
	List<PurchasingGroupEntity> findByAbolished(Integer abolished);

	
	@Query("from PurchasingGroupEntity a where a.code = ?1")
	PurchasingGroupEntity getId(String vendorCode);

}
