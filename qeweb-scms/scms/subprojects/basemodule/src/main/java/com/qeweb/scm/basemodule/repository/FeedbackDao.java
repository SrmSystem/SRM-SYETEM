package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FeedbackDao extends BaseRepository<FeedbackEntity, Serializable>,JpaSpecificationExecutor<FeedbackEntity>{

	@Override
	public List<FeedbackEntity> findAll();
	
}
