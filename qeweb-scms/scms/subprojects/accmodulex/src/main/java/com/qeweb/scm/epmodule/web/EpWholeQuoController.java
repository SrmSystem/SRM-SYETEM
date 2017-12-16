package com.qeweb.scm.epmodule.web;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.dom4j.DocumentException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpSubQuoDao;
import com.qeweb.scm.epmodule.repository.EpWholeQuoDao;
import com.qeweb.scm.epmodule.service.EpPriceService;
import com.qeweb.scm.epmodule.service.EpVendorService;
import com.qeweb.scm.epmodule.service.EpWholeQuoHisService;
import com.qeweb.scm.epmodule.service.EpWholeQuoService;



/**
 *  整项报价controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epWholeQuo")
public class EpWholeQuoController {
	
	private static final Integer STATIC_INIT = 0 ;	
	private static final Integer STATIC_VENDOR = 1 ;	
	private static final Integer STATIC_REFUSE = -1 ;	
	private static final Integer STATIC_SUBMIT_AUDIT = 2;
	
	private Map<String,Object> map;
	
	@Autowired
	private EpPriceService epPriceService; 
	
	@Autowired
	private EpVendorService epVendorService; 
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	@Autowired
	private EpWholeQuoHisService epWholeQuoHisService;
	

	
	@Autowired
	private EpSubQuoDao epSubQuoDao;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private EpWholeQuoDao epWholeQuoDao;
	
	/**
	 * 打开供应商整项报价页面
	 * @return
	 */
	@LogClass(method="供应商整项报价",module="供应商整项报价管理")
	@RequestMapping(value = "openEpWholeQuoWin")
	public String openEpWholeQuoWin(Long epPriceId,Long epMaterialId,Long epVendorId,Integer isVendor,Model model){
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		EpWholeQuoEntity epWholeQuo = new EpWholeQuoEntity();
		if(STATIC_VENDOR.equals(isVendor)){
			epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId,STATIC_VENDOR);
			if(epWholeQuo == null){
				epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId,STATIC_INIT);
			}
		}else if(STATIC_INIT.equals(isVendor)){
			epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId,STATIC_INIT);
		}
		epWholeQuo.setTaxRate(0.17);
		model.addAttribute("epWholeQuo", epWholeQuo);
		model.addAttribute("epPrice", epPrice);
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("epMaterialId", epMaterialId);
		model.addAttribute("epVendorId", epVendorId);
		model.addAttribute("isVendor", isVendor);
		return "back/ep/epWholeQuo";       
	}
	
	/**
	 * 采购商查看整项报价详情页面
	 * @return
	 */
	@LogClass(method="整项报价详情",module="整项报价详情管理")
	@RequestMapping(value = "buyerOpenEpWholeQuoWin")
	public String buyerOpenEpWholeQuoWin(Long epPriceId,Long epMaterialId,Long epVendorId,Integer isVendor,Model model){
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		EpWholeQuoEntity epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, epMaterialId,STATIC_INIT);
		model.addAttribute("epWholeQuo", epWholeQuo);
		model.addAttribute("epPrice", epPrice);
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("epMaterialId", epMaterialId);
		model.addAttribute("epVendorId", epVendorId);
		model.addAttribute("isVendor", isVendor);
		return "back/ep/epWholeQuoItem";       
	}
	
	/**
	 * 供应商保存或提交报价单
	 * @param epWholeQuo
	 * @param type
	 * @param epWholeQuoId
	 * @param quoteTemplateFile
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@LogClass(method="整项报价",module="整项报价管理")
	@RequestMapping(value="saveEpWholeQuo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveEpWholeQuo(@Valid EpWholeQuoEntity epWholeQuo,@RequestParam("type") String type,@RequestParam("epWholeQuoId") Long epWholeQuoId,@RequestParam("quoteTemplate") MultipartFile quoteTemplateFile) throws IOException{
		map = new HashMap<String, Object>();
		EpWholeQuoEntity _epWholeQuo = new EpWholeQuoEntity();
		_epWholeQuo = epWholeQuoService.findById(epWholeQuoId);
		String quoteTemplateName = _epWholeQuo.getQuoteTemplateName();
		String quoteTemplateUrl = _epWholeQuo.getQuoteTemplateUrl();
		if (quoteTemplateFile != null && StringUtils.isNotEmpty(quoteTemplateFile.getOriginalFilename())) {
			File savefile = FileUtil.savefile(quoteTemplateFile, request.getSession().getServletContext().getRealPath("/") + "upload/");
			quoteTemplateUrl = savefile.getPath().replaceAll("\\\\", "/");
			quoteTemplateName = quoteTemplateFile.getOriginalFilename();
		}
		_epWholeQuo.setQuoteTemplateName(quoteTemplateName);	//报价附件名称
		_epWholeQuo.setQuoteTemplateUrl(quoteTemplateUrl);	//报价附件说明
		if("save".equals(type)){
			map = epWholeQuoService.saveEpWholeQuoEntity(_epWholeQuo, epWholeQuo);
		}else if("submit".equals(type)){
			map = epWholeQuoService.submitEpWholeQuoEntity(_epWholeQuo, epWholeQuo);
		}
		return map;
	}
	
	/**
	 * 采购商通过询价供应商查看整项报价信息
	 * @return
	 */
	@LogClass(method="整项报价",module="整项报价管理")
	@RequestMapping(value = "openSeeWin")
	public String openSeeWin(Long epPriceId,Long epVendorId,Integer isVendor,Model model){
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		model.addAttribute("epPrice", epPrice);
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("epVendorId", epVendorId);
		model.addAttribute("isVendor", isVendor);
		return "back/ep/epWholeQuoList";       
	}
	
	/**
	 * 获得整项报价列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="整项报价列表",module="整项报价管理")
	@RequestMapping(value="buyerGetList/{epPriceId}/{epVendorId}/{isVendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="epPriceId") String epPriceId,@PathVariable(value="epVendorId") String epVendorId,@PathVariable(value="isVendor") String isVendor,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_epPrice.id", epPriceId);
		searchParamMap.put("EQ_epVendor.id", epVendorId);
		searchParamMap.put("EQ_isVendor", isVendor);
		searchParamMap =resetSearchMap(searchParamMap,"EQ_epMaterial.id","IN_epMaterial.id");
		Page<EpWholeQuoEntity> epWholeQuoPage = epWholeQuoService.getEpWholeQuoLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="saveNegotiatedPriceBefore",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveNegotiatedPriceBefore(@RequestParam(value="datas") String epWholeDatas,HttpSession session) throws RemoteException, ServiceException{
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		JSONObject object = JSONObject.fromObject(epWholeDatas);     
		JSONArray array = (JSONArray) object.get("rows");

		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(StringUtils.convertToLong(object.get("id") + ""));
			if(epWholeQuoEntity != null){
				Double nePrice = StringUtils.convertToDouble(object.get("negotiatedPrice")+"");
				epWholeQuoEntity.setNegotiatedPrice(nePrice==null?epWholeQuoEntity.getTotalQuotePrice():nePrice);		//协商单价
				epWholeQuoEntity.setNegotiatedStatus(STATIC_VENDOR);//采方议价状态： 1=已议价
				epWholeQuoService.save(epWholeQuoEntity);
			}
		}
		map.put("success", true);
		map.put("msg", "保存成功");
		return map;
	}
	
	/**
	 * 采购商议价
	 * @param epWholeQuoList
	 * @param request
	 * @param session
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	@LogClass(method="采购商议价",module="采购商议价管理")
	@RequestMapping(value="saveNegotiatedPrice",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveNegotiatedPrice(@Valid EpVendorEntity epVendorx,HttpSession session) throws RemoteException, ServiceException{
		String epWholeDatas=epVendorx.getTableDatas();
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		JSONArray array = JSONArray.fromObject(epWholeDatas);
		
		List<EpWholeQuoEntity> wholeList=new ArrayList<EpWholeQuoEntity>();
		Map<Long, List<EpSubQuoEntity>> map1=new HashMap<Long, List<EpSubQuoEntity>>();
		for(int i= 0; i < array.size(); i ++) {
			JSONObject object = array.getJSONObject(i);
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(StringUtils.convertToLong(object.get("id") + ""));
			if(epWholeQuoEntity != null){
				epWholeQuoEntity.setNegotiatedStatus(STATIC_VENDOR);	//采方议价状态： 1=已议价
				epWholeQuoEntity.setNegotiatedTime(DateUtil.getCurrentTimestamp());	//采方议价时间
				epWholeQuoEntity.setNegotiatedUserId(user.orgId);  //采方议价人
				epWholeQuoEntity.setCooperationStatus(STATIC_VENDOR); 
				epWholeQuoEntity.setEipApprovalStatus(STATIC_SUBMIT_AUDIT);
				wholeList.add(epWholeQuoEntity);
				map1.put(epWholeQuoEntity.getId(), epSubQuoDao.findByWholeQuoId(epWholeQuoEntity.getId()));
			}
		}
	
			for (EpWholeQuoEntity epWholeQuoEntity : wholeList) {
				epWholeQuoService.save(epWholeQuoEntity);
				EpVendorEntity epVendor = epWholeQuoEntity.getEpVendor();
				epVendor.setEipApprovalStatus(STATIC_SUBMIT_AUDIT);
				epVendorService.save(epVendor);
				
				//更新整项报价历史表
				epWholeQuoHisService.saveToHis(epWholeQuoEntity);
			}
			
			map.put("success", true);
			map.put("msg", "提交审核成功");
			return map;
			
/*			for (EpWholeQuoEntity epWholeQuoEntity : wholeList) {
	
				epWholeQuoEntity.setNegotiatedStatus(STATIC_INIT);	//采方议价状态： 1=已议价
				epWholeQuoEntity.setNegotiatedTime(null);	//采方议价时间
				epWholeQuoEntity.setNegotiatedUserId(null);  //采方议价人
				epWholeQuoEntity.setCooperationStatus(STATIC_INIT); 
				epWholeQuoEntity.setEipApprovalStatus(STATIC_INIT);
				epWholeQuoService.save(epWholeQuoEntity);
			}
			map.put("success", false);
			map.put("msg", sapResult.get(1));
			return map;*/
		

		

	}
	
	
	/**
	 * 询价结果回传
	 * @throws DocumentException 
	 */
