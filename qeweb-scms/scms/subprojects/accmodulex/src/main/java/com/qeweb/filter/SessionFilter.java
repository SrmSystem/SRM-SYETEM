package com.qeweb.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;

import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

public class SessionFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		// 登陆url
		String loginUrl = httpRequest.getContextPath() + "/public/login";
		String path = httpRequest.getRequestURI();

		
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		// 登陆页面无需过滤
	    if(path.indexOf("/login") > -1) {
		    chain.doFilter(request, response);
	        return;
		 }
		
		
		// 超时处理，ajax请求超时设置超时状态，页面请求超时则返回提示并重定向
		if (user == null) {
		    
			// 判断是否为ajax请求
			if (httpRequest.getHeader("x-requested-with") != null  && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				httpResponse.addHeader("sessionstatus", "timeOut");
				httpResponse.addHeader("loginPath", loginUrl);
				chain.doFilter(request, response);// 不可少，否则请求会出错
			} else {
				String str = "<script language='javascript'>alert('会话过期,请重新登录');"
				+ "window.top.location.href='"
				+ loginUrl
				+ "';</script>";
				response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
				try {
					PrintWriter writer = response.getWriter();
					writer.write(str);
					writer.flush();
					writer.close();
				} catch (Exception e) {
				    e.printStackTrace();
				}
		     }
		} else {
		     chain.doFilter(request, response);
		}
	}
		
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		}

	

}
