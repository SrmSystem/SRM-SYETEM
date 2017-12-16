package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springside.modules.utils.Collections3;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.MaterialImpVO;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;

@Service
@Transactional(rollbackOn=Exception.class) 
public class MaterialService {
	
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private BuyerMaterialRelDao buyerMaterialRelDao;
	
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;

	
	/**
	 * 瑞声使用，按物料+供应商+工厂显示
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<VendorMaterialRelEntity> getAacMaterialList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorMaterialRelEntity.class);
		return vendorMaterialRelDao.findAll(spec,pagin);
	}
	
	
	/**
	 * 获取物料列表
	 * @param pageNumber 页数
	 * @param pageSize 每页大小
	 * @param searchParamMap 查询参数
	 * @return
	 */
	public Page<MaterialEntity> getMaterialList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialEntity.class);
		return materialDao.findAll(spec,pagin);
	}
	
	public String getMaterialListHql(Map<String, Object> searchParamMap){
		StringBuilder sql=new StringBuilder();
		sql.append("select distinct mat  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id ");
		sql.append(" and rel.buyerId ").append(buyerOrgPermissionUtil.getBuyerIdCond());
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	public String getMaterialListCountHql(Map<String, Object> searchParamMap){
		StringBuilder sql=new StringBuilder();
		sql.append("select count(distinct mat)  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id ");
		sql.append(" and rel.buyerId ").append(buyerOrgPermissionUtil.getBuyerIdCond());
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	
	public String getMaterialListHql(Map<String, Object> searchParamMap,Long buyerId){
		StringBuilder sql=new StringBuilder();
		sql.append("select distinct mat  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id ");
		sql.append(" and rel.buyerId =").append(buyerId);
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	public String getMaterialListCountHql(Map<String, Object> searchParamMap,Long buyerId){
		StringBuilder sql=new StringBuilder();
		sql.append("select count(distinct mat)  from BuyerMaterialRelEntity  rel,MaterialEntity mat where rel.materialId =mat.id ");
		sql.append(" and rel.buyerId =").append(buyerId);
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and mat.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	
	public List<MaterialEntity> getMaterialListByBuyer(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		if(buyerOrgPermissionUtil.getBuyerIds()==null){
			return new ArrayList<MaterialEntity>();
		}
		List<MaterialEntity> list= (List<MaterialEntity>) generialDao.querybyhql(getMaterialListHql(searchParamMap), pageNumber, pageSize);
		return list;
	}
	
	public Integer getMaterialListCount( Map<String, Object> searchParamMap) {
		if(buyerOrgPermissionUtil.getBuyerIds()==null){
			return 0;
		}
		return generialDao.findCountByHql(getMaterialListCountHql(searchParamMap));
	}
	
	public List<MaterialEntity> getMaterialListByBuyer(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Long buyerId) {
		List<MaterialEntity> list= (List<MaterialEntity>) generialDao.querybyhql(getMaterialListHql(searchParamMap,buyerId), pageNumber, pageSize);
		return list;
	}
	
	public Integer getMaterialListCount( Map<String, Object> searchParamMap,Long buyerId) {
		return generialDao.findCountByHql(getMaterialListCountHql(searchParamMap,buyerId));
	}

	/**
	 * 物料分类设置
	 * @param materialIds 物料的ID
	 * @param materialTypeId 物料分类ID
	 * @return 分类结果
	 */
	public Map<String, Object> category(String materialIds, String materialTypeId) {
		String[] idArray = materialIds.split(",");
		List<MaterialEntity> materialList = new ArrayList<MaterialEntity>();
		for(String id : idArray){
			MaterialEntity material = materialDao.findOne(Long.parseLong(id));
			MaterialTypeEntity mt= materialTypeDao.findOne(Long.parseLong(materialTypeId));
			material.setMaterialTypeId(Long.parseLong(materialTypeId));
			material.setCategoryStatus(StatusConstant.STATUS_YES);
			material.setPartsCode(mt.getCode());
			material.setPartsName(mt.getName());
			if(mt.getImportance()==null|| mt.getImportance().equals(""))
			{
				material.setPartsType("");
			}
			else
			{
				material.setPartsType(""+mt.getImportance());
			}
			materialList.add(material);
		}
		materialDao.save(materialList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	
	
	public List<MaterialEntity> getMaterialByCode(String code){
		return materialDao.findByCode(code);
	}
	
	public MaterialEntity getMaterialById(Long id) {
		return materialDao.findOne(id);
	}
	

	public List<MaterialEntity> findByCodeAndAbolished(String code,Integer abolished){
		return materialDao.findByCodeAndAbolished(code,abolished);
	}
	
	

	/**
	 * 添加一个物料
	 * @param material 物料对象
	 * @return 添加结果
	 */
	public Map<String, Object> addNew(MaterialEntity material) {
		//检查是否有重复图号的物料
		Map<String,Object> map = new HashMap<String, Object>();
		List<MaterialEntity> materialList = materialDao.findByCode(material.getCode());
		if(Collections3.isNotEmpty(materialList)){
			map.put("success", false);
			map.put("msg", "已经存在该图号！");
			return map;
		}
		material.setId(0L);
		materialDao.save(material);
		List<Long> buyerIds=buyerOrgPermissionUtil.getBuyerIds();
		if(buyerIds!=null){
			for (Long buyerId : buyerIds) {
				BuyerMaterialRelEntity rel=new BuyerMaterialRelEntity();
				rel.setBuyerId(buyerId);
				rel.setMaterialId(material.getId());
				rel.setAbolished(0);
				buyerMaterialRelDao.save(rel);
			}
		}
		map.put("success", true);
		return map;
	}

	
	public Map<String, Object> update(MaterialEntity material) {
		//检查是否有重复图号的物料
		Map<String,Object> map = new HashMap<String, Object>();
		List<MaterialEntity> materialList = materialDao.findByCodeAndIdNotIn(material.getCode(),Lists.newArrayList(material.getId()));
		MaterialEntity m = materialDao.findOne(material.getId());
		if(Collections3.isNotEmpty(materialList)){
			map.put("success", false);
			map.put("msg", "已经存在该图号！");
			return map;
		}
//		material.setAbolished(m.getAbolished());
		m.setCode(material.getCode());
		m.setName(material.getName());
		m.setDescribe(material.getDescribe());
		m.setPicStatus(material.getPicStatus());
		m.setPartsType(material.getPartsType());
		m.setTechnician(material.getTechnician());
		m.setAbolished(material.getAbolished());
		materialDao.save(m);
		map.put("success", true);
		return map;
	}

	/**
	 * 获得全部的物料
	 * @return
	 */
	public List<MaterialEntity> getAll() {
		return (List<MaterialEntity>) materialDao.findAll();
	}

	public void deleteMaterialList(List<MaterialEntity> materialList) {
		//materialDao.delete(materialList);
		for (MaterialEntity materialEntity : materialList) {
			materialDao.abolish(materialEntity.getId());
		}
	}

	public String getPicStatus() {
		String data="[";
		int i=0;
		for(String str:Constant.PIC_STATUS){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+i+"\",\"text\":\""+str+"\"}";
					i++;
		}
		data=data+"]";
		return data;
	}

	public Map<String, Object> abolishBatch(List<MaterialEntity> materials) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(MaterialEntity material:materials)
		{
			materialDao.abolish(material.getId());
		}
		map.put("success", true);
		return map;
	}

	/**
	 * 导入物料
	 * @param list
	 * @param logger
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> importFile(List<MaterialImpVO> list, ILogger logger) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> pMap = new HashMap<String, Object>();//上级物料分组
		Map<String,Object> yanz = new HashMap<String, Object>();//上级物料分组
		List<MaterialEntity> mslist=new ArrayList<MaterialEntity>();
		for(MaterialImpVO vo : list){
			int statr=yanz.size();
			yanz.put(""+vo.getCode(),vo);
			int end=yanz.size();
			if(statr==end)
			{
				map.put("success", false);
				map.put("msg", "导入数据中，有相同的物料编号（"+vo.getCode()+"）！");
				return map;
			}
			List<MaterialEntity> exsitTypeList = materialDao.findByCode(vo.getCode());
			if(Collections3.isNotEmpty(exsitTypeList)){
				map.put("success", false);
				map.put("msg", "系统中，有相同的物料编号（"+vo.getCode()+"）！");
				return map;
			}
			
			MaterialTypeEntity pMaterialType = null;
			if(StringUtils.isNotEmptyStr(vo.getPartsCode())){
				String typeCode = vo.getPartsCode();
				pMaterialType = (MaterialTypeEntity) pMap.get(typeCode);
				if(pMaterialType==null) {
					List<MaterialTypeEntity> pMaterialTypeList =  materialTypeDao.findByCode(typeCode);//上级分类
					if(Collections3.isNotEmpty(pMaterialTypeList)) {
						pMaterialType = pMaterialTypeList.get(0);
						pMap.put(typeCode, pMaterialType);
					}
					else{
						map.put("success", false);
						map.put("msg", "零部件："+typeCode+"不存在");
						return map;
					}
				}
			}
			
			if(StringUtils.isNotEmpty(vo.getPicStatus())){
				boolean flag = true;
				String picList = "";
				for(String str:Constant.PIC_STATUS){
					if(vo.getPicStatus().equals(str)){
						flag = false;
					}
					picList += str+",";
				}
				if(flag){
					logger.log("图纸状态格式为："+picList+"----编码["+vo.getCode()+"]的图纸状态'"+vo.getPicStatus()+"'不正确！");
					continue;
				}
			}
			MaterialEntity material = new MaterialEntity();
			BeanUtil.copyPropertyNotNull(vo, material);
			material.setCategoryStatus(StatusConstant.STATUS_NO);
			material.setEnableStatus(StatusConstant.STATUS_YES);
			if(pMaterialType!=null){
				material.setMaterialTypeId(pMaterialType.getId());
				material.setCategoryStatus(StatusConstant.STATUS_YES);
				material.setPartsCode(pMaterialType.getCode());
				material.setPartsName(pMaterialType.getName());
				if(pMaterialType.getImportance()==null)
				{
					if(vo.getPartsType()==null)
					{
						material.setPartsType("次要零件");
					}
					else
					{
						material.setPartsType(vo.getPartsType());
					}
				}
				else if(pMaterialType.getImportance()==1)
				{
					material.setPartsType("主要零件");
				}
			}
			mslist.add(material);
		}
		materialDao.save(mslist);
		map.put("success", true);
		return map;
	}

	/**
	 * 查找所有的物料
	 * @return
	 */
	public List<MaterialEntity> findAll() {
		return (List<MaterialEntity>) materialDao.findAll();
	}
	
	public void saveMaterials(List<MaterialEntity> materialEntitys){
		materialDao.save(materialEntitys);
	}

	/**
	 * 物料分页查询
	 * @param pageNumber 当前页数
	 * @param pageSize 每页大小
	 * @return
	 */
	public Map<String, Object> materialList1(int pageNumber, int pageSize, Model model, ServletRequest request, HttpSession session) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialEntity> userPage = this.getMaterialList(pageNumber,pageSize,searchParamMap);
		for(MaterialEntity en:userPage.getContent())
		{
//			double vmiStockQty = 0;
//			double wmsStockQty = 0;
//			double reqMonthQty = 0;
//			en.setVmiStockQty(vmiStockQty);
//			en.setWmsStockQty(wmsStockQty);
//			en.setReqMonthQty(reqMonthQty);
			en.setC22(en.getCode()+"/"+en.getName());
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	/**
	 * 修改包装数
	 * @param m
	 */
	public void submitPackage(VendorMaterialRelEntity m) {
		Double maxPackageQty =m.getMaxPackageQty();
		Double minPackageQty =m.getMinPackageQty();
		String relIds=m.getVendorMaterialRelIds();
		for (String id:relIds.split(",")) {
			VendorMaterialRelEntity rel=vendorMaterialRelDao.findOne(Long.valueOf(id));
			rel.setMaxPackageQty(maxPackageQty);
			rel.setMinPackageQty(minPackageQty);
			vendorMaterialRelDao.save(rel);
		}
		
	}
	
	
	public Map<String, Object> abolishBatchVendorMaterialRel(List<VendorMaterialRelEntity> vendorMaterialRels) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<Long,MaterialEntity> materialMap = new HashMap<Long,MaterialEntity>();
		
		
		for(VendorMaterialRelEntity item:vendorMaterialRels){
			vendorMaterialRelDao.abolish(item.getId());
			materialMap.put(item.getMaterial().getId(), item.getMaterial());
		}
		//查询是否存在供应商对应的物料的生效
		List<MaterialEntity>  matList = new ArrayList<MaterialEntity>();
		 for (Map.Entry<Long,MaterialEntity> entry : materialMap.entrySet()) {
			List<VendorMaterialRelEntity> dataList = vendorMaterialRelDao.findByMaterialIdAndAbolished(entry.getKey(), 0);
			//查询是否存在生效的对应的关系
			if(dataList == null ){
				//将物料失效
				 MaterialEntity mat1 =  materialDao.findOne(entry.getKey());
				  if(mat1 != null  ){
					  mat1.setAbolished(1);
					  matList.add(mat1);
				  }	  
			}else{
				//将物料生效
				 MaterialEntity mat2 =  materialDao.findOne(entry.getKey());
				  if(mat2 != null  ){
					  mat2.setAbolished(0);
					  matList.add(mat2);
				  }
			}
		}
		 //更新物料的状态
		 if(matList != null && matList.size() >0){
			  materialDao.save(matList);
		  }
		
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 生效关系
	 * @return
	 */
	
	public Map<String, Object> effectBatch(List<VendorMaterialRelEntity> vendorMaterialRels) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<Long,MaterialEntity> materialMap = new HashMap<Long,MaterialEntity>();
		
		int i = 0;
		for(VendorMaterialRelEntity vendorMaterialRelEntity : vendorMaterialRels){
			vendorMaterialRelDao.effect(vendorMaterialRelEntity.getId());
			materialMap.put(vendorMaterialRelEntity.getMaterial().getId(), vendorMaterialRelEntity.getMaterial());
			i++;
		}
		
		//将对应的物料生效
		  List<MaterialEntity>  matList = new ArrayList<MaterialEntity>();
		  for (Map.Entry<Long,MaterialEntity> entry : materialMap.entrySet()) {
			  
			  MaterialEntity mat =  materialDao.findOne(entry.getKey());
			  if(mat != null){
				  mat.setAbolished(0);
			  }
			  matList.add(mat);
			  
		  }
		  //保存物料
		  if(matList != null && matList.size() >0){
			  materialDao.save(matList);
		  }
		 
		  
		
		if( i == vendorMaterialRels.size()){
			map.put("msg", "操作成功");
			map.put("success", true);
		}else{
			map.put("msg", "操作失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 更新包装数
	 * @param vendorId
	 * @param materialId
	 * @param factoryId
	 * @param maxPackageQty
	 * @param minPackageQty
	 */
	public void updateVendorMaterialPackage(Long vendorId,Long materialId,Long factoryId,Double maxPackageQty,Double minPackageQty){
		VendorMaterialRelEntity rel=vendorMaterialRelDao.findByOrgIdAndMaterialIdAndFactoryId(vendorId, materialId, factoryId);
		if(null!=rel){
					rel.setMaxPackageQty(maxPackageQty);
					rel.setMinPackageQty(minPackageQty);
					vendorMaterialRelDao.save(rel);
		}
	}

}
