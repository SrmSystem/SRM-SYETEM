package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorPhaseCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;

@Service
@Transactional
public class VendorPhaseCfgService {
	
	@Autowired
	private VendorPhaseCfgDao vendorPhaseCfgDao;
	@Autowired
	private VendorSurveyCfgDao vendorSurveyCfgDao;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;


	public Page<VendorPhaseCfgEntity> getVendorPhaseCfgList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPhaseCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPhaseCfgEntity.class);
		return vendorPhaseCfgDao.findAll(spec,pagin);
	}


	public void addNewVendorPhaseCfg(VendorPhaseCfgEntity vendorPhaseCfg) {
		vendorPhaseCfgDao.save(vendorPhaseCfg);
	}

	public VendorPhaseCfgEntity getVendorPhaseCfg(Long id) {
		return vendorPhaseCfgDao.findOne(id);
	}

	public void updateVendorPhaseCfg(VendorPhaseCfgEntity vendorPhaseCfg) {
		vendorPhaseCfgDao.save(vendorPhaseCfg);
	}

	public void deleteVendorPhaseCfgList(List<VendorPhaseCfgEntity> vendorPhaseCfgList) {
		vendorPhaseCfgDao.delete(vendorPhaseCfgList);
	}

    /**
     * 获得所有的阶段
     * @return 阶段集合
     */
	public List<VendorPhaseCfgEntity> getVendorPhaseCfgListAll() {
		return (List<VendorPhaseCfgEntity>) vendorPhaseCfgDao.findAll();
	}


	/**
	 * 获得该供应商目前的阶段和阶段下的调查表，前一阶段的调查表也应当显示
	 * @param vendorId 供应商ID
	 * @return 供应商阶段配置
	 */
	public List<VendorPhaseCfgEntity> getVendorPhaseSurveyCfg(Long vendorId) {
		//重新查询下该供应商，保证其状态的一致性
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findOne(vendorId);
		Long phaseId = vendor.getPhaseId();
		List<VendorPhaseCfgEntity> phaseCfgAllList = vendorPhaseCfgDao.findByOrgIdOrderByPhaseSnAsc(vendor.getOrgId());
		//符合当前供应商阶段的阶段配置和调查表需要过滤后放在这里
		List<VendorPhaseCfgEntity> phaseCfgList = new ArrayList<VendorPhaseCfgEntity>();
		for(VendorPhaseCfgEntity vendorPhaseCfg : phaseCfgAllList){
			if(vendorPhaseCfg.getPhaseId().intValue()==phaseId.intValue()){
				//如果等于该阶段了，配置完调查表后就应当退出了
				setSurveyCfg(vendorPhaseCfg);
				phaseCfgList.add(vendorPhaseCfg);
				break;
			}
			setSurveyCfg(vendorPhaseCfg);
			phaseCfgList.add(vendorPhaseCfg);
		}
		return phaseCfgList;
	}


	/**
	 * 给当前配置设置调查表配置
	 * @param vendorPhaseCfg
	 */
	private void setSurveyCfg(VendorPhaseCfgEntity vendorPhaseCfg) {
		//获取调查表配置
		List<VendorSurveyCfgEntity> vendorSurveyCfgList = vendorSurveyCfgDao.findByVendorPhasecfgIdOrderBySnAsc(vendorPhaseCfg.getId());
		vendorPhaseCfg.setVendorSurveyCfgList(vendorSurveyCfgList);
	}


	/**
	 * 判断该供应商是否处于配置的最后阶段，如果是无法晋级
	 * @param vendor
	 * @return
	 */
    public boolean validateIsEndPhase(VendorBaseInfoEntity vendor) {
		//查询是否还有后置阶段
		List<VendorPhaseCfgEntity> phaseCfgList = vendorPhaseCfgDao.findByOrgIdAndPhaseSnGreaterThan(vendor.getOrgId(),vendor.getPhaseSn());
		if(Collections3.isEmpty(phaseCfgList)){
			return true;
		}
		return false;
	}

    /**
     * 根据供应商获取可降级的阶段
     * @param vendorId 供应商ID
     * @return 可降级的阶段
     */
	public List<VendorPhaseCfgEntity> getDemotionPhaseCfgList(Long vendorId) {
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findOne(vendorId);
		Integer phaseSn = vendor.getPhaseSn();
		List<VendorPhaseCfgEntity> demotionList = vendorPhaseCfgDao.findByOrgIdAndPhaseSnLessThanOrderByPhaseSnDesc(vendor.getOrgId(),phaseSn);
		return demotionList;
	}





	

}
