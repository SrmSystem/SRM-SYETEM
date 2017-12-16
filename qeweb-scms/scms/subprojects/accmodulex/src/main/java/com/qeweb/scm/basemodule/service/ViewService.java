package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.constants.ViewType;
import com.qeweb.scm.basemodule.entity.RoleViewEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.RoleViewDao;
import com.qeweb.scm.basemodule.repository.ViewDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;




@Service
@Transactional
public class ViewService {
	public static String ADMIN_LOGIN_NAME = "ADMIN";
	public static List<ViewEntity> allMenus = new ArrayList<ViewEntity>();
	private static Map<Long,List<ViewEntity>> USERMENUS = new LinkedHashMap<Long, List<ViewEntity>>();

	@Autowired
	private ViewDao viewDao;
	@Autowired
	private RoleViewDao roleViewDao;
	@Autowired
	private RoleUserDao roleUserDao;
	
	private final static int MENU_UP = 0;
	private final static int MENU_DOWN = 1;

	
	/**
	 * 获取用户菜单
	 * @param userID
	 * @return
	 */
	public static List<ViewEntity> getUserViews(Long userID) {
		return USERMENUS.get(userID);
	}
	
	/**
	 * 更新用户菜单统一入口
	 * @param userId
	 * @param viewList
	 */
	public static synchronized void putUserViews(Long userId, List<ViewEntity> viewList) {
		 USERMENUS.put(userId, viewList);
	}
	
	/**
	 * 清除用户菜单缓存
	 * @param userId
	 */
	public static void removeUserViews(Long userId) {
		USERMENUS.remove(userId);
	}
	
	
	public List<ViewEntity> getMenuList(){
		List<ViewEntity> menuList = viewDao.findByViewTypeAndAbolishedOrderByMenuSnAsc(ViewType.VIEW_MENU,0);
		return menuList;
	}
	
	/**
	 * 保存菜单
	 * @return
	 */
	public Map<String,Object> saveMenu(ViewEntity menu){
		Map<String,Object> map = new HashMap<String, Object>();
		Long parentId = menu.getParentId();
		List<ViewEntity> menuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(parentId,ViewType.VIEW_MENU);
		//对新增菜单排序
		if(menu.getBeforeId()==0){
			menu.setMenuSn(menuList.size()+1);
		}else{
			reSortMenu(menuList,menu);
			viewDao.save(menuList);
		}
		viewDao.save(menu);
		map.put("success", true);
		return map;
	}


	/**
	 * 对菜单列重新排序
	 * @param menuList
	 * @param menu
	 */
	private void reSortMenu(List<ViewEntity> menuList, ViewEntity menu) {
		
		Integer newSn = null;
		for(ViewEntity oldMenu : menuList){
			//如果找到前置菜单，新菜单序号就确定了
			if(oldMenu.getId() == menu.getBeforeId().intValue()){
				newSn = oldMenu.getMenuSn()+1;
				continue;
			}
			//如果新菜单序号已经确定,后面的则都需要后延1
			if(newSn!=null){
				oldMenu.setMenuSn(oldMenu.getMenuSn()+1);
			}
		}
		//最后加入新菜单
		menu.setMenuSn(newSn);
	}

	public ViewEntity getMenu(Long id) {
		return viewDao.findOne(id);
	}

