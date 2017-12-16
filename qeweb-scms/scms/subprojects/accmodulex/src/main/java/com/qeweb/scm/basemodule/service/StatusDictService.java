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
import com.qeweb.scm.basemodule.entity.StatusDictEntity;
import com.qeweb.scm.basemodule.repository.StatusDictDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
/**
 * 状态字典Service
 * @author lw
 * 创建时间：2015年6月8日15:51:37
 * 最后更新时间：2015年6月24日14:13:11
 * 最后更新人：lw
 */
@Service
@Transactional
public class StatusDictService {
	
	@Autowired
	private StatusDictDao statusDictDao;


	public Page<StatusDictEntity> getStatusDictList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "statusType");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<StatusDictEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), StatusDictEntity.class);
		return statusDictDao.findAll(spec,pagin);
	}

	public void addNewStatusDict(StatusDictEntity statusDict) {
		statusDictDao.save(statusDict);
	}

	public StatusDictEntity getStatusDict(Long id) {
		return statusDictDao.findOne(id);
	}
	public List<StatusDictEntity> getStatusDictByType(String statusType) {
		return statusDictDao.findByStatusType(statusType);
	}

	public void updateStatusDict(StatusDictEntity statusDict) {
		statusDictDao.save(statusDict);
	}

	public void deleteStatusDictList(List<StatusDictEntity> statusDictList) {
		statusDictDao.delete(statusDictList);
		
	}
	public Iterable<StatusDictEntity> findAll(){
		return statusDictDao.findAll();
	}
	
	
	/**
	 * 供下拉选择使用
	 * @param statusType
	 * @return
	 */
	public String getStatusByType(String statusType){
		List<StatusDictEntity> list = statusDictDao.findByStatusType(statusType);
		String data = "[";
		for (int i = 0 ; i < list.size() ; i++) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + list.get(i).getStatusValue() + "\",\"text\":\"" + list.get(i).getStatusText() + "\"}";
		}
		data = data + "]";
		return data;
	}


}
