package com.qeweb.scm.basemodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.context.MessageSourceUtil;
import com.qeweb.scm.basemodule.entity.BacklogCfgEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.ViewService;

@Controller
@RequestMapping("/manager/backlog")
public class BacklogController {
	
	@Autowired
	private BacklogService service;
	
	@Autowired
	private ViewService viewService;
	
	private Map<String,Object> map;
	
	@Autowired
	HttpSession session;
	
	@LogClass(method="查看", module="待办管理")
	@RequestMapping(method = RequestMethod.GET)
	public String cfgList(Model model) {
		return "back/backlog/cfgList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cfgList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> queryParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<BacklogCfgEntity> page = service.getCfgList(pageNumber,pageSize,queryParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping("list")
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> queryParamMap = Servlets.getParametersStartingWith(request, "search-");
		List<ViewEntity> menuList = viewService.getUserMenu(user);
		List<Long> viewIdList = new ArrayList<Long>();
		viewIdList.add(-1l);
		if (!CollectionUtils.isEmpty(menuList)){
			for(ViewEntity view : menuList) {
				getUserViewIds(viewIdList, view);
			}
		}
		queryParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + "");
		queryParamMap.put("IN_viewId", viewIdList);
		queryParamMap.put("EQ_orgRoleType", user.orgRoleType + ""); //组织roletype
		Map<String,String> systemMap = new HashMap<String, String>();
		systemMap.put("#currentUserId#", user.id+"");
		systemMap.put("#currentOrgId#", user.orgId+"");
		Page<BacklogCfgEntity> page = service.getList(pageNumber,pageSize,queryParamMap,systemMap,user.orgRoleType);
		//国际化处理
		for(BacklogCfgEntity back : page.getContent()) {
			back.setViewName(MessageSourceUtil.getMessage(back.getViewName(), (HttpServletRequest)request));
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping("getBacklog/{id}")
	@ResponseBody
	public BacklogCfgEntity getBacklog(@PathVariable("id") Long id){
		return service.getCfg(id);
	}
	
	private void getUserViewIds(List<Long> viewIdList, ViewEntity view) {
		viewIdList.add(view.getId());
		if(CollectionUtils.isEmpty(view.getItemList()))
			return;
		
		for(ViewEntity child : view.getItemList()) {
			getUserViewIds(viewIdList, child);
		}
	}
	
	/**
	 * 新增待办配置
	 * @param backlogCfgEntity
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Map<String,Object> add(BacklogCfgEntity backlogCfgEntity,Model model,ServletRequest request){
		if(backlogCfgEntity.getId() > 0l) {
			BacklogCfgEntity entity = service.getCfg(backlogCfgEntity.getId());
			entity.setViewId(backlogCfgEntity.getViewId());
			entity.setOrgRoleType(backlogCfgEntity.getOrgRoleType());
			entity.setContent(backlogCfgEntity.getContent());
			entity.setQueryHql(backlogCfgEntity.getQueryHql());
			entity.setQuerySql(backlogCfgEntity.getQuerySql()); 
			map = service.add(entity);
		} else {
			map = service.add(backlogCfgEntity);
		}
		return map;
	}
	
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@RequestBody List<BacklogCfgEntity> roleList){
		Map<String,Object> map = service.delete(roleList);
		map.put("msg", "删除代办配置成功");
		return map;
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindCfg(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("cfg", service.getCfg(id));
		}
	}
	
	

}
