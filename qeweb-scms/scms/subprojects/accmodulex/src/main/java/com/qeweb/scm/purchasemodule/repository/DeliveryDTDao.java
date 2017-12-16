package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.DeliveryDTEntity;


public interface DeliveryDTDao extends BaseRepository<DeliveryDTEntity, Serializable>,JpaSpecificationExecutor<DeliveryDTEntity>{

	@Query("from DeliveryDTEntity a where a.pcEntity.deliveryItem.id = ?1 ")
	List<DeliveryDTEntity> getByDeliveryItemId(long id);


}
