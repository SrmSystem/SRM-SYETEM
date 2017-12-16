package com.qeweb.scm.epmodule.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.constans.EpModuleConstans;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpQuoSubCostEntity;
import com.qeweb.scm.epmodule.entity.EpQuoSubItemEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoHisEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoHisEntity;
import com.qeweb.scm.epmodule.repository.EpPriceDao;
import com.qeweb.scm.epmodule.repository.EpQuoSubCostDao;
import com.qeweb.scm.epmodule.repository.EpQuoSubItemDao;
import com.qeweb.scm.epmodule.repository.EpSubQuoDao;
import com.qeweb.scm.epmodule.repository.EpSubQuoHisDao;
import com.qeweb.scm.epmodule.repository.EpVendorDao7;
import com.qeweb.scm.epmodule.repository.EpWholeQuoDao;
import com.qeweb.scm.epmodule.repository.EpWholeQuoHisDao;

/**
 */
@Service
@Transactional
public class EpQuoSubService {
	
	@Autowired
	private EpQuoSubCostDao epQuoSubCostDao;

	@Autowired
	private EpQuoSubItemDao epQuoSubItemDao;
	
	@Autowired
	private EpWholeQuoDao epWholeQuoDao;
	
	@Autowired
	private EpWholeQuoHisDao epWholeQuoHisDao;
	
	@Autowired
	private EpSubQuoDao epSubQuoDao;
	
	@Autowired
	private EpSubQuoHisDao epSubQuoHisDao;
	
	@Autowired
	private EpWholeQuoHisService epWholeQuoHisService;
	
	@Autowired
	private EpPriceDao epPriceDao;
	
