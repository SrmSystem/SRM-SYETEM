package com.qeweb.scm.epmodule.web;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpPriceDao;
import com.qeweb.scm.epmodule.service.EpMaterialService;
import com.qeweb.scm.epmodule.service.EpModuleMaterialRelService;
import com.qeweb.scm.epmodule.service.EpModuleService;
import com.qeweb.scm.epmodule.service.EpPriceService;
import com.qeweb.scm.epmodule.service.EpQuoSubItemService;
import com.qeweb.scm.epmodule.service.EpQuoSubService;
import com.qeweb.scm.epmodule.service.EpVendorService;
import com.qeweb.scm.epmodule.service.EpWholeQuoService;

/**
 * 询价单实体类controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epPrice")
public class EpPriceController {
	
	public static final Integer STATUS_INIT = 0;		//新增时状态的初始值
	public static final Integer STATUS_YES = 1;		
	public static final Integer STATUS_COMPLETE = 2;	
	
	//add by yao.jin
	private static final String BUYER_MANAGER="BuyerManage";		//采购主管
	
	private Map<String,Object> map;

	@Autowired
	private EpPriceService epPriceService; 
	
	@Autowired
	private EpVendorService epVendorService; 
	
	@Autowired
	private EpMaterialService epMaterialService; 
	
	@Autowired
	private EpModuleMaterialRelService epModuleMaterialRelService; 
	
	@Autowired
	private EpModuleService epModuleService; 
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	@Autowired
	private EpQuoSubService epQuoSubService;
	
	@Autowired
	private EpQuoSubItemService epQuoSubItemService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private EpPriceDao epPriceDao;
	

	
	
	@RequestMapping("getEpPrice/{id}")
	@ResponseBody
	public EpPriceEntity getEpPrice(@PathVariable("id") Long id){
		EpPriceEntity plan=epPriceDao.findOne(id);
		return plan;
	}
	
	@RequestMapping(value = "saveEditTime",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveEditTime(@Valid EpPriceEntity m){
		map = new HashMap<String, Object>();
		epPriceService.saveEditTime(m);
		map.put("success", true);

		return map;
	}
	
	
	/**
	 * 采方返回到询价单列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		boolean isBuyManage = false;
		List<UserEntity> buyerManageList = epPriceService.findUserByRoleCode(BUYER_MANAGER);
		//判断当前登录者是否是采购主管
		if(buyerManageList!=null){
			for (UserEntity buyManager : buyerManageList) {
				if(buyManager.getId() == user.id){
					isBuyManage=true;
				}
			}
		}
		model.addAttribute("vendor", false);
		model.addAttribute("isApplication", false);	//是否报名界面：否
		model.addAttribute("isBuyManage", isBuyManage);   //是否是采购主管
		return "back/ep/epPriceList";                 
	}
	
	/**
	 * 供方返回到询价单列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="vendor",method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		model.addAttribute("isApplication", false);	//是否报名界面：否
		//return "back/ep/epPriceList";   
		return "back/ep/epVendorList";
	}
	
	/**
	 * 供方返回到报名列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="application",method = RequestMethod.GET)
	public String applicationlist(Model model) {
		model.addAttribute("vendor", true);
		model.addAttribute("isApplication", true);	//是否报名界面：是
		return "back/ep/epPriceApplicationList";                 
	}
	
	/**
	 * 获得询价单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价单列表",module="询价单管理")
	@RequestMapping(value="/{vendor}/{isApplication}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@PathVariable(value="isApplication") boolean isApplication,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_vendorCode" , "IN_id" , "vendor");//供应商编码
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_vendorName" , "IN_id" , "vendor");//供应商名称
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_materialCode" , "IN_id" , "material");//物料编码
		searchParamMap = resetSearchMap(searchParamMap, "LIKE_materialName" , "IN_id" , "material");//物料名称
		searchParamMap = initCreateTime(searchParamMap, "GTE_createTime" , 0);//开始时间
		searchParamMap = initCreateTime(searchParamMap, "LTE_createTime" , 1);//结束时间
		searchParamMap = initCreateTime(searchParamMap, "GTE_quoteEndTime" , 0);//开始时间
		searchParamMap = initCreateTime(searchParamMap, "LTE_quoteEndTime" , 1);//结束时间
		
		if(vendor){
			List<Long> bids=buyerOrgPermissionUtil.getBuyerIdsByVendor(user.orgId);
			if(bids==null||bids.size()<=0){
				searchParamMap.put("EQ_buyerId", -1L);
			}else{
				searchParamMap.put("IN_buyerId", bids);
			}
		}else{
			if(buyerOrgPermissionUtil.getBuyerIds()==null){
				searchParamMap.put("EQ_buyerId", -1L);
			}else{
				searchParamMap.put("IN_buyerId", buyerOrgPermissionUtil.getBuyerIds());
			}
		}
		if(vendor && !isApplication){
			searchParamMap.put("EQ_publishStatus", StringUtils.convertToString(STATUS_YES));
			searchParamMap.put("EQ_vendorId", StringUtils.convertToString(user.orgId));
			searchParamMap = resetSearchMap(searchParamMap, "EQ_vendorId" , "IN_id" , "vendor");//供应商id
		}else if(vendor && isApplication){
			searchParamMap.put("EQ_applicationStatus", StringUtils.convertToString(STATUS_YES));
			searchParamMap.put("EQ_publishStatus", StringUtils.convertToString(STATUS_YES));
			//若是报名界面，则询价单的参与方式为公开
			searchParamMap.put("EQ_joinWay", StringUtils.convertToString(STATUS_YES));
			searchParamMap.put("EQ_vendorId", StringUtils.convertToString(user.orgId));
			searchParamMap = resetSearchMap(searchParamMap, "EQ_vendorId" , "NIN_id" , "vendor");//供应商id
		}
		Page<EpPriceEntity> epPricePage = epPriceService.getEpPriceLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epPricePage.getContent());
		map.put("total", epPricePage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 获得报名查看处询价物料列表[供]
	 * @author chao.gu
	 * @param epPriceId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="报名查看询价物料列表",module="询价物料管理")
	@RequestMapping(value="getApplicationEpMaterialList/{epPriceId}")
	@ResponseBody
	public Map<String,Object> getApplicationEpMaterialList(@PathVariable("epPriceId") String epPriceId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_price.id", epPriceId);
		searchParamMap.put("EQ_abolished", 0);
		Page<EpMaterialEntity> epMaterialPage = epMaterialService.getEpMaterialLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epMaterialPage.getContent());
		map.put("total", epMaterialPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获得询价物料列表
	 * @param epPriceId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价物料列表",module="询价物料管理")
	@RequestMapping(value="getEpMaterialList/{epPriceId}")
	@ResponseBody
	public Map<String,Object> getEpMaterialList(@PathVariable("epPriceId") Long epPriceId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		Integer totalSize = 0;
		//add by yao.jin
		//判断当前登录者是否是供应商，若是供应商则按照询价单id、询价供应商id查询询价物料
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(OrgType.ROLE_TYPE_VENDOR.equals(user.orgRoleType)){
			EpVendorEntity epVendor = epVendorService.findByEpPriceIdAndEpVendorId(epPriceId, user.orgId);
			if(epVendor != null){
				String sql = "SELECT a.ID FROM QEWEB_EP_MATERIAL a RIGHT JOIN QEWEB_EP_WHOLE_QUO b on a.ID = b.ENQUIRE_PRICE_MATERIAL_ID where b.ENQUIRE_PRICE_ID="+epPriceId+" AND b.ENQUIRE_PRICE_VENDOR_ID="+epVendor.getId();
				epMaterialList = epMaterialService.findPageByWholeQuo(sql,pageNumber,pageSize);
				String _sql =  "SELECT COUNT(a.ID) FROM QEWEB_EP_MATERIAL a RIGHT JOIN QEWEB_EP_WHOLE_QUO b on a.ID = b.ENQUIRE_PRICE_MATERIAL_ID where b.ENQUIRE_PRICE_ID="+epPriceId+" AND b.ENQUIRE_PRICE_VENDOR_ID="+epVendor.getId();
				totalSize = epMaterialService.findCountBySql(_sql);
			}
		}else{
			String sql = "SELECT a.ID FROM　QEWEB_EP_MATERIAL a WHERE a.ENQUIRE_PRICE_ID = '"+epPriceId+"'";
			epMaterialList = epMaterialService.findPageByWholeQuo(sql,pageNumber,pageSize);
			String _sql = "SELECT COUNT(a.ID) FROM　QEWEB_EP_MATERIAL a WHERE a.ENQUIRE_PRICE_ID = '"+epPriceId+"'";
			totalSize = epMaterialService.findCountBySql(_sql);
		}
		map = new HashMap<String, Object>();
		map.put("rows", epMaterialList);
		map.put("total", totalSize);
		return map;
	}
	
	
	//add by yao.jin
	/**
	 * 询价单未发布之前的操作
	 * @param epPriceId
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价物料列表",module="询价物料管理")
	@RequestMapping(value="beforePublishMaterial/{epPriceId}")
	@ResponseBody
	public List<EpMaterialEntity> beforePublishMaterial(@PathVariable("epPriceId") Long epPriceId,Model model,ServletRequest request){
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		
		//判断当前登录者是否是供应商，若是供应商则按照询价单id、询价供应商id查询询价物料
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(OrgType.ROLE_TYPE_VENDOR.equals(user.orgRoleType)){
			EpVendorEntity epVendor = epVendorService.findByEpPriceIdAndEpVendorId(epPriceId, user.orgId);
			if(epVendor != null){
				epMaterialList = epMaterialService.findByWholeQuo(epPriceId, epVendor.getId());
			}
		}else{
			epMaterialList = epMaterialService.findByPriceId(epPriceId);
		}
		return epMaterialList;
	}
	
	/**
	 * 获得询价供应商列表
	 * @param epPriceId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价供应商列表",module="询价供应商管理")
	@RequestMapping(value="getEpVendorList/{epPriceId}")
	@ResponseBody
	public Map<String,Object> getEpVendorList(@PathVariable("epPriceId") String epPriceId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_epPrice.id", epPriceId);
		Page<EpVendorEntity> epVendorPage = epVendorService.getEpVendorLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epVendorPage.getContent());
		map.put("total", epVendorPage.getTotalElements());
		return map;
	}
	
	/**
	 * 发布询价单之前的操作 
	 * @param epPriceId
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价供应商列表",module="询价供应商管理")
	@RequestMapping(value="beforePublishVendor/{epPriceId}")
	@ResponseBody
	public List<EpVendorEntity> beforePublishVendor(@PathVariable("epPriceId") Long epPriceId,Model model,ServletRequest request){
		 List<EpVendorEntity> epVendorList = epVendorService.findByEpPriceId(epPriceId);
		if(epVendorList == null || epVendorList.size() == 0){
			epVendorList = new ArrayList<EpVendorEntity>();
		}
		return epVendorList;
	}
	
	
	
	/**
	 * 打开新增询价单页面
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="新增询价单页面",module="新增询价单管理")
	@RequestMapping(value = "openAddEpPriceWin/{epPriceId}/{type}", method = RequestMethod.GET)
	public String openAddEpPriceWin(@PathVariable("epPriceId") String epPriceId,@PathVariable("type") String type,Model model,ServletRequest request){
		EpPriceEntity newEpPrice = new EpPriceEntity();
		if(epPriceId !=null || !("0".equals(epPriceId))){
			Long id = StringUtils.convertToLong(epPriceId);
			newEpPrice = epPriceService.findById(id);
		}
		
		if(newEpPrice == null){
			newEpPrice = createEpPriceCode();
		}
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("type", type);
		model.addAttribute("newEpPrice", newEpPrice);
		return "back/ep/epPriceAdd"; 
	}

	/**
	 * 保存询价单
	 * @param epPrice
	 * @param type    (type = save 时 保存询价单 ; type = publish 时 保存并发布询价单)
	 * @param quoteSpecFile
	 * @param quoteTemplateFile
	 * @param materialdatas
	 * @param vendordatas
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@LogClass(method="询价单",module="询价单保存")
	@RequestMapping(value="saveEpPrice",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveEpPrice(@Valid EpPriceEntity epPrice,@RequestParam("type") String type,@RequestParam("deadlineVal") String applicationDeadlineVal,@RequestParam("quoteEndTimeVal") String quoteEndTimeVal,
			@RequestParam("quoteSpecFile") MultipartFile quoteSpecFile,@RequestParam("quoteTemplateFile") MultipartFile quoteTemplateFile,@RequestParam(value="materialdatas") String materialdatas,@RequestParam(value="vendordatas") String vendordatas) throws IOException{
		map = new HashMap<String, Object>();
		
		String likePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();  		//发送邮件时的网址
		List<Map<String,Object>> dataMapList = new ArrayList<Map<String,Object>>();			//用于存放按供应商维度询价时处理的数据
		List<EpMaterialEntity> epMaterialList ;
		List<EpVendorEntity> epVendorList;
		
		EpPriceEntity newEpPrice = epPriceService.findById(epPrice.getId());
		if(newEpPrice == null){
			newEpPrice = new EpPriceEntity();
			newEpPrice.setEnquirePriceCode(epPrice.getEnquirePriceCode());
			newEpPrice.setQuoteSpecFileName(epPrice.getQuoteTemplateFileName());
			newEpPrice.setQuoteSpecificationUrl(epPrice.getQuoteSpecificationUrl());
			newEpPrice.setQuoteTemplateFileName(epPrice.getQuoteTemplateFileName());
			newEpPrice.setQuoteTemplateUrl(epPrice.getQuoteTemplateUrl());
		}
		newEpPrice.setProjectName(epPrice.getProjectName());
		newEpPrice.setApplicationDeadline(DateUtil.stringToTimestamp(applicationDeadlineVal,DateUtil.DATE_FORMAT_YYYY_MM_DD));
		newEpPrice.setQuoteEndTime(DateUtil.stringToTimestamp(quoteEndTimeVal,DateUtil.DATE_FORMAT_YYYY_MM_DD));
		newEpPrice.setQuoteWay(epPrice.getQuoteWay());
		newEpPrice.setJoinWay(epPrice.getJoinWay());
		newEpPrice.setResultOpenWay(epPrice.getResultOpenWay());
		newEpPrice.setRemarks(epPrice.getRemarks());
		newEpPrice.setIsTop(epPrice.getIsTop());
		newEpPrice.setIsVendor(epPrice.getIsVendor());
		newEpPrice.setQuoteType(epPrice.getQuoteType());
		newEpPrice.setSignPerson1Id(epPrice.getSignPerson1Id());
		newEpPrice.setSignPerson2Id(epPrice.getSignPerson2Id());
		newEpPrice.setSignPerson3Id(epPrice.getSignPerson3Id());
		newEpPrice.setSignPerson4Id(epPrice.getSignPerson4Id());
		newEpPrice.setSignPerson5Id(epPrice.getSignPerson5Id());
		newEpPrice.setPriceStartTime(epPrice.getPriceStartTime());
		newEpPrice.setPriceEndTime(epPrice.getPriceEndTime());
		newEpPrice.setFactory(epPrice.getFactory());
		newEpPrice.setCheckDep(epPrice.getCheckDep());
		newEpPrice.setIsDim(epPrice.getIsDim());
		newEpPrice.setBuyerId(epPrice.getBuyerId());
		
		//报价说明书
		String quoteSpecFileName = newEpPrice.getQuoteSpecFileName();
		String quoteSpecificationUrl = newEpPrice.getQuoteSpecificationUrl();
		if (quoteSpecFile != null && StringUtils.isNotEmpty(quoteSpecFile.getOriginalFilename())) {
			File savefile = FileUtil.savefile(quoteSpecFile, request.getSession().getServletContext().getRealPath("/") + "upload/");
			quoteSpecificationUrl = savefile.getPath().replaceAll("\\\\", "/");
			quoteSpecFileName = quoteSpecFile.getOriginalFilename();
		}
		//报价模板
		String quoteTemplateFileName = newEpPrice.getQuoteTemplateFileName();
		String quoteTemplateUrl = newEpPrice.getQuoteTemplateUrl();
		if (quoteTemplateFile != null && StringUtils.isNotEmpty(quoteTemplateFile.getOriginalFilename())) {
			File savefile = FileUtil.savefile(quoteTemplateFile, request.getSession().getServletContext().getRealPath("/") + "upload/");
			quoteTemplateUrl = savefile.getPath().replaceAll("\\\\", "/");
			quoteTemplateFileName = quoteTemplateFile.getOriginalFilename();
		}
		
		newEpPrice.setQuoteSpecFileName(quoteSpecFileName);
		newEpPrice.setQuoteSpecificationUrl(quoteSpecificationUrl);
		newEpPrice.setQuoteTemplateFileName(quoteTemplateFileName);
		newEpPrice.setQuoteTemplateUrl(quoteTemplateUrl);
		if("save".equals(type)){
			newEpPrice.setPublishStatus(STATUS_INIT);				//发布状态：0=未发布
			newEpPrice.setApplicationStatus(STATUS_INIT);			//报名状态：0=未开始报名
			newEpPrice.setQuoteStatus(STATUS_INIT);					//报价状态：0=未报价
		}else if("publish".equals(type)){
			newEpPrice.setPublishStatus(STATUS_YES);				//发布状态：1=已发布
			newEpPrice.setApplicationStatus(STATUS_YES);			//报名状态：1=报名中
			newEpPrice.setQuoteStatus(STATUS_YES);						//报价状态：1=报价中
		}
		//epPriceService.savePrice(newEpPrice);
		//删除询价物料
		List<EpMaterialEntity> epMaterialList1 = epMaterialService.findByPriceId(epPrice.getId());
		for (EpMaterialEntity epMaterialEntity : epMaterialList1) {
			if(epMaterialEntity !=null){
				epMaterialService.deleteEpMaterial(epMaterialEntity);
			}
		}
		//删除询价供应商
		if(null!=vendordatas){
			List<EpVendorEntity> epVendorList1 = epVendorService.findByEpPriceId(epPrice.getId());
			for (EpVendorEntity epVendorEntity : epVendorList1) {
				if(epVendorEntity !=null){
					epVendorService.deleteEpVendor(epVendorEntity);
				}
			}
		}
		//add by yao.jin
		//如果为是按照供应商维度进行询价，则从供应商物料关系表中选择关系
		
		//供应商的数据
		JSONObject vendorObj = JSONObject.fromObject(vendordatas);    
		JSONArray vendorArr = (JSONArray) vendorObj.get("rows");
		
		//物料数据
		JSONObject materialObj = JSONObject.fromObject(materialdatas);    
		JSONArray materialArr = (JSONArray) materialObj.get("rows");
		
		
		if((STATUS_YES).equals(epPrice.getIsDim())){
/*			for(int i=0;i<vendorArr.size();i++){
				List<MaterialEntity> materialList = new ArrayList<MaterialEntity>();
				JSONObject object = vendorArr.getJSONObject(i);
				Long vendorId = Long.parseLong(object.get("vendorId") + "");
				List<SelVendorMaterialRelEntity> relList = vendorMaterialRelService.findAllByVendorIdAndAbolished(vendorId);
				handleRelData(relList, materialList);
				if(materialList == null || materialList.size()==0){
					continue;
				}else{
					JSONArray newArr = new JSONArray();
					newArr.add(object);
					Map<String , Object> childMap = new HashMap<String, Object>();
					childMap.put("vendorObject", newArr);
					childMap.put("materialList", materialList);
					dataMapList.add(childMap);
				}
			}
			
			if(dataMapList !=null && dataMapList.size()>0){
				epPriceService.savePrice(newEpPrice);
				for (Map<String,Object> dataMap : dataMapList) {
					JSONArray editArr = (JSONArray) dataMap.get("vendorObject");
					List<MaterialEntity> materialList = (List<MaterialEntity>) dataMap.get("materialList");

					//保存询价供应商
					epVendorList = epVendorService.saveVendorList(newEpPrice, vendorObj, editArr,likePath);
					//保存询价物料
					epMaterialList = epMaterialService.initMaterialData(newEpPrice, materialList);
					
					initQuoData(newEpPrice, epVendorList, epMaterialList, type);
					
				}
				
				map.put("msg", "保存成功");
				map.put("success", true);
			}else{
				map.put("msg", "没有需要询价的物料，无法创建询价单");
				map.put("success", false);
			}*/
			
			
		}else{
			epPriceService.savePrice(newEpPrice);
			
			//保存询价物料
			//epMaterialList = epMaterialService.saveMaterialList(newEpPrice, materialdatas);
			//保存询价供应商
			//epVendorList = epVendorService.saveVendorList(newEpPrice, vendordatas,likePath);
			
			//保存询价物料
			epMaterialList = epMaterialService.saveMaterialList(newEpPrice, materialObj, materialArr);
			//保存询价供应商
			epVendorList = epVendorService.saveVendorList(newEpPrice, vendorObj, vendorArr,likePath);
			
			initQuoData(newEpPrice, epVendorList, epMaterialList, type);
			map.put("msg", "保存成功");
			map.put("success", true);
			
		}
		return map;
	}
	
	
	/**
	 * 删除询价物料
	 */
	@LogClass(method="删除", module="询价物料删除")
	@RequestMapping(value = "deleteEpMaterial")
	@ResponseBody
	public Map<String,Object> deleteEpMaterial(@RequestParam("epMaterialId") Long epMaterialId){
		EpMaterialEntity epMaterial = epMaterialService.findById(epMaterialId);
		if(epMaterial !=null){
			epMaterialService.deleteEpMaterial(epMaterial);
		}
		map = new HashMap<String, Object>();
		map.put("msg", "删除成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除询价供应商
	 */
	@LogClass(method="删除", module="询价供应商删除")
	@RequestMapping(value = "deleteEpVendor")
	@ResponseBody
	public Map<String,Object> deleteEpVendor(@RequestParam("epVendorId") Long epVendorId){
		EpVendorEntity epVendor = epVendorService.findById(epVendorId);
		if(epVendor !=null){
			epVendorService.deleteEpVendor(epVendor);
		}
		map = new HashMap<String, Object>();
		map.put("msg", "删除成功");
		map.put("success", true);
		return map;
	}
	
	
	
	/**
	 * 供应商打开询价单页面
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价单页面",module="询价单管理")
	@RequestMapping(value = "openVendorEpPriceWin/{epPriceId}/{type}", method = RequestMethod.GET)
	public String openVendorEpPriceWin(@PathVariable("epPriceId") String epPriceId,@PathVariable("type") String type,Model model,ServletRequest request){
		EpPriceEntity newEpPrice = new EpPriceEntity();
		EpVendorEntity epVendor = new EpVendorEntity();
		if(epPriceId !=null || !("0".equals(epPriceId))){
			Long id = StringUtils.convertToLong(epPriceId);
			newEpPrice = epPriceService.findById(id);
		}
		
		if(newEpPrice == null){
			newEpPrice = createEpPriceCode();
		}
		
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long epVendorId = user.orgId;
		
		epVendor = epVendorService.findByEpPriceIdAndEpVendorId(StringUtils.convertToLong(epPriceId), epVendorId);
		
		long time=newEpPrice.getQuoteEndTime().getTime()+24*3600000;
		long now=DateUtil.getCurrentTimeInMillis();
		if(time<now){
			newEpPrice.setQuoteStatus(2);
		}
		
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("type", type);
		model.addAttribute("newEpPrice", newEpPrice);
		model.addAttribute("epVendor", epVendor);
		return "back/ep/epPriceItemVendor"; 
	}
	
	/**
	 * 报名查看页面【供应商】
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="报名查看询价单页面",module="询价单管理")
	@RequestMapping(value = "displayApplicationEpPrice/{epPriceId}", method = RequestMethod.GET)
	public String displayApplicationEpPrice(@PathVariable("epPriceId") String epPriceId,Model model,ServletRequest request){
		EpPriceEntity newEpPrice = null;
		if(epPriceId !=null || !("0".equals(epPriceId))){
			Long id = StringUtils.convertToLong(epPriceId);
			newEpPrice = epPriceService.findById(id);
			long time=newEpPrice.getQuoteEndTime().getTime()+24*3600000;
			long now=DateUtil.getCurrentTimeInMillis();
			if(time<now){
				newEpPrice.setQuoteStatus(2);
			}
		}
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("newEpPrice", newEpPrice);
		return "back/ep/epPriceApplicationView"; 
	}
	
	/**
	 * 供应商报名
	 */
	@LogClass(method="供应商报名", module="报名管理")
	@RequestMapping(value = "applicationEpPrice")
	@ResponseBody
	public Map<String,Object> applicationEpPrice(@RequestParam("epPriceId") Long epPriceId,Model model,ServletRequest request){
		map = new HashMap<String, Object>();
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		long time=epPrice.getApplicationDeadline().getTime()+24*3600000;
		long now=DateUtil.getCurrentTimeInMillis();
		if(time<now){
			map.put("msg", "已到报名截止时间");
			map.put("success", true);
			return map;
		}
		
		EpVendorEntity epVendor = new EpVendorEntity();
	
		List<EpMaterialEntity> epMaterialList= epMaterialService.findByPriceId(epPriceId);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		OrganizationEntity org = orgService.getOrg(user.orgId);
		if(epPrice !=null && org != null){
			epVendor = epVendorService.applicationEpPrice(epPrice, org);
			map.put("msg", "报名成功(询价单管理中报价)");
		}else{
			map.put("msg", "报名失败");
		}
		
		if(epPrice != null && epMaterialList !=null && epMaterialList.size()>0){
			epWholeQuoService.saveApplicationVendor(epPrice, epMaterialList, epVendor);
		}
		map.put("success", true);
		return map;
	}
	
	
	
	
	/**
	 * 生成询价单的流水号
	 * @return
	 */
	@LogClass(method="流水号",module="生成流水号")
	@RequestMapping(value = "createEpPriceCode")
	@ResponseBody
	public EpPriceEntity createEpPriceCode(){
		EpPriceEntity epPrice = new EpPriceEntity();
		String epPriceCode = epPriceService.createEpPriceCode();
		epPrice.setEnquirePriceCode(epPriceCode);
		return epPrice;
	} 
	
	/**
	 * 重置查询条件
	 * @param searchParamMap
	 * @param key1
	 * @param key2
	 * @param type  查询的类型
	 * @return
	 */
	private Map<String,Object> resetSearchMap(Map<String,Object> searchParamMap,String key1,String key2,String type){
		String value = (String) searchParamMap.get(key1);
		Map<String,Object> newSearchParamMap = new HashMap<String, Object>();
		List<Long> epPriceIdList = new ArrayList<Long>();
		if("vendor".equals(type) && value !=null && !("").equals(value)){
			newSearchParamMap.put(key1, value);
			epPriceIdList = epVendorService.getEpPriceIdList(newSearchParamMap);
		}else if("material".equals(type) && value !=null && !("").equals(value)){
			newSearchParamMap.put(key1, value);
			epPriceIdList = epMaterialService.getEpPriceIdList(newSearchParamMap);
		}
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
	
	
	@RequestMapping(value="getMaterialList/{buyerId}")
	@ResponseBody
	public Map<String,Object> getMaterialList(@PathVariable(value="buyerId") Long buyerId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		List<MaterialEntity> list = materialService.getMaterialListByBuyer(pageNumber,pageSize,searchParamMap,buyerId);
		map = new HashMap<String, Object>();
		map.put("rows",list);
		map.put("total",materialService.getMaterialListCount(searchParamMap,buyerId));
		return map;
	}
	
	@RequestMapping(value="getOrgList/{buyerId}")
	@ResponseBody
	public Map<String,Object> getOrgList(@PathVariable(value="buyerId") Long buyerId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "1");
		
		List<OrganizationEntity> list = orgService.getOrgsByBuyer(pageNumber,pageSize,searchParamMap,buyerId);
		map = new HashMap<String, Object>();
		map.put("rows",list);
		map.put("total",orgService.getOrgListCount(searchParamMap,buyerId));
		                              
		return map;
	}
	
	@RequestMapping(value="getUserList")
	@ResponseBody
	public Map<String,Object> getUserList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_company.roleType",OrgType.ROLE_TYPE_BUYER);
/*		if(buyerOrgPermissionUtil.getBuyerIds()!=null){
			searchParamMap.put("IN_companyId", buyerOrgPermissionUtil.getBuyerIds());
		}else{
			searchParamMap.put("EQ_id", -1);
		}*/
		Page<UserEntity> userPage = accountService.getUsers(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getUserListByRoleType/{roleType}")
	@ResponseBody
	public Map<String,Object> getUserListByRoleType(@PathVariable("roleType") String roleType,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Page<UserEntity> userPage = epPriceService.getByRoleCode(pageNumber, pageSize, roleType);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	//add by yao.jin 2016.08.19
	//增加采购主管停止报价权限，直接填写协商价，提交到BPM审批
	@RequestMapping(value="stopQuoteOpt",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> stopQuoteOpt(@RequestBody List<EpPriceEntity> epPriceList,ServletRequest request){
		map = new HashMap<String, Object>();
		if(epPriceList != null && epPriceList.size()>0){
			for (EpPriceEntity epPrice : epPriceList) {
				epPrice.setQuoteStatus(STATUS_COMPLETE);		//报价完成
				epPrice.setApplicationStatus(STATUS_COMPLETE); 	//报名完成
				epPriceService.save(epPrice);
				
				//查找所有的询价供应商，将报名状态改为已报名
				List<EpVendorEntity> epVendorList = epVendorService.findByEpPriceId(epPrice.getId());
				if(epVendorList != null && epVendorList.size()>0){
					for (EpVendorEntity epVendor : epVendorList) {
						epVendor.setApplicationStatus(STATUS_YES);	
						epVendorService.save(epVendor);
					}
				}
				
				//查找所有的整项报价单，将报价状态改为已报价
				List<EpWholeQuoEntity> epWholeList = epWholeQuoService.findByEpPrice(epPrice.getId());
				if(epWholeList != null && epWholeList.size()>0){
					for (EpWholeQuoEntity epWhole : epWholeList) {
						epWhole.setQuoteStatus(STATUS_YES);  //报价状态
						epWholeQuoService.save(epWhole);
						
						//查找是否存在分项报价实体，进行操作
						epQuoSubService.getNewSubQuo(epWhole.getId(),epWhole.getEpMaterial().getId());
					}
				}
			}
			
			map.put("msg", "操作成功");
			map.put("success", true);
		}else{
			map.put("msg", "操作失败");
			map.put("success", false);
		}
		return map;
	}

	//add by yao.jin 初始化报价的数据
	private void initQuoData(EpPriceEntity newEpPrice, List<EpVendorEntity> epVendorList, List<EpMaterialEntity> epMaterialList, String type){
		List<EpModuleItemEntity> moduleItemList = new ArrayList<EpModuleItemEntity>();
		List<EpWholeQuoEntity> epWholeQuoList = new ArrayList<EpWholeQuoEntity>();
		List<Long> epModuleIdList = new ArrayList<Long>();
		for (EpMaterialEntity epMaterialEntity : epMaterialList) {
			epModuleIdList = epModuleMaterialRelService.findModuleIdByMaterialId(epMaterialEntity.getMaterialId());
			//如根据物料的id查找不到模板，则查找默认模板 ；0=非默认模板；1=默认模板
			if(epModuleIdList.size() == 0){
				List<EpModuleEntity> epModuleList = epModuleService.findByIsDefault(STATUS_YES);
				if(epModuleList !=null && epModuleList.size()>0){
					for (EpModuleEntity epModuleEntity : epModuleList) {
						if(!(epModuleIdList.contains(epModuleEntity.getId()))){
							epModuleIdList.add(epModuleEntity.getId());
						}
					}
				}
			}
		}
		
		if(epModuleIdList != null && epModuleIdList.size()>0){
			for (Long moduleId : epModuleIdList) {
				List<EpModuleItemEntity> epModuleItemList = epModuleService.findByModuleId(moduleId);		//根据报价模型id获得报价模型明细
				if(epModuleItemList !=null && epModuleItemList.size()>0){
					moduleItemList.addAll(epModuleItemList);
				}
			}
		}

		if(newEpPrice !=null && epVendorList.size()>0 && epMaterialList.size()>0&&"publish".equals(type)){
				//保存整项报价
				epWholeQuoList = epWholeQuoService.saveEpWholeQuos(newEpPrice, epVendorList, epMaterialList);
		}
		 
		if(epMaterialList.size()>0 && moduleItemList.size() > 0){
			epQuoSubItemService.copyFromEpModuleItem(epMaterialList,moduleItemList);
			
		}
	}
	
	/**
	 * 处理供应商物料关系，根据关系中的供应商id、物料id查询是否已经存在报价单，若存在，则忽略此条数据
	 * @param relList
	 * @param materialList
	 */
/*	private void handleRelData(List<SelVendorMaterialRelEntity> relList,List<MaterialEntity> materialList){
		if(relList != null && relList.size()>0){
			for (SelVendorMaterialRelEntity relEntity : relList) {
				List<EpWholeQuoEntity> epWholeQuoList = epWholeQuoService.findByVendorAndMaterial(relEntity.getVendor().getId(), relEntity.getMaterial().getId());
				if(epWholeQuoList != null && epWholeQuoList.size()>0){
					continue;
				}else{
					materialList.add(relEntity.getMaterial());
				}
			}
		}
	}*/
	
	//end add
}
