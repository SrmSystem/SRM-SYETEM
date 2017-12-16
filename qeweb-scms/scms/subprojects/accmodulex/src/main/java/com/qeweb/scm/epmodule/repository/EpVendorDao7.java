package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;

/**
 * 询价供应商实体类DAO
 * @author ronnie
 *
 */
public interface EpVendorDao7 extends BaseRepository<EpVendorEntity, Serializable>,JpaSpecificationExecutor<EpVendorEntity>{

	@Query("from EpVendorEntity a where a.epPrice.id = ?1 and a.vendorId = ?2")
	EpVendorEntity findByEpPriceIdAndEpVendorId(Long epPriceId,Long epVendorId);
	
	@Query("from EpVendorEntity a where a.epPrice.id = ?1")
	List<EpVendorEntity> findByEpPrice(Long epPriceId);
	
}
