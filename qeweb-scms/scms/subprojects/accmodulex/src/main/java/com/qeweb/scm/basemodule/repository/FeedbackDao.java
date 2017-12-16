package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FeedbackDao extends BaseRepository<FeedbackEntity, Serializable>,JpaSpecificationExecutor<FeedbackEntity>{

	@Override
	public List<FeedbackEntity> findAll();
	
	@Query("select count(1) from FeedbackEntity a where a.billId= ?1 ")
	public Integer findFeedBackCountByOrderItemId(Long id);
}
