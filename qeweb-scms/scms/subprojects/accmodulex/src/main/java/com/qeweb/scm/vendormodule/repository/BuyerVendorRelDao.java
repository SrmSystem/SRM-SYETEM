package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorBUEntity;


public interface BuyerVendorRelDao extends BaseRepository<BuyerVendorRelEntity, Serializable>,JpaSpecificationExecutor<BuyerVendorRelEntity>{

	List<BuyerVendorRelEntity> findByBuyerId(long buyerId);
	
	List<BuyerVendorRelEntity> findByVendorId(long vendorId);
	
	BuyerVendorRelEntity findByBuyerIdAndVendorId(long buyerId,long vendorId);
	
	
	List<BuyerVendorRelEntity> findByBuyerIdIn(List<Long> buyerIds);
	
	@Query("select t.buyerId from BuyerVendorRelEntity t where t.vendorId=?")
	List<Long> findBuyerIdsByVendorId(long vendorId);

}
