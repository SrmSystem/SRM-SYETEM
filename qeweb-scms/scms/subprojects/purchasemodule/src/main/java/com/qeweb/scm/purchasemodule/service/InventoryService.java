package com.qeweb.scm.purchasemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.InventoryEntity;
import com.qeweb.scm.purchasemodule.entity.MinInventoryEntity;
import com.qeweb.scm.purchasemodule.repository.InventoryDao;
import com.qeweb.scm.purchasemodule.repository.MinInventoryDao;
import com.qeweb.scm.purchasemodule.web.vo.InventoryTransfer;
import com.qeweb.scm.purchasemodule.web.vo.MinInventorySettingTransfer;



@Service
@Transactional
public class InventoryService {

	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private MinInventoryDao minInventoryDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;

	public Page<InventoryEntity> getVmiInventory(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InventoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InventoryEntity.class);
		return inventoryDao.findAll(spec,pagin);
	}
	
	public List<InventoryEntity> getVmiInventory(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InventoryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InventoryEntity.class);
		return inventoryDao.findAll(spec);  
	}
	
	public List<InventoryTransfer> getVmiInventoryVo(Map<String, Object> searchParamMap) {
		List<InventoryTransfer> transList = new ArrayList<InventoryTransfer>();
		List<InventoryEntity> list = getVmiInventory(searchParamMap); 
		if(CollectionUtils.isEmpty(list))
			return transList;
		
		InventoryTransfer trans = null;
		for(InventoryEntity entity : list) {
			trans = new InventoryTransfer();
			trans.setVendorCode(entity.getVendor().getCode());
			trans.setVendorName(entity.getVendor().getName());
			trans.setMaterialCode(entity.getMaterial().getCode());
			trans.setMaterialName(entity.getMaterial().getName());
			trans.setStockQty(StringUtils.toString(entity.getStockQty()));
			trans.setStockMinQty(entity.getMinInventory() != null ? StringUtils.toString(entity.getMinInventory().getStockMinQty()) : "");
			transList.add(trans); 
		}
		return transList;
	}
	
	public void updateMinInventory(InventoryEntity inventory) {
		MinInventoryEntity minInventory = getMinInventory(inventory.getVendorCode(), inventory.getMaterialCode());
		if(minInventory.getId() == 0l){
			minInventory.setVendor(organizationDao.findOne(inventory.getVendorId()));
			minInventory.setMaterial(materialDao.findOne(inventory.getMaterialId()));
		}
		minInventory.setStockMinQty(inventory.getStockMinQty());
		minInventoryDao.save(minInventory);
		InventoryEntity invEntity = getInventory(inventory.getId());
		invEntity.setMinInventory(minInventory);
		inventoryDao.save(invEntity);
	}

	/**
	 * 保存批量设置最小库存
	 * @param list
	 * @param logger
	 * @return
	 */
	public boolean saveMinInventorySetting(List<MinInventorySettingTransfer> tranList, ILogger logger) {
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<MinInventorySettingTransfer> list = validateTransfers(tranList, orgMap, materialMap, counts, logger);
		if(list.size() != tranList.size())  
			return false;
		
		logger.log("\n->正在准备合并并保存最小库存设置\n");
		List<InventoryEntity> invList = new ArrayList<InventoryEntity>();
		MinInventoryEntity minInventory = null;
		List<InventoryEntity> inventoryList = null;   
		for(MinInventorySettingTransfer minsetting : list) {
			minInventory = getMinInventory(minsetting.getVendorCode(), minsetting.getMaterialCode());
			minInventory.setVendor(orgMap.get(minsetting.getVendorCode()));
			minInventory.setMaterial(materialMap.get(minsetting.getMaterialCode()));
			minInventory.setStockMinQty(StringUtils.convertToDouble(minsetting.getStockMinQty()));
			minInventory.setRemark(minsetting.getRemark()); 
			minInventoryDao.save(minInventory);
			
			inventoryList = getInventoryEntity(minsetting.getVendorCode(), minsetting.getMaterialCode());
			if(CollectionUtils.isEmpty(inventoryList))
				continue;
			
			for(InventoryEntity in : inventoryList)
				in.setMinInventory(minInventory);
			invList.addAll(inventoryList);   
		}
		inventoryDao.save(invList);      
		logger.log("\n->保存批量设置最小库存成功\n");
		return true;
	}
	
	public boolean saveInventoryEntity(List<InventoryEntity> invList){
		inventoryDao.save(invList);    
		return true;
	}
			

	private List<MinInventorySettingTransfer> validateTransfers(List<MinInventorySettingTransfer> tranList, Map<String, OrganizationEntity> orgMap,
			Map<String, MaterialEntity> materialMap, Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入最小库存设置信息进行预处理,共有[" + (tranList == null ? 0 : tranList.size()) + "]条数据";
		logger.log(logMsg);
		counts[0] = tranList.size();
		List<MinInventorySettingTransfer> ret = new ArrayList<MinInventorySettingTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<MaterialEntity> materialList = null;	
		boolean lineValidat = true;
		int index  = 2; 
		for(MinInventorySettingTransfer trans : tranList) {
			if(!orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCode(trans.getVendorCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],供方代码[" + trans.getVendorCode() + "]未在系统中维护,忽略此最小库存设置");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			if(!materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findByCode(trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + (index) + "],物料(" + trans.getMaterialCode() + "], 不存在,忽略此最小库存设置");
				} else {
					materialMap.put(trans.getMaterialCode(), materialList.get(0));
				}
			}
			if(lineValidat) {
				logMsg = "[SUCCESS]行索引[" + (index) + "],预处理[" + trans.getVendorCode() + "|" + trans.getMaterialCode() + "|" + trans.getStockMinQty() + "]成功。";
				logger.log(logMsg);
				ret.add(trans);
			}
			index ++;
			lineValidat = true;
		}
		counts[1] = ret.size();
		logMsg = "<-导入的采购订单主信息预处理完毕,共有[" + ret.size() + "]条有效数据";
		logger.log(logMsg);
		return ret;   
	}
	
	private MinInventoryEntity getMinInventory(String vendorCode, String materialCode) {
		List<MinInventoryEntity> list = minInventoryDao.findMinInventoryEntityByVendorAndMaterial(vendorCode, materialCode);
		return CollectionUtils.isEmpty(list) ? new MinInventoryEntity() : list.get(0);   
	}
	
	private List<InventoryEntity> getInventoryEntity(String vendorCode, String materialCode){
		List<InventoryEntity> list = inventoryDao.findInventoryEntityByVendorAndMaterial(vendorCode, materialCode);
		return list;   
	}

	public InventoryEntity getInventory(Long id) {
		return inventoryDao.findOne(id);
	}

	public InventoryEntity getInventoryByVendorAndMaterial(long vendorId, long materialId) {
		List<InventoryEntity> list = inventoryDao.getInventoryByVendorAndMaterial(vendorId, materialId);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
