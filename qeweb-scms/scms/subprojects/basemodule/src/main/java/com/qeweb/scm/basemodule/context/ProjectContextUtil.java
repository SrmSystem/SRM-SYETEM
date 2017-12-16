package com.qeweb.scm.basemodule.context;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.utils.StringUtils;

public class ProjectContextUtil implements ApplicationProConstant {
	
	/**
	 * 获取项目名称
	 * @return
	 */
	public static String getProjectName() {
		String projectName = PropertiesUtil.getProperty(PROJECT_NAME);
		if(StringUtils.isEmpty(projectName)){
			projectName = "快维供应链系统";
		}
		return projectName;
	} 
	
	/**
	 * 项目LOGO
	 * @return
	 */
	public static String getProjectLogo() {
		String projectLogo = PropertiesUtil.getProperty(PROJECT_LOGO);
		if(StringUtils.isEmpty(projectLogo)){
			projectLogo = "static/logo/logo.png";
		}
		return projectLogo;
	}
	/**
	 * 获取供应商阶段查询条件
	 * @return
	 */
	public static String getPhaseId(){
		String phaseIds = PropertiesUtil.getProperty("phaseIds");
		return phaseIds;
	}
}
