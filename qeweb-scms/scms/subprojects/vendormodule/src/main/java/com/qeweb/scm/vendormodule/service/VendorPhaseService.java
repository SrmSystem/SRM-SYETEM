package com.qeweb.scm.vendormodule.service;

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
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.repository.RoleDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.repository.VendorPhaseDao;

@Service
@Transactional
public class VendorPhaseService {
	
	@Autowired
	private VendorPhaseDao vendorPhaseDao;
	
	@Autowired
	private RoleDao roleDao;


	public Page<VendorPhaseEntity> getVendorPhaseList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPhaseEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPhaseEntity.class);
		return vendorPhaseDao.findAll(spec,pagin);
	}


	public void addNewVendorPhase(VendorPhaseEntity vendorPhase) {
		setRole(vendorPhase);
		vendorPhaseDao.save(vendorPhase);
	}

	public VendorPhaseEntity getVendorPhase(Long id) {
		return vendorPhaseDao.findOne(id);
	}

	/**
	 * 更新供应商阶段
	 * @param vendorPhase 供应商阶段
	 */
	public void updateVendorPhase(VendorPhaseEntity vendorPhase) {
		setRole(vendorPhase);
		vendorPhaseDao.save(vendorPhase);
	}

	public void deleteVendorPhaseList(List<VendorPhaseEntity> vendorPhaseList) {
		vendorPhaseDao.delete(vendorPhaseList);
	}

    /**
     * 获得所有的阶段
     * @return 阶段集合
     */
	public List<VendorPhaseEntity> getVendorPhaseListAll() {
		return (List<VendorPhaseEntity>) vendorPhaseDao.findAll();
	}
	
	private void setRole(VendorPhaseEntity vendorPhase) {
		if(vendorPhase.getRoleId()!=null && vendorPhase.getRoleId()!=0l){
			RoleEntity role = roleDao.findOne(vendorPhase.getRoleId());
			vendorPhase.setRoleCode(role.getCode());
			vendorPhase.setRoleName(role.getName());
		}
	}


	public VendorPhaseEntity getVendorPhaseByCode(String code) {
		List<VendorPhaseEntity> vList = vendorPhaseDao.findByCode(code);
		return vList != null && vList.size() > 0 ? vList.get(0) : null;
	}

}
