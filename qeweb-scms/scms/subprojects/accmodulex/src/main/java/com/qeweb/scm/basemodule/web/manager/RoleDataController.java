package com.qeweb.scm.basemodule.web.manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.entity.RoleDataCfgEntity;
import com.qeweb.scm.basemodule.entity.RoleDataEntity;
import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.service.RoleDataService;
import com.qeweb.scm.basemodule.vo.RoleDataVO;

@Controller
@RequestMapping("/manager/admin/roledata")
public class RoleDataController {
	
	@Autowired
	private RoleDataService roleDataService;
	
	private Map<String,Object> map;
	
	/**
	 * 获取数据权限配置
	 * @param roleId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{roleId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> roleDataList(@PathVariable("roleId") long roleId, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<RoleDataCfgEntity> page = roleDataService.getRoleDataCfgList(pageNumber,pageSize,searchParamMap);
		for(RoleDataCfgEntity rdc : page.getContent()){
			rdc.setRoleId(roleId);
		} 
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getRoleDataList/{roleId}/{roleDataCfgId}", method = RequestMethod.GET)
	public String getRoleDataList(@PathVariable("roleId") long roleId, @PathVariable("roleDataCfgId") long roleDataCfgId, Model model) {
		model.addAttribute("type", "Role");
		model.addAttribute("roleId", roleId);
		model.addAttribute("roleDataCfgId", roleDataCfgId);
		RoleDataEntity roleData = roleDataService.getRoleDataEntity(roleId, roleDataCfgId);
        if(roleData != null ){
			if(!com.qeweb.scm.basemodule.utils.StringUtils.isEmpty(roleData.getDataIds())){
				model.addAttribute("dataIds", roleData.getDataIds());
			}else{
				model.addAttribute("dataIds", "-1");
			}
		}else{
			model.addAttribute("dataIds", "-1");
		}
		return "back/role/roleDataList";
	}
	
	@RequestMapping(value="getUserDataList/{userId}/{roleDataCfgId}", method = RequestMethod.GET)
	public String getUserDataList(@PathVariable("userId") long userId, @PathVariable("roleDataCfgId") long roleDataCfgId, Model model) {
		model.addAttribute("type", "User");
		model.addAttribute("roleId", userId);
		model.addAttribute("roleDataCfgId", roleDataCfgId);
		UserDataEntity userData = roleDataService.getUserDataEntity(userId, roleDataCfgId);
        if(userData != null ){
			if(!com.qeweb.scm.basemodule.utils.StringUtils.isEmpty(userData.getDataIds())){
				model.addAttribute("dataIds", userData.getDataIds());
			}else{
				model.addAttribute("dataIds", "-1");
			}
		}else{
			model.addAttribute("dataIds", "-1");
		}
		return "back/role/roleDataList";
	}
	
	@RequestMapping(value="/getDataList/{dataIds}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDataList(@RequestParam(value="page") int pageNumber,@PathVariable("dataIds") String dataIds,
			@RequestParam(value="rows") int pageSize, Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		List<RoleDataVO> arrayList = roleDataService.getRoleDataList(pageNumber,pageSize,searchParamMap);
		//进行排序
		if(!dataIds.equals("-1") ){
			String[] datas = dataIds.split(",");
			for(RoleDataVO item : arrayList ){
				item.setIsCheck(0);
				for(String data : datas){
					Long id = Long.parseLong(data);
					if(id == item.getId() ){
						item.setIsCheck(1);
						break;
					}
				}
			}
		
			Collections.sort(arrayList, new Comparator<RoleDataVO>() {  
	            public int compare(RoleDataVO o1, RoleDataVO o2) {  
	                return o2.getIsCheck().compareTo( o1.getIsCheck());  
	            }  
	        });
			
		}
	
		//假分页
		int totalcount=arrayList.size();
		int pagecount=0;
		int m=totalcount%pageSize;
		if  (m>0)
		{
		   pagecount=totalcount/pageSize+1;
		}
		else
		{
		   pagecount=totalcount/pageSize;
		}
		
		if (m==0)
		{
			if(arrayList.size() > 0){
				arrayList= arrayList.subList((pageNumber-1)*pageSize,pageSize*(pageNumber));
			}
		}
		else
		{
		   if (pageNumber==pagecount)
		   {
			   arrayList= arrayList.subList((pageNumber-1)*pageSize,totalcount);
		   }
		   else
		   {
			   arrayList= arrayList.subList((pageNumber-1)*pageSize,pageSize*(pageNumber));
		   }
		}
		PageRequest pagin = new PageRequest(pageNumber , pageSize, null);
		long total = roleDataService.getRoleDataListSize(searchParamMap);
		Page<RoleDataVO> page2=new PageImpl<RoleDataVO>(arrayList,pagin,total);
		map = new HashMap<String, Object>();
		map.put("rows",page2.getContent());
		map.put("total",total);
		return map;
	}
	
	/**
	 * 清除角色数据权限
	 * @param roleId
	 * @param roleDataCfgId
	 * @return
	 */
	@RequestMapping(value="/clearRoleData/{roleId}/{roleDataCfgId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> clearRoleData(@PathVariable("roleId") long roleId, @PathVariable("roleDataCfgId") long roleDataCfgId,ServletRequest request) {
		map = new HashMap<String, Object>();
		roleDataService.deleteRoleData(roleId, roleDataCfgId,request);
		map.put("success", true);
		map.put("msg", "清除数据权限成功");
		return map;
	}
	
	/**
	 * 清除用户数据权限
	 * @param roleId
	 * @param roleDataCfgId
	 * @return
	 */
	@RequestMapping(value="/clearUserData/{userId}/{roleDataCfgId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> clearUserData(@PathVariable("userId") long userId, @PathVariable("roleDataCfgId") long roleDataCfgId,ServletRequest request) {
		map = new HashMap<String, Object>();
		roleDataService.deleteUserData(userId, roleDataCfgId,request);
		map.put("success", true);
		map.put("msg", "清除数据权限成功");   
		return map;
	}
}
