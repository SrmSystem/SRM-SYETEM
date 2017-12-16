package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.AnswerEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface AnswerDao extends BaseRepository<AnswerEntity, Serializable>,JpaSpecificationExecutor<AnswerEntity>{

	List<AnswerEntity> findBySubjectId(long id);

	@Modifying  
	@Query("update AnswerEntity set title=?1 where id=?2")
	void updateanswer(String answer, long parseLong);
	
}
