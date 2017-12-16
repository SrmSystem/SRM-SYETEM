package com.qeweb.scm.epmodule.service;

import java.util.ArrayList;
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
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpQuoSubItemEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpSubQuoDao;

/**
 * 分项报价实体类service
 * @author u
 *
 */
@Service
@Transactional
public class EpSubQuoService {
	
	private static final Integer ISTOP_YES = 1;
	private static final Integer ISTOP_NO = 2;
	private static final Integer ISVENDOR_NO = 0;
	private static final Integer ISVENDOR_YES = 1;

	@Autowired
	private EpSubQuoDao epSubQuoDao;
	
	public EpSubQuoEntity save(EpSubQuoEntity epSubQuo){
		return epSubQuoDao.save(epSubQuo);
	}
	
	public Page<EpSubQuoEntity> getList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpSubQuoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpSubQuoEntity.class);
		return epSubQuoDao.findAll(spec,pagin);
	}
	
	public List<EpSubQuoEntity> saveEpSubQuos(EpPriceEntity newEpPrice,List<EpWholeQuoEntity> epWholeQuoList,List<EpModuleItemEntity> moduleItemList){
/*		List<EpSubQuoEntity> epSubQuoList = new ArrayList<EpSubQuoEntity>();
		for (EpWholeQuoEntity epWholeQuo : epWholeQuoList) {
			for (EpModuleItemEntity epModuleItem : moduleItemList) {
				EpSubQuoEntity epSubQuo = epSubQuoDao.findByWholeQuoAndModuleItem(epWholeQuo.getId(), epModuleItem.getId());
				if(epSubQuo == null){
					epSubQuo = new EpSubQuoEntity();
				}				
				epSubQuo.setWholeQuo(epWholeQuo);	//整项报价
				epSubQuo.setCarriageCharges(epWholeQuo.getEpMaterial().getFreight());  //运输费
				epSubQuo.setMaterialName(epWholeQuo.getEpMaterial().getMaterialName()); //部件名称
				epSubQuo.setMaterialSpec(epWholeQuo.getEpMaterial().getMaterialSpec()); //型号规格
				epSubQuo.setQty(epWholeQuo.getEpMaterial().getPlanPurchaseQty()); //数量
				epSubQuo.setMaterialUnit(epWholeQuo.getEpMaterial().getMaterialUnit()); //单位
				epSubQuo.setBrand(epWholeQuo.getEpMaterial().getManufacturer()); //品牌/生产厂家
				epSubQuo.setIsOutData(StatusConstant.STATUS_YES);
				epSubQuo.setAbolished(Constant.UNDELETE_FLAG);
				//若询价单主页面选择一级报价 或选择二级报价并且选择供应商 时，添加parentId为null的报价模板明细
				if((newEpPrice.getIsTop() == ISTOP_YES) ||(newEpPrice.getIsTop() == ISTOP_NO && newEpPrice.getIsVendor() ==ISVENDOR_YES)){
					if(epModuleItem.getParentId()==null ||epModuleItem.getParentId()==0){
					epSubQuo.setModuleItem(epModuleItem);	//报价模型明细
					}
					//若询价单主页面选择二级报价并且选择采购商 时，添加parentId不为null的报价模板明细
				}else if(newEpPrice.getIsTop() == ISTOP_NO && newEpPrice.getIsVendor() ==ISVENDOR_NO){
					if(epModuleItem.getParentId()!=null && epModuleItem.getParentId()>=0){
						epSubQuo.setModuleItem(epModuleItem);	//报价模型明细
					}
				}
				epSubQuo = save(epSubQuo);
				epSubQuoList.add(epSubQuo);
			}
		}
		return epSubQuoList;*/
		return null;
	}
	
}
