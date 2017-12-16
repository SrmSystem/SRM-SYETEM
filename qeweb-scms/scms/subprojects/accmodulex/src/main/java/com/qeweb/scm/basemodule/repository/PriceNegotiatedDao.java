package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.PriceNegotiatedEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface PriceNegotiatedDao extends BaseRepository<PriceNegotiatedEntity, Serializable>,JpaSpecificationExecutor<PriceNegotiatedEntity>{

	PriceNegotiatedEntity findByVendorCodeAndMaterialCode(String vendorCode,String materialCode);
	
	List<PriceNegotiatedEntity> findByVendorCodeAndCurrentVersionAndMaterialCodeIn(String vendorCode, Integer version, List<String> codeList);
}
