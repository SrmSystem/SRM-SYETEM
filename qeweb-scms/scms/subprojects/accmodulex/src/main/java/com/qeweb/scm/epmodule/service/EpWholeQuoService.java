package com.qeweb.scm.epmodule.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoHisEntity;
import com.qeweb.scm.epmodule.repository.EpPriceDao;
import com.qeweb.scm.epmodule.repository.EpSubQuoDao;
import com.qeweb.scm.epmodule.repository.EpVendorDao7;
import com.qeweb.scm.epmodule.repository.EpWholeQuoDao;
import com.qeweb.scm.epmodule.web.vo.EpWholeQuoTransfer;

/**
 * 整项报价service
 * @author u
 *
 */
@Service
@Transactional
public class EpWholeQuoService {
	
	@Autowired
	private EpWholeQuoDao epWholeQuoDao;
	
	@Autowired
	private EpSubQuoDao epSubQuoDao;
	
	@Autowired
	private EpPriceDao epPriceDao;
	
	@Autowired
	private EpVendorDao7 epVendorDao7;
	
	@Autowired
	private EpWholeQuoHisService epWholeQuoHisService;
	
	public EpWholeQuoEntity findById(Long id){
		return epWholeQuoDao.findOne(id);
	}
	
	/**
	 * 保存整项报价
	 * @param epWholeQuo
	 * @return
	 */
	public EpWholeQuoEntity save(EpWholeQuoEntity epWholeQuo){
		return epWholeQuoDao.save(epWholeQuo);
	}
	
	public EpWholeQuoEntity savex(EpWholeQuoEntity epWholeQuo,List<EpSubQuoEntity> itemList){
		EpWholeQuoEntity whole= epWholeQuoDao.save(epWholeQuo);
		for (EpSubQuoEntity epSubQuoEntity : itemList) {
			EpSubQuoEntity old=epSubQuoDao.findOne(epSubQuoEntity.getId());
			old.setNegotiatedSubPrice(epSubQuoEntity.getNegotiatedSubPrice());
			old.setNegotiatedSubTotalPrice(epSubQuoEntity.getNegotiatedSubTotalPrice());
			epSubQuoDao.save(old);
		}
		return whole;
	}
	
	/**
	 * 保存整项报价集合
	 * @param epWholeQuoList
	 * @return
	 */
	public Iterable<EpWholeQuoEntity> save(List<EpWholeQuoEntity> epWholeQuoList){
		return epWholeQuoDao.save(epWholeQuoList);
	}
	
