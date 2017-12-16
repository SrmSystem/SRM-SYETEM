package com.qeweb.scm.basemodule.web.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;

@Controller
@RequestMapping(value="/common/easyuiselect")
public class EasyUiSelectController {
	
	@RequestMapping(value = "getOrgRoleType/{isAll}",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getOrgRoleType(@PathVariable(value="isAll") boolean isAll){
		List<EasyuiComboBox> optionList = new LinkedList<EasyuiComboBox>();
		if(isAll) {
			optionList.add(new EasyuiComboBox("", "---请选择---",true));
		}
		Map<Integer,String> map = OrgType.getRoleTypeMap();
		for(Integer key : map.keySet()){
			EasyuiComboBox option = new EasyuiComboBox(String.valueOf(key), map.get(key));
			optionList.add(option);
		}
		return optionList;
	}
	
	@RequestMapping(value = "getOrgType/{isAll}",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getOrgType(@PathVariable(value="isAll") boolean isAll){
		List<EasyuiComboBox> optionList = new LinkedList<EasyuiComboBox>();
		if(isAll) {
			optionList.add(new EasyuiComboBox("", "---请选择---",true));
		}
		Map<Integer,String> map = OrgType.getOrgTypeMap();
		for(Integer key : map.keySet()){
			EasyuiComboBox option = new EasyuiComboBox(String.valueOf(key), map.get(key));
			optionList.add(option);
		}
		return optionList;
	}
	

}
