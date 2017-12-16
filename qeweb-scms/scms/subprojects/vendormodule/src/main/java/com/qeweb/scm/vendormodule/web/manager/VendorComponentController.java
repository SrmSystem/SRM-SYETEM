package com.qeweb.scm.vendormodule.web.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;

@Controller
@RequestMapping(value="/public/vendor")
public class VendorComponentController {
	
	@RequestMapping("getVendorProperties")
	@ResponseBody
	public List<EasyuiComboBox> getVendorProperties(){
		Map<String, Object> map = VendorModuleTypeConstant.getVendorPropertyMap();
		List<EasyuiComboBox> propertyList = new ArrayList<EasyuiComboBox>();
		for(String key : map.keySet()){
			EasyuiComboBox box = new EasyuiComboBox(key, (String) map.get(key));
			propertyList.add(box);
		}
		return propertyList;
	}


}
