package com.qeweb.scm.contractmodule.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;
import com.qeweb.scm.contractmodule.service.ContractItemService;


@Controller
@RequestMapping("/manager/contract/contractItem")
public class ContractItemController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private ContractItemService contractItemService;
	
	
	@RequestMapping(value="getContractItemList/{contractId}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getContractItemList(@PathVariable("contractId") Long contractId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("contractId", contractId);
		Page<ContractItemEntity> userPage = contractItemService.getList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	@Override
	public void log(Object message) {
		
	}

}



