package com.qeweb.scm.filemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;

/**
 * 
 * 文件反馈dao
 *
 */
public interface FileFeedbackDao extends BaseRepository<FileFeedbackEntity, Serializable>,JpaSpecificationExecutor<FileFeedbackEntity>{

	@Override
	public List<FileFeedbackEntity> findAll();
	
	 /**
     * 根据id获取文件反馈信息
     */
	public FileFeedbackEntity findById(Long id);
	
	
	 /**
     * 根据id获取文件反馈信息
     */
	@Query("from FileFeedbackEntity a where a.fileCollaboration.id = ?1 and a.vendor.id = ?2  and a.abolished=0")
	public FileFeedbackEntity findFileFeedbackByfileCollaborationIdAndVendorId(Long fileCollaborationId, Long vendorId);
}
