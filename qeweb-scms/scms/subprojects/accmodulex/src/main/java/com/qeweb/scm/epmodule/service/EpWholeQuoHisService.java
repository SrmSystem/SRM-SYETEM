package com.qeweb.scm.epmodule.service;

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
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoHisEntity;
import com.qeweb.scm.epmodule.repository.EpWholeQuoHisDao;

/**
 * 整项报价历史service
 * @author u
 *
 */
@Service
@Transactional
public class EpWholeQuoHisService {

	@Autowired
	private EpWholeQuoHisDao epWholeQuoHisDao;
	
	/**
	 * 根据id查询整项报价历史
	 * @param id
	 * @return
	 */
	public EpWholeQuoHisEntity findById(Long id){
		return epWholeQuoHisDao.findOne(id);
	}
	
	/**
	 * 获取整项报价历史列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<EpWholeQuoHisEntity> getEpWholeQuoHisLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpWholeQuoHisEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpWholeQuoHisEntity.class);
		return epWholeQuoHisDao.findAll(spec, pagin);
	}
	
	/**
	 * 供应商提交整项报价单时将报价信息保存到整项报价历史表中
	 * @param epWholeQuo
	 */
	public void saveToHis(EpWholeQuoEntity epWholeQuo){
		EpWholeQuoHisEntity epWholeQuoHis = new EpWholeQuoHisEntity();
		epWholeQuoHis = epWholeQuoHisDao.findByWholeQuoIdAndQuoteCount(epWholeQuo.getId(), epWholeQuo.getQuoteCount());
		if(epWholeQuoHis == null){
			epWholeQuoHis = new EpWholeQuoHisEntity();
		}
		epWholeQuoHis.setWholeQuo(epWholeQuo);  	//整项报价
		Integer c=epWholeQuo.getQuoteCount()==null?0:epWholeQuo.getQuoteCount();
		c++;
		epWholeQuoHis.setQuoteCount(c);	//报价当前次数
		epWholeQuoHis.setQuoteEndTime(epWholeQuo.getQuoteEndTime());	//报价截止日期
		epWholeQuoHis.setApplicationStatus(epWholeQuo.getApplicationStatus());		//报名状态
		epWholeQuoHis.setQuoteStatus(epWholeQuo.getQuoteStatus());		//报价状态
		epWholeQuoHis.setQuotePrice(epWholeQuo.getQuotePrice());		//含税单价
		epWholeQuoHis.setTotalQuotePrice(epWholeQuo.getTotalQuotePrice());		//总报价(无税单价)
		epWholeQuoHis.setNegotiatedPrice(epWholeQuo.getNegotiatedPrice());		//协商无税价格
		epWholeQuoHis.setTotalNegotiatedPrice(epWholeQuo.getTotalNegotiatedPrice());		//总协商含税价格
		epWholeQuoHis.setNegotiatedStatus(epWholeQuo.getNegotiatedStatus());		//采方议价状态
		epWholeQuoHis.setNegotiatedTime(epWholeQuo.getNegotiatedTime());		//采方议价时间
		epWholeQuoHis.setNegotiatedCheckStatus(epWholeQuo.getNegotiatedCheckStatus());		//供方确认议价状态
		epWholeQuoHis.setNegotiatedCheckTime(epWholeQuo.getNegotiatedCheckTime());		//供方确认议价时间
		epWholeQuoHis.setCloseStatus(epWholeQuo.getCloseStatus());		//报价单关闭状态
		epWholeQuoHis.setCloseTime(epWholeQuo.getCloseTime());		//报价单关闭时间
		epWholeQuoHis.setSupplyCycle(epWholeQuo.getSupplyCycle());		//供货周期
		epWholeQuoHis.setBrand(epWholeQuo.getBrand());		//品牌
		epWholeQuoHis.setManufacturer(epWholeQuo.getManufacturer());		//生产厂家
		epWholeQuoHis.setMaterialComposition(epWholeQuo.getMaterialComposition());				//材质构成
		epWholeQuoHis.setWarrantyPeriod(epWholeQuo.getWarrantyPeriod());				//保质期
		epWholeQuoHis.setTransportationMode(epWholeQuo.getTransportationMode());				//运输方式
		epWholeQuoHis.setBearFreightChargesBy(epWholeQuo.getBearFreightChargesBy());				//由哪方承担运费
		epWholeQuoHis.setPaymentMeans(epWholeQuo.getPaymentMeans());				//支付方式
		epWholeQuoHis.setQuoteTemplateUrl(epWholeQuo.getQuoteTemplateUrl());				//报价附件说明
		epWholeQuoHis.setQuoteTemplateName(epWholeQuo.getQuoteTemplateName());				//报价附件名称
		epWholeQuoHis.setTaxCategory(epWholeQuo.getTaxCategory());				//税种
		epWholeQuoHis.setTaxRate(epWholeQuo.getTaxRate());				//税率
		epWholeQuoHis.setTechnologyPromises(epWholeQuo.getTechnologyPromises());				//技术承诺
		epWholeQuoHis.setQualityPromises(epWholeQuo.getQualityPromises());				//质量承诺
		epWholeQuoHis.setServicePromises(epWholeQuo.getServicePromises());				//服务承诺
		epWholeQuoHis.setDeliveryPromises(epWholeQuo.getDeliveryPromises());				//交期承诺
		epWholeQuoHis.setOtherPromises(epWholeQuo.getOtherPromises());				//其他承诺
		epWholeQuoHis.setCooperationQty(epWholeQuo.getCooperationQty());				//合作数量
		epWholeQuoHis.setCooperationStatus(epWholeQuo.getCooperationStatus());				//合作状态
		epWholeQuoHis.setRemarks(epWholeQuo.getRemarks());				//备注
		epWholeQuoHis.setEpPrice(epWholeQuo.getEpPrice());				//询价单
		epWholeQuoHis.setEpVendor(epWholeQuo.getEpVendor());				//询价单供应商
		epWholeQuoHis.setEpMaterial(epWholeQuo.getEpMaterial());				//询价单物料
		epWholeQuoHis.setRequoteStatus(epWholeQuo.getRequoteStatus());				//重新报价状态
		epWholeQuoHis.setDeliveryDays(epWholeQuo.getDeliveryDays());				//交货期(天)
		epWholeQuoHis.setNegotiatedUserId(epWholeQuo.getNegotiatedUserId());				//采方议价人
		epWholeQuoHis.setNegotiatedCheckUserId(epWholeQuo.getNegotiatedCheckUserId());				//供方确认议价人
		epWholeQuoHis.setMpq(epWholeQuo.getMpq());				//最小包装数量
		epWholeQuoHis.setMoq(epWholeQuo.getMoq());				//最小订货数量
		epWholeQuoHis.setPublishStatus(epWholeQuo.getPublishStatus());				//发布状态
		epWholeQuoHis.setPublishTime(epWholeQuo.getPublishTime());				//发布时间
		epWholeQuoHis.setPublishUserId(epWholeQuo.getPublishUserId());				//发布人id
		epWholeQuoHis.setEipApprovalStatus(epWholeQuo.getEipApprovalStatus());				//审核状态
		epWholeQuoHis.setEipApprovalTime(epWholeQuo.getEipApprovalTime());				//审核时间
		epWholeQuoHis.setEipApprovalUserId(epWholeQuo.getEipApprovalUserId());				//审核人id
		epWholeQuoHis.setCloseUserId(epWholeQuo.getCloseUserId());				//报价单关闭人
		
		//epWholeQuoHis.setIsOutData(StatusConstant.STATUS_YES);
		epWholeQuoHis.setAbolished(Constant.UNDELETE_FLAG);
		
		epWholeQuoHisDao.save(epWholeQuoHis);
	}	
	
	
	/**
	 * 更新历史表中的协商价格及协商状态
	 * @param epWholeQuo
	 */
	/*public void updateHis(EpWholeQuoEntity epWholeQuo){
		EpWholeQuoHisEntity hisEntity = epWholeQuoHisDao.findByWholeQuoIdAndQuoteCount(epWholeQuo.getId(), epWholeQuo.getQuoteCount());
		if(hisEntity !=null){
			hisEntity.setNegotiatedPrice(epWholeQuo.getNegotiatedPrice());
			hisEntity.setNegotiatedStatus(epWholeQuo.getNegotiatedStatus());
			hisEntity.setNegotiatedTime(epWholeQuo.getNegotiatedTime());
			hisEntity.setNegotiatedUserId(epWholeQuo.getNegotiatedUserId());
			hisEntity.setNegotiatedCheckStatus(epWholeQuo.getNegotiatedCheckStatus());
			hisEntity.setNegotiatedCheckTime(epWholeQuo.getNegotiatedCheckTime());
			hisEntity.setNegotiatedCheckUserId(epWholeQuo.getNegotiatedCheckUserId());
		}
	}*/
}
