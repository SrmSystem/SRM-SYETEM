package com.qeweb.scm.basemodule.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.QueryFilterCfgService;
import com.qeweb.scm.basemodule.service.RoleService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.service.ViewService;
import com.qeweb.scm.basemodule.utils.DateUtil;


/**
 * 系统启动初始化加载数据
 */
public class ContextInit {
	
	private Log logger = LogFactory.getLog(ContextInit.class);

	@Autowired
	private QueryFilterCfgService queryFilterCfgService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private ViewService viewService;
	@Autowired
	private UserServiceImpl userSerice;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	
	List<Long> list = new ArrayList<Long>();
	List<Long> list1 = new ArrayList<Long>();
	List<Long> list2 = new ArrayList<Long>();
	public void init() {
		logger.info("-----> 系统初始化数据加载  <-----"); 
		logger.info("1、数据权限配置加载...");
		queryFilterCfgService.loadQueryFilterCfgList();
		
		logger.info("2、组织结构树加载...");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String,Object> searchParamMap = new HashMap<String, Object>();
				searchParamMap.put("EQ_abolished", "0");
				searchParamMap.put("EQ_roleType", "4");
				searchParamMap.put("LIKE_name", "福田汽车集团");
				searchParamMap.put("EQ_parentId", 0);
				orgService.orgNodeList(searchParamMap);
			}
		});
		t.start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.warn("加载所有菜单到内存中...");
				ViewService.allMenus = viewService.getAllMenuList();
				logger.warn("所有菜单加载完成...");
			}
		}).start();
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.warn("加载所有用户对应的菜单列表...");
				long t1 = DateUtil.getCurrentTimeInMillis();
				list = userSerice.findEnableUserIds();
				/*list1 = list.subList(0, list.size()/2);
				list2 = list.subList(list.size()/2, list.size());*/
				
				for(Long userId : list){  
					if(ViewService.getUserViews(userId)==null){
						ViewService.putUserViews(userId, viewService.getUserMenusByUserId(userId));
//						logger.info("当前加载用户："+ userId +",进度:"+ViewService.userMenus.size()+"/"+ list.size()+"...1");
					}
					accountService.initUserDataRight(userId);
				}
				
				long t2 = DateUtil.getCurrentTimeInMillis();
				logger.warn("加载用户菜单列表完成...\n耗时："+(t2-t1)/1000 +"秒。");
			}
		}).start();
	}
}