	@Autowired
	private EpVendorDao7 epVendorDao7;
	

	
	
	
	public Page<EpSubQuoEntity> getSubQuoList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpSubQuoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpSubQuoEntity.class);
		return epSubQuoDao.findAll(spec,pagin);
	}
	
	public Page<EpQuoSubCostEntity> getSubQuoCostList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpQuoSubCostEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpQuoSubCostEntity.class);
		return epQuoSubCostDao.findAll(spec,pagin);
	}

	
	public EpWholeQuoEntity findWholeQuoByVendorAndMaterial(Long vendorId,Long epMaterialId,Integer isVendor){
		return epWholeQuoDao.findByEpPriceAndVendorAndEpMaterial(vendorId,epMaterialId,isVendor);
	}
	
	public List<EpSubQuoEntity> findEpSubQuoListByWholeId(Long wholeQuoId){
		return epSubQuoDao.findByWholeQuoId(wholeQuoId);
	}
	
	
	public void getNewSubQuo(Long wholeId,Long epMaterialId){
		List<EpQuoSubItemEntity> res=epQuoSubItemDao.findByEpMaterialIdAndIsTop(epMaterialId, EpModuleConstans.IS_TOP_YES);
		for (EpQuoSubItemEntity epQuoSubItemEntity : res) {
			EpSubQuoEntity sub=new EpSubQuoEntity();
			sub.setMaterialName(epQuoSubItemEntity.getName());
			sub.setWholeQuoId(wholeId);
			sub.setQty(1d);
			sub.setTotalQuotePrice(0d);
			sub.setSubtotal(0d);
			sub.setModuleItemId(epQuoSubItemEntity.getId());
			sub.setRemarks(epQuoSubItemEntity.getRemarks());
			sub.setMaterialUnit(epQuoSubItemEntity.getUnitId());
			epSubQuoDao.save(sub);
		}
	}
	
	public List<EpQuoSubCostEntity> findQuoSubCost(Long epQuoSubId){
		return epQuoSubCostDao.findByEpQuoSubId(epQuoSubId);
	}
	
	public List<EpQuoSubCostEntity> getNewQuoSubCost(Long parentId){
		List<EpQuoSubItemEntity> res=epQuoSubItemDao.findByParentId(parentId);
		List<EpQuoSubCostEntity> list=new ArrayList<EpQuoSubCostEntity>();
		for (EpQuoSubItemEntity epQuoSubItemEntity : res) {
			EpQuoSubCostEntity cost=new EpQuoSubCostEntity();
			cost.setName(epQuoSubItemEntity.getName());
		}
		return list;
	}
	
	public void saveQuoSub(int submitStatus,List<EpSubQuoEntity> itemList,EpWholeQuoEntity epWholeQuo,File f,String fileName){
		EpWholeQuoEntity old=epWholeQuoDao.findOne(epWholeQuo.getId());
		if(f!=null){
			epWholeQuo.setQuoteTemplateName(fileName);	//报价附件
			epWholeQuo.setQuoteTemplateUrl(f.getPath().replaceAll("\\\\", "/"));	
		}else{
			epWholeQuo.setQuoteTemplateName(old.getQuoteTemplateName());	//报价附件
			epWholeQuo.setQuoteTemplateUrl(old.getQuoteTemplateUrl());
		}
		
		int status=old.getIsVendor();
		
		Long wholeId =null;
		if(EpConstans.QUOTE_STATUS_NO==submitStatus){
			if(EpConstans.STATUS_NO==status){
				EpWholeQuoEntity newEpWholeQuo = new EpWholeQuoEntity();
				BeanUtils.copyProperties(old, newEpWholeQuo);
				Set<EpWholeQuoHisEntity> hisSet = old.getWholeQuoHises();
				Set<EpWholeQuoHisEntity> newHisSet = new HashSet<EpWholeQuoHisEntity>(hisSet.size());
				if(null != hisSet && ! hisSet.isEmpty()){
					for (EpWholeQuoHisEntity _hisSet : hisSet) {
						newHisSet.add(_hisSet);
					}
				}
				newEpWholeQuo.setWholeQuoHises(newHisSet);
				
				newEpWholeQuo.setId(0L);
				newEpWholeQuo.setIsVendor(EpConstans.STATUS_YES);
				newEpWholeQuo.setAbolished(Constant.UNDELETE_FLAG);
				newEpWholeQuo=parseData(epWholeQuo, newEpWholeQuo);
				wholeId=newEpWholeQuo.getId();
			}else{
				old=parseData(epWholeQuo, old);
				wholeId=old.getId();
			}
		}else{
			if(EpConstans.STATUS_NO==status){
				old.setQuoteStatus(EpConstans.QUOTE_STATUS_YES);
				old=parseData(epWholeQuo, old);
				wholeId=old.getId();
			}else{				
				EpWholeQuoEntity editEntity = epWholeQuoDao.findByEpPriceAndEpVendorAndEpMaterial(old.getEpPrice().getId(), old.getEpVendor().getId(), old.getEpMaterial().getId(), EpConstans.STATUS_NO);

				Set<EpWholeQuoHisEntity> wholeQuoHises = old.getWholeQuoHises();
				epWholeQuoHisDao.delete(wholeQuoHises);
				epWholeQuoDao.delete(old);
				
				editEntity.setQuoteStatus(EpConstans.QUOTE_STATUS_YES);
				editEntity=parseData(epWholeQuo, editEntity);
				wholeId=editEntity.getId();
				
				//old.setQuoteStatus(EpConstans.QUOTE_STATUS_YES);
				//old.setIsVendor(EpConstans.STATUS_NO);
				//old=parseData(epWholeQuo, old);
				//wholeId=old.getId();
				
				
			}
		}
		
		List<EpSubQuoEntity> subList=new ArrayList<EpSubQuoEntity>();
		for (EpSubQuoEntity subQuo : itemList) {
			EpSubQuoEntity oldsubQuo=epSubQuoDao.findOne(subQuo.getId());
			oldsubQuo.setWholeQuoId(wholeId);
			oldsubQuo.setQty(subQuo.getQty());
			oldsubQuo.setTotalQuotePrice(subQuo.getTotalQuotePrice());
			oldsubQuo.setQuotePrice(subQuo.getQuotePrice());
			oldsubQuo.setSubtotal(subQuo.getSubtotal());
			oldsubQuo.setNegotiatedSubPrice(subQuo.getTotalQuotePrice());
			oldsubQuo.setNegotiatedSubTotalPrice(subQuo.getSubtotal());
			epSubQuoDao.save(oldsubQuo);
			subList.add(oldsubQuo);
		}
		if(f!=null){
			// to do
			EpWholeQuoEntity _epWholeQuo=epWholeQuoDao.findOne(wholeId);
			_epWholeQuo.setQuoteTemplateName(fileName);	//报价附件名称
			_epWholeQuo.setQuoteTemplateUrl(f.getPath().replaceAll("\\\\", "/"));	//报价附件说明
			epWholeQuoDao.save(_epWholeQuo);
		}
		
		if(EpConstans.QUOTE_STATUS_YES==submitStatus){
			//判断同一询价单的整项报价是否全部已报价，若全部已报价询价单中的报价状态改为报价完成，否则询价单中的报价状态不改变
			EpWholeQuoEntity buyerEpWholeQuo= epWholeQuoDao.findOne(wholeId);
			EpPriceEntity epPrice = buyerEpWholeQuo.getEpPrice();		//询价单
			EpVendorEntity epVendor=buyerEpWholeQuo.getEpVendor();
			if(epPrice !=null){
				List<EpWholeQuoEntity> epWholeQuoList = new ArrayList<EpWholeQuoEntity>();
				List<EpWholeQuoEntity> epWholeQuoListx = new ArrayList<EpWholeQuoEntity>();
				epWholeQuoList = findByEpPrice(epPrice.getId());
				epWholeQuoListx=findWholeByVendorId(epVendor.getId());
				if(epWholeQuoList !=null && epWholeQuoList.size()>0){
					boolean temp = isAllQuote(epWholeQuoList);
					if(temp){
						//更新询价单中的询价状态
						epPrice.setQuoteStatus(EpConstans.QUOTE_STATUS_ALL);
						epPriceDao.save(epPrice);
					}
				}
				if(epWholeQuoListx !=null && epWholeQuoListx.size()>0){
					boolean tempx = isAllQuote(epWholeQuoListx);
					if(tempx){
						epVendor.setQuoteStatus(EpConstans.STATUS_YES);
						epVendorDao7.save(epVendor);
					}
				}
			}
			
			//将当前的报价单添加到报价历史表中
			epWholeQuoHisService.saveToHis(buyerEpWholeQuo);
			saveToSubHis(buyerEpWholeQuo,subList);
		}
	}
	
	private void saveToSubHis(EpWholeQuoEntity buyerEpWholeQuo,
			List<EpSubQuoEntity> subList) {
		for (EpSubQuoEntity subQuo : subList) {
			EpSubQuoHisEntity his=new EpSubQuoHisEntity();
			his.setWholeQuoId(buyerEpWholeQuo.getId());
			his.setQty(subQuo.getQty());
			his.setTotalQuotePrice(subQuo.getTotalQuotePrice());
			his.setSubtotal(subQuo.getSubtotal());
			epSubQuoHisDao.save(his);
		}
	}

	/**
	 * 根据询价单id获得整项报价表
	 * @param epPriceId
	 * @return
	 */
	public List<EpWholeQuoEntity> findByEpPrice(Long epPriceId){
		return epWholeQuoDao.findByEpPrice(epPriceId);
	}
	
	public List<EpWholeQuoEntity> findWholeByVendorId(Long epVendorId){
		return epWholeQuoDao.findWholeByVendorId(epVendorId);
	}
	
	/**
	 * 循环整项报价集合，查看集合中是否全部已报价
	 * @param epWholeQuoList
	 */
	private boolean isAllQuote(List<EpWholeQuoEntity> epWholeQuoList){
		boolean temp = true;
		for (EpWholeQuoEntity epWholeQuo : epWholeQuoList) {
			if(epWholeQuo.getQuoteStatus().equals(EpConstans.STATUS_NO)){
				temp = false;
				break;
			}else if(epWholeQuo.getQuoteStatus().equals(EpConstans.STATUS_NO)){
				temp = true;
				continue;
			}
		}
		return temp;
	}

	private EpWholeQuoEntity parseData(EpWholeQuoEntity epWholeQuo,
			EpWholeQuoEntity newEpWholeQuo) {
		newEpWholeQuo.setQuoteTemplateName(epWholeQuo.getQuoteTemplateName());
		newEpWholeQuo.setQuoteTemplateUrl(epWholeQuo.getQuoteTemplateUrl());
		newEpWholeQuo.setQuotePrice(epWholeQuo.getQuotePrice());
		newEpWholeQuo.setTotalQuotePrice(epWholeQuo.getTotalQuotePrice());
		newEpWholeQuo.setSupplyCycle(epWholeQuo.getSupplyCycle());
		newEpWholeQuo.setTaxRate(epWholeQuo.getTaxRate());
		newEpWholeQuo.setTaxCategory(epWholeQuo.getTaxCategory());
		newEpWholeQuo.setIsIncludeTax(epWholeQuo.getIsIncludeTax());
		newEpWholeQuo.setWarrantyPeriod(epWholeQuo.getWarrantyPeriod());
		newEpWholeQuo.setTransportationMode(epWholeQuo.getTransportationMode());
		newEpWholeQuo.setPaymentMeans(epWholeQuo.getPaymentMeans());
		newEpWholeQuo.setTechnologyPromises(epWholeQuo.getTechnologyPromises());
		newEpWholeQuo.setQualityPromises(epWholeQuo.getQualityPromises());
		newEpWholeQuo.setServicePromises(epWholeQuo.getServicePromises());
		newEpWholeQuo.setDeliveryPromises(epWholeQuo.getDeliveryPromises());
		newEpWholeQuo.setOtherPromises(epWholeQuo.getOtherPromises());
		epWholeQuo.setQuoteCount(epWholeQuo.getQuoteCount()==null?1:epWholeQuo.getQuoteCount()+1);	//报价次数
		epWholeQuoDao.save(newEpWholeQuo);
		return newEpWholeQuo;
	}
	
	public void saveQuoSubCost(List<EpQuoSubCostEntity> costList,Long quoSubId){
		List<EpQuoSubCostEntity> result=epQuoSubCostDao.findByEpQuoSubId(quoSubId);
		epQuoSubCostDao.delete(result);
		
		EpSubQuoEntity subQuo=epSubQuoDao.findOne(quoSubId);
		EpWholeQuoEntity whole=subQuo.getWholeQuo();
		Double quoTotal=0d;
		for (EpQuoSubCostEntity cost : costList) {
			if(cost.getEpQuoSubId()==null||cost.getEpQuoSubId()<=0){
				cost.setEpQuoSubId(quoSubId);
			}
			cost.setId(0);
			epQuoSubCostDao.save(cost);
			quoTotal=BigDecimalUtil.add(quoTotal, cost.getTotalPrice());
		}
		Double subQty=subQuo.getQty();
		Double oldsubTotal=subQuo.getSubtotal();
		subQuo.setTotalQuotePrice(quoTotal);
		
		subQuo.setSubtotal(BigDecimalUtil.mul(subQuo.getTotalQuotePrice(),subQty));
		Double diff=BigDecimalUtil.sub(subQuo.getSubtotal(), oldsubTotal==null?0d:oldsubTotal);
		
		epSubQuoDao.save(subQuo);
		whole.setQuotePrice(BigDecimalUtil.add(whole.getTotalQuotePrice()==null?0d:whole.getQuotePrice(), diff));
		epWholeQuoDao.save(whole);	
	}
	
	
	
	
	
	
	
	
	
}
