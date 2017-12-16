package com.qeweb.scm.filemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.filemodule.entity.FileCollaborationEntity;

/**
 * 
 * 文件协同dao
 *
 */
public interface FileCollaborationDao extends BaseRepository<FileCollaborationEntity, Serializable>,JpaSpecificationExecutor<FileCollaborationEntity>{
	
	@Override
	public List<FileCollaborationEntity> findAll();
	
	
	 /**
     * 根据id获取文件协同信息
     */
	public FileCollaborationEntity findById(Long id);
	
	 /**
     * 根据title获取文件协同信息
     */
	public FileCollaborationEntity findBytitle(String title);
	
	@Modifying  
	@Query("update FileCollaborationEntity  a  set a.publishTime=?2 , a.publishUser.id =?3 , a.publishStatus = 1  where a.id=?1")
	void publish(Long id,Timestamp time ,Long userId);
	
	
	@Modifying  
	@Query("update FileCollaborationEntity  a  set  a.publishStatus = 3  where a.id=?1")
	void closeFileCollaboration(Long id);
	

}
