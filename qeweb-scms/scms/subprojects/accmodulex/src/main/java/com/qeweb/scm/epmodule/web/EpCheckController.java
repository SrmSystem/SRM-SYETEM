package com.qeweb.scm.epmodule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpSubQuoDao;
import com.qeweb.scm.epmodule.service.EpVendorService;
import com.qeweb.scm.epmodule.service.EpWholeQuoHisService;
import com.qeweb.scm.epmodule.service.EpWholeQuoService;


/**
 *  整项报价controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epCheck")
public class EpCheckController {
	
	private static final Integer STATIC_INIT = 0 ;	
	private static final Integer STATIC_VENDOR = 1 ;	
	private static final Integer STATIC_REFUSE = -1 ;	
	private static final Integer STATIC_SUBMIT_AUDIT = 2;
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	@Autowired
	private EpSubQuoDao epSubQuoDao;
	
	@Autowired
	private EpVendorService epVendorService;
	
	@Autowired
	private EpWholeQuoHisService epWholeQuoHisService;
	
	@Autowired
	private UserDao userDao;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/ep/epCheckList";                 
	}
	
	@RequestMapping(value="noUpload",method = RequestMethod.GET)
	public String listx(Model model) {
		return "back/ep/epCheckNoUploadList";                 
	}
	
	
	@RequestMapping(value="getCheckList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_epVendor.quoteStatus", 1);
		searchParamMap.put("EQ_negotiatedStatus", 1);//查询采方已议价
		searchParamMap.put("ISNOTNULL_negotiatedPrice", 0);
		searchParamMap.put("EQ_negotiatedCheckStatus", 1);
		searchParamMap.put("EQ_eipApprovalStatus", 0);//审核未通过
		Page<EpWholeQuoEntity> epWholeQuoPage = epWholeQuoService.getEpWholeQuoLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	@RequestMapping(value="getCheckNoUploadList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getCheckNoUploadList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("LT_eipStatus", 1);
		searchParamMap.put("EQ_eipApprovalStatus", 1);
		Page<EpWholeQuoEntity> epWholeQuoPage = epWholeQuoService.getEpWholeQuoLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="openCheckWin/{param}")
	public String openCheckWin(Model model,@PathVariable("param") String param){
		model.addAttribute("paramx", param);
		return "back/ep/epCheck";
	}
	
	@RequestMapping(value = "subCheck",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> subCheck(@Valid EpVendorEntity epVendorx) throws Exception{ 
		if(epVendorx.getSignPerson1Id()!=null){
			epVendorx.setSignPerson1(userDao.findOne(epVendorx.getSignPerson1Id()));
		}
		if(epVendorx.getSignPerson2Id()!=null){
			epVendorx.setSignPerson2(userDao.findOne(epVendorx.getSignPerson2Id()));
		}
		if(epVendorx.getSignPerson3Id()!=null){
			epVendorx.setSignPerson3(userDao.findOne(epVendorx.getSignPerson3Id()));
		}
		if(epVendorx.getSignPerson4Id()!=null){
			epVendorx.setSignPerson4(userDao.findOne(epVendorx.getSignPerson4Id()));
		}
		
		String epWholeDatas=epVendorx.getTableDatas();
		String ids[]= epWholeDatas.split(",");
		map = new HashMap<String, Object>();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<Long, List<EpSubQuoEntity>> map1=new HashMap<Long, List<EpSubQuoEntity>>();
		Map<Long, List<EpWholeQuoEntity>> datas=new HashMap<Long, List<EpWholeQuoEntity>>();
		for(int i= 0; i < ids.length; i ++) {
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(StringUtils.convertToLong(ids[i]));
			epWholeQuoEntity.setNegotiatedStatus(STATIC_VENDOR);	//采方议价状态： 1=已议价
			epWholeQuoEntity.setNegotiatedTime(DateUtil.getCurrentTimestamp());	//采方议价时间
			epWholeQuoEntity.setNegotiatedUserId(user.orgId);  //采方议价人
			epWholeQuoEntity.setCooperationStatus(STATIC_VENDOR); 
			epWholeQuoEntity.setEipApprovalStatus(STATIC_SUBMIT_AUDIT);
			epWholeQuoEntity.setEipStatus(0);
			Long epVendorId=epWholeQuoEntity.getEpVendor().getId();
			if(datas.get(epVendorId)==null){
				List<EpWholeQuoEntity> wholes=new ArrayList<EpWholeQuoEntity>();
				wholes.add(epWholeQuoEntity);
				datas.put(epVendorId, wholes);
			}else{
				List<EpWholeQuoEntity> wholes=datas.get(epVendorId);
				wholes.add(epWholeQuoEntity);
				datas.put(epVendorId, wholes);
			}
			map1.put(epWholeQuoEntity.getId(), epSubQuoDao.findByWholeQuoId(epWholeQuoEntity.getId()));
		}
		
		boolean bool=true;
		StringBuilder sb=new StringBuilder();
		for (Long epVendorId : datas.keySet()) {
			List<EpWholeQuoEntity> wholeList=datas.get(epVendorId);


				for (EpWholeQuoEntity epWholeQuoEntity : wholeList) {
					epWholeQuoService.save(epWholeQuoEntity);
					EpVendorEntity epVendor = epWholeQuoEntity.getEpVendor();
					epVendor.setEipApprovalStatus(STATIC_SUBMIT_AUDIT);
					epVendorService.save(epVendor);
					//更新整项报价历史表
					epWholeQuoHisService.saveToHis(epWholeQuoEntity);
				}
		
/*				for (EpWholeQuoEntity epWholeQuoEntity : wholeList) {
		
					epWholeQuoEntity.setNegotiatedStatus(STATIC_INIT);	//采方议价状态： 1=已议价
					epWholeQuoEntity.setNegotiatedTime(null);	//采方议价时间
					epWholeQuoEntity.setNegotiatedUserId(null);  //采方议价人
					epWholeQuoEntity.setCooperationStatus(STATIC_INIT); 
					epWholeQuoEntity.setEipApprovalStatus(STATIC_INIT);
					epWholeQuoService.save(epWholeQuoEntity);
				}*/
			
		}

		
		
		map.put("success", true);
		map.put("msg", "提交审核成功");
		return map;

	}
	
	@RequestMapping(value = "validateCheck/{param}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> validateCheck(@PathVariable("param") String param) {
		String[] ids= param.split(",");
		Set<String> werkSet=new HashSet<String>();
		for (String string : ids) {
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(Long.valueOf(string));
			werkSet.add(epWholeQuoEntity.getEpMaterial().getMaterial().getCol8());
		}
		if(werkSet.size()>1){
			map.put("msg", "A类型不同的不能一起提交");
			map.put("success", false);
			return map; 
		}
		map.put("msg", "操作成功");
		map.put("success", true);
		return map;   
	}
	
	@RequestMapping(value = "submitCheck/{param}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> submitCheck(@PathVariable("param") String param) {
		String[] ids= param.split(",");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for (String string : ids) {
			EpWholeQuoEntity epWholeQuoEntity = epWholeQuoService.findById(Long.valueOf(string));
			for(int i= 0; i < ids.length; i ++) {
				//epWholeQuoEntity.setNegotiatedStatus(STATIC_VENDOR);	//采方议价状态： 1=已议价
				epWholeQuoEntity.setNegotiatedTime(DateUtil.getCurrentTimestamp());	//采方议价时间
				epWholeQuoEntity.setNegotiatedUserId(user.orgId);  //采方议价人
				epWholeQuoEntity.setCooperationStatus(STATIC_VENDOR); 
				epWholeQuoEntity.setEipApprovalStatus(STATIC_SUBMIT_AUDIT);
				epWholeQuoEntity.setEipStatus(0);
				epWholeQuoService.save(epWholeQuoEntity);
			}
	   }
		map.put("msg", "提交审核成功");
		map.put("success", true);
		return map;   
  }
	

	
	
}
