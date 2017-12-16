package com.qeweb.scm.basemodule.web.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;
import com.qeweb.scm.basemodule.utils.TreeUtil;

@Component
public class MaterialTypeControllerImpl implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MaterialTypeService materialTypeService;
	
	@Autowired
	private BuyerOrgPermissionUtil BuyerOrgPermissionUtil;
	
	private Map<String,Object> map;
	

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	@Override
	public void log(Object message) {
		getLogger().log(message); 
	}

	/**
	 * 获取分类树
	 * @param id 父分类ＩＤ
	 * @param orgType 组织类型
	 * @return 分类树集合
	 */
	public List<EasyuiTree> getMaterialTypeTree(Long id, Integer orgType, ServletRequest request) {
		List<EasyuiTree> rootList = new ArrayList<EasyuiTree>();
		if(id==null){
			id = 0l;
		}
		List<MaterialTypeEntity> materialTypeList = materialTypeService.getMaterialListByPId(id);
		rootList = TreeUtil.toEasyuiTree_materialType(materialTypeList);
		return rootList;
	}

	
	
	
	

}
