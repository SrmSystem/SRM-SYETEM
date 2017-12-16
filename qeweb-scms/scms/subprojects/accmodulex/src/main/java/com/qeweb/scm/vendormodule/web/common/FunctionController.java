package com.qeweb.scm.vendormodule.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/common/funcs")
public class FunctionController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/functions/viewServer";
	}

}
