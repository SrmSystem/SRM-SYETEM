package com.qeweb.scm.epmodule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.epmodule.entity.EpMaterialEntity;
import com.qeweb.scm.epmodule.service.EpMaterialService;

/**
 * 询价供应商controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epMaterial")
public class EpMaterialController {
	
	public static final Integer STATUS_INIT = 0;		//新增时状态的初始值
	public static final Integer STATUS_YES = 1;	
	public static final Integer STATUS_REFUSE = -1;
	
	private Map<String,Object> map;
	
	@Autowired
	private EpMaterialService epMaterialService; 
	
	
	/**
	 * 获得询价物料
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="询价物料",module="获得询价物料")
	@RequestMapping(value="getEpMaterial/{epMaterialId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getEpMaterial(@PathVariable(value="epMaterialId") Long epMaterialId,Model model,ServletRequest request){
		EpMaterialEntity epMaterial = epMaterialService.findById(epMaterialId);
		List<EpMaterialEntity> epMaterialList = new ArrayList<EpMaterialEntity>();
		if(epMaterial !=null){
			epMaterialList.add(epMaterial);
		}
		map = new HashMap<String, Object>();
		map.put("rows", epMaterialList);
		map.put("total", epMaterialList.size());
		return map;
	}
	
}
