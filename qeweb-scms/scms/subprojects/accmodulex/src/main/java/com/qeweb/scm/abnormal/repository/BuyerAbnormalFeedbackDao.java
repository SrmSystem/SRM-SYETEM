package com.qeweb.scm.abnormal.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.abnormal.entity.AbnormalFeedbackEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface BuyerAbnormalFeedbackDao extends BaseRepository<AbnormalFeedbackEntity, Serializable>,JpaSpecificationExecutor<AbnormalFeedbackEntity>{

	
	@Override
	public List<AbnormalFeedbackEntity> findAll();
	
	//新增时通过ID查询数据
	AbnormalFeedbackEntity findById(long id); 
	
	@Modifying  
	@Query("update AbnormalFeedbackEntity  a  set  a.publishStatus = 3  where a.id=?1")
	void closeAbnormalFeedback(Long id);
}