	/**
	 * 更新单个菜单，仅仅更新基本信息，不涉及顺序调换
	 * @param menu
	 */
	public Map<String,Object> updateMenu(ViewEntity menu) {
		viewDao.save(menu);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 拷贝菜单
	 * @param menu
	 */
	public Map<String,Object> copyMenu(ViewEntity menu) {
		ViewEntity menu_ = new ViewEntity();
		BeanUtil.copyPropertyNotNull(menu, menu_);
		menu_.setId(0);
		viewDao.save(menu_);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 调整顺序
	 * @param menu 要调整的菜单
	 * @param type 调整类型
	 * @return
	 */
	public Map<String,Object> reSortMenu(ViewEntity menu,int type){
		Map<String,Object> map = new HashMap<String, Object>();
		//向上移动
		if(type==MENU_UP){
			ViewEntity preMenu = viewDao.findByParentIdAndViewTypeAndMenuSn(menu.getParentId(),ViewType.VIEW_MENU,menu.getMenuSn()-1);
			if(preMenu!=null){
				menu.setMenuSn(menu.getMenuSn()-1);
				preMenu.setMenuSn(preMenu.getMenuSn()+1);
				viewDao.save(menu);
				viewDao.save(preMenu);
			}
		}else{
			ViewEntity preMenu = viewDao.findByParentIdAndViewTypeAndMenuSn(menu.getParentId(),ViewType.VIEW_MENU,menu.getMenuSn()+1);
			if(preMenu!=null){
				menu.setMenuSn(menu.getMenuSn()+1);
				preMenu.setMenuSn(preMenu.getMenuSn()-1);
				viewDao.save(menu);
				viewDao.save(preMenu);
			}
		}
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除菜单（物理删除）
	 * @param menu 菜单
	 * @return 删除结果
	 */
	public Map<String,Object> deleteMenu(ViewEntity menu){
		Map<String,Object> map = new HashMap<String, Object>();
		//先删除子系统
		deleteAll(menu);
		map.put("success", true);
		return map;
	}

	/**
	 * 递归删除菜单
	 * @param menu 要删除的菜单
	 */
	private void deleteAll(ViewEntity menu) {
		List<ViewEntity> menuList = viewDao.findByParentId(menu.getId());
		if(!CollectionUtils.isEmpty(menuList)){
			for(ViewEntity curMenu : menuList){
				deleteAll(curMenu);
			}
		}
		viewDao.delete(menu);
	}

	/**
	 * 获取当前用户的菜单
	 * @param user 用户信息
	 * @return 用户菜单信息
	 */
	public List<ViewEntity> getUserMenu(ShiroUser user) {
		List<ViewEntity> topMenuList = null;
		if(user.getRoles().equals("admin")){
			if(allMenus==null||allMenus.size()<=0){
				//获得顶级菜单
				topMenuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(0l, ViewType.VIEW_MENU);
				//递归获取子级菜单
				recursionGetMenuList(topMenuList);
				allMenus = topMenuList;
			}
			putUserViews(user.id, allMenus);
		}else{
			if(getUserViews(user.id) == null){
				putUserViews(user.id, getUserMenusByUserId(user.id));
			}
		}
		return getUserViews(user.id);  
	}

	public List<ViewEntity> getUserMenusByUserId(Long userId) {
		List<Long> roleIdList = roleUserDao.findRoleIdByUserId(userId);
		if(CollectionUtils.isEmpty(roleIdList)){
			return null;
		}
		List<Long> viewIdList = roleViewDao.findRoleMenuIds(roleIdList);
		if(CollectionUtils.isEmpty(viewIdList))
			return new ArrayList<ViewEntity>();
		
		List<ViewEntity> roleMenuList = viewDao.findByIdsAndType(viewIdList, ViewType.VIEW_MENU);
		
		
		List<ViewEntity> topList = new ArrayList<ViewEntity>();
		for(ViewEntity view : new ArrayList<ViewEntity>(roleMenuList)){
			if(view.getParentId()==0l){
				topList.add(view);
				roleMenuList.remove(view);
			}
		}
		Collections.sort(topList, new Comparator<ViewEntity>() {
			@Override
			public int compare(ViewEntity o1, ViewEntity o2) {
				return o1.getMenuSn().compareTo(o2.getMenuSn());
			}
		});
		List<ViewEntity> menuList = new ArrayList<ViewEntity>();
		for(ViewEntity roleView : topList){
			ViewEntity newView = new ViewEntity();
			BeanUtils.copyProperties(roleView, newView);
			ViewEntity menu = convertView(newView,roleMenuList);
			menuList.add(menu);
		}
		return menuList;
	}

	private ViewEntity convertView(ViewEntity pRoleView, List<ViewEntity> roleMenuList) {
		List<ViewEntity> itemList = new ArrayList<ViewEntity>();
		for(ViewEntity roleView : new ArrayList<ViewEntity>(roleMenuList)){
			if(pRoleView.getId() == roleView.getParentId()){
				ViewEntity viewItem = viewDao.findOne(roleView.getId());
				ViewEntity newItemView = new ViewEntity();
				BeanUtils.copyProperties(viewItem, newItemView);
				itemList.add(newItemView);
				roleMenuList.remove(roleView);
				convertView(newItemView,roleMenuList);  
			}
		}
		if(!CollectionUtils.isEmpty(itemList)) {
			Collections.sort(itemList, new Comparator<ViewEntity>() {
				@Override
				public int compare(ViewEntity o1, ViewEntity o2) {
					return o1.getMenuSn().compareTo(o2.getMenuSn());
				}
			});
			pRoleView.setItemList(itemList);
		}
		return pRoleView;
	}

	/**
	 * 递归用户的权限菜单
	 * @param pRoleView 顶级菜单
	 * @param roleViewList 菜单和权限集合
	 * @return 顶级菜单设置
	 */
	private ViewEntity convertView(RoleViewEntity pRoleView, List<RoleViewEntity> roleViewList) {
		ViewEntity view = viewDao.findOne(pRoleView.getViewId());
		ViewEntity newView = new ViewEntity();
		BeanUtils.copyProperties(view, newView);
		List<ViewEntity> itemList = new ArrayList<ViewEntity>();
		for(RoleViewEntity roleView : new ArrayList<RoleViewEntity>(roleViewList)){
			if(pRoleView.getViewId() == roleView.getViewPid()){
				ViewEntity viewItem = viewDao.findOne(roleView.getViewId());
				ViewEntity newItemView = new ViewEntity();
				BeanUtils.copyProperties(viewItem, newItemView);
				itemList.add(newItemView);
				roleViewList.remove(roleView);
				convertView(roleView,roleViewList);
			}
		}
		if(!CollectionUtils.isEmpty(itemList)) {
			newView.setItemList(itemList);
		}
		return newView;
	}


	/**
	 * 递归获取子级菜单
	 * @param topMenuList 父级菜单列表
	 */
	private void recursionGetMenuList(List<ViewEntity> pMenuList) {
		for(ViewEntity menu : pMenuList){
			List<ViewEntity> menuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(menu.getId(), ViewType.VIEW_MENU);
			if(!CollectionUtils.isEmpty(menuList)){
				menu.setItemList(menuList);
				recursionGetMenuList(menuList);
			}
		}
	}
	
	private void recursionGetMenuAndOperateList(List<ViewEntity> pMenuList) {
		for(ViewEntity menu : pMenuList){
			List<ViewEntity> menuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(menu.getId(), ViewType.VIEW_MENU);
			if(!CollectionUtils.isEmpty(menuList)){
				menu.setItemList(menuList);
				recursionGetMenuAndOperateList(menuList);
			}
			menuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(menu.getId(), ViewType.VIEW_OPERATE); 
			if(CollectionUtils.isEmpty(menu.getItemList())){
				menu.setItemList(menuList);
			} else {
				menu.getItemList().addAll(menuList);
			}
		}
	}

	/**
	 * 获取所有的菜单，包括子菜单
	 * @return 所有的菜单
	 */
	public List<ViewEntity> getAllMenuList() {
		//获得顶级菜单
		List<ViewEntity> topMenuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(0l, ViewType.VIEW_MENU);
		//递归获取子级菜单
		recursionGetMenuList(topMenuList);  
		return topMenuList;
	}
	
	public List<ViewEntity> getAllMenuOperateList() {
		//获得顶级菜单
		List<ViewEntity> topMenuList = viewDao.findByParentIdAndViewTypeOrderByMenuSnAsc(0l, ViewType.VIEW_MENU);
		//递归获取子级菜单
		recursionGetMenuAndOperateList(topMenuList);  
		return topMenuList;
	}

	public List<Long> getMenuIdListByRole(Long roleId) {
		return roleViewDao.findRoleMenuLIdList(roleId,ViewType.VIEW_MENU);
	}
	
	public List<ViewEntity> getAllMenuOperateListByRole(String isVendor) {
		//获得顶级菜单
		List<ViewEntity> topMenuList = viewDao.findByParentIdAndViewTypeAndIsVendorOrderByMenuSnAsc(0l, ViewType.VIEW_MENU,isVendor);
		//递归获取子级菜单
		recursionGetMenuAndOperateListByRole(topMenuList,isVendor);  
		return topMenuList;
	}
	
	private void recursionGetMenuAndOperateListByRole(List<ViewEntity> pMenuList,String isVendor) {
		for(ViewEntity menu : pMenuList){
			List<ViewEntity> menuList = viewDao.findByParentIdAndViewTypeAndIsVendorOrderByMenuSnAsc(menu.getId(), ViewType.VIEW_MENU,isVendor);
			if(!CollectionUtils.isEmpty(menuList)){
				menu.setItemList(menuList);
				recursionGetMenuAndOperateListByRole(menuList,isVendor);
			}
			menuList = viewDao.findByParentIdAndViewTypeAndIsVendorOrderByMenuSnAsc(menu.getId(), ViewType.VIEW_OPERATE,isVendor); 
			if(CollectionUtils.isEmpty(menu.getItemList())){
				menu.setItemList(menuList);
			} else {
				menu.getItemList().addAll(menuList);
			}
		}
	}
	
}
