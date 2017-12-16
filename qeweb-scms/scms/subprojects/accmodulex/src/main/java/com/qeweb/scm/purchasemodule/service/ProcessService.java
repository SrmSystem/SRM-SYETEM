package com.qeweb.scm.purchasemodule.service;

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
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.entity.ProcessEntity;
import com.qeweb.scm.purchasemodule.entity.ProcessMaterialRelEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemMatEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderProcessEntity;
import com.qeweb.scm.purchasemodule.repository.ProcessDao;
import com.qeweb.scm.purchasemodule.repository.ProcessMaterialRelDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemMatDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderProcessDao;

/**
 * 物料工序管理service
 */
@Service
@Transactional(rollbackOn=Exception.class)  
public class ProcessService extends BaseService {

	@Autowired
	private ProcessDao processDao;
	
	@Autowired
	private ProcessMaterialRelDao processMaterialRelDao;
	
	@Autowired
	private PurchaseOrderProcessDao purchaseOrderProcessDao;  
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchaseOrderItemMatDao purchaseOrderItemMatDao;
	
	public Page<PurchaseOrderItemMatEntity> getOrderItemMatList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemMatEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemMatEntity.class);
		Page<PurchaseOrderItemMatEntity> page = purchaseOrderItemMatDao.findAll(spec, pagin);
		return page;
	}
	
	public List<PurchaseOrderProcessEntity> getOrderProcessList(Long orderItemId){
		return purchaseOrderProcessDao.findByOrderItemIdAndAbolished(orderItemId, 0);
	}
	
	public List<ProcessMaterialRelEntity> getProcessMaterialRelListByMatId(Long matId){
		return processMaterialRelDao.findByMaterialIdAndAbolished(matId, 0);
	}
	
	public void initOrderProcess(Long orderItemId){
		List<PurchaseOrderProcessEntity> list=getOrderProcessList(orderItemId);
		if(list==null||list.size()<=0){
			PurchaseOrderItemMatEntity item= purchaseOrderItemMatDao.findOne(orderItemId);
			
			List<ProcessMaterialRelEntity> res=getProcessMaterialRelListByMatId(item.getMaterial().getId());
			if(res!=null&&res.size()>0){
				for (ProcessMaterialRelEntity processMaterialRelEntity : res) {
					PurchaseOrderProcessEntity pro=new PurchaseOrderProcessEntity();
					pro.setOrderItemId(orderItemId);
					pro.setProcessId(processMaterialRelEntity.getProcessId());
					pro.setProcessName(processMaterialRelEntity.getProcess().getName());
					purchaseOrderProcessDao.save(pro);
				}
			}else{
				List<ProcessEntity> proList=(List<ProcessEntity>) processDao.findAll();
				if(proList!=null){
					for (ProcessEntity proData : proList) {
						PurchaseOrderProcessEntity pro=new PurchaseOrderProcessEntity();
						pro.setOrderItemId(orderItemId);
						pro.setProcessId(proData.getId());
						pro.setProcessName(proData.getName());
						purchaseOrderProcessDao.save(pro);
					}
				}
			}
		}
	}
	
	
	public Page<ProcessEntity> getProcessList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ProcessEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ProcessEntity.class);
		Page<ProcessEntity> page = processDao.findAll(spec, pagin);
		return page;
	}
	
	public Page<ProcessMaterialRelEntity> getProcessMaterialRelList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ProcessMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ProcessMaterialRelEntity.class);
		Page<ProcessMaterialRelEntity> page = processMaterialRelDao.findAll(spec, pagin);
		return page;
	}
	
	public Page<PurchaseOrderProcessEntity> getPurchaseOrderProcessList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderProcessEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderProcessEntity.class);
		Page<PurchaseOrderProcessEntity> page = purchaseOrderProcessDao.findAll(spec, pagin);
		return page;
	}

	public void saveOrderProcess(List<PurchaseOrderProcessEntity> itemList) {
		for (PurchaseOrderProcessEntity purchaseOrderProcessEntity : itemList) {
			PurchaseOrderProcessEntity old=purchaseOrderProcessDao.findOne(purchaseOrderProcessEntity.getId());
			old.setOrderNum(purchaseOrderProcessEntity.getOrderNum());
			old.setOrderTime(DateUtil.getCurrentTimestamp());
			old.setFileName(purchaseOrderProcessEntity.getFileName());
			old.setFilePath(purchaseOrderProcessEntity.getFilePath());
			purchaseOrderProcessDao.save(old);
		}
	}

	public void saveProcess(ProcessEntity entity) {
		processDao.save(entity);
	}

	public void deleteProcessList(List<ProcessEntity> entityList) {
		for (ProcessEntity processEntity : entityList) {
			List<ProcessMaterialRelEntity> res=processMaterialRelDao.findByProcessIdAndAbolished(processEntity.getId(), 0);
			processMaterialRelDao.delete(res);
			processEntity=processDao.findOne(processEntity.getId());
			processEntity.setAbolished(1);
			processDao.save(processEntity);
		}
		
	}
	
	public void saveProcessMaterialRel(Long processId,List<MaterialEntity> materialList){
		for (MaterialEntity material : materialList) {
			ProcessMaterialRelEntity rel=processMaterialRelDao.findByProcessIdAndMaterialId(processId,material.getId());
			if(rel==null){
				rel=new ProcessMaterialRelEntity();
				rel.setProcessId(processId);
				rel.setMaterialId(material.getId());
				processMaterialRelDao.save(rel);
			}
		}
	}

	public void deleteProcessMaterialRelList(List<ProcessMaterialRelEntity> relList) {
		processMaterialRelDao.delete(relList);
	}
	
	
	

	
}
