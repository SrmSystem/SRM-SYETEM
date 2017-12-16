package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.SubjectEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface SubjectDao extends BaseRepository<SubjectEntity, Serializable>,JpaSpecificationExecutor<SubjectEntity>{

	public List<SubjectEntity> findByQuesId(Long id);

	@Modifying  
	@Query("update SubjectEntity set title=?1 where id=?2")
	public void updatesubject(String subjects, long parseLong);
}
