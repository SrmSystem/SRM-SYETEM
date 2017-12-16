package com.qeweb.scm.purchasemodule.web.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.purchasemodule.entity.RemindRecordEntity;
import com.qeweb.scm.purchasemodule.repository.RemindRecordDao;

/**
 * 服务器启动初始化操作
 * @author zhangjiejun
 */
public class InitConfig {
	/**
	 * 日志工具
	 */
	public static ILogger logger = new FileLogger();
	
	/**
	 * 提醒记录Dao
	 */
	@Autowired
	private RemindRecordDao remindRecordDao;
	
	/**
	 * 提醒map
	 */
	public Map<String, Boolean> remindMap = CommonUtil.remindMap;					
	
	public void initConfig(){
		logger.log("...............................Service inited start................................");
		if(!CommonUtil.isNotNullMap(remindMap)){									//map为空，则开始初始化map
			logger.log("remindMap is null, so init data into map");
			Iterable<RemindRecordEntity> list = remindRecordDao.findAll();
			for (RemindRecordEntity remindRecordEntity : list) {
				remindMap.put(remindRecordEntity.getCode(), Boolean.valueOf(remindRecordEntity.getFlag()));
			}
			logger.log("remindMap == " + remindMap);
			logger.log("remindMap size == " + remindMap.size());
		}
		logger.log("...............................Service inited end................................");
	}
	
	public static void main(String[] args) {
		
	}
}
