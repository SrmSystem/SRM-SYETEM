package com.qeweb.scm.filemodule.service;


import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.filemodule.entity.FileCollaborationEntity;
import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;
/**
 * 
 * 文件协同service接口
 *
 */
public interface FileCollaborationService {
	
	/**
	 * 获取文件协同列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<FileCollaborationEntity> getFileCollaboationList(int pageNumber, int pageSize, Map<String, Object> searchParamMap);
	

	/**
	 * 获取文件协同详细列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<FileFeedbackEntity> getFeedbackList(int pageNumber, int pageSize, Map<String, Object> searchParamMap);
	

	

    /**
     * 根据标题获取对象
     * @param title
     * @return
     */
	public FileCollaborationEntity getFileCollaboationByTitle(String title);
	

	/**
	 * 新增对象
	 * @param fileCollaboration
	 */
	public void add(FileCollaborationEntity fileCollaboration);
	
	
	/**
	 * 更新对象
	 * @param fileCollaboration
	 */
	public void update(FileCollaborationEntity fileCollaboration);
	
	
    /**
     * 删除对象
     * @param fileCollaborationList
     */
	public void delete(List<FileCollaborationEntity> fileCollaborationList);
	
	/**
	 * 获取单个对象
	 * @param title
	 * @return
	 */
	public FileCollaborationEntity get(Long id);
	

    /**
     * 发布对象
     * @param fileCollaborationList
     */
	public void publish(List<FileCollaborationEntity> fileCollaborationList)throws IOException;
	
	
    /**
     * 发布对象
     * @param 
     */
	public void  fileFeedbackUpload(Long id,String fileName,String filePath,Blob fileContent);
	
	
}
