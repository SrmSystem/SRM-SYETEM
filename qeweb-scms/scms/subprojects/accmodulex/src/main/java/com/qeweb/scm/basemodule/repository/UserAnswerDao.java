package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.UserAnswerEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface UserAnswerDao extends BaseRepository<UserAnswerEntity, Serializable>,JpaSpecificationExecutor<UserAnswerEntity>{

	List<UserAnswerEntity> findByQuestionnaireIdAndCreateUserId(long id, long id2);

	List<UserAnswerEntity> findByAnswerIdAndQuestionnaireId(long id, Long id2);
	
}
