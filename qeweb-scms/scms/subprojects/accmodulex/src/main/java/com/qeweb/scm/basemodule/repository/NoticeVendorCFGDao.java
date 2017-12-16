package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.NoticeVendorCFGEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface NoticeVendorCFGDao extends BaseRepository<NoticeVendorCFGEntity, Serializable>,JpaSpecificationExecutor<NoticeVendorCFGEntity>{

	List<NoticeVendorCFGEntity> findByNoticeId(long id);

	NoticeVendorCFGEntity findByNoticeIdAndOrgId(long id, Long companyId);

	NoticeVendorCFGEntity findByNoticeIdAndRoleId(long id, long id2);
	
	NoticeVendorCFGEntity findByNoticeIdAndUserId(long id, Long userId);
}