/*	@RequestMapping(value="uploadQuotePrice",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadQuotePrice(@Valid EpVendorEntity epVendorx,HttpSession session) throws RemoteException, ServiceException, DocumentException{
		String epWholeDatas=epVendorx.getTableDatas();
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		JSONArray array = JSONArray.fromObject(epWholeDatas);
		
		List<EpWholeQuoEntity> wholeList=new ArrayList<EpWholeQuoEntity>();
	
		for(int i= 0; i < array.size(); i ++) {
			JSONObject object = array.getJSONObject(i);
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(StringUtils.convertToLong(object.get("id") + ""));
			wholeList.add(epWholeQuoEntity);
		}
		EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(7645352L);
		EpWholeQuoEntity epWholeQuoEntityq = epWholeQuoService.findById(7645353L);
		wholeList.add(epWholeQuoEntity);
		wholeList.add(epWholeQuoEntityq);
		
		InterfaceWebserviceUtil util =new InterfaceWebserviceUtil();
		List<String> resx=util.createEpPrice(wholeList);
		
		StringBuilder sb=new  StringBuilder();
		if(resx!=null){
			List<List<String>> resxx= SelectionWebServiceUtil.paresResultXml(resx.get(1));
			for (List<String> listx : resxx) {
				String sign=listx.get(0);
				String message=listx.get(1);
				String qid=listx.get(2);
				EpWholeQuoEntity who=epWholeQuoDao.findOne(Long.valueOf(qid));
				if("0".equals(sign)){
					who.setEipStatus(1);
				}else{
					who.setEipStatus(-1);
					sb.append(message).append(",");
				}
				epWholeQuoDao.save(who);
			}
		}
		if(StringUtils.isEmpty(sb.toString())){
			map.put("success", true);
			map.put("msg", "回传成功");
		}else{
			map.put("success", false);
			map.put("msg", sb.toString());
		}
		
		return map;
		

		

	}*/
	
	
	/**
	 * 采购商详细页面议价
	 * @param epWholeQuoList
	 * @param request
	 * @param session
	 * @return
	 */
	@LogClass(method="采购商议价",module="采购商议价管理")
	@RequestMapping(value="saveNegotiatedPriceByItem",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveNegotiatedPriceByItem(@Valid EpWholeQuoEntity epWholeQuo,HttpSession session){
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EpWholeQuoEntity _epWholeQuo = epWholeQuoService.findById(epWholeQuo.getId());
		
		String subItems=epWholeQuo.getTableDatas();
		Double total=0d;
		//明细
		List<EpSubQuoEntity> itemList = new ArrayList<EpSubQuoEntity>();
		if(!StringUtils.isEmpty(subItems)){
			EpSubQuoEntity item = null;
			JSONObject object = JSONObject.fromObject(subItems);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new EpSubQuoEntity(); 

					if(StringUtils.isEmpty(StringUtils.convertToString(object.get("negotiatedSubTotalPrice")))){
							map.put("success", false);
							map.put("msg", "协商小计不能为空");
							return map;
					}
					item.setId(object.get("id") == null ? 0l : StringUtils.convertToLong(object.get("id") + ""));
					item.setNegotiatedSubPrice(StringUtils.convertToDouble(object.get("negotiatedSubPrice")+""));
					item.setNegotiatedSubTotalPrice(StringUtils.convertToDouble(object.get("negotiatedSubTotalPrice")+""));

					itemList.add(item);
					total=BigDecimalUtil.add(total, item.getNegotiatedSubTotalPrice());
				}
			}
		}
		if(_epWholeQuo !=null){
			if(itemList.size()>0){
				_epWholeQuo.setNegotiatedPrice(total); //议价价格
			}else{
				if(epWholeQuo.getNegotiatedPrice()==null){
					_epWholeQuo.setNegotiatedPrice(epWholeQuo.getTotalQuotePrice()); //议价价格
				}else{
					_epWholeQuo.setNegotiatedPrice(epWholeQuo.getNegotiatedPrice()); //议价价格
				}
			}
			_epWholeQuo.setPriceChangeNum(epWholeQuo.getPriceChangeNum());
			
			_epWholeQuo.setNegotiatedStatus(STATIC_VENDOR);	//采方议价状态： 1=已议价
			/*_epWholeQuo.setNegotiatedTime(DateUtil.getCurrentTimestamp());	//采方议价时间
			_epWholeQuo.setNegotiatedUserId(user.orgId);  //采方议价人
			
			_epWholeQuo.setEipApprovalStatus(STATIC_SUBMIT_AUDIT); 	//审核状态：2=提交审核
			//_epWholeQuo.setEipApprovalTime(DateUtil.getCurrentTimestamp());	//审核时间
			//_epWholeQuo.setEipApprovalUserId(user.orgId); //审核人id
			
			_epWholeQuo.setCooperationStatus(STATIC_VENDOR); //合作状态：1=已合作*/
			epWholeQuoService.savex(_epWholeQuo,itemList);
			
			
			//更新整项报价历史表
			//epWholeQuoHisService.saveToHis(_epWholeQuo);
			
		}
		map.put("msg", "提交成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 供应商分项确认议价
	 * @param epWholeQuo
	 * @param type 	(accept = 接受议价；refuse = 拒绝议价)
	 * @param epWholeQuoId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@LogClass(method="确认议价",module="确认议价管理")
	@RequestMapping(value="confirmEpWholeSubQuo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmEpWholeSubQuo(@RequestParam("type") String type,@RequestParam("epWholeQuoId") Long epWholeQuoId) throws IOException{
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EpWholeQuoEntity epWholeQuo = new EpWholeQuoEntity();
		epWholeQuo = epWholeQuoService.findById(epWholeQuoId);
		if("accept".equals(type)){
			epWholeQuo.setNegotiatedCheckStatus(STATIC_VENDOR);		//供方确认(接受)议价

			
		}else if("refuse".equals(type)){
			epWholeQuo.setNegotiatedCheckStatus(STATIC_REFUSE);		//供方拒绝议价
		}
		epWholeQuo.setNegotiatedCheckTime(DateUtil.getCurrentTimestamp()); 	//供方确认议价时间
		epWholeQuo.setNegotiatedCheckUserId(user.orgId);		//供方确认议价人id
		epWholeQuoService.save(epWholeQuo);
		
		//更新整项报价历史表
		epWholeQuoHisService.saveToHis(epWholeQuo);
		
		map.put("msg", "确认议价成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 供应商整项确认议价
	 * @param epWholeQuo
	 * @param type 	(accept = 接受议价；refuse = 拒绝议价)
	 * @param epWholeQuoId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@LogClass(method="确认议价",module="确认议价管理")
	@RequestMapping(value="confirmEpWholeQuo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmEpWholeQuo(@RequestParam("type") String type,@RequestParam("epWholeQuoId") Long epWholeQuoId) throws IOException{
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EpWholeQuoEntity epWholeQuo = new EpWholeQuoEntity();
		epWholeQuo = epWholeQuoService.findById(epWholeQuoId);
		if("accept".equals(type)){
			epWholeQuo.setNegotiatedCheckStatus(STATIC_VENDOR);		//供方确认(接受)议价

			
		}else if("refuse".equals(type)){
			epWholeQuo.setNegotiatedCheckStatus(STATIC_REFUSE);		//供方拒绝议价
		}
		epWholeQuo.setNegotiatedCheckTime(DateUtil.getCurrentTimestamp()); 	//供方确认议价时间
		epWholeQuo.setNegotiatedCheckUserId(user.orgId);		//供方确认议价人id
		epWholeQuoService.save(epWholeQuo);
		
		//更新整项报价历史表
		epWholeQuoHisService.saveToHis(epWholeQuo);
		
		map.put("msg", "确认议价成功");
		map.put("success", true);
		return map;
	}
	

	/**
	 * 根据整项报价的id查找整项报价
	 * @param epWholeQuoId
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="整项报价",module="整项报价管理")
	@RequestMapping(value="getById/{epWholeQuoId}")
	@ResponseBody
	public EpWholeQuoEntity getById(@PathVariable(value="epWholeQuoId") Long epWholeQuoId,Model model){
		EpWholeQuoEntity epWholeQuo = epWholeQuoService.findById(epWholeQuoId);
		return epWholeQuo;
	}
	
	@LogClass(method="修改报价截止时间",module="修改报价截止时间")
	@RequestMapping(value="submitQuoteEndTime")
	@ResponseBody
	public Map<String,Object> submitQuoteEndTime(@Valid EpWholeQuoEntity epWholeQuo){
		map = new HashMap<String, Object>();
		EpWholeQuoEntity oldEpWholeQuo = epWholeQuoService.findById(epWholeQuo.getId());
		if(oldEpWholeQuo !=null){
			//修改询价单中的报价截止时间及报价状态
			EpPriceEntity epPrice = oldEpWholeQuo.getEpPrice();
			map = epPriceService.submitQuoteEndTime(epPrice, epWholeQuo);
			//修改整项报价中的报价截止时间及报价状态
			map = epWholeQuoService.submitQuoteEndTime(oldEpWholeQuo, epWholeQuo);
		}else{
			map.put("msg", "提交失败！");
			map.put("success", false);
		}
		return map;
	}
	
	@LogClass(method="合作状态",module="修改合作状态")
	@RequestMapping(value="changeCooperationStatus")
	@ResponseBody
	public Map<String,Object> changeCooperationStatus(@RequestParam("epWholeQuoId") Long epWholeQuoId){
		map = new HashMap<String, Object>();
		EpWholeQuoEntity epWholeQuo = epWholeQuoService.findById(epWholeQuoId);
		if(epWholeQuo !=null){
			epWholeQuo.setCooperationStatus(STATIC_VENDOR);  //合作状态 ：1=已合作
			epWholeQuoService.save(epWholeQuo);
			epWholeQuoHisService.saveToHis(epWholeQuo);
			map.put("msg", "合作成功");
			map.put("success", true);
		}else{
			map.put("msg", "合作失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 跳转至供应商批量报价页面
	 * @return
	 */
	@LogClass(method="批量报价",module="批量报价管理")
	@RequestMapping(value = "openMassWin")
	public String openMassWin(Long epPriceId,Long epVendorId,String epMaterialIds,Model model){
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		EpVendorEntity epVendor = epVendorService.findById(epVendorId);
		model.addAttribute("epPrice", epPrice);
		model.addAttribute("epVendor", epVendor);
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("epVendorId", epVendorId);
		model.addAttribute("epMaterialIds", epMaterialIds);
		return "back/ep/epWholeQuoMass";       
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
		String valStr = (String) searchParamMap.get(key1);
		if(valStr !=null){
			List<Long> valList = new ArrayList<Long>();
			String[] val = valStr.split(" ");
			for (String string : val) {
				valList.add(StringUtils.convertToLong(string));
			}
			searchParamMap.remove(key1);
			searchParamMap.put(key2, valList);
		}
		return searchParamMap;
	}

	/**
	 * 根据询价单id、询价供应商id、询价物料id查找整项报价
	 * 用于判断是否可以批量报价
	 * @param epPriceId
	 * @param epVendorId
	 * @param epMaterialId
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="整项报价",module="整项报价管理")
	@RequestMapping(value="getByPriceAndVendorAndMaterial")
	@ResponseBody
	public List<EpWholeQuoEntity> getByPriceAndVendorAndMaterial(@RequestParam(value="epPriceId") Long epPriceId,@RequestParam(value="epVendorId") Long epVendorId,
			@RequestParam(value="epMaterialIds") String epMaterialIds,Model model){
		List<EpWholeQuoEntity> epWholeQuoList = new ArrayList<EpWholeQuoEntity>();
		String[] epMaterialString = epMaterialIds.split(" ");
		List<String> epMaterialList = Arrays.asList(epMaterialString);
		if(epMaterialList !=null && epMaterialList.size()>0){
			for (String epMaterialId : epMaterialList) {
				//查询供应商端的数据是否存在
				EpWholeQuoEntity epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId, StringUtils.convertToLong(epMaterialId), STATIC_VENDOR);
				if(epWholeQuo ==null){
					//若供应商端的数据不存在，则查询采购商端的数据
					epWholeQuo = epWholeQuoService.findByEpPriceAndEpVendorAndEpMaterial(epPriceId, epVendorId,  StringUtils.convertToLong(epMaterialId), STATIC_INIT);
				}
				epWholeQuoList.add(epWholeQuo);
			}
		}
		return epWholeQuoList;
	}

	/**
	 * 供应商保存或提交批量报价
	 * @throws IOException 
	 */
	@LogClass(method="批量报价",module="批量报价管理")
	@RequestMapping(value="saveEpWholeQuoMass",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveEpWholeQuoMass(@Valid EpWholeQuoEntity epWholeQuo,@RequestParam(value="datas") String datas,@RequestParam(value="type") String type,
			@RequestParam("quoteTemplate") MultipartFile quoteTemplateFile,HttpSession session) throws IOException{
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String quoteTemplateName = "";
		String quoteTemplateUrl = "";
		if (quoteTemplateFile != null && StringUtils.isNotEmpty(quoteTemplateFile.getOriginalFilename())) {
			File savefile = FileUtil.savefile(quoteTemplateFile, request.getServletContext().getRealPath("/") + "upload/");
			quoteTemplateUrl = savefile.getPath().replaceAll("\\\\", "/");
			quoteTemplateName = quoteTemplateFile.getOriginalFilename();
		}
		//epWholeQuo.setQuoteTemplateName(quoteTemplateName);	//报价附件名称
		//epWholeQuo.setQuoteTemplateUrl(quoteTemplateUrl);	//报价附件说明
		
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			EpWholeQuoEntity _epWholeQuo = epWholeQuoService.findById(StringUtils.convertToLong(object.get("id")+""));
			epWholeQuo.setTotalQuotePrice(StringUtils.convertToDouble(object.get("totalQuotePrice")+""));
			epWholeQuo.setQuotePrice(StringUtils.convertToDouble(object.get("quotePrice")+""));
			epWholeQuo.setSupplyCycle(StringUtils.convertToString(object.get("supplyCycle")));
			if(_epWholeQuo !=null){
				if("save".equals(type)){
					_epWholeQuo.setQuoteTemplateName(quoteTemplateName);
					_epWholeQuo.setQuoteTemplateUrl(quoteTemplateUrl);	//报价附件说明
					map = epWholeQuoService.saveEpWholeQuoEntity(_epWholeQuo, epWholeQuo);
				}else if("submit".equals(type)){
					map = epWholeQuoService.submitEpWholeQuoEntity(_epWholeQuo, epWholeQuo);
				}
			}
			
		}
		return map;
	}
}
