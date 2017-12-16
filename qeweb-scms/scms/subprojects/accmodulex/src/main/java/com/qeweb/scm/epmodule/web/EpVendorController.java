package com.qeweb.scm.epmodule.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.service.EpMaterialService;
import com.qeweb.scm.epmodule.service.EpPriceService;
import com.qeweb.scm.epmodule.service.EpVendorService;

/**
 * 询价供应商controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epVendor")
public class EpVendorController {
	
	public static final Integer STATUS_INIT = 0;		//新增时状态的初始值
	public static final Integer STATUS_YES = 1;	
	public static final Integer STATUS_REFUSE = -1;
	
	private Map<String,Object> map;

	@Autowired
	private EpVendorService epVendorService; 
	
	@Autowired
	private EpMaterialService epMaterialService; 
	
	@Autowired
	private EpPriceService epPriceService;
	
	/**
	 * 获得询价单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价供应商列表",module="询价供应商管理")
	@RequestMapping(value="/{vendor}/{isApplication}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@PathVariable(value="isApplication") boolean isApplication,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_materialCode" , "IN_epPrice.id");//物料编码
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_materialName" , "IN_epPrice.id");//物料名称
		searchParamMap = initCreateTime(searchParamMap, "GTE_epPrice.createTime" , 0);//开始时间
		searchParamMap = initCreateTime(searchParamMap, "LTE_epPrice.createTime" , 1);//结束时间
		searchParamMap = initCreateTime(searchParamMap, "GTE_epPrice.quoteEndTime" , 0);//开始时间
		searchParamMap = initCreateTime(searchParamMap, "LTE_epPrice.quoteEndTime" , 1);//结束时间
		if(vendor && !isApplication){
			searchParamMap.put("NEQ_epPrice.publishStatus", StringUtils.convertToString(STATUS_INIT));
			searchParamMap.put("EQ_vendorId", StringUtils.convertToString(user.orgId));
		}
		Page<EpVendorEntity> epVendorPage = epVendorService.getEpVendorLists(pageNumber, pageSize, searchParamMap);
		List<EpVendorEntity> epVendorList = epVendorPage.getContent();
		List<EpPriceEntity> epPriceList = new ArrayList<EpPriceEntity>();
		for (EpVendorEntity epVendorEntity : epVendorList) {
			epPriceList.add(epVendorEntity.getEpPrice());
		}
		map = new HashMap<String, Object>();
		//map.put("rows", epPriceList);
		map.put("rows", epVendorPage.getContent());
		map.put("total", epVendorPage.getTotalElements());
		return map;
	}
	
	/**
	 * 供应商拒绝参与、参与
	 */
	@LogClass(method="供应商参与", module="供应商参与管理")
	@RequestMapping(value = "isTakePart")
	@ResponseBody
	public Map<String,Object> isTakePart(@RequestParam("type") String type,@RequestParam("epVendorId") Long epVendorId,Model model,ServletRequest request){
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EpVendorEntity epVendor = epVendorService.findById(epVendorId);
		EpPriceEntity epPrice;
		Integer statusVal = STATUS_INIT;
		if("refuse".equals(type)){
			statusVal = STATUS_REFUSE;
		}else if("accept".equals(type)){
			statusVal = STATUS_YES;
		}
		if(epVendor != null){
			epVendor.setApplicationStatus(statusVal);
			epVendorService.save(epVendor);
			map.put("msg", "操作成功");
		}else{
			map.put("msg", "操作失败");
		}
		map.put("success", true);
		
		//更新询价单中的报名状态
		epPrice = epVendor.getEpPrice();
		isAllApplication(epPrice);
		
		return map;
	}
	
	/**
	 * 重置查询条件
	 * @param searchParamMap
	 * @param key1
	 * @param key2
	 * @param type  查询的类型
	 * @return
	 */
	private Map<String,Object> resetSearchMap(Map<String,Object> searchParamMap,String key1,String key2){
		String value = (String) searchParamMap.get(key1);
		Map<String,Object> newSearchParamMap = new HashMap<String, Object>();
		List<Long> epPriceIdList = new ArrayList<Long>();
		newSearchParamMap.put(key1, value);
		epPriceIdList = epMaterialService.getEpPriceIdList(newSearchParamMap);
		if(value !=null && !("").equals(value)){
			if(epPriceIdList ==null || epPriceIdList.size()==0){
				epPriceIdList = new ArrayList<Long>();
				epPriceIdList.add(0L);
			}
			searchParamMap.remove(key1);
			searchParamMap.put(key2,epPriceIdList);
		}
		return searchParamMap;
	}
	
	/**
	 * 初始化查询时间成为Date类型
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	public static Map<String, Object> initCreateTime(Map<String, Object> searchParamMap, String key, int type) {
		String time = (String) searchParamMap.get(key);
		if(time != null && !("").equals(time)){			//时间不为空，则自动添加时分秒后缀
			if(type == 0){
				time = time+":00:00";
			}else if(type == 1){
				time = time+":59:59";
			}
			try {
				searchParamMap.put(key,DateUtil.parseUtilDate(time, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return searchParamMap;
	}
	
	
	//判断询价单(邀请)中的供应商是否全部参与
	private void isAllApplication(EpPriceEntity epPrice){
		List<EpVendorEntity> vendorList;
		if(epPrice !=null && epPrice.getJoinWay().equals(EpConstans.STATUS_NO)){//epPrice 不为null  并且未邀请状态
			vendorList = epVendorService.findByEpPriceId(epPrice.getId());
			if(vendorList !=null && vendorList.size()>0){
				boolean temp = epVendorService.isAllAppalication(vendorList);
				if(temp){
					epPrice.setApplicationStatus(EpConstans.QUOTE_STATUS_ALL);
				}
			}
			epPriceService.save(epPrice);
		}
	}
}
