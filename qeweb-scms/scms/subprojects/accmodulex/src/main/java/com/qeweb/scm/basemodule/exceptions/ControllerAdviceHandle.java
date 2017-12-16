package com.qeweb.scm.basemodule.exceptions;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomTimestampEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

/**
 * 主要用来做异常处理，所有的controller都会被该类拦截
 * @author pjjxiajun
 * @date 2015年3月11日
 * @path com.qeweb.scm.baseweb.web.utils.com.qeweb.scm.baseweb.web.utils
 */
@ControllerAdvice
public class ControllerAdviceHandle {
	
	Logger logger = LoggerFactory.getLogger(ControllerAdviceHandle.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder){
		SimpleDateFormat fmt1 = new SimpleDateFormat(DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);  
		SimpleDateFormat fmt2 = new SimpleDateFormat(DateUtil.DATE_FORMAT_YYYY_MM_DD);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(fmt1, true));   //true:允许输入空值，false:不能为空值
		binder.registerCustomEditor(Date.class, new CustomDateEditor(fmt2, true));
		
		binder.registerCustomEditor(Timestamp.class, new CustomTimestampEditor(fmt1, true));
		binder.registerCustomEditor(Timestamp.class, new CustomTimestampEditor(fmt2, true));
	}
	
	
	/**
	 * 异常处理，用来处理ajax异常和普通异常
	 * ajax异常可以通过@ResponseBody返回
	 * 普通异常，继续抛出，那么会被SimpleMappingExceptionResolver处理
	 * @param request
	 * @param reponse
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler
	@ResponseBody
	public Map<String,Object> handlerException(HttpServletRequest request, HttpServletResponse reponse,Exception e) throws Exception{
		Map<String,Object> errorMap = new HashMap<String, Object>();
		errorMap.put("success", false);
		errorMap.put("error", true);
		errorMap.put("msg", e.getMessage());
		return errorMap;
	}
	
	/**
	 * 文件过大的异常
	 * return
	 */
	@ExceptionHandler({MaxUploadSizeExceededException.class})
	@ResponseBody
	public Map<String, Object> maxUploadSizeExceededException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		String msg = e.getMessage();
		Double size1 = 0D;
		Double size2 = 0D;
		Pattern p = Pattern.compile("\\([\\w\\!\\s]+\\)");
		Matcher m = p.matcher(msg);
		String value = "";
		List<String> valueStr = new ArrayList<String>();
		while (m.find()) {
			value = m.group().replace("(", "");
			value = value.replace(")", "");
			valueStr.add(value);
		}

		for (int i = 0; (i + 1) < valueStr.size(); i++) {
			size1 = BigDecimalUtil.round(
					StringUtils.convertToDouble(valueStr.get(i)) / 1024 / 1024,
					BigDecimal.ROUND_CEILING);
			size2 = BigDecimalUtil
					.round(StringUtils.convertToDouble(valueStr.get(i + 1)) / 1024 / 1024,
							BigDecimal.ROUND_CEILING);
		}
		// try {
		// response.setCharacterEncoding("UTF-8");
		// response.setContentType("application/json; charset=utf-8");
		// response.getWriter().write("您上传文件的总大小为："+size1+" M ,超过了"+size2+" M !");
		// response.getWriter().flush();
		// response.getWriter().close();
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// }
		errorMap.put("success", false);
		errorMap.put("error", true);
		errorMap.put("msg", "您上传文件的总大小为：" + size1 + " M ,超过了" + size2 + " M !");
		return errorMap;
	}
	
	
	
	/**
	 * 无权限访问
	 * @return
	 */
	@ExceptionHandler({UnauthorizedException.class})
	public String unauthorizedException(HttpServletResponse response) { 
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			try {
				response.setCharacterEncoding("UTF-8"); 
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write("<div class='unauth'><span>您没有权限进行此操作！</span></div>");
				response.getWriter().flush();    
				response.getWriter().close();
			} catch (IOException e) {    
				e.printStackTrace();
			}
			return null;
		} else {
			return "error/unauth";
		}
	}

}
