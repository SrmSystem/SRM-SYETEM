package com.qeweb.scm.basemodule.web.manager.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;

@Component
public class MaterialControllerImpl implements ILog{
	private ILogger logger = new FileLogger();
	
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MaterialTypeService materialTypeService;
	
	private Map<String,Object> map;
	
	

	@Override
	public void log(Object message) {
		getLogger().log(message);
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	public ModelAndView list(Model model) {
		ModelAndView mv = new ModelAndView("back/basedata/materialList");
		String leafLevel = PropertiesUtil.getProperty(ApplicationProConstant.MATERIALTYPE_LEAFLEVEL, "");
		String noleafAllow = PropertiesUtil.getProperty(ApplicationProConstant.MATERIALTYPE_NOLEAFLEVEL_ALLOW, "");
		mv.addObject("leafLevel", leafLevel);
		mv.addObject("noleafAllow", noleafAllow);
		return mv;
	}

	/**
	 * 物料分页查询
	 * @param pageNumber 当前页数
	 * @param pageSize 每页大小
	 * @return
	 */
	public Map<String, Object> materialList(int pageNumber, int pageSize, Model model, ServletRequest request) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialEntity> userPage = materialService.getMaterialList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
}
