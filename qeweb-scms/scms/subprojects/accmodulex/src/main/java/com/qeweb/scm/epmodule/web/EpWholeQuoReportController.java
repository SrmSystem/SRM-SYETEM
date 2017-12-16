package com.qeweb.scm.epmodule.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qeweb.scm.epmodule.service.EpWholeQuoService;

/**
 * 询比价报表Controller
 * @author u
 *
 */
@Controller
@RequestMapping(value="/manager/ep/epReport")
public class EpWholeQuoReportController {

	private Map<String,Object> map;
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	/**
	 * 供方返回到报名列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="",method = RequestMethod.GET)
	public String epWholeList(Model model) {
		return "back/ep/epWholeQuoReport";                 
	}
}
