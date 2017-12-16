package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.CommentEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface CommentDao extends BaseRepository<CommentEntity, Serializable>,JpaSpecificationExecutor<CommentEntity>{
	
	public List<CommentEntity> findByNoticeIdOrderByIdDesc(Long id);

	public List<CommentEntity> findByNoticeId(long id);

}
