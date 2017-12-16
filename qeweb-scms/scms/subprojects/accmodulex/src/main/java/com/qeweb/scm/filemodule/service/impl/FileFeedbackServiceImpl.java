package com.qeweb.scm.filemodule.service.impl;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;
import com.qeweb.scm.filemodule.repository.FileFeedbackDao;
import com.qeweb.scm.filemodule.service.FileFeedbackService;

/**
 * 
 * 文件反馈serviceImpl
 *
 */
@Service
@Transactional
public class FileFeedbackServiceImpl  implements FileFeedbackService{
	
	@Autowired
	private FileFeedbackDao fileFeedbackDao;
	
	
	public Page<FileFeedbackEntity> getFeedbackList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FileFeedbackEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), FileFeedbackEntity.class);
		return fileFeedbackDao.findAll(spec,pagin);
	}
	

	public void add(FileFeedbackEntity fileFeedback) {
		fileFeedbackDao.save(fileFeedback);
	}

	public void addList(List<FileFeedbackEntity> fileFeedbackList) {
		fileFeedbackDao.save(fileFeedbackList);
	}

	public FileFeedbackEntity findFileFeedbackByfileCollaborationIdAndVendorId(Long fileCollaborationId, Long vendorId) {
		return fileFeedbackDao.findFileFeedbackByfileCollaborationIdAndVendorId(fileCollaborationId,vendorId);
	}

	public FileFeedbackEntity get(Long id) {
		return fileFeedbackDao.findById(id);
	}
	

}
