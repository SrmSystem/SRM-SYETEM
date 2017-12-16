package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 物料DAO 
 */
public interface MaterialDao extends BaseRepository<MaterialEntity, Serializable>,JpaSpecificationExecutor<MaterialEntity>{
	List<MaterialEntity> findByCode(String code);
	
	List<MaterialEntity> findByCodeAndAbolished(String code,Integer abolished);

	List<MaterialEntity> findByCodeAndIdNotIn(String code, ArrayList<Long> idList);

	List<MaterialEntity> findByMaterialTypeId(long id);
	
	MaterialEntity findByName(String name);
	
	@Query("from MaterialEntity a where upper(a.code)=?1")
	List<MaterialEntity> findByUpperCode(String code);
	
	@Query("select distinct mat  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id and rel.buyerId in ?1")
	List<MaterialEntity> findMaterialListByBuyer(List<Long> buyerId);
	
	/**
	 * 根据采购组织id和物料编码查物料
	 * @param buyerId
	 * @param code
	 * @return
	 */
	@Query("select distinct mat  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id and rel.buyerId = ?1 and mat.code = ?2 and mat.abolished=0 and rel.abolished=0")
	List<MaterialEntity> findMaterialListByBuyerAndCodeAndAbolished(Long buyerId,String code);

	
	@Query("from MaterialEntity  where code = ?1")
	MaterialEntity getMaterials(String materialCode);
	
	
	//add by hao.qin  20171020
	@Query("from MaterialEntity where id not in (?1)")
	public List<MaterialEntity> findByNotInIds(List<Long> ids); 
	//end
	
}
