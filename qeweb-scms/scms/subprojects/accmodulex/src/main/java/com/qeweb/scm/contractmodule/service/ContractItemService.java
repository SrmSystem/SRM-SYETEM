package com.qeweb.scm.contractmodule.service;
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
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;
import com.qeweb.scm.contractmodule.repository.ContractItemDao;
@Service
@Transactional(rollbackOn=Exception.class)
public class ContractItemService extends BaseService{

	
	@Autowired
	private ContractItemDao contractItemDao;
	

	
	public ContractItemEntity findOne(Long id) {
		return contractItemDao.findOne(id);
	}
	
	public Page<ContractItemEntity> getList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ContractItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ContractItemEntity.class);
		Page<ContractItemEntity> page = contractItemDao.findAll(spec, pagin);
		return page;
	}
	
	/**
	 * 根据合同id查找合同明细
	 * @param contractId
	 * @param abolished
	 * @return
	 */
	public List<ContractItemEntity> getAllItemByContractId(Long contractId,Integer abolished){
		return contractItemDao.findByContractIdAndAbolished(contractId, abolished);
	}
	

}
