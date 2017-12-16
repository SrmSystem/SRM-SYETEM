package com.qeweb.scm.basemodule.web.manager;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.WebSubject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.qeweb.scm.basemodule.annotation.QueryFilterRequired;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.IdEntity;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.utils.LogUtils;

@Component
@Aspect
public class CommonAspect {
	
	private static final Log _logger = LogFactory.getLog(CommonAspect.class);
	
	private GenerialDao generialDao;
	
	@AfterReturning("@annotation(com.qeweb.scm.basemodule.annotation.LogClass)")
	public void logMethod(JoinPoint joinPoint) {
		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();  
		LogUtils.saveLog(((HttpServletRequest)request), joinPoint, null, null); 
	}
	
	//标注该方法体为异常通知，当目标方法出现异常时，执行该方法体  
    @AfterThrowing(pointcut="within(com.qeweb..*) && @annotation(com.qeweb.scm.basemodule.annotation.LogClass)", throwing="ex")  
    public void addLog(JoinPoint joinPoint, Exception ex){  
    	ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();  
    	LogUtils.saveLog(((HttpServletRequest)request), joinPoint, ex, null); 
    }  
	
    /**
     * 处理业务日志记录参数
     * @param joinPoint
     */
    @AfterReturning("execution(* com.qeweb.scm.*.web.*.*Controller.*(..))")
	public void logBusMethod(JoinPoint joinPoint) {

	}
    
    
    @AfterReturning("execution(* com.qeweb.scm.*.service.*Service*.*(..))")
   	public void logBusMethodx(JoinPoint joinPoint) { 
    	try{
	    	ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest(); 
			Object[] args = joinPoint.getArgs();
	    	if(args == null || args.length == 0)
	    		return;
	    	
	    	for(Object obj : args) {
	    		if(!(obj instanceof IdEntity) && !(obj instanceof Collection)) 
	    			continue;
	    		
	    		if(obj instanceof IdEntity) {
	    			LogUtils.saveBusLog(((HttpServletRequest)request), joinPoint, (IdEntity)obj, null);
	    			continue;
	    		}
	    		
	    		for(Object o : (Collection<?>)obj){ 
	    			if(!(o instanceof IdEntity))
	    				return;
	    			
	    			LogUtils.saveBusLog(((HttpServletRequest)request), joinPoint, (IdEntity)o, null);
	    		}
	    	}
    	}catch(Exception e) {
//    		e.printStackTrace();
    	}
    }
    
    /**
	 * 数据权限过滤
	 * 
	 * @param joinPoint
	 */
	@Before(value = "@annotation(com.qeweb.scm.basemodule.annotation.QueryFilterRequired)")
	public void dataRightMethod(JoinPoint joinPoint) {
		String targetClass = joinPoint.getTarget().getClass().getName();
		HttpServletRequest request = (HttpServletRequest) ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
		// request.setAttribute("search-abc", 123);
		Method[] ms = ((JoinPoint) joinPoint).getTarget().getClass().getMethods();
		String methodName = ((JoinPoint) joinPoint).getSignature().getName();
		QueryFilterRequired filter = null;
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				filter = m.getAnnotation(QueryFilterRequired.class);
			}
		}

		String[] dataNames = filter.dataNames();
		int[] dataTypes = filter.dataTypes();
		 _logger.info("Searching query filter for '" + targetClass + "'.");
		 String hql = "from QueryFilterCfgEntity where clazz =  '" + targetClass + "' and method = '" + methodName + "'";
        /* List<QueryFilterCfgEntity> cfgList = getGenerialDao().findAllByHql(QueryFilterCfgEntity.class, hql);
		 
         //是否数据权限
         if(!CollectionUtils.isEmpty(cfgList)) {
        	 QueryFilterCfgEntity cfgEntity = cfgList.get(0);
        	 _logger.info("Searched query filter config, dataNames = '" + cfgEntity.getDataNames() + "', dataTypes = '" + cfgEntity.getDataTypes() + "' for " + targetClass);
        	 dataNames = cfgEntity.getDataNames().split(",");
        	 String[] _dataTypes = cfgEntity.getDataTypes().split(",");
        	 dataTypes = new int[_dataTypes.length];
        	 for (int j = 0; j < _dataTypes.length; j++) {
        		 dataTypes[j] = Integer.parseInt(_dataTypes[j]);
        	 }
         }*/

	}
    
    
	public GenerialDao getGenerialDao() {
		if(generialDao == null)
			generialDao = SpringContextUtils.getBean("generialDao");
		return generialDao;
	}

	public void setGenerialDao(GenerialDao generialDao) {
		this.generialDao = generialDao;
	}

}
