package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 物料DAO 
 */
public interface MaterialTypeDao extends BaseRepository<MaterialTypeEntity, Serializable>,JpaSpecificationExecutor<MaterialTypeEntity>{
	
	@Query("select distinct mat  from BuyerMaterialTypeRelEntity relx,MaterialTypeEntity mat where relx.materialTypeId =mat.id and mat.parentId=?1 and mat.abolished=?2 and relx.buyerId in ?3")
	List<MaterialTypeEntity> findParentIdAndBuyer(Long pId,Integer abolished,List<Long> buyerIds);
	
	@Query("select distinct mat  from BuyerMaterialTypeRelEntity relx,MaterialTypeEntity mat where relx.materialTypeId =mat.id and mat.parentId is null and mat.abolished=?1 and relx.buyerId in ?2")
	List<MaterialTypeEntity> findParentIdIsNullAndBuyer(Integer abolished,List<Long> buyerIds);

	
	List<MaterialTypeEntity> findByCode(String code);

	List<MaterialTypeEntity> findByParentIdIsNull();

	List<MaterialTypeEntity> findByParentId(Long pId);
	
	List<MaterialTypeEntity> findByParentIdAndAbolished(Long pId,Integer abolished);
	
	List<MaterialTypeEntity> findByParentIdIsNullAndAbolished(Integer abolished);

	List<MaterialTypeEntity> findByLeaf(Integer leaf);

	List<MaterialTypeEntity> findByCodeAndIdNotIn(String code, ArrayList<Long> newArrayList);

	List<MaterialTypeEntity> findByLevelLayer(Integer level);

	List<MaterialTypeEntity> findById(Long id);
	
	@Query("from MaterialTypeEntity where leaf=?1 and levelLayer=?2 order by id asc")
	public List<MaterialTypeEntity> getMaterialType(Integer leaf,Integer levelLayer);

	MaterialTypeEntity findByIdAndNeedSecondVendor(long materialTypeId, int statusYes);

	MaterialTypeEntity findByIdAndNeedSecondVendorAndAbolished(long materialTypeId, int statusYes, int statusNo);
	
	List<MaterialTypeEntity> findByLevelLayerAndAbolishedAndIdNotIn(Integer level,Integer abolished,ArrayList<Long> newArrayList);

	List<MaterialTypeEntity> findByLevelLayerAndAbolishedAndIdNotInAndCodeLike(int i, int j, ArrayList<Long> newArrayList,String code);

	List<MaterialTypeEntity> findByLevelLayerAndAbolishedAndIdNotInAndNameLike(int i, int j, ArrayList<Long> newArrayList, String string);

	List<MaterialTypeEntity> findByLevelLayerAndAbolishedAndIdNotInAndCodeLikeAndNameLike(int i, int j, ArrayList<Long> newArrayList, String string,String string2);

	List<MaterialTypeEntity> findByLevelLayerAndAbolishedAndNameLike(int i,
			int j, String name);

	List<MaterialTypeEntity> findByCol2(String col2);

	List<MaterialTypeEntity> findByParentIdIsNullAndAbolishedAndCol2IsNull(
			int statusNo);

	List<MaterialTypeEntity> findByParentIdAndAbolishedAndCol2IsNull(Long pId,
			int statusNo);

	List<MaterialTypeEntity> findByParentIdIsNullAndAbolishedAndCol2(
			int statusNo, String string);

	List<MaterialTypeEntity> findByParentIdAndAbolishedAndCol2(Long pId,
			int statusNo, String string);

	MaterialTypeEntity findByNameAndLevelLayer(String matTypeName, Integer levelLayer);

	MaterialTypeEntity findByCodeAndLevelLayer(String matTypeCode, Integer levelLayer);
}
