package com.qeweb.scm.epmodule.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.repository.EpMaterialDao;

/**
 * 询价物料service
 * @author u
 *
 */
@Service
@Transactional
public class EpMaterialService {

	@Autowired
	private EpMaterialDao epMaterialDao;
	
 
	private EpModuleMaterialRelService epModuleMaterialRelService; 
	
	@Autowired
	private GenerialDao generialDao;
	
	/**
	 * 根据询价物料获得询价单的id
	 * @param searchParamMap
	 * @return
	 */
	public List<Long> getEpPriceIdList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpMaterialEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpMaterialEntity.class);
		List<EpMaterialEntity> epMaterialList = epMaterialDao.findAll(spec);
		List<Long> epPriceIdList = new ArrayList<Long>();
		if(epMaterialList != null && epMaterialList.size()>0){
			for (EpMaterialEntity epMaterial : epMaterialList) {
				Long epPriceId = epMaterial.getPrice().getId();
				epPriceIdList.add(epPriceId);
			}
		}
		return epPriceIdList;
	}
	
	/**
	 * 获取询价物料列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<EpMaterialEntity> getEpMaterialLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpMaterialEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpMaterialEntity.class);
		return epMaterialDao.findAll(spec, pagin);
	}
	
	/**
	 * 保存询价物料
	 * @param epMaterial
	 * @return
	 */
	public EpMaterialEntity save(EpMaterialEntity epMaterial){
		return epMaterialDao.save(epMaterial);
	}
	
	/**
	 * 根据询价物料id查找询价物料
	 * @param id
	 * @return
	 */
	public EpMaterialEntity findById(Long id){
		return epMaterialDao.findOne(id);
	}
	
	/**
	 * 根据询价单的id查找询价物料
	 * @param epPriceId
	 * @return
	 */
	public List<EpMaterialEntity> findByPriceId(Long epPriceId){
		return epMaterialDao.findByPriceId(epPriceId);
	}
	
	/**
	 * 编辑询价物料后保存
	 * @param epPrice
	 * @param materialdatas
	 * @return
	 */
	/*public List<EpMaterialEntity> saveMaterialList(EpPriceEntity epPrice,String materialdatas){
		JSONObject object = JSONObject.fromObject(materialdatas);    
		JSONArray array = (JSONArray) object.get("rows");
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		for(int i=0;i<array.size();i++){
			object = array.getJSONObject(i);
			EpMaterialEntity epMaterial = new EpMaterialEntity();
			String idStr = StringUtils.convertToString(object.get("id"));
			if(idStr != null && idStr.length()>0){
				Long id = Long.parseLong(object.get("id") + "");
				epMaterial = findById(id);
			}
			if(epMaterial == null){
				epMaterial = new EpMaterialEntity();
				
			}
			epMaterial.setIsOutData(StatusConstant.STATUS_YES);
			epMaterial.setAbolished(Constant.UNDELETE_FLAG);
			epMaterial.setPrice(epPrice);
			Long materialId = StringUtils.convertToLong((object.get("materialId") + ""));		//物料Id
			epMaterial.setMaterialId(materialId);				//物料id
			epMaterial.setMaterialCode(StringUtils.convertToString(object.get("materialCode")));				//物料编码
			epMaterial.setMaterialName(StringUtils.convertToString(object.get("materialName")));				//物料名称
			epMaterial.setMaterialSpec(StringUtils.convertToString(object.get("materialSpec")));				//物料规格
			epMaterial.setMaterialUnit(StringUtils.convertToString(object.get("materialUnit")));				//物料单位
			epMaterial.setExpectedBrand(StringUtils.convertToString(object.get("expectedBrand")));				//期望品牌
			epMaterial.setWarranty(StringUtils.convertToString(object.get("warranty")));						//保修期
			epMaterial.setFreight(StringUtils.convertToDouble(object.get("freight")+""));						//运输费
			epMaterial.setPlanPurchaseQty(StringUtils.convertToDouble(StringUtils.convertToString(object.get("planPurchaseQty"))));		//预计采购数量
			Timestamp arrivalTime = DateUtil.stringToTimestamp(StringUtils.convertToString(object.get("arrivalTime")), DateUtil.DATE_FORMAT_YYYY_MM_DD);
			epMaterial.setArrivalTime(arrivalTime);	//期望到达时间

			epMaterial.setCompareMaterialCode(StringUtils.convertToString(object.get("compareMaterialCode")));		//类比零部件编码
			epMaterial.setCompareMaterialPrice(StringUtils.convertToDouble(StringUtils.convertToString(object.get("compareMaterialPrice"))));	//类比零部件现价
			epMaterial.setCompareVendorName(StringUtils.convertToString(object.get("compareVendorName")));			//类比零部件供应商
			epMaterial.setProductStatusDiffer(StringUtils.convertToString(object.get("productStatusDiffer")));		//产品状态差异说明

			
			epMaterial.setRemarks(StringUtils.convertToString(object.get("remarks")));			//备注
			List<Long> moduleIds = getEpModuleMaterialRelService().findModuleIdByMaterialId(materialId);		//根据物料id从报价模型与物料关系表中获得报价模型id
			if(moduleIds != null && moduleIds.size()>0){
				for (Long moduleId : moduleIds) {
					epMaterial.setModule(new EpModuleEntity(moduleId));
					save(epMaterial);
					epMaterialList.add(epMaterial);
				}
			}else{
				save(epMaterial);
				epMaterialList.add(epMaterial);
			}
			
		}
		return epMaterialList;
	}*/
	public List<EpMaterialEntity> saveMaterialList(EpPriceEntity epPrice,JSONObject object, JSONArray array){
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		for(int i=0;i<array.size();i++){
			object = array.getJSONObject(i);
			EpMaterialEntity epMaterial = new EpMaterialEntity();
			String idStr = StringUtils.convertToString(object.get("id"));
			if(idStr != null && idStr.length()>0){
				Long id = Long.parseLong(object.get("id") + "");
				epMaterial = findById(id);
			}
			if(epMaterial == null){
				epMaterial = new EpMaterialEntity();
				
			}
			epMaterial.setIsOutData(StatusConstant.STATUS_YES);
			epMaterial.setAbolished(Constant.UNDELETE_FLAG);
			epMaterial.setPrice(epPrice);
			Long materialId = StringUtils.convertToLong((object.get("materialId") + ""));		//物料Id
			epMaterial.setMaterialId(materialId);				//物料id
			epMaterial.setMaterialCode(StringUtils.convertToString(object.get("materialCode")));				//物料编码
			epMaterial.setMaterialName(StringUtils.convertToString(object.get("materialName")));				//物料名称
			epMaterial.setMaterialSpec(StringUtils.convertToString(object.get("materialSpec")));				//物料规格
			epMaterial.setMaterialUnit(StringUtils.convertToString(object.get("materialUnit")));				//物料单位
			epMaterial.setExpectedBrand(StringUtils.convertToString(object.get("expectedBrand")));				//期望品牌
			epMaterial.setWarranty(StringUtils.convertToString(object.get("warranty")));						//保修期
			epMaterial.setFreight(StringUtils.convertToDouble(object.get("freight")+""));						//运输费
			epMaterial.setPlanPurchaseQty(StringUtils.convertToDouble(StringUtils.convertToString(object.get("planPurchaseQty"))));		//预计采购数量
			Timestamp arrivalTime = DateUtil.stringToTimestamp(StringUtils.convertToString(object.get("arrivalTime")), DateUtil.DATE_FORMAT_YYYY_MM_DD);
			epMaterial.setArrivalTime(arrivalTime);	//期望到达时间

			epMaterial.setCompareMaterialCode(StringUtils.convertToString(object.get("compareMaterialCode")));		//类比零部件编码
			epMaterial.setCompareMaterialPrice(StringUtils.convertToDouble(StringUtils.convertToString(object.get("compareMaterialPrice"))));	//类比零部件现价
			epMaterial.setCompareVendorName(StringUtils.convertToString(object.get("compareVendorName")));			//类比零部件供应商
			epMaterial.setProductStatusDiffer(StringUtils.convertToString(object.get("productStatusDiffer")));		//产品状态差异说明

			
			epMaterial.setRemarks(StringUtils.convertToString(object.get("remarks")));			//备注
			List<Long> moduleIds = getEpModuleMaterialRelService().findModuleIdByMaterialId(materialId);		//根据物料id从报价模型与物料关系表中获得报价模型id
			if(moduleIds != null && moduleIds.size()>0){
				for (Long moduleId : moduleIds) {
					epMaterial.setModule(new EpModuleEntity(moduleId));
					save(epMaterial);
					epMaterialList.add(epMaterial);
				}
			}else{
				save(epMaterial);
				epMaterialList.add(epMaterial);
			}
			
		}
		return epMaterialList;
	}
	
	/**
	 * 删除询价物料
	 * @param epMaterial
	 */
	public void deleteEpMaterial(EpMaterialEntity epMaterial){
		epMaterialDao.delete(epMaterial);
	}

	public EpModuleMaterialRelService getEpModuleMaterialRelService() {
		if(epModuleMaterialRelService == null)
			epModuleMaterialRelService = SpringContextUtils.getBean("epModuleMaterialRelService");
		return epModuleMaterialRelService;
	}

	public void setEpModuleMaterialRelService(
			EpModuleMaterialRelService epModuleMaterialRelService) {
		this.epModuleMaterialRelService = epModuleMaterialRelService;
	}
	
	//add by yao.jin  
	//处理当选择按照供应商维度询价时的数据
	public List<EpMaterialEntity> initMaterialData(EpPriceEntity epPrice,List<MaterialEntity> materialList){
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		if(materialList !=null && materialList.size()>0){
			for (MaterialEntity material : materialList) {
				EpMaterialEntity epMaterial = epMaterialDao.findByPriceIdAndMaterialId(epPrice.getId(), material.getId());
				if(epMaterial == null){
					epMaterial = new EpMaterialEntity();
				}
				epMaterial.setIsOutData(StatusConstant.STATUS_YES);
				epMaterial.setAbolished(Constant.UNDELETE_FLAG);
				epMaterial.setPrice(epPrice);
				Long materialId = material.getId();		//物料Id
				epMaterial.setMaterialId(materialId);				//物料id
				epMaterial.setMaterialCode(material.getCode());				//物料编码
				epMaterial.setMaterialName(material.getName());				//物料名称
				epMaterial.setMaterialSpec(material.getSpecification());				//物料规格
				epMaterial.setMaterialUnit(material.getUnit());				//物料单位

				List<Long> moduleIds = getEpModuleMaterialRelService().findModuleIdByMaterialId(materialId);		//根据物料id从报价模型与物料关系表中获得报价模型id
				if(moduleIds != null && moduleIds.size()>0){
					for (Long moduleId : moduleIds) {
						epMaterial.setModule(new EpModuleEntity(moduleId));
						save(epMaterial);
						epMaterialList.add(epMaterial);
					}
				}else{
					save(epMaterial);
					epMaterialList.add(epMaterial);
				}
				
			}
		}
		return epMaterialList;
	}
	
	//结合报价单，根据询价单id和询价供应商的id查找出供应商所对应的询价物料
	public List<EpMaterialEntity> findByWholeQuo(Long epPriceId, Long epVendorId){
		return epMaterialDao.findByWholeQuo(epPriceId, epVendorId);
	}
	
	/**
	 * 根据sql查询询价物料对象
	 * @param sql
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<EpMaterialEntity> findPageByWholeQuo(String sql,Integer pageNumber,Integer pageSize){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		 List<Map> mapList = (List<Map>) generialDao.querybysql(sql, pagin);
		 for (Map map : mapList) {
			 Long epMaterialId = ((BigDecimal)map.get("ID")).longValue();
			 EpMaterialEntity epMaterial = epMaterialDao.findOne(epMaterialId);
			 if(epMaterial != null){
				 epMaterialList.add(epMaterial);
			 } 
		}
		 return epMaterialList;
	}
	
	/**
	 * 根据sql查询出总数量
	 * @param sql
	 * @return
	 */
	public Integer findCountBySql(String sql){
		 return generialDao.findCountBySql(sql);
	}
	
	
	//end add
	
	
	
}
