package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface BussinessRangeDao extends BaseRepository<BussinessRangeEntity,Serializable>,JpaSpecificationExecutor<BussinessRangeEntity>{

	List<BussinessRangeEntity> findByBussinessTypeAndAbolished(int bussinessrangeType, int statusNo);

	List<BussinessRangeEntity> findByBussinessTypeAndAbolishedAndParentId(int bussinessrangeType, int statusNo, Long id);

	List<BussinessRangeEntity> findByAbolishedAndParentId(int statusNo, Long id);

	List<BussinessRangeEntity> findByParentIdAndBussinessType(Long id,
			Integer type);

	List<BussinessRangeEntity> findByBussinessType(Integer type);
	
	List<BussinessRangeEntity> findByCodeAndBussinessType(String code,Integer type);

	BussinessRangeEntity findByNameAndBussinessTypeAndAbolished(String name,Integer type, int statusNo);

	BussinessRangeEntity findByCodeAndBussinessTypeAndAbolished(String brandCode, int bussinessrangeType2, int statusNo);
}
