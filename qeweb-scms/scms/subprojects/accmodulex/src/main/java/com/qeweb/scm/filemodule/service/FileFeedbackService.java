package com.qeweb.scm.filemodule.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;
/**
 * 
 * 文件反馈service接口
 *
 */
public interface FileFeedbackService {
	
	/**
	 * 新增对象
	 * @param fileFeedback
	 */
	public void add(FileFeedbackEntity fileFeedback);
	
	/**
	 * 获取单个对象
	 * @param fileFeedback
	 */
	public FileFeedbackEntity get(Long id);
	
	
	/**
	 * 新增对象List
	 * @param fileFeedbackList
	 */
	public void addList(List<FileFeedbackEntity> fileFeedbackList);
	
	
	/**
	 *通过fileCollaborationId and 供应商id
	 * @param fileFeedbackList
	 */
	public FileFeedbackEntity findFileFeedbackByfileCollaborationIdAndVendorId(Long fileCollaborationId,Long vendorId);
	
	
	
	/**
	 * 获取文件协同详细列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<FileFeedbackEntity> getFeedbackList(int pageNumber, int pageSize, Map<String, Object> searchParamMap);
}
