package com.qeweb.scm.basemodule.quartz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.springframework.jdbc.core.RowMapper;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.context.ClassScanner;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.jdbc.IBaseDao;
import com.qeweb.scm.basemodule.quartz.service.SchedulerServiceImpl;
import com.qeweb.scm.basemodule.utils.ClassUtil;

public class DefaultTaskServlet extends HttpServlet {

	private static final long serialVersionUID = 4358891512968485766L;
	private final Log log = LogFactory.getLog(DefaultTaskServlet.class);
	
	private IBaseDao dao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() throws ServletException {  
		log.info(":::DefaultTaskServlet started :::");
		if(!"true".equals(PropertiesUtil.getProperty("task.enable", "true"))) { 
			log.error("task enable is " + false);
			return;  
		}
			
		Set<Class> bcs = ClassScanner.scan("com.qeweb", JobBean.class);
		String jobName = null;
		JobDetail jobDetail = null;
		Map<String, JobDetail> jobMap = new HashMap<String, JobDetail>();
		for(Class bc : bcs) {
			jobName = ClassUtil.getSimpleClassName(bc);
			jobDetail = (JobDetail)SpringContextUtils.getBean(jobName + "Detail");
			if(jobDetail == null) {
				log.error("scanne jobDetail error jobDetail is null :" + jobName);
				continue;
			}
			jobMap.put(jobName + "Detail", jobDetail);
		}
		SchedulerServiceImpl.jobDetailMap = jobMap;       
		
		String sql = "SELECT JOB_NAME FROM QRTZ_JOB_DETAILS";
		List<String> jobList = (List<String>) getDao().find(sql, new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("JOB_NAME");
			} 
		});
		
		SchedulerServiceImpl scheduler = (SchedulerServiceImpl)SpringContextUtils.getBean("schedulerService");
		boolean flage = false;
		for(Map.Entry<String, JobDetail> entry : jobMap.entrySet()) {
			for(String _jobName : jobList) {
				if(entry.getKey().equals(_jobName)) {
					flage = true;
					break;
				}
			}
			if(flage) {
				flage = false;
				continue;
			}
			log.error("add job -->" + entry.getKey() + " trigger :" + entry.getKey() + "");
			scheduler.addJob(entry.getValue().getKey().getName(), null);  
		}    
	}

	public IBaseDao getDao() {
		if(dao == null)
			dao = SpringContextUtils.getBean("jdbcDao");
		return dao;
	}

	public void setDao(IBaseDao dao) {
		this.dao = dao;
	}
	
}
