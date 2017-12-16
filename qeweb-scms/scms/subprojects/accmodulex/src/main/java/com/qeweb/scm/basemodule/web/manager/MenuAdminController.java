package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.context.MessageSourceUtil;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.service.RoleService;
import com.qeweb.scm.basemodule.service.ViewService;
import com.qeweb.scm.basemodule.utils.TreeUtil;

@Controller
@RequestMapping(value="/manager/admin/menu")
public class MenuAdminController {
	
	private Map<String,Object> map = new HashMap<String, Object>();
	
	@Autowired
	private ViewService viewService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private RoleService roleService;

	@LogClass(method="查看", module="菜单管理")
	@RequestMapping(value = "goMenu",method=RequestMethod.GET)
	public String goMenu(Model model){
		return "back/menu/menuList";
	}
	
	@RequestMapping(value = "getMenuEasyuiTreeGrid")
	@ResponseBody
	public Map<String,Object> getMenuEasyuiTreeGrid(){
		map = new HashMap<String, Object>();
		List<ViewEntity> menuList = viewService.getMenuList();
		formateMessageMenuList(menuList);
		map.put("rows", menuList);
		map.put("total", menuList.size());
		return map;
	}
	
	@RequestMapping(value = "getMenuEasyuiTree")
	@ResponseBody
	public List<EasyuiTree> getMenuEasyuiTree(){
		List<ViewEntity> menuList = viewService.getAllMenuOperateList();
		List<EasyuiTree> nodeList = TreeUtil.toEasyuiTree_menu(menuList);
		formateMessage(nodeList);
		return nodeList;
	}
	
	@RequestMapping(value = "getMenuEasyuiTree/{roleId}")
	@ResponseBody
	public List<EasyuiTree> getMenuEasyuiTreeByRole(@PathVariable("roleId") Long roleId){
		RoleEntity role=roleService.getRole(roleId);
		
		List<ViewEntity> menuList = viewService.getAllMenuOperateListByRole(role.getIsVendor());
		List<EasyuiTree> nodeList = TreeUtil.toEasyuiTree_menu(menuList);
		formateMessage(nodeList);
		return nodeList;
	}
	
	/**
	 * 国际化处理
	 * @param treelist
	 */
	private void formateMessage(List<EasyuiTree> treelist) {
		if(CollectionUtils.isEmpty(treelist))
			return;
		
		for(EasyuiTree tree : treelist) {
			tree.setText(MessageSourceUtil.getMessage(tree.getText(), request));
			formateMessage(tree.getChildren());
		}
	}
	
	/**
	 * 国际化处理（对于开发的页面的菜单的国际化的处理）
	 * @param treelist
	 */
	private void formateMessageMenuList(List<ViewEntity> menulist) {
		if(CollectionUtils.isEmpty(menulist))
			return;
		
		for(ViewEntity tree : menulist) {
			tree.setViewName(MessageSourceUtil.getMessage(tree.getViewName(), request));
		}
	}
	
	@RequestMapping("getRoleMenu")
	@ResponseBody
	public List<Long> getRoleMenu(Long roleId){
		return viewService.getMenuIdListByRole(roleId);
	}
	
	@LogClass(method="保存", module="菜单保存")
	@RequestMapping(value = "saveMenu")
	@ResponseBody
	public Map<String,Object> saveMenu(@Valid ViewEntity menu){
		map = viewService.saveMenu(menu);
		return map;
	}
	
	@LogClass(method="修改", module="菜单修改")
	@RequestMapping(value = "updateMenu")
	@ResponseBody
	public Map<String,Object> updateMenu(@Valid @ModelAttribute("menu") ViewEntity menu){
		map = viewService.updateMenu(menu);
		return map;
	}
	
	@RequestMapping(value = "copyMenu")
	@ResponseBody
	public Map<String,Object> copyMenu(@Valid @ModelAttribute("menu") ViewEntity menu){
		map = viewService.copyMenu(menu);
		return map;
	}
	
	@LogClass(method="移动", module="菜单移动")
	@RequestMapping(value = "moveMenu")
	@ResponseBody
	public Map<String,Object> moveMenu(@ModelAttribute("menu") ViewEntity menu,int moveType){
		map = viewService.reSortMenu(menu, moveType);
		return map;
	}
	
	@LogClass(method="删除", module="菜单删除")
	@RequestMapping(value = "deleteMenu")
	@ResponseBody
	public Map<String,Object> moveMenu(@ModelAttribute("menu") ViewEntity menu){
		map = viewService.deleteMenu(menu);
		return map;
	}
	
	@ModelAttribute
	public void bindMenu(@RequestParam(value = "id",defaultValue = "-1") Long id,Model model){
		if(id != -1){
			model.addAttribute("menu",viewService.getMenu(id));
		}
	}
	
	
}
