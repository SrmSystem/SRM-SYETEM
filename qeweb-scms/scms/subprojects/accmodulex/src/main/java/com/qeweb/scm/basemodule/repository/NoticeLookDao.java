package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.NoticeLookEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface NoticeLookDao extends BaseRepository<NoticeLookEntity, Serializable>,JpaSpecificationExecutor<NoticeLookEntity>{
	
//	@Query(value="SELECT COUNT(*) FROM QEWEB_NOTICE_LOOK WHERE NOTICE_ID=?1",nativeQuery=true)
//	public int getNoticeLooks(long noticeId);
	
	public List<NoticeLookEntity> findByNoticeId(long noticeId);
}
