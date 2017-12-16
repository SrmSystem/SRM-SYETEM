package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.MaterialSAP;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
/**
 * 采购组 SAP->SRM
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class MaterialSyncService extends BaseService{
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	
	
	public boolean execute(ILogger iLogger,String codes) throws Exception {
		iLogger.log("method execute start");
		List<OrganizationEntity> vendorList = organizationDao.findByRoleTypeAndAbolished(1,0);
		 List<VendorMaterialRelEntity> res=MaterialSAP.getMaterialByVendorCode(vendorList,iLogger);
		
		if(res!=null){
			iLogger.log("总条数:"+res.size());
			List<MaterialEntity> materialIds = new ArrayList<MaterialEntity>();
			List<VendorMaterialRelEntity> vendorMaterialRelIds = new ArrayList<VendorMaterialRelEntity>();
			
 			for (VendorMaterialRelEntity rel : res) {
				if(StringUtils.isEmpty(rel.getVendorCode())){
					iLogger.log("供应商为空");
					continue;
				}
				
				List<OrganizationEntity> orgList=organizationDao.findByCode(rel.getVendorCode());
				OrganizationEntity vendor=null;
				if(null!=orgList && orgList.size()>0){
					vendor=orgList.get(0);
				}
				if(vendor==null){
					iLogger.log("供应商在系统不存在:"+rel.getVendorCode());
					continue;
				}
				MaterialEntity mat=null;
				List<MaterialEntity> materials=materialDao.findByCode(rel.getMaterialCode());
				
				
				
				if(materials==null||materials.size()<=0){
					iLogger.log("新增物料:"+rel.getMaterialCode());
					mat  = new MaterialEntity();
					mat.setCode(rel.getMaterialCode());
					mat.setName(rel.getMaterialName());
					mat.setUnit(rel.getMaterialUnit());
					mat.setAbolished(0);
					mat.setIsOutData(1);
					mat.setMaterialTypeId(null);
					//add by chao.gu 20171108 chao.gu
					mat.setCdqwl(rel.getCdqwl());
					//add end
				}else{
					mat=materials.get(0);
					//add by chao.gu 20171017修改名称
					mat.setName(rel.getMaterialName());
					mat.setUnit(rel.getMaterialUnit());
					mat.setAbolished(0);
					//add end
					//add by chao.gu 20171108 chao.gu
					mat.setCdqwl(rel.getCdqwl());
					//add end
					mat.setIsOutData(1);
				}
				
				materialDao.save(mat);
				materialIds.add(mat);
				
				if(StringUtils.isEmpty(rel.getFactoryCode())){
					iLogger.log("工厂为空");
					continue;
				}
				FactoryEntity factory=factoryDao.findByCode(rel.getFactoryCode());
				if(factory==null){
					iLogger.log("工厂在系统不存在:"+rel.getFactoryCode());
					continue;
				}
				VendorMaterialRelEntity oldRel=vendorMaterialRelDao.findByOrgIdAndMaterialIdAndFactoryId(vendor.getId(), mat.getId(), factory.getId());
				if(oldRel==null){
					iLogger.log("新增物料供应商工厂关系:"+rel.getVendorCode()+"-"+rel.getMaterialCode()+"-"+rel.getFactoryCode());
					oldRel=new VendorMaterialRelEntity();
					oldRel.setFactoryId(factory.getId());
					oldRel.setMaterialId(mat.getId());
					oldRel.setOrgId(vendor.getId());
					oldRel.setAbolished(0);
					oldRel.setIsOutData(BOHelper.OUT_DATA_YES);
				}else{
					oldRel.setAbolished(0);
					
				}
				vendorMaterialRelDao.save(oldRel);
				vendorMaterialRelIds.add(oldRel);
			}
 			
 			
 			//处理数据将未传过来的数据失效（add by hao.qin  20171019）
 			List<Long> ids1 = new ArrayList<Long>();
			List<Long> ids2 = new ArrayList<Long>();
			for(MaterialEntity it1 : materialIds){
				ids1.add(it1.getId());
			}
			
			for(VendorMaterialRelEntity it2 : vendorMaterialRelIds){
				ids2.add(it2.getId());
			}
			
 			List<VendorMaterialRelEntity> saveVendorMaterialList = new ArrayList<VendorMaterialRelEntity>();
 			List<MaterialEntity> saveMaterialList  = new ArrayList<MaterialEntity>();
 			
 			if(vendorMaterialRelIds.size() > 0 && vendorMaterialRelIds != null ){
 				List<VendorMaterialRelEntity> vendorMaterialList = vendorMaterialRelDao.findByNotInIds(ids2);
 				if(vendorMaterialList != null && vendorMaterialList.size() > 0){
 	 				for(VendorMaterialRelEntity vm : vendorMaterialList){
 	 					vm.setAbolished(1);
 	 					saveVendorMaterialList.add(vm);
 	 				}
 	 			}	
 			}
 			
 			if(materialIds.size() > 0 && materialIds != null){
 				List<MaterialEntity>  materialList = materialDao.findByNotInIds(ids1);
 				if(materialList != null && materialList.size() > 0){
 	 				for(MaterialEntity m : materialList){
 	 					m.setAbolished(1);
 	 					saveMaterialList.add(m);
 	 				}
 	 			}
 			}
 			
 			//保存
 			vendorMaterialRelDao.save(saveVendorMaterialList);
 			materialDao.save(saveMaterialList);

			iLogger.log("method execute end");
			return true;
		}else{
			iLogger.log("SAP连接失败");
			return false;
		}
		
	}
}
