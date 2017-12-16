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
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;
import com.qeweb.scm.epmodule.entity.EpQuoSubItemEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.repository.EpModuleItemDao;
import com.qeweb.scm.epmodule.repository.EpQuoSubItemDao;

/**
 * 复制报价模型明细service
 * @author u
 *
 */
@Service
@Transactional
public class EpQuoSubItemService {

	@Autowired
	private EpQuoSubItemDao epQuoSubItemDao;
	
	@Autowired
	private EpModuleItemDao epmoduleItemDao;
	
	public Page<EpQuoSubItemEntity> getList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpQuoSubItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpQuoSubItemEntity.class);
		return epQuoSubItemDao.findAll(spec,pagin);
	}
	
	
	/**
	 * 保存复制的报价模板明细
	 * @param epMaterialList
	 * @param moduleItemList
	 */
	public void copyFromEpModuleItem(List<EpMaterialEntity> epMaterialList,List<EpModuleItemEntity> moduleItemList){
		List<EpQuoSubItemEntity> oldEpQuoSubItemList = new ArrayList<EpQuoSubItemEntity>();
		for (EpMaterialEntity epMaterial : epMaterialList) {
			List<EpQuoSubItemEntity> _oldEpQuoSubItemList = epQuoSubItemDao.findByEpMaterialId(epMaterial.getId());
			if(_oldEpQuoSubItemList !=null && _oldEpQuoSubItemList.size()>0){
				oldEpQuoSubItemList.addAll(_oldEpQuoSubItemList);
			}
		}
		//删除已经存在的报价模型明细
		epQuoSubItemDao.delete(oldEpQuoSubItemList);

		List<EpModuleItemEntity> parentList = new ArrayList<EpModuleItemEntity>();
		List<EpModuleItemEntity> childList = new ArrayList<EpModuleItemEntity>();
		//获得所有的父类的报价模型明细
		for (EpModuleItemEntity epModuleItem : moduleItemList) {
			EpModuleItemEntity newParent = getParent(epModuleItem);
			if(newParent !=null){
				parentList.add(newParent);
			}
		}
		if(parentList!=null && parentList.size()>0){
		for (EpMaterialEntity epMaterial : epMaterialList) {
			for (EpModuleItemEntity epModuleItem : parentList) {
				Long _id = epModuleItem.getId();
				//保存父节点
				EpQuoSubItemEntity newEpQuoSubItem = new EpQuoSubItemEntity();
				newEpQuoSubItem.setEpMaterialId(epMaterial.getId());
				newEpQuoSubItem.setParentId(epModuleItem.getParentId());
				newEpQuoSubItem.setName(epModuleItem.getName());
				newEpQuoSubItem.setUnitId(epModuleItem.getUnitId());
				newEpQuoSubItem.setRemarks(epModuleItem.getRemarks());
				newEpQuoSubItem.setIsTop(epModuleItem.getIsTop());
				newEpQuoSubItem.setIsOutData(StatusConstant.STATUS_YES);
				newEpQuoSubItem.setAbolished(Constant.UNDELETE_FLAG);
				epQuoSubItemDao.save(newEpQuoSubItem);
				
				//找到子节点
				childList = epmoduleItemDao.findByParentId(_id);
				//保存子节点
				if(childList !=null && childList.size()>0){
					for (EpModuleItemEntity child : childList) {
						EpQuoSubItemEntity newChild = new EpQuoSubItemEntity();
						newChild.setEpMaterialId(epMaterial.getId());
						newChild.setParentId(newEpQuoSubItem.getId());
						newChild.setName(child.getName());
						newChild.setUnitId(child.getUnitId());
						newChild.setRemarks(child.getRemarks());
						newChild.setIsTop(child.getIsTop());
						newChild.setIsOutData(StatusConstant.STATUS_YES);
						newChild.setAbolished(Constant.UNDELETE_FLAG);
						epQuoSubItemDao.save(newChild);
					}
				}
			}
		}
		}
		
	}
	
	/**
	 * 递归得出父节点
	 * @return
	 */
	public EpModuleItemEntity getParent(EpModuleItemEntity epModuleItem){
		EpModuleItemEntity parent ;
		Long parentId = epModuleItem.getParentId();
		if(parentId != null && parentId!=0){
			parent = epmoduleItemDao.findOne(epModuleItem.getParentId());
			if(parent !=null){
				return getParent(parent);
			}else{
				return epModuleItem;
			}
		}else{
			return epModuleItem;
		}
	}
	
	/**
	 * 复制保存
	 * @param parentList
	 * @param epMaterial
	 */
	public void saveEpQuoSubItem(List<EpModuleItemEntity> parentList,EpMaterialEntity epMaterial){
		
		//复制EpModuleItemEntity 保存到 EpQuoSubItemEntity
		if(parentList !=null && parentList.size()>0){
			
		}
	}
}
