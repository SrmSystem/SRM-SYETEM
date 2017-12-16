package com.qeweb.scm.baseline.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.entity.WarnItemEntity;
import com.qeweb.scm.baseline.common.entity.WarnMainEntity;
import com.qeweb.scm.baseline.common.entity.WarnMessageEntity;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;

@Controller
@RequestMapping(value = "/manager/common/warning")
public class WarningController implements ILog {

	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private WarnMainService warnService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/common/warning";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getWarnMainList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		/*searchParamMap.put("EQ_isRead", "1");*/
		Page<WarnMainEntity> userPage = warnService.getWarnMainList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("warnMessageList")
	@ResponseBody
	public Map<String,Object> getWarnMessageList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_isRead", "1");
		Page<WarnMessageEntity> userPage = warnService.getWarnMessageList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="roleSelect")
	@ResponseBody
	public List<RoleEntity> getRoleName(){
		List<RoleEntity> list = warnService.getRoleName();
		
		return list;
	}
	
	
	
	//通过id获取单个WarnMainEntity类
	@RequestMapping(value="getMain/{id}")
	@ResponseBody
	public WarnMainEntity getMainById(@PathVariable(value="id") Long id){
		return warnService.findOne(id);
	}
	
	
	/**
	 * 
	 * @param id
	 * @param warnContent
	 * @param content
	 * @param model
	 * @param request
	 * @return 保存预警主信息
	 */
	@RequestMapping(value="saveEditWarnInfo")
	@ResponseBody
	public Map<String,Object> saveEditWarnInfo(@Valid @ModelAttribute("warnMain") WarnMainEntity warnMain){
		try{
		warnService.save(warnMain);
		map.put("success", true);
		}catch(Exception e){
			map.put("success", false);
			map.put("msg", "error");
		}
		return map;
	}
	/**
	 * 
	 * @param enableStatus
	 * @param id
	 * @return 通过禁用/启用 按钮修改主信息表的enableStatus属性；
	 */
	@RequestMapping(value="editEnableStatus/{enableStatus}/{id}")
	@ResponseBody
	public Map<String,Object> editEnableStatus(@PathVariable(value="enableStatus") int enableStatus,
			@PathVariable(value="id") Long id){
		
		WarnMainEntity warnMain = warnService.findOne(id);
		System.out.println("123");
		
		warnMain.setEnableStatus(enableStatus == 0 ? 1 : 0);
		warnService.save(warnMain);
		map.put("success", true);
		return map;
	}
	/**
	 * 
	 * @param id
	 * @param model
	 * @return 传递id值 进入晋级提醒页面
	 */
	@RequestMapping(value="promotionWarn/{mainId}")
	public String getWarnItemList(@PathVariable("mainId") Long mianId,Model model){
		model.addAttribute("mainId",mianId);
		return "back/common/promotionWarn";
	}
	
	@RequestMapping(value="getItemList/{mainId}")
	@ResponseBody
	public Map<String,Object> getItemListByMainId(@PathVariable(value = "mainId")Long mainId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_warnMainId", mainId);
		searchParamMap.put("EQ_abolished", 0);
		Page<WarnItemEntity> userPage = warnService.getItemListByMainId(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="deletePromotion/{itemId}")
	@ResponseBody
	public Map<String,Object> deletePromotion(@PathVariable(value="itemId") Long id){
		try{
		warnService.deletePromotion(id);
		map.put("sucMsg","删除晋级提醒成功");
		map.put("success", true);
		}catch(Exception e){
			map.put("msg", "删除失败");
		}
		return map;
	}
	
	@RequestMapping(value="getItem/{id}")
	@ResponseBody
	public WarnItemEntity getItem(@PathVariable(value="id") Long id){
		return warnService.findItemList(id);
	}
	
	@RequestMapping(value="updateItem")
	@ResponseBody
	public Map<String,Object> updateItem(@Valid WarnItemEntity warnItem){
		
		warnItem.setAbolished(0);
		map.put("success", true);
		map.put("sucMsg","保存晋级提醒成功");
		warnService.saveWarnItem(warnItem);
		return map;
		
	}
	
	@RequestMapping(value="addItem")
	@ResponseBody
	public Map<String,Object> saveItem(@Valid WarnItemEntity warnItem){
		try{
		map = new HashMap<String, Object>();
		System.out.println("123");
		warnService.saveWarnItem(warnItem);
		map.put("success", true);
		}catch(Exception e){
			map.put("msg","保存出现错误");
		}
			
		return map;
	}
	
	@RequestMapping(value="setIsRead/{id}")
	@ResponseBody
	public Map<String, Object> setIsRead(@PathVariable(value="id") Long id){
		map = new HashMap<String, Object>();
		warnService.closeMessage(id);
		map.put("message", "关闭消息成功");
		map.put("success", true);
		return map;
	}

	
	@RequestMapping(value="/getWarningInfo")
	@ResponseBody
	public String  getWarningInfo(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//info
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("EQ_userId", user.id + "");
		param.put("EQ_isRead", StatusConstant.STATUS_NO + "");     
		param.put("EQ_abolished", StatusConstant.STATUS_NO + "");  
		param.put("EQ_billType", "INFO");    
		List<WarnMessageEntity> warnInfoList = warnService.getWarnInfoList(param);
		
		if(CollectionUtils.isEmpty(warnInfoList))
			return "";
		
		StringBuffer sb = new StringBuffer();
		for(WarnMessageEntity warn : warnInfoList) {
			if(warn.getBillType().equals("INFO")){
				sb.append("<li id='warn_" + warn.getId() + "'><a href='javascript:;' class='easyui-linkbutton' data-options=\"iconCls:'icon-delete',plain:true\" onclick=\"operateWarn(" + warn.getId() + ")\"></a>【消息】");
				sb.append(DateUtil.dateToString(warn.getCreateTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM)).append(warn.getWarnMessage() + "");
				sb.append("【节点】： "+warn.getWarnTitle()+"</li>");
			}		
		}
		return sb.toString();
	}
	@RequestMapping(value="/getWarningWarn")
	@ResponseBody
	public String  getWarningWarn(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//warn		
		List<WarnMessageEntity> warnInfoList = warnService.getViewWarnList(user.id);

		if(CollectionUtils.isEmpty(warnInfoList))
			return "";
		
		StringBuffer sb = new StringBuffer();
		for(WarnMessageEntity warn : warnInfoList) {
             if(warn.getBillType().equals("WARN")){
				sb.append("<li id='warn_" + warn.getId() + "'><font color='red'>【预警】</font>");
				sb.append(DateUtil.dateToString(warn.getWarnTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM)).append(warn.getWarnMessage() + "");
				sb.append("【节点】： "+warn.getWarnTitle()+"</li>");
			}else if(warn.getBillType().equals("PROMOTIN_WARN")){
				sb.append("<li id='warn_" + warn.getId() + "'><font color='red'>【晋级预警】</font>");
				sb.append(DateUtil.dateToString(warn.getWarnTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM)).append(warn.getWarnMessage() + "");
				sb.append("【节点】： "+warn.getWarnTitle()+"</li>");
			}			
		}
		return sb.toString();
	}
	
	
	
	
	@Override
	public void log(Object message) {
		getLogger().log(message); 
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	@ModelAttribute
	public void bindWarnMain(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("warnMain", warnService.getWarnMain(id));
		}
	}

}
