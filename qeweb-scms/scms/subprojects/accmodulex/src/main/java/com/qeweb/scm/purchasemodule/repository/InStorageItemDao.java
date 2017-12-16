package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;


public interface InStorageItemDao extends BaseRepository<InStorageItemEntity, Serializable>,JpaSpecificationExecutor<InStorageItemEntity>{

	@Override
	public List<InStorageItemEntity> findAll();
	
	@Override
	public Page<InStorageItemEntity> findAll(Pageable page);

	public List<InStorageItemEntity> findByIdIn(List<Long> ids);
	
	public InStorageItemEntity findById(Long id);

	public InStorageItemEntity findByInStorageIdAndMaterialId(Long inStaId,
			Long matId);
	
}
