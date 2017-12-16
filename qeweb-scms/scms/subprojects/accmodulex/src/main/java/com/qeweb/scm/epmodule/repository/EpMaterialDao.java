package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;

/**
 * 询价物料DAO
 * @author ronnie
 *
 */
public interface EpMaterialDao extends BaseRepository<EpMaterialEntity, Serializable>,JpaSpecificationExecutor<EpMaterialEntity>{
	
	@Query("from EpMaterialEntity a where a.price.id = ?1")
	List<EpMaterialEntity> findByPriceId(Long epPriceId);
	
	@Query("from EpMaterialEntity a where a.price.id = ?1 and a.material.id = ?2")
	EpMaterialEntity findByPriceIdAndMaterialId(Long epPriceId,Long materialId);
	
	//@Query("select a.id from EpMaterialEntity a right join EpWholeQuoEntity b on a.id = b.epMaterial.id where b.epPrice.id = ?1 and b.epVendor.id = ?2")
	//List<Long> findByWholeQuo(Long epPriceId, Long epVendorId);
	
	@Query(value="select a.* from QEWEB_EP_MATERIAL a RIGHT JOIN QEWEB_EP_WHOLE_QUO b on a.id = b.ENQUIRE_PRICE_MATERIAL_ID where b.ENQUIRE_PRICE_ID=?1 and b.ENQUIRE_PRICE_VENDOR_ID=?2",nativeQuery=true)
	List<EpMaterialEntity> findByWholeQuo(Long epPriceId, Long epVendorId);
}
