package com.qeweb.scm.vendormodule.utils;

import com.qeweb.scm.basemodule.constants.StatusConstant;

public class SurveyStatusUtil {

	/**
	 * 根据状态返回调查表的状态图标
	 * @param submitStatus 提交状态
	 * @param auditStatus 审核状态
	 * @return 状态图标
	 */
	public static String getSurveyStatusIcon(int submitStatus,int auditStatus){
		String icon = "icon-bullet_edit";
		if(submitStatus==StatusConstant.STATUS_YES && auditStatus==StatusConstant.STATUS_NO){
			icon = "icon-hourglass";//等待审核
		}else if(submitStatus==StatusConstant.STATUS_YES && auditStatus==StatusConstant.STATUS_REJECT){
			icon = "icon-user_cross";//审核驳回
		}else if(submitStatus==StatusConstant.STATUS_YES && auditStatus==StatusConstant.STATUS_YES){
			icon = "icon-user_tick";//审核通过
		}
		return icon;
		
		
	}
	
	
}
