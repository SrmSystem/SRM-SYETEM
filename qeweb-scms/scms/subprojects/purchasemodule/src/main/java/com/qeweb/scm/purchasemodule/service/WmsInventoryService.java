package com.qeweb.scm.purchasemodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.WmsInventoryEntity;
import com.qeweb.scm.purchasemodule.repository.WmsInventoryDao;
import com.qeweb.scm.purchasemodule.web.vo.WmsInventoryTransfer;



@Service
@Transactional
public class WmsInventoryService {

	@Autowired
	private WmsInventoryDao wmsInventoryDao;
	
	@Autowired
	private MaterialDao materialDao;

	public Page<WmsInventoryEntity> getWmsInventory(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WmsInventoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WmsInventoryEntity.class);
		return wmsInventoryDao.findAll(spec,pagin);
	}
	
	public List<WmsInventoryEntity> getWmsInventory(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WmsInventoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WmsInventoryEntity.class);
		return wmsInventoryDao.findAll(spec);   
	}
	
	public boolean saveWmsInventoryEntity(List<WmsInventoryEntity> entityList){
		wmsInventoryDao.save(entityList);    
		return true;
	}
	
	public List<WmsInventoryTransfer> getWmsInventoryVo(Map<String, Object> searchParamMap) {
		List<WmsInventoryTransfer> transList = new ArrayList<WmsInventoryTransfer>();
		List<WmsInventoryEntity> list = getWmsInventory(searchParamMap); 
		if(CollectionUtils.isEmpty(list))
			return transList;
		
		WmsInventoryTransfer trans = null;
		for(WmsInventoryEntity entity : list) {
			trans = new WmsInventoryTransfer();
			trans.setMaterialCode(entity.getMaterial().getCode());
			trans.setMaterialName(entity.getMaterial().getName());
			trans.setStockQty(StringUtils.toString(entity.getStockQty()));
			transList.add(trans);   
		}
		return transList;
	}

	public void saveWmsInventorys(List<WmsInventoryEntity> wmsInventorys) {
		 if(wmsInventorys!=null&&wmsInventorys.size()>0)
		 {
			 for(WmsInventoryEntity wmsInventory:wmsInventorys)
			 {
				 List<WmsInventoryEntity> list= wmsInventoryDao.findInventoryEntityByMaterial(wmsInventory.getMaterial().getCode());
				 
				 if(list!=null&&list.size()==1)
				 {
					 WmsInventoryEntity w1 =list.get(0);
					 w1.setStockQty(wmsInventory.getStockQty());
					 wmsInventoryDao.save(w1);
				 }
				 else if(list==null||list.size()==0)
				 {
					 wmsInventory.setIsOutData(StatusConstant.STATUS_YES);
					 wmsInventory.setAbolished(0);
					 wmsInventory.setReqMonthQty(0.0);
					 wmsInventory.setCreateTime(DateUtil.getCurrentTimestamp());
					 wmsInventoryDao.save(wmsInventory);
				 }
			 }
		 }
	}

	public MaterialEntity getMTLCode(String materialCode) {
		List<MaterialEntity>  list=materialDao.findByCode(materialCode);
		if(list!=null&&list.size()==1)
			return list.get(0);
		else
			return null;
	}

	public WmsInventoryEntity getWmsInventoryByMaterial(long materialId) {
		List<WmsInventoryEntity> list = wmsInventoryDao.getWmsInventoryByMaterial(materialId);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