	/**
	 * 根据询价单id、询价供应商id、询价物料id获得整项报价表
	 * @param epPriceId
	 * @param epVendorId
	 * @param epMaterialId
	 * @return
	 */
	public EpWholeQuoEntity findByEpPriceAndEpVendorAndEpMaterial(Long epPriceId,Long epVendorId,Long epMaterialId,Integer isVendor){
		return epWholeQuoDao.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId,isVendor);
	}
	
	/**
	 * 根据询价单id、询价物料id获得整项报价表
	 * @param epPriceId
	 * @param epMaterialId
	 * @return
	 */
	public List<EpWholeQuoEntity> findByEpPriceAndEpMaterial(Long epPriceId,Long epMaterialId,Integer isVendor){
		return epWholeQuoDao.findByEpPriceAndEpMaterial(epPriceId, epMaterialId,isVendor);
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
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<EpWholeQuoEntity> getEpWholeQuoLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpWholeQuoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpWholeQuoEntity.class);
		return epWholeQuoDao.findAll(spec, pagin);
	}
	
	/**
	 * 创建询价单时保存整项报价
	 * @param epPrice
	 * @param epVendorList
	 * @param epMaterialList
	 * @return
	 */
	public List<EpWholeQuoEntity> saveEpWholeQuos(EpPriceEntity epPrice,List<EpVendorEntity> epVendorList, List<EpMaterialEntity> epMaterialList){
		List<EpWholeQuoEntity> epWholeList = new ArrayList<EpWholeQuoEntity>();
		for (EpVendorEntity epVendor : epVendorList) {
			for (EpMaterialEntity epMaterial : epMaterialList) {
				EpWholeQuoEntity epWhole = epWholeQuoDao.findByEpPriceAndEpVendorAndEpMaterial(epPrice.getId(), epVendor.getId(), epMaterial.getId(),EpConstans.STATUS_NO);
				if(epWhole == null){
					epWhole = new EpWholeQuoEntity();
				}
				epWhole.setQuoteCount(EpConstans.STATUS_NO); 		//报价当前次数
				epWhole.setQuoteEndTime(epPrice.getQuoteEndTime());  //报价截止日期
				epWhole.setApplicationStatus(epPrice.getApplicationStatus()); //报名状态
				epWhole.setQuoteStatus(EpConstans.QUOTE_STATUS_NO); //报价状态
				epWhole.setNegotiatedStatus(EpConstans.STATUS_NO); //采方议价状态
				epWhole.setNegotiatedCheckStatus(EpConstans.STATUS_NO); //供方确认议价状态
				epWhole.setCloseStatus(EpConstans.STATUS_NO); //报价单关闭状态
				epWhole.setBrand(epMaterial.getExpectedBrand()); //品牌
				epWhole.setManufacturer(epMaterial.getManufacturer()); //生产厂家
				epWhole.setMaterialComposition(epMaterial.getMaterialComposition()); //材质构成
				epWhole.setWarrantyPeriod(epMaterial.getWarranty()); //保质期
				epWhole.setCooperationQty(epMaterial.getPlanPurchaseQty());//合作数量
				epWhole.setCooperationStatus(EpConstans.STATUS_NO);//合作状态
				epWhole.setEpPrice(epPrice);//询价单
				epWhole.setEpVendor(epVendor);//询价单供应商
				epWhole.setEpMaterial(epMaterial);//询价单物料
			    epWhole.setRequoteStatus(EpConstans.STATUS_NO);//重新报价状态
				epWhole.setPublishStatus(EpConstans.STATUS_NO);//发布状态
				epWhole.setEipApprovalStatus(EpConstans.STATUS_NO);//审核状态
				epWhole.setIsOutData(StatusConstant.STATUS_YES);
				epWhole.setAbolished(Constant.UNDELETE_FLAG);
				epWhole.setIsVendor(EpConstans.STATUS_NO); //用于区分采购商、供应商
				epWhole = save(epWhole);
				epWholeList.add(epWhole);
			}
		}
		return epWholeList;
	}
	
	/**
	 * 供应商报名时在整项报价表中添加询价供应商的信息
	 * @param epPrice
	 * @param epMaterialList
	 * @param epVendor
	 */
	public void saveApplicationVendor(EpPriceEntity epPrice,List<EpMaterialEntity> epMaterialList,EpVendorEntity epVendor){
		for (EpMaterialEntity epMaterial : epMaterialList) {
				EpWholeQuoEntity epWholeQuo = new EpWholeQuoEntity();
				epWholeQuo.setIsVendor(EpConstans.STATUS_NO); //用于区分采购商、供应商
				epWholeQuo.setQuoteCount(EpConstans.STATUS_NO); 		//报价当前次数
				epWholeQuo.setQuoteEndTime(epPrice.getQuoteEndTime());  //报价截止日期
				epWholeQuo.setApplicationStatus(epPrice.getApplicationStatus()); //报名状态
				epWholeQuo.setQuoteStatus(EpConstans.STATUS_NO); //报价状态
				epWholeQuo.setNegotiatedStatus(EpConstans.STATUS_NO); //采方议价状态
				epWholeQuo.setNegotiatedCheckStatus(EpConstans.STATUS_NO); //供方确认议价状态
				epWholeQuo.setCloseStatus(EpConstans.STATUS_NO); //报价单关闭状态
				epWholeQuo.setBrand(epMaterial.getExpectedBrand()); //品牌
				epWholeQuo.setManufacturer(epMaterial.getManufacturer()); //生产厂家
				epWholeQuo.setMaterialComposition(epMaterial.getMaterialComposition()); //材质构成
				epWholeQuo.setWarrantyPeriod(epMaterial.getWarranty()); //保质期
				epWholeQuo.setCooperationQty(epMaterial.getPlanPurchaseQty());//合作数量
				epWholeQuo.setCooperationStatus(EpConstans.STATUS_NO);//合作状态
				epWholeQuo.setEpPrice(epPrice);//询价单
				epWholeQuo.setEpVendor(epVendor);//询价单供应商
				epWholeQuo.setEpMaterial(epMaterial);//询价单物料
				epWholeQuo.setRequoteStatus(EpConstans.STATUS_NO);//重新报价状态
				epWholeQuo.setPublishStatus(EpConstans.STATUS_NO);//发布状态
				epWholeQuo.setEipApprovalStatus(EpConstans.STATUS_NO);//审核状态
				epWholeQuo.setIsOutData(StatusConstant.STATUS_YES);
				epWholeQuo.setAbolished(Constant.UNDELETE_FLAG);
				save(epWholeQuo);
			}
	}
	
	/**
	 * 保存整项报价
	 * @param newEpWholeQuo
	 * @param epWholeQuo
	 */
	public Map<String,Object> saveEpWholeQuoEntity(EpWholeQuoEntity _epWholeQuo,EpWholeQuoEntity epWholeQuo){
		Map<String,Object> map = new HashMap<String, Object>();
		EpWholeQuoEntity newEpWholeQuo = new EpWholeQuoEntity();
		Integer isVendor = _epWholeQuo.getIsVendor();
		if(isVendor.equals(EpConstans.STATUS_NO)){//采购商端数据
			BeanUtils.copyProperties(_epWholeQuo, newEpWholeQuo);
			newEpWholeQuo.setId(0L);
			newEpWholeQuo.setIsVendor(EpConstans.STATUS_YES);
			newEpWholeQuo.setAbolished(Constant.UNDELETE_FLAG);
			newEpWholeQuo.setIsOutData(StatusConstant.STATUS_YES);
		
			Set<EpWholeQuoHisEntity> hisSet = _epWholeQuo.getWholeQuoHises();
			Set<EpWholeQuoHisEntity> newHisSet = new HashSet<EpWholeQuoHisEntity>(hisSet.size());
			if(null != hisSet && ! hisSet.isEmpty()){
				for (EpWholeQuoHisEntity _hisSet : hisSet) {
					newHisSet.add(_hisSet);
				}
			}
			newEpWholeQuo.setWholeQuoHises(newHisSet);
		}else if(isVendor.equals(EpConstans.STATUS_YES)){
			newEpWholeQuo = _epWholeQuo;
		}
		newEpWholeQuo.setCreateTime(epWholeQuo.getCreateTime());
		newEpWholeQuo.setQuotePrice(epWholeQuo.getQuotePrice());	//含税单价
		newEpWholeQuo.setTotalQuotePrice(epWholeQuo.getQuotePrice());	//不含税单价
		newEpWholeQuo.setSupplyCycle(epWholeQuo.getSupplyCycle());
		newEpWholeQuo.setTaxRate(epWholeQuo.getTaxRate());
		newEpWholeQuo.setTaxCategory(epWholeQuo.getTaxCategory());
		newEpWholeQuo.setWarrantyPeriod(epWholeQuo.getWarrantyPeriod());
		newEpWholeQuo.setTransportationMode(epWholeQuo.getTransportationMode());
		newEpWholeQuo.setPaymentMeans(epWholeQuo.getPaymentMeans());
		newEpWholeQuo.setTechnologyPromises(epWholeQuo.getTechnologyPromises());
		newEpWholeQuo.setQualityPromises(epWholeQuo.getQualityPromises());
		newEpWholeQuo.setServicePromises(epWholeQuo.getServicePromises());
		newEpWholeQuo.setDeliveryPromises(epWholeQuo.getDeliveryPromises());
		newEpWholeQuo.setOtherPromises(epWholeQuo.getOtherPromises());
		epWholeQuoDao.save(newEpWholeQuo);
		
		map.put("msg", "保存成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 提交整项报价
	 * @param newEpWholeQuo
	 * @param epWholeQuo
	 */
	public Map<String,Object> submitEpWholeQuoEntity(EpWholeQuoEntity newEpWholeQuo,EpWholeQuoEntity epWholeQuo){
		Map<String,Object> map = new HashMap<String, Object>();
		EpWholeQuoEntity buyerEpWholeQuo = new EpWholeQuoEntity();
		List<EpWholeQuoEntity> epWholeQuoList = new ArrayList<EpWholeQuoEntity>();
		List<EpWholeQuoEntity> epWholeQuoListx = new ArrayList<EpWholeQuoEntity>();
		Integer isVendor = newEpWholeQuo.getIsVendor();
		Long epPriceId = 0l;
		Long epMaterialId = 0l;
		Long epVendorId = 0l;
		if(isVendor.equals(EpConstans.STATUS_YES)){//供应商端数据
			epPriceId = newEpWholeQuo.getEpPrice().getId();
			epMaterialId = newEpWholeQuo.getEpMaterial().getId();
			epVendorId = newEpWholeQuo.getEpVendor().getId();
			epWholeQuoDao.delete(newEpWholeQuo);
			buyerEpWholeQuo = epWholeQuoDao.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId, EpConstans.STATUS_NO);
		}else{
			buyerEpWholeQuo = newEpWholeQuo;
		}
		if(buyerEpWholeQuo !=null){
			buyerEpWholeQuo.setQuoteStatus(EpConstans.QUOTE_STATUS_YES);	//报价状态
			buyerEpWholeQuo.setQuoteCount(buyerEpWholeQuo.getQuoteCount()+1);	//报价当前次数
			buyerEpWholeQuo.setCreateTime(epWholeQuo.getCreateTime());		//报价时间
			buyerEpWholeQuo.setTotalQuotePrice(epWholeQuo.getTotalQuotePrice()); 	//无税单价
			buyerEpWholeQuo.setQuotePrice(epWholeQuo.getQuotePrice());		//含税单价
			buyerEpWholeQuo.setSupplyCycle(epWholeQuo.getSupplyCycle());		//供货周期
			buyerEpWholeQuo.setTaxRate(epWholeQuo.getTaxRate());		//税率
			buyerEpWholeQuo.setTaxCategory(epWholeQuo.getTaxCategory());		//税种
			buyerEpWholeQuo.setWarrantyPeriod(epWholeQuo.getWarrantyPeriod());		//保质期
			buyerEpWholeQuo.setTransportationMode(epWholeQuo.getTransportationMode());		//运输方式
			buyerEpWholeQuo.setPaymentMeans(epWholeQuo.getPaymentMeans());		//支付方式
			buyerEpWholeQuo.setTechnologyPromises(epWholeQuo.getTechnologyPromises());		//技术承诺
			buyerEpWholeQuo.setQualityPromises(epWholeQuo.getQualityPromises());		//质量承诺
			buyerEpWholeQuo.setServicePromises(epWholeQuo.getServicePromises());		//服务承诺
			buyerEpWholeQuo.setDeliveryPromises(epWholeQuo.getDeliveryPromises());		//交期承诺
			buyerEpWholeQuo.setOtherPromises(epWholeQuo.getOtherPromises());		//其他承诺
			
			epWholeQuoDao.save(buyerEpWholeQuo);
			
			map.put("msg", "提交成功");
		}else{
			map.put("msg", "提交失败");
		}
		
		//判断同一询价单的整项报价是否全部已报价，若全部已报价询价单中的报价状态改为报价完成，否则询价单中的报价状态不改变
		EpPriceEntity epPrice = buyerEpWholeQuo.getEpPrice();		//询价单
		EpVendorEntity epVendor=buyerEpWholeQuo.getEpVendor();
		if(epPrice !=null && epPrice.getJoinWay().equals(EpConstans.STATUS_NO)){//epPrice 不为null 并且为邀请方式
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
		
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 循环整项报价集合，查看集合中是否全部已报价
	 * @param epWholeQuoList
	 */
	private boolean isAllQuote(List<EpWholeQuoEntity> epWholeQuoList){
		boolean temp = true;
		for (EpWholeQuoEntity epWholeQuo : epWholeQuoList) {
			if(epWholeQuo.getQuoteStatus().equals(EpConstans.QUOTE_STATUS_NO)){
				temp = false;
				break;
			}else if(epWholeQuo.getQuoteStatus().equals(EpConstans.QUOTE_STATUS_YES)){
				temp = true;
				continue;
			}
		}
		return temp;
	}
	
	
	/**
	 * 采购商点击重新报价时修改报价截止时间
	 * @param epPrice
	 * @param epWholeQuo
	 */
	public Map<String,Object> submitQuoteEndTime(EpWholeQuoEntity oldEpWholeQuo,EpWholeQuoEntity epWholeQuo){
		Map<String,Object> map = new HashMap<String, Object>();
		Long newQuoteEndTime = epWholeQuo.getQuoteEndTime().getTime();  //修改后的报价截止时间
		Long oldQuoteEndTime = oldEpWholeQuo.getQuoteEndTime().getTime();	//修改前的报价截止时间
		
		if((oldQuoteEndTime-newQuoteEndTime) >= 0){
			map.put("msg", "修改后的时间小于或等于修改前的时间，请重新修改！");
			map.put("success", false);
		}else{
			oldEpWholeQuo.setQuoteEndTime(epWholeQuo.getQuoteEndTime()); //报价截止时间
			oldEpWholeQuo.setQuoteStatus(EpConstans.QUOTE_STATUS_NO); 	//	报价状态
			oldEpWholeQuo.setNegotiatedPrice(null); //协商无税价格
			oldEpWholeQuo.setNegotiatedStatus(EpConstans.STATUS_NO);//采方议价状态
			oldEpWholeQuo.setNegotiatedTime(null);//采方议价时间
			oldEpWholeQuo.setNegotiatedCheckStatus(EpConstans.STATUS_NO);//供方确认议价状态
			oldEpWholeQuo.setNegotiatedCheckTime(null);//供方确认议价时间
			oldEpWholeQuo.setRequoteStatus(EpConstans.STATUS_YES);//重新报价状态:0=未重新报价；1=重新报价
			oldEpWholeQuo.setNegotiatedUserId(null);//采方议价人
			oldEpWholeQuo.setNegotiatedCheckUserId(null);//供方确认议价人

			save(oldEpWholeQuo);
			map.put("msg", "提交成功！");
			map.put("success", true);
		}
		return map;
	}
	
	
	/**
	 * 获得整项报价vo
	 */
	public List<EpWholeQuoTransfer> getTransfer(EpWholeQuoEntity epWholeQuo){
		List<EpWholeQuoTransfer> list = new ArrayList<EpWholeQuoTransfer>();
		EpWholeQuoTransfer transfer = new EpWholeQuoTransfer();
		transfer.setQuoteCount(StringUtils.convertToString(epWholeQuo.getQuoteCount()));	//报价当前次数
		transfer.setQuoteEndTime(StringUtils.convertToString(epWholeQuo.getQuoteEndTime()));	//报价截止日期
		transfer.setApplicationStatus(StringUtils.convertToString(epWholeQuo.getEpVendor().getApplicationStatus()));	//报名状态
		transfer.setQuoteStatus(StringUtils.convertToString(epWholeQuo.getQuoteStatus()));	//报价状态
		transfer.setQuotePrice(StringUtils.convertToString(epWholeQuo.getQuotePrice()));	//含税单价
		transfer.setNegotiatedPrice(StringUtils.convertToString(epWholeQuo.getNegotiatedPrice()));	//协商无税价格
		transfer.setNegotiatedStatus(StringUtils.convertToString(epWholeQuo.getNegotiatedStatus()));	//采方议价状态
		transfer.setNegotiatedTime(StringUtils.convertToString(epWholeQuo.getNegotiatedTime()));	//采方议价时间
		transfer.setNegotiatedCheckStatus(StringUtils.convertToString(epWholeQuo.getNegotiatedCheckStatus()));	//供方确认议价状态
		transfer.setNegotiatedCheckTime(StringUtils.convertToString(epWholeQuo.getNegotiatedCheckTime()));	//供方确认议价时间
		transfer.setCloseStatus(StringUtils.convertToString(epWholeQuo.getCloseStatus()));	//报价单关闭状态
		transfer.setSupplyCycle(StringUtils.convertToString(epWholeQuo.getSupplyCycle()));	//供货周期
		transfer.setBrand(StringUtils.convertToString(epWholeQuo.getBrand()));	//品牌
		transfer.setManufacturer(StringUtils.convertToString(epWholeQuo.getManufacturer()));	//生产厂家
		transfer.setMaterialComposition(StringUtils.convertToString(epWholeQuo.getMaterialComposition()));	//材质构成
		transfer.setWarrantyPeriod(StringUtils.convertToString(epWholeQuo.getWarrantyPeriod()));	//保质期
		transfer.setTransportationMode(StringUtils.convertToString(epWholeQuo.getTransportationMode()));	//运输方式
		transfer.setPaymentMeans(StringUtils.convertToString(epWholeQuo.getPaymentMeans()));	//交付方式
		transfer.setTaxCategory(StringUtils.convertToString(epWholeQuo.getTaxCategory()));	//税种
		transfer.setTaxRate(StringUtils.convertToString(epWholeQuo.getTaxRate()));	//税率
		transfer.setTechnologyPromises(StringUtils.convertToString(epWholeQuo.getTechnologyPromises()));	//技术承诺
		transfer.setQualityPromises(StringUtils.convertToString(epWholeQuo.getQualityPromises()));	//质量承诺
		transfer.setServicePromises(StringUtils.convertToString(epWholeQuo.getServicePromises()));	//服务承诺
		transfer.setDeliveryPromises(StringUtils.convertToString(epWholeQuo.getDeliveryPromises()));	//交期承诺
		transfer.setOtherPromises(StringUtils.convertToString(epWholeQuo.getOtherPromises()));	//其他承诺
		transfer.setCooperationQty(StringUtils.convertToString(epWholeQuo.getCooperationQty()));	//合作数量
		transfer.setCooperationStatus(StringUtils.convertToString(epWholeQuo.getCooperationStatus()));	//合作状态
		transfer.setEnquirePriceCode(StringUtils.convertToString(epWholeQuo.getEpPrice().getEnquirePriceCode()));	//询价单号
		transfer.setVendorCode(StringUtils.convertToString(epWholeQuo.getEpVendor().getVendorCode()));	//询价单供应商编号
		transfer.setVendorName(StringUtils.convertToString(epWholeQuo.getEpVendor().getVendorName()));	//询价单供应商名称
		transfer.setMaterialCode(StringUtils.convertToString(epWholeQuo.getEpMaterial().getMaterialCode()));	//询价单物料编号
		transfer.setMaterialName(StringUtils.convertToString(epWholeQuo.getEpMaterial().getMaterialName()));	//询价单物料名称
		transfer.setRequoteStatus(StringUtils.convertToString(epWholeQuo.getRequoteStatus())); 	//重新报价状态
		list.add(transfer);
		return list;
		
	}
	
	//add by yao.jin
	/**
	 * 根据供应商id、物料id查找报价单
	 * @param vendorId
	 * @param materialId
	 * @return
	 */
	public List<EpWholeQuoEntity> findByVendorAndMaterial(Long vendorId, Long materialId){
		return epWholeQuoDao.findByVendorAndMaterial(vendorId, materialId);
	}
	//end add
}
