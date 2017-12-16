package com.qeweb.scm.basemodule.service;

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
import com.qeweb.scm.basemodule.entity.MailSetEntity;
import com.qeweb.scm.basemodule.repository.MailSetDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class MailSetService {
	
	@Autowired
	private MailSetDao mailSetDao;


	public Page<MailSetEntity> getMailSetList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MailSetEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MailSetEntity.class);
		return mailSetDao.findAll(spec,pagin);
	}

	public void addNewMailSet(MailSetEntity mailSet) {
		mailSetDao.save(mailSet);
	}

	public MailSetEntity getMailSet(Long id) {
		return mailSetDao.findOne(id);
	}

	public void updateMailSet(MailSetEntity mailSet) {
		mailSetDao.save(mailSet);
	}

	public void abolishMailSetList(List<MailSetEntity> mailSetList) {
//		mailSetDao.delete(mailSetList);
		for(MailSetEntity i : mailSetList)
			i.setAbolished(1);
		mailSetDao.save(mailSetList);
	}

	public MailSetEntity getMailSetByTempId(Integer mailTemplateId) {
		return mailSetDao.findByMailTemplateIdAndAbolished(mailTemplateId, 0);
	}
	


}
