package com.qeweb.scm.epmodule.service;

import java.sql.Timestamp;
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

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpPriceDao;

/**
 * 询价单实体类service
 * @author u
 *
 */
@Service
@Transactional
public class EpPriceService extends BaseService{
	
	public static final Integer STATUS_INIT = 0;		//新增时状态的初始值
	public static final Integer STATUS_YES = 1;		
	public static final Integer STATUS_COMPLETE = 2;	
	public static final Integer MATERIALTYPE_NO = 0;	//无料号
	public static final Integer MATERIALTYPE_YES = 1;	//有料号
	public static final String CODE_KEY = "Ep";	

	@Autowired
	private EpPriceDao epPriceDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	/**
	 * 创建询价单的编号
	 */
	public String createEpPriceCode(){
		String epPriceCode = getSerialNumberService().geneterNextNumberByKey(CODE_KEY);
		return epPriceCode;
	}
	
	/**
	 * 保存询价单对象
	 * @param epPrice
	 */
	public EpPriceEntity save(EpPriceEntity epPrice){
		return epPriceDao.save(epPrice);
	}
	
	/**
	 * 根据id获得询价单
	 * @param id
	 * @return
	 */
	public EpPriceEntity findById(Long id){
		return epPriceDao.findOne(id);
	}
	
	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<EpPriceEntity> getEpPriceLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpPriceEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpPriceEntity.class);
		return epPriceDao.findAll(spec, pagin);
	}
	
	/**
	 * 保存询价单信息
	 * @param epPrice
	 * @return
	 */
	public EpPriceEntity savePrice(EpPriceEntity epPrice){
		epPrice.setAbolished(Constant.UNDELETE_FLAG);		
		epPrice.setAuditStatus(STATUS_INIT);				//审核状态
		epPrice.setCloseStatus(STATUS_INIT);				//关闭状态	
		epPrice.setNegotiationStatus(STATUS_INIT);			//议价状态：0=未协商；1=协商完成
		epPrice.setMaterialType(MATERIALTYPE_YES); 			//询价物料类型
		return save(epPrice);
	}
	
	/**
	 * 自动任务执行主方法
	 * 判断当前时间是否超过报价截止时间，若超过将报价状态改为报价完成
	 * @param logger 
	 * @return 执行结果状态
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger) throws Exception {
		iLogger.log("method execute start");
		Iterable<EpPriceEntity> epPriceIterable = epPriceDao.findAll();
		List<EpPriceEntity> epPriceList = new ArrayList<EpPriceEntity>();
		if(epPriceIterable !=null){
			for (EpPriceEntity epPriceEntity : epPriceIterable) {
				Long nowCurrentTime = DateUtil.getCurrentTimeInMillis();
				Long quoteEndCurrentTime = epPriceEntity.getQuoteEndTime().getTime();	//报价截止时间
				Long applicationDeadline = epPriceEntity.getApplicationDeadline().getTime(); 	//报名截止时间
				if(nowCurrentTime >= quoteEndCurrentTime){
					iLogger.log("enquirePriceCode : " + epPriceEntity.getEnquirePriceCode() +" has been more than quoteEndCurrentTime");
					epPriceEntity.setQuoteStatus(STATUS_COMPLETE);		
				}
				if(nowCurrentTime >= applicationDeadline){
					iLogger.log("enquirePriceCode : " + epPriceEntity.getEnquirePriceCode() +" has been more than applicationDeadline");
					epPriceEntity.setApplicationStatus(STATUS_COMPLETE);
					epPriceEntity.setIsOutData(1);
					epPriceEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
				}
				epPriceList.add(epPriceEntity);
			}
			iLogger.log("update epPriceList");
			epPriceDao.save(epPriceList);
		}else{
			iLogger.log("not find EpPriceEntity");
		}
		iLogger.log("method execute end");
		return true;
	}
	
	/**
	 * 采购商点击重新报价时修改报价截止时间
	 * @param epPrice
	 * @param epWholeQuo
	 */
	public Map<String,Object> submitQuoteEndTime(EpPriceEntity epPrice,EpWholeQuoEntity epWholeQuo){
		Map<String,Object> map = new HashMap<String, Object>();
		Long newQuoteEndTime = epWholeQuo.getQuoteEndTime().getTime();  //修改后的报价截止时间
		Long oldQuoteEndTime = epPrice.getQuoteEndTime().getTime();	//修改前的报价截止时间
		
		if((oldQuoteEndTime-newQuoteEndTime) >= 0){
			map.put("msg", "修改后的时间小于或等于修改前的时间，请重新修改！");
			map.put("success", false);
		}else{
			epPrice.setQuoteEndTime(epWholeQuo.getQuoteEndTime());  //报价截止时间
			epPrice.setQuoteStatus(STATUS_YES);			//报价状态：1=报价中
			save(epPrice);
			
			map.put("msg", "提交成功！");
			map.put("success", true);
		}
		return map;
	}
	
	/**
	 * 根据角色的Code查找到属于该角色的用户
	 * @param roleCode
	 * @return
	 */
	public Page<UserEntity> getByRoleCode(int pageNumber, int number,String roleCode){
		PageRequest pagin = new PageRequest(pageNumber-1, number);
		return roleUserDao.findAllByPage(pagin, roleCode);
	}
	
	//add by yao.jin
	/**
	 * 根据角色Code查找用户
	 * @param roleCode
	 * @return
	 */
	public List<UserEntity> findUserByRoleCode(String roleCode){
		return roleUserDao.findUserByRoleCode(roleCode);
	}
	//end add

	public void saveEditTime(EpPriceEntity m) {
		Timestamp quote=m.getQuoteEndTime();
		Timestamp application=m.getApplicationDeadline();
		m=epPriceDao.findOne(m.getId());
		m.setQuoteStatus(1);	
		m.setApplicationStatus(1);
		m.setQuoteEndTime(quote);
		m.setApplicationDeadline(application);
		epPriceDao.save(m);
		
	}
	
}
