package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.NoticeEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface NoticeDao extends BaseRepository<NoticeEntity, Serializable>,JpaSpecificationExecutor<NoticeEntity>{
	
	@Query("from NoticeEntity where validStartTime<=?1 and validEndTime>=?1 order by noticeType asc,id desc")
	public List<NoticeEntity> getNoticeEntity(Timestamp time);
}
