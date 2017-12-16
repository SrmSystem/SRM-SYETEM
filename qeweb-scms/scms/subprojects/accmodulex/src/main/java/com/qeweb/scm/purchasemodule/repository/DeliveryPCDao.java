package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.DeliveryPCEntity;


public interface DeliveryPCDao extends BaseRepository<DeliveryPCEntity, Serializable>,JpaSpecificationExecutor<DeliveryPCEntity>{

	@Query("from DeliveryPCEntity a where a.deliveryItem.id = ?1 ")
	List<DeliveryPCEntity> getByDeliveryItemId(long id);


}
