package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.QuestionnaireEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface QuestionnaireDao extends BaseRepository<QuestionnaireEntity, Serializable>,JpaSpecificationExecutor<QuestionnaireEntity>{

	@Modifying  
	@Query("update QuestionnaireEntity set releaseerUserName=?1,releaseTime=?2,status=?3 where id=?4")
	void updateQuestionnaire(String releaseerUserName,
			Timestamp releaseTime, int status, long id);
	@Modifying  
	@Query("update QuestionnaireEntity set endTime=?1,status=?2 where id=?3")
	void updateQuestionnaire(Timestamp endTime,
			 int status, long id);
	@Modifying  
	@Query("update QuestionnaireEntity set title=?1,updateUserId=?2,updateUserName=?3,lastUpdateTime=?4 where id=?5")
	void updateQuestionnaire(String title,Long updateUserId,String updateUserName,
			Timestamp lastUpdateTime, long id);
	
	@Query("from QuestionnaireEntity where  endTime>=?1 and status=?2 and abolished=0 order by id desc")
	List<QuestionnaireEntity> getQuestionnaireEntityAndStatus(Timestamp currentTimestamp, int i);
}
