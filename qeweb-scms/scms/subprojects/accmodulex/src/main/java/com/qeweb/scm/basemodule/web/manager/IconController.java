package com.qeweb.scm.basemodule.web.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/public/component/icon")
public class IconController {
   
   @RequestMapping	
   @ResponseBody
   public List<String> list(Model model,HttpServletRequest request){
	   String root = request.getSession().getServletContext().getRealPath("/");
	   File iconDir = new File(root,"static/style/icons/IconsExtension");
	   if(!iconDir.exists()) {
		return null;
	}
	  String[] iconList = iconDir.list(new SuffixFileFilter(".png"));
	  List<String> list = Arrays.asList(iconList);
	  return list;
	  
   }
}
