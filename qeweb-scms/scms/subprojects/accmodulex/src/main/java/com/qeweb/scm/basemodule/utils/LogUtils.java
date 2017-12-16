package com.qeweb.scm.basemodule.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.BusOperateLogEntity;
import com.qeweb.scm.basemodule.entity.IdEntity;
import com.qeweb.scm.basemodule.entity.LogEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.BusOperateLogDao;
import com.qeweb.scm.basemodule.repository.LogDao;
import com.qeweb.scm.basemodule.repository.ViewDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;


/**
 * 字典工具类
 * @author ThinkGem
 * @version 2014-11-7
 */
public class LogUtils {
	
	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	public static final String CACHE_BUS_CLASS_MAP = "busClassMap";
	
	private static LogDao logDao = SpringContextUtils.getBean("logDao");
	private static ViewDao menuDao = SpringContextUtils.getBean("viewDao");
	private static BusOperateLogDao busDao = SpringContextUtils.getBean("busOperateLogDao");
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object joinPoint, Exception ex, String module){ 
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (user != null && user.id != null){
			LogEntity log = new LogEntity();
			log.setOperationName(module);
			log.setUserLogin(user.loginName);
			log.setOrgName(user.orgName);
			log.setIp(getRemoteAddr(request));
			log.setRequestUrl(request.getRequestURI()); 
			log.setOs((String)(request.getSession().getAttribute("os")));
			// 异步保存日志
			new SaveLogThread(log, joinPoint, ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private LogEntity log;
		private Object joinPoint;
		private Exception ex;
		
		public SaveLogThread(LogEntity log, Object joinPoint, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.joinPoint = joinPoint;
			this.ex = ex;
		}
		
		@Override
		public void run() {
			Method[] ms = ((JoinPoint)joinPoint).getTarget().getClass().getMethods();
			String methodName = ((JoinPoint)joinPoint).getSignature().getName();
			LogClass logClass = null;
			for (Method m : ms) {
				if (m.getName().equals(methodName)) {
					if (m.isAnnotationPresent(LogClass.class)) {
						logClass = m.getAnnotation(LogClass.class);
					}
				}
			}
			if(logClass == null)
				return;
			
			log.setContent(logClass.method());
			log.setOperationName(logClass.module());
			log.setRequestPage(((JoinPoint)joinPoint).getTarget().getClass().getName());
			if(ex != null)
				log.setException(ex.getMessage()); 
			if(StringUtils.isBlank(log.getOperationName())) {
				log.setOperationName(getMenuNamePath(log.getRequestUrl(), null));
			}
			// 保存日志信息
			logDao.save(log);
		}
	}

	public static void saveBusLog(HttpServletRequest httpServletRequest, JoinPoint joinPoint, IdEntity arg, Map<String, String> logMap) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (user != null && user.id != null){
			BusOperateLogEntity log = new BusOperateLogEntity();
			log.setRecord(arg.getId());
			List<String> filterField = Lists.newArrayList("createTime", "lastUpdateTime", "createUserId","updateUserId"
					,"createUserName", "updateUserName", "isOutData");
			for(Field field : arg.getClass().getDeclaredFields()){
				Class<?> type = field.getType();
				if(!ClassUtil.isBasicClassType(type))
					filterField.add(field.getName());
			} 
			log.setClassz(joinPoint.getTarget().getClass().getName());
			log.setOperate(joinPoint.getSignature().getName());
			log.setObjClassz(arg.getClass().getSimpleName()); 
			JsonConfig jsonConfig = getJsonConfig(filterField);
			log.setContent(JSONObject.fromObject(arg, jsonConfig).toString()); 
			log.setAbolished(0);
			log.setCreateTime(DateUtil.getCurrentTimestamp());
			log.setCreateUserName(user.name);
			new SaveBusLogThread(log).start();
		}
	}
	
	private static JsonConfig getJsonConfig(final List<String> filterField) {
		JsonConfig  jsconfig = new JsonConfig();
		jsconfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object arg0, String arg1, Object arg2) {
				for(String field : filterField) {
					if(field.equals(arg1))
						return true;
				}
				return false;
			}
		});
		return jsconfig;
	}

	public static class SaveBusLogThread extends Thread{
		private BusOperateLogEntity log;
		
		public SaveBusLogThread(BusOperateLogEntity log){
			super(SaveBusLogThread.class.getSimpleName());
			this.log = log;
		}
		
		@Override
		public void run() {
			try{
				busDao.save(log);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission){
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<ViewEntity> menuList = (List<ViewEntity>) menuDao.findAll();
			//
			for (ViewEntity menu : menuList){
				if (StringUtils.isNotBlank(menu.getViewUrl())){
					menuMap.put("/" + menu.getViewUrl(), menu.getViewName());
				}
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(requestUri + "/");
		if (menuNamePath == null){
//			for (String p : StringUtils.split(permission)){
//				menuNamePath = menuMap.get(p);
//				if (StringUtils.isNotBlank(menuNamePath)){
//					break;
//				}
//			}
//			if (menuNamePath == null){
//				return "";
//			}
		}
		return menuNamePath;
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isEmpty(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isEmpty(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
}
