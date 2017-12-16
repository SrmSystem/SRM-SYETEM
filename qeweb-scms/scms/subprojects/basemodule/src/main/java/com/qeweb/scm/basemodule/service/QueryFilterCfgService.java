package com.qeweb.scm.basemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.qeweb.scm.basemodule.entity.QueryFilterCfgEntity;
import com.qeweb.scm.basemodule.repository.QueryFilterCfgDao;

@Service
@Transactional
public class QueryFilterCfgService {
	
	private Log logger = LogFactory.getLog(QueryFilterCfgService.class);
	
	public static Map<String, QueryFilterCfgEntity> QUERY_FILTER_MAP = Maps.newHashMap(); 
	
	@Autowired
	private QueryFilterCfgDao queryFilterCfgDao;
	
	public void loadQueryFilterCfgList() {
		List<QueryFilterCfgEntity> list = (List<QueryFilterCfgEntity>) queryFilterCfgDao.findAll();
		if(CollectionUtils.isEmpty(list))
			return;
		String[] dataNames = null;
		String[] dataTypes = null;
		for(QueryFilterCfgEntity entity : list) {
			if(StringUtils.isEmpty(entity.getDataNames()) || StringUtils.isEmpty(entity.getDataTypes()))
				continue;
			
			dataNames = StringUtils.split(entity.getDataNames(), ",");
			dataTypes = StringUtils.split(entity.getDataTypes(), ",");
			if (dataNames.length != dataTypes.length) {
				logger.error("Can't load setting, the query setting not valid clazz:" + entity.getClazz() + ".");
				continue;
            }
			QUERY_FILTER_MAP.put(entity.getClazz(), entity);
		}
	}
	
	
}
